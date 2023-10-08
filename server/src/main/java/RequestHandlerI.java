import AppInterface.ReceiverPrx;
import AppInterface.RequestHandler;
import com.zeroc.Ice.Current;
import java.text.DecimalFormat;
import java.util.concurrent.ConcurrentMap;

public class RequestHandlerI implements RequestHandler
{
    private final ConcurrentMap<String, Command> commandMap;
    private final Command notFoundCommand;
    private final ConcurrentMap<String, ReceiverPrx> proxyMap;
    private long receivedRequests;
    private long unprocessedRequests;

    public RequestHandlerI(ConcurrentMap<String, Command> commandMap,
                           Command notFoundCommand,
                           ConcurrentMap<String, ReceiverPrx> proxyMap) {
        this.commandMap = commandMap;
        this.notFoundCommand = notFoundCommand;
        this.proxyMap = proxyMap;
        this.receivedRequests = 0;
        this.unprocessedRequests = 0;
    }

    public ConcurrentMap<String, Command> getCommandMap() {
        return commandMap;
    }

    public ConcurrentMap<String, ReceiverPrx> getProxyMap() {
        return proxyMap;
    }

    @Override
    public String handleRequest(ReceiverPrx clientProxy, String s, Current current)
    {
        receivedRequests++;
        Request request = new Request(s);
        Command command = commandMap.getOrDefault(request.getCommand(), notFoundCommand);
        command.execute(clientProxy, request.getUsername(), request.getArgs());

        if (command.isErroneous()) {
            unprocessedRequests++;
        }

        String performanceReport = buildPerformanceReport(command.getExecutionTime());
        String response = command.getOutput() + performanceReport;
        String serverResponse = request.getPrefix() + response;
        String clientResponse = "\nResponse: " + response;

        System.out.println(serverResponse);

        return clientResponse;
    }

    private String buildPerformanceReport(long executionTime) {
        String performanceReport = "";

        double failureRate = (double) unprocessedRequests / receivedRequests;
        double successRate = (double) 1 - failureRate;

        performanceReport += "\n\nServer latency: " + new DecimalFormat("#.##").format(executionTime * 1e-6) + " ms.\n";
        performanceReport += "Server success rate: " + new DecimalFormat("#.##").format(successRate * 100) + "%\n";
        performanceReport += "Server failure (unprocessed) rate: " + new DecimalFormat("#.##").format(failureRate * 100) + "%\n";

        return performanceReport;
    }
}
