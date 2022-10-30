import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Yunrui Huang
 */
public class KernelandProcess {
    private ArrayList<UserlandProcess> processesList;
    private ArrayList<UserlandProcess> realTimeList;
    private ArrayList<UserlandProcess> interactiveList;
    private ArrayList<UserlandProcess> backgroundList;
    private Map<UserlandProcess,PriorityEnum> sleepList;
    private Map<Integer,UserlandProcess> deviceList;

    /**
     * the constructor of KernelandProcess class
     */
    public KernelandProcess(){
        this.processesList = new ArrayList<>();
        this.realTimeList = new ArrayList<>();
        this.interactiveList = new ArrayList<>();
        this.backgroundList = new ArrayList<>();
        this.sleepList = new HashMap<>();
        this.deviceList = new HashMap<>();
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
     * @param priorityEnum
     * the priority list that process ready to add in
     * @return
     * the PID of this new process
     */
    public int AddProcess(UserlandProcess userlandProcess, PriorityEnum priorityEnum ){
        if(this.processesList.indexOf(userlandProcess) == -1){
            if(priorityEnum == PriorityEnum.RealTime){
                this.processesList.add(userlandProcess);
                this.realTimeList.add(userlandProcess);
            }else if(priorityEnum == PriorityEnum.Interactive){
                this.processesList.add(userlandProcess);
                this.interactiveList.add(userlandProcess);
            }else if(priorityEnum == PriorityEnum.Background){
                this.processesList.add(userlandProcess);
                this.backgroundList.add(userlandProcess);
            }
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
            this.realTimeList.remove(userlandProcess);
            this.interactiveList.remove(userlandProcess);
            this.backgroundList.remove(userlandProcess);
            for(Map.Entry<Integer, UserlandProcess> entry : this.deviceList.entrySet()){
                if(entry.getValue() == userlandProcess){
                    OS.getOs().Close(entry.getKey());
                }
            }
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
            UserlandProcess removeProcess = this.processesList.get(PID);
            this.processesList.remove(removeProcess);
            this.realTimeList.remove(removeProcess);
            this.interactiveList.remove(removeProcess);
            this.backgroundList.remove(removeProcess);
            for(Map.Entry<Integer, UserlandProcess> entry : this.deviceList.entrySet()){
                if(entry.getValue() == removeProcess){
                    OS.getOs().Close(entry.getKey());
                }
            }
            return true;
        }else {
            return false;
        }
    }


    /**
     * get a sleep process via a process ID
     * @param PID
     * the process ID used to find the sleep process
     * @return
     * the process of that PID
     */
    public UserlandProcess GetSleepProcess(int PID){
        UserlandProcess sleepProcess = this.processesList.get(PID);
        if(this.sleepList.get(sleepProcess) != null){
            return sleepProcess;
        }
        return null;
    }

    /**
     * Add a new process into sleepList
     * @param userlandProcess
     * the process ready to add into list
     * @param priorityEnum
     * the list that process come from
     * @return
     * the PID of this process
     */
    public int AddSleepProcess(UserlandProcess userlandProcess, PriorityEnum priorityEnum){
        if(this.sleepList.get(userlandProcess) == null){
            this.sleepList.put(userlandProcess,priorityEnum);
        }
        return this.processesList.indexOf(userlandProcess);
    }

    /**
     * Remove a process from the sleepList by PID
     * @param PID
     * the process ID of the process ready to remove
     * @return
     * true if remove successful, false if not found
     */
    public boolean RemoveSleepProcess(int PID){
        if(this.sleepList.get(this.processesList.get(PID)) != null){
            this.sleepList.remove(this.processesList.get(PID));
            return true;
        }else {
            return false;
        }
    }

    /**
     * Remove a process form the sleepList by process
     * @param userlandProcess
     * the process ready to remove
     * @return
     * true if remove successful, false if not found
     */
    public boolean RemoveSleepProcess(UserlandProcess userlandProcess){
        if(this.sleepList.get(userlandProcess) == null){
            return false;
        }else{
            this.sleepList.remove(userlandProcess);
            return true;
        }
    }


    /**
     * get the size of the process list
     * @return
     * the number of process
     */
    public int GetProcessSize(){
        return this.processesList.size();
    }


    /**
     * get the size of the sleepList
     * @return
     * the number of process in sleepList
     */
    public int GetSleepSize(){
        return this.sleepList.size();
    }

    /**
     * check and update the process sleep time left in the sleep list
     * @param millisecondsUsed
     * the time that has pass
     */
    public void CheckSleep(int millisecondsUsed){
        for(Map.Entry<UserlandProcess, PriorityEnum> entry : this.sleepList.entrySet()){
            UserlandProcess userlandProcess = entry.getKey();
            PriorityEnum priorityEnum = entry.getValue();
            userlandProcess.SetSleepTime(userlandProcess.GetSleepTime()-millisecondsUsed);
            if(userlandProcess.GetSleepTime() <= 0){

                if(priorityEnum == PriorityEnum.RealTime){
                    this.sleepList.remove(userlandProcess);
                    this.realTimeList.add(userlandProcess);
                }else if(priorityEnum == PriorityEnum.Interactive){
                    this.sleepList.remove(userlandProcess);
                    this.interactiveList.add(userlandProcess);
                }else if(priorityEnum == PriorityEnum.Background){
                    this.sleepList.remove(userlandProcess);
                    this.backgroundList.add(userlandProcess);
                }

            }
        }
    }

    /**
     * get next process for run from real time list
     * @return
     * the next process in the real time list, return NULL if empty
     */
    public UserlandProcess GetRealTimeProcess(){
        if(this.realTimeList.isEmpty()){
            return null;
        }
        UserlandProcess userlandProcess = this.realTimeList.get(0);
        this.realTimeList.remove(userlandProcess);
        return userlandProcess;
    }

    /**
     * get next process for run from interactive list
     * @return
     * the next process in the interactive list, return NULL if empty
     */
    public UserlandProcess GetInteractiveProcess(){
        if(this.interactiveList.isEmpty()){
            return null;
        }
        UserlandProcess userlandProcess = this.interactiveList.get(0);
        this.interactiveList.remove(userlandProcess);
        return userlandProcess;
    }

    /**
     * get next process for run from background list
     * @return
     * the next process in the background list, return NULL if empty
     */
    public UserlandProcess GetBackgroundProcess(){
        if(this.backgroundList.isEmpty()){
            return null;
        }
        UserlandProcess userlandProcess = this.backgroundList.get(0);
        this.backgroundList.remove(userlandProcess);
        return userlandProcess;
    }

    /**
     * put the process back to the end of real time list
     * @param userlandProcess
     * the process finish running and back to the real time list
     */
    public void AddRealTimeProcess(UserlandProcess userlandProcess){
        this.realTimeList.add(userlandProcess);
    }

    /**
     * put the process back to the end of interactive list
     * @param userlandProcess
     * the process finish running and back to the interactive list
     */
    public void AddInteractiveProcess(UserlandProcess userlandProcess){
        this.interactiveList.add(userlandProcess);
    }

    /**
     * put the process back to the end of background list
     * @param userlandProcess
     * the process finish running and back to the background list
     */
    public void AddBackgroundProcess(UserlandProcess userlandProcess){
        this.backgroundList.add(userlandProcess);
    }

    /**
     * Add the open device to the device check list
     * @param id
     * the VFS id of open device
     * @param process
     * the process own this device
     */
    public void AddDivice(int id, UserlandProcess process){
        this.deviceList.put(id,process);
    }

}
