/**
 * @author Yunrui Huang
 */
public class OS implements OSInterface{
    private PriorityScheduler priorityScheduler = new PriorityScheduler();

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
     * Call that instance’s methods from PriorityScheduler
     * @param myNewProcess
     * the new process ready to add into list
     * @param priority
     * the priority list that process ready to add in
     * @return
     * the PID of new process
     */
    public int CreateProcess(UserlandProcess myNewProcess, PriorityEnum priority) {
        return priorityScheduler.CreateProcess(myNewProcess, priority);
    }

    /**
     * Call that instance’s methods from PriorityScheduler
     * @param processId
     * the PID of the process ready to remove
     * @return
     * turn if success, false if not found
     */
    public boolean DeleteProcess(int processId){
        return priorityScheduler.DeleteProcess(processId);
    }

    /**
     * Call that instance’s methods from PriorityScheduler
     * @param milliseconds
     * the time ready to sleep
     */
    public void Sleep(int milliseconds){
        priorityScheduler.Sleep(milliseconds);
    }

    /**
     * Call that instance’s methods from PriorityScheduler
     */
    public void run(){
        priorityScheduler.run();
    }

    /**
     * Call that instance’s methods from PriorityScheduler
     * @param s
     * the input string for open a new device, first word is device type, such as "random" "file" "pipe"
     * @return
     * the VFS id of this device
     */
    @Override
    public int Open(String s) {
        return priorityScheduler.Open(s);
    }

    /**
     * Call that instance’s methods from PriorityScheduler
     * @param id
     * the id of device need to close
     */
    @Override
    public void Close(int id) {
        priorityScheduler.Close(id);
    }

    /**
     * Call that instance’s methods from PriorityScheduler
     * @param id
     * the id of device ready to read
     * @param size
     * the length of byte ready to read from the device
     * @return
     * the data from the device
     */
    @Override
    public byte[] Read(int id, int size) {
        return priorityScheduler.Read(id,size);
    }

    /**
     * Call that instance’s methods from PriorityScheduler
     * @param id
     * the id of device ready to seek
     * @param to
     * the length of byte used to seek
     */
    @Override
    public void Seek(int id, int to) {
        priorityScheduler.Seek(id,to);
    }

    /**
     * Call that instance’s methods from PriorityScheduler
     * @param id
     * the id of device ready to write in
     * @param data
     * the data for write in to device
     * @return
     * write success or not, 0 is success and -1 is fail
     */
    @Override
    public int Write(int id, byte[] data) {
        return priorityScheduler.Write(id,data);
    }

    /**
     * Call that instance’s methods from PriorityScheduler
     * @param address
     * the virtual address of memory that process hold for write
     * @param value
     * the value ready to write into memory, one byte each time
     * @throws RescheduleException
     * if the address is out of bounds will throw this exception
     */
    @Override
    public void WriteMemory(int address, byte value) throws RescheduleException {
        priorityScheduler.WriteMemory(address,value);
    }

    /**
     * Call that instance’s methods from PriorityScheduler
     * @param address
     * the virtual memory address that process hold for read memory
     * @return
     * the data read from that memory address
     * @throws RescheduleException
     * if the address is out of bounds will throw this exception
     */
    @Override
    public byte ReadMemory(int address) throws RescheduleException {
        return priorityScheduler.ReadMemory(address);
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
        return priorityScheduler.sbrk(amount);
    }
}
