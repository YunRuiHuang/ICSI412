import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
/**
 * @author Yunrui Huang
 */
public class PriorityScheduler implements OSInterface {
    private VFS vfs = new VFS();
    private KernelandProcess processList = new KernelandProcess();
    private UserlandProcess runningProcess;
    private PriorityEnum runningPriorityEnum;
    private boolean ifNotSleep = true;

    /**
     * The run method looping to run all the process in the process list
     * after the running loop, the running time will be save and used to update the sleep list
     */
    public void run(){
        this.processList.init();
        while(true){

            this.processList.ClearTlb();
            int timeToRun = 0;

            Random random = new Random();
            int ran = random.nextInt(10);

            if(ran < 6){ // 6 out of 10 times will run real time list
                //real time
                this.runningProcess = this.processList.GetRealTimeProcess();

                //if the list empty skip this run
                if(this.runningProcess == null){
//                    System.out.println(" Real Time list is empty");
                    continue;
                }

                //check if process time out 5 times, downgrade it priority
                if(this.runningProcess.GetAbusingCount() >= 5){
                    this.runningPriorityEnum = PriorityEnum.Interactive;
                    this.runningProcess.SetAbusingCount(0);
                }else{
                    this.runningPriorityEnum = PriorityEnum.RealTime;
                }

                RunResult runResult;
                try{
                    //running process
                     runResult = this.runningProcess.run();
                }catch(RescheduleException e){
                    this.processList.RemoveProcess(this.runningProcess);
                    this.processList.ClearTlb();
                    this.runningPriorityEnum = null;
                    this.runningProcess = null;
                    this.ifNotSleep = true;
                    System.out.println("===kill the process===");
                    continue;
                }

                this.processList.FreeMemory(this.runningProcess);
                timeToRun = runResult.millisecondsUsed;

                //check if any device need to close
                int[] file = runResult.fileID;
                if(file != null){
                    for (int i = 0; i < file.length; i++) {
                        OS.getOs().Close(file[i]);
                        System.out.println("\t\tdevice close, Id : " + file[i]);
                    }
                }

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
//                    System.out.println(" Background list is empty");
                    continue;
                }
                this.runningPriorityEnum = PriorityEnum.Background;
                RunResult runResult;
                try{
                    //running process
                    runResult = this.runningProcess.run();
                }catch(RescheduleException e){
                    this.processList.RemoveProcess(this.runningProcess);
                    this.processList.ClearTlb();
                    this.runningPriorityEnum = null;
                    this.runningProcess = null;
                    this.ifNotSleep = true;
                    System.out.println("===kill the process===");
                    continue;
                }
                this.processList.FreeMemory(this.runningProcess);
                timeToRun = runResult.millisecondsUsed;
                int[] file = runResult.fileID;
                if(file != null){
                    for (int i = 0; i < file.length; i++) {
                        OS.getOs().Close(file[i]);
                        System.out.println("\t\tdevice close, Id : " + file[i]);
                    }
                }
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
//                    System.out.println(" interactive list is empty");
                    continue;
                }

                if(this.runningProcess.GetAbusingCount() >= 5){
                    this.runningPriorityEnum = PriorityEnum.Background;
                    this.runningProcess.SetAbusingCount(0);
                }else{
                    this.runningPriorityEnum = PriorityEnum.Interactive;
                }

                RunResult runResult;
                try{
                    //running process
                    runResult = this.runningProcess.run();
                }catch(RescheduleException e){
                    this.processList.RemoveProcess(this.runningProcess);
                    this.processList.ClearTlb();
                    this.runningPriorityEnum = null;
                    this.runningProcess = null;
                    this.ifNotSleep = true;
                    System.out.println("===kill the process===");
                    continue;
                }
                this.processList.FreeMemory(this.runningProcess);
                timeToRun = runResult.millisecondsUsed;
                int[] file = runResult.fileID;
                if(file != null){
                    for (int i = 0; i < file.length; i++) {
                        OS.getOs().Close(file[i]);
                        System.out.println("\t\tdevice close, Id : " + file[i]);
                    }
                }
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

    /**
     * call the VFS to open a new device
     * @param s
     * the input string for open a new device, first word is device type, such as "random" "file" "pipe"
     * @return
     * the VFS id of this device
     */
    @Override
    public int Open(String s) {
        int id = this.vfs.Open(s);
        this.processList.AddDivice(id,this.runningProcess);
        return id;
    }

    /**
     * Call the VFS to close a device
     * @param id
     * the id of device need to close
     */
    @Override
    public void Close(int id) {
        this.vfs.Close(id);
    }

    /**
     * Call the VFS to read a device
     * @param id
     * the id of device ready to read
     * @param size
     * the length of byte ready to read from the device
     * @return
     * the data from the device
     */
    @Override
    public byte[] Read(int id, int size) {

        return this.vfs.Read(id,size);
    }

    /**
     * Call the VFS to seek a device
     * @param id
     * the id of device ready to seek
     * @param to
     * the length of byte used to seek
     */
    @Override
    public void Seek(int id, int to) {
        this.vfs.Seek(id,to);
    }

    /**
     * Call the VFS to write in a device
     * @param id
     * the id of device ready to write in
     * @param data
     * the data for write in to device
     * @return
     * write success or not, 0 is success and -1 is fail
     */
    @Override
    public int Write(int id, byte[] data) {
        return this.vfs.Write(id,data);
    }

    /**
     * call the KernelandProcess to Write the data into memory, If the address not match to the TLB, using the address to search the physical address first
     * @param address
     * the virtual address of memory that process hold for write
     * @param value
     * the value ready to write into memory, one byte each time
     * @throws RescheduleException
     * if the address is out of bounds will throw this exception
     */
    @Override
    public void WriteMemory(int address, byte value) throws RescheduleException {
//        if(this.tlbVirtual != address){
//            this.tlbVirtual = -1;
//            this.tlbPhysical = this.processList.CheckTLB(address);
//            if(this.tlbPhysical == -1){
//                throw new RescheduleException();
//            }
//            this.tlbVirtual = address;
//        }
        this.processList.WriteMemory(address,value);
    }

    /**
     * call the KernelandProcess to Read the data from memory, If the address not match to the TLB, using the address to search the physical address first
     * @param address
     * the virtual memory address that process hold for read memory
     * @return
     * the data read from that memory address
     * @throws RescheduleException
     * if the address is out of bounds will throw this exception
     */
    @Override
    public byte ReadMemory(int address) throws RescheduleException {
//        if(this.tlbVirtual != address){
//            this.tlbVirtual = -1;
//            this.tlbPhysical = this.processList.CheckTLB(address);
//            if(this.tlbPhysical == -1){
//                throw new RescheduleException();
//            }
//            this.tlbVirtual = address;
//        }
        return this.processList.ReadMemory(address);
    }

    /**
     * get a new space of memory from the memory
     * @param amount
     * the space request from process. if over 1024 byte, will only return 0
     * @return
     * first time call will return 0 and second time call will return a free memory space address
     */
    @Override
    public int sbrk(int amount) {
        return this.processList.sbrk(amount,this.runningProcess);
    }

    /**
     * pass the VFS to MemoryManagement class to use the FFS to creat the swap file
     * @return
     * the VFS in the KLP
     */
    public VFS getVfS(){
        return this.vfs;
    }


    /**
     * attach a process to the mutex object base on the mutex name
     * @param name
     * the name of mutex for attach
     * @return
     * the id of mutex, we can add max 10 mutex, fail to attach will return -1
     */
    @Override
    public int AttachToMutex(String name) {
        return this.processList.AttachToMutex(name,runningProcess);
    }

    /**
     * Lock the mutex
     * @param mutexId
     * the id of mutex ready to lock
     * @return
     * return true if success or already lock by this process, false if already lock
     */
    @Override
    public boolean Lock(int mutexId) {
        boolean result = this.processList.Lock(mutexId,runningProcess);
        if(result){
            return true; //success
        }else{
            this.processList.AddWaitProcess(runningProcess,runningPriorityEnum);
            this.ifNotSleep = false;
            return false;
        }

    }

    /**
     * Unlock the mutex, if the mutex is lock by current process
     * @param mutexId
     * the id of mutex ready for unlock
     */
    @Override
    public void Unlock(int mutexId) {
        this.processList.Unlock(mutexId,runningProcess);
    }

    /**
     * Release the process from a mutex
     * @param mutexId
     * the id of mutex of process attach to
     */
    @Override
    public void ReleaseMutex(int mutexId) {
        this.processList.ReleaseMutex(mutexId,runningProcess);
    }
}
