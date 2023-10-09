import AppInterface.ReceiverPrx;
import AppInterface.RequestHandlerPrx;
import com.zeroc.Ice.*;
import com.zeroc.Ice.Object;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.Exception;
import java.net.InetAddress;
import java.text.DecimalFormat;

public class Client {
    private static RequestHandlerPrx serverProxy;
    private static ReceiverPrx clientProxy;
    private static String username;
    private static String hostname;
    private static boolean timedOut;

    public static void main(String[] args) {
        try {
            clientInit(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void clientInit(String[] args) throws IOException, InterruptedException {
        try(Communicator communicator = Util.initialize(args, "config.client"))
        {
            serverProxy = createServerProxy(communicator);
            clientProxy = createClientProxy(communicator);
            username = System.getProperty("user.name");
            hostname = InetAddress.getLocalHost().getHostName();

            if (args.length >= 2 && args[0].equals("rff")) {
                long quantity;
                try { quantity = Long.parseLong(args[2]); } catch (NumberFormatException e) { quantity = 1; }
                startRequestFromFile(args[1], quantity);
                if (timedOut) {
                    System.out.println("\nOne of the requests timed out\n.");
                }
            } else {
                startRequestLoop();
            }
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
                adapter.createProxy(Util.stringToIdentity("Receiver")).ice_twoway().ice_secure(false));
    }

    private static void startRequestFromFile(String filePath, long quantity) throws IOException, InterruptedException {
        Thread previousThread = null;

        for (int i = 0; i < quantity; i++) {
            previousThread = sendFromFile(previousThread, filePath);
        }

        if (previousThread != null)
            previousThread.join();
    }

    private static Thread sendFromFile(Thread previousThread, String filePath) throws IOException, InterruptedException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath)))
        {
            Thread previous = previousThread;

            String line;
            while ((line = reader.readLine()) != null) {
                Runnable runnable = createDependentRunnable(previous, line);
                Thread thread = new Thread(runnable);
                thread.start();

                previous = thread;
            }

            return previous;
        }
    }

    private static Runnable createDependentRunnable(Thread previousThread, String readLine) {
        Runnable runnable;

        if (previousThread == null)
            runnable = () -> sendRequest(readLine);
        else {
            runnable = () ->
            {
                sendRequest(readLine);
                try { previousThread.join(); } catch (InterruptedException e) { throw new RuntimeException(e); }
            };
        }

        return runnable;
    }

    private static void startRequestLoop() throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in)))
        {
            System.out.println("Enter a message. You can type 'exit' to close the connection to the server.");
            String message;

            while (!(message = br.readLine()).equalsIgnoreCase("exit")) {
                final String writtenMessage = message;
                Runnable runnable = () -> sendRequest(writtenMessage);
                Thread thread = new Thread(runnable);
                thread.start();
            }
        }
    }

    private static void sendRequest(String message) {
        long start = System.nanoTime();

        String response;
        try {
            response = serverProxy.handleRequest(clientProxy, username + " : " + hostname + " : " + message);
        } catch (InvocationTimeoutException e) {
            timedOut = true;
            response = "The request timed out. ";
        }

        long end = System.nanoTime();

        response += getClientLatency(start, end);
        System.out.println(response);
    }

    private static String getClientLatency(long start, long end) {
        return "Client latency: " + new DecimalFormat("#.##").format((double)(end - start) * 1e-6) + " ms.\n";
    }
}
