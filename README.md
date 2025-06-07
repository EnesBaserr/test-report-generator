# JUnit 5 Test Report Generator

A JUnit 5 test execution listener that automatically generates beautiful HTML reports for your test results.

## Features

- Automatic test execution listener registration via SPI
- Beautiful HTML report generation
- Support for all test results (Success, Failure, Error)
- Detailed error messages and stack traces
- Clean and modern UI
- No configuration needed

## Installation

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.test.generator</groupId>
    <artifactId>test-report-generator</artifactId>
    <version>1.0.0</version>
    <scope>test</scope>
</dependency>
```

## Usage

The test report generator works automatically through Java's Service Provider Interface (SPI) mechanism. Once you add the dependency to your project, it will:

1. Automatically register itself as a test listener
2. Generate an HTML report at `target/test-report.html` after test execution
3. Show test execution progress in the console

No additional configuration is needed!

## Requirements

- Java 15 or higher
- JUnit 5.10.0 or higher
- Maven 3.6.0 or higher

## Report Location

After running your tests with `mvn test`, you can find the HTML report at:

```
target/test-report.html
```

## Example Report

The HTML report includes:

- Overall test execution summary
- Test results grouped by class
- Detailed error messages and stack traces
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

## License

This project is open source and available under the MIT License.
