public class Request {
    private String prefix;
    private String message;

    public Request(String message) {
        int messageIndex = message.indexOf(":", message.indexOf(":") + 2) + 1;

        this.prefix = message.substring(0, messageIndex);
        this.message = message.substring(messageIndex).trim();
    }

    public String getPrefix() {
        return prefix;
    }
    public String getMessage() {
        return message;
    }

    public String executeRequest() throws java.io.IOException {
        String output;
        Long integerValue = getNumber(message);

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
            output = " " + getPrimeFactors(integerValue);

        } else {
            output = " " + message;
        }

        return output;
    }

    private String executeCommand(String[] command) throws java.io.IOException {
        String output = "";

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            java.io.BufferedReader bufferedReader = new java.io.BufferedReader(
                new java.io.InputStreamReader(process.getInputStream()));

            String line;
            
            while ((line = bufferedReader.readLine()) != null) {
                output += line + "\n";
            }

        return output.trim() + "\n";
    }

    private Long getNumber(String s) {
        try {
            return Long.valueOf(s);
        } catch (Exception e) {
            return null;
        }
    }

    private String getPrimeFactors(long integerValue) {
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
