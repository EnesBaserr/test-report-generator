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

### Maven

Add this to your `pom.xml`:

```xml
<dependency>
    <groupId>com.test.generator</groupId>
    <artifactId>test-report-generator</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

Add this to your `build.gradle`:

```groovy
dependencies {
    implementation 'com.test.generator:test-report-generator:1.0.0'
}
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
>
> In such cases, you don't need to declare them explicitly unless you need a specific version.

For Gradle users:

```groovy
dependencies {
    // You can use 5.9.3 or any newer version
    def junitVersion = '5.9.3'
    testImplementation "org.junit.jupiter:junit-jupiter-api:${junitVersion}"
    testRuntimeOnly "org.junit.jupiter:junit-jupiter-engine:${junitVersion}"
    implementation "org.junit.platform:junit-platform-launcher:1.9.3"
}
```

Note: The test report generator is compatible with:

- JUnit Jupiter 5.9.3 or higher
- JUnit Platform 1.9.3 or higher
- Java 15 or higher

We recommend using the latest stable versions of JUnit for best results.

### GitHub Packages Configuration

To use this package from GitHub Packages, you need to configure both your `pom.xml` and `settings.xml`:

1. Add this to your project's `pom.xml`:

```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/EnesBaserr/test-report-generator</url>
    </repository>
</repositories>
```

2. Add this to your Maven `settings.xml` (usually in `~/.m2/settings.xml`):

```xml
<settings>
  <servers>
    <server>
      <id>github</id>
      <username>${github.username}</username>
      <password>${github.token}</password>
    </server>
  </servers>
</settings>
```

For Gradle, add to your `build.gradle`:

```groovy
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/EnesBaserr/test-report-generator")
        credentials {
            username = project.findProperty("gpr.user") ?: System.getenv("USERNAME")
            password = project.findProperty("gpr.key") ?: System.getenv("TOKEN")
        }
    }
}
```

## Usage

The test report generator works automatically through Java's Service Provider Interface (SPI) mechanism. Once you add the dependency to your project, it will:

1. Automatically register itself as a test listener
2. Generate an HTML report at `target/test-report.html` after test execution
3. Show test execution progress in the console

No additional configuration is needed!

## Requirements

- Java 15 or higher
- JUnit 5.9.3 or higher
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
