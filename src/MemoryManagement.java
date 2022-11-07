import java.util.BitSet;

/**
 * @author Yunrui Huang
 */
public class MemoryManagement implements MemoryInterface{
    private byte[][] memory;
    private BitSet tracker;
    private int pointer;

    /**
     * the constructor of MemoryManagement
     */
    public MemoryManagement() {
        this.memory = new byte[1024][1024];
        this.tracker = new BitSet(1024);
        this.tracker.clear();
        this.tracker.set(0);
        this.pointer = 0;
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
            return 0;
        }
        if(this.pointer == 0){
            for (int i = 0; i < 1024; i++) {
                if(!this.tracker.get(i)){
                    this.pointer = i*1024;
                    return 0;
                }
            }
            return 0;
        }else{
            int value = this.pointer;
            this.tracker.set(pointer/1024);
            this.pointer = 0;
            return value;
        }
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
        this.pointer = 0;
    }
}
