package anagramcounter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class Utils {

   public static Stream<Path> getFilesFromDir(Path dir) {
      try {
         // Files.walk recurively visits also subdirs
         if (!dir.toFile().isDirectory())
            throw new IllegalArgumentException();
         return Files.walk(dir)
               .filter(p -> p.toFile().isFile() && p.toString().endsWith(".txt"));
      } catch (IllegalArgumentException | IOException e) {
         e.printStackTrace();
         return Stream.empty();
      }
   }
}
