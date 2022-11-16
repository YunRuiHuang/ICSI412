/**
 * @author Yunrui Huang
 */
public class TestDevices extends UserlandProcess{
    private int abusingTimes = 0;
    private int[] fileId = new int[0];
    private int state = 0;
    private int memoryAddress = 0;

    /**
     * get the count of ran of timeout
     * @return
     * the count of ran of timeout
     */
    @Override
    public int GetAbusingCount() {
        return this.abusingTimes;
    }

    /**
     * set the count of ran of timeout
     * @param times
     * the count of ran of timeout
     */
    @Override
    public void SetAbusingCount(int times) {
        this.abusingTimes = times;
    }

    private int sleepTime = 0;

    /**
     * get the time that process need to sleep
     * @return
     * the time process need to sleep
     */
    @Override
    public int GetSleepTime() {
        return this.sleepTime;
    }

    /**
     * set the time that process need to sleep
     * @param milliseconds
     * the time that process ready to sleep
     */
    @Override
    public void SetSleepTime(int milliseconds) {
        this.sleepTime = milliseconds;
    }
    /**
     * rewrite the Run method from UserlandProcess and print the message
     * @return
     * return an empty RunResult
     */
    public RunResult run() throws RescheduleException{

        System.out.println("Test Devices Process running");

        OS.getOs().sbrk(1024);
        int address = OS.getOs().sbrk(1024);
        System.out.println("\tget the memory address : " + address);
        OS.getOs().WriteMemory(address,(byte)'T');
        OS.getOs().WriteMemory(address+1,(byte)'e');
        OS.getOs().WriteMemory(address+2,(byte)'s');
        OS.getOs().WriteMemory(address+3,(byte)'t');
        OS.getOs().WriteMemory(address+4,(byte)'1');
        System.out.println("\tread from memory : " + (char)OS.getOs().ReadMemory(address)+ (char)OS.getOs().ReadMemory(address+1)+ (char)OS.getOs().ReadMemory(address+2)+ (char)OS.getOs().ReadMemory(address+3)+ (char)OS.getOs().ReadMemory(address+4) );

        OS.getOs().sbrk(1024);
        OS.getOs().sbrk(1024);
        OS.getOs().sbrk(1024);
        OS.getOs().sbrk(1024);
        this.memoryAddress = OS.getOs().sbrk(1024);
        System.out.println("\tget more memory address : " + this.memoryAddress);

        //write into memory

        System.out.println("\ttry swapping first page to disk");
        byte testInput = (byte)'a';
        OS.getOs().WriteMemory(this.memoryAddress,testInput);

        byte readBack = OS.getOs().ReadMemory(this.memoryAddress);
        System.out.println("\tread from memory : " + (char)readBack);
        System.out.println("\ttry read disk back to memory");
        System.out.println("\tread from memory : " + (char)OS.getOs().ReadMemory(address)+ (char)OS.getOs().ReadMemory(address+1)+ (char)OS.getOs().ReadMemory(address+2)+ (char)OS.getOs().ReadMemory(address+3)+ (char)OS.getOs().ReadMemory(address+4) );

        System.out.println("\ttry outofbounds");
        OS.getOs().ReadMemory(this.memoryAddress+1024);
        //process should be killed here

        RunResult runResult = new RunResult();
        runResult.ranToTimeout = false;
        runResult.millisecondsUsed = 100;
        runResult.fileID = new int[0];
        return runResult;
    }
}
