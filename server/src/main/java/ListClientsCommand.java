import AppInterface.ReceiverPrx;

import java.util.Arrays;
import java.util.concurrent.ConcurrentMap;

public class ListClientsCommand extends Command {
    ConcurrentMap<String, ReceiverPrx> proxyMap;

    public ListClientsCommand(ConcurrentMap<String, ReceiverPrx> proxyMap) {
        this.proxyMap = proxyMap;
    }

    @Override
    protected void executeProcess(ReceiverPrx clientProxy, String username, String hostname, String args) {
        String receiverList = Arrays.toString(proxyMap.keySet().toArray());
        setOutput(receiverList);
    }
}
