package strategyjobscheduler;

import java.util.List;
import java.util.stream.Stream;

public abstract class JobSchedulerStrategy<K,V> {
   /**
    * These are the two hotspots which JobSchedulerStrategy subclasses must implement
   */
   public abstract Stream<AJob<K,V>> emit ();
   public abstract void output(Stream<Pair<K,List<V>>> groupedComputations);
}
