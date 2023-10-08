import AppInterface.ReceiverPrx;

import java.util.concurrent.ConcurrentMap;

public class RegisterCommand extends Command {
    ConcurrentMap<String, ReceiverPrx> proxyMap;

    public RegisterCommand(ConcurrentMap<String, ReceiverPrx> proxyMap) {
        this.proxyMap = proxyMap;
    }

    @Override
    protected void executeProcess(ReceiverPrx clientProxy, String username, String args) {
        if (proxyMap.get(username) == null) {
            proxyMap.put(username, clientProxy);
            setOutput("You have been registered successfully.");
        } else {
            setOutput("That name has already been chosen.");
        }
    }
}
