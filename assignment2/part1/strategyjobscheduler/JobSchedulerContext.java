package assignment2.part1.strategyjobscheduler;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class JobSchedulerContext<K,V> {
   JobSchedulerStrategy<K,V> st;
   public JobSchedulerContext(JobSchedulerStrategy<K,V> st){
      this.st = st;
   }

   private Stream<Pair<K,V>> compute (Stream<AJob<K,V>> jobs) {
      return jobs.flatMap(j -> j.execute());
   }

   private Stream<Pair<K,List<V>>> collect (Stream<Pair<K,V>> pairs)
   {
      return pairs
         .collect(Collectors
            .groupingBy(
               Pair::getKey,
               Collectors.mapping(
                  Pair::getValue,
                  Collectors.toList()
               )
            )
         )
         .entrySet()
         .stream()
         .map(e -> new Pair<>(e.getKey(),e.getValue()));
   };
   
   public void process(){
      var jobs = st.emit();
      var results = compute(jobs);
      var groupedResults = collect(results);
      st.output(groupedResults);
   }
}
