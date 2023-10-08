import AppInterface.ReceiverPrx;

import java.util.concurrent.ConcurrentMap;

public class RegisterCommand extends Command {
    ConcurrentMap<String, ReceiverPrx> proxyMap;

    public RegisterCommand(ConcurrentMap<String, ReceiverPrx> proxyMap) {
        this.proxyMap = proxyMap;
    }

    @Override
    protected void executeProcess(ReceiverPrx clientProxy, String username, String args) {
        if (proxyMap.get(username) != null) {
            setOutput("Registration wasn't possible because the name has already been picked.");
            return;
        }

        proxyMap.put(username, clientProxy);
        setOutput("You have been registered successfully.");
    }
}
