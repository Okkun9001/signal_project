package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

//Fixed indentation for the whole file according to Google guidelines (2 spaces)
public class FileOutputStrategy implements OutputStrategy {  //changed class name to UpperCamelCase

  private String baseDirectory; //changed variable name to lowerCamelCase

  public final ConcurrentHashMap<String, String> file_map = new ConcurrentHashMap<>();

    
  //changed constructor name to UpperCamelCase
  public FileOutputStrategy(String baseDirectory){
    this.baseDirectory = baseDirectory; //changed (BaseDirctory) variable name to lowerCamelCase
  }

  @Override
  public void output(int patientId, long timestamp, String label, String data) {
    try {
      // Create the directory
      Files.createDirectories(Paths.get(baseDirectory));  //changed (BaseDirectory) variable name to lowerCamelCase
    } catch (IOException e) {
      System.err.println("Error creating base directory: " + e.getMessage());
      return;
    }
      // Set the FilePath variable
      String filePath = file_map.computeIfAbsent(label, k -> Paths.get(baseDirectory, label + ".txt").toString());    //changed (FilePath and BaseDirectory) variable names to lowerCamelCase 

      // Write the data to the file
      try (PrintWriter out = new PrintWriter(
        Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {  //changed (FilePath) variable name to lowerCamelCase
        out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
      } catch (Exception e) {
        System.err.println("Error writing to file " + filePath + ": " + e.getMessage());    //changed (FilePath) variable name to lowerCamelCase
      }
  }
}