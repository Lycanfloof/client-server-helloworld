import AppInterface.ReceiverPrx;

import java.util.concurrent.ConcurrentMap;

public class BroadCastCommand extends Command {
    ConcurrentMap<String, ReceiverPrx> proxyMap;

    public BroadCastCommand(ConcurrentMap<String, ReceiverPrx> proxyMap) {
        this.proxyMap = proxyMap;
    }

    @Override
    protected void executeProcess(ReceiverPrx clientProxy, String username, String args) {
        String message = args.trim();
        if (message.isEmpty()) {
            setOutput("The message couldn't be sent because is empty.");
            return;
        }

        proxyMap.values().forEach((ReceiverPrx proxy) -> {
            if (clientProxy != proxy) {
                proxy.printString(message);
            }
        });

        setOutput("Message sent to every client registered in the server.");
    }
}
