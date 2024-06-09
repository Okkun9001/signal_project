package com.data_management;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * FileDataReader is an implementation of the DataReader interface.
 * It reads data from a directory of files, with each line in each file representing a data record.
 * The data is then stored in a DataStorage object for further processing.
 */  
public class FileDataReader implements DataReader{


    /**
     * The directory from which to read the data.
     */
    private final String directory;

    /**
     * Constructs a new FileDataReader that reads data from the specified directory.
     *
     * @param directory the directory from which to read the data
     */
    public FileDataReader(String directory){
        this.directory = directory;
    }

    /**
     * Reads data from the directory specified in the constructor and stores it in the provided DataStorage object.
     * Each line in each file is parsed into a patient ID, a measurement value, a record type, and a timestamp,
     * and this parsed data is then stored in the DataStorage object.
     *
     * @param dataStorage the DataStorage object in which to store the read data
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        Files.walk(Paths.get(directory)).filter(Files::isRegularFile).forEach(file -> parseFile(file, dataStorage));
    }

    /**
     * Parses a file into data records and stores them in the provided DataStorage object.
     * Each line in the file is parsed into a patient ID, a measurement value, a record type, and a timestamp,
     * and this parsed data is then stored in the DataStorage object.
     *
     * @param file the file to parse
     * @param dataStorage the DataStorage object in which to store the parsed data
     */
    private void parseFile(Path file, DataStorage dataStorage) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                int patientId = Integer.parseInt(parts[0].split(": ")[1]);
                double measurementValue = Double.parseDouble(parts[1].split(": ")[1]);
                String recordType = parts[2].split(": ")[1];
                long timestamp = Long.parseLong(parts[3].split(": ")[1]);

                dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
            }
        } catch (IOException e) {
            System.err.println("Error reading data from file " + file + ": " + e.getMessage());
        }
    }
    
}
