import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Object;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Server
{
    private static final ConcurrentMap<String, Command> commandMap = new ConcurrentHashMap<>();

    public Server() {
        commandMap.put("listifs", new ListInterfacesCommand());
        commandMap.put("listports", new ListPortsCommand());
        commandMap.put("os-command", new ExecuteSystemCommand());
        commandMap.put("prime-factors", new PrimeFactorsCommand());
    }

    public static void main(String[] args)
    {
        ServerInit(args);
    }

    private static void ServerInit(String[] args) {
        try(Communicator communicator = Util.initialize(args, "config.server"))
        {
            ObjectAdapter adapter = communicator.createObjectAdapter("Server");
            Object object = new RequestHandlerI(new ConcurrentHashMap<>(), commandMap, new NotFoundCommand());

            adapter.add(object, Util.stringToIdentity("RequestHandler"));
            adapter.activate();
            
            communicator.waitForShutdown();
        }
    }
}
