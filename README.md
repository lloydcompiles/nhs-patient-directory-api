# NHS Patient Directory API (Learning Prototype)

A lightweight REST API server for accessing patient records, built using the Java `HttpServer` class (no frameworks). This project is a follow-on from [nhs-patient-demographic-engine](https://github.com/lloydcompiles/nhs-patient-demographic-engine), reusing its domain model (`Patient`, `NhsNumber`, `PatientRegistry`, etc.) and exposing it over HTTP instead of a command-line interface.

## Status: In progress

This project is being built incrementally as part of a structured NHS-focused Java portfolio. Current state reflects work in progress, not a finished product.

## NHS context

NHS systems frequently need to expose patient demographic data to other services — for example, a clinical system querying a patient directory for basic details. This project simulates that scenario: a directory service that other systems could query over HTTP, built using core Java networking classes to understand what frameworks like Spring Boot handle under the hood.

## What it does (so far)

- Reuses the `Patient` domain model, `NhsNumber` validation, and `PatientRegistry` in-memory store from the demographic engine project
- Loads synthetic NHS PDS test data from CSV

## Planned

- `/api/patient` GET endpoint — retrieve a single patient by NHS number
- `/api/patients` GET endpoint — retrieve all patients
- Proper HTTP status codes (200, 404, 400) for success and error cases
- Graceful handling of malformed requests

## How to run

Instructions will be added once the server is functional.

## Tech

- Java (JDK 25)
- `com.sun.net.httpserver.HttpServer` — no external frameworks
