package assignment2.part1.anagramcounter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collector;
import java.util.stream.Stream;

import assignment2.part1.strategyjobscheduler.*;

public class ReadFileStrategy extends JobSchedulerStrategy<String, String> {

   @Override
   public Stream<AJob<String, String>> emit() {
      // throw new UnsupportedOperationException("Unimplemented method 'emit'");
      String userinput;
      Path path;
      do {
         Scanner scanner = new Scanner(System.in); // Create a Scanner object
         System.out.println("Enter (valid) path to directory");

         userinput = scanner.nextLine(); // Read user input
         path = Paths.get(userinput);
         scanner.close();
      } while (!path.toFile().isDirectory());

      return Utils.getFilesFromDir(path).map((f) -> new ReadFileJob(f));
   }

   @Override
   public void output(Stream<Pair<String, List<String>>> groupedComputations) {

      Stream<String> lines = groupedComputations.map(pair -> pair.getKey() + " : " + pair.getValue().size() + "\n");
      String outputPath = "output/count_anagrams.txt";
      // This doesn't exploit Streams
      // try {
      // // Write the string to the file
      // Files.write(Paths.get(outputPath),
      // String.join("",lines.toList()).getBytes());

      // System.out.println("File written successfully.");
      // } catch (IOException e) {
      // e.printStackTrace();
      // }

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

      public ReadFileJob(Path path) {
         this.path = path;
      }

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

   private static Stream<String> getWordStreamFromFile(Path path) throws IOException {
      if (!path.toFile().isFile())
         throw new IllegalArgumentException();
      return Files.lines(path)
            .flatMap(line -> Stream.of(line.split("\\s+|(?<=[\\s\\n])|(?=[\\s\\n])")))
            .map(String::toLowerCase)
            .filter(w -> w.length() >= 4 && w.matches("[a-z]+"));
   }

   private static String ciao(String s) {
      // toLowerCase is performed also in getWordStreamFromFile:
      // it is redundant but we leave it anyway for robustness
      char[] chars = s.toLowerCase().toCharArray();
      Arrays.sort(chars);
      return new String(chars);
   }
}
