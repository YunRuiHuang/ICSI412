/**
 * @author Yunrui Huang
 */
public class BasicScheduler implements OSInterface {

    private KernelandProcess processList = new KernelandProcess();

    /**
     * The run method looping to run all the process in the process list
     */
    public void run(){
        while(true){
            for(int i = 0; i < processList.size(); i++){
                this.processList.GetProcess(i).run();
            }
        }

    }

    /**
     * Override the CreateProcess to add the new process into process list
     * @param myNewProcess
     * the new process ready to add into list
     * @return
     * the PID of new process
     */
    @Override
    public int CreateProcess(UserlandProcess myNewProcess) {
        return this.processList.AddProcess(myNewProcess);
    }

    /**
     * Override the DeleteProcess to remove the process from process list
     * @param processId
     * the PID of the process ready to remove
     * @return
     * turn if success, false if not found
     */
    @Override
    public boolean DeleteProcess(int processId) {
        return this.processList.RemoveProcess(processId);
    }

    @Override
    public void Sleep(int milliseconds){

    }
}
