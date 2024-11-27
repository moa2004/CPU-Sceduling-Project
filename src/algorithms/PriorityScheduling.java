package algorithms;

import models.Process;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PriorityScheduling {

    public static void calculate(List<Process> processes) {
        Collections.sort(processes, Comparator.comparingInt(Process::getPriority));

        int currentTime = 0;

        for (Process process : processes) {
            if (currentTime < process.getArrivalTime()) {
                currentTime = process.getArrivalTime();
            }

            process.setCompletionTime(currentTime + process.getBurstTime());
            process.setTurnaroundTime(process.getCompletionTime() - process.getArrivalTime());
            process.setWaitingTime(process.getTurnaroundTime() - process.getBurstTime());

            currentTime += process.getBurstTime();
        }
    }
}
