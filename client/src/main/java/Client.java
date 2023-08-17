public class Client
{
    public static void main(String[] args)
    {
        java.util.List<String> extraArgs = new java.util.ArrayList<>();

        try(com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args,"config.client",extraArgs))
        {
            Demo.PrinterPrx printer = Demo.PrinterPrx.checkedCast(
                communicator.propertyToProxy("Printer.Proxy")).ice_twoway().ice_secure(false);

            if(printer == null)
            {
                throw new Error("Invalid proxy.");
            }

            try {
                printMessage(printer);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private static void printMessage(Demo.PrinterPrx printer) throws java.io.IOException {
        String username = System.getProperty("user.name");
        String hostname = java.net.InetAddress.getLocalHost().getHostName();

        java.io.BufferedReader br = new java.io.BufferedReader(
            new java.io.InputStreamReader(System.in));

        System.out.println("Enter a message. You can type 'exit' to close the connection to the server.");
        String message;

        while (!(message = br.readLine()).equals("exit")) {
            System.out.println(printer.printString(username + " : " + hostname + " : " + message));
        }

        br.close();
    }
}
