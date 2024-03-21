import java.util.LinkedList;

public class SchedulerFCFS implements Scheduler {

    // the reference to platform
    private Platform platform;
    //the number of times a task goes from one state to another
    private int contextSwitches = 0;
    // the queue of processes
    private LinkedList<Process> queue;


    /**
     * @author Aaron Hales
     * the default constructor for SchedulerFCFS
     * @param platform the reference to platform
     */
    public SchedulerFCFS(Platform platform) {
        // saves the reference to platform to use later
        this.platform = platform;
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
        queue.add(p);
        // puts task in ready sate;
        contextSwitches++;
        System.out.printf("Process: %s was added to queue\n", p.getName());
    }

    /**
     * @author Aaron Hales
     * Update the scheduling algorithm for a single CPU.
     *
     * @param cpu the process that is currently running on the cpu
     * @return Reference to the process that is executing on the CPU; result might be null
     * if no process available for scheduling.
     */
    @Override
    public Process update(Process cpu) {
        // if the current process exists
        if (cpu != null) {
            // if the process is done
            if (cpu.isExecutionComplete()) {
                platform.log(String.format("%s burst complete", cpu.getName()));
                platform.log(String.format("%s execution complete", cpu.getName()));
            }
            // if the process's burst is done
            else if (cpu.isBurstComplete()) {
                platform.log(String.format("%s burst complete", cpu.getName()));
                // re-add the process to the end of the queue
                queue.add(cpu);
                // increase the number of context switches
                contextSwitches++;
            }
            // if process is done or burst is done and the queue is not empty
            if ((cpu.isExecutionComplete() || cpu.isBurstComplete()) && !isQueueEmpty()) {
                // increase the number of context switches
                contextSwitches++;
                // say which process is scheduled now
                platform.log(String.format("Scheduled: %s", queue.peek().getName()));
                // return the next process in queue
                return queue.pop();
            }
            // if the process is not done return itself
            if (cpu.getRemainingBurst() > 0) {
                return cpu;
            }
        }

        // if the process does not exist get the first task in the queue
        if (cpu == null) {
            platform.log(String.format("Scheduled: %s", queue.peek().getName()));
            contextSwitches++;
            return queue.pop();
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
