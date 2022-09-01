/**
 * @author Yunrui Huang
 */
public class HelloWorldProcess extends UserlandProcess {

    /**
     * rewrite the Run method from UserlandProcess and print the message
     * @return
     * return an empty RunResult
     */
    public RunResult run(){
        System.out.println("Hello World");
        return new RunResult();
    }
}
