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
  private final List<Process> processes;
  private final List<Process>  tickets = new ArrayList<>();
  public static int quantumTime = 50;

  public SchedulingAlgorithm(List<Process> processes) {

    String resultsFile = "Results\\Summary-Processes";
    try {
      out = new PrintStream(new FileOutputStream(resultsFile));
    } catch (FileNotFoundException e) {
      System.out.println("It is impossible to open such a file to write summary of processes work");
      throw new RuntimeException(e);
    }
    this.processes = processes;
    for(Process process:processes){
      for(int i=0;i<process.weight;i++){
        tickets.add(process);
      }
    }
  }
  public Process selectProcess(){
    int i = random.nextInt(tickets.size());
    Process selectedProcess = tickets.get(i);
    printProcess(selectedProcess,"registered");
    return selectedProcess;
  }
  void printProcess(Process process, String state) {
    String processString = String.format("Process %d: %s (%s)", process.indexOfProcess, state, process);
    out.println(processString);
  }

  public Results run(int runtime, Results result) {
    result.schedulingType = "Preemptive";
    result.schedulingName = "Lottery";
    int comptime = 0;
    int completed = 0;

    while(comptime<=runtime){
      Process process = selectProcess();
      if(process.isIOBlocked()){
        process.numblocked++;
        process.ionext=0;
        printProcess(process,"I/O blocked");
        comptime+=10;
        continue;
      }
      if(process.timeToBlocking()<=quantumTime){
        if(process.timeToComplete()<=process.timeToBlocking()){
          comptime+=process.timeToComplete();
          process.cpudone+=process.timeToComplete();
          process.ionext+=process.timeToComplete();
        }
        else if(process.timeToComplete()>process.timeToBlocking()){
          comptime+=process.timeToBlocking();
          process.cpudone+=process.timeToBlocking();
          process.ionext+=process.timeToBlocking();
        }
      }
      else if (process.timeToComplete()<=quantumTime && process.timeToBlocking()>quantumTime){
        comptime+=process.timeToComplete();
        process.cpudone+=process.timeToComplete();
        process.ionext+=process.timeToComplete();
      }
      else{
        process.cpudone+=quantumTime;
        process.ionext+=quantumTime;
        comptime+=quantumTime;
      }
      if(process.isCompleted()){
        completed++;
        printProcess(process,"completed");
        if(completed==processes.size()){
          result.computationTime = comptime;
          return result;
        }
          tickets.removeIf(ticket_process->ticket_process.indexOfProcess== process.indexOfProcess);
      }
    }
    result.computationTime = comptime;
    return result;
  }
}