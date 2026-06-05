# wabern

A Flink streaming job for matching events from two Kafka topics based on ID correlation.

## Overview

Wabern is a Java-based Apache Flink application that demonstrates stream processing patterns for event correlation. It reads events from two separate Kafka topics and uses a `KeyedCoprocessor` to match events by ID within a configurable time window.

## Features

- **Dual-stream processing**: Consumes events from two independent Kafka topics
- **Event correlation**: Matches events from both streams based on shared ID
- **Time-windowed matching**: Configurable matching timeout window (default: 5 seconds)
- **Stateful processing**: Maintains pending events across stream operations
- **JSON serialization**: Uses Jackson for data model serialization

## Project Structure

```
src/main/java/com/mintuk/streaming/
├── StreamMatchingJob.java              # Main entry point for the Flink job
├── model/
│   ├── ExpectedEvent.java             # Data model for Stream 1 events
│   ├── PaymentEvent.java              # Data model for Stream 2 events
│   └── MatchedEvents.java             # Data model for correlated event pairs
└── service/
    └── StreamMatchingOperator.java    # Stateful event matching logic
```

## Building

This project uses **Maven 3.9.16+** for builds.

### Prerequisites

- Java 11 or higher
- Maven 3.9+

### Build Commands

```bash
# Clean and install
mvn clean install

# Clean build artifacts
mvn clean

# Run tests
mvn test
```

### Build Output

The build produces a JAR file: `target/wabern-1.0-SNAPSHOT.jar`

Main class: `com.mintuk.streaming.StreamMatchingJob`

## Data Models

### ExpectedEvent
Event from Stream 1 containing:
- `id` (String): Event identifier
- `timestamp` (long): Event creation time in milliseconds
- `data` (String): Event payload

### PaymentEvent
Event from Stream 2 containing:
- `id` (String): Event identifier
- `timestamp` (long): Event creation time in milliseconds
- `value` (double): Numeric value (e.g., payment amount)

### MatchedEvents
Result of successful event correlation containing:
- `id` (String): Shared identifier of matched events
- `event1` (ExpectedEvent): The event from Stream 1
- `event2` (PaymentEvent): The event from Stream 2
- `matchedAt` (long): Timestamp when events were matched

## Configuration

The application accepts system properties for configuration:

```bash
java -D<property>=<value> -jar wabern-1.0-SNAPSHOT.jar
```

Available properties:
- `kafka.brokers` (default: `localhost:9092`) - Kafka broker addresses
- `kafka.topic1` (default: `events-stream-1`) - Source topic for Stream 1
- `kafka.topic2` (default: `events-stream-2`) - Source topic for Stream 2
- `kafka.output.topic` (default: `matched-events`) - Output topic for matched events
- `matching.timeout.ms` (default: `5000`) - Time window for matching in milliseconds

## Dependencies

- **Flink**: Apache Flink for stream processing (commented out - uncomment when SSL/TLS issues are resolved)
- **Kafka**: Apache Kafka clients (commented out - uncomment when SSL/TLS issues are resolved)
- **Jackson**: JSON serialization/deserialization
- **SLF4J/Log4j**: Logging framework

## Known Issues

- Flink and Kafka dependencies are currently commented out in `pom.xml` due to SSL/TLS certificate issues
- Full Flink job implementation is commented in `StreamMatchingJob.java` and can be uncommented once dependencies are available

## Development Notes

The `StreamMatchingOperator` demonstrates:
- Stateful stream processing with event buffering
- Time-based matching windows
- Logging and debugging information for stream state

## Example Usage

The `demonstrateStreamMatchingOperator()` method shows a simple example of the matching logic without requiring Flink or Kafka infrastructure.
