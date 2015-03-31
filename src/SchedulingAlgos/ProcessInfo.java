/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SchedulingAlgos;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MithunKumar
 */
public class ProcessInfo {

    int pid;
    int burstTime;
    int arrTime;
        //1 - CPU process
    //0 - IO bound interactive process
    int processType;
    // Number of bursts.
    public int burst;
    int lastPickedTime;
    int turnAroundTime = 0;
    int waitTime = 0;
    int burstRem;
    int totBurstTime = 0;
    double totalTimeSpent = 0;
    int ioWaiTime = 0;

    //new attributes
    int queueInTime = 0;

    //Wait
    List<Integer> readyQueueWaitTimesPerBurst;
    int readyQueueWaitTimeForBurst;
    //Execution
    List<Integer> executionTimesPerBurst;
    int executionTimeForBurst;
    //IO
    List<Integer> IoTimePerBurst;
    int IOTimeForBurst;
    int ReadyQueueInsertTime;

    /**
     * @return the burstRem
     */
    public Integer getBurstRem() {
        return burstRem;
    }

    /**
     * @return the burstTime
     */
    public Integer getBurstTime() {
        return burstTime;
    }

    /**
     * @return the arrTime
     */
    public Integer getArrTime() {
        return arrTime;
    }

    /**
     * @return the processType
     */
    public Integer getProcessType() {
        return processType;
    }

    /**
     * @return the burst
     */
    public Integer getBurst() {
        return burst;
    }

    /**
     * @return the lastPickedTime
     */
    public Integer getLastPickedTime() {
        return lastPickedTime;
    }

    /**
     * @return the turnAroundTime
     */
    public Integer getTurnAroundTime() {
        return turnAroundTime;
    }

    /**
     * @return the waitTime
     */
    public Integer getWaitTime() {
        return waitTime;
    }

    public ProcessInfo() {
        // TODO Auto-generated constructor stub
    }

    public int DecrementProcessBurstTime(int decreaseAmount) {
        executionTimeForBurst += Math.min(burstRem - decreaseAmount, 0) == 0 ? burstRem : decreaseAmount;
        burstRem -= Math.min(burstRem - decreaseAmount, 0) == 0 ? burstRem : decreaseAmount;
        int returnValue = 1;

        if (burstRem == 0) {
            // One burst got over.
            executionTimesPerBurst.add(executionTimeForBurst);
            readyQueueWaitTimesPerBurst.add(readyQueueWaitTimeForBurst);
            IoTimePerBurst.add(IOTimeForBurst);

            //Print the stats!
            //Reset the variables
            executionTimeForBurst = 0;
            readyQueueWaitTimeForBurst = 0;
            IOTimeForBurst = 0;

            burst--;
            burstRem = burstTime;
            returnValue = 0; //Means a new burst will start
        }

        if (burst == 0) {
            returnValue = -1; //Means the process got over.
        }
        //Otherwise returnValue=1 means neither the process got over, nor the current burst got over.
        return returnValue;
    }

    public void WaitForIO(int waitTime) {
        IOTimeForBurst += waitTime;
    }

    public void UpdateReadyQueueInsertTime(int time) {
        ReadyQueueInsertTime = time;
    }

    public void UpdateReadyQueueWaitTime(int time) {
        readyQueueWaitTimeForBurst = time - ReadyQueueInsertTime;
    }

    public void ProcessEnd() {
        // Called when all the bursts of the CPU bound process are done. 
        // Use it to calculate aggregate results over all bursts, and print them if necessary.
    }

    public boolean IsCPUBoundProcess() {
        return processType == 1;
    }

    public ProcessInfo(int pid, int arrTime, int burstTime, int processType, int bursts) {
        this.pid = pid;
        this.burstTime = burstTime;
        this.arrTime = arrTime;

        this.processType = processType;
        this.lastPickedTime = arrTime;
        this.burst = bursts;
        this.burstRem = burstTime;

        // New things
        IoTimePerBurst = new ArrayList<>();
        readyQueueWaitTimesPerBurst = new ArrayList<>();
        executionTimesPerBurst = new ArrayList<>();
        readyQueueWaitTimeForBurst = 0;
        executionTimeForBurst = 0;
        IOTimeForBurst = 0;
        ReadyQueueInsertTime = 0;
    }

}
