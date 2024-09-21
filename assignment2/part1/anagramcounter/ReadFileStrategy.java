package anagramcounter;

import java.io.BufferedWriter;
import java.io.Console;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import strategyjobscheduler.*;

public class ReadFileStrategy extends JobSchedulerStrategy<String, String> {

   /**
    * Prompts the user to enter a path to a directory,
    * and returns a Stream of ReadFileJob objects, one for each file in the directory (and subdirs)
    */
   @Override
   public Stream<AJob<String, String>> emit() {
      String userinput;
      Path path = null;

      // Using console for stdin allows multiple reuse
      Console console = System.console();

      if (console == null) {
          System.out.println("No console available");
          return Stream.empty();
      }
  
      do {
          userinput = console.readLine("Enter (valid) path to directory: "); // Read user input
          path = Paths.get(userinput);
      } while (!path.toFile().isDirectory());

      return Utils.getFilesFromDir(path).map((f) -> new ReadFileJob(f));
   }

   /**
    * Groups the results of the ReadFileJob computations by the 'ciao' value
    * and writes the count of anagrams for each 'ciao' value to a file in the work dir
    */
   @Override
   public void output(Stream<Pair<String, List<String>>> groupedComputations) {

      Stream<String> lines = groupedComputations.map(pair -> pair.getKey() + " : " + pair.getValue().size());
      String outputPath = "count_anagrams.txt";

      try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputPath))) {
         // Writing each string to the file
         lines.forEach(line -> {
            try {
               writer.write(line);
               writer.newLine(); // Add a new line after each string
            } catch (IOException e) {
               throw new UncheckedIOException(e);
            }
         });
      } catch (IOException e) {
         e.printStackTrace();
         System.out.println("Cannot write to " + outputPath);
      }
   }

   private class ReadFileJob extends AJob<String, String> {
      Path path;

      /**
       * @param path of the file to be read
       */
      public ReadFileJob(Path path) {
         this.path = path;
      }
      
      /**
       * Reads every word from a file and computes its 'ciao'
       * @return Stream of <word,ciao(word)> pairs
       */
      public Stream<Pair<String, String>> execute() {
         try {
            return getWordStreamFromFile(this.path)
                  .map(w -> new Pair<>(ciao(w), w));
         } catch (IOException e) {
            e.printStackTrace();
            return null;
         }

      }
   }

   /**
    * Generates a stream containing all the words inside a .txt file
    * @param path
    * @return Stream of the words inside the path file
    * @throws IOException
    */
   private static Stream<String> getWordStreamFromFile(Path path) throws IOException {
      if (!path.toFile().isFile() || !path.toString().endsWith(".txt"))
         throw new IllegalArgumentException();
      return Files.lines(path)
            .flatMap(line -> Stream.of(line.split("\\s+|(?<=[\\s\\n])|(?=[\\s\\n])")))
            .map(String::toLowerCase)
            .filter(w -> w.length() >= 4 && w.matches("[a-z]+"));
   }

   /**
    * ciao(str) is the string having the same length of str and
    * containing all the characters of str
    * in lower case and alphabetical order
    * @param s 
    * @return ciao(s)
    */
   private static String ciao(String s) {
      // toLowerCase is performed also in getWordStreamFromFile:
      // it is redundant but we leave it anyway for robustness
      char[] chars = s.toLowerCase().toCharArray();
      Arrays.sort(chars);
      return new String(chars);
   }
}
