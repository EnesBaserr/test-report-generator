package com.test.generator.report;

/**
 * Enum representing priority levels for test methods.
 */
public enum PriorityLevel {
    HIGH("High", "#991b1b", "#fecaca"),
    MEDIUM("Medium", "#92400e", "#fef3c7"),
    LOW("Low", "#374151", "#e5e7eb");

    private final String displayName;
    private final String textColor;
    private final String backgroundColor;

    PriorityLevel(String displayName, String textColor, String backgroundColor) {
        this.displayName = displayName;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getTextColor() {
        return textColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }
} 