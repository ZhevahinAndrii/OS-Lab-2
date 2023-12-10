package src;

public class Process {
  public int indexOfProcess;
  public int cputime; //time which process should run to complete
  public int ioblocking; // time of process' work between it block for I/O
  public int cpudone; // time which process worked
  public int ionext; // shows how much time process worked since the last I/O blocking
  public int numblocked; // number of times process was I/O blocked
  public int weight; // priority

  public Process(int indexOfProcess, int cputime, int ioblocking,int weight){
    this.indexOfProcess = indexOfProcess;
    this.cputime = cputime;
    this.ioblocking = ioblocking;
    this.cpudone = 0;
    this.ionext = 0;
    this.numblocked = 0;
    this.weight = weight;
  }
  public boolean isCompleted(){
    return cpudone>=cputime;
  }
  public boolean isIOBlocked(){
    return ionext==ioblocking;
  }
  public int timeToBlocking(){
    return ioblocking-ionext;
  }
  public int timeToComplete(){
    return cputime-cpudone;
  }
  public String toString(){
    return String.format("%d %d %d",this.cputime,this.ioblocking,this.cpudone);
  }
}
