package com.test.generator.report;

/*
 @author Enes Baser
 This class represents the result of a test case with enhanced metadata.
 */
public class TestResult {
    public String className;
    public String name;
    public String status;
    public String message;
    public long durationMillis; // Duration in milliseconds
    
    // New fields for enhanced reporting
    public String description;        // Test description from annotation
    public String[] categories;       // Test categories/tags
    public String priority;           // Test priority level
    public String[] dependencies;     // Test dependencies
    public Object[] parameters;       // Test parameters
    public String setupInfo;          // Setup information
    public String teardownInfo;       // Teardown information
    public int retryCount;           // Number of retry attempts
    public ResourceMetrics resources; // Resource usage metrics
    
    // Inner class for resource metrics
    public static class ResourceMetrics {
        public long memoryUsageMB;
        public double cpuUsagePercent;
        
        public ResourceMetrics(long memoryUsageMB, double cpuUsagePercent) {
            this.memoryUsageMB = memoryUsageMB;
            this.cpuUsagePercent = cpuUsagePercent;
        }
    }

    public TestResult(String className, String name, String status, String message, long durationMillis) {
        this.className = className;
        this.name = name;
        this.status = status;
        this.message = message;
        this.durationMillis = durationMillis;
        this.retryCount = 0;
        this.resources = new ResourceMetrics(0, 0);
    }
    
    // Builder pattern for optional fields
    public TestResult withDescription(String description) {
        this.description = description;
        return this;
    }
    
    public TestResult withCategories(String... categories) {
        this.categories = categories;
        return this;
    }
    
    public TestResult withPriority(String priority) {
        this.priority = priority;
        return this;
    }
    
    public TestResult withDependencies(String... dependencies) {
        this.dependencies = dependencies;
        return this;
    }
    
    public TestResult withParameters(Object... parameters) {
        this.parameters = parameters;
        return this;
    }
    
    public TestResult withSetupInfo(String setupInfo) {
        this.setupInfo = setupInfo;
        return this;
    }
    
    public TestResult withTeardownInfo(String teardownInfo) {
        this.teardownInfo = teardownInfo;
        return this;
    }
    
    public TestResult withRetryCount(int retryCount) {
        this.retryCount = retryCount;
        return this;
    }
    
    public TestResult withResourceMetrics(long memoryUsageMB, double cpuUsagePercent) {
        this.resources = new ResourceMetrics(memoryUsageMB, cpuUsagePercent);
        return this;
    }
}

