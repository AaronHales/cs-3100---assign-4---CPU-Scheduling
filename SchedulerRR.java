import java.util.LinkedList;

public class SchedulerRR implements Scheduler {

    // the reference to platform
    private Platform platform;
    // the number of times a task goes from one state to another
    private int contextSwitches = 0;
    // the time quantum
    private int timeQuantum;
    // the queue of processes
    private LinkedList<Process> queue;

    public SchedulerRR(Platform platform, int i) {
        // saves the reference to platform to use later
        this.platform = platform;
        // saves the time quantum
        this. timeQuantum = i;
        // creates the queue
        queue = new LinkedList<>();
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
     * @param cpu
     * @return Reference to the process that is executing on the CPU; result might be null
     * if no process available for scheduling.
     */
    @Override
    public Process update(Process cpu) {
        if (cpu == null) {
            platform.log(String.format("Scheduled: %s", queue.peek().getName()));
            return queue.poll();
        }
        if (isQueueEmpty() && cpu == null) {
            return null;
        }
        else if (cpu != null) {
            if (cpu.isBurstComplete() || cpu.isExecutionComplete() || cpu.getRemainingBurst() % timeQuantum == 0) {
                // if the process burst is done but not completed
                if ((cpu.isBurstComplete() || cpu.getRemainingBurst() % timeQuantum == 0) && !cpu.isExecutionComplete()) {
                    if (cpu.isBurstComplete()) {
                        // say that the process burst is done
                        platform.log(String.format("Process %s burst completed", cpu.getName()));
                    }
                    // if the time quantum is expired say so
                    else if (cpu.getRemainingBurst() % timeQuantum == 0) {
                        platform.log(String.format("Time quantum completed for process %s", cpu.getName()));
                    }
                    // add the process back to back of queue
                    contextSwitches++;
                    queue.add(cpu);
                }
                // if the process is completed
                if (cpu.isExecutionComplete()) {
                    platform.log(String.format("Process %s execution completed", cpu.getName()));
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
