package anagramcounter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
public class Utils {
   private Utils(){}

   public static Stream<Path> getFilesFromDir(Path dir) {
      if (dir.toFile().isDirectory()) {
         return getFileStream(dir);
      }

      return Stream.empty();
   }

   private static Stream<Path> getFileStream(Path dir) {
      try {
         return Files.walk(dir)
            .filter(p -> p.toFile().isFile() && p.toString().endsWith(".txt"));
               // .flatMap(p -> {
               //    File f = p.toFile();
               //    if (f.isDirectory())
               //       return getFileStream(p);
               //    if (p.toString().endsWith(".txt"))
               //       return Stream.of(p);
               //    return Stream.empty();
               // });
      } catch (IOException e) {
         e.printStackTrace();
         return Stream.empty();
      }
   }
}
