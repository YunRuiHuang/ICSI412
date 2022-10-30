/**
 * @author Yunrui Huang
 */
public class PipeInstance {
    private String name;
    private byte[] buffer;
    private int user;

    /**
     * the constructor of PipeInstance class
     * @param name
     * the name of this pipe
     */
    public PipeInstance(String name) {
        this.name = name;
        this.buffer = new byte[0];
        this.user = 1;
    }

    /**
     * update the number of user use this pipe
     * @param user
     * the number of user
     */
    public void setUser(int user) {
        this.user = user;
    }

    /**
     * check the number of user use this pipe
     * @return
     * the number of user
     */
    public int getUser() {
        return user;
    }

    /**
     * set the data of buffer in this pipe
     * @param buffer
     * the buffer data
     */
    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    /**
     * get the pipe name
     * @return
     * the name of pipe
     */
    public String getName() {
        return name;
    }

    /**
     * get the data of buffer in this pipe
     * @return
     * the data buffer in this pipe
     */
    public byte[] getBuffer() {
        return buffer;
    }
}
