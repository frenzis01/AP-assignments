package assignment2.part1.strategyjobscheduler;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public abstract class JobScheduler<K,V> {
   public abstract Stream<AJob<K,V>> emit ();
   private Stream<Pair<K,V>> compute (Stream<AJob<K,V>> jobs) {
      return jobs.flatMap(j -> j.execute());
   }
   // private class KeysGrouper implements Collector<Pair<K,V>,Pair<K,List<V>>,Pair<K,List<V>>>{
   //    public Pair<K,List<V>> supplier() {
   //       return new Stream.
   //    }
   // }
   private Stream<Pair<K,List<V>>> collect (Stream<Pair<K,V>> pairs) {
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
   public abstract void output ();

}
