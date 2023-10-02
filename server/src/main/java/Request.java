import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Request {
    private String prefix;
    private String message;
    private long startTime;
    private long endTime;
    private String output;

    public Request(String message) {
        int messageIndex = message.indexOf(":", message.indexOf(":") + 2) + 1;
        this.prefix = message.substring(0, messageIndex);
        this.message = message.substring(messageIndex).trim();
        this.startTime = 0;
        this.endTime = 0;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getMessage() {
        return message;
    }

    public long requestTime() {
        return endTime - startTime;
    }

    public String getOutput() {
        return output;
    }

    public void start() {
        startTime = System.nanoTime();
        startRequest();
        endTime = System.nanoTime();
    }

    private void startRequest() {
        try {
            Long integerValue = convertToNumber(message);

            if (message.equals("listifs")) {
                String[] command = {"ifconfig"};
                output = "\n\n" + executeCommand(command);

            } else if (message.startsWith("listports") && message.length() > 9) {
                String[] command = {"nmap", message.substring(9).trim()};
                output = "\n\n" + executeCommand(command);

            } else if (message.startsWith("!") && message.length() > 1) {
                String[] command = message.substring(1).trim().split(" ");
                output = "\n\n" + executeCommand(command);

            } else if (integerValue != null) {
                output = " " + calculatePrimeFactors(integerValue);

            } else {
                output = " " + message;
            }
        } catch (Exception e) {
            output = "An error has occurred while processing the request.";
            e.printStackTrace();
        }
    }

    private Long convertToNumber(String s) {
        try {
            return Long.valueOf(s);
        } catch (Exception e) {
            return null;
        }
    }

    private String executeCommand(String[] command) throws IOException {
        String output = "";

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));

            String line;
            
            while ((line = bufferedReader.readLine()) != null) {
                output += line + "\n";
            }

        return output.trim() + "\n";
    }

    private String calculatePrimeFactors(long integerValue) {
        String output = "";

        while (integerValue % 2 == 0) {
            output += 2 + " ";
            integerValue /= 2;
        }

        for (long i = 3; i <= Math.sqrt(Math.abs(integerValue)); i += 2) {
            while (integerValue % i == 0) {
                output += i + " ";
                integerValue /= i;
            }
        }

        if (Math.abs(integerValue) > 2) {
            output += integerValue;
        }

        return output;
    }
}
