/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SchedulingAlgos;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MithunKumar
 */
public class FCFSRunner implements Runnable {

    private PriorityBlockingQueue<ProcessInfo> processes = null;
    CPUProcessCounter counter = null;
    private int threadNo = -1;
    private int csTime;

    public FCFSRunner(PriorityBlockingQueue<ProcessInfo> queue, CPUProcessCounter counter, int threadNo, int csTime) {
        this.processes = queue;
        this.counter = counter;
        this.threadNo = threadNo;
        this.csTime = csTime;
    }

    @Override
    public void run() {
        int timeCounter = 0;
        while (!processes.isEmpty() && counter.getCount() != 0) {
            ProcessInfo p = processes.poll();
            if (p == null) {
                return;
            }
            //System.out.format("[time %dms] %s process ID %d entered ready queue (requires %dms CPU time\n",timeCounter, p.getProcessType() == 0 ? "Interactive" : "CPU",
            //            p.pid, p.getBurstTime());

            p.burst--;
            timeCounter += p.getBurstTime();

            try {
                Thread.sleep(p.getBurstTime());
            } catch (InterruptedException ex) {
                Logger.getLogger(SJFNoPreemptionRunner_New.class.getName()).log(Level.SEVERE, null, ex);
            }

            p.waitTime = p.getWaitTime() + (timeCounter - p.getLastPickedTime());
            p.turnAroundTime = p.getTurnAroundTime() + (timeCounter - p.getLastPickedTime());
            p.arrTime = timeCounter + 4500;
            p.lastPickedTime = timeCounter;

            if (p.getBurst() == 0) {
                if (p.getProcessType() == 1) {
                    counter.decrementCount();
                }
                //Show a process terminated message
                System.out.format("[time %dms in thread no : %d] %s process ID %d terminated (turnaround time %dms Wait Time %dms)\n", timeCounter, threadNo, p.getProcessType() == 0 ? "Interactive" : "CPU",
                        p.pid, p.getTurnAroundTime(), p.getWaitTime());
            } else {
                //Show a burst done message
                System.out.format("[time %dms in thread no : %d] %s process ID %d with CPU burst of %dms done (turnaround time %dms Wait Time %dms)\n", timeCounter, threadNo, p.getProcessType() == 0 ? "Interactive" : "CPU",
                        p.pid, p.burstTime, p.getTurnAroundTime(), p.getWaitTime());

                processes.add(p);
            }
            try {
                Thread.sleep(csTime);
            } catch (InterruptedException ex) {
                Logger.getLogger(FCFSRunner.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (counter.getCount() <= 0) {
                break;
            }

        }
    }

}
