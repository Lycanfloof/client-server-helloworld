import AppInterface.ReceiverPrx;

import java.io.IOException;

public class ExecuteSystemCommand extends Command {
    @Override
    public void executeProcess(ReceiverPrx clientProxy, String username, String args) throws IOException {
        setOutput("\n\n" + CommonLibrary.executeCommand(args.split(" ")));
    }
}
