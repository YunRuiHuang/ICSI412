import java.util.HashMap;
import java.util.Map;

/**
 * @author Yunrui Huang
 */
public class PipeDevice implements Device{

    private HashMap<Integer,PipeInstance> pipeList;

    /**
     * the constructor of PipeDevice class
     */
    public PipeDevice(){
        this.pipeList = new HashMap<>();
    }

    /**
     * Open a new pipe device and save to list if without has same named, else return the pipe id
     * @param s
     * the pipe name use to open or attach the pipe
     * @return
     * the device id in the pipe device list
     */
    @Override
    public int Open(String s) {
        for (Map.Entry<Integer,PipeInstance> entry:this.pipeList.entrySet()){
            if(entry.getValue().getName().equals(s)){
                entry.getValue().setUser(entry.getValue().getUser()+1);
                return entry.getKey();
            }
        }
        for (int i = 0; i < 10; i++) {
            if(this.pipeList.get(i) == null){
                this.pipeList.put(i,new PipeInstance(s));
                return i;
            }
        }
        return 0;
    }

    /**
     * Close a file device and remove from list if pipe has no user
     * @param id
     * the device id on the pipe device list
     */
    @Override
    public void Close(int id) {
        PipeInstance pipeInstance = this.pipeList.get(id);
        if(this.pipeList.get(id) == null){
            return;
        }
        pipeInstance.setUser(pipeInstance.getUser()-1);
        if(pipeInstance.getUser() == 0){
            this.pipeList.remove(id);
        }

    }

    /**
     * Read the value from the pipe device
     * @param id
     * the device id on the pipe device list
     * @param size
     * the data size of how many byte has to read, if more than buffer size, just return buffer
     * @return
     * the buffer data on request size
     */
    @Override
    public byte[] Read(int id, int size) {
        PipeInstance pipeInstance = this.pipeList.get(id);
        if(this.pipeList.get(id) == null){
            return null;
        }
        int bufferSize = pipeInstance.getBuffer().length;
        if(size >= bufferSize){
            byte[] buffer = pipeInstance.getBuffer();
            pipeInstance.setBuffer(new byte[0]);
            return buffer;
        }else{
            byte[] getBuffer = pipeInstance.getBuffer();
            byte[] output = new byte[size];
            byte[] buffer = new byte[bufferSize - size];
            for (int i = 0; i < size; i++) {
                output[i] = getBuffer[i];
            }
            for (int i = size; i < bufferSize; i++) {
                buffer[i-size] = getBuffer[i];
            }
            pipeInstance.setBuffer(buffer);
            return output;
        }
    }

    /**
     * Seek the byte on the pipe device
     * @param id
     * device id on the pipe device list
     * @param to
     * the length of byte request to seek on buffer
     */
    @Override
    public void Seek(int id, int to) {
        PipeInstance pipeInstance = this.pipeList.get(id);
        if(this.pipeList.get(id) == null){
            return;
        }
        int bufferSize = pipeInstance.getBuffer().length;
        if(to >= bufferSize){
            pipeInstance.setBuffer(new byte[0]);
        }else{
            byte[] getBuffer = pipeInstance.getBuffer();
            byte[] buffer = new byte[bufferSize - to];
            for (int i = to; i < bufferSize; i++) {
                buffer[i-to] = getBuffer[i];
            }
            pipeInstance.setBuffer(buffer);
        }
    }

    /**
     * Write the data into the pipe device
     * @param id
     * device id on the pipe device list
     * @param data
     * the data ready to write in to the buffer
     * @return
     * return 0 if success, -1 if fail
     */
    @Override
    public int Write(int id, byte[] data) {
        PipeInstance pipeInstance = this.pipeList.get(id);
        if(this.pipeList.get(id) == null){
            return -1;
        }
        byte[] pipe = pipeInstance.getBuffer();
        byte[] buffer = new byte[data.length + pipe.length];
        for (int i = 0; i < buffer.length; i++) {
            if(i<pipe.length){
                buffer[i] = pipe[i];
            }else{
                buffer[i] = data[i- pipe.length];
            }
        }
        pipeInstance.setBuffer(buffer);
        return 0;
    }
}
