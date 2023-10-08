import AppInterface.ReceiverPrx;

public class PrimeFactorsCommand extends Command {
    @Override
    public void executeProcess(ReceiverPrx clientProxy, String username, String hostname, String args) {
        Long integer = CommonLibrary.convertToNumber(args);
        if (integer != null)
            setOutput(calculatePrimeFactors(integer));
        else
            setOutput("The given argument isn't valid because it's not a number.");
    }

    private String calculatePrimeFactors(long integerValue) {
        StringBuilder output = new StringBuilder();

        while (integerValue % 2 == 0) {
            output.append(2 + " ");
            integerValue /= 2;
        }

        for (long i = 3; i <= Math.sqrt(Math.abs(integerValue)); i += 2) {
            while (integerValue % i == 0) {
                output.append(i).append(" ");
                integerValue /= i;
            }
        }

        if (Math.abs(integerValue) > 2) {
            output.append(integerValue);
        }

        return output.toString();
    }
}
