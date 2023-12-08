// Run() is called from src.Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.
package src;
import src.Results;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.io.*;

public class SchedulingAlgorithm {
  static void printProcess(PrintStream out,int number,String state,sProcess process){
    String processString = String.format("Process %d: %s (%s)",number,state,process.toString());
    out.println(processString);
  }

  public static Results Run(int runtime, List<sProcess> processVector, Results result) {
    int i = 0;
    int comptime = 0;
    int currentProcess = 0;
    int previousProcess = 0;
    int size = processVector.size();
    int completed = 0;
    String resultsFile = "Results\\Summary-Processes";

    result.schedulingType = "Batch (Nonpreemptive)";
    result.schedulingName = "First-Come First-Served"; 
    try {

      PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
      sProcess process = (sProcess) processVector.elementAt(currentProcess);
      printProcess(out,currentProcess,"registered", process);
      while (comptime < runtime) {
        if (process.cpudone == process.cputime) {
          completed++;
          printProcess(out,currentProcess, "completed",process);
          if (completed == size) {
            result.compuTime = comptime;
            out.close();
            return result;
          }
          for (i = size - 1; i >= 0; i--) {
            process = processVector.elementAt(i);
            if (process.cpudone < process.cputime) { 
              currentProcess = i;
            }
          }
          process = processVector.elementAt(currentProcess);
          printProcess(out,currentProcess,"I/O blocked",process);
          }
        if (process.ioblocking == process.ionext) {
          printProcess(out,currentProcess,"I/O blocked",process);
          process.numblocked++;
          process.ionext = 0; 
          previousProcess = currentProcess;
          for (i = size - 1; i >= 0; i--) {
            process = processVector.elementAt(i);
            if (process.cpudone < process.cputime && previousProcess != i) { 
              currentProcess = i;
            }
          }
          process = processVector.get(currentProcess);
          printProcess(out,currentProcess,"registered",process);
        }
        process.cpudone++;       
        if (process.ioblocking > 0) {
          process.ionext++;
        }
        comptime++;
      }
      out.close();
    } catch (FileNotFoundException e) {
      System.out.println("There is no such a file to write summary for processes");
    }
    result.compuTime = comptime;
    return result;
  }
}
