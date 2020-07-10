package nilesh.regex.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class StateCounter
{
    private static AtomicInteger counter = new AtomicInteger(100);

    public static String next ()
    {
        return Integer.toString(counter.getAndIncrement());
    }
}
