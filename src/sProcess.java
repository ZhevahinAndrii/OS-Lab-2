package src;

public class sProcess {
  public int indexOfProcess;

  public int cputime; //time which process should run to complete
  public int ioblocking; //the time between I/O blocks the process
  public int cpudone; // time which process worked
  public int ionext; // counter for I/O blocks
  public int numblocked; // number of times process was I/O blocked
  public int weight = 1; //priority

  public sProcess(int indexOfProcess, int cputime, int ioblocking, int cpudone, int ionext, int numblocked){
    this.indexOfProcess = indexOfProcess;
    this.cputime = cputime;
    this.ioblocking = ioblocking;
    this.cpudone = cpudone;
    this.ionext = ionext;
    this.numblocked = numblocked;
  }
  public boolean isCompleted(){
    return cpudone>=cputime;
  }
  public boolean isIOBlocked(){
    return ionext>=ioblocking;
  }
  public String toString(){
    return String.format("%d %d %d",this.cputime,this.ioblocking,this.cpudone);
  }
}
