import AppInterface.ReceiverPrx;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Server
{
    public static void main(String[] args)
    {
        serverInit(args);
    }

    private static void serverInit(String[] args) {
        try(Communicator communicator = Util.initialize(args, "config.server"))
        {
            ObjectAdapter adapter = communicator.createObjectAdapter("Server");
            RequestHandlerI object = new RequestHandlerI(new ConcurrentHashMap<>(),
                    new NotFoundCommand(),
                    new ConcurrentHashMap<>());
            commandMapInit(object);

            adapter.add(object, Util.stringToIdentity("RequestHandler"));
            adapter.activate();
            
            communicator.waitForShutdown();
        }
    }

    private static void commandMapInit(RequestHandlerI requestHandler) {
        ConcurrentMap<String, Command> commandMap = requestHandler.getCommandMap();
        ConcurrentMap<String, ReceiverPrx> proxyMap = requestHandler.getProxyMap();

        commandMap.put("register", new RegisterCommand(proxyMap));
        commandMap.put("list-clients", new ListClientsCommand(proxyMap));
        commandMap.put("to", new SendMessageCommand(proxyMap));
        commandMap.put("broadcast", new BroadCastCommand(proxyMap));
        commandMap.put("list-ifs", new ListInterfacesCommand());
        commandMap.put("list-ports", new ListPortsCommand());
        commandMap.put("execute", new ExecuteSystemCommand());
        commandMap.put("prime-factors", new PrimeFactorsCommand());
    }
}
