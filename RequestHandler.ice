module AppInterface
{
    interface Receiver
    {
        void printString(string str);
    }

    interface RequestHandler
    {
        string handleRequest(Receiver* receiver, string request);
    }
}
