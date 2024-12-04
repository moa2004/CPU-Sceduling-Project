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
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Process;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class SchedulerApp extends Application {
    private ObservableList<Process> processes = FXCollections.observableArrayList();
    private TextArea resultArea = new TextArea();
    private PieChart pieChart = new PieChart();

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
        Button runFCFSButton = new Button("Run FCFS");
        Button runSJFButton = new Button("Run SJF");
        Button runRRButton = new Button("Run RR");
        Button runPriorityButton = new Button("Run Priority");
        Button clearButton = new Button("Clear Table");
        Button teamInfoButton = new Button("Team Info");
        Button pieChartButton = new Button("Show PieChart");
        Button exportButton = new Button("Export Report");
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

                if (burstTime <= 0 || arrivalTime < 0 || priority <= 0) {
                    showAlert("Input Error", "Please enter positive values for Burst Time and Priority, and a non-negative value for Arrival Time.");
                    return;
                }

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
        exportButton.setOnAction(e -> {
            if (processes.isEmpty()) {
                showAlert("Error", "Please add processes before exporting the report.");
                return;
            }
            exportReport();
        });
        pieChartButton.setOnAction(e -> showPieChartWindow());
        teamInfoButton.setOnAction(e -> {
            String teamInfo = "A'MMM Gonna Kill my self 😔!\n" +
                    "Moamen Mohammed Mostafa 😍\n" +
                    "Ali Mohamed El Khashab 😘\n" +
                    "Mahmoud Yasser Salah ❤\n" +
                    "Mohamed Osama 💕";
            showAlert("Team Info", teamInfo);
        });
        clearButton.setOnAction(e -> {
            processes.clear();
            resultArea.clear();
        });
        runPriorityButton.setOnAction(e -> {
            PriorityScheduling.
                    calculate(processes);
            displayResults("Priority Scheduling", processes);
        });
        runRRButton.setOnAction(e -> {
            try {
                int timeQuantum = Integer.parseInt(timeQuantumInput.getText());
                RoundRobin.calculate(processes, timeQuantum);
                displayResults("Round Robin", processes);
            } catch (NumberFormatException ex) {
                showAlert("Input Error", "Please enter a valid Time Quantum value.");
            }
        });
        runSJFButton.setOnAction(e -> {
            SJF.calculate(processes);
            displayResults("Shortest Job First (SJF)", processes);
        });
        runFCFSButton.setOnAction(e -> {
            FCFS.calculate(processes);
            displayResults("First Come First Serve (FCFS)", processes);
        });
        TableView<Process> processTable = new TableView<>();
        processTable.setItems(processes);
        processTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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

        processTable.getColumns().addAll(
                processNumberColumn, burstTimeColumn, priorityColumn,
                arrivalTimeColumn, completionTimeColumn, turnaroundTimeColumn, waitingTimeColumn
        );
        HBox buttonSection = new HBox(addButton, runFCFSButton, runSJFButton, runRRButton, runPriorityButton, clearButton, teamInfoButton, pieChartButton, exportButton);
        buttonSection.setSpacing(10);

        VBox inputSection = new VBox(processNumberInput, burstTimeInput, priorityInput, arrivalTimeInput, timeQuantumInput, buttonSection);
        inputSection.setSpacing(10);
        inputSection.setPadding(new Insets(15));

        // إعداد منطقة النتائج
        resultArea.setEditable(false);
        resultArea.setWrapText(true);
        resultArea.setPrefHeight(150);
        VBox resultSection = new VBox(new Label("Results:"), resultArea);
        resultSection.setPadding(new Insets(15));
        resultSection.setSpacing(10);

        VBox centerSection = new VBox(processTable);
        centerSection.setPadding(new Insets(10));
        centerSection.setSpacing(10);
        VBox.setVgrow(processTable, Priority.ALWAYS);

        root.setTop(inputSection);
        root.setCenter(centerSection);
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
    private void showPieChartWindow() {
        Stage pieChartStage = new Stage();
        pieChartStage.setTitle("Pie Chart - Process Burst Time Distribution");

        PieChart pieChartWindow = new PieChart();
        int totalBurstTime = processes.stream().mapToInt(Process::getBurstTime).sum();
        for (Process process : processes) {
            double percentage = (double) process.getBurstTime() / totalBurstTime * 100;
            PieChart.Data slice = new PieChart.Data("Process " + process.getProcessNumber() + " (" + process.getBurstTime() + ")", percentage);
            pieChartWindow.getData().add(slice);
        }

        Scene pieChartScene = new Scene(pieChartWindow, 600, 400);
        pieChartStage.setScene(pieChartScene);
        pieChartStage.show();
    }

    private void exportReport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("CPU Scheduling Report\n");
                writer.write("=====================\n\n");

                for (Process process : processes) {
                    writer.write("Process " + process.getProcessNumber() + "\n");
                    writer.write("Burst Time: " + process.getBurstTime() + "\n");
                    writer.write("Priority: " + process.getPriority() + "\n");
                    writer.write("Arrival Time: " + process.getArrivalTime() + "\n");
                    writer.write("Completion Time: " + process.getCompletionTime() + "\n");
                    writer.write("Turnaround Time: " + process.getTurnaroundTime() + "\n");
                    writer.write("Waiting Time: " + process.getWaitingTime() + "\n");
                    writer.write("\n");
                }

                writer.flush();
                showAlert("Export Successful", "Report has been saved successfully!");
            } catch (IOException e) {
                showAlert("Export Error", "An error occurred while saving the report.");
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

