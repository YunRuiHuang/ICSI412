/**
 * @author Yunrui Huang
 */
public class OS implements OSInterface{
    private PriorityScheduler priorityScheduler = new PriorityScheduler();

    /**
     * new an OS class to Singleton Pattern
     */
    private static OS os = new OS();

    /**
     * the private constructor of OS class
     */
    private OS(){}

    /**
     * get the Singleton Pattern OS class
     * @return
     */
    public static OS getOs(){
        return os;
    }

    /**
     * Call that instance’s methods from PriorityScheduler
     * @param myNewProcess
     * the new process ready to add into list
     * @param priority
     * the priority list that process ready to add in
     * @return
     * the PID of new process
     */
    public int CreateProcess(UserlandProcess myNewProcess, PriorityEnum priority) {
        return priorityScheduler.CreateProcess(myNewProcess, priority);
    }

    /**
     * Call that instance’s methods from PriorityScheduler
     * @param processId
     * the PID of the process ready to remove
     * @return
     * turn if success, false if not found
     */
    public boolean DeleteProcess(int processId){
        return priorityScheduler.DeleteProcess(processId);
    }

    /**
     * Call that instance’s methods from PriorityScheduler
     * @param milliseconds
     * the time ready to sleep
     */
    public void Sleep(int milliseconds){
        priorityScheduler.Sleep(milliseconds);
    }

    /**
     * Call that instance’s methods from PriorityScheduler
     */
    public void run(){
        priorityScheduler.run();
    }



}
