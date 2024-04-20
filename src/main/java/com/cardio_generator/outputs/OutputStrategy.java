package com.cardio_generator.outputs;

/** Interface for various ways of outputing the patients' data. */

public interface OutputStrategy {
    /**
     * Handles the output of patient alerts.
     * 
     * @param patientId The ID of the patient we want to print the data of.
     * @param timestamp Time of recording the output data.
     * @param label The name of the output data.
     * @param data The value of the output.
     */
    void output(int patientId, long timestamp, String label, String data);
}
