
CREATE DATABASE companydb;

CREATE USER company WITH PASSWORD 'company@32!';

GRANT ALL PRIVILEGES ON DATABASE companydb TO company;

ALTER DATABASE companydb OWNER TO company;

Now-> Try new connection for this new schema with new User.


-------------------------------

CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(10,2) NOT NULL,
    quantity INT NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);


CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    order_total NUMERIC(10,2),
    discount_applied NUMERIC(10,2),
    created_at TIMESTAMP
);

CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT,
    product_id BIGINT,
    quantity INT,
    unit_price NUMERIC(10,2),
    total_price NUMERIC(10,2)
);

insert into users values( 1, 'test1', 'test@123', 'ADMIN');
insert into users values( 2, 'test2', 'test2@123', 'USER');
insert into users values( 3, 'test3', 'test3@123', 'PREMIUM_USER');

select * from users;
