# UST-project

### Event Management System - Backend Project Requirements

This project involves building a **Spring Boot microservices-based Event Management System**. The system consists of the following microservices:

1. **Authentication Service** (manages user authentication and roles)
2. **Event Service** (handles event creation and management)
3. **Ticket Service** (manages ticket generation and registration for events)

All communication between microservices will be routed through an **API Gateway**, and service discovery will be managed by **Eureka** (Service Registry). **Config Server** will handle centralized configuration.

---

### 1. **Project Structure**

- **Authentication Service**: 
  - Responsible for user registration, login, and JWT token generation.
  - Roles: 
    - `ROLE_ADMIN`: Can create, update, delete events.
    - `ROLE_USER`: Can view events and register for them.
  
- **Event Service**:
  - Admins can create, update, delete events.
  - Users can view events and register for participation.

- **Ticket Service**:
  - Generates tickets when users register for events.
  - Admins can track all ticket registrations.

- **API Gateway**:
  - Filters all incoming requests for authentication and authorization.
  - Routes the requests to appropriate microservices (Authentication, Event, Ticket services).
  
- **Service Registry (Eureka)**: 
  - Handles dynamic discovery of services.
  
- **Config Server**: 
  - Centralized configuration for all microservices, allowing externalized config across different environments.

---

### 2. **Requirements & Features**

#### **Authentication Service**:
- **Endpoints**:
  - `/auth/register` (POST): User registration.
  - `/auth/login` (POST): User login and JWT generation.
  - `/auth/validate` (GET): Validates JWT for securing other endpoints.
  
- **JWT-based Authentication**:
  - Issues JWT tokens to authenticate users.
  - Admins are assigned `ROLE_ADMIN`, and regular users are assigned `ROLE_USER`.
  
- **Dependencies**:
  - Spring Security
  - JWT for token generation and validation.
  
#### **Event Service**:
- **Endpoints**:
  - `/events` (GET): List all events (accessible to users).
  - `/events/{id}` (GET): Get details of a specific event.
  - `/events` (POST) (Admin only): Create a new event.
  - `/events/{id}` (PUT) (Admin only): Update an existing event.
  - `/events/{id}` (DELETE) (Admin only): Delete an event.
  - `/events/{id}/register` (POST) (User only): Register for an event.
  
- **Dependencies**:
  - Spring Data JPA for event management.
  - JWT for securing endpoints.
  
#### **Ticket Service**:
- **Endpoints**:
  - `/tickets/{eventId}` (GET): List all tickets for an event (Admin only).
  - `/tickets/user/{userId}` (GET): View tickets registered by a user.
  - `/tickets/generate` (POST): Generate tickets for users registering for an event.
  
- **Dependencies**:
  - Spring Data JPA for managing ticket generation and registration.

#### **API Gateway**:
- **Features**:
  - Intercepts all incoming requests and filters unauthorized requests.
  - Handles routing to appropriate microservices.
  
- **Dependencies**:
  - Spring Cloud Gateway.
  - Spring Security to enforce JWT validation for protected routes.

#### **Service Registry (Eureka)**:
- **Features**:
  - Manages service discovery for Event, Ticket, and Authentication services.
  
#### **Config Server**:
- **Features**:
  - Centralized configuration for all services.
  - External configuration management for different environments.

---

### 3. **Database Design**

#### **Authentication Service (User Table)**:
```plaintext
User
-----
- id (Long)
- username (String)
- password (String, hashed)
- roles (Set<Role>)
```

#### **Event Service (Event Table)**:
```plaintext
Event
-----
- id (Long)
- title (String)
- description (String)
- location (String)
- date (Date)
- createdBy (String)
```

#### **Ticket Service (Ticket Table)**:
```plaintext
Ticket
------
- id (Long)
- eventId (Long, foreign key to Event)
- userId (Long, foreign key to User)
- registrationDate (Date)
```

---

### 4. **Complete Backend Implementation Overview**

#### **Step 1: Authentication Service**
- Implement a service that manages user registration, login, and JWT token generation.
  
**Key Dependencies**:
- Spring Boot
- Spring Security
- JWT (for authentication)
- Spring Data JPA (for user persistence)
  
**Sample Code for Authentication Service**:
```java
@PostMapping("/register")
public ResponseEntity<?> registerUser(@RequestBody User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRoles(Set.of(new Role("ROLE_USER")));
    userRepository.save(user);
    return ResponseEntity.ok("User registered successfully");
}

@PostMapping("/login")
public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginRequest.getUsername(),
            loginRequest.getPassword()
        )
    );
    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = tokenProvider.generateToken(authentication);
    return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
}
```

#### **Step 2: Event Service**
- CRUD operations for events (Create, Read, Update, Delete).
  
**Key Dependencies**:
- Spring Boot
- Spring Data JPA
- JWT for security
  
**Sample Code for Event Service**:
```java
@PostMapping("/events")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<Event> createEvent(@RequestBody Event event) {
    Event newEvent = eventRepository.save(event);
    return ResponseEntity.ok(newEvent);
}

@GetMapping("/events")
public ResponseEntity<List<Event>> getAllEvents() {
    return ResponseEntity.ok(eventRepository.findAll());
}
```

#### **Step 3: Ticket Service**
- Handle ticket generation and user registration for events.
  
**Key Dependencies**:
- Spring Boot
- Spring Data JPA
  
**Sample Code for Ticket Service**:
```java
@PostMapping("/tickets/generate")
@PreAuthorize("hasRole('USER')")
public ResponseEntity<Ticket> generateTicket(@RequestBody TicketRequest ticketRequest) {
    Ticket ticket = new Ticket(ticketRequest.getEventId(), ticketRequest.getUserId());
    ticketRepository.save(ticket);
    return ResponseEntity.ok(ticket);
}
```

#### **Step 4: API Gateway**
- Use **Spring Cloud Gateway** to route requests.
- Use **Spring Security** in the gateway to filter requests based on JWT tokens.

**Sample Configuration in `application.yml`**:
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: event-service
          uri: lb://EVENT-SERVICE
          predicates:
            - Path=/events/**
          filters:
            - JwtAuthenticationFilter
        - id: ticket-service
          uri: lb://TICKET-SERVICE
          predicates:
            - Path=/tickets/**
            - JwtAuthenticationFilter
```

#### **Step 5: Service Registry (Eureka)**

- Use **Spring Cloud Netflix Eureka** to manage service registration.

#### **Step 6: Config Server**

- Use **Spring Cloud Config** to manage configuration.

---

### 5. **Testing the Application**

1. **Start all services**: Authentication, Event, Ticket, API Gateway, Service Registry, and Config Server.
2. **Register users** (Admin and regular users).
3. **Login**: Authenticate via the Authentication Service and get JWT tokens.
4. **Create Events**: Admins create events through the Event Service.
5. **Register for Events**: Users can register for events through the Ticket Service.
6. **Test Authorization**: Only authenticated users can register, and only admins can manage events.

This setup will help you implement a fully functional Event Management System with multiple microservices while keeping the complexity manageable.