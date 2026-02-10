CREATE TABLE users (
    user_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(15),
    email VARCHAR(100),
    role VARCHAR(10) CHECK (role IN ('ADMIN', 'OWNER')) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE sites (
    site_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    length_ft INT NOT NULL,
    width_ft INT NOT NULL,
    sqft INT NOT NULL,
    site_type VARCHAR(30) CHECK (
        site_type IN ('VILLA', 'APARTMENT', 'INDEPENDENT_HOUSE', 'OPEN_SITE')
    ) NOT NULL,
    is_occupied BOOLEAN DEFAULT FALSE,
    owner_id INT,
	CONSTRAINT fk_owner FOREIGN KEY (owner_id) REFERENCES users(user_id)
);


CREATE TABLE maintenance (
    maintenance_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    site_id INT NOT NULL,
    amount NUMERIC(10,2) NOT NULL,
    generated_date DATE NOT NULL,
    CONSTRAINT fk_site FOREIGN KEY (site_id) REFERENCES sites(site_id)
);


CREATE TABLE payments(
payment_id INT,
paid_by INT,
paid_amt INT,
pending_amt INT,
status VARCHAR(10) CHECK (
        status IN ('PENDING', 'PAID')
    ) DEFAULT 'PENDING',
CONSTRAINT fk_payments FOREIGN KEY (payment_id) REFERENCES maintenance(maintenance_id)
CONSTRAINT fk_payments2 FOREIGN KEY (paid_by) REFERENCES users(user_id)
)


CREATE TABLE site_update_requests (
    request_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    site_id INT NOT NULL,
    requested_by INT NOT NULL,
    update_type VARCHAR(50) NOT NULL,
    old_value VARCHAR(100),
    new_value VARCHAR(100),
    status VARCHAR(20) CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED')) DEFAULT 'PENDING',
    request_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_site_request FOREIGN KEY (site_id) REFERENCES sites(site_id),
    CONSTRAINT fk_user_request FOREIGN KEY (requested_by) REFERENCES users(user_id)
);


-- INSERT USERS
INSERT INTO users (name, phone, email, role, password)
VALUES ('Admin', '9876543210', 'admin@example.com', 'ADMIN', 'admin123');

INSERT INTO users (name, phone, email, role, password)
VALUES ('Ravi', '9123456780', 'ravi@example.com', 'OWNER', 'ravi123');

INSERT INTO users (name, phone, email, role, password)
VALUES ('Sneha', '9988776655', 'sneha@example.com', 'OWNER', 'sneha123');



