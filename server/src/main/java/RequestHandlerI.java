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
    private boolean measuringPerformance;
    private long receivedRequests;
    private long unprocessedRequests;

    public RequestHandlerI(ConcurrentMap<String, Command> commandMap,
                           Command notFoundCommand,
                           ConcurrentMap<String, ReceiverPrx> proxyMap) {
        this.commandMap = commandMap;
        this.notFoundCommand = notFoundCommand;
        this.proxyMap = proxyMap;
        this.measuringPerformance = false;
        this.receivedRequests = 0;
        this.unprocessedRequests = 0;
    }

    @Override
    public String handleRequest(ReceiverPrx clientProxy, String s, Current current)
    {
        if (isMeasuringPerformance())
            receivedRequests++;

        Request request = new Request(s);
        Command command = commandMap.getOrDefault(request.getCommand(), notFoundCommand);
        command.execute(clientProxy, request.getUsername(), request.getHostname(), request.getArgs());

        if (command.isErroneous() && isMeasuringPerformance()) {
            unprocessedRequests++;
        }

        String latencyReport = buildLatencyReport(command.getExecutionTime());
        String response = command.getOutput() + latencyReport;

        String serverResponse = request.getPrefix() + response;
        String clientResponse = "\nResponse: " + response;
        System.out.println(serverResponse);

        return clientResponse;
    }

    private String buildLatencyReport(long executionTime) {
        return "\n\nServer latency: " + new DecimalFormat("#.##").format(executionTime * 1e-6) + " ms.\n";
    }

    public ConcurrentMap<String, Command> getCommandMap() {
        return commandMap;
    }

    public ConcurrentMap<String, ReceiverPrx> getProxyMap() {
        return proxyMap;
    }

    public boolean isMeasuringPerformance() {
        return measuringPerformance;
    }

    public void setMeasuringPerformance(boolean measuringPerformance) {
        this.measuringPerformance = measuringPerformance;
    }

    public long getReceivedRequests() {
        return receivedRequests;
    }

    public void resetReceivedRequests() {
        this.receivedRequests = 0;
    }

    public long getUnprocessedRequests() {
        return unprocessedRequests;
    }

    public void resetUnprocessedRequests() {
        this.unprocessedRequests = 0;
    }
}
