# 🎟️ EventSphere - Event Ticket Booking System

## 🚀 Overview
EventSphere is a backend-focused event ticket booking system built using Spring Boot. It handles user authentication, event management, seat booking with concurrency control, and payment simulation.

---

## 🔥 Features

### 👤 Authentication & Security
- JWT-based authentication
- Role-based access (USER, ADMIN)

### 🎫 Event Management
- Create, update, delete events (Admin)
- Manage event details (location, date, seats, price)

### 🔍 Event Discovery
- View all events
- Filtering (location, price, status)
- Sorting & Pagination

### 🎟️ Booking System
- Seat locking mechanism to prevent double booking
- Booking confirmation after successful payment
- Booking cancellation

### 💳 Payment
- Payment simulation (success/failure handling)

### ⏱️ Scheduler
- Auto-release locked seats after expiry

### 📧 Notifications
- Async email for booking confirmation

### ⚡ Backend Enhancements
- AOP logging (method tracking, execution time)
- Global exception handling
- Clean layered architecture

---

## 🛠️ Tech Stack

- Java
- Spring Boot
- Spring Security (JWT)
- Hibernate (JPA)
- PostgresSQL
- AOP
- Scheduler

---

## 📌 API Endpoints

- POST /auth/signup
- POST /auth/login
- GET /events
- POST /bookings
- PATCH /booking/{id}/cancel

---

## 🧠 Key Concepts Used

- Concurrency Handling (Seat Locking)
- Transaction Management (@Transactional)
- Scheduling (Auto Seat Release)
- AOP Logging
- REST API Design

---

## 👩‍💻 Author

Sridevi