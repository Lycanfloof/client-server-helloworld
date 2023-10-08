import AppInterface.ReceiverPrx;

import java.util.concurrent.ConcurrentMap;

public class BroadCastCommand extends Command {
    ConcurrentMap<String, ReceiverPrx> proxyMap;

    public BroadCastCommand(ConcurrentMap<String, ReceiverPrx> proxyMap) {
        this.proxyMap = proxyMap;
    }

    @Override
    protected void executeProcess(ReceiverPrx clientProxy, String username, String args) {
        if (args.isEmpty()) {
            setOutput("The message couldn't be sent because it is empty.");
            return;
        }

        proxyMap.keySet().forEach((String receiver) -> {
            if (!username.equals(receiver)) {
                proxyMap.get(receiver).printString(args);
            }
        });

        setOutput("The message was sent to every registered client in the server.");
    }
}