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
        System.out.println("Interactive process running, timeout count: " + this.abusingTimes);
        RunResult runResult = new RunResult();
        runResult.millisecondsUsed = 100;
        runResult.ranToTimeout = false;
        if(abusingTimes < 6){
            runResult.ranToTimeout = true;
        }
        return runResult;
    }
}
