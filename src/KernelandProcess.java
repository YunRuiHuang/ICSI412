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
    private VirtualToPhysicalMapping[] virtualPage;
    private MemoryManagement memoryManagement;
    private Map<Integer,UserlandProcess> memoryList;
    private UserlandProcess inUseProcess;
    private int memoryPointer;
    private Map<UserlandProcess,PriorityEnum> waitList;
    private MutexObject[] mutexObjects;

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
        this.virtualPage = new VirtualToPhysicalMapping[1024];

        this.memoryList = new HashMap<>();
        this.waitList = new HashMap<>();
        this.mutexObjects = new MutexObject[10];
    }

    public void init(){
        this.memoryManagement = new MemoryManagement();
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
            ArrayList<Integer> KeyList = new ArrayList<>();
            for(Map.Entry<Integer, UserlandProcess> entry : this.deviceList.entrySet()){
                if(entry.getValue() == userlandProcess){
                    OS.getOs().Close(entry.getKey());
                    KeyList.add(entry.getKey());
                    //this.deviceList.remove(entry.getKey());
                }
            }
            for (int key:KeyList) {
                this.deviceList.remove(key);
            }
            KeyList = new ArrayList<>();
            for(Map.Entry<Integer, UserlandProcess> entry : this.memoryList.entrySet()){
                if(entry.getValue() == userlandProcess){
                    FreeMemory(entry.getKey());
                    KeyList.add(entry.getKey());
                    //this.memoryList.remove(entry.getKey());
                }
            }
            for (int key:KeyList) {
                this.memoryList.remove(key);
            }
            FreeMemory(userlandProcess);
            for (int i = 0; i < 10; i++) {
                ReleaseMutex(i,userlandProcess);
            }
            this.waitList.remove(userlandProcess);
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
            ArrayList<Integer> KeyList = new ArrayList<>();
            for(Map.Entry<Integer, UserlandProcess> entry : this.deviceList.entrySet()){
                if(entry.getValue() == removeProcess){
                    OS.getOs().Close(entry.getKey());
                    KeyList.add(entry.getKey());
                    //this.deviceList.remove(entry.getKey());
                }
            }
            for (int key:KeyList) {
                this.deviceList.remove(key);
            }
            KeyList = new ArrayList<>();
            for(Map.Entry<Integer, UserlandProcess> entry : this.memoryList.entrySet()){
                if(entry.getValue() == removeProcess){
                    FreeMemory(entry.getKey());
                    KeyList.add(entry.getKey());
                    //this.memoryList.remove(entry.getKey());
                }
            }
            for (int key:KeyList) {
                this.memoryList.remove(key);
            }
            FreeMemory(removeProcess);
            for (int i = 0; i < 10; i++) {
                ReleaseMutex(i,removeProcess);
            }
            this.waitList.remove(removeProcess);
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

    /**
     * Write the data into memory
     * @param address
     * the physical address of memory
     * @param value
     * the value ready to write into memory, one byte each time
     * @throws RescheduleException
     * if the address is out of bounds will throw this exception
     */
    public void WriteMemory(int address, byte value) throws RescheduleException{
        this.virtualPage[address/1024].isDirty = true;

        this.memoryManagement.WriteMemory(VirtualToPhysicalMapping(address),value);

    }

    /**
     * Read the data from memory
     * @param address
     * the physical memory address
     * @return
     * the data read from that memory address
     * @throws RescheduleException
     * if the address is out of bounds will throw this exception
     */
    public byte ReadMemory(int address) throws RescheduleException{
        return this.memoryManagement.ReadMemory(VirtualToPhysicalMapping(address));
    }

    /**
     * get a new space of memory from the memory
     * @param amount
     * the space request from process. if over 1024 byte, will only return 0
     * @return
     * first time call will return 0 and second time call will return a free memory space address
     */
    public int sbrk(int amount, UserlandProcess process){

        if(this.inUseProcess == null){
            this.inUseProcess = process;
            return 0;
        }
        if(this.inUseProcess == process){
            int page = this.memoryPointer / 1024;

            if(page == (this.memoryPointer+amount-1)/1024){

                if(this.virtualPage[page] == null){
                    this.virtualPage[page] = new VirtualToPhysicalMapping();
                    this.memoryList.put(page,process);

                }
                page = this.memoryPointer;
                this.memoryPointer = this.memoryPointer + amount;
                return page;
            }else{
                for (int i = page; i < (this.memoryPointer+amount-1)/1024; i++) {
                    if(this.virtualPage[i] == null){
                        this.virtualPage[i] = new VirtualToPhysicalMapping();
                        this.memoryList.put(i,process);
                    }
                }
                page = this.memoryPointer;
                this.memoryPointer = this.memoryPointer + amount;
                return page;
            }
        }else{
            this.inUseProcess = process;
            if(this.virtualPage[memoryPointer/1024] != null){
                this.memoryPointer = (this.memoryPointer / 1024 + 1) * 1024;
            }
            return 0;
        }

    }

    /**
     * free a memory space via the physical address
     * @param address
     * the physical address of memory ready to free
     */
    public void FreeMemory(int address){
        int phyAddr = this.virtualPage[address].physicalAddress;
        if(phyAddr != -1){
            this.memoryManagement.FreeMemory(phyAddr);
        }
        int diskAdd = this.virtualPage[address].diskPage;
        if(diskAdd != -1){
            this.memoryManagement.ClearDisk(diskAdd);
        }
        this.virtualPage[address] = null;
    }

    /**
     * free all memory space of a process
     * @param process
     * the process ready to free all the memory space
     */
    public void FreeMemory(UserlandProcess process){
        ArrayList<Integer> KeyList = new ArrayList<>();
        for(Map.Entry<Integer, UserlandProcess> entry : this.memoryList.entrySet()){
            if(entry.getValue() == process){
                FreeMemory(entry.getKey());
                KeyList.add(entry.getKey());
                //this.memoryList.remove(entry.getKey());
            }
        }
        for (int key:KeyList) {
            this.memoryList.remove(key);
        }
    }

    /**
     * clear the TLB in the memory management class, run it when change process
     */
    public void ClearTlb(){
        this.memoryManagement.ClearTlb();
    }

    private int nextUnmapPage;

    /**
     * mapping the virtual address to physical address, also handle swapping
     * include swapping other page to disk and load the data from disk to memory
     * also, after load the physical page, it will be updated to TLB
     * for testing, I hard code in 5 max physical page can be use, over that other virtual page will start steal page
     * @param virtual
     * the address of virtual page
     * @return
     * the physical page address, which after finish all work such as swap, get new page, or check the TLB address
     * @throws RescheduleException
     * throw except when out of bound
     */
    //need a new method to handel V to P address
    public int VirtualToPhysicalMapping(int virtual) throws RescheduleException{
        int page = virtual / 1024;
        int offset = virtual % 1024;
        if(this.virtualPage[page] == null){
            throw new RescheduleException();
        }
        if(this.memoryManagement.GetTlbVirtual() == page){
            return this.memoryManagement.GetTlbPhysical() * 1024 + offset;
        }

        if(this.virtualPage[page].physicalAddress != -1){
            this.memoryManagement.SetTlbVirtual(page);
            this.memoryManagement.SetTlbPhysical(this.virtualPage[page].physicalAddress/1024);
            return this.virtualPage[page].physicalAddress + offset;
        }else{
            if(page < 5){ //look for a new physical address
                this.virtualPage[page].physicalAddress = this.memoryManagement.sbrk(1024);
                if(this.virtualPage[page].diskPage != -1){
                    this.memoryManagement.loadData(this.virtualPage[page].physicalAddress,this.virtualPage[page].diskPage);
                }
                this.memoryManagement.SetTlbVirtual(page);
                this.memoryManagement.SetTlbPhysical(this.virtualPage[page].physicalAddress/1024);
                return this.virtualPage[page].physicalAddress + offset;
            }else{ //take other process physical address
                while (true){
                    if (this.virtualPage[nextUnmapPage].physicalAddress != -1){
                        this.virtualPage[page].physicalAddress = this.virtualPage[nextUnmapPage].physicalAddress;
                        if(this.virtualPage[nextUnmapPage].isDirty){
                            this.virtualPage[nextUnmapPage].diskPage = this.memoryManagement.saveToDisk(this.virtualPage[nextUnmapPage].physicalAddress,this.virtualPage[nextUnmapPage].diskPage);
                            this.virtualPage[nextUnmapPage].isDirty = false;
                        }
                        if(this.virtualPage[page].diskPage != -1){
                            this.memoryManagement.loadData(this.virtualPage[page].physicalAddress,this.virtualPage[page].diskPage);
                        }
                        this.virtualPage[nextUnmapPage].physicalAddress = -1;
                        if (this.virtualPage[nextUnmapPage + 1] == null) {
                            nextUnmapPage = 0;
                        }else{
                            nextUnmapPage = nextUnmapPage + 1;
                        }
                        this.memoryManagement.SetTlbVirtual(page);
                        this.memoryManagement.SetTlbPhysical(this.virtualPage[page].physicalAddress/1024);
                        return this.virtualPage[page].physicalAddress + offset;
                    }else{
                        if (this.virtualPage[nextUnmapPage + 1] == null) {
                            nextUnmapPage = 0;
                        }else{
                            nextUnmapPage = nextUnmapPage + 1;
                        }

                    }
                }

            }
        }
    }


    /**
     * attach a process to the mutex object base on the mutex name
     * if no such mutex, just make a new mutex
     * @param name
     * the name of mutex for attach
     * @param runningProcess
     * the process ready to attach to the mutex
     * @return
     * the id of mutex, we can add max 10 mutex, fail to attach will return -1
     */
    public int AttachToMutex(String name, UserlandProcess runningProcess){
        for (int i = 0; i < 10; i++) {
            if(this.mutexObjects[i] != null){
                if(this.mutexObjects[i].name.equalsIgnoreCase(name)){
                    this.mutexObjects[i].attachList.add(runningProcess);
                    return i;
                }
            }
        }
        for (int i = 0; i < 10; i++) {
            if(this.mutexObjects[i] == null){
                this.mutexObjects[i] = new MutexObject(name,runningProcess);
                return i;
            }
        }
        return -1;
    }

    /**
     * Release the process from a mutex
     * if the attachlist in this mutex become empty, which not process is attach, this mutex will delete
     * @param mutexId
     * the id of mutex of process attach to
     * @param runningProcess
     * the process ready for release the mutex
     */
    public void ReleaseMutex(int mutexId, UserlandProcess runningProcess){
        if(this.mutexObjects[mutexId] == null){
            return;
        }
        this.mutexObjects[mutexId].attachList.remove(runningProcess);
        if(this.mutexObjects[mutexId].inUseProcess == runningProcess){
            Unlock(mutexId,runningProcess);
        }
        if(this.mutexObjects[mutexId].attachList.isEmpty()){
            System.out.println("\t\tMutex : " + this.mutexObjects[mutexId].name + " is no attach and has been delete!");
            this.mutexObjects[mutexId] = null;
        }
    }

    /**
     * Lock the mutex
     * @param mutexId
     * the id of mutex ready to lock
     * @param runningProcess
     * the process ready to lock the mutex
     * @return
     * return true if success or already lock by this process, false if already lock
     */
    public boolean Lock(int mutexId, UserlandProcess runningProcess){
        if(this.mutexObjects[mutexId].flag){
            if(this.mutexObjects[mutexId].inUseProcess == runningProcess){
                return true;
            }
            return false;
        }else{
            this.mutexObjects[mutexId].flag = true;
            this.mutexObjects[mutexId].inUseProcess = runningProcess;
            return true;
        }
    }

    /**
     * Unlock the mutex, if the mutex is lock by current process
     * @param mutexId
     * the id of mutex ready for unlock
     * @param runningProcess
     * the process ready to unlock the mutex
     */
    public void Unlock(int mutexId, UserlandProcess runningProcess){
        if(this.mutexObjects[mutexId].inUseProcess == runningProcess){
            this.mutexObjects[mutexId].flag = false;
            this.mutexObjects[mutexId].inUseProcess = null;
            if(this.mutexObjects[mutexId].attachList.size() > 1){
                for (int i = 1; i < this.mutexObjects[mutexId].attachList.size(); i++) {
                    if(ReleaseWaitProcess(this.mutexObjects[mutexId].attachList.get(i))){
                        break;
                    }
                }
            }
        }
    }

    /**
     * add the process to the wait list
     * @param userlandProcess
     * the process ready to add in wait list
     * @param priorityEnum
     * the priority of this process
     * @return
     * the process id
     */
    public int AddWaitProcess(UserlandProcess userlandProcess, PriorityEnum priorityEnum){
        if(this.waitList.get(userlandProcess) == null){
            this.waitList.put(userlandProcess,priorityEnum);
        }
        return this.processesList.indexOf(userlandProcess);
    }

    /**
     * release the process from wait list back to run list
     * @param userlandProcess
     * the process need to release
     * @return
     * true if successful release, false if no such process in wait list
     */
    public boolean ReleaseWaitProcess(UserlandProcess userlandProcess){
        if(this.waitList.get(userlandProcess) != null){
            if(this.waitList.get(userlandProcess) == PriorityEnum.RealTime){
                this.realTimeList.add(userlandProcess);
                this.waitList.remove(userlandProcess);
                return true;
            }else if(this.waitList.get(userlandProcess) == PriorityEnum.Interactive){
                this.interactiveList.add(userlandProcess);
                this.waitList.remove(userlandProcess);
                return true;
            }else if(this.waitList.get(userlandProcess) == PriorityEnum.Background){
                this.backgroundList.add(userlandProcess);
                this.waitList.remove(userlandProcess);
                return true;
            }
        }
        return false;
    }

}
