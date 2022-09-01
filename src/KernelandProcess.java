import java.util.ArrayList;

/**
 * @author Yunrui Huang
 */
public class KernelandProcess {
    private ArrayList<UserlandProcess> processesList;

    /**
     * the constructor of KernelandProcess class
     */
    public KernelandProcess(){
        this.processesList = new ArrayList<>();
    }

    /**
     * get a process ID via a UserlandProcess
     * @param userlandProcess
     * the process used to find the ID
     * @return
     * the ID of the process, return -1 if not found
     */
    public int GetPID(UserlandProcess userlandProcess){
        return this.processesList.indexOf(userlandProcess);
    }

    /**
     * get a process via a process ID
     * @param PID
     * the process ID used to find the process
     * @return
     * the process of the PID
     */
    public UserlandProcess GetProcess(int PID){
        return this.processesList.get(PID);
    }

    /**
     * Add a new process into list
     * @param userlandProcess
     * the process ready to add into list
     * @return
     * the PID of this new process
     */
    public int AddProcess(UserlandProcess userlandProcess){
        if(this.processesList.indexOf(userlandProcess) == -1){
            this.processesList.add(userlandProcess);
        }
        return this.processesList.indexOf(userlandProcess);
    }

    /**
     * Remove a process form the list by process
     * @param userlandProcess
     * the process ready to remove
     * @return
     * true if remove successful, false if not found
     */
    public boolean RemoveProcess(UserlandProcess userlandProcess){
        if(this.processesList.indexOf(userlandProcess) == -1){
            return false;
        }else{
            this.processesList.remove(userlandProcess);
            return true;
        }
    }

    /**
     * Remove a process from the list by PID
     * @param PID
     * the process ID of the process ready to remove
     * @return
     * true if remove successful, false if not found
     */
    public boolean RemoveProcess(int PID){
        if(this.processesList.get(PID) != null){
            this.processesList.remove(PID);
            return true;
        }else {
            return false;
        }
    }

    /**
     * get the size of the process list
     * @return
     * the number of process
     */
    public int size(){
        return this.processesList.size();
    }

}
