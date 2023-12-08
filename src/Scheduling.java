// This file contains the main() function for the src.Scheduling
// simulation.  Init() initializes most of the variables by
// reading from a provided file.  src.SchedulingAlgorithm.Run() is
// called from main() to run the simulation.  Summary-src.Results
// is where the summary results are written, and Summary-Processes
// is where the process scheduling summary is written.

// Created by Alexander Reeder, 2001 January 06
package src;
import java.io.*;
import java.util.*;
import java.util.ArrayList;
public class Scheduling {

  private static int processNum = 5;
  private static int meanDev = 1000;
  private static int standardDev = 100;
  private static int runtime = 1000;
  private static final List<sProcess> processes = new ArrayList<>();
  private static Results result = new Results("null","null",0);
  private static final String resultsFile = "Results\\Summary-Results";

  private static void Init(String file) {
    File f = new File(file);
    String tmp;
    int cputime = 0;
    int ioblocking = 0;
    double X = 0.0;

    try {

      Scanner configuration_in = new Scanner(f);
      while(configuration_in.hasNextLine()){
        String line = configuration_in.nextLine();
        if (line.startsWith("numprocess")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          processNum = Common.configurationTokenToInteger(st.nextToken());
        }
        if (line.startsWith("meandev")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          meanDev = Common.configurationTokenToInteger(st.nextToken());
        }
        if (line.startsWith("standdev")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          standardDev = Common.configurationTokenToInteger(st.nextToken());
        }
        if (line.startsWith("process")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          ioblocking = Common.configurationTokenToInteger(st.nextToken());
          X = Common.R1();
          while (X == -1.0) {
            X = Common.R1();
          }
          X = X * standardDev;
          cputime = (int)X + meanDev;
          processes.add(new sProcess(cputime, ioblocking, 0, 0, 0));
        }
        if (line.startsWith("runtime")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          runtime = Common.configurationTokenToInteger(st.nextToken());
        }
      }
      configuration_in.close();
    } catch (FileNotFoundException e) {
        System.out.println("There is no such a file to read configuration from");
    }
    System.out.println("processnum " + processNum);
    System.out.println("meandev " + meanDev);
    System.out.println("standdev " + standardDev);
    for (int i = 0; i < processes.size(); i++) {
      sProcess process = processes.get(i);
      System.out.println("process " + i + " " + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.numblocked);
    }
    System.out.println("runtime " + runtime);
  }

  public static void main(String[] args) {
    int i = 0;
    File configuration_file = null;
    if(args.length==0)
      configuration_file = new File("scheduling.conf");
    else{
      configuration_file = new File(args[0]);
    }
    if (!configuration_file.exists()){
      System.out.println("Scheduling: error, configuration file '" + configuration_file.getName() + "' does not exist.");
      System.exit(-1);
    }  
    if (!configuration_file.canRead()) {
      System.out.println("Scheduling: error, read of " + configuration_file.getName() + " failed.");
      System.exit(-1);
    }
    System.out.println("Working...");
    Init(configuration_file.getName());
    if (processes.size() < processNum) {
      i = 0;
      while (processes.size() < processNum) {
          double X = Common.R1();
          while (X == -1.0) {
            X = Common.R1();
          }
          X = X * standardDev;
        int cputime = (int) X + meanDev;
        processes.add(new sProcess(cputime,i*100,0,0,0));
        i++;
      }
    }
    result = SchedulingAlgorithm.Run(runtime, processes, result);
    PrintStream out = null;
    try {
      //BufferedWriter out = new BufferedWriter(new FileWriter(resultsFile));
      out = new PrintStream(new FileOutputStream(resultsFile));
      out.println("Scheduling Type: " + result.schedulingType);
      out.println("Scheduling Name: " + result.schedulingName);
      out.println("Simulation Run Time: " + result.compuTime);
      out.println("Mean: " + meanDev);
      out.println("Standard Deviation: " + standardDev);
      out.println("src.Process #\tCPU Time\tIO Blocking\tCPU Completed\tCPU Blocked");
      for (i = 0; i < processes.size(); i++) {
        sProcess process = processes.elementAt(i);
        out.print(i);
        if (i < 100) { out.print("\t\t"); } else { out.print("\t"); }
        out.print(process.cputime);
        if (process.cputime < 100) { out.print(" (ms)\t\t"); } else { out.print(" (ms)\t"); }
        out.print(process.ioblocking);
        if (process.ioblocking < 100) { out.print(" (ms)\t\t"); } else { out.print(" (ms)\t"); }
        out.print(process.cpudone);
        if (process.cpudone < 100) { out.print(" (ms)\t\t"); } else { out.print(" (ms)\t"); }
        out.println(process.numblocked + " times");
      }
    } catch (FileNotFoundException e) {
        System.out.println("Couldn't find a file to write results");
    }
    finally {
      if(out!=null){
        out.close();
      }
    }
  System.out.println("Completed.");
  }
}

