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
    private static int processNum = 3;
    private static int meanDev = 1000;
    private static int standardDev = 100;
    private static int runtime = 1000;
    private static final List<Process> processes = new ArrayList<>();
    private static Results result = new Results("null","null",0);
    private static final String resultsFile = "Results\\Summary-Results";

  private static void readConfig(String file) {
    File f = new File(file);
    int cputime;
    int ioblocking;
    int weight;
    double X;

    try {

      Scanner configuration_in = new Scanner(f);
      int nextProcessIndex=0;
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
          weight = Common.configurationTokenToInteger(st.nextToken());
          X = Common.R1();
          while (X == -1.0) {
            X = Common.R1();
          }
          X = X * standardDev;
          cputime = (int)X + meanDev;
          processes.add(new Process(nextProcessIndex, cputime, ioblocking,weight));
          nextProcessIndex++;
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
  }

  public static void main(String[] args) {
    int i;
    File configuration_file;
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
    readConfig(configuration_file.getName());
    result = new SchedulingAlgorithm(processes).run(runtime, result);
    try (PrintStream out = new PrintStream(new FileOutputStream(resultsFile))) {
          out.println("Scheduling Type: " + result.schedulingType);
          out.println("Scheduling Name: " + result.schedulingName);
          out.println("Number of processes: "+processNum);
          out.println("Simulation Run Time: " + result.computationTime);
          out.println("Mean: " + meanDev);
          out.println("Standard Deviation: " + standardDev);
          out.println("Process #\tCPU Time\tIO Blocking\tCPU Completed\tCPU Blocked");
          for (i = 0; i < processes.size(); i++) {
              Process process = processes.get(i);
              out.print(i);
              if (i < 100) {
                  out.print("\t\t");
              } else {
                  out.print("\t");
              }
              out.print(process.cputime);
              if (process.cputime < 100) {
                  out.print(" (ms)\t\t");
              } else {
                  out.print(" (ms)\t");
              }
              out.print(process.ioblocking);
              if (process.ioblocking < 100) {
                  out.print(" (ms)\t\t");
              } else {
                  out.print(" (ms)\t");
              }
              out.print(process.cpudone);
              if (process.cpudone < 100) {
                  out.print(" (ms)\t\t");
              } else {
                  out.print(" (ms)\t");
              }
              out.println(process.numblocked + " times");
          }
      } catch (FileNotFoundException e) {
          System.out.println("Couldn't find a file to write results");
      }
  System.out.println("Completed.");
  }
}

