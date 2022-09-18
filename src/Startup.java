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

        int testremove = OS.getOs().CreateProcess(new GoodbyeWorldProcess()); //add the goodbye process to list and get the PID of process
        OS.getOs().DeleteProcess(testremove); //remove the goodbye process
        OS.getOs().CreateProcess(new GoodbyeWorldProcess()); //add the goodbye process to list
        OS.getOs().CreateProcess(new HelloWorldProcess()); // add the helloworld process back to list
        OS.getOs().run();

    }
}
