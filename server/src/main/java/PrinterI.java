public class PrinterI implements Demo.Printer
{
    private long receivedRequests = 0;
    private long unprocessedRequests = 0;
    private long processedTime = 0;

    public String printString(String s, com.zeroc.Ice.Current current)
    {
        Request request = new Request(s);
        receivedRequests++;

        String output;
        long start = System.nanoTime();

        try {
            output = request.executeRequest();
        } catch (Exception e) {
            output = e.getMessage();
            unprocessedRequests++;
        }

        long end = System.nanoTime();

        String perfomance_report = getPerfomanceReport(start, end);
        String serverOutput = request.getPrefix() + output + perfomance_report;
        String clientOutput = "\nResponse:" + output + perfomance_report;

        System.out.println(serverOutput);
        return clientOutput;
    }

    private String getPerfomanceReport(long start, long end) {
        String perfomanceReport = "";

        double latency = (double) (end - start);
        processedTime += latency;

        double serverThroughput = (double) (receivedRequests - unprocessedRequests) / (processedTime);
        double failureRate = (double) unprocessedRequests / receivedRequests;
        double successRate = (double) 1 - failureRate;

        perfomanceReport += "\n\nServer latency: " + new java.text.DecimalFormat("#.##").format(latency * 1e-6) + " ms.\n";
        perfomanceReport += "Server throughput: " + new java.text.DecimalFormat("#.##").format(serverThroughput * 1e9) + " requests per second.\n";
        perfomanceReport += "Server success rate: " + new java.text.DecimalFormat("#.##").format(successRate * 100) + "%\n";
        perfomanceReport += "Server failure (unprocessed) rate: " + new java.text.DecimalFormat("#.##").format(failureRate * 100) + "%\n";

        return perfomanceReport;
    }
}
