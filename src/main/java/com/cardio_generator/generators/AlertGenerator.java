package com.cardio_generator.generators;

//Grouped non-static imports
import java.util.Random;
import com.cardio_generator.outputs.OutputStrategy;

//Fixed indentation for the whole file according to Google guidelines (2 spaces)
public class AlertGenerator implements PatientDataGenerator {

  public static final Random randomGenerator = new Random();
  private boolean[] alertStates; // false = resolved, true = pressed  //Changed (AlertStates) variable to lowerCamelCase

  public AlertGenerator(int patientCount) {
    alertStates = new boolean[patientCount + 1];    //Changed (AlertStates) variable to lowerCamelCase
  }

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
