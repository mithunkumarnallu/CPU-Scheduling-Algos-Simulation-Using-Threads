/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SchedulingAlgos;

/*import static os_hw2.Main1.noCPUs;
 import static os_hw2.Main1.noProc;
 import static os_hw2.Main1.rrTimeSlice;*/
/**
 *
 * @author MithunKumar
 */
public class CommandLine {

    public static void main(String[] ar)//1:no of processes,2-no of processors,3:tslice
    {
        //would be extracted from the parameters
        int argCnt = 0;
        for (String s : ar) {

            if (argCnt == 0) {
                if (!s.equals("-")) {
                    Integer i = Integer.parseInt(s);
                    System.out.println(i);;
                }

            }

            if (argCnt == 1) {
                if (!s.equals("-")) {
                    Integer i = Integer.parseInt(s);
                    System.out.println(i);;
                }

            }

            if (argCnt == 2) {
                if (!s.equals("-")) {
                    Integer i = Integer.parseInt(s);
                    System.out.println(i);;
                }

            }

        }
    }
}
