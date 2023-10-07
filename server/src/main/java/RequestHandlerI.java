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
        Request request = new Request(s);
        receivedRequests++;

        request.start();

        if (request.isErroneous()) {
            unprocessedRequests++;
        }

        return printAndGetResponse(request);
    }

    private String printAndGetResponse(Request request) {
        String performanceReport = getPerformanceReport(request.requestTime());

        String serverOutput = request.getPrefix() + request.getOutput() + performanceReport;
        String clientOutput = "\nResponse:" + request.getOutput() + performanceReport;

        System.out.println(serverOutput);

        return clientOutput;
    }

    private String getPerformanceReport(long latency) {
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
