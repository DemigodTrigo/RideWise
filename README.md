# RideWise

**Author:** Vishal Verma  
**Purpose:** Overview and setup guide for the RideWise console application.

## Overview
RideWise is a console-based ride-booking simulation. It supports rider and driver registration, ride requests, driver matching, ride completion/cancellation, fare calculation, and fare receipts.

## Technology
- Java (use the version configured in `pom.xml`; Java 21 is recommended if configured)
- Maven
- JUnit 5
- In-memory collections
- IntelliJ IDEA (recommended)

## Project structure
```text
RideWise/
├── pom.xml
├── README.md
├── docs/
│   ├── Requirements.md
│   ├── Class_Model.md
│   ├── Object_Relationships.md
│   ├── SOLID_Reflection.md
│   ├── Exception_Handling.md
│   └── Testing.md
└── src/
    ├── main/java/com/airtribe/ridewise/
    └── test/java/com/airtribe/ridewise/
```

## Prerequisites
1. Install a JDK matching the version configured in `pom.xml`.
2. Install Maven, or use the Maven wrapper if provided.
3. Open the project in IntelliJ and reload Maven dependencies.

Verify your setup:
```bash
java -version
mvn -version
```

## Run
From the project root:
```bash
mvn clean compile
```
You can run `Main.java` from IntelliJ. Alternatively, `mvn exec:java -Dexec.mainClass="com.airtribe.ridewise.Main"` works only if the Maven Exec Plugin is configured in `pom.xml`.

## Run automated tests
```bash
mvn test
```
In IntelliJ, right-click the test source folder or a test class and choose **Run**.

## Quick demo
1. Select `1`; register rider `Vishal`, location `10`.
2. Select `2`; register driver `Rahul`, location `12`, vehicle `CAR`.
3. Select `2`; register driver `Aman`, location `30`, vehicle `BIKE`.
4. Select `6`; copy the rider ID.
5. Select `3`; enter that exact Rider ID and distance `10`.
6. Copy the Ride ID, select `4`, and complete the ride.
7. Select `9` and enter the Ride ID to view the receipt.

With the current sample default rate of 15.00 per distance unit, distance `10` produces fare `150.00`. Confirm rates against the assignment brief.

## Current limitations
- Locations are numeric placeholders, not real addresses or GPS coordinates.
- Data is stored in memory and disappears when the application exits.
- Fare rates are sample values until confirmed against assignment requirements.
- The menu currently uses `NearestDriverStrategy` and `DefaultFareStrategy` unless modified.
- This is an educational console application, not a production ride-hailing platform.

## Documentation
- [Requirements](docs/Requirements.md)
- [Class Model](docs/Class_Model.md)
- [Object Relationships](docs/Object_Relationships.md)
- [SOLID Reflection](docs/SOLID_Reflection.md)
- [Exception Handling](docs/Exception_Handling.md)
- [Testing Plan](docs/Testing.md)
