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
    }

    /**
     * Update the scheduling algorithm for a single CPU.
     *
     * @param cpu the process to run
     * @return Reference to the process that is executing on the CPU; result might be null
     * if no process available for scheduling.
     */
    @Override
    public Process update(Process cpu) {
        // if the queue is empty and cpu is null return null
        if (isQueueEmpty() && cpu == null) {
            return null;
        }
        // if cpu exists
        else if (cpu != null) {
            // if the process is either done with burst or completed
            if (cpu.isBurstComplete() || cpu.isExecutionComplete()) {
                // if the process burst is done but not completed
                if (cpu.isBurstComplete() && !cpu.isExecutionComplete()) {
                    // add the process back to back of queue
                    contextSwitches++;
                    queue.add(cpu);
                }
                // say that the process burst is done
                platform.log(String.format("%s burst completed", cpu.getName()));
                // if the process is completed
                if (cpu.isExecutionComplete()) {
                    platform.log(String.format("%s execution completed", cpu.getName()));
                    contextSwitches++;
                }
                contextSwitches++;
                // if the queue is not empty but cpu is null
                if (!isQueueEmpty()) {
                    // say which process is scheduled
                    platform.log(String.format("Scheduled: %s", queue.peek().getName()));
                }
                // get the next process
                return queue.poll();
            }
            // if the process is still needing to run return that process
            else if (cpu.getRemainingBurst() > 0) {
                return cpu;
            }
        }
        // if cpu is null get the next process
        else if (cpu == null) {
            platform.log(String.format("Scheduled: %s", queue.peek().getName()));
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
}

/**
 * @author Aaron Hales
 */
class CompareProcess implements Comparator<Process> {

    /**
     * set the order of the queue to have the shortest process first
     * @param p1 the first object to be compared.
     * @param p2 the second object to be compared.
     * @return the comparison
     */
    public int compare(Process p1, Process p2) {
        int a = Integer.compare(p1.getBurstTime(), p2.getBurstTime());
        int b = Integer.compare(p1.getRemainingBurst(), p2.getRemainingBurst());
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
