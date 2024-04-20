package com.cardio_generator.generators;

//Grouped non-static imports
import java.util.Random;
import com.cardio_generator.outputs.OutputStrategy;


/**
 * This class is responsible for generating alerts for patients, by assigning alertStates to them based on a value generated randomly. 
 * These alert states are later handled by classes that implement the OutoutStrategy interface.
 */
public class AlertGenerator implements PatientDataGenerator {  //Fixed indentation for the whole file according to Google guidelines (2 spaces)

  public static final Random randomGenerator = new Random();
  private boolean[] alertStates; // false = resolved, true = pressed  //Changed (AlertStates) variable to lowerCamelCase

  /**
   * The constructor initializes the class with the given patientCount + 1
   * 
   * @param patientCount Number of patients.
   */
  public AlertGenerator(int patientCount) {
    alertStates = new boolean[patientCount + 1];  //Changed (AlertStates) variable to lowerCamelCase
  }

  /**
   * Generates an alert for the given patient and outputs the state of the alert.
   * It has 90% chance of being resolved instantly, but 10% chance to trigger the alert.
   * 
   * @param patientId The ID of the current patient.
   * @param outputStrategy The choosen method for outputting the patient data.
   * 
   * @throws Exception if any errors occur, printing the patient ID and the stack trace.
   */
  @Override
  public void generate(int patientId, OutputStrategy outputStrategy) {
    try {
      if (alertStates[patientId]) {   //Changed (AlertStates) variable to lowerCamelCase
        if (randomGenerator.nextDouble() < 0.9) { // 90% chance to resolve
          alertStates[patientId] = false; //Changed (AlertStates) variable to lowerCamelCase
          // Output the alert
          outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
        }
      } else {
        double Lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency
        double p = -Math.expm1(-Lambda); // Probability of at least one alert in the period
        boolean alertTriggered = randomGenerator.nextDouble() < p;

        if (alertTriggered) {
          alertStates[patientId] = true;  //Changed (AlertStates) variable to lowerCamelCase
          // Output the alert
          outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
        }
      }
    } catch (Exception e) {
      System.err.println("An error occurred while generating alert data for patient " + patientId);
      e.printStackTrace();
    }
  }
}
