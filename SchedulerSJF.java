import java.util.*;

public class SchedulerSJF implements Scheduler {

    // the reference to platform
    private Platform platform;
    //the number of times a task goes from one state to another
    private int contextSwitches = 0;
    // the queue of processes
    private PriorityQueue<Process> queue;

    public SchedulerSJF(Platform platform) {
        // saves the reference to platform to use later
        this.platform = platform;
        // creates the queue
        queue = new PriorityQueue<>(1, new CompareProcess());
    }

    /**
     * A scheduler must track the number of context switches performed during the simulation.
     * This method returns that count.
     *
     * @return The number of context switches that occurred during the simulation
     */
    @Override
    public int getNumberOfContextSwitches() {
        return contextSwitches;
    }

    /**
     * @author Aaron Hales
     * Used to notify the scheduler a new process has just entered the ready state.
     *
     * @param p the process to add to the queue.
     */
    @Override
    public void notifyNewProcess(Process p) {
        // puts task in ready state;
        queue.add(p);
        contextSwitches++;
//        for (Process process : queue) {
//            System.out.println(process.getName());
//        }
//        System.out.printf("size: %s\n\n", queue.size());
    }

    /**
     * Update the scheduling algorithm for a single CPU.
     *
     * @param cpu
     * @return Reference to the process that is executing on the CPU; result might be null
     * if no process available for scheduling.
     */
    @Override
    public Process update(Process cpu) {
        if (isQueueEmpty() && cpu == null) {
            return null;
        }
        else if (cpu != null) {
            if (cpu.isBurstComplete() || cpu.isExecutionComplete()) {
                if (cpu.isBurstComplete() && !cpu.isExecutionComplete()) {
                    contextSwitches++;
                    queue.add(cpu);
                }
                platform.log(String.format("%s burst completed", cpu.getName()));
                if (cpu.isExecutionComplete()) {
                    platform.log(String.format("%s execution completed", cpu.getName()));
                }
                contextSwitches++;
                if (!isQueueEmpty()) {
                    platform.log(String.format("Scheduled: %s", queue.peek().getName()));
                }
                return queue.poll();
            }
            else if (cpu.getRemainingBurst() > 0) {
                return cpu;
            }
        }
        else if (cpu == null) {
            platform.log(String.format("Scheduled: %s", queue.peek().getName()));
            contextSwitches++;
            return queue.poll();
        }
        return null;
    }

    /**
     * @author Aaron Hales
     * checks if the queue is empty
     * @return true of empty, false if not empty
     */
    private boolean isQueueEmpty() {
        return queue.isEmpty();
    }

    private void shuffleQueue() {

    }

}

class CompareProcess implements Comparator<Process> {

    public int compare(Process p1, Process p2) {
        int a = Integer.compare(p1.getRemainingBurst(), p2.getRemainingBurst());
        int b = Integer.compare(p1.getBurstTime(), p2.getBurstTime());
        int c = Integer.compare(p2.getTotalTime(), p1.getTotalTime());
        if(a == 0 && b ==0){
            return c;
        }
        else if (a==0 && b!=0){
            return b;
        }
        return a;

    }
}
