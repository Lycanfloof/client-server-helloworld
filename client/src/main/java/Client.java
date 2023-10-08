import AppInterface.ReceiverPrx;
import AppInterface.RequestHandlerPrx;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Object;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.InetAddress;
import java.text.DecimalFormat;

public class Client {
    public static void main(String[] args) {
        try {
            clientInit(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void clientInit(String[] args) throws IOException {
        try(Communicator communicator = Util.initialize(args, "config.client"))
        {
            RequestHandlerPrx serverProxy = createServerProxy(communicator);
            ReceiverPrx clientProxy = createClientProxy(communicator);
            String username = System.getProperty("user.name");
            String hostname = InetAddress.getLocalHost().getHostName();

            startRequestLoop(serverProxy, clientProxy, username, hostname);
            communicator.waitForShutdown();
        }
    }

    private static RequestHandlerPrx createServerProxy(Communicator communicator) {
        RequestHandlerPrx serverProxy = RequestHandlerPrx.checkedCast(
                communicator.propertyToProxy("Server.Proxy")).ice_twoway().ice_secure(false);

        if (serverProxy == null)
            throw new Error("Invalid Proxy: Property might not exist in the configuration file.");

        return serverProxy;
    }

    private static ReceiverPrx createClientProxy(Communicator communicator) {
        ObjectAdapter adapter = communicator.createObjectAdapter("Client");
        Object object = new ReceiverI();

        adapter.add(object, Util.stringToIdentity("Receiver"));
        adapter.activate();

        return ReceiverPrx.checkedCast(
                adapter.createProxy(Util.stringToIdentity("Receiver")).ice_twoway().ice_secure(false)).ice_timeout(5000);
    }

    private static void startRequestLoop(RequestHandlerPrx serverProxy, ReceiverPrx clientProxy, String username, String hostname) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in)))
        {
            System.out.println("Enter a message. You can type 'exit' to close the connection to the server.");
            String message;

            while (!(message = br.readLine()).equalsIgnoreCase("exit")) {
                final String writtenMessage = message;

                Runnable runnable = () -> {
                    long start = System.nanoTime();
                    String response = serverProxy.handleRequest(clientProxy, username + " : " + hostname + " : " + writtenMessage);
                    long end = System.nanoTime();

                    response += getClientLatency(start, end);
                    System.out.println(response);
                };

                Thread thread = new Thread(runnable);
                thread.start();
            }
        }
    }

    private static String getClientLatency(long start, long end) {
        return "\nClient latency: " + new DecimalFormat("#.##").format((double)(end - start) * 1e-6) + " ms.\n";
    }
}
