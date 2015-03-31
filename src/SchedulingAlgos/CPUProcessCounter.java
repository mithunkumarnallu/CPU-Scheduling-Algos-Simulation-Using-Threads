/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SchedulingAlgos;

/**
 *
 * @author MithunKumar
 */
public class CPUProcessCounter {
    private int count = 0;
    
    public synchronized int getCount()
    {
        return this.count;
    }
    
    public synchronized int decrementCount()
    {
        this.count--;
        return this.count;
    }
    
    
    
    public CPUProcessCounter(int count)
    {
        this.count = count;
    }
}
