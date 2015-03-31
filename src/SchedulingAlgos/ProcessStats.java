/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SchedulingAlgos;

import java.util.List;

/**
 *
 * @author MithunKumar
 */
public class ProcessStats {

    public static void procStats(List<ProcessInfo> procList) {
        int maxTT = procList.get(0).turnAroundTime;
        int minTT = procList.get(0).turnAroundTime;
        int avgTT = 0;
        int maxWT = procList.get(0).waitTime;
        int minWT = procList.get(0).waitTime;
        int avgWT = 0;
        int wtSum = 0, taSum = 0;

        for (ProcessInfo proc : procList) {
            if (proc.turnAroundTime > maxTT) {
                maxTT = proc.turnAroundTime;
            }
            if (proc.turnAroundTime < minTT) {
                minTT = proc.turnAroundTime;
            }
            if (proc.waitTime > maxWT) {
                maxWT = proc.waitTime;
            }
            if (proc.waitTime < minWT) {
                minWT = proc.waitTime;
            }
            wtSum += proc.waitTime;;

            taSum += proc.turnAroundTime;
        }
        avgWT = wtSum / procList.size();
        avgTT = taSum / procList.size();
        System.out.println("Turnaround time: min " + minTT + "ms; avg " + avgTT + "ms max " + maxTT + "ms");
        System.out.println("Total Wait time: min " + minWT + "ms; avg " + avgWT + "ms max " + maxWT + "ms");

    }

    public static void cpuUtilStatsFCFSSJF(List<SJFNoPreemptionRunner_New> srList, List<ProcessInfo> procList) {
        double utilSum = 0;
        for (SJFNoPreemptionRunner_New s : srList) {
            double util = (s.totCSTime) / s.totTime;
            utilSum += util;
        }
        double avg = utilSum / srList.size();
        System.out.println("Average CPU Utilization " + (1 - avg));

        double totalTime = 0;
        for (SJFNoPreemptionRunner_New s : srList) {
            totalTime += s.totTime;
        }
        for (ProcessInfo p : procList) {
            double util = p.totBurstTime / p.totalTimeSpent;
            System.out.println("Average CPU Utilization for Process " + p.pid + 1 + " is " + util + " and its burst is " + p.burstTime);
        }
    }

    public static void cpuUtilStatsSJFPre(List<SJFPreemptionRunner_New> srList, List<ProcessInfo> procList) {
        double utilSum = 0;
        for (SJFPreemptionRunner_New s : srList) {
            double util = (s.totCSTime) / s.totTime;
            utilSum += util;
        }
        double avg = utilSum / srList.size();
        System.out.println("Average CPU Utilization " + (1 - avg));

        double totalTime = 0;
        for (SJFPreemptionRunner_New s : srList) {
            totalTime += s.totTime;
        }
        for (ProcessInfo p : procList) {
            double util = p.totBurstTime / p.totalTimeSpent;
            System.out.println("Average CPU Utilization for Process " + p.pid + 1 + " is " + util + " and its burst is " + p.burstTime);
        }
    }

    public static void cpuUtilStatsRR(List<RoundRobinRunner_New> srList, List<ProcessInfo> procList) {
        double utilSum = 0;
        for (RoundRobinRunner_New s : srList) {
            double util = (s.totCSTime) / s.totalTime;
            utilSum += util;
        }
        double avg = utilSum / srList.size();
        System.out.println("Average CPU Utilization " + (1 - avg));

        double totalTime = 0;
        for (RoundRobinRunner_New s : srList) {
            totalTime += s.totalTime;
        }
        for (ProcessInfo p : procList) {
            double util = p.totBurstTime / p.totalTimeSpent;
            System.out.println("Average CPU Utilization for Process " + p.pid + " is" + util + " " + p.burstTime);
        }
    }
}
