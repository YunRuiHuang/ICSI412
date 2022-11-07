/**
 * @author Yunrui Huang
 */
public class Startup {
    /**
     * the main method of program
     * @param args
     */
    public static void main(String[] args){
//        BasicScheduler scheduler = new BasicScheduler();
//        int testremove = scheduler.CreateProcess(new HelloWorldProcess()); //add the helloworld process to list
//        scheduler.DeleteProcess(testremove); //remove the helloworld process from list
//        scheduler.CreateProcess(new GoodbyeWorldProcess()); //add the goodbye process to list
//        scheduler.CreateProcess(new HelloWorldProcess()); // add the helloworld process back to list
//        scheduler.run();

        int testremove = OS.getOs().CreateProcess(new Realtime(), PriorityEnum.RealTime); //add the realtime process to list and get the PID of process
        OS.getOs().DeleteProcess(testremove); //remove the realtime process

        //real time process will test the sleep method
        //to test the pipe between different process, the realtime will creat the "joe" pipe first and write in "realtime input", interactive will attach "joe" pipe, read the "realtime input" and write in "interactive input" and close it pipe, after realtime wake, realtime process can read "interactive input" from "joe" pipe
        OS.getOs().CreateProcess(new Realtime(), PriorityEnum.RealTime); //add the real time process to realtime list
        //interactive process will test the timeout and downgrade priority, if the counter add to 6 means already down to background level
        OS.getOs().CreateProcess(new Interactive(), PriorityEnum.Interactive); // add the interactive process back to interactive list
        OS.getOs().CreateProcess(new Background(), PriorityEnum.Background); // add the background process back to background list
        OS.getOs().CreateProcess(new TestDevices(), PriorityEnum.RealTime); // add the TestDevices process back to background list
        OS.getOs().run();

    }
}
