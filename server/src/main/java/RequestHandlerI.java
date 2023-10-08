import AppInterface.ReceiverPrx;
import AppInterface.RequestHandler;
import com.zeroc.Ice.Current;
import java.text.DecimalFormat;
import java.util.concurrent.ConcurrentMap;

public class RequestHandlerI implements RequestHandler
{
    private final ConcurrentMap<String, Command> commandMap;
    private final Command notFoundCommand;
    private ConcurrentMap<String, ReceiverPrx> proxyMap;
    private long receivedRequests;
    private long unprocessedRequests;
    private long processedTime;

    public RequestHandlerI(ConcurrentMap<String, ReceiverPrx> proxyMap,
                           ConcurrentMap<String, Command> commandMap,
                           Command notFoundCommand) {
        this.proxyMap = proxyMap;
        this.commandMap = commandMap;
        this.notFoundCommand = notFoundCommand;
        this.receivedRequests = 0;
        this.unprocessedRequests = 0;
        this.processedTime = 0;
    }

    @Override
    public String handleRequest(ReceiverPrx clientProxy, String s, Current current)
    {
        receivedRequests++;
        Request request = new Request(s);
        Command command = commandMap.getOrDefault(request.getCommand(), notFoundCommand);
        command.execute(request.getArgs());

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

        processedTime += executionTime;
        double serverThroughput = (double) (receivedRequests - unprocessedRequests) / (processedTime);
        double failureRate = (double) unprocessedRequests / receivedRequests;
        double successRate = (double) 1 - failureRate;

        performanceReport += "\n\nServer latency: " + new DecimalFormat("#.##").format(executionTime * 1e-6) + " ms.\n";
        performanceReport += "Server throughput: " + new DecimalFormat("#.##").format(serverThroughput * 1e9) + " requests per second.\n";
        performanceReport += "Server success rate: " + new DecimalFormat("#.##").format(successRate * 100) + "%\n";
        performanceReport += "Server failure (unprocessed) rate: " + new DecimalFormat("#.##").format(failureRate * 100) + "%\n";

        return performanceReport;
    }
}
