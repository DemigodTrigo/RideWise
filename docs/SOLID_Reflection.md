# RideWise SOLID Reflection

**Author:** Vishal Verma  
**Purpose:** Explain design choices using SOLID principles and record improvement areas.

## Single Responsibility Principle (SRP)
`RiderService` manages riders, `DriverService` manages drivers, `RideService` coordinates ride use cases, strategies implement algorithms, and `Main` handles console interaction. Keep formatting and input parsing out of business services.

## Open/Closed Principle (OCP)
`RideMatchingStrategy` and `FareStrategy` allow new algorithms to be added without rewriting the core ride workflow. Examples include a weekend fare strategy or a different driver selection strategy. Add configuration/menu selection if users must choose strategies at runtime.

## Liskov Substitution Principle (LSP)
Every `RideMatchingStrategy` implementation should satisfy the same selection contract. Every `FareStrategy` implementation should calculate fares for valid rides and report invalid conditions consistently. Tests should verify no-driver behavior and fare calculation for each implementation.

## Interface Segregation Principle (ISP)
Matching and fare interfaces are small and focused. A matching strategy does not need fare methods, and a fare strategy does not need driver-selection methods.

## Dependency Inversion Principle (DIP)
`RideService` receives strategy interfaces through its constructor instead of creating concrete strategies internally. This supports substitution and makes testing easier. Validate injected dependencies with `Objects.requireNonNull`.

## Additional principles
- **Encapsulation:** State is private and updated through methods.
- **Composition:** Services collaborate with other services and strategies.
- **Program to interfaces:** Strategy consumers use interfaces rather than concrete algorithms.
- **Fail fast:** Validate inputs before mutating state.
- **Testability:** Business logic is testable independently of the console.

## Trade-offs and improvements
- In-memory storage is simple but data disappears on exit.
- Numeric locations are placeholders, not geographic coordinates.
- If active drivers are always marked unavailable, a least-active strategy cannot meaningfully balance active rides. Clarify the required driver-capacity rules.
- Duplicate-name rejection is a policy choice; UUIDs already provide unique identity.
- Fare rates are sample values and must match the assignment specification.

SOLID principles should be demonstrated by code structure and tests, not only described in this document.
