package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/** A type of OutputStrategy, saves the outputs to a file. */
public class FileOutputStrategy implements OutputStrategy {  //changed class name to UpperCamelCase  //Fixed indentation for the whole file according to Google guidelines (2 spaces)

  private String baseDirectory; //changed variable name to lowerCamelCase

  public final ConcurrentHashMap<String, String> file_map = new ConcurrentHashMap<>();

    
  /**
   * Constructs the output strategy, initializing the 
   * baseDirectory variable with the given filepath.
   * 
   * @param baseDirectory The file path to the directory we want to save our output.
   */
  public FileOutputStrategy(String baseDirectory){  //changed constructor name to UpperCamelCase
    this.baseDirectory = baseDirectory; //changed (BaseDirctory) variable name to lowerCamelCase
  }

  /**
   * This method is defined in the OutputStrategy interface 
   * and is implemented to save the output data to a file.
   * In this implementation it creates a new directory at the given path,
   * if it doesn't already exist and wirtes the data in a file
   * named after the label parameter.
   * 
   * @throws Exception if any errors occur and prints the filePath and the error message.
   */
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