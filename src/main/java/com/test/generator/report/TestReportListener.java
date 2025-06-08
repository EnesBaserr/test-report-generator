package com.test.generator.report;

import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.engine.TestExecutionResult;
import com.test.generator.report.Priority;
import com.test.generator.report.PriorityLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.ThreadMXBean;
import com.sun.management.OperatingSystemMXBean;

public class TestReportListener implements TestExecutionListener {

    private final List<TestResult> results = new ArrayList<>();
    private final Map<String, Long> testStartTimes = new HashMap<>();
    private final Map<String, Long> testStartCpuTimes = new HashMap<>();
    private final MemoryMXBean memoryBean;
    private final ThreadMXBean threadBean;
    private final OperatingSystemMXBean osBean;
    private boolean reportGenerated = false;

    public TestReportListener() {
        System.out.println("TestReportListener instantiated!");
        this.memoryBean = ManagementFactory.getMemoryMXBean();
        this.threadBean = ManagementFactory.getThreadMXBean();
        this.osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    }

    @Override
    public void executionStarted(TestIdentifier testIdentifier) {
        if (testIdentifier.isTest()) {
            String uniqueId = testIdentifier.getUniqueId();
            testStartTimes.put(uniqueId, System.currentTimeMillis());
            testStartCpuTimes.put(uniqueId, threadBean.getCurrentThreadCpuTime());
        }
    }

    private TestResult.ResourceMetrics captureResourceMetrics(String uniqueId) {
        // Calculate CPU usage
        long cpuTime = threadBean.getCurrentThreadCpuTime() - testStartCpuTimes.getOrDefault(uniqueId, 0L);
        long elapsedTime = System.currentTimeMillis() - testStartTimes.getOrDefault(uniqueId, System.currentTimeMillis());
        
        // Ensure we don't divide by zero and handle very short tests
        if (elapsedTime == 0) {
            elapsedTime = 1; // Minimum 1ms to avoid division by zero
        }
        
        // Convert nanoseconds to milliseconds for CPU time (divide by 1_000_000)
        // Then calculate percentage of CPU time relative to elapsed time
        double cpuUsage = ((double) cpuTime / 1_000_000) / elapsedTime * 100.0;
        
        // Cap CPU usage at 100%
        cpuUsage = Math.min(cpuUsage, 100.0);
        
        // Get memory usage
        long usedMemory = memoryBean.getHeapMemoryUsage().getUsed() / (1024 * 1024); // Convert to MB
        
        return new TestResult.ResourceMetrics(usedMemory, cpuUsage);
    }

    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult result) {
        if (testIdentifier.isTest()) {
            // Get the method name without parameters
            String methodName = testIdentifier.getDisplayName();
            if (methodName.contains("(")) {
                methodName = methodName.substring(0, methodName.indexOf('('));
            }
            
            // Get the class name from the unique ID segment
            String uniqueId = testIdentifier.getUniqueId();
            String className = "Unknown";
            
            // Parse class name from unique ID which has format like [engine:junit-jupiter]/[class:com.example.TestClass]/[method:testMethod()]
            String[] segments = uniqueId.split("/");
            for (String segment : segments) {
                if (segment.startsWith("[class:")) {
                    className = segment.substring(7, segment.length() - 1); // Remove [class: and ]
                    // Get simple class name
                    if (className.contains(".")) {
                        className = className.substring(className.lastIndexOf('.') + 1);
                    }
                    break;
                }
            }

            // Calculate test duration
            long startTime = testStartTimes.getOrDefault(uniqueId, System.currentTimeMillis());
            long duration = System.currentTimeMillis() - startTime;

            // Get the full error message including the assertion message
            String message = result.getThrowable()
                    .map(throwable -> {
                        String msg = throwable.getMessage();
                        if (throwable instanceof AssertionError) {
                            // For assertion errors, include the full message
                            return msg != null ? msg : throwable.toString();
                        }
                        // For other errors, include the full stack trace
                        return throwable.toString();
                    })
                    .orElse("");

            // Get the priority annotation if present
            PriorityLevel priority = null;
            try {
                // Get the full class name from the unique ID
                String fullClassName = null;
                for (String segment : segments) {
                    if (segment.startsWith("[class:")) {
                        fullClassName = segment.substring(7, segment.length() - 1);
                        break;
                    }
                }
                
                System.out.println("Full class name: " + fullClassName);
                System.out.println("Method name: " + methodName);
                
                if (fullClassName != null) {
                    Class<?> testClass = Class.forName(fullClassName);
                    // Print all available methods for debugging
                    System.out.println("Available methods in class:");
                    for (java.lang.reflect.Method method : testClass.getDeclaredMethods()) {
                        System.out.println(" - " + method.getName());
                    }
                    
                    java.lang.reflect.Method method = testClass.getDeclaredMethod(methodName);
                    Priority priorityAnnotation = method.getAnnotation(Priority.class);
                    if (priorityAnnotation != null) {
                        priority = priorityAnnotation.value();
                    }
                }
            } catch (Exception e) {
                System.err.println("Failed to get priority annotation: " + e.getMessage());
                e.printStackTrace();
            }

            // Create test result with resource metrics
            TestResult testResult = new TestResult(
                    className,
                    methodName,
                    result.getStatus().name(),
                    message,
                    duration
            );
            
            // Add resource metrics and priority
            TestResult.ResourceMetrics metrics = captureResourceMetrics(uniqueId);
            testResult.withResourceMetrics(metrics.memoryUsageMB, metrics.cpuUsagePercent)
                     .withPriority(priority);
            
            results.add(testResult);
            
            // Clean up the start times
            testStartTimes.remove(uniqueId);
            testStartCpuTimes.remove(uniqueId);
        }
    }

    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        generateReport();
    }

    private void generateReport() {
        if (!reportGenerated) {
            System.out.println("Test plan execution finished. Generating report...");
            ReportWriter.writeHtml(results);
            reportGenerated = true;
        }
    }

    // Ensure report is generated even if JVM shuts down
    @Override
    public void testPlanExecutionStarted(TestPlan testPlan) {
        Runtime.getRuntime().addShutdownHook(new Thread(this::generateReport));
    }
}