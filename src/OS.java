/**
 * @author Yunrui Huang
 */
public class OS implements OSInterface{
    private BasicScheduler basicScheduler = new BasicScheduler();

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
     * Call that instance’s methods from BasicScheduler
     * @param myNewProcess
     * the new process ready to add into list
     * @return
     * the PID of new process
     */
    public int CreateProcess(UserlandProcess myNewProcess) {
        return basicScheduler.CreateProcess(myNewProcess);
    }

    /**
     * Call that instance’s methods from BasicScheduler
     * @param processId
     * the PID of the process ready to remove
     * @return
     * turn if success, false if not found
     */
    public boolean DeleteProcess(int processId){
        return basicScheduler.DeleteProcess(processId);
    }

    /**
     * Call that instance’s methods from BasicScheduler
     * @param milliseconds
     * the time ready to sleep
     */
    public void Sleep(int milliseconds){
        basicScheduler.Sleep(milliseconds);
    }

    /**
     * Call that instance’s methods from BasicScheduler
     */
    public void run(){
        basicScheduler.run();
    }



}
