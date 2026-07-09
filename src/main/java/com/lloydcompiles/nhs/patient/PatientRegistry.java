package com.lloydcompiles.nhs.patient;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Manages an in-memory collection of Patient records.
 * Stores patients in an ArrayList to preserve insertion order,
 * and in a HashMap keyed by NHS Number for fast lookup by identifier.
 */
public class PatientRegistry {

    private ArrayList<Patient> patientArrayList;
    private HashMap<String, Patient>  patientHashMap;

    public PatientRegistry() {
        this.patientArrayList = new ArrayList<>();
        this.patientHashMap = new HashMap<>();
    }

    public void addPatient(Patient patient) throws DuplicatePatientException {

        Patient existingPatient = patientHashMap.get(patient.getNhsNumber().getValue());
        if (existingPatient == null) {
            patientArrayList.add(patient);
            patientHashMap.put(patient.getNhsNumber().getValue(), patient);
        } else {
            throw new DuplicatePatientException("NHS Number " + patient.getNhsNumber().getValue() + " already exists. \nNHS numbers must be unique. \n");
        }
    }

    public Patient findByNhsNumber(NhsNumber nhsNumber) {
        return patientHashMap.get(nhsNumber.getValue());
    }

    public ArrayList<Patient> getAllPatients() {
        return  patientArrayList;
    }
}
