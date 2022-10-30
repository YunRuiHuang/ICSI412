import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * @author Yunrui Huang
 */
public class RandomDevice implements Device{


    private HashMap<Integer,Random> randomList;

    /**
     * the constructor of RandomDevice class
     */
    public RandomDevice(){
        this.randomList = new HashMap<>();
    }

    /**
     * Open a new random device and save to list
     * @param s
     * the string will convert to integer and use as seek
     * @return
     * the device id in the random device list
     */
    @Override
    public int Open(String s) {

        for (int i = 0; i < 10; i++) {
            if(this.randomList.get(i) == null){
                if(!s.isEmpty()){
                    this.randomList.put(i,new Random(Integer.parseInt(s)));
                    return i;
                }else{
                    this.randomList.put(i,new Random());
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Close a random device and remove from list
     * @param id
     * the device id on the random device list
     */
    @Override
    public void Close(int id) {
        this.randomList.remove(id);
    }

    /**
     * Read a new random value from the device
     * @param id
     * the device id on the random device list
     * @param size
     * the data size of how many byte has to return
     * @return
     * the random data on request size
     */
    @Override
    public byte[] Read(int id, int size) {
        byte[] output = new byte[size];
        Random random = this.randomList.get(id);
        if(this.randomList.get(id) == null){

            return null;
        }
        random.nextBytes(output);
        return output;
    }

    /**
     * Seek the next random byte
     * @param id
     * device id on the random device list
     * @param to
     * the length of byte request to read
     */
    @Override
    public void Seek(int id, int to) {
        byte[] output = new byte[to];
        Random random = this.randomList.get(id);
        if(this.randomList.get(id) == null){
            return;
        }
        random.nextBytes(output);
    }

    /**
     * Write in to random device, just return zero and do nothing
     * @return
     * return 0
     */
    @Override
    public int Write(int id, byte[] data) {
        return 0;
    }
}
