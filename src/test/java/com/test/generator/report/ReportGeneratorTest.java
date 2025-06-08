package com.test.generator.report;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Test class demonstrating the usage of the test report generator.
 * This class includes various tests with different resource usage patterns.
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

    @Test
    void cpuIntensiveTest() {
        // Perform CPU-intensive calculation
        long result = 0;
        for (int i = 0; i < 1_000_000; i++) {
            result += fibonacci(20); // Calculate fibonacci numbers repeatedly
        }
        assertTrue(result > 0, "CPU intensive operation completed");
    }

    @Test
    void memoryIntensiveTest() {
        // Allocate and manipulate memory
        List<byte[]> memoryBlocks = new ArrayList<>();
        try {
            // Allocate multiple 1MB blocks
            for (int i = 0; i < 100; i++) {
                memoryBlocks.add(new byte[1024 * 1024]); // 1MB each
                // Do some work with the memory
                for (int j = 0; j < 1024; j++) {
                    memoryBlocks.get(i)[j] = (byte) j;
                }
            }
        } finally {
            // Clear references to help GC
            memoryBlocks.clear();
        }
        assertTrue(true, "Memory intensive operation completed");
    }

    @Test
    void mixedResourceTest() throws InterruptedException {
        // Mix of CPU, memory, and time usage
        List<Double> results = new ArrayList<>();
        Random random = new Random();
        
        // Simulate complex calculations with memory allocation
        for (int i = 0; i < 50; i++) {
            double[] array = new double[100000];
            for (int j = 0; j < array.length; j++) {
                array[j] = Math.sqrt(random.nextDouble() * 10000);
            }
            results.add(array[random.nextInt(array.length)]);
            TimeUnit.MILLISECONDS.sleep(10); // Add some delay
        }
        
        assertTrue(results.size() > 0, "Mixed resource test completed");
    }

    @Test
    void longRunningTest() throws InterruptedException {
        // Simulate a long-running test with moderate resource usage
        int iterations = 5;
        for (int i = 0; i < iterations; i++) {
            // Some CPU work
            double result = 0;
            for (int j = 0; j < 100000; j++) {
                result += Math.sin(j) * Math.cos(j);
            }
            // Some memory work
            byte[] block = new byte[512 * 1024]; // 512KB
            Random random = new Random();
            random.nextBytes(block);
            // Add delay
            TimeUnit.MILLISECONDS.sleep(200);
            assertTrue(result != 0, "Iteration " + i + " completed");
        }
    }

    // Helper method for CPU-intensive test
    private long fibonacci(int n) {
        if (n <= 1) return n;
        return fibonacci(n - 1) + fibonacci(n - 2);
    }
} 