import AppInterface.ReceiverPrx;

import java.io.IOException;

public class ListPortsCommand extends Command {
    @Override
    protected void executeProcess(ReceiverPrx clientProxy, String username, String args) throws IOException {
        String[] command = {"nmap", args};
        setOutput("\n\n" + CommonLibrary.executeCommand(command));
    }
}
