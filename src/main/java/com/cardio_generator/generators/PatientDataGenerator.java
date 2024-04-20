package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/** Interface for generating various types of patient data. */
public interface PatientDataGenerator {
    
    /**
     * Generates some patient data and handles the output with outputStrategy
     * 
     * @param patientId ID of patient to generate the data for.
     * @param outputStrategy The specified way of outputing the data.
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
