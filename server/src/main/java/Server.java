import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Object;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

public class Server
{
    public static void main(String[] args)
    {
        ServerInit(args);
    }

    private static void ServerInit(String[] args) {
        try(Communicator communicator = Util.initialize(args, "config.server"))
        {
            ObjectAdapter adapter = communicator.createObjectAdapter("Server");
            Object object = new RequestHandlerI();

            adapter.add(object, Util.stringToIdentity("RequestHandler"));
            adapter.activate();
            
            communicator.waitForShutdown();
        }
    }
}
