// Name: Shahed Samir Mohammed
// ID: 220231639
// Programming III - Task Management System

package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Task {
    private int id;
    private String title;
    private String status; // "open" or "closed"
    private String addedBy;
    private LocalDate creationDate;
    
    private static int nextId = 1;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public Task(String title, String status, String addedBy, LocalDate creationDate) {
        this.id = nextId++;
        this.title = title;
        this.status = status;
        this.addedBy = addedBy;
        this.creationDate = creationDate;
    }
    
    // For loading from CSV with specific ID
    public Task(int id, String title, String status, String addedBy, LocalDate creationDate) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.addedBy = addedBy;
        this.creationDate = creationDate;
        if (id >= nextId) {
            nextId = id + 1;
        }
    }
    
    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getStatus() { return status; }
    public String getAddedBy() { return addedBy; }
    public LocalDate getCreationDate() { return creationDate; }
    
    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setStatus(String status) { this.status = status; }
    public void setAddedBy(String addedBy) { this.addedBy = addedBy; }
    public void setCreationDate(LocalDate creationDate) { this.creationDate = creationDate; }
    
    // Convert to CSV string
    public String toCSV() {
        return id + "," + title + "," + status + "," + addedBy + "," + creationDate.format(DATE_FORMATTER);
    }
    
    // Display format for ListView
    @Override
    public String toString() {
        return "ID: " + id + " | " + title + " | [" + status + "] | By: " + addedBy + " | Date: " + creationDate.format(DATE_FORMATTER);
    }
    
    public static void resetIdCounter() {
        nextId = 1;
    }
}