import AppInterface.RequestHandlerPrx;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Util;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.InetAddress;
import java.text.DecimalFormat;

public class Client
{
    public static void main(String[] args)
    {
        try
        {    
            clientInit(args);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void clientInit(String[] args) throws IOException {

        try(Communicator communicator = Util.initialize(args, "config.client"))
        {
            String username = System.getProperty("user.name");
            String hostname = InetAddress.getLocalHost().getHostName();
            RequestHandlerPrx serverProxy = RequestHandlerPrx.checkedCast(
                communicator.propertyToProxy("Server.Proxy")).ice_twoway().ice_secure(false);

            if (serverProxy == null)
            {
                throw new Error("Invalid Proxy: Property might not exist in the configuration file.");
            }

            startRequestLoop(serverProxy, username, hostname);
        }
    }

    private static void startRequestLoop(RequestHandlerPrx serverProxy, String username, String hostname) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in)))
        {
            System.out.println("Enter a message. You can type 'exit' to close the connection to the server.");
            String message;

            while (!(message = br.readLine()).equals("exit")) {
                long start = System.nanoTime();
                String response = serverProxy.handleRequest(username + " : " + hostname + " : " + message);
                long end = System.nanoTime();

                response += "\nClient latency: " + new DecimalFormat("#.##").format((double)(end - start) * 1e-6) + " ms.\n";

                System.out.println(response);
            }
        }
    }
}
