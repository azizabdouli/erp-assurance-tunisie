-- V9: Seed default admin user
-- Password: Admin@2024  (BCrypt hash)

INSERT INTO users (id, username, email, password_hash, first_name, last_name, active, created_at, updated_at, version)
VALUES (
    uuid_generate_v4(),
    'admin',
    'admin@erp-assurance.tn',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.',
    'System',
    'Administrator',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    0
) ON CONFLICT (username) DO NOTHING;

-- Assign ADMIN role to the default admin user
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.username = 'admin'
  AND r.name = 'ADMIN'
ON CONFLICT DO NOTHING;
