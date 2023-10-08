public abstract class Command {
    private long startTime;
    private long endTime;
    private String output;
    private boolean erroneous;

    public Command() {
        this.startTime = 0;
        this.endTime = 0;
        this.erroneous = false;
    }

    public void execute(String[] args) {
        try {
            startTime = System.nanoTime();
            executeProcess(args);
            endTime = System.nanoTime();
        } catch (Exception e) {
            setErroneous();
            setOutput("An error has occurred while processing the request.");
        }
    }

    protected abstract void executeProcess(String[] args) throws Exception;

    public long getExecutionTime() {
        return endTime - startTime;
    }

    public String getOutput() {
        return output;
    }

    public boolean isErroneous() {
        return erroneous;
    }

    protected void setOutput(String output) {
        this.output = output;
    }

    protected void setErroneous() {
        this.erroneous = true;
    }
}
