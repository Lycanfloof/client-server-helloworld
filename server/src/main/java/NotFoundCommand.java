import AppInterface.ReceiverPrx;

public class NotFoundCommand extends Command {
    @Override
    protected void executeProcess(ReceiverPrx clientProxy, String username, String args) {
        setOutput("The given command does not exist.");
    }
}
