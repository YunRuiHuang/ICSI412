import java.nio.charset.StandardCharsets;

/**
 * @author Yunrui Huang
 */
public class Interactive extends UserlandProcess{
    private int abusingTimes = 0;


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
    public RunResult run(){
        System.out.println("Interactive process running");
        System.out.println("\t\ttimeout count : " + this.abusingTimes);

        int[] id = new int[1];
        id[0] = OS.getOs().Open("pipe joe");
        System.out.println("\t\tOpen pipe device (joe), Id : " + id[0]);
        //test the pipe read
        System.out.println("\t\tread from pipe: " + new String(OS.getOs().Read(id[0],100), StandardCharsets.UTF_8));
        //test the pipe write
        OS.getOs().Write(id[0], "interactive input".getBytes(StandardCharsets.UTF_8));

        RunResult runResult = new RunResult();
        runResult.millisecondsUsed = 100;
        runResult.ranToTimeout = false;
        runResult.fileID = id;
        if(abusingTimes < 6){
            runResult.ranToTimeout = true;
        }
        return runResult;
    }
}
