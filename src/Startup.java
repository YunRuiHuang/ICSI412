/**
 * @author Yunrui Huang
 */
public class Startup {
    /**
     * the main method of program
     * @param args
     */
    public static void main(String[] args){
        BasicScheduler scheduler = new BasicScheduler();
        int testremove = scheduler.CreateProcess(new HelloWorldProcess()); //add the helloworld process to list
        scheduler.DeleteProcess(testremove); //remove the helloworld process from list
        scheduler.CreateProcess(new GoodbyeWorldProcess()); //add the goodbye process to list
        scheduler.CreateProcess(new HelloWorldProcess()); // add the helloworld process back to list
        scheduler.run();

    }
}
