package com.test.generator.report;

/*
  @author Enes Baser
 * This class generates an HTML report for test results.
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ReportWriter {

    public static void writeHtml(List<TestResult> results) {
        System.out.println("Generating HTML report...");
        try {
            // Ensure target directory exists
            File targetDir = new File("target");
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }
            
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
    
            sb.append("""
            <html><head><title>Test Report</title>
            <meta name='viewport' content='width=device-width, initial-scale=1'>
            <style>
              :root {
                --primary: #2563eb;
                --background: #f8fafc;
                --surface: #fff;
                --border: #e5e7eb;
                --success: green;
                --fail: #ef4444;
                --error: #f59e42;
                --text: #222;
                --muted: #6b7280;
                --radius: 8px;
                --shadow: 0 1px 3px #0001;
              }
              body {
                font-family: 'Segoe UI', Arial, sans-serif;
                margin: 0; padding: 0;
                background: var(--background);
                color: var(--text);
              }
              .container {
                max-width: 1000px;
                margin: 40px auto;
                padding: 0 20px;
              }
              h1 { 
                color: var(--primary); 
                margin-bottom: 1.5em;
                font-size: 2em;
                font-weight: 600;
              }
              .summary-bar {
                display: flex;
                align-items: center;
                gap: 2em;
                margin-bottom: 2em;
                font-size: 1em;
                background: var(--surface);
                padding: 1em;
                border-radius: var(--radius);
                box-shadow: var(--shadow);
              }
              .summary-bar span { 
                display: inline-flex;
                align-items: center;
                gap: 0.5em;
              }
              .summary-bar b { font-weight: 500; }
              .summary-bar .pass { color: var(--success); }
              .summary-bar .fail { color: var(--fail); }
              .summary-bar .error { color: var(--error); }
    
              .test-class {
                background: var(--surface);
                border-radius: var(--radius);
                box-shadow: var(--shadow);
                margin-bottom: 1.5em;
                overflow: hidden;
              }
              .class-header {
                background: var(--primary);
                color: white;
                padding: 0.8em 1.2em;
                font-weight: 500;
                font-size: 1em;
              }
              table { 
                width: 100%;
                border-collapse: collapse;
              }
              th, td { 
                padding: 0.8em 1.2em;
                text-align: left;
                border-bottom: 1px solid var(--border);
              }
              th { 
                background: var(--background);
                font-weight: 500;
                color: var(--text);
                font-size: 0.95em;
              }
              tr:last-child td {
                border-bottom: none;
              }
              .SUCCESSFUL { color: var(--success); font-weight: 500; }
              .FAILED { color: var(--fail); font-weight: 500; }
              .ERROR { color: var(--error); font-weight: 500; }
              
              .message-container { position: relative; }
              .message-toggle {
                cursor: pointer;
                user-select: none;
                display: inline-flex;
                align-items: center;
                gap: 0.5em;
                color: var(--primary);
                font-weight: 500;
              }
              .message-toggle::after {
                content: '▾';
                font-size: 1.2em;
                transition: transform 0.2s;
              }
              .message-toggle.open::after {
                transform: rotate(180deg);
              }
              .message-content {
                display: none;
                padding: 1em;
                background: var(--background);
                border-radius: var(--radius);
                margin-top: 0.5em;
                white-space: pre-wrap;
                font-family: monospace;
                font-size: 0.9em;
                color: var(--muted);
              }
              .message-content.show {
                display: block;
                animation: slideDown 0.2s ease-out;
              }
              @keyframes slideDown {
                from { opacity: 0; transform: translateY(-10px); }
                to { opacity: 1; transform: translateY(0); }
              }
              @media (max-width: 700px) {
                .container { padding: 10px; margin: 20px auto; }
                .summary-bar { 
                  flex-direction: column;
                  align-items: flex-start;
                  gap: 0.5em;
                }
                th, td { padding: 0.8em 1em; }
              }
            </style>
            <script>
            function toggleMessage(el) {
              el.classList.toggle('open');
              el.nextElementSibling.classList.toggle('show');
            }
            </script>
            </head><body>
            <div class='container'>
            <h1>Test Execution Report</h1>
            """);
            
            // Summary bar with colon format
            sb.append("<div class='summary-bar'>")
              .append("<span>Total: <b>").append(total).append("</b></span>")
              .append("<span class='pass'>Passed: <b>").append(passed).append("</b></span>")
              .append("<span class='fail'>Failed: <b>").append(failed).append("</b></span>")
              .append("<span class='error'>Error: <b>").append(error).append("</b></span>")
              .append("</div>\n");
    
            // Generate class-based cards
            for (Map.Entry<String, List<TestResult>> entry : grouped.entrySet()) {
                String className = entry.getKey();
                List<TestResult> classResults = entry.getValue();
                
                sb.append("<div class='test-class'>")
                  .append("<div class='class-header'>")
                  .append(className)
                  .append("</div>")
                  .append("<table><tr><th>Method</th><th>Status</th><th>Details</th></tr>");
                
                for (TestResult r : classResults) {
                    sb.append("<tr>")
                      .append("<td>").append(r.name).append("</td>")
                      .append("<td class='").append(r.status).append("'>").append(r.status).append("</td>");
                    
                    // Add collapsible message if there is one
                    if (r.message != null && !r.message.trim().isEmpty()) {
                        sb.append("<td><div class='message-container'>")
                          .append("<div class='message-toggle' onclick='toggleMessage(this)'>View Details</div>")
                          .append("<div class='message-content'>").append(r.message).append("</div>")
                          .append("</div></td>");
                    } else {
                        sb.append("<td>-</td>");
                    }
                    
                    sb.append("</tr>");
                }
                sb.append("</table></div>");
            }
    
            sb.append("</div></body></html>");
    
            try (FileWriter writer = new FileWriter("target/test-report.html")) {
                writer.write(sb.toString());
                System.out.println("Test report generated at: target/test-report.html");
            } catch (IOException e) {
                System.err.println("Failed to write test report: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.err.println("Error generating report: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

