import AppInterface.ReceiverPrx;

import java.io.IOException;

public class ListInterfacesCommand extends Command{
    @Override
    protected void executeProcess(ReceiverPrx clientProxy, String username, String args) throws IOException {
        String[] command = {"ifconfig"};
        setOutput("\n\n" + CommonLibrary.executeCommand(command));
    }
}
