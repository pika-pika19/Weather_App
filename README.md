# 🌦 Weather App — Full Stack Weather Search Application

This is a full-stack weather application built using **Spring Boot (backend)** and **React + Vite (frontend)**.  
Users can search for any city and view real-time weather information such as temperature, humidity, wind speed, and weather conditions.

This project was built as part of a coding assessment.

---

## 📁 Project Structure

```
Weather_App/
│
├── backend/
│   └── weather-service/
│       ├── pom.xml
│       ├── mvnw / mvnw.cmd
│       ├── src/main/java/... (backend source)
│       └── src/main/resources/application.yml
│
└── frontend/
    └── weather-ui/
        ├── package.json
        ├── vite.config.js
        ├── src/
        └── public/
```

---

## 🚀 Backend (Spring Boot)

### ▶ How to Run

Navigate to backend:

```bash
cd backend/weather-service
```

Run using Maven Wrapper (Windows):

```bash
mvnw spring-boot:run
```

Or using system Maven:

```bash
mvn spring-boot:run
```

### ▶ Backend Runs At:
```
http://localhost:8080
```

---

## 🔥 Backend API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/weather?city={name}` | Get weather details for a city |
| GET | `/api/v1/health` | Health check |
| GET | `/api/v1/cache/stats` | View cache hit/miss metrics |

Caching is implemented using **Caffeine** with TTL and max size.

---

## 💻 Frontend (React + Vite)

### ▶ How to Run

Navigate to frontend:

```bash
cd frontend/weather-ui
```

Install dependencies:

```bash
npm install
```

Start development server:

```bash
npm run dev
```

### ▶ Frontend Runs At:
```
http://localhost:5173
```

The UI includes:

- City search bar  
- Temperature  
- Feels Like  
- Humidity  
- Wind Speed  
- Weather condition  
- Icons  
- Day/Night theme toggle  
- Last updated timestamp  

---

## 🛠️ Technologies Used

### Backend
- Java
- Spring Boot
- Spring Web
- Caffeine Cache
- Lombok

### Frontend
- React
- Vite
- JavaScript (ES6+)

---

## 🎯 Assessment Requirements — Completed

✔ Weather search by city  
✔ Backend REST API  
✔ Caching (Caffeine)  
✔ Clean modern UI  
✔ Backend + frontend run locally  
✔ Proper folder structure  
✔ Public GitHub repository  

---

## 👨‍💻 Author
Shivraj Deshmukh

---

## 📩 Submission

Repository Link:  
**https://github.com/pika-pika19/Weather_App**

