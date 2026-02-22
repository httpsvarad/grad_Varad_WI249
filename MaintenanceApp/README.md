# Layout Maintenance Management System

A role-based **Java console application** for managing residential layout sites, maintenance generation, payment tracking, and site update approvals using **JDBC** and **PostgreSQL**.

---

## Overview

The Layout Maintenance Management System is designed to streamline the administration of residential layout properties. The system supports two user roles:

* **Administrator** – Manages sites, maintenance generation, payments, and update approvals.
* **Owner** – Views owned sites, pays maintenance, and submits update requests.

The application follows **OOP principles** and implements the **DAO (Data Access Object) pattern** to separate business logic from database operations.

---

## Features

### Authentication

* Secure login system
* Role-based access control (ADMIN / OWNER)

---

### Administrator Capabilities

* Add new site
* Edit site details (dimensions, type, owner, occupancy)
* Remove site
* Generate maintenance for all sites
* View payment records
* Approve or reject owner update requests

---

### Owner Capabilities

* View owned sites
* Pay maintenance (partial or full payment supported)
* Submit update requests (site type / occupancy status)

---

## System Architecture

The application is structured into:

* **Model Layer**

  * `User`
  * `Site`
  * `SiteUpdateRequest`
  * Enums: `Role`, `SiteType`, `UpdateStatus`

* **DAO Layer**

  * `AdminDAO`
  * `OwnerDAO`
  * `AdminDBOperations`
  * `OwnerOperations`

* **Service Layer**

  * `AuthService`
  * `DBConnection`

* **Controller**

  * `MenuController`

* **Entry Point**

  * `LayoutAppMain`

---

## 🗄️ Database Configuration

### Database

* PostgreSQL
* JDBC Driver: `org.postgresql.Driver`
* Default Database Name: `my_db`

---

## Maintenance Calculation Logic

```
If Occupied     → Area × 9
If Not Occupied → Area × 6
```

---

## Setup Instructions

### 1️⃣ Create Database

```sql
CREATE DATABASE my_db;
```

---

### 2️⃣ Update Database Credentials

Modify the `DBConnection` class:

```java
DriverManager.getConnection(
    "jdbc:postgresql://localhost:5432/my_db",
    "postgres",
    "your_password"
);
```

---

### 3️⃣ Compile and Run

```bash
javac LayoutAppMain.java
java LayoutAppMain
```
