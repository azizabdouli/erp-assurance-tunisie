-- V6: Indemnity Module

CREATE TABLE settlements (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    settlement_number VARCHAR(50) NOT NULL UNIQUE,
    claim_id UUID NOT NULL REFERENCES claims(id),
    gross_amount DECIMAL(15,3) NOT NULL,
    deductible_amount DECIMAL(15,3) DEFAULT 0,
    co_insurance_share DECIMAL(5,2) DEFAULT 100.00,
    net_amount DECIMAL(15,3) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'CALCULATED',
    approved_by VARCHAR(100),
    approved_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    modified_by VARCHAR(100),
    version BIGINT DEFAULT 0
);

CREATE TABLE settlement_payments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    settlement_id UUID NOT NULL REFERENCES settlements(id),
    payment_reference VARCHAR(50) NOT NULL UNIQUE,
    amount DECIMAL(15,3) NOT NULL,
    payment_method VARCHAR(20) NOT NULL,
    payment_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    payment_date TIMESTAMP,
    beneficiary_name VARCHAR(200),
    beneficiary_iban VARCHAR(30),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    modified_by VARCHAR(100),
    version BIGINT DEFAULT 0
);

CREATE TABLE subrogation_claims (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    claim_id UUID NOT NULL REFERENCES claims(id),
    third_party_name VARCHAR(200) NOT NULL,
    third_party_insurer VARCHAR(200),
    claimed_amount DECIMAL(15,3) NOT NULL,
    recovered_amount DECIMAL(15,3) DEFAULT 0,
    subrogation_status VARCHAR(30) DEFAULT 'INITIATED',
    initiated_date DATE,
    resolved_date DATE,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    modified_by VARCHAR(100),
    version BIGINT DEFAULT 0
);

CREATE INDEX idx_settlements_claim ON settlements(claim_id);
CREATE INDEX idx_settlements_status ON settlements(status);
CREATE INDEX idx_settlement_payments_settlement ON settlement_payments(settlement_id);
CREATE INDEX idx_subrogation_claim ON subrogation_claims(claim_id);
