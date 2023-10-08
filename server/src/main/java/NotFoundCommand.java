public class NotFoundCommand extends Command{
    @Override
    protected void executeProcess(String[] args) {
        setOutput("The given command doesn't exist.");
    }
}
