/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SchedulingAlgos;

import java.util.TimerTask;
import java.util.concurrent.PriorityBlockingQueue;

/**
 *
 * @author MithunKumar
 */
public class ProcessReEntryTask implements Runnable {

    private ProcessInfo process = null;
    private PriorityBlockingQueue<ProcessInfo> queue = null;
    private int threadNo, curTime;

    public ProcessReEntryTask(PriorityBlockingQueue<ProcessInfo> queue, ProcessInfo process, int curTime) {
        this.process = process;
        this.queue = queue;
        //this.threadNo=threadNo;
        this.curTime = curTime;
    }

    @Override
    public void run() {

        process.burstRem = process.burstTime;
        if (process.processType == 1) {
            System.out.println("[time " + curTime + "ms] CPU bound process " + process.pid + " entered ready queue (requires " + process.burstRem + "ms CPU time)");
        } else {
            System.out.println("[time " + curTime + "ms] IO bound process " + process.pid + " entered ready queue (requires " + process.burstRem + "ms CPU time)");
        }

        this.queue.add(process);
    }
}
