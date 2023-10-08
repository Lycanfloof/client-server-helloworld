import java.io.IOException;

public class ExecuteSystemCommand extends Command {
    @Override
    public void executeProcess(String[] args) throws IOException {
        setOutput("\n\n" + CommonLibrary.executeCommand(args));
    }
}
