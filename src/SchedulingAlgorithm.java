// Run() is called from src.Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.
package src;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class SchedulingAlgorithm {
  PrintStream out;
  private List<sProcess> processes;

  public SchedulingAlgorithm(List<sProcess> processes) {
    this.processes = processes;
    String resultsFile = "Results\\Summary-Processes";
    try {
      out = new PrintStream(new FileOutputStream(resultsFile));
    } catch (FileNotFoundException e) {
      System.out.println("It is impossible to open such a file to write summary of processes work");
      throw new RuntimeException(e);
    }
  }

  void printProcess(int number_of_process, String state) {
    sProcess process = processes.get(number_of_process);
    String processString = String.format("Process %d: %s (%s)", number_of_process, state, process.toString());
    out.println(processString);
  }

  public Results run(int runtime, Results result) {
    int i = 0;
    int comptime = 0;
    int currentProcess = 0;
    int previousProcess = 0;
    int size = processes.size();
    int completed = 0;

    result.schedulingType = "Batch (Nonpreemptive)";
    result.schedulingName = "First-Come First-Served";


    printProcess(currentProcess, "registered");
    sProcess process = processes.get(currentProcess);
    while (comptime < runtime) {
      if (process.cpudone == process.cputime) {
        completed++;
        printProcess(currentProcess, "completed");
        if (completed == size) {
          result.compuTime = comptime;
          out.close();
          return result;
        }
        for (i = size - 1; i >= 0; i--) {
          process = processes.get(i);
          if (process.cpudone < process.cputime) {
            currentProcess = i;
          }
        }
        process = processes.get(currentProcess);
        printProcess(currentProcess, "registered");
      }
      if (process.ioblocking == process.ionext) {
        printProcess(currentProcess, "I/O blocked");
        process.numblocked++;
        process.ionext = 0;
        previousProcess = currentProcess;
        for (i = size - 1; i >= 0; i--) {
          process = processes.get(i);
          if (process.cpudone < process.cputime && previousProcess != i) {
            currentProcess = i;
          }
        }
        process = processes.get(currentProcess);
        printProcess(currentProcess, "registered");
      }
      process.cpudone++;
      if (process.ioblocking > 0) {
          process.ionext++;
      }
      comptime++;
    }
    result.compuTime = comptime;
    return result;
  }
}