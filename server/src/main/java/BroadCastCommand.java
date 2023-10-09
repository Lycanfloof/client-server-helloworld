import AppInterface.ReceiverPrx;

import java.util.concurrent.ConcurrentMap;

public class BroadCastCommand extends Command {
    ConcurrentMap<String, ReceiverPrx> proxyMap;

    public BroadCastCommand(ConcurrentMap<String, ReceiverPrx> proxyMap) {
        this.proxyMap = proxyMap;
    }

    @Override
    protected void executeProcess(ReceiverPrx clientProxy, String username, String hostname, String args) {
        if (args.isEmpty()) {
            setOutput("The message couldn't be sent because it is empty.");
            return;
        }

        proxyMap.keySet().forEach((String receiver) -> {
            if (!(username + "-" + hostname).equals(receiver)) {
                proxyMap.get(receiver).printString((username + "-" + hostname) + ": " + args);
            }
        });

        setOutput("The message was sent to every registered client in the server.");
    }
}
