package anagramcounter;
import  strategyjobscheduler.*;

public class Counter {
   public static void main(String[] args) {
      // Instantiate framework and strategy
      JobSchedulerStrategy<String,String> st = new ReadFileStrategy();
      JobSchedulerContext<String,String> ct = new JobSchedulerContext<>(st);
      // inversion of control
      ct.process();
   }
}
