package com.alerts;

import java.sql.Time;
import java.util.List;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import com.cardio_generator.generators.*;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    private DataStorage dataStorage;

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {
        // Check the conditions for each alert
        if (trendAlert(patient,10)) {
          triggerAlert(new Alert(String.valueOf(patient.getId()), "Trend Alert", System.currentTimeMillis()));
        }
        if (criticalThresholdAlert(patient)) {
          triggerAlert(new Alert(String.valueOf(patient.getId()), "Critical Threshold Alert", System.currentTimeMillis()));
        }
        if (lowSaturationAlert(patient,92)) {
          triggerAlert(new Alert(String.valueOf(patient.getId()), "Low Saturation Alert", System.currentTimeMillis()));
        }
        if (rapidDropAlert(patient, 5)) {
          triggerAlert(new Alert(String.valueOf(patient.getId()), "Rapid Drop Alert", System.currentTimeMillis()));
        }
        if (hypotensiveHypoxemiaAlert(patient)) {
          triggerAlert(new Alert(String.valueOf(patient.getId()), "Hypotensive Hypoxemia Alert", System.currentTimeMillis()));
        }
        if (abnormalDataAlert(patient, 10, 85)) {
          triggerAlert(new Alert(String.valueOf(patient.getId()), "Abnormal Data Alert", System.currentTimeMillis()));
        }
    }
    /**
     * Evaluates the specified patient's ECG data to determine if an alart needs to be triggered.
     * @param patient the patient to evaluate for alert conditions
     * @param windowSize the size of the window to evaluate
     * @param farBeyondPrcentage the percentage of the average that the last value needs to be to trigger an alert
     * @return true if an alert needs to be triggered, false otherwise
     */
    public boolean abnormalDataAlert(Patient patient, int windowSize, int farBeyondPrcentage) {
        long now = System.currentTimeMillis();
        long windowSecAgo = now - (windowSize * 1000);
        List<PatientRecord> ecgData = patient.getRecordsFor("ECG",now, windowSecAgo);

        // Calculate the average of the ECG data in last 10 seconds
        double sum=0;
        for(int i = 0; i < ecgData.size()-1; i++) {
          sum+=ecgData.get(i).getMeasurementValue();
        }
        double average = sum/ecgData.size();
        double farBeyond = ((farBeyondPrcentage/100)+1)*average;
        System.out.println(ecgData.size());
        if(ecgData.get(ecgData.size()-1).getMeasurementValue() >= farBeyond) {
          return true;
        }
        return false;
    }

    public boolean hypotensiveHypoxemiaAlert(Patient patient) {
      long now = System.currentTimeMillis();
      long fiveMinutesAgo = now - (5 * 60 * 1000);
      List<PatientRecord> systolicData = patient.getRecordsFor("SystolicPressure",now, fiveMinutesAgo);
      List<PatientRecord> saturationData = patient.getRecordsFor("Saturation",now, fiveMinutesAgo);

      PatientRecord lastSystolic = systolicData.get(systolicData.size()-1);
      PatientRecord lastSaturation = saturationData.get(saturationData.size()-1);
      if(lastSystolic.getMeasurementValue()<90 && lastSaturation.getMeasurementValue()<92) {
        return true;
      }
      return false;
    }

    private boolean rapidDropAlert(Patient patient, int changePercentage) {
        long now = System.currentTimeMillis();
        long tenMinAgo = now - (600 * 1000);
        List<PatientRecord> saturationData = patient.getRecordsFor("Saturation",now, tenMinAgo);
        double diff = changePercentage/100;

        double change = saturationData.get(0).getMeasurementValue()-saturationData.get(saturationData.size()-1).getMeasurementValue();

        if((change/saturationData.get(0).getMeasurementValue()) >= diff) {
          return true;
        }
        return false;

    }

    private boolean lowSaturationAlert(Patient patient, int thrshold) {
        long now = System.currentTimeMillis();
        long fivSecAgo = now - (5 * 1000);
        List<PatientRecord> saturationData = patient.getRecordsFor("Saturation",now, fivSecAgo);
        int threshold = thrshold;
        for (PatientRecord record : saturationData) {
          if (record.getMeasurementValue() < threshold) {
            return true;
          }
        }
        return false;
    }

    private boolean criticalThresholdAlert(Patient patient) {
      long now = System.currentTimeMillis();
      long fiveMinutesAgo = now - (5 * 60 * 1000);
      List<PatientRecord> systolicData = patient.getRecordsFor("SystolicPressure",now, fiveMinutesAgo);
      List<PatientRecord> diastolicData = patient.getRecordsFor("DiastolicPressure",now, fiveMinutesAgo);

      int sLowerBound = 90;
      int sUpperBound = 180;
      int dLowerBound = 60;
      int dUpperBound = 120;

      for (PatientRecord record : systolicData) {
        if (record.getMeasurementValue() < sLowerBound || record.getMeasurementValue() > sUpperBound) {
        return true;
        }
      }
      for (PatientRecord record : diastolicData) {
        if (record.getMeasurementValue() < dLowerBound || record.getMeasurementValue() > dUpperBound) {
        return true;
        }
      }
    return false;
    }

    private boolean trendAlert(Patient patient, int rateOfChange) {
      int rate = rateOfChange;
      long now = System.currentTimeMillis();
      long fiveMinutesAgo = now - (5 * 60 * 1000);
      List<PatientRecord> systolicData = patient.getRecordsFor("SystolicPressure", now, fiveMinutesAgo);
      List<PatientRecord> diastolicData = patient.getRecordsFor("DiastolicPressure", now, fiveMinutesAgo);


      if (systolicData.size() < 2 && diastolicData.size() < 2) {
        return false;
      }
      int count = 0;
      for(int i = systolicData.size()-1; i > 0; i--) {
        if (Math.abs(systolicData.get(i).getMeasurementValue()-systolicData.get(i - 1).getMeasurementValue()) <= rate) {
          count = 0;
        }else{
          count++;
          if (count == 3) {
           return true;
          }
        }
      }
      for(int i = diastolicData.size()-1; i > 0; i--) {
        if (Math.abs(diastolicData.get(i).getMeasurementValue()-diastolicData.get(i - 1).getMeasurementValue()) <= rate) {
          count = 0;
        }else{
          count++;
          if (count == 3) {
           return true;
          }
        }
      }
      return false;
    }

    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(Alert alert) {
        System.out.println("ALERT: " + alert.getCondition() + " for patient ID: " + alert.getPatientId() + " at " + new Time(alert.getTimestamp()));
    }
}
