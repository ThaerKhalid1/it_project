
CREATE DATABASE warehouse_management;
USE warehouse_management;


CREATE TABLE admins (
    admin_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(15) NOT NULL,
    email VARCHAR(30) UNIQUE,
    phone VARCHAR(20),
    address TEXT,
    access_level VARCHAR(20)
);


CREATE TABLE employees (
    employee_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(15) NOT NULL,
    email VARCHAR(30) UNIQUE,
    phone VARCHAR(20),
    address TEXT,
    admin_id INT,
    FOREIGN KEY (admin_id) REFERENCES admins(admin_id)
);


CREATE TABLE visitors (
    visitor_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(15) NOT NULL,
    identity_number VARCHAR(50) UNIQUE,
    visit_purpose TEXT,
    visit_date DATE,
    receiving_employee_id INT,
    FOREIGN KEY (receiving_employee_id) REFERENCES employees(employee_id)
);

CREATE TABLE suppliers (
    supplier_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(15) NOT NULL,
    email VARCHAR(30) UNIQUE,
    phone VARCHAR(20),
    address TEXT
);


CREATE TABLE customers (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(15) NOT NULL,
    email VARCHAR(30) UNIQUE,
    phone VARCHAR(20),
    address TEXT
);

CREATE TABLE items (
    item_id INT PRIMARY KEY AUTO_INCREMENT,
    item_name VARCHAR(100) NOT NULL,
    description TEXT,
    item_type VARCHAR(50),
    location VARCHAR(255);
    quantity INT,
    receive_date DATE,
    expiry_date DATE,
    purchase_date DATE;
    supplier_id INT,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(supplier_id)
);

CREATE TABLE orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    order_date DATE,
    customer_id INT,
    item_id INT,
    quantity INT,
    status VARCHAR(20),
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    FOREIGN KEY (item_id) REFERENCES items(item_id)
);

CREATE TABLE storage (
    storage_id INT PRIMARY KEY AUTO_INCREMENT,
    item_id INT,
    quantity INT,
    storage_date DATE,
    expiry_date DATE,
    FOREIGN KEY (item_id) REFERENCES items(item_id)
);
