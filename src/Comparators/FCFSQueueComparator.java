/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Comparators;


import java.util.Comparator;
import SchedulingAlgos.ProcessInfo;


public class FCFSQueueComparator  implements Comparator<ProcessInfo>{
    
     @Override
    public int compare(ProcessInfo o1, ProcessInfo o2) {
        return o1.getArrTime().compareTo(o2.getArrTime());
    }
    
}
