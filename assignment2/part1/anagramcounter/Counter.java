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
   public static void main(String[] args) {
      if (args.length != 1)
      throw new IllegalArgumentException();
   }
   

   private static Stream<Path> getFilesFromDir(Path dir) {
      if (dir.toFile().isDirectory()) {
         return getFileStream(dir);
      }

      return Stream.empty();
   }

   private static Stream<Path> getFileStream(Path dir) {
      try {
         return Files.walk(dir)
               .flatMap(p -> {
                  File f = p.toFile();
                  if (f.isDirectory())
                     return getFileStream(p);
                  if (p.toString().endsWith(".txt"))
                     return Stream.of(p);
                  return Stream.empty();
               });
      } catch (IOException e) {
         e.printStackTrace();
         return Stream.empty();
      }
   }
   
   private class FileReader extends AJob<String,String>{
      String path;
      public FileReader (String path){
         this.path = path;
      }

      public Stream<Pair<String,String>> execute() {
         try {
            return getWordStreamFromFile(this.path)
                     .map(w -> new Pair<>(ciao(w),w));
         } catch (IOException e) {
            e.printStackTrace();
            return null;
         }
         
      }
   }
   
   private static Stream<String> getWordStreamFromFile(String path) throws IOException{
      Path p = Paths.get(path);
      return Files.lines(p)
            .flatMap(line -> Stream.of(line.split("\\s+|(?<=[\\s\\n])|(?=[\\s\\n])")))
            .map(String::toLowerCase)
            .filter(w -> w.length() >= 4 && w.matches("[a-z]+"));
   }

   private static String ciao (String s){
      // toLowerCase is performed also in getWordStreamFromFile:
      // it is redundant but we leave it anyway for robustness 
      char[] chars = s.toLowerCase().toCharArray();
      Arrays.sort(chars); 
      return new String(chars);
   }
}
