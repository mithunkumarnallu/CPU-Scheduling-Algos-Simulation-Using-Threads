/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SchedulingAlgos;

import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MithunKumar
 */
public class SJFPreemptionRunner_New implements Runnable {
   private PriorityBlockingQueue<ProcessInfo> processes = null;
   CPUProcessCounter counter = null;
   private int threadNo = -1;
   private ScheduledThreadPoolExecutor processReEntryExecutor = null;
   int csTime;
   int totCSTime=0;
   public double totTime=0;
   private long startTime = 0 ;
   
    public SJFPreemptionRunner_New(PriorityBlockingQueue<ProcessInfo> queue, CPUProcessCounter counter, int threadNo, ScheduledThreadPoolExecutor processReEntryExecutor,int csTime)
    {
       this.processes = queue;
       this.counter = counter;
       this.threadNo = threadNo;
       this.processReEntryExecutor = processReEntryExecutor;
       this.csTime=csTime;
    }

    @Override
    public void run() {
        long timeCounter = 0;
        int randomTime = 0;
        int prevProcID=-1;
        Random rand = new Random(3500);
        startTime = System.currentTimeMillis();
        while(counter.getCount() > 0)
        {        
            while(!processes.isEmpty() && counter.getCount() != 0)
            {
                ProcessInfo p = processes.poll();
                if(p != null)
                {
                    if(prevProcID!=-1)
                    {
                        System.out.println("[time "+timeCounter +"ms in thread no : "+threadNo+"] Context switch (swapping out process ID " +prevProcID +" for process ID "+ p.pid + ")");
                    }
                    timeCounter = System.currentTimeMillis() - startTime; 
                    p.totalTimeSpent+=(timeCounter - p.getLastPickedTime());
                    p.waitTime = p.getWaitTime() + (int)(timeCounter - p.getLastPickedTime());
                    //p.totBurstTime+=p.waitTime;
                    for(int i= p.getBurstRem(); i>0; i--)
                    {
                        //timeCounter++;
                        try {
                            Thread.sleep(1);
                            p.totBurstTime++;
                            p.totalTimeSpent++;
                        } catch (InterruptedException ex) {
                            Logger.getLogger(SJFPreemptionRunner_New.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        timeCounter = System.currentTimeMillis() - startTime;     
                    
                        p.burstRem --;
                        if(!processes.isEmpty() && processes.peek() != null &&  processes.peek().burstRem < p.burstRem)
                        {
                            //Found a process with smaller burst than p in process queue
                            //Calculate turnaround and other fields of p before pushing it into process queue
                            p.turnAroundTime = p.getTurnAroundTime() + (int)(timeCounter - p.getLastPickedTime());
                            //randomTime = rand.nextInt(3501) + 1000;
                            p.lastPickedTime = (int)(timeCounter + csTime);
                            //p.arrTime = (int)(timeCounter + randomTime);
                            processes.add(p);                            
                            break;
                        }
                    }
                    if(p.burstRem == 0)
                    {
                        //Process terminated genuinely and now blocking on IO
                        timeCounter = System.currentTimeMillis() - startTime;
                        p.turnAroundTime = p.getTurnAroundTime() + (int)(timeCounter - p.getLastPickedTime());                
                        randomTime = rand.nextInt(3501) + 1000;
                        p.lastPickedTime = (int)(timeCounter + randomTime);
                        p.arrTime = (int)(timeCounter + randomTime);
                        if(p.processType == 1)
                            p.burst--;

                        if(p.getBurst() == 0)
                        {
                            if(p.getProcessType() == 1)
                                counter.decrementCount();
                            //Show a process terminated message
                            System.out.format("[time %dms in thread no : %d] %s process ID %d terminated (turnaround time %dms Wait Time %dms)\n", timeCounter, threadNo, p.getProcessType() == 0 ? "Interactive" : "CPU", 
                                    p.pid, p.getTurnAroundTime(), p.getWaitTime());
                        }
                        else
                        {                
                            //Show a burst done message
                            System.out.format("[time %dms in thread no : %d] %s process ID %d with CPU burst of %dms done (turnaround time %dms Wait Time %dms)\n",  timeCounter, threadNo, p.getProcessType() == 0 ? "Interactive" : "CPU", 
                                    p.pid, p.burstTime, p.getTurnAroundTime(), p.getWaitTime());
                            p.ioWaiTime = randomTime;
                            processReEntryExecutor.schedule(new ProcessReEntryTask(processes, p, (int)(timeCounter + randomTime)), (int)(1 * randomTime), TimeUnit.MILLISECONDS);
                        }                
                    }
                    prevProcID = p.pid;
                    try {
                        p.totalTimeSpent+=csTime;
                        timeCounter += csTime;
                        Thread.sleep(csTime);
                        timeCounter = System.currentTimeMillis() - startTime;
                        totCSTime+=csTime;
                        totTime=timeCounter;
                    } catch (InterruptedException ex) {
                        Logger.getLogger(FCFSRunner.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    if(counter.getCount() <= 0)
                        break;
                }
            }
        }
    }
}
