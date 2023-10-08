public class Request {
    private final String username;
    private final String hostname;
    private final String command;
    private final String args;

    public Request(String request) {
        int commandIndex = request.indexOf(":", request.indexOf(":") + 2) + 1;
        String command = request.substring(commandIndex).trim();
        int argumentsIndex = command.indexOf(" ");

        String[] strings = request.split(":");
        this.username = strings[0].trim();
        this.hostname = strings[1].trim();

        if (argumentsIndex == -1) {
            this.command = command;
            this.args = "";
        } else {
            this.command = command.substring(0, argumentsIndex).trim();
            this.args = command.substring(argumentsIndex).trim();
        }
    }

    public String getUsername() {
        return username;
    }

    public String getHostname() {
        return hostname;
    }

    public String getPrefix() {
        return username + " : " + hostname + " : ";
    }

    public String getCommand() {
        return command;
    }

    public String getArgs() {
        return args;
    }
}
