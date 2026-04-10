-- ============================================================
-- NFI CMDWS (V1.2) - MySQL DDL Skeleton
-- Purpose : Create tables, indexes, and reporting-parity views
-- Engine  : MySQL 8.x (InnoDB), utf8mb4
-- Notes   :
--  - Application generates UUIDs (CHAR(36)); IDs use BIGINT AUTO_INCREMENT.
--  - Actual files are stored in Azure Blob / S3-compatible storage.
--  - Verified docs are locked (is_verified => is_locked by default).
-- ============================================================

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;

-- ------------------------------------------------------------
-- (Optional) Create and use database
-- ------------------------------------------------------------
-- CREATE DATABASE IF NOT EXISTS nfi_cmdws DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- USE nfi_cmdws;

-- ============================================================
-- 1) MASTER TABLES
-- ============================================================

CREATE TABLE IF NOT EXISTS hospital (
  hospital_id            BIGINT AUTO_INCREMENT PRIMARY KEY,
  hospital_uuid          CHAR(36) NOT NULL UNIQUE,
  hospital_name          VARCHAR(200) NOT NULL,
  hospital_code          VARCHAR(50) NULL,
  hospital_type          ENUM('BRC','BRRC','BGRC','BCRC','NON_BRC') NOT NULL,
  city                   VARCHAR(100) NULL,
  state                  VARCHAR(100) NULL,
  spoc_name              VARCHAR(150) NULL,
  spoc_phone             VARCHAR(30) NULL,
  active_flag            BOOLEAN NOT NULL DEFAULT TRUE,
  created_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_hospital_name (hospital_name),
  INDEX idx_hospital_state_city (state, city)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS app_user (
  user_id                BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_uuid              CHAR(36) NOT NULL UNIQUE,
  full_name              VARCHAR(150) NOT NULL,
  email                  VARCHAR(200) NOT NULL UNIQUE,
  phone                  VARCHAR(30) NULL,
  primary_role           ENUM('Hospital','Intake','Committee','Volunteer','Verifier','Admin') NOT NULL,
  hospital_id            BIGINT NULL,
  active_flag            BOOLEAN NOT NULL DEFAULT TRUE,
  created_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_user_hospital
    FOREIGN KEY (hospital_id) REFERENCES hospital(hospital_id)
    ON UPDATE CASCADE ON DELETE SET NULL,
  INDEX idx_user_role (primary_role),
  INDEX idx_user_hospital (hospital_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS user_role_map (
  user_role_id           BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id                BIGINT NOT NULL,
  role                   ENUM('Hospital','Intake','Committee','Volunteer','Verifier','Admin') NOT NULL,
  scope_hospital_id      BIGINT NULL,
  active_flag            BOOLEAN NOT NULL DEFAULT TRUE,
  created_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_user_role_user
    FOREIGN KEY (user_id) REFERENCES app_user(user_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_user_role_scope_hospital
    FOREIGN KEY (scope_hospital_id) REFERENCES hospital(hospital_id)
    ON UPDATE CASCADE ON DELETE SET NULL,
  INDEX idx_user_role_map_user (user_id),
  INDEX idx_user_role_map_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 2) CASE + CORE PROFILE TABLES
-- ============================================================

CREATE TABLE IF NOT EXISTS case_master (
  case_id                BIGINT AUTO_INCREMENT PRIMARY KEY,
  case_uuid              CHAR(36) NOT NULL UNIQUE,
  case_reference_no      VARCHAR(50) NOT NULL UNIQUE,
  hospital_id            BIGINT NOT NULL,
  process_type           ENUM('BRC','BRRC','BGRC','BCRC','NON_BRC') NOT NULL,
  case_status            ENUM('Draft','Submitted','Under_Verification','Under_Review','Approved','Rejected','Closed') NOT NULL DEFAULT 'Draft',
  status_nfi_level       VARCHAR(100) NULL,
  status_committee_level VARCHAR(100) NULL,
  intake_date            DATE NULL,
  closure_date           DATE NULL,
  case_category          VARCHAR(100) NULL,
  link_beneficiary_folder VARCHAR(500) NULL,
  created_by             BIGINT NOT NULL,
  created_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_case_hospital
    FOREIGN KEY (hospital_id) REFERENCES hospital(hospital_id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT fk_case_created_by
    FOREIGN KEY (created_by) REFERENCES app_user(user_id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  INDEX idx_case_process_status (process_type, case_status),
  INDEX idx_case_hospital_intake (hospital_id, intake_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS child_profile (
  child_id               BIGINT AUTO_INCREMENT PRIMARY KEY,
  case_id                BIGINT NOT NULL UNIQUE,
  beneficiary_no         VARCHAR(50) NOT NULL UNIQUE,
  beneficiary_name       VARCHAR(150) NOT NULL,
  gender                 ENUM('Male','Female','Other','Unknown') NOT NULL DEFAULT 'Unknown',
  date_of_birth          DATE NOT NULL,
  birth_status           VARCHAR(50) NULL,
  gestational_age_weeks  DECIMAL(5,2) NULL,
  birth_weight_kg        DECIMAL(5,2) NULL,
  current_weight_kg      DECIMAL(5,2) NULL,
  admission_date         DATE NULL,
  nicu_stay_days         INT NULL,
  mortality              VARCHAR(50) NULL,
  morbidity              VARCHAR(200) NULL,
  date_of_closure        DATE NULL,
  created_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_child_case
    FOREIGN KEY (case_id) REFERENCES case_master(case_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  INDEX idx_child_dob (date_of_birth),
  INDEX idx_child_gender (gender)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS family_profile (
  family_id              BIGINT AUTO_INCREMENT PRIMARY KEY,
  case_id                BIGINT NOT NULL UNIQUE,
  father_name            VARCHAR(150) NULL,
  father_phone           VARCHAR(30) NULL,
  mother_name            VARCHAR(150) NULL,
  mother_phone           VARCHAR(30) NULL,
  email_id               VARCHAR(200) NULL,
  address                TEXT NULL,
  father_employment_profile     VARCHAR(150) NULL,
  father_employment_profile_alt VARCHAR(150) NULL,
  total_monthly_income   DECIMAL(12,2) NULL,
  created_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_family_case
    FOREIGN KEY (case_id) REFERENCES case_master(case_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  INDEX idx_family_income (total_monthly_income)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS clinical_case_details (
  clinical_id            BIGINT AUTO_INCREMENT PRIMARY KEY,
  case_id                BIGINT NOT NULL UNIQUE,
  hospital_name_snapshot VARCHAR(200) NULL,
  primary_diagnosis      VARCHAR(200) NULL,
  case_summary_0         TEXT NULL,
  case_summary_1         TEXT NULL,
  case_summary_2         TEXT NULL,
  case_summary_3         TEXT NULL,
  created_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_clinical_case
    FOREIGN KEY (case_id) REFERENCES case_master(case_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  FULLTEXT INDEX ft_case_summaries (case_summary_0, case_summary_1, case_summary_2, case_summary_3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS financial_case_details (
  financial_id           BIGINT AUTO_INCREMENT PRIMARY KEY,
  case_id                BIGINT NOT NULL UNIQUE,
  medical_bill_estimate  DECIMAL(12,2) NULL,
  total_amount_approved  DECIMAL(12,2) NULL,
  created_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_financial_case
    FOREIGN KEY (case_id) REFERENCES case_master(case_id)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 3) WORKFLOW, COMMITTEE, REJECTION
-- ============================================================

CREATE TABLE IF NOT EXISTS workflow_status_history (
  workflow_id            BIGINT AUTO_INCREMENT PRIMARY KEY,
  case_id                BIGINT NOT NULL,
  from_status            VARCHAR(100) NULL,
  to_status              VARCHAR(100) NOT NULL,
  changed_by             BIGINT NOT NULL,
  changed_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  change_reason          VARCHAR(300) NULL,
  CONSTRAINT fk_workflow_case
    FOREIGN KEY (case_id) REFERENCES case_master(case_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_workflow_user
    FOREIGN KEY (changed_by) REFERENCES app_user(user_id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  INDEX idx_workflow_case (case_id, changed_at),
  INDEX idx_workflow_to_status (to_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS committee_decision (
  decision_id            BIGINT AUTO_INCREMENT PRIMARY KEY,
  case_id                BIGINT NOT NULL,
  committee_type         ENUM('BRC','BRRC','BGRC','BCRC','NFI_INTERNAL') NOT NULL,
  submission_date        DATE NULL,
  approval_date          DATE NULL,
  outcome               ENUM('Approved','Rejected','Need_More_Info','Deferred') NULL,
  comments               TEXT NULL,
  created_by             BIGINT NOT NULL,
  created_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_decision_case
    FOREIGN KEY (case_id) REFERENCES case_master(case_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_decision_user
    FOREIGN KEY (created_by) REFERENCES app_user(user_id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  INDEX idx_decision_case_type (case_id, committee_type),
  INDEX idx_decision_dates (committee_type, submission_date, approval_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS committee_funding_installment (
  funding_id             BIGINT AUTO_INCREMENT PRIMARY KEY,
  decision_id            BIGINT NOT NULL,
  funding_source         ENUM('NFI','BGRC','Other') NOT NULL,
  installment_no         TINYINT NOT NULL DEFAULT 0,
  amount_approved        DECIMAL(12,2) NOT NULL DEFAULT 0,
  created_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_funding_decision
    FOREIGN KEY (decision_id) REFERENCES committee_decision(decision_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  UNIQUE KEY uq_funding_unique (decision_id, funding_source, installment_no),
  INDEX idx_funding_source (funding_source)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS rejection_details (
  rejection_id           BIGINT AUTO_INCREMENT PRIMARY KEY,
  case_id                BIGINT NOT NULL UNIQUE,
  rejection_reason_category ENUM('Medical','Financial','Other') NULL,
  rejection_level        ENUM('NFI','BRC','BRRC','BGRC','BCRC','Other') NULL,
  rejection_communication_status VARCHAR(100) NULL,
  referring_hospital     VARCHAR(200) NULL,
  rejection_case_summary TEXT NULL,
  created_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_rejection_case
    FOREIGN KEY (case_id) REFERENCES case_master(case_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  INDEX idx_rejection_level (rejection_level),
  INDEX idx_rejection_reason (rejection_reason_category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 4) DOCUMENTS, VERSIONING, CHECKLIST
-- ============================================================

CREATE TABLE IF NOT EXISTS bgrc_cycle (
  bgrc_cycle_id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  cycle_month            TINYINT NOT NULL,
  cycle_year             SMALLINT NOT NULL,
  notes                  TEXT NULL,
  utilization_document_id BIGINT NULL,
  created_by             BIGINT NOT NULL,
  created_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_bgrc_created_by
    FOREIGN KEY (created_by) REFERENCES app_user(user_id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  UNIQUE KEY uq_bgrc_month_year (cycle_month, cycle_year)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS document_metadata (
  document_id            BIGINT AUTO_INCREMENT PRIMARY KEY,
  document_uuid          CHAR(36) NOT NULL UNIQUE,
  case_id                BIGINT NULL,
  bgrc_cycle_id          BIGINT NULL,
  document_category      ENUM('GENERAL','MEDICAL','FINANCIAL','FINAL','COMMUNICATION') NOT NULL,
  document_type          VARCHAR(150) NOT NULL,
  file_name              VARCHAR(255) NOT NULL,
  file_format            ENUM('PDF','JPG','PNG','DOC','DOCX','XLS','XLSX','TXT') NOT NULL,
  file_size_kb           INT NULL,
  storage_path           VARCHAR(500) NOT NULL,
  checksum_sha256        CHAR(64) NULL,
  uploaded_by            BIGINT NOT NULL,
  uploaded_at            DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  immutable_flag         BOOLEAN NOT NULL DEFAULT TRUE,
  is_verified            BOOLEAN NOT NULL DEFAULT FALSE,
  verified_by            BIGINT NULL,
  verified_at            DATETIME NULL,
  verification_notes     TEXT NULL,
  is_locked              BOOLEAN NOT NULL DEFAULT FALSE,
  locked_reason          VARCHAR(300) NULL,
  active_flag            BOOLEAN NOT NULL DEFAULT TRUE,
  created_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_doc_case
    FOREIGN KEY (case_id) REFERENCES case_master(case_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_doc_cycle
    FOREIGN KEY (bgrc_cycle_id) REFERENCES bgrc_cycle(bgrc_cycle_id)
    ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT fk_doc_uploaded_by
    FOREIGN KEY (uploaded_by) REFERENCES app_user(user_id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT fk_doc_verified_by
    FOREIGN KEY (verified_by) REFERENCES app_user(user_id)
    ON UPDATE CASCADE ON DELETE SET NULL,
  INDEX idx_doc_case (case_id),
  INDEX idx_doc_category_type (document_category, document_type),
  INDEX idx_doc_verified (is_verified, is_locked),
  INDEX idx_doc_uploaded_at (uploaded_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Link utilization doc after document table exists
ALTER TABLE bgrc_cycle
  ADD CONSTRAINT fk_bgrc_util_doc
  FOREIGN KEY (utilization_document_id) REFERENCES document_metadata(document_id)
  ON UPDATE CASCADE ON DELETE SET NULL;

CREATE TABLE IF NOT EXISTS document_version (
  document_version_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
  document_id            BIGINT NOT NULL,
  version_no             INT NOT NULL,
  supersedes_version_id  BIGINT NULL,
  storage_path           VARCHAR(500) NOT NULL,
  file_name              VARCHAR(255) NOT NULL,
  file_size_kb           INT NULL,
  checksum_sha256        CHAR(64) NULL,
  created_by             BIGINT NOT NULL,
  created_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_docver_doc
    FOREIGN KEY (document_id) REFERENCES document_metadata(document_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_docver_supersedes
    FOREIGN KEY (supersedes_version_id) REFERENCES document_version(document_version_id)
    ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT fk_docver_user
    FOREIGN KEY (created_by) REFERENCES app_user(user_id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  UNIQUE KEY uq_docver_unique (document_id, version_no),
  INDEX idx_docver_doc (document_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS document_requirement_template (
  requirement_id         BIGINT AUTO_INCREMENT PRIMARY KEY,
  process_type           ENUM('BRC','BRRC','BGRC','BCRC','NON_BRC') NOT NULL,
  document_category      ENUM('GENERAL','MEDICAL','FINANCIAL','FINAL','COMMUNICATION') NOT NULL,
  document_type          VARCHAR(150) NOT NULL,
  mandatory_flag         BOOLEAN NOT NULL DEFAULT TRUE,
  condition_notes        VARCHAR(500) NULL,
  active_flag            BOOLEAN NOT NULL DEFAULT TRUE,
  created_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uq_req_unique (process_type, document_category, document_type),
  INDEX idx_req_process (process_type, mandatory_flag)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS case_document_checklist_status (
  checklist_id           BIGINT AUTO_INCREMENT PRIMARY KEY,
  case_id                BIGINT NOT NULL,
  requirement_id         BIGINT NOT NULL,
  status                 ENUM('Missing','Uploaded','Verified','Not_Applicable') NOT NULL DEFAULT 'Missing',
  satisfied_by_document_id BIGINT NULL,
  last_updated_by        BIGINT NOT NULL,
  last_updated_at        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_check_case
    FOREIGN KEY (case_id) REFERENCES case_master(case_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_check_req
    FOREIGN KEY (requirement_id) REFERENCES document_requirement_template(requirement_id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT fk_check_doc
    FOREIGN KEY (satisfied_by_document_id) REFERENCES document_metadata(document_id)
    ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT fk_check_user
    FOREIGN KEY (last_updated_by) REFERENCES app_user(user_id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  UNIQUE KEY uq_case_req_unique (case_id, requirement_id),
  INDEX idx_check_status (case_id, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS bgrc_cycle_case_map (
  map_id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
  bgrc_cycle_id          BIGINT NOT NULL,
  case_id                BIGINT NOT NULL,
  created_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_cycle_map_cycle
    FOREIGN KEY (bgrc_cycle_id) REFERENCES bgrc_cycle(bgrc_cycle_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_cycle_map_case
    FOREIGN KEY (case_id) REFERENCES case_master(case_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  UNIQUE KEY uq_cycle_case (bgrc_cycle_id, case_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 5) BENI/BIMS OPERATIONS + FOLLOWUPS
-- ============================================================

CREATE TABLE IF NOT EXISTS beni_program_ops (
  beni_ops_id            BIGINT AUTO_INCREMENT PRIMARY KEY,
  case_id                BIGINT NOT NULL UNIQUE,
  beni_team_member_allotted VARCHAR(150) NULL,
  spoc_name              VARCHAR(150) NULL,
  spoc_number            VARCHAR(30) NULL,
  spoc_contacted_flag    BOOLEAN NULL,
  contact_1_nicu         VARCHAR(30) NULL,
  contact_2_home         VARCHAR(30) NULL,
  voice_note_or_wa_cert_received_at DATETIME NULL,
  hamper_sent_date       DATE NULL,
  created_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_beni_case
    FOREIGN KEY (case_id) REFERENCES case_master(case_id)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS followup_event (
  followup_id            BIGINT AUTO_INCREMENT PRIMARY KEY,
  case_id                BIGINT NOT NULL,
  milestone_months       ENUM('3','6','9','12','18','24') NOT NULL,
  due_date               DATE NULL,
  reached_flag           BOOLEAN NOT NULL DEFAULT FALSE,
  reached_at             DATETIME NULL,
  conducted_by           BIGINT NULL,
  remarks                TEXT NULL,
  created_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_followup_case
    FOREIGN KEY (case_id) REFERENCES case_master(case_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_followup_user
    FOREIGN KEY (conducted_by) REFERENCES app_user(user_id)
    ON UPDATE CASCADE ON DELETE SET NULL,
  UNIQUE KEY uq_case_milestone (case_id, milestone_months),
  INDEX idx_followup_due (milestone_months, due_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS followup_metric_def (
  metric_def_id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  milestone_months       ENUM('3','6','9','12','18','24') NOT NULL,
  template_global_index  INT NULL,
  template_column_key    VARCHAR(50) NULL,   -- e.g., "Unnamed: 24"
  metric_label           VARCHAR(255) NOT NULL,
  value_type             ENUM('BOOLEAN','NUMBER','TEXT','DATE') NOT NULL DEFAULT 'BOOLEAN',
  active_flag            BOOLEAN NOT NULL DEFAULT TRUE,
  created_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_metric_def_milestone (milestone_months, active_flag),
  INDEX idx_metric_def_template (template_column_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS followup_metric_value (
  metric_value_id        BIGINT AUTO_INCREMENT PRIMARY KEY,
  followup_id            BIGINT NOT NULL,
  metric_def_id          BIGINT NOT NULL,
  value_bool             BOOLEAN NULL,
  value_number           DECIMAL(10,2) NULL,
  value_text             TEXT NULL,
  value_date             DATE NULL,
  created_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_metric_value_followup
    FOREIGN KEY (followup_id) REFERENCES followup_event(followup_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_metric_value_def
    FOREIGN KEY (metric_def_id) REFERENCES followup_metric_def(metric_def_id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  UNIQUE KEY uq_followup_metric (followup_id, metric_def_id),
  INDEX idx_metric_value_followup (followup_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 6) COMMUNICATION ARTIFACTS (TRANSCRIPTS, NOTES)
-- ============================================================

CREATE TABLE IF NOT EXISTS communication_artifact (
  artifact_id            BIGINT AUTO_INCREMENT PRIMARY KEY,
  case_id                BIGINT NOT NULL,
  artifact_type          ENUM('CALL_TRANSCRIPT','MEETING_TRANSCRIPT','MEETING_NOTES','OTHER') NOT NULL,
  document_id            BIGINT NOT NULL,
  created_by             BIGINT NOT NULL,
  created_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_artifact_case
    FOREIGN KEY (case_id) REFERENCES case_master(case_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_artifact_doc
    FOREIGN KEY (document_id) REFERENCES document_metadata(document_id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT fk_artifact_user
    FOREIGN KEY (created_by) REFERENCES app_user(user_id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  INDEX idx_artifact_case (case_id, artifact_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 7) DONOR, STORY, PAYMENT, LINKS
-- ============================================================

CREATE TABLE IF NOT EXISTS donor (
  donor_id               BIGINT AUTO_INCREMENT PRIMARY KEY,
  donor_name             VARCHAR(200) NOT NULL,
  donor_type             ENUM('Individual','Corporate','CSR','Foundation','Other') NOT NULL DEFAULT 'Other',
  contact_email          VARCHAR(200) NULL,
  active_flag            BOOLEAN NOT NULL DEFAULT TRUE,
  created_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_donor_name (donor_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS case_donor_map (
  case_donor_id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  case_id                BIGINT NOT NULL,
  donor_id               BIGINT NOT NULL,
  mapped_amount          DECIMAL(12,2) NULL,
  created_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_case_donor_case
    FOREIGN KEY (case_id) REFERENCES case_master(case_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_case_donor_donor
    FOREIGN KEY (donor_id) REFERENCES donor(donor_id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  UNIQUE KEY uq_case_donor (case_id, donor_id),
  INDEX idx_case_donor_donor (donor_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS hope_story (
  hope_story_id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  case_id                BIGINT NOT NULL UNIQUE,
  status                 ENUM('Not_Started','In_Progress','Published') NOT NULL DEFAULT 'Not_Started',
  story_link             VARCHAR(500) NULL,
  created_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_story_case
    FOREIGN KEY (case_id) REFERENCES case_master(case_id)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS payment (
  payment_id             BIGINT AUTO_INCREMENT PRIMARY KEY,
  case_id                BIGINT NOT NULL,
  due_date               DATE NULL,
  status                 ENUM('Pending','Initiated','Paid','Failed','On_Hold') NOT NULL DEFAULT 'Pending',
  amount                 DECIMAL(12,2) NULL,
  reference_no           VARCHAR(100) NULL,
  remarks                TEXT NULL,
  created_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_payment_case
    FOREIGN KEY (case_id) REFERENCES case_master(case_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  INDEX idx_payment_status (status, due_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS external_link (
  link_id                BIGINT AUTO_INCREMENT PRIMARY KEY,
  case_id                BIGINT NOT NULL,
  link_type              ENUM('BENEFICIARY_FOLDER','HOPE_STORY','OTHER') NOT NULL,
  url                    VARCHAR(800) NOT NULL,
  created_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_link_case
    FOREIGN KEY (case_id) REFERENCES case_master(case_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  INDEX idx_link_type (link_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 8) AUDIT LOG
-- ============================================================

CREATE TABLE IF NOT EXISTS audit_log (
  audit_id               BIGINT AUTO_INCREMENT PRIMARY KEY,
  entity_type            VARCHAR(50) NOT NULL,
  entity_id              BIGINT NOT NULL,
  action                 VARCHAR(100) NOT NULL,
  actor_user_id          BIGINT NOT NULL,
  event_at               DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  details_json           JSON NULL,
  CONSTRAINT fk_audit_user
    FOREIGN KEY (actor_user_id) REFERENCES app_user(user_id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  INDEX idx_audit_entity (entity_type, entity_id),
  INDEX idx_audit_time (event_at),
  INDEX idx_audit_actor (actor_user_id, event_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 9) REPORTING / TEMPLATE-PARITY VIEWS
-- ============================================================

-- Helper derived table (Aadhaar status per case)
-- Rule requested:
--   Export as: 'No' / 'Yes - Uploaded' / 'Yes - Verified'
-- We treat Aadhaar as present if any GENERAL doc_type contains 'Aadhaar'/'Aadhar'
-- Verified means at least two verified Aadhaar docs OR at least one verified + explicit checklist verified.
-- (Developers can tighten doc_type matching to exact catalog later.)

CREATE OR REPLACE VIEW vw_case_aadhaar_status AS
SELECT
  d.case_id,
  SUM(CASE WHEN d.document_category='GENERAL'
            AND (d.document_type LIKE '%Aadhaar%' OR d.document_type LIKE '%Aadhar%')
            AND d.active_flag=TRUE
           THEN 1 ELSE 0 END) AS aadhaar_uploaded_count,
  SUM(CASE WHEN d.document_category='GENERAL'
            AND (d.document_type LIKE '%Aadhaar%' OR d.document_type LIKE '%Aadhar%')
            AND d.active_flag=TRUE
            AND d.is_verified=TRUE
           THEN 1 ELSE 0 END) AS aadhaar_verified_count
FROM document_metadata d
WHERE d.case_id IS NOT NULL
GROUP BY d.case_id;

-- Aggregate committee dates & comments per case
CREATE OR REPLACE VIEW vw_case_committee_agg AS
SELECT
  cd.case_id,
  MAX(CASE WHEN cd.committee_type='BRC'  THEN cd.submission_date END) AS brc_submission_date,
  MAX(CASE WHEN cd.committee_type='BRC'  THEN cd.approval_date   END) AS brc_approval_date,
  MAX(CASE WHEN cd.committee_type='BRRC' THEN cd.submission_date END) AS brrc_submission_date,
  MAX(CASE WHEN cd.committee_type='BRRC' THEN cd.approval_date   END) AS brrc_approval_date,
  MAX(CASE WHEN cd.committee_type='BGRC' THEN cd.submission_date END) AS bgrc_submission_date,
  MAX(CASE WHEN cd.committee_type='BGRC' THEN cd.approval_date   END) AS bgrc_approval_date,
  MAX(CASE WHEN cd.committee_type='BGRC' THEN cd.comments END)         AS bgrc_comments
FROM committee_decision cd
GROUP BY cd.case_id;

-- Pivot funding installments per case (NFI 0..3 + BGRC 0)
CREATE OR REPLACE VIEW vw_case_funding_agg AS
SELECT
  cd.case_id,
  MAX(CASE WHEN cfi.funding_source='NFI'  AND cfi.installment_no=0 THEN cfi.amount_approved END) AS nfi_amt_0,
  MAX(CASE WHEN cfi.funding_source='NFI'  AND cfi.installment_no=1 THEN cfi.amount_approved END) AS nfi_amt_1,
  MAX(CASE WHEN cfi.funding_source='NFI'  AND cfi.installment_no=2 THEN cfi.amount_approved END) AS nfi_amt_2,
  MAX(CASE WHEN cfi.funding_source='NFI'  AND cfi.installment_no=3 THEN cfi.amount_approved END) AS nfi_amt_3,
  MAX(CASE WHEN cfi.funding_source='BGRC' AND cfi.installment_no=0 THEN cfi.amount_approved END) AS bgrc_amt_0
FROM committee_decision cd
LEFT JOIN committee_funding_installment cfi ON cfi.decision_id = cd.decision_id
GROUP BY cd.case_id;

-- ------------------------------------------------------------
-- WDG Parity View: Beneficiary Master Data (53 columns)
-- ------------------------------------------------------------
CREATE OR REPLACE VIEW vw_beneficiary_master_data AS
SELECT
  -- 1 FY Sl No (computed)
  ROW_NUMBER() OVER (ORDER BY cm.intake_date, cm.case_id)                                       AS `FY Sl No`,
  -- 2 NFI BN
  cp.beneficiary_no                                                                             AS `NFI BN`,
  -- 3 Name of the Beneficiary
  cp.beneficiary_name                                                                           AS `Name of the Beneficiary`,
  -- 4 Mortality
  cp.mortality                                                                                  AS `Mortality`,
  -- 5 Gender
  cp.gender                                                                                     AS `Gender`,
  -- 6 Date of Birth
  cp.date_of_birth                                                                              AS `Date of Birth`,
  -- 7 Birth Status
  cp.birth_status                                                                               AS `Birth Status`,
  -- 8 Date of Admission
  cp.admission_date                                                                             AS `Date of Admission`,
  -- 9 Gestational Age
  cp.gestational_age_weeks                                                                      AS `Gestational Age`,
  -- 10 Weight at Birth (in Kgs)
  cp.birth_weight_kg                                                                            AS `Weight at Birth (in Kgs)`,
  -- 11 Hospital
  h.hospital_name                                                                               AS `Hospital`,
  -- 12 Current Weight (in Kgs)
  cp.current_weight_kg                                                                          AS `Current Weight (in Kgs)`,
  -- 13 Morbidity
  cp.morbidity                                                                                  AS `Morbidity`,
  -- 14 Date of Closure
  COALESCE(cp.date_of_closure, cm.closure_date)                                                  AS `Date of Closure`,
  -- 15 Duration of Stay in NICU (days)
  cp.nicu_stay_days                                                                             AS `Duration of Stay in NICU (days)`,

  -- 16 Date of Submission to BRC
  cca.brc_submission_date                                                                       AS `Date of Submission to BRC`,
  -- 17 Date of Approval from BRC
  cca.brc_approval_date                                                                         AS `Date of Approval from BRC`,
  -- 18 Status of the Case - At the NFI Level
  cm.status_nfi_level                                                                           AS `Status of the Case - At the NFI Level`,
  -- 19 Case Category
  cm.case_category                                                                              AS `Case Category`,
  -- 20 Primary Diagnosis
  ccd.primary_diagnosis                                                                         AS `Primary Diagnosis`,
  -- 21 Case Summary
  ccd.case_summary_0                                                                            AS `Case Summary`,
  -- 22 Case Summary.1
  ccd.case_summary_1                                                                            AS `Case Summary.1`,
  -- 23 Case Summary.2
  ccd.case_summary_2                                                                            AS `Case Summary.2`,
  -- 24 Case Summary.3
  ccd.case_summary_3                                                                            AS `Case Summary.3`,

  -- 25 Medical bill Estimate
  fcd.medical_bill_estimate                                                                     AS `Medical bill Estimate`,

  -- 26 Amount Approved from NFI
  cfa.nfi_amt_0                                                                                 AS `Amount Approved from NFI`,
  -- 27 Amount Approved from NFI.1
  cfa.nfi_amt_1                                                                                 AS `Amount Approved from NFI.1`,
  -- 28 Amount Approved from NFI.2
  cfa.nfi_amt_2                                                                                 AS `Amount Approved from NFI.2`,
  -- 29 Amount Approved from NFI.3
  cfa.nfi_amt_3                                                                                 AS `Amount Approved from NFI.3`,

  -- 30 Total Amount Approved (sum all known)
  (COALESCE(cfa.nfi_amt_0,0)+COALESCE(cfa.nfi_amt_1,0)+COALESCE(cfa.nfi_amt_2,0)+COALESCE(cfa.nfi_amt_3,0)+COALESCE(cfa.bgrc_amt_0,0)) AS `Total Amount Approved`,
  -- 31 Total Amount Approved.1 (cumulative to installment 1)
  (COALESCE(cfa.nfi_amt_0,0)+COALESCE(cfa.nfi_amt_1,0))                                         AS `Total Amount Approved.1`,
  -- 32 Total Amount Approved.2 (cumulative to installment 2)
  (COALESCE(cfa.nfi_amt_0,0)+COALESCE(cfa.nfi_amt_1,0)+COALESCE(cfa.nfi_amt_2,0))               AS `Total Amount Approved.2`,
  -- 33 Total Amount Approved.3 (cumulative to installment 3)
  (COALESCE(cfa.nfi_amt_0,0)+COALESCE(cfa.nfi_amt_1,0)+COALESCE(cfa.nfi_amt_2,0)+COALESCE(cfa.nfi_amt_3,0)) AS `Total Amount Approved.3`,

  -- 34 Status of the Case - At the BRC Level
  cm.status_committee_level                                                                     AS `Status of the Case - At the BRC Level`,
  -- 35 Date of Submitting to BRRC
  cca.brrc_submission_date                                                                      AS `Date of Submitting to BRRC`,
  -- 36 Date of Approval from BRRC
  cca.brrc_approval_date                                                                        AS `Date of Approval from BRRC`,
  -- 37 Date of Submission to BGRC
  cca.bgrc_submission_date                                                                      AS `Date of Submission to BGRC`,
  -- 38 Date of Approval from BGRC
  cca.bgrc_approval_date                                                                        AS `Date of Approval from BGRC`,
  -- 39 Amount Approved from BGRC
  cfa.bgrc_amt_0                                                                                AS `Amount Approved from BGRC`,
  -- 40 BGRC Comments
  cca.bgrc_comments                                                                             AS `BGRC Comments`,

  -- 41 Name of the Father
  fp.father_name                                                                                AS `Name of the Father`,
  -- 42 Contact Number of the Father
  fp.father_phone                                                                               AS `Contact Number of the Father`,
  -- 43 Name of the Mother
  fp.mother_name                                                                                AS `Name of the Mother`,
  -- 44 Contact Number of the Mother
  fp.mother_phone                                                                               AS `Contact Number of the Mother`,
  -- 45 Email id
  fp.email_id                                                                                   AS `Email id`,
  -- 46 Address
  fp.address                                                                                    AS `Address`,

  -- 47 Aadhar Cards - Father and Mother (requested export: No / Yes - Uploaded / Yes - Verified)
  CASE
    WHEN COALESCE(acs.aadhaar_uploaded_count,0) = 0 THEN 'No'
    WHEN COALESCE(acs.aadhaar_verified_count,0) >= 2 THEN 'Yes - Verified'
    ELSE 'Yes - Uploaded'
  END                                                                                           AS `Aadhar Cards - Father and Mother`,

  -- 48 Employment Profile of the Father
  fp.father_employment_profile                                                                  AS `Employment Profile of the Father`,
  -- 49 Employment Profile of the Father.1
  fp.father_employment_profile_alt                                                              AS `Employment Profile of the Father.1`,
  -- 50 Total Monthly Income of the Family
  fp.total_monthly_income                                                                       AS `Total Monthly Income of the Family`,
  -- 51 Reason for Case Rejection (Medical/Financial)
  rd.rejection_reason_category                                                                   AS `Reason for Case Rejection (Medical/Financial)`,
  -- 52 Status of Rejection (At NFI Level/ At BRC level)
  rd.rejection_level                                                                            AS `Status of Rejection (At NFI Level/ At BRC level)`,
  -- 53 Status of Rejection Communication
  rd.rejection_communication_status                                                              AS `Status of Rejection Communication`

FROM case_master cm
JOIN hospital h ON h.hospital_id = cm.hospital_id
LEFT JOIN child_profile cp ON cp.case_id = cm.case_id
LEFT JOIN family_profile fp ON fp.case_id = cm.case_id
LEFT JOIN clinical_case_details ccd ON ccd.case_id = cm.case_id
LEFT JOIN financial_case_details fcd ON fcd.case_id = cm.case_id
LEFT JOIN rejection_details rd ON rd.case_id = cm.case_id
LEFT JOIN vw_case_committee_agg cca ON cca.case_id = cm.case_id
LEFT JOIN vw_case_funding_agg cfa ON cfa.case_id = cm.case_id
LEFT JOIN vw_case_aadhaar_status acs ON acs.case_id = cm.case_id;

-- ------------------------------------------------------------
-- WDG Parity View: Rejection Master Data (10 columns)
-- ------------------------------------------------------------
CREATE OR REPLACE VIEW vw_rejection_master_data AS
SELECT
  ROW_NUMBER() OVER (ORDER BY rd.rejection_id) AS `Sl No`,
  cp.beneficiary_name                          AS `Name of the Rejected Beneficiary`,
  rd.referring_hospital                        AS `Refering Hospital`,
  rd.rejection_case_summary                    AS `Case Summary`,
  fp.father_name                               AS `Name of the Father`,
  fp.father_employment_profile                 AS `Employment Profile of the Father`,
  fp.total_monthly_income                      AS `Total Monthly Income of the Family`,
  rd.rejection_reason_category                 AS `Reason for Case Rejection (Medical/Financial)`,
  rd.rejection_level                           AS `Status of Rejection (At NFI Level/ At BRC level)`,
  rd.rejection_communication_status            AS `Status of Rejection Communication`
FROM rejection_details rd
JOIN case_master cm ON cm.case_id = rd.case_id
LEFT JOIN child_profile cp ON cp.case_id = cm.case_id
LEFT JOIN family_profile fp ON fp.case_id = cm.case_id;

-- ------------------------------------------------------------
-- OPTIONAL: WIDE BIMS/BENI export view (skeleton)
-- Recommended to keep normalized tables for followups; implement this view only if needed.
-- ------------------------------------------------------------
-- CREATE OR REPLACE VIEW vw_bims_beni_master_data AS
-- SELECT
--   cm.case_reference_no,
--   cp.beneficiary_no,
--   cp.beneficiary_name,
--   cp.gender,
--   cp.date_of_birth,
--   bop.beni_team_member_allotted,
--   bop.spoc_name,
--   bop.spoc_number,
--   bop.hamper_sent_date,
--   -- Example: milestone completion flags
--   MAX(CASE WHEN fe.milestone_months='3'  THEN fe.reached_flag END)  AS reached_3m,
--   MAX(CASE WHEN fe.milestone_months='6'  THEN fe.reached_flag END)  AS reached_6m,
--   MAX(CASE WHEN fe.milestone_months='9'  THEN fe.reached_flag END)  AS reached_9m,
--   MAX(CASE WHEN fe.milestone_months='12' THEN fe.reached_flag END)  AS reached_12m,
--   MAX(CASE WHEN fe.milestone_months='18' THEN fe.reached_flag END)  AS reached_18m,
--   MAX(CASE WHEN fe.milestone_months='24' THEN fe.reached_flag END)  AS reached_24m
-- FROM case_master cm
-- LEFT JOIN child_profile cp ON cp.case_id=cm.case_id
-- LEFT JOIN beni_program_ops bop ON bop.case_id=cm.case_id
-- LEFT JOIN followup_event fe ON fe.case_id=cm.case_id
-- GROUP BY cm.case_id;

-- ============================================================
-- 10) TRIGGERS (optional) - enforce is_locked when verified
-- ============================================================
-- Note: Keep business logic in API for MVP; enable trigger only if desired.
-- DELIMITER $$
-- CREATE TRIGGER trg_doc_lock_on_verify
-- BEFORE UPDATE ON document_metadata
-- FOR EACH ROW
-- BEGIN
--   IF NEW.is_verified = TRUE AND OLD.is_verified = FALSE THEN
--     SET NEW.is_locked = TRUE;
--   END IF;
-- END$$
-- DELIMITER ;

-- Restore checks
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

-- ====================== END OF SCRIPT =======================

CREATE TABLE `case_sequence` (
  `seq_year` int NOT NULL,
  `next_val` bigint DEFAULT NULL,
  PRIMARY KEY (`seq_year`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


INSERT INTO `nfi_cmdws`.`case_sequence` (`seq_year`, `next_val`) VALUES ('2026', '1');


INSERT INTO `nfi_cmdws`.`hospital` (`hospital_id`, `hospital_uuid`, `hospital_name`, `hospital_code`, `hospital_type`, `city`, `state`, `spoc_name`, `spoc_phone`, `active_flag`) VALUES ('1', 'qweq', 'Rainbow Vhildrens ', '001', 'BRC', 'Hyd', 'Tel', 'testSpoc', '12345', '1');

INSERT INTO `nfi_cmdws`.`app_user` (`user_id`, `user_uuid`, `full_name`, `email`, `phone`, `primary_role`, `hospital_id`, `active_flag`) VALUES ('12345', '123', 'Test User 1 ', '1@nfi.com', '123456', 'HOSPITAL', '1', '1');

COMMIT;