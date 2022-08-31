public class HelloWorldProcess extends UserlandProcess {
    public RunResult run(){
        System.out.println("Hello World");
        return new RunResult();
    }
}
