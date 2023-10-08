public class Request {
    private final String prefix;
    private final String command;
    private final String[] args;

    public Request(String request) {
        int commandIndex = request.indexOf(":", request.indexOf(":") + 2) + 1;
        String command = request.substring(commandIndex).trim();
        int argumentsIndex = command.indexOf(" ");

        this.prefix = request.substring(0, commandIndex);

        if (argumentsIndex == -1) {
            this.command = command;
            this.args = new String[0];
        } else {
            this.command = command.substring(0, argumentsIndex);
            this.args = command.substring(argumentsIndex).trim().split(" ");
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public String getCommand() {
        return command;
    }

    public String[] getArgs() {
        return args;
    }
}
