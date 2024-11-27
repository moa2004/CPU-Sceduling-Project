package models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Process {
    private final IntegerProperty processNumber;
    private final IntegerProperty burstTime;
    private final IntegerProperty priority;
    private final IntegerProperty arrivalTime;
    private final IntegerProperty completionTime;
    private final IntegerProperty turnaroundTime;
    private final IntegerProperty waitingTime;
    private int remainingTime;
    public Process(int processNumber, int burstTime, int priority, int arrivalTime) {
        this.processNumber = new SimpleIntegerProperty(processNumber);
        this.burstTime = new SimpleIntegerProperty(burstTime);
        this.priority = new SimpleIntegerProperty(priority);
        this.arrivalTime = new SimpleIntegerProperty(arrivalTime);
        this.completionTime = new SimpleIntegerProperty(0);
        this.turnaroundTime = new SimpleIntegerProperty(0);
        this.waitingTime = new SimpleIntegerProperty(0);
        this.remainingTime = burstTime;
    }
    public int getProcessNumber() {
        return processNumber.get();
    }

    public IntegerProperty processNumberProperty() {
        return processNumber;
    }
    public int getBurstTime() {
        return burstTime.get();
    }

    public IntegerProperty burstTimeProperty() {
        return burstTime;
    }
    public int getPriority() {
        return priority.get();
    }

    public IntegerProperty priorityProperty() {
        return priority;
    }
    public int getArrivalTime() {
        return arrivalTime.get();
    }

    public IntegerProperty arrivalTimeProperty() {
        return arrivalTime;
    }
    public int getCompletionTime() {
        return completionTime.get();
    }

    public void setCompletionTime(int completionTime) {
        this.completionTime.set(completionTime);
    }

    public IntegerProperty completionTimeProperty() {
        return completionTime;
    }
    public int getTurnaroundTime() {
        return turnaroundTime.get();
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime.set(turnaroundTime);
    }

    public IntegerProperty turnaroundTimeProperty() {
        return turnaroundTime;
    }
    public int getWaitingTime() {
        return waitingTime.get();
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime.set(waitingTime);
    }

    public IntegerProperty waitingTimeProperty() {
        return waitingTime;
    }
    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }
}
