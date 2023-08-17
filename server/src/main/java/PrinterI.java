public class PrinterI implements Demo.Printer
{
    public String printString(String s, com.zeroc.Ice.Current current)
    {
        String output;

        int messageIndex = s.indexOf(":", s.indexOf(":") + 2) + 1;
        String prefix = s.substring(0, messageIndex);
        String message = s.substring(messageIndex).trim();

        Integer integerValue;

        if (message.equals("listifs")) {
            String[] command = {"ifconfig"};
            output = "\n\n" + executeCommand(command);

        } else if (message.startsWith("listports") && message.length() > 9) {
            String[] command = {"nmap", "-Pn", message.substring(9).trim()};
            output = "\n\n" + executeCommand(command);

        } else if (message.startsWith("!") && message.length() > 1) {
            String[] command = {message.substring(1)};
            output = "\n\n" + executeCommand(command);

        } else if ((integerValue = getNumber(message)) != null) {
            output = " " + getPrimeFactors(integerValue);

        } else {
            output = " " + message;

        }

        String serverOutput = prefix + output;
        String clientOutput = "response:" + output;

        System.out.println(serverOutput);

        return clientOutput;
    }

    private Integer getNumber(String s) {
        try {
            return Integer.valueOf(s);
        } catch (Exception e) {
            return null;
        }
    }

    private String getPrimeFactors(int integerValue) {
        String output = "";

        while (integerValue % 2 == 0) {
            output += 2 + " ";
            integerValue /= 2;
        }

        for (int i = 3; i <= Math.sqrt(integerValue); i += 2) {
            while (integerValue % i == 0) {
                output += i + " ";
                integerValue /= i;
            }
        }

        if (integerValue > 2) {
            output += integerValue;
        }

        return output;
    }

    private String executeCommand(String[] command) {
        String output = "";

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            java.io.BufferedReader bufferedReader = new java.io.BufferedReader(
                new java.io.InputStreamReader(process.getInputStream()));

            String line;
            
            while ((line = bufferedReader.readLine()) != null) {
                output += line + "\n";
            }
        } catch (Exception e) {
            output = e.getMessage();
        }

        return output.trim() + "\n";
    }
}
