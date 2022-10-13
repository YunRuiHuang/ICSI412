public abstract class UserlandProcess {
    public abstract int GetAbusingCount();
    public abstract void SetAbusingCount(int times);
    public abstract int GetSleepTime();
    public abstract void SetSleepTime(int milliseconds);
    public abstract RunResult run();
}

