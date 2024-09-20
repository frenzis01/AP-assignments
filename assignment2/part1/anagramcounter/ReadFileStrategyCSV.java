package anagramcounter;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import strategyjobscheduler.Pair;

public class ReadFileStrategyCSV extends ReadFileStrategy{
    @Override
    public void output(Stream<Pair<String, List<String>>> groupedComputations) {

        Stream<String> lines = groupedComputations.map(pair -> pair.getKey() + ";" + pair.getValue().size());
        String outputPath = "count_anagrams.csv";
  
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputPath))) {
            // Write CSV header
            writer.write("Ciao;Count");
            writer.newLine();

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
}