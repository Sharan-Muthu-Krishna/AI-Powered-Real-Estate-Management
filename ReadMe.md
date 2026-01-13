Real Estate Management System

Full-Stack Platform with AI, RAG & Image Forgery Detection

This project is a complete real-estate management system built using Spring Boot, MySQL, FastAPI (RAG) and Flask + TensorFlow (Forgery Detection).
It combines traditional web application features with AI-powered services for smarter property management.

🚀 System Architecture

The system runs as three independent services:

Service	Tech Stack	Purpose
Main Web App	Spring Boot, Thymeleaf, MySQL	Real estate portal, users, properties, authentication
RAG Service	FastAPI + Vector DB	AI-powered property Q&A
Forgery Detection	Flask + TensorFlow	Detect manipulated property images
📦 Requirements

Make sure you have these installed:

Java 17+

Maven

Python 3.9+

MySQL Server

Node is not required

🗄️ Database Setup

Start MySQL:

mysql -u root -p


Then create the database:

CREATE DATABASE realestate_db;


Spring Boot will automatically create tables.

▶️ How to Run the Full System
1️⃣ Start Spring Boot (Main App)

Open terminal in project root:

mvn spring-boot:run


App will run at:

http://localhost:8080

2️⃣ Start RAG AI Service

Open new terminal:

cd rag/ragpickerz
venv\Scripts\activate
uvicorn app.main:app --reload


Runs at:

http://localhost:8000

3️⃣ Start Forgery Detection API

Open another terminal:

cd forgery_detection_api
venv\Scripts\activate
python app.py


Runs at:

http://localhost:5000


Swagger UI:

http://localhost:5000/apidocs

4️⃣ (Optional) MySQL Console
mysql -u root -p

🔐 Login Roles

The system supports:

Admin

Agent

Customer

Roles are stored in the database and used by Spring Security.

🧠 AI Features
🔍 Image Forgery Detection

Uses Error Level Analysis (ELA)

CNN model (MobileNet based)

API endpoint:

POST /detect-forgery

🤖 RAG (Retrieval Augmented Generation)

Allows users to ask questions about properties

Uses vector search + LLM