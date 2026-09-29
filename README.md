# Fruit Manager (Quarkus + Vue.js)

A minimalist and modern full-stack web application to manage fruits, built using Java and JavaScript ecosystems.

## 🚀 Tech Stack

- **Backend:** Quarkus (Java 17), RESTEasy Reactive, Hibernate ORM with Panache.
- **Frontend:** Vue.js (Vite), Modern Vanilla CSS Core.
- **Database:** MySQL 8.0.
- **Infrastructure:** Docker & Docker Compose.

## 🛠️ How to Run

### Prerequisites
Make sure you have **Docker Desktop** installed on your machine.

### 1. Build the Backend
Navigate to the API folder and compile the project skipping tests:
```cmd
cd api-quarkus
mvnw clean package -DskipTests
```

### 2. Start the Docker Infrastructure
From the root folder, spin up the database and the API containers:
```cmd
docker compose up -d --build
```
The API will be live at `http://localhost:8080/frutas`.

### 3. Run the Frontend
Navigate to the Vue folder, install dependencies, and start the Vite dev server:
```cmd
cd ..
cd front-vue.js
npm install
npm run dev
```
Open your browser at `http://localhost:5173` to manage your fruits.

## 🔒 Architecture Note
- **CORS Handling:** Managed globally via custom Quarkus reactive filters for smooth, multi-origin resource sharing with the local client dashboard.
