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
public class RoundRobinRunner_New implements Runnable {
    
    private PriorityBlockingQueue<ProcessInfo> processes = null;
    CPUProcessCounter counter = null;
    private int threadNo = -1;
    private ScheduledThreadPoolExecutor processReEntryExecutor;
    private int timeSlice=0;
    private int csTime = 0;
    public int totCSTime=0;
    public double timeCt;
    public double totalTime=0;
    private long startTime = 0 ;
    public RoundRobinRunner_New(PriorityBlockingQueue<ProcessInfo> queue, CPUProcessCounter counter, int threadNo, int timeSlice, ScheduledThreadPoolExecutor processReEntryExecutor, int csTime)
    {
        this.processes = queue;
        this.counter = counter;
        this.threadNo = threadNo;
        this.processReEntryExecutor = processReEntryExecutor;
        this.timeSlice = timeSlice;
        this.csTime = csTime;
    }
    
    
    @Override
    public void run() {
        ProcessInfo p=null;
        long timeCounter = 0;
        int sliceTime=0, prevProcID = -1;
        int randomTime=0;
        Random rand = new Random(3500);
        startTime = System.currentTimeMillis();
        
        while(counter.getCount() > 0)
        {
            while(!processes.isEmpty() && counter.getCount() > 0)
            {
                p = processes.poll();
                if(p != null)
                {
                    if(prevProcID!=-1)
                    {
                        System.out.println("[time "+timeCounter +"ms in thread no : "+threadNo+"] Context switch (swapping out process ID " +prevProcID +" for process ID "+ p.pid + ")");
                    }
                    
                    //Reduce the burst time by round robin time slice OR by remaining burst time period in the current burst, whichever is earlier.
                    sliceTime = (p.burstRem-timeSlice) <= 0 ? p.burstRem : timeSlice;
                    p.totBurstTime+=sliceTime;
                    //p.totalTimeSpent += sliceTime;
                    
                    //if(p.getTurnAroundTime()> 0)
                        p.waitTime = p.getWaitTime() + (int)(timeCounter - p.getLastPickedTime());
                    //p.sliceTime= p.getWaitTime() + (int)(timeCounter - p.getLastPickedTime());
            
                    try {
                        Thread.sleep(sliceTime);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(RoundRobinRunner_New.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    p.burstRem-=sliceTime;
                    
                    if(p.burstRem <= 0)
                    {                        
                        if(p.processType == 1)
                            p.burst--;
                        
                        timeCounter = System.currentTimeMillis() - startTime;
                        p.totalTimeSpent += (timeCounter - p.getLastPickedTime());
                        //timeCounter+=sliceTime;
                        p.turnAroundTime = p.getTurnAroundTime() + (int)(timeCounter - p.getLastPickedTime());
                        randomTime = rand.nextInt(3501) + 1000;
                        p.lastPickedTime = (int)(timeCounter + randomTime);
                        //p.totalTimeSpent += (timeCounter - p.getLastPickedTime());
                    
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
                            
                            processReEntryExecutor.schedule(new ProcessReEntryTask(processes, p, (int)(timeCounter + randomTime)), randomTime, TimeUnit.MILLISECONDS);
                        }                
                    }
                    else
                    {
                        timeCounter = System.currentTimeMillis() - startTime;
                        p.totalTimeSpent += (timeCounter - p.getLastPickedTime());
                        //timeCounter+=sliceTime;
                        p.turnAroundTime = p.getTurnAroundTime() + (int)(timeCounter - p.getLastPickedTime());
                        p.lastPickedTime = (int)(timeCounter + csTime);                        
                        p.totalTimeSpent += (timeCounter - p.getLastPickedTime());
                        //p.totalTimeSpent += (timeCounter - p.getLastPickedTime());
                                        
                        //Put the process back in ready Queue, and continue with another process. Handle the IO thing here
                        //Show a burst done message
                        System.out.format("[time %dms in thread no : %d] %s process ID %d completed it's time slice of  %dms\n",  timeCounter, threadNo, p.getProcessType() == 0 ? "Interactive" : "CPU", 
                                p.pid, sliceTime);
                        
                        processes.add(p);
                    }
                    prevProcID = p.pid;
                   
                    try {
                        Thread.sleep(csTime);
                        timeCounter = System.currentTimeMillis() - startTime;
                        //timeCounter += csTime;
                        totCSTime+=csTime;
                        p.totalTimeSpent+=csTime;
                        totalTime=timeCounter;
                    } catch (InterruptedException ex) {
                        Logger.getLogger(RoundRobinRunner_New.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }                
            }
        }           
    }
}
    

