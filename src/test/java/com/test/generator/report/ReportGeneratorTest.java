package com.test.generator.report;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class demonstrating the usage of the test report generator.
 * This class also serves as an example of how the report will look.
 */
public class ReportGeneratorTest {
    
    @Test
    void demonstrateSuccessfulTest() {
        assertTrue(true, "This test demonstrates a successful test case");
    }
    
    @Test
    void demonstrateTestWithAssertions() {
        String expected = "Hello";
        String actual = "Hello";
        assertEquals(expected, actual, "This test demonstrates assertions");
    }
} 