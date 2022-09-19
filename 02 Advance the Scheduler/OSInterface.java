public interface OSInterface {
    int CreateProcess(UserlandProcess myNewProcess );

    boolean DeleteProcess(int processId);

    void Sleep(int milliseconds);
    void run();
}

