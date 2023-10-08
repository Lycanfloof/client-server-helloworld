import AppInterface.ReceiverPrx;

public class PrimeFactorsCommand extends Command {
    @Override
    public void executeProcess(ReceiverPrx clientProxy, String username, String args) {
        Long number = convertToNumber(args.trim());
        if (number != null)
            setOutput(calculatePrimeFactors(number));
        else
            setOutput("The given argument isn't valid.");
    }

    private Long convertToNumber(String s) {
        try {
            return Long.valueOf(s);
        } catch (Exception e) {
            return null;
        }
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
