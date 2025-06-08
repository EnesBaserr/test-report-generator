package com.test.generator.report;

/*
  @author Enes Baser
 * This class generates an HTML report for test results.
 */

import java.io.*;
import java.util.*;

public class ReportWriter {

    private static String formatStatus(String status) {
        return switch(status) {
            case "SUCCESSFUL" -> "Passed";
            case "FAILED" -> "Failed";
            case "ERROR" -> "Error";
            default -> status;
        };
    }

    private static String getStatusClass(String status) {
        return switch(status) {
            case "SUCCESSFUL" -> "successful";
            case "FAILED" -> "failed";
            case "ERROR" -> "error";
            default -> status.toLowerCase();
        };
    }

    private static String getStatusIcon(String status) {
        return switch(status) {
            case "SUCCESSFUL" -> "<img src='static/icons/check-circle.svg' class='icon' alt='success'>";
            case "FAILED" -> "<img src='static/icons/x-circle.svg' class='icon' alt='failed'>";
            case "ERROR" -> "<img src='static/icons/alert-triangle.svg' class='icon' alt='error'>";
            default -> "";
        };
    }

    private static String formatDuration(long millis) {
        if (millis < 1000) {
            return millis + "ms";
        }
        long seconds = millis / 1000;
        long remainingMillis = millis % 1000;
        if (seconds < 60) {
            return String.format("%d.%03ds", seconds, remainingMillis);
        }
        long minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%dm %ds", minutes, seconds);
    }

    private static String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&#39;");
    }

    public static void writeHtml(List<TestResult> results) {
        System.out.println("Generating HTML report...");
        try {
            // Create directories for static resources
            createResourceDirectories();
            copyStaticResources();
            
            Map<String, List<TestResult>> grouped = new LinkedHashMap<>();
            for (TestResult r : results) {
                grouped.computeIfAbsent(r.className, k -> new ArrayList<>()).add(r);
            }
    
            StringBuilder sb = new StringBuilder();
            // Collect summary stats
            int total = results.size();
            long passed = results.stream().filter(r -> "SUCCESSFUL".equals(r.status)).count();
            long failed = results.stream().filter(r -> "FAILED".equals(r.status)).count();
            long error = results.stream().filter(r -> "ERROR".equals(r.status)).count();
            long totalDuration = results.stream().mapToLong(r -> r.durationMillis).sum();
    
            sb.append("""
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Test Report</title>
                    <link rel="stylesheet" href="static/css/report.css">
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>Test Execution Report</h1>
                            <div class="total-duration">
                                <img src='static/icons/clock.svg' class='icon' alt='duration'>
                                Total Duration: %s
                            </div>
                        </div>
            """.formatted(formatDuration(totalDuration)));

            // Summary cards with icons
            sb.append("<div class='summary-bar'>")
              .append(createSummaryCard("Total Tests", total, ""))
              .append(createSummaryCard("Passed", passed, "success"))
              .append(createSummaryCard("Failed", failed, "fail"))
              .append(createSummaryCard("Errors", error, "error"))
              .append("</div>");

            // Test class cards
            for (Map.Entry<String, List<TestResult>> entry : grouped.entrySet()) {
                sb.append(createTestClassCard(entry.getKey(), entry.getValue()));
            }

            sb.append("""
                    </div>
                    <script src="static/js/report.js"></script>
                </body>
                </html>
            """);

            // Write the report
            writeReportFile(sb.toString());
            
        } catch (Exception e) {
            System.err.println("Error generating report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String createSummaryCard(String label, long value, String type) {
        String iconPath = switch(type) {
            case "success" -> "static/icons/check-circle.svg";
            case "fail" -> "static/icons/x-circle.svg";
            case "error" -> "static/icons/alert-triangle.svg";
            default -> "";
        };
        
        return String.format("""
            <div class="summary-card %s">
                %s
                <div>
                    <div class="label">%s</div>
                    <div class="value">%d</div>
                </div>
            </div>
        """, type, iconPath.isEmpty() ? "" : String.format("<img src='%s' class='icon' alt='%s'>", iconPath, label), escapeHtml(label), value);
    }

    private static String createTestClassCard(String className, List<TestResult> results) {
        StringBuilder sb = new StringBuilder();
        
        // Calculate class-level stats
        long passed = results.stream().filter(r -> "SUCCESSFUL".equals(r.status)).count();
        long failed = results.stream().filter(r -> "FAILED".equals(r.status)).count();
        long error = results.stream().filter(r -> "ERROR".equals(r.status)).count();
        long classDuration = results.stream().mapToLong(r -> r.durationMillis).sum();
        
        // Calculate average resource usage for the class
        double avgMemoryUsage = results.stream()
            .mapToLong(r -> r.resources.memoryUsageMB)
            .average()
            .orElse(0.0);
        double avgCpuUsage = results.stream()
            .mapToDouble(r -> r.resources.cpuUsagePercent)
            .average()
            .orElse(0.0);
        
        sb.append(String.format("""
            <div class="test-class">
                <div class="class-header">
                    <div class="class-info">
                        <span>%s</span>
                        <div class="class-metrics">
                            <span class="class-duration">
                                <img src='static/icons/clock.svg' class='icon' alt='duration'>
                                %s
                            </span>
                            <span class="class-memory">
                                <img src='static/icons/database.svg' class='icon' alt='memory'>
                                %.1f MB
                            </span>
                            <span class="class-cpu">
                                <img src='static/icons/cpu.svg' class='icon' alt='cpu'>
                                %.1f%%
                            </span>
                        </div>
                    </div>
                    <div class="status-summary">
                        <span class="status-count"><img src='static/icons/check-circle.svg' class='icon' alt='success'> %d</span>
                        <span class="status-count"><img src='static/icons/x-circle.svg' class='icon' alt='failed'> %d</span>
                        <span class="status-count"><img src='static/icons/alert-triangle.svg' class='icon' alt='error'> %d</span>
                    </div>
                </div>
                <table class="test-table">
                    <tr>
                        <th>Method</th>
                        <th>Status</th>
                        <th>Duration</th>
                        <th>Resources</th>
                        <th>Details</th>
                    </tr>
        """, escapeHtml(className), formatDuration(classDuration), avgMemoryUsage, avgCpuUsage, passed, failed, error));

        for (TestResult r : results) {
            boolean isFailed = "FAILED".equals(r.status) || "ERROR".equals(r.status);
            String statusClass = getStatusClass(r.status);
            String statusIcon = getStatusIcon(r.status);
            
            sb.append(String.format("""
                <tr%s>
                    <td>%s</td>
                    <td><span class="status-badge %s">%s %s</span></td>
                    <td class="duration"><img src='static/icons/clock.svg' class='icon' alt='duration'> %s</td>
                    <td class="resources">
                        <div class="resource-metrics">
                            <div class="resource-metric">
                                <img src='static/icons/database.svg' class='icon' alt='memory'>
                                %.1f MB
                            </div>
                            <div class="resource-metric">
                                <img src='static/icons/cpu.svg' class='icon' alt='cpu'>
                                %.1f%%
                            </div>
                        </div>
                    </td>
                    <td>
                """, 
                isFailed ? " class='failed'" : "",
                escapeHtml(r.name), 
                statusClass,
                statusIcon,
                escapeHtml(formatStatus(r.status)),
                formatDuration(r.durationMillis),
                (double)r.resources.memoryUsageMB,
                r.resources.cpuUsagePercent
            ));

            if (r.message != null && !r.message.trim().isEmpty()) {
                sb.append("""
                    <button class="details-button">
                        View Details
                        <img src="static/icons/chevron-down.svg" class="chevron-icon" alt="chevron">
                    </button>
                    <div class="message-content">
                """)
                .append(escapeHtml(r.message))
                .append("</div>");
            } else {
                sb.append("-");
            }

            sb.append("</td></tr>");
        }

        sb.append("</table></div>");
        return sb.toString();
    }

    private static void createResourceDirectories() {
        List<String> dirs = Arrays.asList(
            "target/static/css",
            "target/static/js",
            "target/static/icons"
        );
        
        for (String dir : dirs) {
            new File(dir).mkdirs();
        }
    }

    private static void copyStaticResources() {
        // Copy CSS, JS, and icons to target directory
        copyResource("static/css/report.css", "target/static/css/report.css");
        copyResource("static/js/report.js", "target/static/js/report.js");
        copyResource("static/icons/check-circle.svg", "target/static/icons/check-circle.svg");
        copyResource("static/icons/x-circle.svg", "target/static/icons/x-circle.svg");
        copyResource("static/icons/alert-triangle.svg", "target/static/icons/alert-triangle.svg");
        copyResource("static/icons/chevron-down.svg", "target/static/icons/chevron-down.svg");
        copyResource("static/icons/clock.svg", "target/static/icons/clock.svg");
        copyResource("static/icons/database.svg", "target/static/icons/database.svg");
        copyResource("static/icons/cpu.svg", "target/static/icons/cpu.svg");
    }

    private static void copyResource(String source, String target) {
        try (InputStream is = ReportWriter.class.getClassLoader().getResourceAsStream(source);
             OutputStream os = new FileOutputStream(target)) {
            if (is == null) {
                throw new IOException("Resource not found: " + source);
            }
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } catch (IOException e) {
            System.err.println("Failed to copy resource " + source + ": " + e.getMessage());
        }
    }

    private static void writeReportFile(String content) {
        try (FileWriter writer = new FileWriter("target/test-report.html")) {
            writer.write(content);
            System.out.println("Test report generated at: target/test-report.html");
        } catch (IOException e) {
            System.err.println("Failed to write test report: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

