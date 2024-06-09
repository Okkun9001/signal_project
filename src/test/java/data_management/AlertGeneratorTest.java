package data_management;

import com.alerts.AlertGenerator;
import com.data_management.DataStorage;
import com.data_management.Patient;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class AlertGeneratorTest {

    @Test
    void testAbnormalDataAlert() {
        // Arrange
        Patient patient = new Patient(1); // Initialize patient with necessary data
        int windowSize = 10; // Set window size
        int farBeyondPercentage = 20; // Set far beyond percentage
        DataStorage storage = new DataStorage(); // Initialize data storage
        storage.addPatientData(1, 75.0, "ECG", 1714376789050L);
        storage.addPatientData(1, 75.0, "ECG", 1714376789051L);
        storage.addPatientData(1, 75.0, "ECG", 1714376789052L);
        storage.addPatientData(1, 75.0, "ECG", 1714376789053L);
        storage.addPatientData(1, 75.0, "ECG", 1714376789054L);
        storage.addPatientData(1, 75.0, "ECG", 1714376789055L);
        storage.addPatientData(1, 75.0, "ECG", 1714376789056L);
        storage.addPatientData(1, 75.0, "ECG", 1714376789057L);
        storage.addPatientData(1, 75.0, "ECG", 1714376789058L);
        storage.addPatientData(1, 100.0, "ECG", 1714376789059L);
        AlertGenerator alertGenerator = new AlertGenerator(storage);

        // Act
        boolean result = alertGenerator.abnormalDataAlert(patient, windowSize, farBeyondPercentage);

        // Assert
        assertTrue(result); // Replace with expected result
    }
}