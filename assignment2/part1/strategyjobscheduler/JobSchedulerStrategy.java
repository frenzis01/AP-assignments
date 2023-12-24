package assignment2.part1.strategyjobscheduler;

import java.util.List;
import java.util.stream.Stream;

public abstract class JobSchedulerStrategy<K,V> {
   public abstract Stream<AJob<K,V>> emit ();
   public abstract void output(Stream<Pair<K,List<V>>> groupedComputations);
}
