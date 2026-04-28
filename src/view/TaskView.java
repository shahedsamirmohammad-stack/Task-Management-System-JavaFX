// Name: Shahed Samir Mohammed
// ID: 220231639
// Programming III - Task Management System

package view;

import controller.TaskController;
import model.Task;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.util.List;

public class TaskView extends Application {
    
    private TaskController controller;
    private ListView<Task> taskListView;
    private Label totalTasksLabel;
    private Label statsLabel;
    
    @Override
    public void start(Stage primaryStage) {
        controller = new TaskController();
        
        primaryStage.setTitle("Task Management System - Shahed Samir Mohammed (220231639)");
        
        // Main layout
        BorderPane mainLayout = new BorderPane();
        
        // Menu Bar
        MenuBar menuBar = createMenuBar(primaryStage);
        mainLayout.setTop(menuBar);
        
        // Center: ListView
        taskListView = new ListView<>();
        mainLayout.setCenter(taskListView);
        
        // Right panel: Statistics
        VBox statsPanel = createStatsPanel();
        mainLayout.setRight(statsPanel);
        
        // Bottom: Add Task Form
        HBox addTaskPanel = createAddTaskPanel();
        mainLayout.setBottom(addTaskPanel);
        
        // Top panel with total tasks label
        HBox topPanel = new HBox();
        topPanel.setPadding(new Insets(10));
        topPanel.setAlignment(Pos.CENTER_RIGHT);
        totalTasksLabel = new Label();
        totalTasksLabel.setStyle("-fx-font-weight: bold; -fx-padding: 5;");
        topPanel.getChildren().add(totalTasksLabel);
        mainLayout.setTop(topPanel);
        
        // Add menu bar ABOVE top panel
        VBox topContainer = new VBox();
        topContainer.getChildren().addAll(menuBar, topPanel);
        mainLayout.setTop(topContainer);
        
        Scene scene = new Scene(mainLayout, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Refresh after showing
        refreshTaskList();
        updateStats();
    }
    
    private MenuBar createMenuBar(Stage stage) {
        MenuBar menuBar = new MenuBar();
        
        // File Menu
        Menu fileMenu = new Menu("File");
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> stage.close());
        fileMenu.getItems().add(exitItem);
        
        // View Menu - Appearance
        Menu viewMenu = new Menu("View");
        Menu fontMenu = new Menu("Font");
        
        MenuItem fontDefault = new MenuItem("Default");
        fontDefault.setOnAction(e -> {
            taskListView.setStyle("-fx-font-size: 12px;");
            totalTasksLabel.setStyle("-fx-font-weight: bold; -fx-padding: 5; -fx-font-size: 12px;");
            statsLabel.setStyle("-fx-font-size: 12px;");
        });
        
        MenuItem fontLarge = new MenuItem("Large");
        fontLarge.setOnAction(e -> {
            taskListView.setStyle("-fx-font-size: 16px;");
            totalTasksLabel.setStyle("-fx-font-weight: bold; -fx-padding: 5; -fx-font-size: 16px;");
            statsLabel.setStyle("-fx-font-size: 16px;");
        });
        
        MenuItem fontBold = new MenuItem("Bold Style");
        fontBold.setOnAction(e -> {
            taskListView.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");
        });
        
        fontMenu.getItems().addAll(fontDefault, fontLarge, fontBold);
        viewMenu.getItems().add(fontMenu);
        
        // Help Menu
        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About Task Management System");
            alert.setHeaderText("Task Management System v1.0");
            alert.setContentText("Created by: Shahed Samir Mohammed (220231639)\n\n"
                    + "Features:\n"
                    + "- Add and manage tasks\n"
                    + "- Import/export CSV\n"
                    + "- Analyze task statistics\n"
                    + "- Filter tasks by user, date, and title");
            alert.showAndWait();
        });
        helpMenu.getItems().add(aboutItem);
        
        menuBar.getMenus().addAll(fileMenu, viewMenu, helpMenu);
        return menuBar;
    }
    
    private VBox createStatsPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));
        panel.setPrefWidth(250);
        panel.setStyle("-fx-border-color: lightgray; -fx-border-width: 1;");
        
        Label statsTitle = new Label("Task Statistics");
        statsTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        statsLabel = new Label();
        statsLabel.setWrapText(true);
        
        // Filter by user input
        Label filterLabel = new Label("Filter by user:");
        TextField userFilterField = new TextField();
        userFilterField.setPromptText("Enter username");
        Button filterBtn = new Button("Show User's Tasks");
        
        TextField specificUserField = new TextField();
        specificUserField.setPromptText("Enter username for count");
        Button countUserBtn = new Button("Count tasks by user");
        
        Label userTasksLabel = new Label();
        
        filterBtn.setOnAction(e -> {
            String userName = userFilterField.getText().trim();
            if (!userName.isEmpty()) {
                List<Task> userTasks = controller.getTasksByUser(userName);
                taskListView.getItems().setAll(userTasks);
                totalTasksLabel.setText("Tasks by " + userName + ": " + userTasks.size());
            } else {
                refreshTaskList();
            }
        });
        
        countUserBtn.setOnAction(e -> {
            String userName = specificUserField.getText().trim();
            if (!userName.isEmpty()) {
                int count = controller.getTaskCountByUser(userName);
                userTasksLabel.setText("Tasks by " + userName + ": " + count);
            }
        });
        
        // Additional filter buttons
        Button earliestBtn = new Button("Show Earliest 4 Tasks");
        earliestBtn.setOnAction(e -> {
            List<Task> earliest = controller.getEarliestFourTasks();
            taskListView.getItems().setAll(earliest);
            totalTasksLabel.setText("Earliest 4 Tasks: " + earliest.size());
        });
        
        Button aLetterBtn = new Button("Tasks starting with 'a' (7 letters)");
        aLetterBtn.setOnAction(e -> {
            List<Task> aTasks = controller.getTasksStartingWithAExactSeven();
            taskListView.getItems().setAll(aTasks);
            totalTasksLabel.setText("Tasks starting with 'a' (7 letters): " + aTasks.size());
        });
        
        Button resetBtn = new Button("Show All Tasks");
        resetBtn.setOnAction(e -> refreshTaskList());
        
        Button refreshStatsBtn = new Button("Refresh Stats");
        refreshStatsBtn.setOnAction(e -> updateStats());
        
        panel.getChildren().addAll(
            statsTitle, statsLabel,
            new Separator(),
            filterLabel, userFilterField, filterBtn,
            new Separator(),
            specificUserField, countUserBtn, userTasksLabel,
            new Separator(),
            earliestBtn, aLetterBtn, resetBtn, refreshStatsBtn
        );
        
        return panel;
    }
    
    private HBox createAddTaskPanel() {
        HBox panel = new HBox(10);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-border-color: lightgray; -fx-border-width: 1;");
        panel.setAlignment(Pos.CENTER);
        
        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        titleField.setPrefWidth(150);
        
        ComboBox<String> statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll("open", "closed");
        statusCombo.setPromptText("Status");
        
        TextField addedByField = new TextField();
        addedByField.setPromptText("Added By");
        addedByField.setPrefWidth(120);
        
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Creation Date");
        
        Button addBtn = new Button("Add Task");
        
        addBtn.setOnAction(e -> {
            String title = titleField.getText();
            String status = statusCombo.getValue();
            String addedBy = addedByField.getText();
            String dateStr = datePicker.getValue() != null ? datePicker.getValue().toString() : "";
            
            if (controller.addTask(title, status, addedBy, dateStr)) {
                refreshTaskList();
                updateStats();
                titleField.clear();
                statusCombo.setValue(null);
                addedByField.clear();
                datePicker.setValue(null);
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Task added successfully!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Invalid input. Check title (not empty), status (open/closed), and date (yyyy-MM-dd).");
                alert.showAndWait();
            }
        });
        
        panel.getChildren().addAll(
            new Label("Title:"), titleField,
            new Label("Status:"), statusCombo,
            new Label("Added By:"), addedByField,
            new Label("Date:"), datePicker,
            addBtn
        );
        
        return panel;
    }
    
    private void refreshTaskList() {
        if (taskListView != null && totalTasksLabel != null) {
            taskListView.getItems().setAll(controller.getAllTasks());
            totalTasksLabel.setText("Total Tasks: " + controller.getTotalTaskCount());
        }
    }
    
    private void updateStats() {
        if (statsLabel != null) {
            int open = controller.getOpenTaskCount();
            int closed = controller.getClosedTaskCount();
            String mostActive = controller.getMostActiveUser();
            
            statsLabel.setText("Open Tasks: " + open + "\n"
                    + "Closed Tasks: " + closed + "\n"
                    + "Most Active User: " + mostActive);
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}