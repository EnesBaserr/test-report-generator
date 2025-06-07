package com.test.generator.report;


import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.engine.TestExecutionResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TestReportListener implements TestExecutionListener {

    private final List<TestResult> results = new ArrayList<>();
    private boolean reportGenerated = false;

    public TestReportListener() {
        System.out.println("TestReportListener instantiated!");
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

            results.add(new TestResult(
                    className,
                    methodName,
                    result.getStatus().name(),
                    message
            ));
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