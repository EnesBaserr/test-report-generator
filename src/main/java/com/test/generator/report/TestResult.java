package com.test.generator.report;

/*
 @author Enes Baser
 This class represents the result of a test case.
 */
public class TestResult {
    public String className;
    public String name;
    public String status;
    public String message;

    public TestResult(String className, String name, String status, String message) {
        this.className = className;
        this.name = name;
        this.status = status;
        this.message = message;
    }
}

