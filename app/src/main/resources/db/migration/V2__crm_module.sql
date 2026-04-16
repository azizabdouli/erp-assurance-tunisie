-- V2: CRM Module

CREATE TABLE customer_segments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    criteria TEXT,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0
);

CREATE TABLE clients (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    client_type VARCHAR(20) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(255) UNIQUE,
    phone VARCHAR(20),
    cin VARCHAR(8) UNIQUE,
    date_of_birth DATE,
    company_name VARCHAR(255),
    registration_number VARCHAR(100),
    tax_id VARCHAR(50),
    kyc_status VARCHAR(20) DEFAULT 'PENDING',
    aml_risk_level VARCHAR(20) DEFAULT 'LOW',
    address_line1 VARCHAR(255),
    address_line2 VARCHAR(255),
    city VARCHAR(100),
    governorate VARCHAR(100),
    postal_code VARCHAR(10),
    country VARCHAR(5) DEFAULT 'TN',
    iban VARCHAR(30),
    bank_name VARCHAR(100),
    deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    modified_by VARCHAR(100),
    version BIGINT DEFAULT 0
);

CREATE TABLE client_interactions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    client_id UUID NOT NULL REFERENCES clients(id),
    interaction_type VARCHAR(20) NOT NULL,
    subject VARCHAR(255) NOT NULL,
    description TEXT,
    interaction_date TIMESTAMP NOT NULL,
    performed_by VARCHAR(100),
    outcome VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    modified_by VARCHAR(100),
    version BIGINT DEFAULT 0
);

CREATE TABLE kyc_documents (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    client_id UUID NOT NULL REFERENCES clients(id),
    document_type VARCHAR(50) NOT NULL,
    document_number VARCHAR(100),
    file_name VARCHAR(255),
    file_path VARCHAR(500),
    file_hash VARCHAR(64),
    issue_date DATE,
    expiry_date DATE,
    verified BOOLEAN DEFAULT FALSE,
    verified_by VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    modified_by VARCHAR(100),
    version BIGINT DEFAULT 0
);

CREATE TABLE aml_screenings (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    client_id UUID NOT NULL REFERENCES clients(id),
    screening_date TIMESTAMP NOT NULL,
    screening_provider VARCHAR(100),
    pep_match BOOLEAN DEFAULT FALSE,
    sanction_match BOOLEAN DEFAULT FALSE,
    risk_level VARCHAR(20),
    screening_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    notes TEXT,
    reviewed_by VARCHAR(100),
    review_date TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    modified_by VARCHAR(100),
    version BIGINT DEFAULT 0
);

CREATE TABLE contact_persons (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    client_id UUID NOT NULL REFERENCES clients(id),
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    position VARCHAR(100),
    email VARCHAR(255),
    phone VARCHAR(20),
    is_primary BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0
);

CREATE INDEX idx_clients_cin ON clients(cin);
CREATE INDEX idx_clients_email ON clients(email);
CREATE INDEX idx_clients_type ON clients(client_type);
CREATE INDEX idx_clients_kyc ON clients(kyc_status);
CREATE INDEX idx_client_interactions_client ON client_interactions(client_id);
CREATE INDEX idx_kyc_documents_client ON kyc_documents(client_id);
CREATE INDEX idx_aml_screenings_client ON aml_screenings(client_id);
