import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommonLibrary {
    public static String executeCommand(String[] command) throws IOException {
        StringBuilder output = new StringBuilder();

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));

        String line;

        while ((line = bufferedReader.readLine()) != null) {
            output.append(line).append("\n");
        }

        return output.toString().trim() + "\n";
    }
}
