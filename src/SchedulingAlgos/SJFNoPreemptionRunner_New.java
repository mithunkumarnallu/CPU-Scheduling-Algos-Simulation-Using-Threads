/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SchedulingAlgos;

import java.util.Random;
import java.util.Timer;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MithunKumar
 */
public class SJFNoPreemptionRunner_New implements Runnable {

    private PriorityBlockingQueue<ProcessInfo> processes = null;
    CPUProcessCounter counter = null;
    private int threadNo = -1;
    private ScheduledThreadPoolExecutor processReEntryExecutor = null;
    private int csTime;
    int totCSTime = 0;
    public double totTime = 0;
    private long startTime = 0;

    public SJFNoPreemptionRunner_New(PriorityBlockingQueue<ProcessInfo> queue, CPUProcessCounter counter, int threadNo, ScheduledThreadPoolExecutor processReEntryExecutor, int csTime) {
        this.processes = queue;
        this.counter = counter;
        this.threadNo = threadNo;
        this.processReEntryExecutor = processReEntryExecutor;
        this.csTime = csTime;
    }

    @Override
    public void run() {
        long timeCounter = 0;
        int randomTime = 0;
        int prevProcID = -1;
        Random rand = new Random(3500);
        startTime = System.currentTimeMillis();
        while (counter.getCount() > 0) {
            while (!processes.isEmpty() && counter.getCount() != 0) {

                ProcessInfo p = processes.poll();
                if (p != null) {
                    if (prevProcID != -1) {
                        System.out.println("[time " + timeCounter + "ms in thread no : " + threadNo + "] Context switch (swapping out process ID " + prevProcID + " for process ID " + p.pid + ")");
                    }
                    if (p.getProcessType() == 1) {
                        p.burst--;
                    }
                    timeCounter = System.currentTimeMillis() - startTime;
                    int tm = (int) (timeCounter - p.getLastPickedTime());
                    p.waitTime = p.getWaitTime() + (int) (timeCounter - p.getLastPickedTime());
                    p.totBurstTime += p.getBurstTime();
                    //timeCounter += p.getBurstTime();
                    p.totalTimeSpent += p.getBurstTime() + tm;
                    // System.out.println("TIME left"+p.totalTimeSpent);
                    try {
                        Thread.sleep(p.getBurstTime());
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SJFNoPreemptionRunner_New.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    timeCounter = System.currentTimeMillis() - startTime;
                    p.turnAroundTime = p.getTurnAroundTime() + (int) (timeCounter - p.getLastPickedTime());
                    randomTime = rand.nextInt(3501) + 1000;
                    p.arrTime = (int) (timeCounter + randomTime);
                    p.lastPickedTime = (int) (timeCounter + randomTime);

                    if (p.getBurst() == 0) {
                        if (p.getProcessType() == 1) {
                            counter.decrementCount();
                            //System.out.format("Process id %d terminated. Processess avaibale are %d\n", p.pid, counter.getCount());
                            //Show a process terminated message
                            System.out.format("[time %dms in thread no : %d] %s process ID %d terminated (turnaround time %dms Wait Time %dms)\n", timeCounter, threadNo, p.getProcessType() == 0 ? "Interactive" : "CPU",
                                    p.pid, p.getTurnAroundTime(), p.getWaitTime());
                        }
                    } else {
                        //Show a burst done message
                        System.out.format("[time %dms in thread no : %d] %s process ID %d with CPU burst of %dms done (turnaround time %dms Wait Time %dms)\n", timeCounter, threadNo, p.getProcessType() == 0 ? "Interactive" : "CPU",
                                p.pid, p.burstTime, p.getTurnAroundTime(), p.getWaitTime());
                        int time = (int) (timeCounter + randomTime);
                        p.ioWaiTime = randomTime;
                        Thread t = new Thread(new ProcessReEntryTask(processes, p, time));
                        t.setPriority(Thread.MIN_PRIORITY);
                        this.processReEntryExecutor.schedule(t, randomTime, TimeUnit.MILLISECONDS);
                     //   p.totalTimeSpent;

                    }
                    prevProcID = p.pid;
                    try {
                        Thread.sleep(csTime);
                        timeCounter = System.currentTimeMillis() - startTime;
                        totCSTime += csTime;
                        totTime = timeCounter;
                        p.totalTimeSpent += csTime;
                    } catch (InterruptedException ex) {
                        Logger.getLogger(FCFSRunner.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    if (counter.getCount() <= 0) {
                        break;
                    }
                }
            }
        }
    }
}
