# Test Report Generator

A JUnit test report generator that creates beautiful HTML reports with enhanced test metadata, including test priorities, resource usage metrics, and execution details.

## Features

- 📊 Beautiful HTML test reports
- 🎯 Test priority levels with visual indicators
- 📈 Resource usage metrics (CPU, Memory)
- ⏱️ Detailed execution timing
- 🔍 Test categorization and organization

## Installation

### Maven

Add this to your `pom.xml`:

```xml
<dependency>
    <groupId>com.test.generator</groupId>
    <artifactId>test-report-generator</artifactId>
    <version>1.0.0</version>
    <scope>test</scope>
</dependency>
```

### Required Dependencies

The project requires JUnit 5.9.3 or higher. Here are the minimum required dependencies:

```xml
<properties>
    <!-- You can use 5.9.3 or any newer version -->
    <junit.jupiter.version>5.9.3</junit.jupiter.version>
    <junit.platform.version>1.9.3</junit.platform.version>
</properties>

<dependencies>
    <!-- JUnit Jupiter API for writing tests -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>${junit.jupiter.version}</version>
        <scope>test</scope>
    </dependency>

    <!-- JUnit Jupiter Engine for running tests -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-engine</artifactId>
        <version>${junit.jupiter.version}</version>
        <scope>test</scope>
    </dependency>

    <!-- JUnit Platform Launcher -->
    <dependency>
        <groupId>org.junit.platform</groupId>
        <artifactId>junit-platform-launcher</artifactId>
        <version>${junit.platform.version}</version>
    </dependency>
</dependencies>
```

> **Note**: These dependencies are often automatically included if you're using:
>
> - `spring-boot-starter-test`
> - `mockito-junit-jupiter`
> - Other testing frameworks that depend on JUnit 5

### Gradle

Add this to your `build.gradle`:

```groovy
dependencies {
    implementation 'com.test.generator:test-report-generator:1.0.0'

    // Required JUnit dependencies
    def junitVersion = '5.9.3'
    testImplementation "org.junit.jupiter:junit-jupiter-api:${junitVersion}"
    testRuntimeOnly "org.junit.jupiter:junit-jupiter-engine:${junitVersion}"
    implementation "org.junit.platform:junit-platform-launcher:1.9.3"
}
```

## Usage

### Adding Priority to Tests

You can mark your test methods with the `@Priority` annotation to indicate their importance level. The priority will be displayed in the report with color-coded badges.

```java
import com.test.generator.report.Priority;
import com.test.generator.report.PriorityLevel;

public class YourTestClass {

    @Test
    @Priority(PriorityLevel.HIGH)
    void criticalFeatureTest() {
        // Test implementation
    }

    @Test
    @Priority(PriorityLevel.MEDIUM)
    void importantFeatureTest() {
        // Test implementation
    }

    @Test
    @Priority(PriorityLevel.LOW)
    void minorFeatureTest() {
        // Test implementation
    }
}
```

### Priority Levels

The following priority levels are available:

| Level    | Visual       | Use Case                                               |
| -------- | ------------ | ------------------------------------------------------ |
| `HIGH`   | Red badge    | Critical functionality, core features, security tests  |
| `MEDIUM` | Yellow badge | Important features, business logic tests               |
| `LOW`    | Gray badge   | Minor features, edge cases, nice-to-have functionality |

### Report Features

The generated report includes:

- Overall test execution summary
- Test class statistics
- Individual test results with:
  - Priority level indicators
  - Execution duration
  - Resource usage (CPU, Memory)
  - Test status (Passed/Failed/Error)

## Requirements

- Java 15 or higher
- JUnit 5.9.3 or higher
- Maven 3.6.0 or higher (if using Maven)

## Report Location

After running your tests with `mvn test`, find the generated report at:

```
target/test-report.html
```

## Example Report

The HTML report includes:

- Overall test execution summary
- Test results grouped by class
- Detailed error messages and stack traces
- Test execution duration for each test and overall suite
- Clean, modern UI with collapsible sections

## Building from Source

To build the project:

```bash
mvn clean install
```

This will:

- Compile the source code
- Run the tests
- Generate the JAR file
- Create source and javadoc JARs

## Contributing

Feel free to submit issues and enhancement requests!
