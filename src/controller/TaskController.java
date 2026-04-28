// Name: Shahed Samir Mohammed
// ID: 220231639
// Programming III - Task Management System

package controller;

import model.Task;
import utils.CSVHandler;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class TaskController {
    
    private List<Task> tasks;
    private DateTimeFormatter dateFormatter;
    
    public TaskController() {
        this.tasks = CSVHandler.loadTasks();
        this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        // Update next ID counter based on loaded tasks
        if (!tasks.isEmpty()) {
            int maxId = tasks.stream().mapToInt(Task::getId).max().getAsInt();
            // Reset counter logic handled in Task class
        }
    }
    
    // Get all tasks
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }
    
    // Add new task
    public boolean addTask(String title, String status, String addedBy, String dateStr) {
        // Validation
        if (title == null || title.trim().isEmpty()) {
            return false;
        }
        
        if (!status.equalsIgnoreCase("open") && !status.equalsIgnoreCase("closed")) {
            return false;
        }
        
        if (addedBy == null || addedBy.trim().isEmpty()) {
            return false;
        }
        
        LocalDate date;
        try {
            date = LocalDate.parse(dateStr, dateFormatter);
        } catch (DateTimeParseException e) {
            return false;
        }
        
        Task newTask = new Task(title.trim(), status.toLowerCase(), addedBy.trim(), date);
        tasks.add(newTask);
        CSVHandler.saveTasks(tasks);
        return true;
    }
    
    // Get total number of tasks
    public int getTotalTaskCount() {
        return tasks.size();
    }
    
    // 6. View all task titles added by a specific user, sorted by task ID
    public List<Task> getTasksByUser(String userName) {
        return tasks.stream()
                .filter(t -> t.getAddedBy().equalsIgnoreCase(userName))
                .sorted(Comparator.comparingInt(Task::getId))
                .collect(Collectors.toList());
    }
    
    // 7. Display earliest four tasks based on creation date
    public List<Task> getEarliestFourTasks() {
        return tasks.stream()
                .sorted(Comparator.comparing(Task::getCreationDate))
                .limit(4)
                .collect(Collectors.toList());
    }
    
    // 8. Find tasks that start with letter 'a' and have exactly seven letters (case-insensitive)
    public List<Task> getTasksStartingWithAExactSeven() {
        return tasks.stream()
                .filter(t -> {
                    String title = t.getTitle().toLowerCase();
                    return title.startsWith("a") && title.length() == 7;
                })
                .collect(Collectors.toList());
    }
    
    // 9. Determine user who added the highest number of tasks
    public String getMostActiveUser() {
        if (tasks.isEmpty()) return "No tasks";
        
        Map<String, Long> userCount = tasks.stream()
                .collect(Collectors.groupingBy(Task::getAddedBy, Collectors.counting()));
        
        return userCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> entry.getKey() + " (" + entry.getValue() + " tasks)")
                .orElse("No tasks");
    }
    
    // 10. Count open and closed tasks
    public int getOpenTaskCount() {
        return (int) tasks.stream().filter(t -> t.getStatus().equalsIgnoreCase("open")).count();
    }
    
    public int getClosedTaskCount() {
        return (int) tasks.stream().filter(t -> t.getStatus().equalsIgnoreCase("closed")).count();
    }
    
    // 11. Count tasks added by a specific user
    public int getTaskCountByUser(String userName) {
        return (int) tasks.stream().filter(t -> t.getAddedBy().equalsIgnoreCase(userName)).count();
    }
}