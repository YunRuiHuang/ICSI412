import java.util.Random;
import java.util.concurrent.TimeUnit;
/**
 * @author Yunrui Huang
 */
public class PriorityScheduler implements OSInterface {
    private KernelandProcess processList = new KernelandProcess();
    private UserlandProcess runningProcess;
    private PriorityEnum runningPriorityEnum;
    private boolean ifNotSleep = true;

    /**
     * The run method looping to run all the process in the process list
     * after the running loop, the running time will be save and used to update the sleep list
     */
    public void run(){
        while(true){

            int timeToRun = 0;

            Random random = new Random();
            int ran = random.nextInt(10);

            if(ran < 6){ // 6 out of 10 times will run real time list
                //real time
                this.runningProcess = this.processList.GetRealTimeProcess();

                //if the list empty skip this run
                if(this.runningProcess == null){
                    System.out.println(" Real Time list is empty");
                    continue;
                }

                //check if process time out 5 times, downgrade it priority
                if(this.runningProcess.GetAbusingCount() >= 5){
                    this.runningPriorityEnum = PriorityEnum.Interactive;
                    this.runningProcess.SetAbusingCount(0);
                }else{
                    this.runningPriorityEnum = PriorityEnum.RealTime;
                }

                //running process
                RunResult runResult = this.runningProcess.run();
                timeToRun = runResult.millisecondsUsed;

                //update the timeout counter if timeout
                if(runResult.ranToTimeout){
                    this.runningProcess.SetAbusingCount(this.runningProcess.GetAbusingCount() + 1);
                }

                //put the process back to list if not sleep
                if(ifNotSleep){
                    if(this.runningPriorityEnum == PriorityEnum.RealTime){
                        this.processList.AddRealTimeProcess(this.runningProcess);
                    }else if(this.runningPriorityEnum == PriorityEnum.Interactive){
                        this.processList.AddInteractiveProcess(this.runningProcess);
                    }else{
                        this.processList.AddBackgroundProcess(this.runningProcess);
                    }

                }else{
                    this.ifNotSleep = true;
                }

            }else if(ran == 9){// 1 out of 10 times will run background list
                //background
                this.runningProcess = this.processList.GetBackgroundProcess();
                if(this.runningProcess == null){
                    System.out.println(" Background list is empty");
                    continue;
                }
                this.runningPriorityEnum = PriorityEnum.Background;
                RunResult runResult = this.runningProcess.run();
                timeToRun = runResult.millisecondsUsed;
                if(runResult.ranToTimeout){
                    this.runningProcess.SetAbusingCount(this.runningProcess.GetAbusingCount() + 1);
                }
                if(ifNotSleep){
                    if(this.runningPriorityEnum == PriorityEnum.RealTime){
                        this.processList.AddRealTimeProcess(this.runningProcess);
                    }else if(this.runningPriorityEnum == PriorityEnum.Interactive){
                        this.processList.AddInteractiveProcess(this.runningProcess);
                    }else{
                        this.processList.AddBackgroundProcess(this.runningProcess);
                    }
                }else{
                    this.ifNotSleep = true;
                }

            }else{// 3 out of 10 times will run interactive list
                //interactive
                this.runningProcess = this.processList.GetInteractiveProcess();
                if(this.runningProcess == null){
                    System.out.println(" interactive list is empty");
                    continue;
                }

                if(this.runningProcess.GetAbusingCount() >= 5){
                    this.runningPriorityEnum = PriorityEnum.Background;
                    this.runningProcess.SetAbusingCount(0);
                }else{
                    this.runningPriorityEnum = PriorityEnum.Interactive;
                }

                RunResult runResult = this.runningProcess.run();
                timeToRun = runResult.millisecondsUsed;
                if(runResult.ranToTimeout){
                    this.runningProcess.SetAbusingCount(this.runningProcess.GetAbusingCount() + 1);
                }
                if(ifNotSleep){
                    if(this.runningPriorityEnum == PriorityEnum.RealTime){
                        this.processList.AddRealTimeProcess(this.runningProcess);
                    }else if(this.runningPriorityEnum == PriorityEnum.Interactive){
                        this.processList.AddInteractiveProcess(this.runningProcess);
                    }else{
                        this.processList.AddBackgroundProcess(this.runningProcess);
                    }
                }else{
                    this.ifNotSleep = true;
                }
            }

            //slow down the process running
            this.processList.CheckSleep(timeToRun);
            try{
                TimeUnit.SECONDS.sleep(1);
            }catch(InterruptedException e){}

        }

    }

    /**
     * Override the CreateProcess to add the new process into process list
     * @param myNewProcess
     * the new process ready to add into list
     * @param priority
     * the priority list that process ready to add in
     * @return
     * the PID of new process
     */
    @Override
    public int CreateProcess(UserlandProcess myNewProcess, PriorityEnum priority) {
        return this.processList.AddProcess(myNewProcess, priority);
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

    /**
     * update the sleep time and move the process to sleep list
     * @param milliseconds
     * the time ready to sleep
     */
    @Override
    public void Sleep(int milliseconds){
        this.runningProcess.SetSleepTime(milliseconds);
        this.processList.AddSleepProcess(this.runningProcess,this.runningPriorityEnum);
        this.ifNotSleep = false;
    }
}
