package anagramcounter;
import  strategyjobscheduler.*;

public class Counter {
   public static void main(String[] args) {
      // Instantiate framework and strategy
      JobSchedulerStrategy<String,String> st = new ReadFileStrategy();
      JobSchedulerContext<String,String> ct = new JobSchedulerContext<>(st);
      // inversion of control
      ct.process();

      // Let's process again but changing the output to CSV
      // st = new ReadFileStrategyCSV();
      // ct = new JobSchedulerContext<>(st);
      // ct.process();
   }
}
