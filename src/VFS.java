import java.util.HashMap;

/**
 * @author Yunrui Huang
 */
public class VFS implements Device{

    /**
     * an inner class use for store device information
     */
    private class DeviceInfo{
        public int id;
        public String diviceType;
        public DeviceInfo(int id, String diviceType){
            this.id = id;;
            this.diviceType = diviceType;
        }
    }

    private RandomDevice randomDevice;
    private FakeFileSystem fakeFileSystem;
    private PipeDevice pipeDevice;
    private HashMap<Integer,DeviceInfo> map;

    /**
     * constructor of VFS
     */
    public VFS(){
        this.randomDevice = new RandomDevice();
        this.fakeFileSystem = new FakeFileSystem();
        this.pipeDevice = new PipeDevice();
        this.map = new HashMap<>();
    }

    /**
     * Open a new device base on the first word of input string
     * @param s
     * the input string for open a new device, first word is device type, such as "random" "file" "pipe"
     * @return
     * the VFS id of this device
     */
    @Override
    public int Open(String s) {
        String[] input = s.split(" ");

        if(input[0].equalsIgnoreCase("random")){
            int id = this.randomDevice.Open(input[1]);
            for (int i = 0; i < 100; i++) {
                if (this.map.get(i) == null){
                    this.map.put(i,new DeviceInfo(id,"random"));
                    return i;
                }
            }
        }else if(input[0].equalsIgnoreCase("file")){
            int id = this.fakeFileSystem.Open(input[1]);
            for (int i = 0; i < 100; i++) {
                if (this.map.get(i) == null){
                    this.map.put(i,new DeviceInfo(id,"file"));
                    return i;
                }
            }
        }else if(input[0].equalsIgnoreCase("pipe")){
            int id = this.pipeDevice.Open(input[1]);
            for (int i = 0; i < 100; i++) {
                if (this.map.get(i) == null){
                    this.map.put(i,new DeviceInfo(id,"pipe"));
                    return i;
                }
            }
        }

        return 0;
    }

    /**
     * Close the device and remove from list
     * @param id
     * the VFS id use to search and close the device
     */
    @Override
    public void Close(int id) {
        DeviceInfo deviceInfo = this.map.get(id);
        if(this.map.get(id) == null){
            return;
        }
        this.map.remove(id);
        if(deviceInfo.diviceType.equals("random")){
            this.randomDevice.Close(deviceInfo.id);
        }else if(deviceInfo.diviceType.equals("file")){
            this.fakeFileSystem.Close(deviceInfo.id);
        }else if(deviceInfo.diviceType.equals("pipe")){
            this.pipeDevice.Close(deviceInfo.id);
        }

    }

    /**
     * Read from the device
     * @param id
     * the VFS id of device ready to read
     * @param size
     * the length of byte ready to read from the device
     * @return
     * the data from the device
     */
    @Override
    public byte[] Read(int id, int size) {
        DeviceInfo deviceInfo = this.map.get(id);
        if(this.map.get(id) == null){
            return null;
        }

        if(deviceInfo.diviceType.equalsIgnoreCase("random")){
            return this.randomDevice.Read(deviceInfo.id,size);
        }else if(deviceInfo.diviceType.equalsIgnoreCase("file")){
            return this.fakeFileSystem.Read(deviceInfo.id,size);
        }else if(deviceInfo.diviceType.equalsIgnoreCase("pipe")){
            return this.pipeDevice.Read(deviceInfo.id,size);
        }
        return new byte[0];
    }

    /**
     * Seek from the device
     * @param id
     * the VFS id of device ready to seek
     * @param to
     * the length of byte used to seek
     */
    @Override
    public void Seek(int id, int to) {
        DeviceInfo deviceInfo = this.map.get(id);
        if(this.map.get(id) == null){
            return;
        }

        if(deviceInfo.diviceType.equalsIgnoreCase("random")){
            this.randomDevice.Seek(deviceInfo.id,to);
        }else if(deviceInfo.diviceType.equalsIgnoreCase("file")){
            this.fakeFileSystem.Seek(deviceInfo.id,to);
        }else if(deviceInfo.diviceType.equalsIgnoreCase("pipe")){
            this.pipeDevice.Seek(deviceInfo.id,to);
        }
    }

    /**
     * Write the data into the device
     * @param id
     * the VFS id of device ready to write in
     * @param data
     * the data for write in to device
     * @return
     * write success or not, 0 is success and -1 is fail
     */
    @Override
    public int Write(int id, byte[] data) {
        DeviceInfo deviceInfo = this.map.get(id);
        if(this.map.get(id) == null){
            return -1;
        }

        if(deviceInfo.diviceType.equalsIgnoreCase("random")){
            return this.randomDevice.Write(deviceInfo.id,data);
        }else if(deviceInfo.diviceType.equalsIgnoreCase("file")){
            return this.fakeFileSystem.Write(deviceInfo.id,data);
        }else if(deviceInfo.diviceType.equalsIgnoreCase("pipe")){
            return this.pipeDevice.Write(deviceInfo.id,data);
        }
        return 0;
    }
}
