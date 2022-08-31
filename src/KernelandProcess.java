import java.util.ArrayList;

public class KernelandProcess {
    private ArrayList<UserlandProcess> processesList;

    public KernelandProcess(){
        this.processesList = new ArrayList<>();
    }

    public int GetPID(UserlandProcess userlandProcess){
        return this.processesList.indexOf(userlandProcess);
    }

    public UserlandProcess GetProcess(int PID){
        return this.processesList.get(PID);
    }

    public int AddProcess(UserlandProcess userlandProcess){
        if(this.processesList.indexOf(userlandProcess) == -1){
            this.processesList.add(userlandProcess);
        }
        return this.processesList.indexOf(userlandProcess);
    }

    public boolean RemoveProcess(UserlandProcess userlandProcess){
        if(this.processesList.indexOf(userlandProcess) == -1){
            return false;
        }else{
            this.processesList.remove(userlandProcess);
            return true;
        }
    }

    public boolean RemoveProcess(int PID){
        if(this.processesList.get(PID) != null){
            this.processesList.remove(PID);
            return true;
        }else {
            return false;
        }
    }

    public int size(){
        return this.processesList.size();
    }

}
