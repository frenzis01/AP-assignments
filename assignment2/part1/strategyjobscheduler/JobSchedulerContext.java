package strategyjobscheduler;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class JobSchedulerContext<K,V> {
   JobSchedulerStrategy<K,V> st;
   /**
    * Contructor which takes a Strategy as parameter,
    * providing the implementation for emit and output hotspots 
    * @param st JobSchedulerStrategy subclass implementing framework's hotspots
    */
   public JobSchedulerContext(JobSchedulerStrategy<K,V> st){
      this.st = st;
   }
   
   /**
    * Invokes execute on each AJob obj 
    * @param pairs
    * @return stream of results of each job
    */
   private Stream<Pair<K,V>> compute (Stream<AJob<K,V>> jobs) {
      return jobs.flatMap(j -> j.execute());
   }

   /**
    * Groups AJob objs computation results which share the same Key K
    * @param pairs
    * @return <K,V> pairs grouped on the K keys
    */
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
   
   /**
    * Framework workflow, ultimately providing the "Inversion of Control"
    */
   public void process(){
      var jobs = st.emit();
      var results = compute(jobs);
      var groupedResults = collect(results);
      st.output(groupedResults);
   }
}
