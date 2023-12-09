// Run() is called from src.Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.
package src;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.util.Random;
public class SchedulingAlgorithm {
  private final static Random random = new Random();
  PrintStream out;
  private List<sProcess> processes;
  private List<sProcess> tickets = new ArrayList<>();
  private int currentProcessIndex=0;
  public static int quantumTime = 20;

  public SchedulingAlgorithm(List<sProcess> processes) {

    String resultsFile = "Results\\Summary-Processes";
    try {
      out = new PrintStream(new FileOutputStream(resultsFile));
    } catch (FileNotFoundException e) {
      System.out.println("It is impossible to open such a file to write summary of processes work");
      throw new RuntimeException(e);
    }
    this.processes = processes;
    for(sProcess process:processes){
      for(int i=0;i<process.weight;i++){
        tickets.add(process);
      }
    }
  }
  public sProcess selectProcess(){
    int i = random.nextInt(tickets.size());
    sProcess selectedProcess = tickets.get(i);
    printProcess(selectedProcess,"registered");
    return selectedProcess;
  }
  void printProcess(sProcess process, String state) {
    String processString = String.format("Process %d: %s (%s)", process.indexOfProcess, state, process.toString());
    out.println(processString);
  }

  public Results run(int runtime, Results result) {


    result.schedulingType = "Preemptive";
    result.schedulingName = "Lottery";
    int comptime = 0;
    int completed = 0;

    while(comptime<=runtime){
      sProcess process = selectProcess();
      process.cpudone+=quantumTime;
      process.ionext+=quantumTime;
      if(process.isCompleted()){
        completed++;
        printProcess(process,"completed");
        if(completed== processes.size()){
          result.compuTime=comptime;
          return result;
        }
        tickets.removeIf(ticket_process->ticket_process.indexOfProcess == process.indexOfProcess);
      }
      if(process.isIOBlocked()){
        printProcess(process,"I/O blocked");
          process.numblocked++;
          process.ionext=0;
      }
      comptime+=quantumTime;
    }
    result.compuTime = comptime;
    return result;
  }
}