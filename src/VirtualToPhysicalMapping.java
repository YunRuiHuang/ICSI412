/**
 * @author Yunrui Huang
 */
public class VirtualToPhysicalMapping {
    public int physicalAddress;
    public int diskPage;
    public boolean isDirty;

    /**
     * the constructor of VirtualToPhysicalMapping class
     * both physicalAddress and diskPage will set to -1
     * the isDirty flag set to false
     */
    public VirtualToPhysicalMapping() {
        this.physicalAddress = -1;
        this.diskPage = -1;
        this.isDirty = false;
    }
}
