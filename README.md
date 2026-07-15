# NHS Patient Directory API (Learning Prototype)

[![Java](https://img.shields.io/badge/Java-25-orange)](https://oracle.com)
[![FHIR R4](https://img.shields.io/badge/Standard-FHIR%20R4-blue)](https://hl7.org/fhir/R4)
[![NHS Digital](https://img.shields.io/badge/Domain-NHS%20Digital-005EB8)](https://digital.nhs.uk)

A lightweight REST API server exploring how patient demographic data can be exposed over HTTP, built using Java's core `HttpServer` class with no external frameworks — a hands-on learning project while training toward an NHS-focused Java developer role.

This is a **learning prototype**, not a production system — built to practise raw HTTP server mechanics, request/response handling, and API design before introducing frameworks like Spring Boot.

## 🔬 Domain Mapping

This project reuses the `Patient` domain model and FHIR R4-shaped JSON output built in [NHS Patient Demographic Engine](https://github.com/lloydcompiles/nhs-patient-demographic-engine), extending it from a console application into an HTTP API:

| System Attribute | Target FHIR R4 Resource / Element |
| :--- | :--- |
| **NHS Number** | `Patient.identifier` (System: `https://fhir.nhs.uk`) |
| **Patient Name** | `Patient.name` (Use: `official`) |
| **Admin Gender** | `Patient.gender` (`male` \| `female` \| `other` \| `unknown`) |
| **Contact Details** | `Patient.telecom` |

## 🛠️ Current Capabilities

- **HTTP server built from the JDK's `HttpServer`** — no Spring, no frameworks, request handling built from first principles
- **Single patient lookup** (`/api/patient`) — retrieve one patient by NHS number via query parameter
- **Bulk patient retrieval** (`/api/patients`) — retrieve all patients as a JSON array
- **NHS number validation** — malformed or missing NHS numbers return `400 Bad Request`
- **Structured JSON error responses** — every error returns a descriptive `{"error": "..."}` payload, not a bare status code
- **Health check endpoint** (`/`) — returns `{"status": "ok"}` for monitoring purposes
- **In-memory patient registry** — loads synthetic NHS PDS test data from CSV on server startup

## 🏗️ Architecture

CSV data → `CsvPatientLoader` → `PatientRegistry` (in-memory, loaded once at startup) → `HttpServer` request handlers → NHS number validated via `NhsNumber` → patient looked up → FHIR-shaped JSON via `PatientFhirJsonBuilder`.

## 🚀 Getting Started

1. Clone the repository:
    ```
    git clone https://github.com/lloydcompiles/nhs-patient-directory-api.git
    ```
2. Open in IntelliJ IDEA (or your preferred IDE) — built and tested on JDK 25.
3. Ensure `data/patients.csv` is present in the project root.
4. Run `Main.java`.
5. Server starts on `http://localhost:8080`.

## 📋 Sample Requests

**Health check:**
```bash
curl "http://localhost:8080/"
```
```json
{"status": "ok"}
```

**Get a single patient:**
```bash
curl "http://localhost:8080/api/patient?nhs_number=9449306605"
```
```json
{
  "resourceType": "Patient",
  "id": "9449306605",
  "name": [{
    "family": "GILMAN",
    "given": ["PERCY"],
    "prefix": ["MR"]
  }],
  "gender": "male",
  "birthDate": "2011-01-28",
  "telecom": [{
    "system": "phone",
    "value": "+44 20 7946 0192"
  }]
}
```

**Get all patients:**
```bash
curl "http://localhost:8080/api/patients"
```

**Invalid NHS number:**
```bash
curl "http://localhost:8080/api/patient?nhs_number=abc123"
```
```json
{"error": "Invalid NHS number format"}
```

**Patient not found:**
```bash
curl "http://localhost:8080/api/patient?nhs_number=9999999999"
```
```json
{"error": "Patient not found"}
```

## 🎓 Learning Context

Built as the next project in a structured Java learning programme, following a background in biochemistry, clinical SAS programming, and pharmaceutical data management. This project builds directly on the domain model from a previous project (NHS Patient Demographic Engine), extending it into HTTP API territory.

---
*lloydcompiles — career-changing into Java development, one milestone at a time.*