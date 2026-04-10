# Neonates Workflow Backend

## Table of Contents
- [About](#about)
- [Tech Stack](#tech-stack)
- [Architecture Overview](#architecture-overview)
- [Prerequisites](#prerequisites)
- [Configuration](#configuration)
- [Running Locally](#running-locally)
- [API Reference](#api-reference)
- [Testing](#testing)
- [Swagger UI](#swagger-ui)
- [Contributing](#contributing)
- [License](#license)

## About
Neonates is a Spring Boot 3.5 service that backs a neonatal case and document workflow. It tracks incoming cases, their attached documents, and auxiliary lookup data while persisting metadata in MySQL and binaries in Azure Blob Storage. Versioning, secure download links, and Azure SAS URL generation are first-class features so that the front-end can orchestrate secure uploads and downloads without storing payloads locally.

## Tech Stack
- Java 21, Spring Boot 3.5
- Spring Web MVC, Spring Data JPA
- Azure Storage Blob SDK for uploading/downloading files
- MySQL as the relational datastore
- SpringDoc OpenAPI for documentation
- Lombok for DTOs/entities

## Architecture Overview
- **Document management** (`DocumentController`, `DocumentService`, `AzureBlobService`, `DocumentRepository`, `Document`/`DocumentStore` entities) tracks versions, stores metadata in `document_metadata`, and persists binary assets in an Azure container. It exposes upload, download, delete, blob-SAS-generation, and version-history endpoints.
- **Case orchestration** (`CaseController`, `CaseService`, `CasesRepository`, `ChildProfileRepository`, `CaseSequenceRepository`, `Cases`, `ChildProfile`) generates case reference numbers (`NEO{year}-{seq}`), captures reporting hospital/process metadata, and records child profiles every time a case is created or updated.
- **Lookup data** (`LookupMasterController`, `LookupService`, `LookupMasterRepository`) lets clients fetch reference values (e.g., hospitals, process types, roles) by category.
- **Swagger/OpenAPI** configures the documentation UI at runtime via `SwaggerConfig`.

## Prerequisites
- Java 21 SDK
- Maven (invoked via `./mvnw` or locally-installed `mvn`)
- MySQL database (`neonates_rnd_test` by default)
- An Azure Storage account + container for blobs

## Configuration
Adjust `src/main/resources/application.properties` before running:

| Property | Purpose | Example/Notes |
| --- | --- | --- |
| `spring.datasource.url` | JDBC URL for MySQL | `jdbc:mysql://localhost:3306/neonates_rnd_test` |
| `spring.datasource.username` / `spring.datasource.password` | DB credentials | `root` / `root` (change for production) |
| `spring.servlet.multipart.*` | Maximum upload/request size | Defaults to `50MB` |
| `azure.storage.connection-string` | Azure Storage connection string | `DefaultEndpointsProtocol=...` |
| `azure.storage.container-name` | Target container for documents | `containerName` |
| `server.port` | HTTP port (defaults to `9998`) | `9998` |

> **Hint:** The Azure connection string must include access keys; the service relies on that string and the container name for every file operation.

## Running Locally
1. Start your MySQL instance and ensure the `neonates_rnd_test` schema exists (the entities expect tables like `cases`, `document_metadata`, `lookup_master`, etc.).
2. Seed any required lookup/reference data (hospitals, lookup masters, document stores) before hitting the endpoints.
3. Run the app:
   ```bash
   ./mvnw clean spring-boot:run
   ```
4. Alternatively, build a WAR and deploy to an external Tomcat or run with `java -jar`:
   ```bash
   ./mvnw clean package
   java -jar target/Neonates-0.0.1-SNAPSHOT.war
   ```

## API Reference
- `POST /documents/upload` – multipart upload with `userId` + `file`; returns `DocumentResponse` with version/blobs. Versions increment automatically via `DocumentService`.
- `GET /documents/versions?userId={}&documentName={}` – returns previously uploaded `Document` rows for the user/name.
- `DELETE /documents/delete/{id}` – removes metadata + deletes the blob from Azure.
- `GET /documents/download/{id}` – streams the latest blob via `AzureBlobService` with `Content-Disposition` headers.
- `POST /documents/sas/upload-url` – body params `userId`, `fileName`; returns a short-lived SAS upload URL so front-end clients can ship blobs directly to Azure.
- `GET /case` – returns `CaseResponse` DTOs summarizing existing cases, hospital, and reporter info.
- `POST /case/create` – accepts `FormRequest` (case metadata + nested `ChildInfo`) to create or update cases, updates statuses, and stores the child profile. Case IDs/metadata live in `cases`, `child_profile`, while sequences land in `case_sequence`.
- `GET /lookup/fetchByCategory/{category}` – returns `LookupData` for dropdowns (process types, statuses, roles, etc.).

## Testing
`src/test/java/com/neonates/DocumentApplicationTests.java` runs a simple Spring context-smoke test. Execute:

```bash
./mvnw test
```

## Swagger UI
Once running, visit `http://localhost:9998/swagger-ui/index.html` (default port) to explore endpoints and generated models (powered by `springdoc-openapi`).

## Contributing
- Follow existing package layout: controllers → services → repositories → entities → DTOs.
- Keep business logic in services, controllers should orchestrate request/response transformation.
- Add integration tests under `src/test/java/com/neonates` when touching persistence or Azure behavior.

## License
This project does not currently specify a license. Add one in `pom.xml`/`README.md` before releasing.
