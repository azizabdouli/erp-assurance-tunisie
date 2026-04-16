-- V8: Audit Module

CREATE TABLE audit_logs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    entity_type VARCHAR(100) NOT NULL,
    entity_id VARCHAR(100) NOT NULL,
    action VARCHAR(20) NOT NULL,
    performed_by VARCHAR(100),
    performed_at TIMESTAMP NOT NULL,
    old_value TEXT,
    new_value TEXT,
    ip_address VARCHAR(45),
    description VARCHAR(500)
);

CREATE TABLE audit_trail (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    table_name VARCHAR(100) NOT NULL,
    record_id VARCHAR(100) NOT NULL,
    field_name VARCHAR(100) NOT NULL,
    old_value TEXT,
    new_value TEXT,
    changed_by VARCHAR(100),
    changed_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_audit_logs_entity ON audit_logs(entity_type, entity_id);
CREATE INDEX idx_audit_logs_user ON audit_logs(performed_by);
CREATE INDEX idx_audit_logs_date ON audit_logs(performed_at);
CREATE INDEX idx_audit_trail_record ON audit_trail(table_name, record_id);
CREATE INDEX idx_audit_trail_date ON audit_trail(changed_at);
