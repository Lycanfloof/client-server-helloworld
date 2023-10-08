import AppInterface.ReceiverPrx;

import java.util.concurrent.ConcurrentMap;

public class SendMessageCommand extends Command {
    ConcurrentMap<String, ReceiverPrx> proxyMap;

    public SendMessageCommand(ConcurrentMap<String, ReceiverPrx> proxyMap) {
        this.proxyMap = proxyMap;
    }

    @Override
    protected void executeProcess(ReceiverPrx clientProxy, String username, String args) {
        String[] command = args.split(":");
        if (command.length < 2) {
            setOutput("Command formatted incorrectly.");
            return;
        }

        String addressee = command[0];
        if (username.equals(addressee)) {
            setOutput("You cannot send messages to yourself. Why would you do that?");
            return;
        }

        ReceiverPrx remoteProxy = proxyMap.get(addressee);
        if (remoteProxy != null) {
            String message = command[1].trim();
            remoteProxy.printString(message);
            setOutput("Message sent successfully.");
        } else {
            setOutput("The name isn't registered in the server.");
        }
    }
}
