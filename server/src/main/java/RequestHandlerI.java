import AppInterface.ReceiverPrx;
import AppInterface.RequestHandler;
import com.zeroc.Ice.Current;
import java.text.DecimalFormat;

public class RequestHandlerI implements RequestHandler
{
    private long receivedRequests = 0;
    private long unprocessedRequests = 0;
    private long processedTime = 0;

    @Override
    public String handleRequest(ReceiverPrx clientProxy, String s, Current current)
    {
        receivedRequests++;
        Request request = new Request(s);
        request.start();

        if (request.isErroneous()) {
            unprocessedRequests++;
        }

        String performanceReport = buildPerformanceReport(request.requestTime());
        String serverResponse = buildServerResponse(request) + performanceReport;
        String clientResponse = buildClientResponse(request) + performanceReport;

        System.out.println(serverResponse);

        return clientResponse;
    }

    private String buildServerResponse(Request request) {
        return request.getPrefix() + request.getOutput();
    }

    private String buildClientResponse(Request request) {
        return "\nResponse:" + request.getOutput();
    }

    private String buildPerformanceReport(long latency) {
        String perfomanceReport = "";

        processedTime += latency;
        double serverThroughput = (double) (receivedRequests - unprocessedRequests) / (processedTime);
        double failureRate = (double) unprocessedRequests / receivedRequests;
        double successRate = (double) 1 - failureRate;

        perfomanceReport += "\n\nServer latency: " + new DecimalFormat("#.##").format(latency * 1e-6) + " ms.\n";
        perfomanceReport += "Server throughput: " + new DecimalFormat("#.##").format(serverThroughput * 1e9) + " requests per second.\n";
        perfomanceReport += "Server success rate: " + new DecimalFormat("#.##").format(successRate * 100) + "%\n";
        perfomanceReport += "Server failure (unprocessed) rate: " + new DecimalFormat("#.##").format(failureRate * 100) + "%\n";

        return perfomanceReport;
    }
}
