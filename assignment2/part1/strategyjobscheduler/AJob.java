package assignment2.part1.strategyjobscheduler;

import java.util.stream.Stream;

public abstract class AJob<K,V>{

    public abstract Stream<Pair<K,V>> execute();

}
