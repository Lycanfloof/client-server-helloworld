import AppInterface.ReceiverPrx;

import java.util.concurrent.ConcurrentMap;

public class SendMessageCommand extends Command {
    ConcurrentMap<String, ReceiverPrx> proxyMap;

    public SendMessageCommand(ConcurrentMap<String, ReceiverPrx> proxyMap) {
        this.proxyMap = proxyMap;
    }

    @Override
    protected void executeProcess(ReceiverPrx clientProxy, String username, String hostname, String args) {
        String[] command = args.split(":");
        if (command.length < 2) {
            setOutput("The message couldn't be sent due to a formatting error on your part.");
            return;
        }

        String receiver = command[0];
        if (username.equals(receiver)) {
            setOutput("You cannot send messages to yourself (ha ha, you're lonely).");
            return;
        }

        ReceiverPrx receiverPrx = proxyMap.get(receiver);
        if (receiverPrx == null) {
            setOutput("The message couldn't be sent because the receiver isn't registered.");
            return;
        }

        String message = command[1].trim();
        receiverPrx.printString(message);
        setOutput("Message sent successfully.");
    }
}
