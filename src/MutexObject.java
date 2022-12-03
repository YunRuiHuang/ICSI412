import java.util.ArrayList;

/**
 * @author Yunrui Huang
 */

public class MutexObject {
    public String name;
    public boolean flag;
    public UserlandProcess inUseProcess;
    public ArrayList<UserlandProcess> attachList;

    /**
     * the constructor of MutexObject class
     * @param name
     * the name of this mutex object
     * @param attachProcess
     * thie first process attach to this mutex
     */
    public MutexObject(String name,UserlandProcess attachProcess){
        this.name = name;
        this.flag = false;
        this.inUseProcess = null;
        this.attachList = new ArrayList<>();
        this.attachList.add(attachProcess);
    }

}
