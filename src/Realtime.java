import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;

/**
 * @author Yunrui Huang
 */
public class Realtime extends UserlandProcess {
    private int abusingTimes = 0;
    private int[] fileId = new int[0];
    private int state = 0;
    private int mutexId = 0;
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

        if(state == 0){
            System.out.println("Real Time process running");
            mutexId = OS.getOs().AttachToMutex("Aflag");
            if(OS.getOs().Lock(mutexId)){
                this.state = 1;
                System.out.println("\t\tReal Time process Successful Lock Aflag");
                OS.getOs().Sleep(300);
                System.out.println("\t\tReal Time process sleep 300ms");
            }else{
                this.state = 0;
                System.out.println("\t\tReal Time process fail to Lock Aflag");
            }

            RunResult runResult = new RunResult();
            runResult.millisecondsUsed = 100;
            runResult.ranToTimeout = false;
            runResult.fileID = new int[0];
            return runResult;
        }else{
            System.out.println("Real Time process running");

            OS.getOs().Unlock(mutexId);
            OS.getOs().ReleaseMutex(mutexId);
            System.out.println("\t\tReal Time process release MutexId");
            this.state=0;

            OS.getOs().Sleep(300);
            System.out.println("\t\tReal Time process sleep 300ms");

            RunResult runResult = new RunResult();
            runResult.millisecondsUsed = 100;
            runResult.ranToTimeout = false;
            runResult.fileID = new int[0];
            return runResult;
        }


    }
}
