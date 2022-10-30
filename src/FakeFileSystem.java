import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;

/**
 * @author Yunrui Huang
 */
public class FakeFileSystem implements Device{

    private HashMap<Integer,RandomAccessFile> randomAccessFile;

    /**
     * the constructor of FakeFileSystem class
     */
    public FakeFileSystem(){
        this.randomAccessFile = new HashMap<>();
    }

    /**
     * Open a new file device and save to list
     * @param s
     * the file name use to open, if empty throw an exception
     * @return
     * the device id in the file device list
     */
    @Override
    public int Open(String s) {
        if(s.isEmpty()){
            throw new RuntimeException("no file name");
        }

        try{
            for (int i = 0; i < 10; i++) {
                if(this.randomAccessFile.get(i) == null){
                    this.randomAccessFile.put(i,new RandomAccessFile(s,"rw"));
                    return i;
                }
            }
        }catch(FileNotFoundException e){}


        return -1;
    }

    /**
     * Close a file device and remove from list
     * @param id
     * the device id on the file device list
     */
    @Override
    public void Close(int id) {
        RandomAccessFile file = this.randomAccessFile.get(id);
        if(this.randomAccessFile.get(id) == null){
            return;
        }
        try{
            file.close();
        }catch(IOException e){}
        this.randomAccessFile.remove(id);
    }

    /**
     * Read the value from the file device
     * @param id
     * the device id on the file device list
     * @param size
     * the data size of how many byte has to read
     * @return
     * the file data on request size
     */
    @Override
    public byte[] Read(int id, int size) {
        RandomAccessFile file = this.randomAccessFile.get(id);
        if(this.randomAccessFile.get(id) == null){
            return null;
        }
        byte[] output = new byte[size];

        try{
            file.read(output);
        }catch(IOException e){}

        return output;
    }

    /**
     * Seek the byte on the file device
     * @param id
     * device id on the file device list
     * @param to
     * the length of byte request to seek
     */
    @Override
    public void Seek(int id, int to) {
        RandomAccessFile file = this.randomAccessFile.get(id);
        if(this.randomAccessFile.get(id) == null){
            return;
        }
        try{
            file.seek(to);
        }catch(IOException e){}
    }

    /**
     * Write the data into the file device
     * @param id
     * device id on the file device list
     * @param data
     * the data ready to write in to the file
     * @return
     * return 0 if success, -1 if fail
     */
    @Override
    public int Write(int id, byte[] data) {
        RandomAccessFile file = this.randomAccessFile.get(id);
        if(this.randomAccessFile.get(id) == null){
            return -1;
        }

        try{
            file.write(data);
        }catch(IOException e){}

        return 0;
    }
}
