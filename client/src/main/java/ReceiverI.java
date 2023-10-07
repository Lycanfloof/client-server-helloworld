import AppInterface.Receiver;
import com.zeroc.Ice.Current;

public class ReceiverI implements Receiver {
    @Override
    public void printString(String str, Current current) {
        System.out.println(str);
    }
}
