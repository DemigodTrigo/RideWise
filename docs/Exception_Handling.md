# RideWise Exception Handling

**Author:** Vishal Verma  
**Purpose:** Describe validation, domain errors, and safe recovery behavior.

## Goals
- Show understandable error messages.
- Reject invalid operations before changing state.
- Use specific exception types for expected failures.
- Keep stack traces out of ordinary user-facing output.
- Make failure cases testable.

## Exception catalogue

| Exception | Typical cause |
|---|---|
| `InvalidInputException` | Blank text, invalid distance, or invalid required value |
| `RiderNotFoundException` | Rider ID is not registered |
| `DriverNotFoundException` | Driver ID is not registered |
| `RideNotFoundException` | Ride ID is not registered |
| `DuplicateRiderException` | Duplicate rider under the current policy |
| `DuplicateDriverException` | Duplicate driver under the current policy |
| `NoDriverAvailableException` | No eligible driver exists |
| `DriverUnavailableException` | Operation requires a driver who is available |
| `InvalidRideStateException` | Completion/cancellation violates the lifecycle |
| `RideWiseException` | Base class for expected domain errors |

Exact messages depend on the implementation; keep this list synchronized with the actual exception classes.

## Validation layers
**Console boundary (`Main`):**
- Menu choices must parse as integers.
- IDs must parse as UUIDs.
- Distance must parse as a number.
- Vehicle type must be supported.

**Service/model layer:**
- Required strings must not be blank.
- Distance must be finite and positive.
- Referenced IDs must exist.
- Ride transitions must be legal.
- Receipt must belong to the ride being completed.

Boundary validation improves usability; service/model validation protects the rules even when methods are called by tests or another entry point.

## Recommended handling flow
1. Parse input.
2. Validate it.
3. Invoke the service.
4. Change state only after preconditions pass.
5. Catch expected exceptions at the console boundary.
6. Show a concise message and return to the menu.
7. During development, log unexpected defects rather than disguising every exception as a user error.

Avoid relying on a broad `catch (Exception)` as the only handling strategy. Unexpected exceptions may indicate programming defects.

## State safety
When an operation fails, verify that:
- Ride status is unchanged.
- Driver availability and active ride count are unchanged.
- No invalid fare receipt was attached.
- No duplicate or partial record was created.

For example, if fare calculation fails, the ride should remain assigned and the driver should not be released.

## Suggested messages
- Invalid menu option: `Please choose an option from 0 to 9.`
- Invalid integer: `Please enter a whole number.`
- Invalid distance: `Enter a positive distance, for example 10.5.`
- Invalid vehicle: `Choose BIKE, AUTO, or CAR.`
- Missing record: `No record was found for that ID.`
- No driver: `No drivers are currently available. Please try again later.`
- No receipt: `This ride has no receipt yet. Complete the ride first.`

## Exception tests
Use JUnit `assertThrows` for service/model validation and manual testing for console parsing. Also verify that rejected operations leave all related state unchanged.
