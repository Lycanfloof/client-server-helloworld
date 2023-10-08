import java.io.IOException;

public class ListPortsCommand extends Command{
    @Override
    protected void executeProcess(String[] args) throws IOException {
        String[] command = CommonLibrary.appendAtStart("nmap", args);
        setOutput("\n\n" + CommonLibrary.executeCommand(command));
    }
}
