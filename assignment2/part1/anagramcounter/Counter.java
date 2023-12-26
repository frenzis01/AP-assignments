package anagramcounter;
import  strategyjobscheduler.*;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Counter {
   public static void main(String[] args) {
      // Instantiate framework
      JobSchedulerStrategy<String,String> st = new ReadFileStrategy();
      JobSchedulerContext<String,String> ct = new JobSchedulerContext<>(st);
      ct.process();
   }
}
