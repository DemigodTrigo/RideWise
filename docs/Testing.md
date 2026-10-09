# RideWise Testing Plan and Detailed Instructions

**Author:** Vishal Verma  
**Purpose:** Document automated tests, end-to-end console tests, negative cases, regression tests, and evidence.

## 1. Objectives
Verify rider/driver workflows, matching, fare calculation, lifecycle rules, input validation, state consistency, and menu recovery.

## 2. Test types
- **Unit tests:** JUnit tests for services, models, and strategies.
- **Manual tests:** Interactive console checks for prompts, IDs, error messages, and menu recovery.
- **Regression tests:** Rerun the failed test and full suite after each fix.

## 3. Preparation
1. Save all Java files.
2. Ensure package names match folder paths.
3. Confirm JUnit 5 is configured in `pom.xml`.
4. Reload Maven dependencies in IntelliJ if needed.
5. Run `mvn clean test` from the project root.
6. Fix compilation errors before manual testing.

```bash
mvn clean test
mvn clean compile
```

## 4. Start the application
1. Open `src/main/java/com/airtribe/ridewise/Main.java`.
2. Click the Run icon next to `main`.
3. Keep this process running during the test session; records are in memory.
4. Copy the exact IDs printed by the app.
5. If the app restarts, register new records because prior IDs no longer exist.

## 5. Happy-path manual test (one application run)

### TC-01 — Register rider
Choose `1`; enter name `Vishal` and location `10`. Expected: success message and Rider ID.

### TC-02 — Register nearest driver
Choose `2`; enter `Rahul`, location `12`, vehicle `CAR`. Expected: success and Driver ID.

### TC-03 — Register farther driver
Choose `2`; enter `Aman`, location `30`, vehicle `BIKE`. Expected: success and Driver ID.

### TC-04 — View records
Choose `6`, then `7`. Expected: rider and both drivers appear with IDs and details.

### TC-05 — Request ride
Choose `3`; enter the exact Rider ID from TC-01 and distance `10`. Expected: Rahul is selected because position 12 is closer to 10 than position 30. Record the Ride ID.

### TC-06 — View ride
Choose `8`. Expected: ride details and `ASSIGNED` status.

### TC-07 — Complete ride
Choose `4`; enter the Ride ID. Expected: `COMPLETED` and a fare receipt. At the current sample rate 15.00/unit, distance 10 produces `150.00`.

### TC-08 — View receipt
Choose `9`; enter the completed Ride ID. Expected: receipt amount and timestamp.

### TC-09 — Check driver release
Choose `7`. Expected: Rahul is available again and active ride count is zero, if driver state handling is implemented correctly.

### TC-10 — Exit
Choose `0`. Expected: clean exit.

## 6. Negative and boundary tests

Run each scenario, note the actual output, and verify the application remains usable.

| Test ID | Action/input | Expected result |
|---|---|---|
| TC-11 | Menu option `99` | Friendly error; menu repeats |
| TC-12 | Menu input `hello` | Integer validation message; app continues |
| TC-13 | Blank rider name | Registration rejected |
| TC-14 | Blank rider location | Registration rejected |
| TC-15 | Duplicate rider name | Duplicate error if policy is enabled |
| TC-16 | Vehicle `PLANE` | Friendly message listing valid vehicle types |
| TC-17 | ID `not-a-uuid` | Friendly UUID error |
| TC-18 | Valid-format random Rider UUID | Rider-not-found error |
| TC-19 | Valid-format random Ride UUID | Ride-not-found error |
| TC-20 | Distance `-5` | Rejected; no ride or driver state change |
| TC-21 | Distance `0` | Rejected |
| TC-22 | Distance `abc` | Numeric-input error |
| TC-23 | Distance `NaN` | Rejected as non-finite |
| TC-24 | Distance `Infinity` | Rejected as non-finite |
| TC-25 | Request with no available drivers | No-driver message; app continues |
| TC-26 | Complete the same ride twice | Second completion rejected |
| TC-27 | Cancel a completed ride | Rejected; completed status remains |
| TC-28 | Cancel an assigned ride | Ride cancelled; driver released once |
| TC-29 | View receipt before completion | Helpful no-receipt message |
| TC-30 | View riders before registration | Empty-state message |
| TC-31 | View drivers before registration | Empty-state message |
| TC-32 | View rides before requesting any | Empty-state message |
| TC-33 | Enter multiple invalid menu values | App keeps accepting input |
| TC-34 | Restart the application | In-memory records are gone; document limitation |

These are desired behaviors, not proof that the current code already supports every case. Mark a test as failed if the observed behavior differs, fix it, then retest.

## 7. State consistency checks
After a rejected operation, verify:
- No unexpected ride was created.
- Ride status did not change.
- Driver availability and active ride count did not change.
- No incorrect receipt was stored.
- The same valid workflow can still be performed afterward.

Examples: unknown rider ID must not allocate a driver; cancelling a completed ride must not release a driver again; a failed fare calculation must not complete the ride.

## 8. Strategy tests

### Nearest driver
Rider at `10`; available drivers at `12` and `30`; expect driver at `12`. Mark that driver unavailable and verify the other driver is selected. With no eligible drivers, expect `NoDriverAvailableException`.

### Least-active driver
Use drivers with different active counts if the business rules permit them to remain eligible. If every driver with an active ride is excluded, the strategy cannot meaningfully balance load; document and resolve this design decision.

### Default fare
Distance `10` at `15.00` per unit should produce `150.00`. Test invalid distance and rounding.

### Peak fare
Distance `10` at sample rate `20.00` should produce `200.00`. Verify whether the menu can actually select this strategy; it may currently always use the default strategy.

## 9. Run tests in IntelliJ
1. Open `src/test/java`.
2. Right-click the test package or test source directory.
3. Choose **Run All Tests** (wording may vary).
4. Inspect the results tree.
5. Open each failure and compare expected versus actual output.
6. Fix the code or correct an inaccurate test.
7. Rerun the failed test, then the entire suite.

## 10. Run tests with Maven
```bash
mvn clean test
```
Look for `BUILD SUCCESS` and verify the test counts. If Surefire reports are available, inspect `target/surefire-reports/`.

## 11. Record actual results

Do not mark tests as passed before execution.

| Test ID/suite | Expected | Actual result | Status | Notes/defect ID |
|---|---|---|---|---|
| Automated unit suite | All tests pass | Pending | NOT RUN | |
| TC-01 to TC-10 | Happy path succeeds | Pending | NOT RUN | |
| TC-11 to TC-24 | Input validation works | Pending | NOT RUN | |
| TC-25 to TC-34 | Business rules and recovery work | Pending | NOT RUN | |

Allowed statuses: `PASS`, `FAIL`, `BLOCKED`, `NOT RUN`.

## 12. Defect log
For every failure, record:
- Defect ID and test ID
- Steps to reproduce
- Expected result
- Actual result
- Root cause
- Fix
- Retest result

## 13. Exit criteria
- Project compiles.
- Full automated test suite passes.
- Happy-path manual flow passes.
- Negative tests are executed; remaining failures are fixed or honestly documented.
- Actual results are filled in.
- README instructions work from a clean checkout.

## 14. Known limitations
- Numeric locations are placeholders, not geographic coordinates.
- Data disappears when the process exits.
- Fare rates are samples until confirmed against the assignment.
- The menu may not expose all strategies.
- Some empty-state messages and input validation may still need implementation.
