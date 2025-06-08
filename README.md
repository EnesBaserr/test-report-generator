# Test Report Generator

A JUnit test report generator that creates beautiful HTML reports with enhanced test metadata, including test priorities, resource usage metrics, and execution details.

## Features

- 📊 Beautiful HTML test reports
- 🎯 Test priority levels with visual indicators
- 📈 Resource usage metrics (CPU, Memory)
- ⏱️ Detailed execution timing
- 🔍 Test categorization and organization

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

## Installation

1. Add the dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.test.generator</groupId>
    <artifactId>test-report-generator</artifactId>
    <version>1.0.0</version>
    <scope>test</scope>
</dependency>
```

2. The report generator will automatically run after your tests and generate a report in the `target` directory.

## Report Location

After running your tests, find the generated report at:

```
target/test-report.html
```

## Contributing

Feel free to submit issues and enhancement requests!
