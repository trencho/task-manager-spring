# Task Manager Spring Boot Application

This is a RESTful API built with Spring Boot for managing tasks. It allows users to:

- **Register** for an account
- **Login** to access their tasks
- **Create** new tasks
- **View** a list of all their tasks
- **Update** existing tasks
- **Delete** tasks they no longer need

**Technologies Used**

- Spring Boot: Framework for building production-ready Spring applications
- Spring Security: Robust security framework for authentication and authorization
- Spring Data MongoDB: Provides integration with MongoDB database
- JWT (JSON Web Token): Used for stateless, secure authentication
- Maven: Dependency management and build tool

**Features**

- User Authentication: Users need to register and login to access the API.
- JWT-based Authentication: Securely authenticate users and protect routes using JWTs.
- Task Management: CRUD (Create, Read, Update, Delete) operations for tasks.
- MongoDB Database: Stores user information and task data.

**Getting Started**

1. Prerequisites:
    - Java 21
    - Maven
    - MongoDB
2. Clone the repository:
    - git clone https://github.com/your-username/task-manager-spring.git
3. Configure application.yml:
    - Update the MongoDB connection URI (spring.data.mongodb.uri) with your database credentials.
    - Set a strong JWT secret key (jwt.secret).
4. Build and Run:
    - mvn clean install
    - mvn spring-boot:run

**API Endpoints**

**Authentication:**

- POST /api/auth/register: Register a new user account.
- POST /api/auth/login: Login with username and password to receive a JWT token.

**Tasks:**

- GET /api/tasks: Get a list of all tasks for the authenticated user.
- POST /api/tasks: Create a new task.
- PUT /api/tasks/{id}: Update an existing task by ID.
- DELETE /api/tasks/{id}: Delete a task by ID.

**Note:** All task endpoints require a valid JWT token in the Authorization header (e.g., Bearer your-jwt-token).

**Future Improvements**

- Implement pagination for task lists.
- Add search functionality to filter tasks.
- Implement task priority levels.
- Integrate with a frontend framework (e.g., React, Angular, Vue.js) to create a user interface.
