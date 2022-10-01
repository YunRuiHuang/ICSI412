///**
// * @author Yunrui Huang
// */
//public class GoodbyeWorldProcess extends UserlandProcess {
//    private int sleepTime = 0;
//
//
//    /**
//     * get the time that process need to sleep
//     * @return
//     * the time process need to sleep
//     */
//    @Override
//    public int GetSleepTime() {
//        return this.sleepTime;
//    }
//
//    /**
//     * set the time that process need to sleep
//     * @param milliseconds
//     * the time that process ready to sleep
//     */
//    @Override
//    public void SetSleepTime(int milliseconds) {
//        this.sleepTime = milliseconds;
//    }
//
//    /**
//     * rewrite the Run method from UserlandProcess and print the message
//     * call the sleep method to sleep 300ms (right now all process run 100ms)
//     * @return
//     * return an empty RunResult
//     */
//    public RunResult run(){
//        System.out.println("Goodbye World");
//        RunResult runResult = new RunResult();
//        runResult.millisecondsUsed = 100;
//        runResult.ranToTimeout = false;
//        OS.getOs().Sleep(300);
//        return runResult;
//    }
//}
