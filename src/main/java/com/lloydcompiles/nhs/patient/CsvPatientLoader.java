package com.lloydcompiles.nhs.patient;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads and parses patient records from a CSV file into a List of Patient objects.
 * Reads the file line by line using BufferedReader, maps each column to the
 * corresponding domain model field, and skips any rows with invalid NHS numbers.
 */
public class CsvPatientLoader {

    private static final int EXPECTED_COLUMN_COUNT = 8;
    private String filePath;

    public CsvPatientLoader(String filePath) {
        this.filePath = filePath;
    }

    public List<Patient> loadPatients() {

        ArrayList<Patient> patients = new ArrayList<>();
        String line = "";
        String csvSplitBy = ",";


        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] data = line.split(csvSplitBy);

                if (data.length >= EXPECTED_COLUMN_COUNT) {

                    String nhsNumber = data[0].trim();
                    String familyName = data[1].trim();
                    String givenName = data[2].trim();
                    String prefix = data[3].trim();
                    String contactSystem = data[4].trim();
                    String contactValue = data[5].trim();
                    String gender = data[6].trim();
                    String dateOfBirth = data[7].trim();

                    try {
                        NhsNumber validNhsNumber = new NhsNumber(nhsNumber);
                        HumanName name = new HumanName(familyName, givenName, prefix);
                        ContactPoint contact = new ContactPoint(ContactSystem.valueOf(contactSystem), contactValue);
                        LocalDate dob = LocalDate.parse(dateOfBirth);

                        Patient patient = new Patient(validNhsNumber,name,contact,AdministrativeGender.valueOf(gender),dob);
                        patients.add(patient);

                    } catch (InvalidNHSNumberException e) {
                        System.err.println("Skipping invalid NHS Number: " + nhsNumber + "\n" + e.getMessage());
                    }
                } else {
                    System.err.println("Skipping malformed row (expected " + EXPECTED_COLUMN_COUNT + " columns): " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Could not read patient file: " + e.getMessage());
        }

        return patients;
    }
}
