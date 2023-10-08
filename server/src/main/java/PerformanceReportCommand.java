import AppInterface.ReceiverPrx;

import java.text.DecimalFormat;

public class PerformanceReportCommand extends Command {
    RequestHandlerI requestHandler;

    public PerformanceReportCommand(RequestHandlerI requestHandler) {
        this.requestHandler = requestHandler;
    }

    @Override
    protected void executeProcess(ReceiverPrx clientProxy, String username, String args) throws Exception {
        if (requestHandler.isMeasuringPerformance()) {
            setOutput("A performance report has already been requested.");
            return;
        }

        Long measureTime = CommonLibrary.convertToNumber(args);
        if (measureTime == null) {
            setOutput("The given argument isn't valid. It needs to be the number of milliseconds of measure time.");
            return;
        }

        clientProxy.printString("Performance report has been initiated.");
        measurePerformance(measureTime);
        setOutput(buildPerformanceReport(measureTime));

        resetState();
    }

    private void measurePerformance(long measureTime) throws InterruptedException {
        requestHandler.setMeasuringPerformance(true);
        Thread.sleep(measureTime);
        requestHandler.setMeasuringPerformance(false);
    }

    private void resetState() {
        requestHandler.resetReceivedRequests();
        requestHandler.resetUnprocessedRequests();
    }

    private String buildPerformanceReport(long measureTime) {
        String performanceReport = "";

        double throughput = (double) (requestHandler.getReceivedRequests() - requestHandler.getUnprocessedRequests()) / measureTime;
        double failureRate = (double) requestHandler.getUnprocessedRequests() / requestHandler.getReceivedRequests();
        double successRate = (double) 1 - failureRate;

        performanceReport += "\n\nServer throughput: " + new DecimalFormat("#.##").format(throughput * 1e3) + " requests per second.\n";
        performanceReport += "Server success rate: " + new DecimalFormat("#.##").format(successRate * 100) + "%\n";
        performanceReport += "Server failure (unprocessed) rate: " + new DecimalFormat("#.##").format(failureRate * 100) + "%";

        return performanceReport;
    }
}
