import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;

/**
 * @author Yunrui Huang
 */
public class Realtime extends UserlandProcess {
    private int abusingTimes = 0;
    private int[] fileId = new int[0];
    private int state = 0;

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
            int[] id = new int[3];

            id[0] = OS.getOs().Open("random 100");
            System.out.println("\t\tOpen random device (100), Id : " + id[0]);
            id[1] = OS.getOs().Open("file data.bat");
            System.out.println("\t\tOpen file device (data.bat), Id : " + id[1]);
            id[2] = OS.getOs().Open("pipe joe");
            System.out.println("\t\tOpen pipe device (joe), Id : " + id[2]);

            String testInput = "Realtime input";
            //test the pipe write
            OS.getOs().Write(id[2], testInput.getBytes(StandardCharsets.UTF_8));

            //test the file write
            OS.getOs().Write(id[1], testInput.getBytes(StandardCharsets.UTF_8));

            //test the file seek (skip two byte, should across the "He")
            OS.getOs().Seek(id[1],2);

            this.fileId = id;
            this.state = 1;
            OS.getOs().Sleep(300);
            System.out.println("\t\tReal Time process sleep 300ms");

            RunResult runResult = new RunResult();
            runResult.millisecondsUsed = 100;
            runResult.ranToTimeout = false;
            runResult.fileID = new int[0];
            return runResult;
        }else{
            System.out.println("Real Time process running");

            //test the random read
            String hex = DatatypeConverter.printHexBinary(OS.getOs().Read(this.fileId[0],10));
            System.out.println("\t\tread from random: " + hex);

            //test the file read
            System.out.println("\t\tread from file: " + new String(OS.getOs().Read(this.fileId[1],10),StandardCharsets.UTF_8));

            //test the pipe read
            System.out.println("\t\tread from pipe: " + new String(OS.getOs().Read(this.fileId[2],100),StandardCharsets.UTF_8));

            this.state=0;

            OS.getOs().Sleep(300);
            System.out.println("\t\tReal Time process sleep 300ms");

            RunResult runResult = new RunResult();
            runResult.millisecondsUsed = 100;
            runResult.ranToTimeout = false;
            //close all the device id
            runResult.fileID = this.fileId;
            return runResult;
        }


    }
}
