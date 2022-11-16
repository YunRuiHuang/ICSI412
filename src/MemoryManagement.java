import java.util.BitSet;

/**
 * @author Yunrui Huang
 */
public class MemoryManagement implements MemoryInterface{
    private byte[][] memory;
    private BitSet tracker;
    private int tlbVirtual = -1;
    private int tlbPhysical = -1;
    private int fileID;
    private int nextPage;

    /**
     * the constructor of MemoryManagement
     */
    public MemoryManagement() {
        this.memory = new byte[1024][1024];
        this.tracker = new BitSet(1024);
        this.tracker.clear();
        this.tracker.set(0);
        this.fileID = OS.getOs().getVFS().Open("file swapfile");
        this.nextPage = 0;
    }

    /**
     * Write the data into memory
     * @param address
     * the physical address of memory for write
     * @param value
     * the value ready to write into memory, one byte each time
     * @throws RescheduleException
     * if the address is out of bounds or page not in use will throw this exception
     */
    @Override
    public void WriteMemory(int address, byte value) throws RescheduleException {
        int page = address / 1024;
        int offset = address % 1024;
        byte input = value;
        if(page >= 1024 || page < 0){
            throw new RescheduleException();
        }
        if(!this.tracker.get(page)){
            throw new RescheduleException();
        }
        this.memory[page][offset] = input;
    }

    /**
     * Read the data from memory
     * @param address
     * the physical memory address
     * @return
     * the data read from that memory address
     * @throws RescheduleException
     * if the address is out of bounds or page not in use will throw this exception
     */
    @Override
    public byte ReadMemory(int address) throws RescheduleException {
        int page = address / 1024;
        int offset = address % 1024;
        if(page >= 1024 || page < 0){
            throw new RescheduleException();
        }
        if(!this.tracker.get(page)){
            throw new RescheduleException();
        }
        byte value = this.memory[page][offset];
        return value;
    }

    /**
     * get a new space of memory from the memory, right now we give one whole page every time
     * @param amount
     * the space request from process. if over 1024 byte, will only return 0
     * @return
     * first time call will return 0 and second time call will return a free memory space address
     */
    @Override
    public int sbrk(int amount) {
        if(amount > 1024){
            return -1;
        }
        for (int i = 0; i < 1024; i++) {
            if(!this.tracker.get(i)){
                this.tracker.set(i);
                return i * 1024;
            }
        }
        return -1;
    }

    /**
     * free a memory space via the physical address
     * @param address
     * the physical address of memory ready to free
     */
    public void FreeMemory(int address){
        int page = address / 1024;
        this.memory[page] = new byte[1024];
        this.tracker.clear(page);
    }

    /**
     * get the virtual page address of TLB
     * @return
     * the virtual page address of TLB
     */
    public int GetTlbVirtual(){
        return this.tlbVirtual;
    }

    /**
     * Set the virtual page address of TLB
     * @param tlbVirtual
     * the virtual page address of TLB
     */
    public void SetTlbVirtual(int tlbVirtual){
        this.tlbVirtual = tlbVirtual;
    }

    /**
     * get the physical page address of TLB
     * @return
     * the physical page address of TLB
     */
    public int GetTlbPhysical(){
        return this.tlbPhysical;
    }

    /**
     * set the physical page address of TLB
     * @param tlbPhysical
     * the physical page address of TLB
     */
    public void SetTlbPhysical(int tlbPhysical){
        this.tlbPhysical = tlbPhysical;
    }

    /**
     * clear the TLB value, both will set to -1, call it when switch process
     */
    public void ClearTlb(){
        this.tlbVirtual = -1;
        this.tlbPhysical = -1;
    }

    /**
     * load the data from disk back to memory
     * @param address
     * the physical page address use to load the data in
     * @param diskPage
     * the disk page use to load the data from
     */
    public void loadData(int address, int diskPage){
        System.out.println(" MemoryManagement > Load disk page " + diskPage + " to memory address " + address);
        OS.getOs().Seek(this.fileID,diskPage*1024);
        this.memory[address/1024] = new byte[1024];
        this.memory[address/1024] = OS.getOs().Read(this.fileID,1024);
    }

    /**
     * save the data from memory page to disk
     * @param address
     * the physical page address ready to save the data in to disk
     * @param diskPage
     * the disk page use to save the data to, If first time swap data, just input -1 and method will sign a new page to save data
     * @return
     * the disk page address, if input not -1, just return same address
     */
    public int saveToDisk(int address, int diskPage){

        byte[] buffer = this.memory[address/1024];
        if(diskPage == -1){
            OS.getOs().Seek(this.fileID,this.nextPage*1024);
            this.nextPage = this.nextPage + 1;
            OS.getOs().Write(this.fileID,buffer);
            this.memory[address/1024] = new byte[1024];
            System.out.println(" MemoryManagement > save memory " + address + " to disk page " + (nextPage-1));
            return nextPage - 1;
        }else{
            OS.getOs().Seek(this.fileID,diskPage*1024);
            OS.getOs().Write(this.fileID,buffer);
            this.memory[address/1024] = new byte[1024];
            return diskPage;
        }
    }

    /**
     * clear the specific page in the disk, call it when free the memory but has been swap to disk
     * @param diskPage
     * the disk page ready to clear
     */
    public void ClearDisk(int diskPage){
        OS.getOs().Seek(this.fileID,diskPage*1024);
        OS.getOs().Write(this.fileID,new byte[1024]);
    }
}
