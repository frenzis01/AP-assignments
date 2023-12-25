package assignment2.part1.anagramcounter;

import assignment2.part1.strategyjobscheduler.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

public class Counter {
   // Singleton pattern to prevent multiple instances
   private static final Counter c = new Counter();
   public static void main(String[] args) {
      if (args.length != 1)
        throw new IllegalArgumentException();
      
      Path dir = Paths.get(args[0]);
      if (!dir.toFile().isDirectory())
         throw new IllegalArgumentException();
      
      // var jobs = Utils.getFilesFromDir(dir).map((f) -> c.new ReadFileJob(f));
      
      // Instantiate framework
   }

   public Counter() { super();}
}
