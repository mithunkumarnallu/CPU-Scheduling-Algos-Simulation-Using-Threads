/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SchedulingAlgos;

import Comparators.*;
import Comparators.SJFQueueComparator;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 *
 * @author MithunKumar
 */
public class Main {

    //   public static List<ProcessInfo> procList=new ArrayList<ProcessInfo>();
    public static List<List<ProcessInfo>> procListList = new ArrayList<List<ProcessInfo>>();
    //  public static List<List<ProcessInfo>> procList1=new ArrayList<>()
    //public static List<Thread> threadList=new ArrayList<Thread>();
    public static int noProc = 12, csTime = 4, noCPUs = 4, rrTimeSlice = 80;
    public static CPUProcessCounter cpuCounter;

    public static void main(String[] ar) {
        //would be extracted from the parameters

        cpuCounter = new CPUProcessCounter(6);
        Random cpuRandom = new Random(2800);
        Random ioRandom = new Random(180);

        int noIOProc = (int) 0.8 * noProc;
        int noCPUProc = noProc - noIOProc;
        int k = 0;
        for (int i = 0; i < 4; i++) {
            List<ProcessInfo> list = new ArrayList<ProcessInfo>();
            procListList.add(list);
        }

        for (int i = 0; i < noCPUProc; i++) {
            int cpRandTime = cpuRandom.nextInt(2801);
            for (int j = 0; j < 4; j++) {
                List<ProcessInfo> procList = procListList.get(j);
                procList.add(new ProcessInfo(i, 0, cpRandTime + 200, 1, 6));

            }
            k++;

        }
        for (int i = 0; i < noIOProc; i++) {
            int ioRandTime = ioRandom.nextInt(181);
            for (int j = 0; j < 4; j++) {
                List<ProcessInfo> procList = procListList.get(j);
                procList.add(new ProcessInfo(k, 0, ioRandTime + 20, 0, 6));

            }
            k++;

        }

        fcfsExec();
        sjfNonPreExec();
        sjfPreExec();
        rrExec();
    }

    public static void fcfsExec() {
        cpuCounter = new CPUProcessCounter(6);
        System.out.println("FCFS EXECUTION");
        //PriorityBlockingQueue<ProcessInfo> processess = new PriorityBlockingQueue<>(noProc, new);
        PriorityBlockingQueue<ProcessInfo> processess = new PriorityBlockingQueue<>(noProc, new FCFSQueueComparator());
        // PriorityBlockingQueue<ProcessInfo> processess = new PriorityBlockingQueue<>(noProc, new SJFQueueComparator());
        List<Thread> threadList = new ArrayList<Thread>();
        processess.addAll(procListList.get(0));
        ScheduledThreadPoolExecutor processReEntryTimer = new ScheduledThreadPoolExecutor(1);
        List<SJFNoPreemptionRunner_New> srList = new ArrayList<SJFNoPreemptionRunner_New>();
        for (int i = 0; i < noCPUs; i++) {
            SJFNoPreemptionRunner_New sjf = new SJFNoPreemptionRunner_New(processess, cpuCounter, i + 1, processReEntryTimer, csTime);
            Thread t1 = new Thread(sjf);
            threadList.add(t1);
            srList.add(sjf);

        }
        // threadExecution(threadList, processReEntryTimer);
        ProcessStats.procStats(procListList.get(0));
        ProcessStats.cpuUtilStatsFCFSSJF(srList, procListList.get(0));

    }

    public static void sjfNonPreExec() {
        cpuCounter = new CPUProcessCounter(6);
        //PriorityBlockingQueue<ProcessInfo> processess = new PriorityBlockingQueue<>(noProc, new);
        System.out.println("SJF Non Premeptive EXECUTION");
        PriorityBlockingQueue<ProcessInfo> processess = new PriorityBlockingQueue<>(noProc, new SJFQueueComparator());
        // PriorityBlockingQueue<ProcessInfo> processess = new PriorityBlockingQueue<>(noProc, new SJFQueueComparator());
        List<Thread> threadList = new ArrayList<Thread>();
        processess.addAll(procListList.get(1));
        //    System.out.println("SJF Non Premeptive EXECUTION"+procList.size());
        ScheduledThreadPoolExecutor processReEntryTimer = new ScheduledThreadPoolExecutor(1);
        List<SJFNoPreemptionRunner_New> srList = new ArrayList<SJFNoPreemptionRunner_New>();
        for (int i = 0; i < noCPUs; i++) {
            SJFNoPreemptionRunner_New sjf = new SJFNoPreemptionRunner_New(processess, cpuCounter, i + 1, processReEntryTimer, csTime);
            Thread t1 = new Thread(sjf);
            threadList.add(t1);
            srList.add(sjf);
        }
        // threadExecution(threadList, processReEntryTimer);
        ProcessStats.procStats(procListList.get(1));
        ProcessStats.cpuUtilStatsFCFSSJF(srList, procListList.get(1));

    }

    public static void sjfPreExec() {
        cpuCounter = new CPUProcessCounter(6);
        //PriorityBlockingQueue<ProcessInfo> processess = new PriorityBlockingQueue<>(noProc, new);
        System.out.println("SJF Prememptive EXECUTION");
        PriorityBlockingQueue<ProcessInfo> processess = new PriorityBlockingQueue<>(noProc, new SJFPreemptionQueueComparator());
        // PriorityBlockingQueue<ProcessInfo> processess = new PriorityBlockingQueue<>(noProc, new SJFQueueComparator());
        List<Thread> threadList = new ArrayList<Thread>();
        processess.addAll(procListList.get(2));
        ScheduledThreadPoolExecutor processReEntryTimer = new ScheduledThreadPoolExecutor(1);
        List<SJFPreemptionRunner_New> srList = new ArrayList<SJFPreemptionRunner_New>();
        for (int i = 0; i < noCPUs; i++) {
            SJFPreemptionRunner_New sjf = new SJFPreemptionRunner_New(processess, cpuCounter, i + 1, processReEntryTimer, csTime);
            Thread t1 = new Thread(sjf);
            threadList.add(t1);
            srList.add(sjf);
        }
         //threadExecution(threadList, processReEntryTimer);

        ProcessStats.procStats(procListList.get(2));
        ProcessStats.cpuUtilStatsSJFPre(srList, procListList.get(2));
    }

    public static void rrExec() {
        cpuCounter = new CPUProcessCounter(6);
        //PriorityBlockingQueue<ProcessInfo> processess = new PriorityBlockingQueue<>(noProc, new);
        System.out.println("Round Robin EXECUTION");
        PriorityBlockingQueue<ProcessInfo> processess = new PriorityBlockingQueue<>(noProc, new RoundRobinQueueComparator());
        // PriorityBlockingQueue<ProcessInfo> processess = new PriorityBlockingQueue<>(noProc, new SJFQueueComparator());
        List<Thread> threadList = new ArrayList<Thread>();
        processess.addAll(procListList.get(3));
        ScheduledThreadPoolExecutor processReEntryTimer = new ScheduledThreadPoolExecutor(1);
        List<RoundRobinRunner_New> rrList = new ArrayList<RoundRobinRunner_New>();
        for (int i = 0; i < noCPUs; i++) {
            RoundRobinRunner_New rr = new RoundRobinRunner_New(processess, cpuCounter, i + 1, rrTimeSlice, processReEntryTimer, csTime);
            Thread t1 = new Thread(rr);
            threadList.add(t1);
            rrList.add(rr);

        }
        // threadExecution(threadList, processReEntryTimer);
        ProcessStats.procStats(procListList.get(3));
        ProcessStats.cpuUtilStatsRR(rrList, procListList.get(3));

    }

}
