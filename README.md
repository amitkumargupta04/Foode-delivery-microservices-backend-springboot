# Food Delivery Microservices

A production-style food delivery backend built using Java, Spring Boot, and Microservices architecture.

## Tech Stack

- Java 17+
- Spring Boot
- Spring Cloud
- Spring Cloud Gateway
- Netflix Eureka
- Spring Data JPA / Hibernate
- PostgreSQL
- Spring Security
- JWT
- OpenFeign
- Apache Kafka
- AWS S3
- Razorpay
- Docker
- Swagger / OpenAPI

## Architecture

The application follows a microservices-based architecture where each service is responsible for a specific business domain.

### Services

| Service | Responsibility |
|---|---|
| Account Service | Authentication, users, JWT |
| Restaurant Service | Restaurants and menu management |
| Inventory Service | Stock management |
| Order Service | Order management |
| Payment Service | Payments and Razorpay integration |
| Notification Service | Email notifications |
| Admin Service | Restaurant and admin management |
| API Gateway | Single entry point for APIs |
| Service Registry | Eureka service discovery |

## Architecture Flow

```text
                    Client
                      |
                      v
                API Gateway
                      |
        +-------------+-------------+
        |             |             |
        v             v             v
   Account       Restaurant       Order
   Service        Service        Service
                      |             |
                      v             v
                 Inventory       Payment
                  Service         Service
                      |
                      v
                    Kafka
                      |
                      v
              Notification Service

          Service Discovery
               (Eureka)
