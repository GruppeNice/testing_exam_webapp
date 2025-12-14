# Risk Assessment

This section describes the identified risks related to the development of the hospital web application. The risks are assessed based on their probability and impact, and mitigation and follow-up actions are defined.

## Risk Assessment Table

| Risk ID | Risk Name                 | Description                                                                                                                  | Probability (1–5) | Impact (1–5) | Risk Factor (P×I) | Mitigation Actions                                                                                    | Status              | Follow-up Date             |
|---------|---------------------------|------------------------------------------------------------------------------------------------------------------------------|-------------------|--------------|-------------------|-------------------------------------------------------------------------------------------------------|---------------------|----------------------------|
| R1      | Unauthorized Data Access  | Non-admin users may gain access to data or operations beyond their intended permissions due to insufficient role validation. | 3                 | 5            | 15                | Implement role-based access checks in the backend, review permission logic, perform access testing.   | Closed (13/12/2025) | During backend development |
| R2      | Incomplete Requirements   | CRUD operations and role permissions are not fully specified in the SRS, leading to misunderstandings during development.    | 4                 | 3            | 12                | Review and refine the SRS, clarify entities and permissions; validate requirements with stakeholders. | Closed (23/11/2025) | End of requirements phase  |
| R3      | Data Integrity Issues     | Incorrect or concurrent CRUD operations may lead to inconsistent or corrupted data in the database.                          | 3                 | 4            | 12                | Add validation rules, use database transactions, test concurrent operations.                          | Closed (7/11/2025)  | Before system testing      |
| R4      | Frontend–Backend Mismatch | Frontend functionality may not correctly reflect backend constraints, causing errors or unauthorized actions.                | 3                 | 3            | 9                 | Perform integration testing, improve error handling.                                                  | Closed (30/11/2025) | During integration phase   |
| R5      | Authentication Failure    | Weak or improperly implemented authentication may allow unauthorized access to the system.                                   | 2                 | 5            | 10                | Use secure authentication mechanisms, test login flows, enforce password policies.                    | Closed (13/12/2025) | During backend development |
| R6      | Lack of Auditability      | User actions may not be logged, making it difficult to trace changes in a hospital environment.                              | 3                 | 4            | 12                | Add basic logging for CRUD actions, identify critical entities for audit logs.                        | Closed (13/12/2025) | During backend development |

## Risk Matrix

| Impact ↓ / Probability → | 1 | 2  | 3      | 4  | 5 |
|--------------------------|---|----|--------|----|---|
| **5 (Critical)**         |   | R5 | R1     |    |   |
| **4 (High)**             |   |    | R3, R6 |    |   |
| **3 (Medium)**           |   |    | R4     | R2 |   |
| **2 (Low)**              |   |    |        |    |   |
| **1 (Negligible)**       |   |    |        |    |   |
