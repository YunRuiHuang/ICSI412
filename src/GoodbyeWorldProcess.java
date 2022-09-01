/**
 * @author Yunrui Huang
 */
public class GoodbyeWorldProcess extends UserlandProcess {

    /**
     * rewrite the Run method from UserlandProcess and print the message
     * @return
     * return an empty RunResult
     */
    public RunResult run(){
        System.out.println("Goodbye World");
        RunResult runResult = new RunResult();
        runResult.millisecondsUsed = 100;
        runResult.ranToTimeout = false;
        return runResult;
    }
}
