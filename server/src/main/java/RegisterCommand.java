import AppInterface.ReceiverPrx;

import java.util.concurrent.ConcurrentMap;

public class RegisterCommand extends Command {
    ConcurrentMap<String, ReceiverPrx> proxyMap;

    public RegisterCommand(ConcurrentMap<String, ReceiverPrx> proxyMap) {
        this.proxyMap = proxyMap;
    }

    @Override
    protected void executeProcess(ReceiverPrx clientProxy, String username, String hostname, String args) {
        String key = username + "-" + hostname;
        if (proxyMap.get(key) != null) {
            setOutput("Registration wasn't possible because the name has already been picked.");
            return;
        }

        proxyMap.put(key, clientProxy);
        setOutput("You have been registered successfully.");
    }
}
