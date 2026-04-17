-- ============================================================================
-- NFI BMS / CMDWS
-- MySQL 8.x Schema Script
-- Version: 1.2 Final
-- Date: 09-Apr-2026
-- Source of truth: Data Dictionary v1.2 Final
-- Notes:
--   * This is a clean-install script for development / fresh environment setup.
--   * It DROPS and recreates the target schema.
--   * Object-storage bytes are not stored in MySQL; only metadata/pointers are stored.
-- ============================================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP DATABASE IF EXISTS `nfi_bms_cmdws`;
CREATE DATABASE `nfi_bms_cmdws`
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
USE `nfi_bms_cmdws`;

-- ============================================================================
-- 1. MASTER / ACCESS CONTROL
-- ============================================================================

CREATE TABLE `hospital` (
  `hospital_id` BIGINT NOT NULL AUTO_INCREMENT,
  `hospital_uuid` CHAR(36) NOT NULL,
  `hospital_name` VARCHAR(200) NOT NULL,
  `hospital_type` ENUM('BRC','BRRC','BGRC','BCRC','NON_BRC') NOT NULL,
  `city` VARCHAR(100) DEFAULT NULL,
  `state` VARCHAR(100) DEFAULT NULL,
  `spoc_name` VARCHAR(150) DEFAULT NULL,
  `spoc_phone` VARCHAR(30) DEFAULT NULL,
  `active_flag` BOOLEAN NOT NULL DEFAULT TRUE,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`hospital_id`),
  UNIQUE KEY `uq_hospital_uuid` (`hospital_uuid`),
  KEY `idx_hospital_name` (`hospital_name`),
  KEY `idx_hospital_type` (`hospital_type`),
  KEY `idx_hospital_location` (`state`,`city`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `hospital_process_map` (
  `hospital_process_id` BIGINT NOT NULL AUTO_INCREMENT,
  `hospital_id` BIGINT NOT NULL,
  `process_type` ENUM('BRC','BRRC','BGRC','BCRC','NON_BRC') NOT NULL,
  `effective_from` DATE NOT NULL,
  `effective_to` DATE DEFAULT NULL,
  `active_flag` BOOLEAN NOT NULL DEFAULT TRUE,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`hospital_process_id`),
  KEY `idx_hospital_process_map_hospital` (`hospital_id`),
  KEY `idx_hospital_process_map_active` (`active_flag`,`process_type`),
  CONSTRAINT `fk_hospital_process_map_hospital`
    FOREIGN KEY (`hospital_id`) REFERENCES `hospital` (`hospital_id`)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `app_user` (
  `user_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_uuid` CHAR(36) NOT NULL,
  `full_name` VARCHAR(150) NOT NULL,
  `email` VARCHAR(200) NOT NULL,
  `phone` VARCHAR(30) DEFAULT NULL,
  `primary_role` ENUM('Hospital','Verifier','Clinical_Reviewer','Panel','Volunteer','Accounts','Leadership','Admin') NOT NULL,
  `hospital_id` BIGINT DEFAULT NULL,
  `ui_language_code` VARCHAR(10) NOT NULL DEFAULT 'en',
  `active_flag` BOOLEAN NOT NULL DEFAULT TRUE,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uq_app_user_uuid` (`user_uuid`),
  UNIQUE KEY `uq_app_user_email` (`email`),
  KEY `idx_app_user_primary_role` (`primary_role`),
  KEY `idx_app_user_hospital` (`hospital_id`),
  CONSTRAINT `fk_app_user_hospital`
    FOREIGN KEY (`hospital_id`) REFERENCES `hospital` (`hospital_id`)
    ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `user_role_map` (
  `user_role_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `role` ENUM('Hospital','Verifier','Clinical_Reviewer','Social_Reviewer','Financial_Reviewer','Volunteer','Accounts','Leadership','Admin') NOT NULL,
  `scope_hospital_id` BIGINT DEFAULT NULL,
  `active_flag` BOOLEAN NOT NULL DEFAULT TRUE,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_role_id`),
  KEY `idx_user_role_map_user` (`user_id`),
  KEY `idx_user_role_map_role` (`role`),
  KEY `idx_user_role_map_scope_hospital` (`scope_hospital_id`),
  CONSTRAINT `fk_user_role_map_user`
    FOREIGN KEY (`user_id`) REFERENCES `app_user` (`user_id`)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `fk_user_role_map_scope_hospital`
    FOREIGN KEY (`scope_hospital_id`) REFERENCES `hospital` (`hospital_id`)
    ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `user_language_expertise` (
  `user_language_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `language_code` VARCHAR(10) NOT NULL,
  `proficiency_level` ENUM('Basic','Conversational','Fluent','Native') NOT NULL,
  `is_primary_flag` BOOLEAN NOT NULL DEFAULT FALSE,
  `active_flag` BOOLEAN NOT NULL DEFAULT TRUE,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_language_id`),
  KEY `idx_user_language_expertise_user` (`user_id`),
  KEY `idx_user_language_expertise_lang` (`language_code`,`proficiency_level`),
  CONSTRAINT `fk_user_language_expertise_user`
    FOREIGN KEY (`user_id`) REFERENCES `app_user` (`user_id`)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 2. CASE / STRUCTURED INTAKE
-- ============================================================================

CREATE TABLE `case_master` (
  `case_id` BIGINT NOT NULL AUTO_INCREMENT,
  `case_uuid` CHAR(36) NOT NULL,
  `case_reference_no` BIGINT DEFAULT NULL COMMENT 'System-generated global serial; typically mirrors or is assigned from service layer.',
  `hospital_id` BIGINT NOT NULL,
  `process_type` ENUM('BRC','BRRC','BGRC','BCRC','NON_BRC') NOT NULL,
  `case_status` ENUM('Draft','Submitted','Returned','Under_Verification','Under_Review','Approved','Rejected','Closed') NOT NULL DEFAULT 'Draft',
  `status_nfi_level` VARCHAR(100) DEFAULT NULL,
  `status_panel_level` VARCHAR(100) DEFAULT NULL,
  `case_category` VARCHAR(100) DEFAULT NULL,
  `intake_date` DATE NOT NULL,
  `approval_date` DATE DEFAULT NULL,
  `rejection_date` DATE DEFAULT NULL,
  `closure_date` DATE DEFAULT NULL,
  `created_by` BIGINT NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`case_id`),
  UNIQUE KEY `uq_case_master_uuid` (`case_uuid`),
  UNIQUE KEY `uq_case_master_reference_no` (`case_reference_no`),
  KEY `idx_case_master_hospital` (`hospital_id`),
  KEY `idx_case_master_process` (`process_type`),
  KEY `idx_case_master_status` (`case_status`),
  KEY `idx_case_master_dates` (`intake_date`,`approval_date`,`closure_date`),
  CONSTRAINT `fk_case_master_hospital`
    FOREIGN KEY (`hospital_id`) REFERENCES `hospital` (`hospital_id`)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT `fk_case_master_created_by`
    FOREIGN KEY (`created_by`) REFERENCES `app_user` (`user_id`)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `child_profile` (
  `child_id` BIGINT NOT NULL AUTO_INCREMENT,
  `case_id` BIGINT NOT NULL,
  `beneficiary_no` VARCHAR(30) DEFAULT NULL COMMENT 'Allocated only after approval. Format: NFI-BN-YYYY-######',
  `display_name` VARCHAR(150) NOT NULL,
  `gender` ENUM('Male','Female','Other','Unknown') NOT NULL DEFAULT 'Unknown',
  `date_of_birth` DATE NOT NULL,
  `birth_status` ENUM('Inborn','Outborn','Unknown') DEFAULT NULL,
  `birth_hospital_name` VARCHAR(200) DEFAULT NULL,
  `admission_date` DATE DEFAULT NULL,
  `gestational_age_weeks` DECIMAL(5,2) DEFAULT NULL,
  `birth_weight_kg` DECIMAL(5,2) DEFAULT NULL,
  `current_weight_kg` DECIMAL(5,2) DEFAULT NULL,
  `nicu_stay_days` INT DEFAULT NULL,
  `mortality_status` ENUM('Alive','Expired_In_Hospital','Expired_Post_Discharge','Unknown') NOT NULL DEFAULT 'Unknown',
  `morbidity` VARCHAR(200) DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`child_id`),
  UNIQUE KEY `uq_child_profile_case_id` (`case_id`),
  UNIQUE KEY `uq_child_profile_beneficiary_no` (`beneficiary_no`),
  KEY `idx_child_profile_dob` (`date_of_birth`),
  KEY `idx_child_profile_mortality_status` (`mortality_status`),
  CONSTRAINT `fk_child_profile_case`
    FOREIGN KEY (`case_id`) REFERENCES `case_master` (`case_id`)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `family_profile` (
  `family_id` BIGINT NOT NULL AUTO_INCREMENT,
  `case_id` BIGINT NOT NULL,
  `father_name` VARCHAR(150) DEFAULT NULL,
  `father_date_of_birth` DATE DEFAULT NULL,
  `father_phone` VARCHAR(30) DEFAULT NULL,
  `father_education` VARCHAR(100) DEFAULT NULL,
  `father_occupation` VARCHAR(150) DEFAULT NULL,
  `father_employer_name` VARCHAR(150) DEFAULT NULL,
  `father_monthly_income` DECIMAL(12,2) DEFAULT NULL,
  `father_daily_wage` DECIMAL(12,2) DEFAULT NULL,
  `mother_name` VARCHAR(150) DEFAULT NULL,
  `mother_date_of_birth` DATE DEFAULT NULL,
  `mother_phone` VARCHAR(30) DEFAULT NULL,
  `mother_education` VARCHAR(100) DEFAULT NULL,
  `mother_occupation` VARCHAR(150) DEFAULT NULL,
  `mother_employer_name` VARCHAR(150) DEFAULT NULL,
  `mother_monthly_income` DECIMAL(12,2) DEFAULT NULL,
  `mother_daily_wage` DECIMAL(12,2) DEFAULT NULL,
  `date_of_marriage` DATE DEFAULT NULL,
  `years_married` DECIMAL(5,2) DEFAULT NULL,
  `email_id` VARCHAR(200) DEFAULT NULL,
  `address` TEXT DEFAULT NULL,
  `city` VARCHAR(100) DEFAULT NULL,
  `state` VARCHAR(100) DEFAULT NULL,
  `pincode` VARCHAR(15) DEFAULT NULL,
  `family_members_count` INT DEFAULT NULL,
  `total_family_income` DECIMAL(12,2) DEFAULT NULL,
  `income_capture_basis` ENUM('Monthly','Daily_Wage','Mixed','Unknown') NOT NULL DEFAULT 'Unknown',
  `income_threshold_flag` BOOLEAN NOT NULL DEFAULT FALSE,
  `income_exception_review_required` BOOLEAN NOT NULL DEFAULT FALSE,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`family_id`),
  UNIQUE KEY `uq_family_profile_case_id` (`case_id`),
  KEY `idx_family_profile_income` (`total_family_income`,`income_threshold_flag`),
  CONSTRAINT `fk_family_profile_case`
    FOREIGN KEY (`case_id`) REFERENCES `case_master` (`case_id`)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `clinical_case_details` (
  `clinical_id` BIGINT NOT NULL AUTO_INCREMENT,
  `case_id` BIGINT NOT NULL,
  `primary_diagnosis` VARCHAR(200) DEFAULT NULL,
  `case_summary_0` TEXT DEFAULT NULL,
  `case_summary_1` TEXT DEFAULT NULL,
  `case_summary_2` TEXT DEFAULT NULL,
  `case_summary_3` TEXT DEFAULT NULL,
  `apgar_score` VARCHAR(50) DEFAULT NULL,
  `time_of_birth` TIME DEFAULT NULL,
  `place_of_birth` VARCHAR(150) DEFAULT NULL,
  `maternal_age_years` DECIMAL(5,2) DEFAULT NULL,
  `gravida` INT DEFAULT NULL,
  `para` INT DEFAULT NULL,
  `abortion_count` INT DEFAULT NULL,
  `living_children_count` INT DEFAULT NULL,
  `antenatal_risk_factors` TEXT DEFAULT NULL,
  `diagnosis_checklist` TEXT DEFAULT NULL,
  `treatment_given` TEXT DEFAULT NULL,
  `current_day_of_life` INT DEFAULT NULL,
  `corrected_gestational_age_weeks` DECIMAL(5,2) DEFAULT NULL,
  `feeding_mode` VARCHAR(150) DEFAULT NULL,
  `respiration_mode` VARCHAR(150) DEFAULT NULL,
  `discharge_plan` TEXT DEFAULT NULL,
  `investigation_attached_flag` BOOLEAN NOT NULL DEFAULT FALSE,
  `interim_summary_complete_flag` BOOLEAN NOT NULL DEFAULT FALSE,
  `remarks` TEXT DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`clinical_id`),
  UNIQUE KEY `uq_clinical_case_details_case_id` (`case_id`),
  KEY `idx_clinical_case_details_primary_diagnosis` (`primary_diagnosis`),
  CONSTRAINT `fk_clinical_case_details_case`
    FOREIGN KEY (`case_id`) REFERENCES `case_master` (`case_id`)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `financial_case_details` (
  `financial_id` BIGINT NOT NULL AUTO_INCREMENT,
  `case_id` BIGINT NOT NULL,
  `income_proof_type` ENUM('Bank_Statement','Income_Certificate','Talati_Card','BPL_Card','Other') DEFAULT NULL,
  `bank_statement_months_count` TINYINT DEFAULT NULL,
  `economic_card_type` VARCHAR(100) DEFAULT NULL,
  `bpl_apl_status` ENUM('BPL','APL','Unknown') NOT NULL DEFAULT 'Unknown',
  `advance_paid_amount` DECIMAL(12,2) DEFAULT NULL,
  `medical_bill_estimate` DECIMAL(12,2) DEFAULT NULL,
  `estimated_nicu_days` INT DEFAULT NULL,
  `hospital_discount_amount` DECIMAL(12,2) DEFAULT NULL,
  `beneficiary_payable_balance` DECIMAL(12,2) DEFAULT NULL,
  `reduction_amount` DECIMAL(12,2) DEFAULT NULL,
  `reduction_notes` TEXT DEFAULT NULL,
  `sponsor_amount_quantified` DECIMAL(12,2) DEFAULT NULL,
  `sponsor_amount_final` DECIMAL(12,2) DEFAULT NULL,
  `total_amount_approved` DECIMAL(12,2) DEFAULT NULL,
  `financial_summary_snapshot` TEXT DEFAULT NULL,
  `manual_exception_notes` TEXT DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`financial_id`),
  UNIQUE KEY `uq_financial_case_details_case_id` (`case_id`),
  KEY `idx_financial_case_details_estimate` (`medical_bill_estimate`),
  CONSTRAINT `fk_financial_case_details_case`
    FOREIGN KEY (`case_id`) REFERENCES `case_master` (`case_id`)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `beneficiary_interview` (
  `interview_id` BIGINT NOT NULL AUTO_INCREMENT,
  `case_id` BIGINT NOT NULL,
  `interview_status` ENUM('Not_Started','Draft','Completed') NOT NULL DEFAULT 'Not_Started',
  `outcome` VARCHAR(100) DEFAULT NULL,
  `notes` TEXT DEFAULT NULL,
  `interviewed_by` BIGINT DEFAULT NULL,
  `interviewed_at` DATETIME DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`interview_id`),
  KEY `idx_beneficiary_interview_case` (`case_id`),
  KEY `idx_beneficiary_interview_status` (`interview_status`),
  CONSTRAINT `fk_beneficiary_interview_case`
    FOREIGN KEY (`case_id`) REFERENCES `case_master` (`case_id`)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `fk_beneficiary_interview_user`
    FOREIGN KEY (`interviewed_by`) REFERENCES `app_user` (`user_id`)
    ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `appeal_request` (
  `appeal_id` BIGINT NOT NULL AUTO_INCREMENT,
  `case_id` BIGINT NOT NULL,
  `appeal_status` ENUM('Draft','Submitted','Under_Review','Accepted','Rejected') NOT NULL DEFAULT 'Draft',
  `appeal_reason` TEXT DEFAULT NULL,
  `submitted_by` BIGINT DEFAULT NULL,
  `submitted_at` DATETIME DEFAULT NULL,
  `review_notes` TEXT DEFAULT NULL,
  `resolved_at` DATETIME DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`appeal_id`),
  KEY `idx_appeal_request_case` (`case_id`),
  KEY `idx_appeal_request_status` (`appeal_status`),
  CONSTRAINT `fk_appeal_request_case`
    FOREIGN KEY (`case_id`) REFERENCES `case_master` (`case_id`)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `fk_appeal_request_submitted_by`
    FOREIGN KEY (`submitted_by`) REFERENCES `app_user` (`user_id`)
    ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `bgrc_cycle` (
  `bgrc_cycle_id` BIGINT NOT NULL AUTO_INCREMENT,
  `cycle_month` TINYINT NOT NULL,
  `cycle_year` SMALLINT NOT NULL,
  `notes` TEXT DEFAULT NULL,
  `utilization_document_id` BIGINT DEFAULT NULL,
  `created_by` BIGINT NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`bgrc_cycle_id`),
  UNIQUE KEY `uq_bgrc_cycle_month_year` (`cycle_month`,`cycle_year`),
  KEY `idx_bgrc_cycle_created_by` (`created_by`),
  CONSTRAINT `fk_bgrc_cycle_created_by`
    FOREIGN KEY (`created_by`) REFERENCES `app_user` (`user_id`)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 3. DOCUMENTS / COMMUNICATIONS
-- ============================================================================

CREATE TABLE `document_metadata` (
  `document_id` BIGINT NOT NULL AUTO_INCREMENT,
  `document_uuid` CHAR(36) NOT NULL,
  `case_id` BIGINT DEFAULT NULL,
  `bgrc_cycle_id` BIGINT DEFAULT NULL,
  `document_category` ENUM('GENERAL','MEDICAL','FINANCIAL','FINAL','COMMUNICATION') NOT NULL,
  `document_type` VARCHAR(150) NOT NULL,
  `document_source` ENUM('Uploaded_File','Portal_Form_Output','Generated_Export','Transcript_Note') NOT NULL DEFAULT 'Uploaded_File',
  `file_name` VARCHAR(255) NOT NULL,
  `file_format` ENUM('PDF','JPG','PNG','DOC','DOCX','XLS','XLSX','TXT') NOT NULL,
  `file_size_kb` INT NOT NULL,
  `storage_path` VARCHAR(500) NOT NULL,
  `checksum_sha256` CHAR(64) DEFAULT NULL,
  `uploaded_by` BIGINT NOT NULL,
  `uploaded_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `immutable_flag` BOOLEAN NOT NULL DEFAULT TRUE,
  `is_verified` BOOLEAN NOT NULL DEFAULT FALSE,
  `verified_by` BIGINT DEFAULT NULL,
  `verified_at` DATETIME DEFAULT NULL,
  `verification_notes` TEXT DEFAULT NULL,
  `is_locked` BOOLEAN NOT NULL DEFAULT FALSE,
  `locked_reason` VARCHAR(300) DEFAULT NULL,
  `visibility_scope` ENUM('All_Internal','Medical_Only','Hospital_View','Internal_Only') NOT NULL DEFAULT 'All_Internal',
  `active_flag` BOOLEAN NOT NULL DEFAULT TRUE,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`document_id`),
  UNIQUE KEY `uq_document_metadata_uuid` (`document_uuid`),
  KEY `idx_document_metadata_case` (`case_id`),
  KEY `idx_document_metadata_bgrc_cycle` (`bgrc_cycle_id`),
  KEY `idx_document_metadata_category_type` (`document_category`,`document_type`),
  KEY `idx_document_metadata_visibility` (`visibility_scope`,`active_flag`),
  KEY `idx_document_metadata_uploaded_by` (`uploaded_by`),
  KEY `idx_document_metadata_verified_by` (`verified_by`),
  CONSTRAINT `fk_document_metadata_case`
    FOREIGN KEY (`case_id`) REFERENCES `case_master` (`case_id`)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `fk_document_metadata_bgrc_cycle`
    FOREIGN KEY (`bgrc_cycle_id`) REFERENCES `bgrc_cycle` (`bgrc_cycle_id`)
    ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT `fk_document_metadata_uploaded_by`
    FOREIGN KEY (`uploaded_by`) REFERENCES `app_user` (`user_id`)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT `fk_document_metadata_verified_by`
    FOREIGN KEY (`verified_by`) REFERENCES `app_user` (`user_id`)
    ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `document_versioning` (
  `document_version_id` BIGINT NOT NULL AUTO_INCREMENT,
  `document_id` BIGINT NOT NULL,
  `version_no` INT NOT NULL,
  `supersedes_version_id` BIGINT DEFAULT NULL,
  `storage_path` VARCHAR(500) NOT NULL,
  `file_name` VARCHAR(255) NOT NULL,
  `file_size_kb` INT NOT NULL,
  `checksum_sha256` CHAR(64) DEFAULT NULL,
  `created_by` BIGINT NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`document_version_id`),
  UNIQUE KEY `uq_document_versioning_doc_version` (`document_id`,`version_no`),
  KEY `idx_document_versioning_supersedes` (`supersedes_version_id`),
  KEY `idx_document_versioning_created_by` (`created_by`),
  CONSTRAINT `fk_document_versioning_document`
    FOREIGN KEY (`document_id`) REFERENCES `document_metadata` (`document_id`)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `fk_document_versioning_supersedes`
    FOREIGN KEY (`supersedes_version_id`) REFERENCES `document_versioning` (`document_version_id`)
    ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT `fk_document_versioning_created_by`
    FOREIGN KEY (`created_by`) REFERENCES `app_user` (`user_id`)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `document_requirement_template` (
  `requirement_id` BIGINT NOT NULL AUTO_INCREMENT,
  `process_type` ENUM('BRC','BRRC','BGRC','BCRC','NON_BRC') NOT NULL,
  `document_category` ENUM('GENERAL','MEDICAL','FINANCIAL','FINAL','COMMUNICATION') NOT NULL,
  `document_type` VARCHAR(150) NOT NULL,
  `mandatory_flag` BOOLEAN NOT NULL DEFAULT FALSE,
  `multiple_attachments_allowed` BOOLEAN NOT NULL DEFAULT FALSE,
  `condition_notes` VARCHAR(500) DEFAULT NULL,
  `folder_order` INT DEFAULT NULL,
  `visibility_scope` ENUM('All_Internal','Medical_Only','Hospital_View','Internal_Only') NOT NULL DEFAULT 'All_Internal',
  `active_flag` BOOLEAN NOT NULL DEFAULT TRUE,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`requirement_id`),
  KEY `idx_document_requirement_template_process` (`process_type`,`document_category`,`active_flag`),
  KEY `idx_document_requirement_template_visibility` (`visibility_scope`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `case_document_checklist_status` (
  `checklist_id` BIGINT NOT NULL AUTO_INCREMENT,
  `case_id` BIGINT NOT NULL,
  `requirement_id` BIGINT NOT NULL,
  `status` ENUM('Missing','Uploaded','Verified','Not_Applicable') NOT NULL DEFAULT 'Missing',
  `satisfied_by_document_id` BIGINT DEFAULT NULL,
  `last_updated_by` BIGINT NOT NULL,
  `last_updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`checklist_id`),
  UNIQUE KEY `uq_case_document_checklist_status_case_req` (`case_id`,`requirement_id`),
  KEY `idx_case_document_checklist_status_status` (`status`),
  KEY `idx_case_document_checklist_status_doc` (`satisfied_by_document_id`),
  CONSTRAINT `fk_case_document_checklist_status_case`
    FOREIGN KEY (`case_id`) REFERENCES `case_master` (`case_id`)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `fk_case_document_checklist_status_requirement`
    FOREIGN KEY (`requirement_id`) REFERENCES `document_requirement_template` (`requirement_id`)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT `fk_case_document_checklist_status_document`
    FOREIGN KEY (`satisfied_by_document_id`) REFERENCES `document_metadata` (`document_id`)
    ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT `fk_case_document_checklist_status_updated_by`
    FOREIGN KEY (`last_updated_by`) REFERENCES `app_user` (`user_id`)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `communication_artifact` (
  `artifact_id` BIGINT NOT NULL AUTO_INCREMENT,
  `case_id` BIGINT NOT NULL,
  `artifact_type` ENUM('CALL_TRANSCRIPT','MEETING_TRANSCRIPT','MEETING_NOTES','OTHER') NOT NULL,
  `document_id` BIGINT NOT NULL,
  `created_by` BIGINT NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`artifact_id`),
  KEY `idx_communication_artifact_case` (`case_id`),
  KEY `idx_communication_artifact_document` (`document_id`),
  KEY `idx_communication_artifact_created_by` (`created_by`),
  CONSTRAINT `fk_communication_artifact_case`
    FOREIGN KEY (`case_id`) REFERENCES `case_master` (`case_id`)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `fk_communication_artifact_document`
    FOREIGN KEY (`document_id`) REFERENCES `document_metadata` (`document_id`)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT `fk_communication_artifact_created_by`
    FOREIGN KEY (`created_by`) REFERENCES `app_user` (`user_id`)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `external_link` (
  `link_id` BIGINT NOT NULL AUTO_INCREMENT,
  `case_id` BIGINT NOT NULL,
  `link_type` ENUM('BENEFICIARY_FOLDER','HOPE_STORY','OTHER') NOT NULL,
  `url` VARCHAR(800) NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`link_id`),
  KEY `idx_external_link_case` (`case_id`),
  CONSTRAINT `fk_external_link_case`
    FOREIGN KEY (`case_id`) REFERENCES `case_master` (`case_id`)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `case_consent_record` (
  `consent_id` BIGINT NOT NULL AUTO_INCREMENT,
  `case_id` BIGINT NOT NULL,
  `consent_form_type` ENUM('Program','Treatment','Testimonial','Other') NOT NULL,
  `consent_document_id` BIGINT NOT NULL,
  `consent_language_code` VARCHAR(10) DEFAULT NULL,
  `consent_obtained_flag` BOOLEAN NOT NULL DEFAULT FALSE,
  `consent_obtained_at` DATETIME DEFAULT NULL,
  `consent_obtained_by` BIGINT DEFAULT NULL,
  `notes` TEXT DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`consent_id`),
  KEY `idx_case_consent_record_case` (`case_id`),
  KEY `idx_case_consent_record_document` (`consent_document_id`),
  KEY `idx_case_consent_record_obtained_by` (`consent_obtained_by`),
  CONSTRAINT `fk_case_consent_record_case`
    FOREIGN KEY (`case_id`) REFERENCES `case_master` (`case_id`)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `fk_case_consent_record_document`
    FOREIGN KEY (`consent_document_id`) REFERENCES `document_metadata` (`document_id`)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT `fk_case_consent_record_obtained_by`
    FOREIGN KEY (`consent_obtained_by`) REFERENCES `app_user` (`user_id`)
    ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `sponsor_quantification` (
  `quantification_id` BIGINT NOT NULL AUTO_INCREMENT,
  `case_id` BIGINT NOT NULL,
  `sponsor_program` ENUM('NICU_Support_Program','TBC','SRT','Other') NOT NULL,
  `reference_amount_basis` ENUM('Estimate_Bill','Approved_Amount','Other') NOT NULL,
  `recommended_sponsor_amount` DECIMAL(12,2) DEFAULT NULL,
  `justification_notes` TEXT DEFAULT NULL,
  `reference_document_id` BIGINT DEFAULT NULL,
  `quantified_by` BIGINT DEFAULT NULL,
  `quantified_at` DATETIME DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`quantification_id`),
  KEY `idx_sponsor_quantification_case` (`case_id`),
  KEY `idx_sponsor_quantification_document` (`reference_document_id`),
  KEY `idx_sponsor_quantification_user` (`quantified_by`),
  CONSTRAINT `fk_sponsor_quantification_case`
    FOREIGN KEY (`case_id`) REFERENCES `case_master` (`case_id`)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `fk_sponsor_quantification_document`
    FOREIGN KEY (`reference_document_id`) REFERENCES `document_metadata` (`document_id`)
    ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT `fk_sponsor_quantification_user`
    FOREIGN KEY (`quantified_by`) REFERENCES `app_user` (`user_id`)
    ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 4. REVIEW / APPROVAL / WORKFLOW
-- ============================================================================

CREATE TABLE `panel_assignment` (
  `panel_assignment_id` BIGINT NOT NULL AUTO_INCREMENT,
  `case_id` BIGINT NOT NULL,
  `panel_type` ENUM('Clinical','Social','Financial') NOT NULL,
  `reviewer_user_id` BIGINT NOT NULL,
  `assigned_by` BIGINT NOT NULL,
  `assignment_status` ENUM('Assigned','In_Progress','Completed','Reassigned','Waived') NOT NULL DEFAULT 'Assigned',
  `due_date` DATE DEFAULT NULL,
  `notes` TEXT DEFAULT NULL,
  `assigned_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`panel_assignment_id`),
  KEY `idx_panel_assignment_case_type` (`case_id`,`panel_type`),
  KEY `idx_panel_assignment_reviewer` (`reviewer_user_id`),
  KEY `idx_panel_assignment_status` (`assignment_status`),
  CONSTRAINT `fk_panel_assignment_case`
    FOREIGN KEY (`case_id`) REFERENCES `case_master` (`case_id`)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `fk_panel_assignment_reviewer`
    FOREIGN KEY (`reviewer_user_id`) REFERENCES `app_user` (`user_id`)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT `fk_panel_assignment_assigned_by`
    FOREIGN KEY (`assigned_by`) REFERENCES `app_user` (`user_id`)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `panel_review` (
  `panel_review_id` BIGINT NOT NULL AUTO_INCREMENT,
  `case_id` BIGINT NOT NULL,
  `panel_type` ENUM('Clinical','Social','Financial') NOT NULL,
  `reviewer_user_id` BIGINT NOT NULL,
  `recommendation` ENUM('Approve','Reject','Return','Pending','Exception') NOT NULL DEFAULT 'Pending',
  `remarks` TEXT DEFAULT NULL,
  `supporting_document_id` BIGINT DEFAULT NULL,
  `completed_flag` BOOLEAN NOT NULL DEFAULT FALSE,
  `completed_at` DATETIME DEFAULT NULL,
  `last_updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`panel_review_id`),
  UNIQUE KEY `uq_panel_review_case_panel_reviewer` (`case_id`,`panel_type`,`reviewer_user_id`),
  KEY `idx_panel_review_recommendation` (`recommendation`),
  KEY `idx_panel_review_document` (`supporting_document_id`),
  CONSTRAINT `fk_panel_review_case`
    FOREIGN KEY (`case_id`) REFERENCES `case_master` (`case_id`)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `fk_panel_review_reviewer`
    FOREIGN KEY (`reviewer_user_id`) REFERENCES `app_user` (`user_id`)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT `fk_panel_review_document`
    FOREIGN KEY (`supporting_document_id`) REFERENCES `document_metadata` (`document_id`)
    ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `panel_consolidation` (
  `panel_consolidation_id` BIGINT NOT NULL AUTO_INCREMENT,
  `case_id` BIGINT NOT NULL,
  `clinical_status` VARCHAR(50) DEFAULT NULL,
  `social_status` VARCHAR(50) DEFAULT NULL,
  `financial_status` VARCHAR(50) DEFAULT NULL,
  `consolidation_status` ENUM('Pending','Ready_For_Final_Approval','Conflicting','Returned') NOT NULL DEFAULT 'Pending',
  `summary_notes` TEXT DEFAULT NULL,
  `last_evaluated_at` DATETIME DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`panel_consolidation_id`),
  UNIQUE KEY `uq_panel_consolidation_case` (`case_id`),
  KEY `idx_panel_consolidation_status` (`consolidation_status`),
  CONSTRAINT `fk_panel_consolidation_case`
    FOREIGN KEY (`case_id`) REFERENCES `case_master` (`case_id`)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `fk_panel_consolidation_updated_by`
    FOREIGN KEY (`updated_by`) REFERENCES `app_user` (`user_id`)
    ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `case_decision` (
  `decision_id` BIGINT NOT NULL AUTO_INCREMENT,
  `case_id` BIGINT NOT NULL,
  `decision_context_type` ENUM('BRC','BRRC','BGRC','BCRC','Panel') NOT NULL,
  `submission_date` DATE DEFAULT NULL,
  `decision_date` DATE DEFAULT NULL,
  `outcome` ENUM('Approved','Rejected','Returned','Need_More_Info','Deferred') NOT NULL,
  `approved_amount_total` DECIMAL(12,2) DEFAULT NULL,
  `comments` TEXT DEFAULT NULL,
  `created_by` BIGINT NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`decision_id`),
  KEY `idx_case_decision_case` (`case_id`),
  KEY `idx_case_decision_outcome` (`outcome`),
  KEY `idx_case_decision_context` (`decision_context_type`),
  CONSTRAINT `fk_case_decision_case`
    FOREIGN KEY (`case_id`) REFERENCES `case_master` (`case_id`)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `fk_case_decision_created_by`
    FOREIGN KEY (`created_by`) REFERENCES `app_user` (`user_id`)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `case_funding_installment` (
  `funding_id` BIGINT NOT NULL AUTO_INCREMENT,
  `decision_id` BIGINT NOT NULL,
  `funding_source` ENUM('NFI','BGRC','Other') NOT NULL,
  `installment_no` TINYINT NOT NULL,
  `amount_approved` DECIMAL(12,2) NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`funding_id`),
  UNIQUE KEY `uq_case_funding_installment_decision_source_no` (`decision_id`,`funding_source`,`installment_no`),
  KEY `idx_case_funding_installment_source` (`funding_source`),
  CONSTRAINT `fk_case_funding_installment_decision`
    FOREIGN KEY (`decision_id`) REFERENCES `case_decision` (`decision_id`)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `rejection_details` (
  `rejection_id` BIGINT NOT NULL AUTO_INCREMENT,
  `case_id` BIGINT NOT NULL,
  `rejection_reason_category` ENUM('Medical','Financial','Other') NOT NULL,
  `rejection_level` VARCHAR(100) DEFAULT NULL,
  `rejection_communication_status` VARCHAR(100) DEFAULT NULL,
  `referring_hospital` VARCHAR(200) DEFAULT NULL,
  `rejection_case_summary` TEXT DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`rejection_id`),
  UNIQUE KEY `uq_rejection_details_case` (`case_id`),
  KEY `idx_rejection_details_category` (`rejection_reason_category`),
  CONSTRAINT `fk_rejection_details_case`
    FOREIGN KEY (`case_id`) REFERENCES `case_master` (`case_id`)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `workflow_status_history` (
  `workflow_id` BIGINT NOT NULL AUTO_INCREMENT,
  `case_id` BIGINT NOT NULL,
  `from_status` VARCHAR(100) DEFAULT NULL,
  `to_status` VARCHAR(100) NOT NULL,
  `changed_by` BIGINT NOT NULL,
  `actor_role` VARCHAR(100) DEFAULT NULL,
  `change_reason` VARCHAR(300) DEFAULT NULL,
  `changed_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`workflow_id`),
  KEY `idx_workflow_status_history_case` (`case_id`,`changed_at`),
  KEY `idx_workflow_status_history_to_status` (`to_status`),
  KEY `idx_workflow_status_history_changed_by` (`changed_by`),
  CONSTRAINT `fk_workflow_status_history_case`
    FOREIGN KEY (`case_id`) REFERENCES `case_master` (`case_id`)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `fk_workflow_status_history_changed_by`
    FOREIGN KEY (`changed_by`) REFERENCES `app_user` (`user_id`)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 5. SETTLEMENT / DONORS / VOLUNTEER OPS / OUTCOMES
-- ============================================================================

CREATE TABLE `settlement_closure` (
  `settlement_id` BIGINT NOT NULL AUTO_INCREMENT,
  `case_id` BIGINT NOT NULL,
  `discharge_date` DATE DEFAULT NULL,
  `final_bill_amount` DECIMAL(12,2) DEFAULT NULL,
  `reference_amount_basis` ENUM('Estimate_Bill','Approved_Amount','Other') NOT NULL,
  `variance_pct` DECIMAL(8,4) DEFAULT NULL,
  `variance_flag` BOOLEAN NOT NULL DEFAULT FALSE,
  `director_review_required_flag` BOOLEAN NOT NULL DEFAULT FALSE,
  `director_review_status` ENUM('Not_Required','Pending','Approved','Returned') NOT NULL DEFAULT 'Not_Required',
  `closure_ready_flag` BOOLEAN NOT NULL DEFAULT FALSE,
  `notes` TEXT DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`settlement_id`),
  UNIQUE KEY `uq_settlement_closure_case` (`case_id`),
  KEY `idx_settlement_closure_variance` (`variance_flag`,`director_review_required_flag`),
  CONSTRAINT `fk_settlement_closure_case`
    FOREIGN KEY (`case_id`) REFERENCES `case_master` (`case_id`)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `payment` (
  `payment_id` BIGINT NOT NULL AUTO_INCREMENT,
  `case_id` BIGINT NOT NULL,
  `due_date` DATE DEFAULT NULL,
  `status` ENUM('Pending','Partially_Paid','Paid','Failed','On_Hold') NOT NULL DEFAULT 'Pending',
  `amount` DECIMAL(12,2) DEFAULT NULL,
  `payment_date` DATE DEFAULT NULL,
  `reference_no` VARCHAR(100) DEFAULT NULL,
  `remarks` TEXT DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`payment_id`),
  KEY `idx_payment_case` (`case_id`),
  KEY `idx_payment_status` (`status`),
  KEY `idx_payment_due_date` (`due_date`),
  CONSTRAINT `fk_payment_case`
    FOREIGN KEY (`case_id`) REFERENCES `case_master` (`case_id`)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `donor` (
  `donor_id` BIGINT NOT NULL AUTO_INCREMENT,
  `donor_name` VARCHAR(200) NOT NULL,
  `donor_type` ENUM('Individual','Corporate','CSR','Foundation','Other') NOT NULL,
  `contact_email` VARCHAR(200) DEFAULT NULL,
  `active_flag` BOOLEAN NOT NULL DEFAULT TRUE,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`donor_id`),
  KEY `idx_donor_name` (`donor_name`),
  KEY `idx_donor_type` (`donor_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `case_donor_map` (
  `case_donor_id` BIGINT NOT NULL AUTO_INCREMENT,
  `case_id` BIGINT NOT NULL,
  `donor_id` BIGINT DEFAULT NULL,
  `sponsor_label` VARCHAR(200) DEFAULT NULL,
  `funding_source_label` VARCHAR(100) DEFAULT NULL,
  `mapped_amount` DECIMAL(12,2) DEFAULT NULL,
  `allocation_notes` TEXT DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`case_donor_id`),
  KEY `idx_case_donor_map_case` (`case_id`),
  KEY `idx_case_donor_map_donor` (`donor_id`),
  CONSTRAINT `fk_case_donor_map_case`
    FOREIGN KEY (`case_id`) REFERENCES `case_master` (`case_id`)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `fk_case_donor_map_donor`
    FOREIGN KEY (`donor_id`) REFERENCES `donor` (`donor_id`)
    ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `beni_program_ops` (
  `beni_ops_id` BIGINT NOT NULL AUTO_INCREMENT,
  `case_id` BIGINT NOT NULL,
  `volunteer_lead_user_id` BIGINT DEFAULT NULL,
  `assigned_volunteer_user_id` BIGINT DEFAULT NULL,
  `beni_team_member_allotted` VARCHAR(150) DEFAULT NULL,
  `spoc_name` VARCHAR(150) DEFAULT NULL,
  `spoc_number` VARCHAR(30) DEFAULT NULL,
  `pre_discharge_contact_flag` BOOLEAN NOT NULL DEFAULT FALSE,
  `pre_discharge_contact_at` DATETIME DEFAULT NULL,
  `spoc_contacted_flag` BOOLEAN NOT NULL DEFAULT FALSE,
  `spoc_contacted_at` DATETIME DEFAULT NULL,
  `parent_contacted_flag` BOOLEAN NOT NULL DEFAULT FALSE,
  `parent_contacted_at` DATETIME DEFAULT NULL,
  `baby_reached_home_flag` BOOLEAN NOT NULL DEFAULT FALSE,
  `baby_reached_home_at` DATETIME DEFAULT NULL,
  `hamper_sent_date` DATE DEFAULT NULL,
  `hamper_status` VARCHAR(100) DEFAULT NULL,
  `notes` TEXT DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`beni_ops_id`),
  UNIQUE KEY `uq_beni_program_ops_case` (`case_id`),
  KEY `idx_beni_program_ops_lead` (`volunteer_lead_user_id`),
  KEY `idx_beni_program_ops_volunteer` (`assigned_volunteer_user_id`),
  CONSTRAINT `fk_beni_program_ops_case`
    FOREIGN KEY (`case_id`) REFERENCES `case_master` (`case_id`)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `fk_beni_program_ops_lead`
    FOREIGN KEY (`volunteer_lead_user_id`) REFERENCES `app_user` (`user_id`)
    ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT `fk_beni_program_ops_volunteer`
    FOREIGN KEY (`assigned_volunteer_user_id`) REFERENCES `app_user` (`user_id`)
    ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `mortality_event` (
  `mortality_event_id` BIGINT NOT NULL AUTO_INCREMENT,
  `case_id` BIGINT NOT NULL,
  `capture_stage` ENUM('Hospital_Stay','Post_Discharge_Followup') NOT NULL,
  `observed_date` DATE DEFAULT NULL,
  `reported_by_user_id` BIGINT DEFAULT NULL,
  `source_type` ENUM('Hospital_Update','Volunteer_Followup','Parent_Report','Other') NOT NULL,
  `mortality_status` ENUM('Suspected','Confirmed') NOT NULL DEFAULT 'Confirmed',
  `place_of_death` VARCHAR(150) DEFAULT NULL,
  `notes` TEXT DEFAULT NULL,
  `communication_completed_flag` BOOLEAN NOT NULL DEFAULT FALSE,
  `workflow_impact_status` ENUM('Info_Only','Needs_Review','Closure_Updated') NOT NULL DEFAULT 'Needs_Review',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`mortality_event_id`),
  KEY `idx_mortality_event_case` (`case_id`),
  KEY `idx_mortality_event_stage` (`capture_stage`),
  KEY `idx_mortality_event_reported_by` (`reported_by_user_id`),
  CONSTRAINT `fk_mortality_event_case`
    FOREIGN KEY (`case_id`) REFERENCES `case_master` (`case_id`)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `fk_mortality_event_reported_by`
    FOREIGN KEY (`reported_by_user_id`) REFERENCES `app_user` (`user_id`)
    ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `followup_event` (
  `followup_id` BIGINT NOT NULL AUTO_INCREMENT,
  `case_id` BIGINT NOT NULL,
  `milestone_months` ENUM('3','6','9','12','18','24') NOT NULL,
  `due_date` DATE DEFAULT NULL,
  `followup_status` ENUM('Due','Attempted','Completed','Missed') NOT NULL DEFAULT 'Due',
  `reached_flag` BOOLEAN NOT NULL DEFAULT FALSE,
  `reached_at` DATETIME DEFAULT NULL,
  `conducted_by` BIGINT DEFAULT NULL,
  `mortality_observed_flag` BOOLEAN NOT NULL DEFAULT FALSE,
  `mortality_event_id` BIGINT DEFAULT NULL,
  `remarks` TEXT DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`followup_id`),
  UNIQUE KEY `uq_followup_event_case_milestone` (`case_id`,`milestone_months`),
  KEY `idx_followup_event_status` (`followup_status`),
  KEY `idx_followup_event_conducted_by` (`conducted_by`),
  KEY `idx_followup_event_mortality_event` (`mortality_event_id`),
  CONSTRAINT `fk_followup_event_case`
    FOREIGN KEY (`case_id`) REFERENCES `case_master` (`case_id`)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `fk_followup_event_conducted_by`
    FOREIGN KEY (`conducted_by`) REFERENCES `app_user` (`user_id`)
    ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT `fk_followup_event_mortality_event`
    FOREIGN KEY (`mortality_event_id`) REFERENCES `mortality_event` (`mortality_event_id`)
    ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `followup_metric_definition` (
  `metric_def_id` BIGINT NOT NULL AUTO_INCREMENT,
  `milestone_months` ENUM('3','6','9','12','18','24') NOT NULL,
  `template_global_index` INT DEFAULT NULL,
  `template_column_key` VARCHAR(50) DEFAULT NULL,
  `metric_label` VARCHAR(255) NOT NULL,
  `value_type` ENUM('BOOLEAN','NUMBER','TEXT','DATE') NOT NULL,
  `active_flag` BOOLEAN NOT NULL DEFAULT TRUE,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`metric_def_id`),
  KEY `idx_followup_metric_definition_milestone` (`milestone_months`,`active_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `followup_metric_value` (
  `metric_value_id` BIGINT NOT NULL AUTO_INCREMENT,
  `followup_id` BIGINT NOT NULL,
  `metric_def_id` BIGINT NOT NULL,
  `value_bool` BOOLEAN DEFAULT NULL,
  `value_number` DECIMAL(10,2) DEFAULT NULL,
  `value_text` TEXT DEFAULT NULL,
  `value_date` DATE DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`metric_value_id`),
  UNIQUE KEY `uq_followup_metric_value_followup_metric` (`followup_id`,`metric_def_id`),
  KEY `idx_followup_metric_value_metric` (`metric_def_id`),
  CONSTRAINT `fk_followup_metric_value_followup`
    FOREIGN KEY (`followup_id`) REFERENCES `followup_event` (`followup_id`)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `fk_followup_metric_value_metric_def`
    FOREIGN KEY (`metric_def_id`) REFERENCES `followup_metric_definition` (`metric_def_id`)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `bgrc_cycle_case_map` (
  `map_id` BIGINT NOT NULL AUTO_INCREMENT,
  `bgrc_cycle_id` BIGINT NOT NULL,
  `case_id` BIGINT NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`map_id`),
  UNIQUE KEY `uq_bgrc_cycle_case_map_cycle_case` (`bgrc_cycle_id`,`case_id`),
  KEY `idx_bgrc_cycle_case_map_case` (`case_id`),
  CONSTRAINT `fk_bgrc_cycle_case_map_cycle`
    FOREIGN KEY (`bgrc_cycle_id`) REFERENCES `bgrc_cycle` (`bgrc_cycle_id`)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `fk_bgrc_cycle_case_map_case`
    FOREIGN KEY (`case_id`) REFERENCES `case_master` (`case_id`)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `audit_log` (
  `audit_id` BIGINT NOT NULL AUTO_INCREMENT,
  `entity_type` VARCHAR(50) NOT NULL,
  `entity_id` BIGINT NOT NULL,
  `action` VARCHAR(100) NOT NULL,
  `actor_user_id` BIGINT NOT NULL,
  `event_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `details_json` JSON DEFAULT NULL,
  PRIMARY KEY (`audit_id`),
  KEY `idx_audit_log_entity` (`entity_type`,`entity_id`),
  KEY `idx_audit_log_actor` (`actor_user_id`),
  KEY `idx_audit_log_event_at` (`event_at`),
  CONSTRAINT `fk_audit_log_actor`
    FOREIGN KEY (`actor_user_id`) REFERENCES `app_user` (`user_id`)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 6. REPORTING DATA MODEL
-- ============================================================================

CREATE TABLE `dim_date` (
  `date_id` INT NOT NULL,
  `date` DATE NOT NULL,
  `day` TINYINT NOT NULL,
  `month` TINYINT NOT NULL,
  `quarter` TINYINT NOT NULL,
  `year` SMALLINT NOT NULL,
  `fiscal_year` SMALLINT NOT NULL,
  `fiscal_month` TINYINT NOT NULL,
  `is_month_end` BOOLEAN NOT NULL DEFAULT FALSE,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`date_id`),
  UNIQUE KEY `uq_dim_date_date` (`date`),
  KEY `idx_dim_date_fiscal` (`fiscal_year`,`fiscal_month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `dim_hospital` (
  `hospital_id` BIGINT NOT NULL,
  `hospital_name` VARCHAR(200) NOT NULL,
  `city` VARCHAR(100) DEFAULT NULL,
  `state` VARCHAR(100) DEFAULT NULL,
  `process_type` ENUM('BRC','BRRC','BGRC','BCRC','NON_BRC') NOT NULL,
  `active_flag` BOOLEAN NOT NULL DEFAULT TRUE,
  `synced_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`hospital_id`),
  KEY `idx_dim_hospital_name` (`hospital_name`),
  KEY `idx_dim_hospital_process_type` (`process_type`),
  CONSTRAINT `fk_dim_hospital_hospital`
    FOREIGN KEY (`hospital_id`) REFERENCES `hospital` (`hospital_id`)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `dim_case_status` (
  `status_code` VARCHAR(50) NOT NULL,
  `status_group` VARCHAR(50) NOT NULL,
  `is_open_flag` BOOLEAN NOT NULL DEFAULT FALSE,
  `sort_order` INT NOT NULL,
  `active_flag` BOOLEAN NOT NULL DEFAULT TRUE,
  PRIMARY KEY (`status_code`),
  KEY `idx_dim_case_status_group` (`status_group`,`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `dim_donor_category` (
  `donor_category_code` VARCHAR(50) NOT NULL,
  `display_name` VARCHAR(100) NOT NULL,
  `active_flag` BOOLEAN NOT NULL DEFAULT TRUE,
  PRIMARY KEY (`donor_category_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `dim_expense_category` (
  `category_code` VARCHAR(50) NOT NULL,
  `subcategory` VARCHAR(100) DEFAULT NULL,
  `cost_center` VARCHAR(100) DEFAULT NULL,
  `active_flag` BOOLEAN NOT NULL DEFAULT TRUE,
  PRIMARY KEY (`category_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `fact_case_event` (
  `event_id` BIGINT NOT NULL AUTO_INCREMENT,
  `case_id` BIGINT NOT NULL,
  `hospital_id` BIGINT NOT NULL,
  `event_type` VARCHAR(50) NOT NULL,
  `event_date_id` INT NOT NULL,
  `event_ts` DATETIME NOT NULL,
  `status_code` VARCHAR(50) DEFAULT NULL,
  `amount_value` DECIMAL(12,2) DEFAULT NULL,
  `details_json` JSON DEFAULT NULL,
  PRIMARY KEY (`event_id`),
  KEY `idx_fact_case_event_case` (`case_id`),
  KEY `idx_fact_case_event_hospital` (`hospital_id`),
  KEY `idx_fact_case_event_date` (`event_date_id`,`event_type`),
  KEY `idx_fact_case_event_status` (`status_code`),
  CONSTRAINT `fk_fact_case_event_case`
    FOREIGN KEY (`case_id`) REFERENCES `case_master` (`case_id`)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `fk_fact_case_event_hospital`
    FOREIGN KEY (`hospital_id`) REFERENCES `hospital` (`hospital_id`)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `fk_fact_case_event_date`
    FOREIGN KEY (`event_date_id`) REFERENCES `dim_date` (`date_id`)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT `fk_fact_case_event_status`
    FOREIGN KEY (`status_code`) REFERENCES `dim_case_status` (`status_code`)
    ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `fact_case_daily_snapshot` (
  `snapshot_id` BIGINT NOT NULL AUTO_INCREMENT,
  `date_id` INT NOT NULL,
  `case_id` BIGINT NOT NULL,
  `hospital_id` BIGINT NOT NULL,
  `status_code` VARCHAR(50) NOT NULL,
  `open_flag` BOOLEAN NOT NULL DEFAULT FALSE,
  `approved_amount` DECIMAL(12,2) DEFAULT NULL,
  `computed_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`snapshot_id`),
  UNIQUE KEY `uq_fact_case_daily_snapshot_date_case` (`date_id`,`case_id`),
  KEY `idx_fact_case_daily_snapshot_hospital` (`hospital_id`,`status_code`),
  CONSTRAINT `fk_fact_case_daily_snapshot_date`
    FOREIGN KEY (`date_id`) REFERENCES `dim_date` (`date_id`)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT `fk_fact_case_daily_snapshot_case`
    FOREIGN KEY (`case_id`) REFERENCES `case_master` (`case_id`)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `fk_fact_case_daily_snapshot_hospital`
    FOREIGN KEY (`hospital_id`) REFERENCES `hospital` (`hospital_id`)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `fk_fact_case_daily_snapshot_status`
    FOREIGN KEY (`status_code`) REFERENCES `dim_case_status` (`status_code`)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `fact_donation_txn` (
  `donation_txn_id` BIGINT NOT NULL AUTO_INCREMENT,
  `date_id` INT NOT NULL,
  `donor_category_code` VARCHAR(50) NOT NULL,
  `amount` DECIMAL(12,2) NOT NULL,
  `reference_no` VARCHAR(100) DEFAULT NULL,
  `notes` TEXT DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`donation_txn_id`),
  KEY `idx_fact_donation_txn_date` (`date_id`),
  KEY `idx_fact_donation_txn_category` (`donor_category_code`),
  CONSTRAINT `fk_fact_donation_txn_date`
    FOREIGN KEY (`date_id`) REFERENCES `dim_date` (`date_id`)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT `fk_fact_donation_txn_category`
    FOREIGN KEY (`donor_category_code`) REFERENCES `dim_donor_category` (`donor_category_code`)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `fact_balance_snapshot` (
  `balance_snapshot_id` BIGINT NOT NULL AUTO_INCREMENT,
  `date_id` INT NOT NULL,
  `bank_amount` DECIMAL(12,2) NOT NULL,
  `fd_amount` DECIMAL(12,2) NOT NULL,
  `captured_by` BIGINT DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`balance_snapshot_id`),
  UNIQUE KEY `uq_fact_balance_snapshot_date` (`date_id`),
  KEY `idx_fact_balance_snapshot_captured_by` (`captured_by`),
  CONSTRAINT `fk_fact_balance_snapshot_date`
    FOREIGN KEY (`date_id`) REFERENCES `dim_date` (`date_id`)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT `fk_fact_balance_snapshot_captured_by`
    FOREIGN KEY (`captured_by`) REFERENCES `app_user` (`user_id`)
    ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `fact_expense_txn` (
  `expense_txn_id` BIGINT NOT NULL AUTO_INCREMENT,
  `date_id` INT NOT NULL,
  `category_code` VARCHAR(50) NOT NULL,
  `amount` DECIMAL(12,2) NOT NULL,
  `reference_no` VARCHAR(100) DEFAULT NULL,
  `notes` TEXT DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`expense_txn_id`),
  KEY `idx_fact_expense_txn_date` (`date_id`),
  KEY `idx_fact_expense_txn_category` (`category_code`),
  CONSTRAINT `fk_fact_expense_txn_date`
    FOREIGN KEY (`date_id`) REFERENCES `dim_date` (`date_id`)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT `fk_fact_expense_txn_category`
    FOREIGN KEY (`category_code`) REFERENCES `dim_expense_category` (`category_code`)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `fact_hospital_onboarding` (
  `onboarding_id` BIGINT NOT NULL AUTO_INCREMENT,
  `hospital_id` BIGINT NOT NULL,
  `stage` VARCHAR(50) NOT NULL,
  `stage_date_id` INT NOT NULL,
  `notes` TEXT DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`onboarding_id`),
  KEY `idx_fact_hospital_onboarding_hospital` (`hospital_id`),
  KEY `idx_fact_hospital_onboarding_stage_date` (`stage`,`stage_date_id`),
  CONSTRAINT `fk_fact_hospital_onboarding_hospital`
    FOREIGN KEY (`hospital_id`) REFERENCES `hospital` (`hospital_id`)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `fk_fact_hospital_onboarding_stage_date`
    FOREIGN KEY (`stage_date_id`) REFERENCES `dim_date` (`date_id`)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `agg_hospital_monthly` (
  `agg_hospital_monthly_id` BIGINT NOT NULL AUTO_INCREMENT,
  `hospital_id` BIGINT NOT NULL,
  `fiscal_year` SMALLINT NOT NULL,
  `fiscal_month` TINYINT NOT NULL,
  `enquiries_count` INT NOT NULL DEFAULT 0,
  `approved_count` INT NOT NULL DEFAULT 0,
  `rejected_count` INT NOT NULL DEFAULT 0,
  `conversion_ratio` DECIMAL(10,4) DEFAULT NULL,
  `carry_forward_count` INT DEFAULT NULL,
  `computed_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`agg_hospital_monthly_id`),
  UNIQUE KEY `uq_agg_hospital_monthly_hospital_period` (`hospital_id`,`fiscal_year`,`fiscal_month`),
  CONSTRAINT `fk_agg_hospital_monthly_hospital`
    FOREIGN KEY (`hospital_id`) REFERENCES `hospital` (`hospital_id`)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `agg_org_monthly` (
  `agg_org_monthly_id` BIGINT NOT NULL AUTO_INCREMENT,
  `fiscal_year` SMALLINT NOT NULL,
  `fiscal_month` TINYINT NOT NULL,
  `enquiries_count` INT NOT NULL DEFAULT 0,
  `approved_count` INT NOT NULL DEFAULT 0,
  `identified_hospitals_count` INT DEFAULT NULL,
  `active_hospitals_count` INT DEFAULT NULL,
  `donation_total` DECIMAL(12,2) DEFAULT NULL,
  `program_cost` DECIMAL(12,2) DEFAULT NULL,
  `ops_cost` DECIMAL(12,2) DEFAULT NULL,
  `program_ops_ratio` DECIMAL(10,4) DEFAULT NULL,
  `computed_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`agg_org_monthly_id`),
  UNIQUE KEY `uq_agg_org_monthly_period` (`fiscal_year`,`fiscal_month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `agg_accounts_daily` (
  `agg_accounts_daily_id` BIGINT NOT NULL AUTO_INCREMENT,
  `date_id` INT NOT NULL,
  `completed_babies_till_date` INT DEFAULT NULL,
  `completed_babies_current_month` INT DEFAULT NULL,
  `inquiries_count` INT DEFAULT NULL,
  `rejected_count` INT DEFAULT NULL,
  `in_pipeline_count` INT DEFAULT NULL,
  `partner_hospital_completed_count` INT DEFAULT NULL,
  `partner_hospital_pipeline_count` INT DEFAULT NULL,
  `funds_raised_mtd` DECIMAL(12,2) DEFAULT NULL,
  `funds_raised_daily` DECIMAL(12,2) DEFAULT NULL,
  `total_funds` DECIMAL(12,2) DEFAULT NULL,
  `committed_outflow_amount` DECIMAL(12,2) DEFAULT NULL,
  `surplus_funds` DECIMAL(12,2) DEFAULT NULL,
  `program_ops_ratio` DECIMAL(10,4) DEFAULT NULL,
  `computed_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`agg_accounts_daily_id`),
  UNIQUE KEY `uq_agg_accounts_daily_date` (`date_id`),
  CONSTRAINT `fk_agg_accounts_daily_date`
    FOREIGN KEY (`date_id`) REFERENCES `dim_date` (`date_id`)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `export_blob` (
  `export_blob_id` BIGINT NOT NULL AUTO_INCREMENT,
  `file_name` VARCHAR(255) NOT NULL,
  `file_format` ENUM('XLSX','CSV','PDF','TXT') NOT NULL,
  `storage_path` VARCHAR(500) NOT NULL,
  `checksum_sha256` CHAR(64) DEFAULT NULL,
  `visibility_scope` ENUM('Internal_Only','Donor_Safe','Hospital_Scoped') NOT NULL DEFAULT 'Internal_Only',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `expires_at` DATETIME DEFAULT NULL,
  PRIMARY KEY (`export_blob_id`),
  KEY `idx_export_blob_visibility` (`visibility_scope`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `report_template` (
  `template_id` BIGINT NOT NULL AUTO_INCREMENT,
  `template_code` VARCHAR(100) NOT NULL,
  `name` VARCHAR(200) NOT NULL,
  `description` TEXT DEFAULT NULL,
  `status` ENUM('Draft','Active','Retired') NOT NULL DEFAULT 'Draft',
  `version_label` VARCHAR(50) NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`template_id`),
  UNIQUE KEY `uq_report_template_code` (`template_code`),
  KEY `idx_report_template_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `report_dataset` (
  `dataset_id` BIGINT NOT NULL AUTO_INCREMENT,
  `dataset_code` VARCHAR(100) NOT NULL,
  `name` VARCHAR(200) NOT NULL,
  `source_type` ENUM('View','Table','Query','Procedure') NOT NULL,
  `source_ref` VARCHAR(300) NOT NULL,
  `active_flag` BOOLEAN NOT NULL DEFAULT TRUE,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`dataset_id`),
  UNIQUE KEY `uq_report_dataset_code` (`dataset_code`),
  KEY `idx_report_dataset_active` (`active_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `report_kpi_def` (
  `kpi_id` BIGINT NOT NULL AUTO_INCREMENT,
  `kpi_code` VARCHAR(100) NOT NULL,
  `name` VARCHAR(200) NOT NULL,
  `formula_expression` TEXT NOT NULL,
  `grain` VARCHAR(100) DEFAULT NULL,
  `active_flag` BOOLEAN NOT NULL DEFAULT TRUE,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`kpi_id`),
  UNIQUE KEY `uq_report_kpi_def_code` (`kpi_code`),
  KEY `idx_report_kpi_def_active` (`active_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `report_template_binding` (
  `binding_id` BIGINT NOT NULL AUTO_INCREMENT,
  `template_id` BIGINT NOT NULL,
  `dataset_id` BIGINT NOT NULL,
  `kpi_id` BIGINT DEFAULT NULL,
  `binding_notes` TEXT DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`binding_id`),
  KEY `idx_report_template_binding_template` (`template_id`),
  KEY `idx_report_template_binding_dataset` (`dataset_id`),
  KEY `idx_report_template_binding_kpi` (`kpi_id`),
  CONSTRAINT `fk_report_template_binding_template`
    FOREIGN KEY (`template_id`) REFERENCES `report_template` (`template_id`)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `fk_report_template_binding_dataset`
    FOREIGN KEY (`dataset_id`) REFERENCES `report_dataset` (`dataset_id`)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `fk_report_template_binding_kpi`
    FOREIGN KEY (`kpi_id`) REFERENCES `report_kpi_def` (`kpi_id`)
    ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `report_run_history` (
  `run_id` BIGINT NOT NULL AUTO_INCREMENT,
  `template_id` BIGINT NOT NULL,
  `parameters_json` JSON DEFAULT NULL,
  `status` ENUM('Queued','Running','Succeeded','Failed') NOT NULL DEFAULT 'Queued',
  `data_as_of` DATETIME DEFAULT NULL,
  `generated_at` DATETIME DEFAULT NULL,
  `generated_by` BIGINT DEFAULT NULL,
  `export_blob_id` BIGINT DEFAULT NULL,
  `notes` TEXT DEFAULT NULL,
  PRIMARY KEY (`run_id`),
  KEY `idx_report_run_history_template` (`template_id`),
  KEY `idx_report_run_history_status` (`status`),
  KEY `idx_report_run_history_generated_by` (`generated_by`),
  KEY `idx_report_run_history_export_blob` (`export_blob_id`),
  CONSTRAINT `fk_report_run_history_template`
    FOREIGN KEY (`template_id`) REFERENCES `report_template` (`template_id`)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT `fk_report_run_history_generated_by`
    FOREIGN KEY (`generated_by`) REFERENCES `app_user` (`user_id`)
    ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT `fk_report_run_history_export_blob`
    FOREIGN KEY (`export_blob_id`) REFERENCES `export_blob` (`export_blob_id`)
    ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `hope_story` (
  `hope_story_id` BIGINT NOT NULL AUTO_INCREMENT,
  `case_id` BIGINT NOT NULL,
  `status` ENUM('Not_Started','In_Progress','Published') NOT NULL DEFAULT 'Not_Started',
  `internal_summary_export_id` BIGINT DEFAULT NULL,
  `story_link` VARCHAR(500) DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`hope_story_id`),
  UNIQUE KEY `uq_hope_story_case` (`case_id`),
  KEY `idx_hope_story_export_blob` (`internal_summary_export_id`),
  CONSTRAINT `fk_hope_story_case`
    FOREIGN KEY (`case_id`) REFERENCES `case_master` (`case_id`)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `fk_hope_story_export_blob`
    FOREIGN KEY (`internal_summary_export_id`) REFERENCES `export_blob` (`export_blob_id`)
    ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE nfi_bms_cmdws.clinical_details (
  clinical_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  case_id BIGINT NOT NULL,

  -- Risk Factors
  antenatal_risk_factors TEXT,
  risk_notes TEXT,

  -- Diagnosis
  diagnoses TEXT,
  other_diagnosis TEXT,

  -- Treatment (General)
  respiration_support TEXT,
  iv_antibiotics BOOLEAN,
  ionotropes BOOLEAN,
  tpn BOOLEAN,
  other_treatment TEXT,

  -- Current Status
  current_day_of_life INT,
  current_weight_kg DECIMAL(5,2),
  corrected_gestational_age_weeks DECIMAL(5,2),

  -- Respiration (Structured)
  mechanical_ventilation BOOLEAN,
  cpap BOOLEAN,
  hfnc BOOLEAN,
  oxygen_support BOOLEAN,

  -- Feeding
  npo BOOLEAN,
  og_feeding BOOLEAN,
  palada_feeding BOOLEAN,
  dbf_feeding BOOLEAN,
  other_feeding TEXT,

  -- Plan
  discharge_plan TEXT,

  -- Investigations
  labs_attached BOOLEAN,
  xray_attached BOOLEAN,
  scans_attached BOOLEAN,
  other_reports_attached BOOLEAN,
  other_investigation_details TEXT,

  -- Remarks & Signature
  remarks TEXT,
  signature_file_path VARCHAR(255),
  signed_date DATETIME,

  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  CONSTRAINT fk_clinical_case
    FOREIGN KEY (case_id)
    REFERENCES case_master(case_id)
    ON DELETE CASCADE
);

-- ============================================================================
-- 7. LATE-BOUND FOREIGN KEYS / HELPER VIEWS
-- ============================================================================

ALTER TABLE `bgrc_cycle`
  ADD CONSTRAINT `fk_bgrc_cycle_utilization_document`
  FOREIGN KEY (`utilization_document_id`) REFERENCES `document_metadata` (`document_id`)
  ON UPDATE CASCADE ON DELETE SET NULL;

CREATE OR REPLACE VIEW `vw_rejection_master_data` AS
SELECT
  cm.`case_id`,
  cm.`case_uuid`,
  cm.`case_reference_no`,
  h.`hospital_name`,
  cp.`beneficiary_no`,
  cp.`display_name`,
  rd.`rejection_reason_category`,
  rd.`rejection_level`,
  rd.`rejection_communication_status`,
  rd.`rejection_case_summary`,
  cm.`rejection_date`,
  cm.`case_status`
FROM `case_master` cm
LEFT JOIN `hospital` h ON h.`hospital_id` = cm.`hospital_id`
LEFT JOIN `child_profile` cp ON cp.`case_id` = cm.`case_id`
LEFT JOIN `rejection_details` rd ON rd.`case_id` = cm.`case_id`;

CREATE OR REPLACE VIEW `vw_internal_case_summary_data` AS
SELECT
  cm.`case_id`,
  cm.`case_uuid`,
  cm.`case_reference_no`,
  cm.`process_type`,
  cm.`case_status`,
  h.`hospital_name`,
  cp.`beneficiary_no`,
  cp.`display_name`,
  cp.`date_of_birth`,
  cp.`mortality_status`,
  fp.`father_name`,
  fp.`mother_name`,
  ccd.`primary_diagnosis`,
  fcd.`medical_bill_estimate`,
  fcd.`sponsor_amount_final`,
  sc.`discharge_date`,
  sc.`final_bill_amount`,
  p.`status` AS `payment_status`,
  bo.`beni_team_member_allotted`,
  bo.`hamper_status`,
  hs.`status` AS `hope_story_status`
FROM `case_master` cm
LEFT JOIN `hospital` h ON h.`hospital_id` = cm.`hospital_id`
LEFT JOIN `child_profile` cp ON cp.`case_id` = cm.`case_id`
LEFT JOIN `family_profile` fp ON fp.`case_id` = cm.`case_id`
LEFT JOIN `clinical_case_details` ccd ON ccd.`case_id` = cm.`case_id`
LEFT JOIN `financial_case_details` fcd ON fcd.`case_id` = cm.`case_id`
LEFT JOIN `settlement_closure` sc ON sc.`case_id` = cm.`case_id`
LEFT JOIN `payment` p ON p.`case_id` = cm.`case_id`
LEFT JOIN `beni_program_ops` bo ON bo.`case_id` = cm.`case_id`
LEFT JOIN `hope_story` hs ON hs.`case_id` = cm.`case_id`;

SET FOREIGN_KEY_CHECKS = 1;
