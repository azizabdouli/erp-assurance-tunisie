-- V7: Accounting Module

CREATE TABLE chart_of_accounts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    account_code VARCHAR(20) NOT NULL UNIQUE,
    account_name VARCHAR(255) NOT NULL,
    account_type VARCHAR(20) NOT NULL,
    parent_account_code VARCHAR(20),
    description VARCHAR(500),
    active BOOLEAN DEFAULT TRUE,
    level INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0
);

CREATE TABLE journals (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    journal_code VARCHAR(20) NOT NULL UNIQUE,
    journal_name VARCHAR(255) NOT NULL,
    journal_type VARCHAR(20) NOT NULL,
    description VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    modified_by VARCHAR(100),
    version BIGINT DEFAULT 0
);

CREATE TABLE journal_entries (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    journal_id UUID NOT NULL REFERENCES journals(id),
    entry_number VARCHAR(50) NOT NULL,
    entry_date DATE NOT NULL,
    account_code VARCHAR(20) NOT NULL,
    description VARCHAR(500) NOT NULL,
    debit_amount DECIMAL(15,3) DEFAULT 0,
    credit_amount DECIMAL(15,3) DEFAULT 0,
    reference VARCHAR(100),
    reference_type VARCHAR(50),
    posted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    modified_by VARCHAR(100),
    version BIGINT DEFAULT 0
);

CREATE TABLE general_ledger (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    account_code VARCHAR(20) NOT NULL,
    period_date DATE NOT NULL,
    total_debit DECIMAL(15,3) DEFAULT 0,
    total_credit DECIMAL(15,3) DEFAULT 0,
    balance DECIMAL(15,3) DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0
);

CREATE TABLE technical_accounts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    account_code VARCHAR(20) NOT NULL UNIQUE,
    account_name VARCHAR(255) NOT NULL,
    product_code VARCHAR(50),
    premiums_written DECIMAL(15,3) DEFAULT 0,
    premiums_earned DECIMAL(15,3) DEFAULT 0,
    claims_incurred DECIMAL(15,3) DEFAULT 0,
    claims_paid DECIMAL(15,3) DEFAULT 0,
    reserves DECIMAL(15,3) DEFAULT 0,
    commissions DECIMAL(15,3) DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    modified_by VARCHAR(100),
    version BIGINT DEFAULT 0
);

CREATE TABLE accounting_periods (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    period_name VARCHAR(50) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    fiscal_year INTEGER NOT NULL,
    closed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0
);

-- Tunisian Chart of Accounts - Initial seed
INSERT INTO chart_of_accounts (id, account_code, account_name, account_type, level) VALUES
    (uuid_generate_v4(), '1', 'Capitaux Propres', 'EQUITY', 1),
    (uuid_generate_v4(), '2', 'Immobilisations', 'ASSET', 1),
    (uuid_generate_v4(), '3', 'Stocks', 'ASSET', 1),
    (uuid_generate_v4(), '4', 'Tiers', 'LIABILITY', 1),
    (uuid_generate_v4(), '5', 'Financiers', 'ASSET', 1),
    (uuid_generate_v4(), '6', 'Charges', 'EXPENSE', 1),
    (uuid_generate_v4(), '7', 'Produits', 'REVENUE', 1),
    (uuid_generate_v4(), '70', 'Primes Emises', 'REVENUE', 2),
    (uuid_generate_v4(), '60', 'Charges Techniques', 'EXPENSE', 2),
    (uuid_generate_v4(), '61', 'Sinistres Payes', 'EXPENSE', 2);

-- Default Journals
INSERT INTO journals (id, journal_code, journal_name, journal_type) VALUES
    (uuid_generate_v4(), 'JNL-PREM', 'Journal des Primes', 'PREMIUM'),
    (uuid_generate_v4(), 'JNL-SIN', 'Journal des Sinistres', 'CLAIM'),
    (uuid_generate_v4(), 'JNL-PAY', 'Journal des Paiements', 'PAYMENT'),
    (uuid_generate_v4(), 'JNL-GEN', 'Journal General', 'GENERAL'),
    (uuid_generate_v4(), 'JNL-TECH', 'Journal Technique', 'TECHNICAL');

CREATE INDEX idx_journal_entries_journal ON journal_entries(journal_id);
CREATE INDEX idx_journal_entries_account ON journal_entries(account_code);
CREATE INDEX idx_journal_entries_date ON journal_entries(entry_date);
CREATE INDEX idx_general_ledger_account ON general_ledger(account_code);
CREATE INDEX idx_technical_accounts_product ON technical_accounts(product_code);
