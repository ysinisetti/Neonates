# Case Management API Documentation

## Overview
Complete REST API implementation for Case Creation and Management in the Neonates application.

## Base URL
```
http://localhost:8080/api/cases
```

## Endpoints

### 1. Create Case
**POST** `/api/cases`

Create a new case with default values set automatically.

#### Request Headers
```
Content-Type: application/json
```

#### Request Body
```json
{
  "hospitalId": 1,
  "processType": "BRC",
  "caseCategory": "Organ Donation",
  "statusNfiLevel": "Pending",
  "statusPanelLevel": "Pending",
  "createdBy": 1
}
```

#### Request Parameters
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| hospitalId | Long | Yes | ID of the hospital handling the case |
| processType | Enum | Yes | Type of process: BRC, BRRC, BGRC, BCRC, NON_BRC |
| caseCategory | String | No | Category of the case (e.g., Organ Donation) |
| statusNfiLevel | String | No | Status at NFI level |
| statusPanelLevel | String | No | Status at panel level |
| createdBy | Long | No | User ID who created the case |

#### Success Response (201 Created)
```json
{
  "timestamp": "2026-04-15T10:30:00.123456",
  "status": 201,
  "success": true,
  "message": "Case created successfully",
  "data": {
    "caseId": 1,
    "caseUuid": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    "caseReferenceNo": 1001,
    "hospitalId": 1,
    "processType": "BRC",
    "caseStatus": "Draft",
    "statusNfiLevel": "Pending",
    "statusPanelLevel": "Pending",
    "caseCategory": "Organ Donation",
    "intakeDate": "2026-04-15",
    "approvalDate": null,
    "rejectionDate": null,
    "closureDate": null,
    "createdBy": 1,
    "createdAt": "2026-04-15T10:30:00.123456",
    "updatedAt": "2026-04-15T10:30:00.123456"
  }
}
```

#### Error Responses

**400 Bad Request - Missing Required Field**
```json
{
  "timestamp": "2026-04-15T10:30:00.123456",
  "status": 400,
  "success": false,
  "message": "Hospital ID is required"
}
```

**400 Bad Request - Missing Process Type**
```json
{
  "timestamp": "2026-04-15T10:30:00.123456",
  "status": 400,
  "success": false,
  "message": "Process type is required"
}
```

**404 Not Found - Hospital Not Found**
```json
{
  "timestamp": "2026-04-15T10:30:00.123456",
  "status": 404,
  "success": false,
  "message": "Hospital not found with id: 999"
}
```

---

### 2. Get Case by ID
**GET** `/api/cases/{id}`

Retrieve case details by case ID.

#### Request Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | Long | Yes | Case ID |

#### Example Request
```
GET http://localhost:8080/api/cases/1
```

#### Success Response (200 OK)
```json
{
  "timestamp": "2026-04-15T10:35:00.123456",
  "status": 200,
  "success": true,
  "message": "Case fetched successfully",
  "data": {
    "caseId": 1,
    "caseUuid": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    "caseReferenceNo": 1001,
    "hospitalId": 1,
    "processType": "BRC",
    "caseStatus": "Draft",
    "statusNfiLevel": "Pending",
    "statusPanelLevel": "Pending",
    "caseCategory": "Organ Donation",
    "intakeDate": "2026-04-15",
    "approvalDate": null,
    "rejectionDate": null,
    "closureDate": null,
    "createdBy": 1,
    "createdAt": "2026-04-15T10:30:00.123456",
    "updatedAt": "2026-04-15T10:30:00.123456"
  }
}
```

#### Error Response (404 Not Found)
```json
{
  "timestamp": "2026-04-15T10:35:00.123456",
  "status": 404,
  "success": false,
  "message": "Case not found with id: 999"
}
```

---

## Default Values

When a case is created, the following default values are automatically set:

| Field | Default Value |
|-------|---|
| case_status | Draft |
| intake_date | Current Date |
| created_at | Current DateTime |
| case_uuid | Auto-generated UUID |
| case_reference_no | Auto-incremented (1001, 1002, 1003, ...) |

---

## Enum Values

### CaseStatus
- `Draft` - Initial status when case is created
- `Submitted` - Case has been submitted for review
- `Returned` - Case returned for modifications
- `Under_Verification` - Case under verification
- `Under_Review` - Case under review
- `Approved` - Case approved
- `Rejected` - Case rejected
- `Closed` - Case closed

### ProcessType
- `BRC` - Blood Related Center
- `BRRC` - Bone Related Resource Center
- `BGRC` - Biomedical Gamete Resource Center
- `BCRC` - Blood Component Resource Center
- `NON_BRC` - Non-BRC Process

---

## Validation Rules

1. **hospitalId** is mandatory
2. **processType** is mandatory
3. Hospital must exist in the system
4. All dates are in ISO format (YYYY-MM-DD)
5. All timestamps are in ISO 8601 format (YYYY-MM-DDTHH:mm:ss.SSSSSS)

---

## Architecture

### Layer Structure
```
Controller (CaseController)
    ↓
Service (CaseService / CaseServiceImpl)
    ↓
Repository (CaseRepository)
    ↓
Database (case_master table)
```

### Key Components

1. **Entity** (`Case.java`)
   - JPA entity mapped to case_master table
   - Uses Lombok for boilerplate code
   - Implements UUID auto-generation
   - Uses @PrePersist for default values

2. **DTOs**
   - `CaseCreateRequestDTO` - For creating cases
   - `CaseResponseDTO` - For API responses

3. **Mapper** (`CaseMapper.java`)
   - MapStruct interface for DTO ↔ Entity conversion
   - Ignores auto-generated fields during mapping

4. **Service Layer**
   - `CaseService` interface
   - `CaseServiceImpl` implementation
   - Business logic for case creation and retrieval
   - Validation and hospital existence check

5. **Controller** (`CaseController.java`)
   - REST endpoints with proper HTTP methods
   - Request validation with @Valid
   - CORS support with @CrossOrigin
   - Proper response structures

6. **Repository** (`CaseRepository.java`)
   - Extends JpaRepository
   - Custom query methods

---

## Usage Examples

### cURL Examples

**Create Case:**
```bash
curl -X POST http://localhost:8080/api/cases \
  -H "Content-Type: application/json" \
  -d '{
    "hospitalId": 1,
    "processType": "BRC",
    "caseCategory": "Organ Donation",
    "statusNfiLevel": "Pending",
    "statusPanelLevel": "Pending",
    "createdBy": 1
  }'
```

**Get Case:**
```bash
curl -X GET http://localhost:8080/api/cases/1 \
  -H "Content-Type: application/json"
```

---

## Testing with Postman

Import the `Neonates_API.postman_collection.json` file into Postman. The collection includes:
- Case folder with "Create Case" and "Get Case by ID" requests
- Pre-configured variables ({{hospital_id}}, {{user_id}}, {{case_id}})
- Sample JSON bodies ready to use

---

## Future Enhancements

1. **Update Case Status**
   - Endpoint: PUT /api/cases/{id}/status
   - Update approval_date, rejection_date, closure_date based on status

2. **List Cases**
   - GET /api/cases (with pagination)
   - GET /api/cases/hospital/{hospitalId}
   - GET /api/cases/status/{status}

3. **Delete Case**
   - DELETE /api/cases/{id}

4. **Search & Filtering**
   - Filter by date range
   - Filter by process type
   - Filter by case status

5. **Case History/Audit**
   - Track all status changes
   - Maintain audit trail

---

## Error Handling

All errors are handled through a centralized `GlobalExceptionHandler` which returns consistent response structures:

```json
{
  "timestamp": "ISO_8601_DATETIME",
  "status": "HTTP_STATUS_CODE",
  "success": false,
  "message": "ERROR_MESSAGE",
  "data": null
}
```

---

## Database Table Schema

```sql
CREATE TABLE case_master (
  case_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  case_uuid CHAR(36) UNIQUE NOT NULL,
  case_reference_no BIGINT UNIQUE NOT NULL,
  hospital_id BIGINT NOT NULL,
  process_type ENUM('BRC','BRRC','BGRC','BCRC','NON_BRC') NOT NULL,
  case_status ENUM('Draft','Submitted','Returned','Under_Verification','Under_Review','Approved','Rejected','Closed') DEFAULT 'Draft',
  status_nfi_level VARCHAR(100),
  status_panel_level VARCHAR(100),
  case_category VARCHAR(255),
  intake_date DATE,
  approval_date DATE,
  rejection_date DATE,
  closure_date DATE,
  created_by BIGINT,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (hospital_id) REFERENCES hospital(hospital_id),
  FOREIGN KEY (created_by) REFERENCES app_user(user_id)
);
```

---

## Version
**API Version:** 1.0
**Last Updated:** April 15, 2026
