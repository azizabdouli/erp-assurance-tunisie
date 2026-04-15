-- V4: Policy Module

CREATE TABLE policies (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    policy_number VARCHAR(50) NOT NULL UNIQUE,
    client_id UUID NOT NULL REFERENCES clients(id),
    product_id UUID NOT NULL REFERENCES products(id),
    quotation_id UUID REFERENCES quotations(id),
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    effective_date DATE NOT NULL,
    expiry_date DATE NOT NULL,
    annual_premium DECIMAL(15,3),
    net_premium DECIMAL(15,3),
    tax_amount DECIMAL(15,3),
    total_premium DECIMAL(15,3),
    sum_insured DECIMAL(15,3),
    renewal_count INTEGER DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    modified_by VARCHAR(100),
    version BIGINT DEFAULT 0
);

CREATE TABLE policy_endorsements (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    policy_id UUID NOT NULL REFERENCES policies(id),
    endorsement_number VARCHAR(50) NOT NULL UNIQUE,
    endorsement_type VARCHAR(30) NOT NULL,
    effective_date DATE NOT NULL,
    description TEXT,
    premium_adjustment DECIMAL(15,3),
    approved BOOLEAN DEFAULT FALSE,
    approved_by VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    modified_by VARCHAR(100),
    version BIGINT DEFAULT 0
);

CREATE TABLE policy_history (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    policy_id UUID NOT NULL REFERENCES policies(id),
    old_status VARCHAR(20),
    new_status VARCHAR(20) NOT NULL,
    changed_by VARCHAR(100),
    changed_at TIMESTAMP NOT NULL,
    comments TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0
);

CREATE TABLE policy_renewals (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    original_policy_id UUID NOT NULL REFERENCES policies(id),
    renewed_policy_id UUID REFERENCES policies(id),
    renewal_date DATE NOT NULL,
    renewal_status VARCHAR(20) DEFAULT 'PENDING',
    comments TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0
);

CREATE TABLE premium_invoices (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    policy_id UUID NOT NULL REFERENCES policies(id),
    invoice_number VARCHAR(50) NOT NULL UNIQUE,
    amount DECIMAL(15,3) NOT NULL,
    tax_amount DECIMAL(15,3),
    total_amount DECIMAL(15,3) NOT NULL,
    due_date DATE NOT NULL,
    paid BOOLEAN DEFAULT FALSE,
    paid_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    modified_by VARCHAR(100),
    version BIGINT DEFAULT 0
);

CREATE TABLE payments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    invoice_id UUID NOT NULL REFERENCES premium_invoices(id),
    payment_reference VARCHAR(50) NOT NULL UNIQUE,
    amount DECIMAL(15,3) NOT NULL,
    payment_method VARCHAR(20) NOT NULL,
    payment_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    payment_date TIMESTAMP,
    bank_reference VARCHAR(100),
    notes VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    modified_by VARCHAR(100),
    version BIGINT DEFAULT 0
);

CREATE INDEX idx_policies_number ON policies(policy_number);
CREATE INDEX idx_policies_client ON policies(client_id);
CREATE INDEX idx_policies_status ON policies(status);
CREATE INDEX idx_policies_expiry ON policies(expiry_date);
CREATE INDEX idx_endorsements_policy ON policy_endorsements(policy_id);
CREATE INDEX idx_invoices_policy ON premium_invoices(policy_id);
CREATE INDEX idx_invoices_due ON premium_invoices(due_date);
CREATE INDEX idx_payments_invoice ON payments(invoice_id);
