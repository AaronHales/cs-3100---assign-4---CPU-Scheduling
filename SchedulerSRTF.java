public class SchedulerSRTF implements Scheduler {
    public SchedulerSRTF(Platform platform) {
    }

    /**
     * A scheduler must track the number of context switches performed during the simulation.
     * This method returns that count.
     *
     * @return The number of context switches that occurred during the simulation
     */
    @Override
    public int getNumberOfContextSwitches() {
        return 0;
    }

    /**
     * Used to notify the scheduler a new process has just entered the ready state.
     *
     * @param p
     */
    @Override
    public void notifyNewProcess(Process p) {

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
        return null;
    }
}
