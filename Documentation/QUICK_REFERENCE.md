# Quick Reference Guide - Case Management API

## 🚀 Quick Start

### 1. Create a Case (in 30 seconds)

**Endpoint:** POST http://localhost:8080/api/cases

**Minimum Required JSON:**
```json
{
  "hospitalId": 1,
  "processType": "BRC"
}
```

**Full JSON Example:**
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

**Expected Response:** 201 Created

---

### 2. Retrieve a Case (in 15 seconds)

**Endpoint:** GET http://localhost:8080/api/cases/1

**Expected Response:** 200 OK

---

## 📚 ProcessType Values

```
BRC        - Blood Related Center
BRRC       - Bone Related Resource Center
BGRC       - Biomedical Gamete Resource Center
BCRC       - Blood Component Resource Center
NON_BRC    - Non-BRC Process
```

---

## 📊 CaseStatus Values

```
Draft                - Initial status (default)
Submitted            - Case submitted for review
Returned             - Returned for modifications
Under_Verification   - Currently being verified
Under_Review         - Under review
Approved             - Case approved
Rejected             - Case rejected
Closed               - Case closed
```

---

## ⚠️ Validation Rules

### Required Fields
- ✅ `hospitalId` - Must be a valid hospital ID
- ✅ `processType` - Must be one of the enum values

### Validation Results
- **Valid:** Returns 201 Created with case data
- **Invalid:** Returns 400 Bad Request with error message
- **Hospital Not Found:** Returns 404 Not Found

---

## 🔄 Response Structure

### Success Response Format
```json
{
  "timestamp": "ISO_8601_DATETIME",
  "status": "HTTP_STATUS_CODE",
  "success": true,
  "message": "Success message",
  "data": {
    "caseId": 1,
    "caseUuid": "UUID_STRING",
    "caseReferenceNo": 1001,
    "hospitalId": 1,
    "processType": "BRC",
    "caseStatus": "Draft",
    ...
  }
}
```

### Error Response Format
```json
{
  "timestamp": "ISO_8601_DATETIME",
  "status": "HTTP_STATUS_CODE",
  "success": false,
  "message": "Error message",
  "data": null
}
```

---

## 💡 Common Scenarios

### Scenario 1: Create a case for Organ Donation
```bash
curl -X POST http://localhost:8080/api/cases \
  -H "Content-Type: application/json" \
  -d '{
    "hospitalId": 1,
    "processType": "BRC",
    "caseCategory": "Organ Donation",
    "createdBy": 1
  }'
```

### Scenario 2: Get case details
```bash
curl -X GET http://localhost:8080/api/cases/1
```

### Scenario 3: What if hospital doesn't exist?
```bash
# This will return 404 Not Found
curl -X POST http://localhost:8080/api/cases \
  -H "Content-Type: application/json" \
  -d '{
    "hospitalId": 999,
    "processType": "BRC"
  }'
```

---

## 🎯 HTTP Status Codes

| Code | Meaning | Example |
|------|---------|---------|
| 201 | Created | Case created successfully |
| 200 | OK | Case retrieved successfully |
| 400 | Bad Request | Missing required field |
| 404 | Not Found | Case or Hospital not found |
| 500 | Server Error | Database error |

---

## 🔐 Default Values (Automatic)

When you create a case, these are set automatically:

```
caseStatus    → "Draft"
intakeDate    → Today's date (2026-04-15)
caseUuid      → Random UUID (e.g., f47ac10b-58cc-4372-a567-0e02b2c3d479)
caseRefNo     → Auto-incremented (1001, 1002, 1003...)
createdAt     → Current timestamp
updatedAt     → Current timestamp
```

---

## 🧪 Test with Postman

1. **Open Neonates_API collection**
2. **Go to Case folder**
3. **Create Case request:**
   - Click Send
   - Check response status (should be 201)
   - Copy the caseId from response

4. **Get Case request:**
   - Update case_id variable with copied value
   - Click Send
   - Check response status (should be 200)

---

## 🐛 Troubleshooting

### Error: "Hospital ID is required"
**Solution:** Add hospitalId to request body

### Error: "Hospital not found with id: X"
**Solution:** Use a valid hospitalId that exists in your database

### Error: "Process type is required"
**Solution:** Add processType field to request (must be one of: BRC, BRRC, BGRC, BCRC, NON_BRC)

### Getting 500 error?
**Solution:** Check server logs for detailed error message

---

## 📋 Postman Variables

Set these variables in Postman for easier testing:

```
{{base_url}}          = http://localhost:8080
{{hospital_id}}       = 1
{{user_id}}           = 1
{{case_id}}           = 1 (update after creating case)
```

---

## 🔗 Related APIs

These APIs are used alongside Case API:

- **Hospital API** - Get hospital details
- **AppUser API** - Get user details  
- **BalanceSnapshot API** - Financial data
- **Donor API** - Donor information

---

## 📞 Support

For detailed documentation, see:
- `CASE_API_DOCUMENTATION.md` - Full API documentation
- `IMPLEMENTATION_COMPLETE.md` - Implementation details

---

**Last Updated:** April 15, 2026
**API Version:** 1.0
