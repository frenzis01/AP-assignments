package assignment2.part1.anagramcounter;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Counter {
   public static void main(String[] args) {
      if (args.length != 1)
        throw new IllegalArgumentException();
      
      Path dir = Paths.get(args[0]);
      if (!dir.toFile().isDirectory())
         throw new IllegalArgumentException();
      
      
      // Instantiate framework
   }

   public Counter() { super();}
}
