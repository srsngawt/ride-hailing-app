# Ride Hailing Backend Service

An AI-Assisted Machine Coding assignment for building an Uber-like ride-hailing backend.

## 🚀 How to Run

1. **Build the project:** `mvn clean install`
2. **Run the application:** `mvn spring-boot:run`
3. **Access Swagger UI (Frontend):** Navigate to `http://localhost:8080/swagger-ui.html` to test all APIs interactively.

## 🏗️ Architecture & Design Decisions

### 1. In-Memory Data Storage
As per the requirements, we used `ConcurrentHashMap` for our Repositories instead of setting up a complex database (like MySQL or Kafka). This fulfills the requirement for complete domain modeling while saving infrastructure setup time during a 2-hour coding round. `ConcurrentHashMap` also inherently helps with basic thread-safety.

### 2. Concurrency Safety (Bonus Requirement)
In `RideService.bookRide()`, we implemented a `ReentrantLock`. This ensures that if two users attempt to book the exact same driver simultaneously, the requests are queued and processed one by one, preventing race conditions where one driver is assigned to multiple rides.

### 3. Strategy Design Pattern
We heavily utilized the **Strategy Pattern** to keep the code extensible (SOLID Principles):
- **`PricingStrategy`**: Extracted into an interface. Currently implemented by `TieredPricingStrategy`. If Surge Pricing needs to be added, a new strategy or a decorator can be created without modifying the core booking logic.
- **`DriverMatchingStrategy`**: Extracted into an interface. Currently implemented by `NearestDriverMatchingStrategy`. It easily allows swapping to a "Highest-Rated Driver" logic in the future.

### 4. Free Upgrade Logic
The matching strategy automatically searches for a SEDAN if a requested HATCHBACK is unavailable. It flags the ride with `isFreeUpgrade=true`. The Pricing Engine then respects this flag and bills the user based on the `requestedCarType` (Hatchback rates) instead of the `assignedCarType` (Sedan rates).

### 5. Distance Calculation
For simplicity, we assumed coordinates (x, y) represent distances in kilometers and used Euclidean distance formula for radius and fare calculations.

## 🤖 AI Usage Note
AI was used to scaffold the boilerplate Spring Boot structure (POM, basic Models) and generate standard JUnit tests rapidly. The core logic (ReentrantLocks, Strategy routing, Upgrade condition) was heavily guided by explicit prompts to ensure the design met the exact expectations of the problem statement.
