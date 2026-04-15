-- V5: Claims Module

CREATE TABLE claims (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    claim_number VARCHAR(50) NOT NULL UNIQUE,
    policy_id UUID NOT NULL REFERENCES policies(id),
    status VARCHAR(30) NOT NULL DEFAULT 'DECLARED',
    incident_date DATE NOT NULL,
    declaration_date TIMESTAMP NOT NULL,
    incident_description TEXT,
    incident_location VARCHAR(255),
    estimated_amount DECIMAL(15,3),
    approved_amount DECIMAL(15,3),
    deductible_amount DECIMAL(15,3),
    fraud_score INTEGER,
    fraud_flagged BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    modified_by VARCHAR(100),
    version BIGINT DEFAULT 0
);

CREATE TABLE claim_documents (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    claim_id UUID NOT NULL REFERENCES claims(id),
    document_type VARCHAR(50) NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500),
    file_size BIGINT,
    content_type VARCHAR(100),
    description VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0
);

CREATE TABLE claim_status_history (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    claim_id UUID NOT NULL REFERENCES claims(id),
    old_status VARCHAR(30),
    new_status VARCHAR(30) NOT NULL,
    changed_by VARCHAR(100),
    changed_at TIMESTAMP NOT NULL,
    comments TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0
);

CREATE TABLE expert_assignments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    claim_id UUID NOT NULL REFERENCES claims(id),
    expert_name VARCHAR(200) NOT NULL,
    expert_speciality VARCHAR(100),
    assignment_date DATE NOT NULL,
    report_date DATE,
    estimated_damage DECIMAL(15,3),
    report_summary TEXT,
    assignment_status VARCHAR(20) DEFAULT 'ASSIGNED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    modified_by VARCHAR(100),
    version BIGINT DEFAULT 0
);

CREATE TABLE fraud_indicators (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    claim_id UUID NOT NULL REFERENCES claims(id),
    indicator_type VARCHAR(50) NOT NULL,
    description TEXT,
    score INTEGER,
    detected_by VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0
);

CREATE TABLE claim_reserves (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    claim_id UUID NOT NULL REFERENCES claims(id),
    reserve_amount DECIMAL(15,3) NOT NULL,
    reserve_type VARCHAR(20) NOT NULL,
    adjustment_date TIMESTAMP NOT NULL,
    adjusted_by VARCHAR(100),
    reason TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    modified_by VARCHAR(100),
    version BIGINT DEFAULT 0
);

CREATE INDEX idx_claims_number ON claims(claim_number);
CREATE INDEX idx_claims_policy ON claims(policy_id);
CREATE INDEX idx_claims_status ON claims(status);
CREATE INDEX idx_claims_fraud ON claims(fraud_flagged);
CREATE INDEX idx_claim_docs_claim ON claim_documents(claim_id);
CREATE INDEX idx_expert_claim ON expert_assignments(claim_id);
