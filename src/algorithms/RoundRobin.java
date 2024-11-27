package algorithms;

import models.Process;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;

public class RoundRobin {

    public static void calculate(List<Process> processes, int timeQuantum) {
        Queue<Process> queue = new LinkedList<>(processes);
        int currentTime = 0;

        while (!queue.isEmpty()) {
            Process currentProcess = queue.poll();

            if (currentProcess.getArrivalTime() > currentTime) {
                currentTime = currentProcess.getArrivalTime();
            }

            if (currentProcess.getRemainingTime() > timeQuantum) {
                currentTime += timeQuantum;
                currentProcess.setRemainingTime(currentProcess.getRemainingTime() - timeQuantum);
                queue.add(currentProcess);
            } else {
                currentTime += currentProcess.getRemainingTime();
                currentProcess.setRemainingTime(0);
                currentProcess.setCompletionTime(currentTime);
                currentProcess.setTurnaroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());
                currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
            }
        }
    }
}
