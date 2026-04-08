# 🎟️ EventSphere – Scalable Event Ticket Booking Backend System

## 🚀 Overview
EventSphere is a production-oriented backend system for event ticket booking built using Spring Boot.  
It focuses on **scalability, reliability, and real-world backend challenges** like concurrency handling, caching, idempotency, and rate limiting.

---

## 🔥 Key Features

### 👤 Authentication & Security
- JWT-based authentication
- Role-based access control (USER, ADMIN)

### 🎫 Event Management
- Create, update, and cancel events (Admin)
- Manage event details (location, date, seats, price)

### 🔍 Event Discovery
- View all events with filtering
- Sorting & pagination support

### 🎟️ Booking System
- Concurrency-safe **seat locking mechanism**
- Prevents double booking
- Booking confirmation after payment
- Booking cancellation support

### 💳 Payment Simulation
- Handles success/failure scenarios
- Integrated with booking workflow

### ⏱️ Scheduler
- Automatically releases expired seat locks

### ⚡ Performance & Optimization
- Redis caching for event APIs
- Reduced database load and faster response times

### 🔁 Idempotency
- Prevents duplicate bookings during retries
- Ensures safe and consistent API behavior

### 🚦 Rate Limiting (Advanced)
- Implemented **Sliding Window Rate Limiter using Redis Sorted Sets**
- Prevents API abuse and booking spam
- Per-user request control with time-based expiry

### 📊 Observability
- AOP-based logging
- Global exception handling

---

## 🛠️ Tech Stack

- **Backend:** Java, Spring Boot
- **Security:** Spring Security (JWT)
- **Database:** PostgreSQL
- **ORM:** Hibernate (JPA)
- **Caching & Rate Limiting:** Redis
- **Other:** AOP, Scheduler

---

## 🧠 Key Concepts Implemented

- Concurrency Handling (Seat Locking)
- Transaction Management (`@Transactional`)
- Idempotent APIs
- Redis Caching
- Sliding Window Rate Limiting
- Scheduler-based automation
- Layered Architecture

---

## 📌 API Endpoints

| Method | Endpoint | Description |
|-------|---------|------------|
| POST | /auth/signup | Register user |
| POST | /auth/login | Login & get JWT |
| GET | /events | Get all events |
| POST | /bookings | Create booking |
| POST | /bookings/confirm | Confirm booking |
| PATCH | /booking/{id}/cancel | Cancel booking |

---

## ⚙️ How It Works (Booking Flow)

```text
Idempotency → Rate Limiting → Seat Lock → Payment → Booking Confirmation