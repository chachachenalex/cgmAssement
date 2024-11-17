CREATE TABLE patient (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         surname VARCHAR(100) NOT NULL,
                         date_of_birth DATE NOT NULL,
                         social_security_number VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE visit (
                       id BIGSERIAL PRIMARY KEY,
                       visit_date TIMESTAMP NOT NULL,
                       type VARCHAR(50) NOT NULL,
                       reason VARCHAR(50) NOT NULL,
                       family_history TEXT,
                       patient_id BIGINT NOT NULL,
                       FOREIGN KEY (patient_id) REFERENCES patient (id) ON DELETE CASCADE
);

-- GRANT ALL PRIVILEGES ON DATABASE mydb TO user;

