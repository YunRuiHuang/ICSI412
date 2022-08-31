
public class BasicScheduler implements OSInterface {

    private KernelandProcess processList = new KernelandProcess();

    public void run(){
        while(true){
            for(int i = 0; i < processList.size(); i++){
                this.processList.GetProcess(i).run();
            }
        }

    }

    @Override
    public int CreateProcess(UserlandProcess myNewProcess) {
        return this.processList.AddProcess(myNewProcess);
    }

    @Override
    public boolean DeleteProcess(int processId) {
        return this.processList.RemoveProcess(processId);
    }
}
