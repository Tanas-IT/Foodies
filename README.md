# 🍔 Foodies – Online Food Ordering System

Foodies is a full-stack web application for managing and ordering food online.  
The system is divided into **three main projects**:

1. **Admin Web** – Manage food items and customer orders.
2. **Customer Web** – Browse, filter, and order food with online payment support.
3. **Back-End API** – Built with Spring Boot and MongoDB, serving as the backbone of the system.

---

## 🚀 Live Links

- **Admin Web**: [https://foodies-delta-one.vercel.app/](https://foodies-delta-one.vercel.app/)
- **Customer Web**: [https://foodies-k3y7.vercel.app/](https://foodies-k3y7.vercel.app/)
- **API**: [https://foodies-be-latest.onrender.com](https://foodies-be-latest.onrender.com)

---

## 📂 Project Structure

- **foodies-admin** → Admin dashboard for managing products and orders.
- **foodies-client** → Customer-facing food ordering website.
- **foodies-api** → Spring Boot back-end API with MongoDB database.

---

## ✨ Features

### 🔹 Admin Web

- Add, edit, and delete food items
- View food list with details
- Change order status (e.g., pending, confirmed, delivered)

### 🔹 Customer Web

- View all available food items
- Filter by category
- View food details
- Add food to cart
- Checkout & payment integration with **VNPay**
- View order history
- Register/Login account

### 🔹 Back-End API

- RESTful APIs built with **Spring Boot**
- **MongoDB** as the database
- Deployed on **Render**

---

## 🛠️ Technologies Used

- **Frontend**: React, Bootstrap, Vercel Deployment
- **Backend**: Spring Boot, REST API, JWT
- **Database**: MongoDB
- **Payment Gateway**: VNPay
- **Hosting**:
  - Frontend → Vercel
  - Backend → Render

---

## 📸 Screenshots

### 🔹 Admin Panel

- **Add Food**  
  ![Add Food Screenshot](https://res.cloudinary.com/deas18dyx/image/upload/v1758366369/Add_Food_owivbp.png)

- **View and Remove Food**  
  ![Admin View And Remove Food Screenshot](https://res.cloudinary.com/deas18dyx/image/upload/v1758366462/View_Food_evobwo.png)

- **Order Management**  
  ![Admin Order Management Screenshot](https://res.cloudinary.com/deas18dyx/image/upload/v1758366370/List_Order_pijmcl.png)

---

### 🔹 Customer Website

- **Login Page**  
  ![Customer Login Screenshot](https://res.cloudinary.com/deas18dyx/image/upload/v1758366371/Sign_in_jrxaql.png)

- **Register Page**  
  ![Customer Register Screenshot](https://res.cloudinary.com/deas18dyx/image/upload/v1758366375/Sign_up_af97gq.png)

- **Home Page (Food List)**  
  ![Customer Home Screenshot](https://res.cloudinary.com/deas18dyx/image/upload/v1758366371/Home_Page_yoz6r3.png)

- **Food Details**  
  ![Customer Food Details Screenshot](https://res.cloudinary.com/deas18dyx/image/upload/v1758366370/Food_Detail_xsyt2c.png)

- **Cart**  
  ![Customer Cart Screenshot](https://res.cloudinary.com/deas18dyx/image/upload/v1758366369/Cart_ydc4ae.png)

- **Checkout**  
  ![Check out Screenshot](https://res.cloudinary.com/deas18dyx/image/upload/v1758366371/Payment_nl559d.png)

- **Order History**  
  ![Customer Order History Screenshot](https://res.cloudinary.com/deas18dyx/image/upload/v1758366370/History_Order_q8rakb.png)

- **Contact Page**  
  ![Contact Screenshot](https://res.cloudinary.com/deas18dyx/image/upload/v1758366369/Contact_bgbbt2.png)

---

### 🔹 API (Postman)

- **API Endpoints Example**  
  ![API Screenshot](https://res.cloudinary.com/deas18dyx/image/upload/v1758366434/Test_API_ornlwr.png)

---

## ⚡ Installation & Setup (Local Development)

### 1. Clone the repository

```bash
git clone https://github.com/Tanas-IT/Foodies.git
```

### 2. Backend (Spring Boot)

```bash
cd foodiesapi
mvn spring-boot:run
Backend will start at: http://localhost:8080
```

### 3. Admin Web

```bash
cd adminpanel
npm install
npm run dev
Runs at: http://localhost:3000
```

### 4. Customer Web

```bash
cd foodies
npm install
npm run dev
Runs at: http://localhost:3001
```

## 🐳 Run with Docker Compose

This project provides a `docker-compose.yml` file to quickly run all services (API + Admin + Client).

### 1. Build & start containers

```bash
docker-compose up --build
```

### 2. Run in detached mode

```bash
docker-compose up -d

```

### 3. Stop containers

```bash
docker-compose down

```

### 4. Access services

- Customer Web → http://localhost:3000
- Admin Web → http://localhost:3001
- API → http://localhost:8080
