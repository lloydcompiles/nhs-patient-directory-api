package com.lloydcompiles.nhs.patient;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        PatientRegistry registry = new PatientRegistry();

        CsvPatientLoader csvPatientLoader = new CsvPatientLoader("data/patients.csv");
        List<Patient> loadedPatients = csvPatientLoader.loadPatients();
        for (Patient patient : loadedPatients){
            try {
                registry.addPatient(patient);
            } catch (DuplicatePatientException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        // Create an HTTP server listening on localhost:8080
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);

        // Register a handler for the root path "/"
        server.createContext("/", exchange -> {
            String response = "Hello";
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
        });

        server.createContext("/api/patient", exchange -> {
            String query = exchange.getRequestURI().getQuery();

            if (query == null || !query.contains("nhs_number=")) {
                sendErrorResponse(exchange, 400, "Missing required parameter: nhs_number");
                return;
            }

            String nhsNumberString = query.split("=")[1];
            NhsNumber nhsNumber = null;
            try {
                nhsNumber = new NhsNumber(nhsNumberString);
            } catch (InvalidNHSNumberException e) {
                sendErrorResponse(exchange, 400, "Invalid NHS number format");
                return;
            }
            Patient patient = registry.findByNhsNumber(nhsNumber);

            if (patient == null) {
                sendErrorResponse(exchange, 404, "Patient not found");
                return;
            }

            PatientFhirJsonBuilder patientFhirJsonBuilder = new PatientFhirJsonBuilder(patient);
            String jsonResponse = patientFhirJsonBuilder.buildPatientFhirJson();
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);
            exchange.getResponseBody().write(jsonResponse.getBytes());
            exchange.close();
        });

        server.createContext("/api/patients", exchange -> {
            ArrayList<Patient>  patientsArrayList =  registry.getAllPatients();
            String patientsJsonString;

            if (!patientsArrayList.isEmpty()) {
                StringBuilder patientsJson = new StringBuilder("[ ");

                for (Patient patient : patientsArrayList){
                    PatientFhirJsonBuilder patientFhirJsonBuilder = new PatientFhirJsonBuilder(patient);
                    patientsJson.append(patientFhirJsonBuilder.buildPatientFhirJson()).append(", ");
                }
                patientsJson.setLength(patientsJson.length() - 2);
                patientsJson.append(" ]");
                patientsJsonString = patientsJson.toString();
            } else {
                patientsJsonString = "[]";
            }

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, patientsJsonString.getBytes().length);
            exchange.getResponseBody().write(patientsJsonString.getBytes());
            exchange.close();
        });

        // Start the server
        server.start();
        System.out.println("Server started on http://localhost:8080");
    }

    private static void sendErrorResponse(HttpExchange exchange, int status, String message) throws IOException {
        String jsonErrorBody = "{ \"error\": \"" + message + "\"}";
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, jsonErrorBody.getBytes().length);
        exchange.getResponseBody().write(jsonErrorBody.getBytes());
        exchange.close();
    }
}