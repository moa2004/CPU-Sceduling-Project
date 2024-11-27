package gui;

import algorithms.FCFS;
import algorithms.SJF;
import algorithms.PriorityScheduling; 
import algorithms.RoundRobin;        
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.Process;

import java.util.List;

public class SchedulerApp extends Application {
    private ObservableList<Process> processes = FXCollections.observableArrayList();
    private TextArea resultArea = new TextArea();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("CPU Scheduling Algorithms");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/images/stx.png")));
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root-pane");
        TextField processNumberInput = new TextField();
        processNumberInput.setPromptText("Process Number");
        TextField burstTimeInput = new TextField();
        burstTimeInput.setPromptText("CPU Time");
        TextField priorityInput = new TextField();
        priorityInput.setPromptText("Priority");
        TextField arrivalTimeInput = new TextField();
        arrivalTimeInput.setPromptText("Arrival Time");

        TextField timeQuantumInput = new TextField();
        timeQuantumInput.setPromptText("Time Quantum (RR)");

        Button addButton = new Button("Add Process");
        addButton.setOnAction(e -> {
            try {
                if (processNumberInput.getText().isEmpty() || burstTimeInput.getText().isEmpty() || priorityInput.getText().isEmpty() || arrivalTimeInput.getText().isEmpty()) {
                    showAlert("Error", "Please fill in all fields before adding a process.");
                    return;
                }
                int processNumber = Integer.parseInt(processNumberInput.getText());
                int burstTime = Integer.parseInt(burstTimeInput.getText());
                int priority = Integer.parseInt(priorityInput.getText());
                int arrivalTime = Integer.parseInt(arrivalTimeInput.getText());
                Process newProcess = new Process(processNumber, burstTime, priority, arrivalTime);
                processes.add(newProcess);
                processNumberInput.clear();
                burstTimeInput.clear();
                priorityInput.clear();
                arrivalTimeInput.clear();
            } catch (NumberFormatException ex) {
                showAlert("Input Error", "Please enter valid numeric values.");
            }
        });
        Button runFCFSButton = new Button("Run FCFS");
        runFCFSButton.setOnAction(e -> {
            FCFS.calculate(processes);
            displayResults("First Come First Serve (FCFS)", processes);
        });
        Button runSJFButton = new Button("Run SJF");
        runSJFButton.setOnAction(e -> {
            SJF.calculate(processes);
            displayResults("Shortest Job First (SJF)", processes);
        });

        Button runRRButton = new Button("Run RR");
        runRRButton.setOnAction(e -> {
            try {
                int timeQuantum = Integer.parseInt(timeQuantumInput.getText());
                RoundRobin.calculate(processes, timeQuantum);
                displayResults("Round Robin", processes);
            } catch (NumberFormatException ex) {
                showAlert("Input Error", "Please enter a valid Time Quantum value.");
            }
        });

        Button runPriorityButton = new Button("Run Priority");
        runPriorityButton.setOnAction(e -> {
            PriorityScheduling.calculate(processes);
            displayResults("Priority Scheduling", processes);
        });

        Button clearButton = new Button("Clear Table");
        clearButton.setOnAction(e -> {
            processes.clear();
            resultArea.clear();
        });
        Button teamInfoButton = new Button("Team Info");
        teamInfoButton.setOnAction(e -> {
            String teamInfo = "no wife no life team!\n" +
                    "moamen mohammed mostafa\n" +
                    "ali mohamed el khashab\n" +
                    "mahmoud yasser salah\n" +
                    "mohamed osama";
            showAlert("Team Info", teamInfo);
        });
        HBox buttonSection = new HBox(addButton, runFCFSButton, runSJFButton, runRRButton, runPriorityButton, clearButton, teamInfoButton);
        buttonSection.setSpacing(10);

        VBox inputSection = new VBox(processNumberInput, burstTimeInput, priorityInput, arrivalTimeInput, timeQuantumInput, buttonSection);
        inputSection.setSpacing(10);
        inputSection.setPadding(new Insets(15));
        TableView<Process> processTable = new TableView<>();
        processTable.setItems(processes);

        TableColumn<Process, Number> processNumberColumn = new TableColumn<>("Process Number");
        processNumberColumn.setCellValueFactory(cellData -> cellData.getValue().processNumberProperty());

        TableColumn<Process, Number> burstTimeColumn = new TableColumn<>("CPU Time");
        burstTimeColumn.setCellValueFactory(cellData -> cellData.getValue().burstTimeProperty());

        TableColumn<Process, Number> priorityColumn = new TableColumn<>("Priority");
        priorityColumn.setCellValueFactory(cellData -> cellData.getValue().priorityProperty());

        TableColumn<Process, Number> arrivalTimeColumn = new TableColumn<>("Arrival Time");
        arrivalTimeColumn.setCellValueFactory(cellData -> cellData.getValue().arrivalTimeProperty());

        TableColumn<Process, Number> completionTimeColumn = new TableColumn<>("Completion Time");
        completionTimeColumn.setCellValueFactory(cellData -> cellData.getValue().completionTimeProperty());

        TableColumn<Process, Number> turnaroundTimeColumn = new TableColumn<>("Turnaround Time");
        turnaroundTimeColumn.setCellValueFactory(cellData -> cellData.getValue().turnaroundTimeProperty());

        TableColumn<Process, Number> waitingTimeColumn = new TableColumn<>("Waiting Time");
        waitingTimeColumn.setCellValueFactory(cellData -> cellData.getValue().waitingTimeProperty());

        processTable.getColumns().addAll(processNumberColumn, burstTimeColumn, priorityColumn, arrivalTimeColumn, completionTimeColumn, turnaroundTimeColumn, waitingTimeColumn);
        resultArea.setEditable(false);
        resultArea.setWrapText(true);
        resultArea.setPrefHeight(150);
        VBox resultSection = new VBox(new Label("Results:"), resultArea);
        resultSection.setPadding(new Insets(15));
        resultSection.setSpacing(10);
        root.setTop(inputSection);
        root.setCenter(processTable);
        root.setBottom(resultSection);

        Scene scene = new Scene(root, 1000, 800);
        try {
            scene.getStylesheets().add(getClass().getResource("/resources/style.css").toExternalForm());
        } catch (NullPointerException e) {
            System.err.println("Error: Could not find the CSS file. Please check the path.");
        }

        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void displayResults(String algorithmName, List<Process> processes) {
        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append("Results for ").append(algorithmName).append(":\n");
        for (Process process : processes) {
            resultBuilder.append("Process ").append(process.getProcessNumber())
                    .append(" -> Completion Time: ").append(process.getCompletionTime())
                    .append(", Turnaround Time: ").append(process.getTurnaroundTime())
                    .append(", Waiting Time: ").append(process.getWaitingTime())
                    .append("\n");
        }

        resultArea.setText(resultBuilder.toString());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
