# 📚 Online Book Store API

![Java](https://img.shields.io/badge/Java-17-orange.svg?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.1-brightgreen.svg?style=for-the-badge&logo=springboot)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6.x-green.svg?style=for-the-badge&logo=springsecurity)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg?style=for-the-badge&logo=mysql)
![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg?style=for-the-badge&logo=docker)

## 📖 Introduction
According to global e-commerce trends, the demand for digital and physical book delivery is growing rapidly. This project is a robust, scalable backend REST API for a modern Online Book Store. It is designed to ensure high performance, strong data validation, and secure role-based access control (RBAC), providing a seamless experience for both customers and administrators.

---

## 💾 Technologies Stack:
[![My Skills](https://skillicons.dev/icons?i=java,spring,docker,aws,postman,maven,mysql,linux)](#)
* Java (v17)
* Spring Framework(Web, Security, Validation, Data-jpa) (v4.0.1)
* MySQL (v8.0)
* Liquibase (v4.29.2)
* Lombok (v1.18.36)
* Mapstruct (v1.5.5)
* Maven (v3.8+)
* JWT (v0.13.0)
* Junit (v5.0+), Mockito
* Swagger / OpenAPI (v3.0)
* Docker
* AWS

## 📝 Architecture Overview
The application strictly follows the **Layered (N-Tier) Architecture** pattern to ensure separation of concerns and maintainability:
* **Controller Layer:** Exposes RESTful endpoints, handles HTTP requests, and performs input validation.
* **Service Layer:** Contains core business logic, isolating controllers from data access details.
* **Repository Layer:** Interacts with the database using Spring Data JPA.
* **DTO & Mapping Layer:** Uses MapStruct to safely transfer data between the internal domain models and external clients.
* **Security Layer:** Implements stateless JWT-based authentication and authorization.

## 📍 API Endpoints

### Authentication (`/auth`)
* `POST /registration` - Register a new user
* `POST /login` - Authenticate user and receive a JWT token

### Books (`/books`)
* `GET /books` - Retrieve all books (with pagination)
* `GET /books/{id}` - Retrieve a specific book
* `GET /books/search` - Filter books dynamically (by title, author, price)
* `POST /books` - Add a new book 🔒 *(Admin only)*
* `PUT /books/{id}` - Update book details 🔒 *(Admin only)*
* `DELETE /books/{id}` - Soft delete a book 🔒 *(Admin only)*

### Categories (`/categories`)
* `GET /categories` - Retrieve all categories
* `GET /categories/{id}/books` - Get all books in a specific category
* `POST /categories` - Create a new category 🔒 *(Admin only)*
* `PUT /categories/{id}` - Update a category 🔒 *(Admin only)*
* `DELETE /categories/{id}` - Delete a category 🔒 *(Admin only)*

### Shopping Cart (`/cart`) & Orders (`/orders`)
* `GET /cart` - View current user's shopping cart
* `POST /cart` - Add an item to the cart
* `PUT /cart/items/{id}` - Update item quantity
* `POST /orders` - Place an order from the cart
* `GET /orders` - View user's order history
* `PATCH /orders/{id}` - Update order status 🔒 *(Admin only)*

---

## 🎯 Database Schema Relationship Diagram

![Database Schema](https://github.com/user-attachments/assets/acff1d6f-7809-4f4f-a1cd-9f5b7a972084)

> [!IMPORTANT]
> Liquibase managing tables are also present (databasechangelog and databasechangeloglock). You won't see them in the diagram to avoid complexity.

---

## 🧪 Testing & Quality Assurance
Unlike basic CRUD apps, this project heavily emphasizes reliability through comprehensive testing:
* **Unit Tests:** Controllers and Services are isolated using `Mockito` to verify business logic and HTTP mapping efficiently.
* **Integration Tests:** Used `MockMvc` combined with a test database (H2/Testcontainers) to test the entire flow (Controller -> Service -> Repository -> DB).
* **Security Testing Focus:** Successfully resolved complex context-loading issues between `JwtAuthenticationFilter` and MockMvc's `@WithMockUser`, ensuring accurate `401 Unauthorized` and `403 Forbidden` validations.


## 📬 Postman Collection & API Testing

To make exploring and testing the API as seamless as possible, I have prepared a complete, pre-configured Postman collection. You can access all endpoints, complete with request bodies and parameters, directly via the web.

👉 **[View the Interactive Postman Collection Here](https://nicelod362-9554410.postman.co/workspace/Niky-Endy's-Workspace~5690ab0d-9a40-473c-861d-98198a3f4048/collection/51898951-9c5c56a3-c834-44d1-981a-714bad5199d6?action=share&source=copy-link&creator=51898951)**

### 🛠 How to test the endpoints:
1. **Access the Collection:** Click the link above to open the collection in your browser or Postman app. You can easily fork it into your own workspace.
2. **Base URL:** The application is configured to run locally on port `8080` with the `/api` prefix. All requests in the collection automatically target `http://localhost:8080/api`.
3. **Authentication Flow (Important!):**
    * The API uses stateless JWT authentication. Start by executing the `POST /auth/login` request using valid credentials.
    * The server will respond with a JWT token. Copy this token string.
    * For any secured endpoints (e.g., creating a book, accessing the cart), navigate to the **Authorization** tab in Postman, select **Bearer Token**, and paste your token. You now have full access!
---

## 📥 How to Clone and Run the Project
Follow these steps to clone the project from GitHub and run it on your local machine:

1️⃣ Clone the Repository
Open your terminal or command prompt, and run the following commands:

```
git clone https://github.com/NaKravon/Online-book-store.git
cd Online-book-store
```

2️⃣ Make sure you have the following installed:
```
Java JDK (version 17 or higher recommended)

Maven (for building and running the project)

MySQL
```

You can check this using cmd commands:

```
java --version
mvn --version
mysql --version
```

3️⃣ Configure the Database
Check the `src/main/resources/application.properties` file for database configuration and adjust the database credentials in application.properties.

```
spring.datasource.url=jdbc:mysql://localhost:3306/bookstore
spring.datasource.username=your_mysql_user
spring.datasource.password=your_mysql_password
```

4️⃣ Build and Run the Application
Run the following commands in the project directory:

```
mvn clean package
mvn spring-boot:run
```

---

## 📥 How to Run Locally

#### **_Step 1: Clone the Repository_**:

https://github.com/NaKraVon/Online-book-store
```bash
git clone https://github.com/NaKraVon/Online-book-store.git
cd online-book-store
```

#### **_Step 2: Set Up Environment Variables_**:

Copy the environment template
```bash
cp .env.template .env
```

#### **_Step 3: Configure Your .env File_**:

Open the .env file and fill in the following variables:

**Database Configuration:**
```
MYSQL_USER=appuser
MYSQL_PASSWORD=your_password
MYSQL_ROOT_PASSWORD=mysql2610
MYSQL_DATABASE=book_store

MYSQL_LOCAL_PORT=3308
MYSQL_DOCKER_PORT=3306
```

**Spring Boot Configuration**
```
SPRING_LOCAL_PORT=8088
SPRING_DOCKER_PORT=8080
DEBUG_PORT=5005
```
**⚠️ Important:**
Change MYSQL_ROOT_PASSWORD to your own secure password
Make sure ports don't conflict with other services

#### **_Step 4: Maven repackage_**:
Repackage the project with command:
```bash
mvn clean package
```

#### **_Step 5: Run with Docker_**:

Start all services
```bash
docker-compose up --build
```

#### **_Step 6: Verify the Setup_**:

After successful startup, services will be available at:

API: http://localhost:8088

Swagger UI: http://localhost:8088/api/swagger-ui.html

MySQL: localhost:3308

---
## 🧠 Challenges Faced & Overcome
Building this application was a tremendous learning experience. Here are a few notable challenges I successfully resolved:

* **Adapting to Spring Boot 4:** Building this project on the cutting-edge Spring Boot 4 framework introduced a steep learning curve. Many familiar configurations, classes, and underlying dependencies were significantly modified, deprecated, or entirely replaced compared to previous versions.
    * *Solution:* I actively studied the official Spring release notes and documentation to navigate the breaking changes. This required refactoring obsolete approaches, adapting to the newest Spring Security configuration standards, and learning how to properly set up the latest components. It greatly improved my ability to quickly adapt to major framework updates.
* **Integration Testing with Spring Security:** While testing secured endpoints (e.g., updating a book) using `MockMvc`, tests were returning unexpected `403 Forbidden` errors instead of `404 Not Found`. Additionally, the custom `JwtAuthenticationFilter` interfered with the `@WithMockUser` context.
    * *Solution:* I explicitly allowed `DispatcherType.ERROR` in the `SecurityFilterChain` to unmask true HTTP statuses. I also refactored the test setup to manual `MockMvcBuilders.webAppContextSetup()` initialization, applying the security config directly to ensure the test context loaded in the correct order.
* **Database Referential Integrity:** When implementing the DELETE operation for Categories, I encountered a `DataIntegrityViolationException` because some books were still referencing the category being deleted via the `books_categories` table.
    * *Solution:* I addressed this architectural issue by implementing **Soft Delete** using Hibernate's `@SQLDelete` and `@Where` annotations on the domain entities. This allowed me to safely mark records as deleted without breaking foreign key constraints or losing historical data.

---

## 📞 Contacts:
[![My Skills](https://skillicons.dev/icons?i=gmail)](mailto:shcherbina.at.work@gmail.com)
[![My Skills](https://skillicons.dev/icons?i=github)](https://github.com/NaKraVon)
[![My Skills](https://skillicons.dev/icons?i=linkedin)](https://www.linkedin.com/in/олександр-щербина-4188072bb)

> [!NOTE]
> For business communication you can send an email on shcherbina.at.work@gmail.com