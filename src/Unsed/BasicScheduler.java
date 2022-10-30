///**
// * @author Yunrui Huang
// */
//
//public class BasicScheduler implements OSInterface {
//
//    private KernelandProcess processList = new KernelandProcess();
//    private UserlandProcess runningProcess;
//
//    /**
//     * The run method looping to run all the process in the process list
//     * after the running loop, the running time will be save and used to update the sleep list
//     */
//    public void run(){
//        while(true){
//
//            int timeToRun = 0;
//
//            for(int i = 0; i < processList.getProcessSize(); i++){
//                this.runningProcess = this.processList.GetProcess(i);
//                timeToRun += this.runningProcess.run().millisecondsUsed;
//            }
//
//            for (int i = 0; i < this.processList.getSleepSize(); i++) {
//                this.runningProcess = this.processList.GetSleepProcess(i);
//                this.runningProcess.SetSleepTime(this.runningProcess.GetSleepTime()-timeToRun);
//                if(this.runningProcess.GetSleepTime() <= 0){
//                    this.processList.AddProcess(runningProcess);
//                    this.processList.RemoveSleepProcess(i);
//                }
//            }
//        }
//
//    }
//
//    /**
//     * Override the CreateProcess to add the new process into process list
//     * @param myNewProcess
//     * the new process ready to add into list
//     * @return
//     * the PID of new process
//     */
//    @Override
//    public int CreateProcess(UserlandProcess myNewProcess, PriorityEnum priority) {
//        return this.processList.AddProcess(myNewProcess);
//    }
//
//    /**
//     * Override the DeleteProcess to remove the process from process list
//     * @param processId
//     * the PID of the process ready to remove
//     * @return
//     * turn if success, false if not found
//     */
//    @Override
//    public boolean DeleteProcess(int processId) {
//        return this.processList.RemoveProcess(processId);
//    }
//
//    /**
//     * update the sleep time and move the process to sleep list
//     * @param milliseconds
//     * the time ready to sleep
//     */
//    @Override
//    public void Sleep(int milliseconds){
//        this.runningProcess.SetSleepTime(milliseconds);
//        this.processList.AddSleepProcess(this.runningProcess);
//        this.processList.RemoveProcess(this.runningProcess);
//    }
//}
