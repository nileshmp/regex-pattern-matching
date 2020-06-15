package nilesh.regex;

public class Automata
{
    private final Node start;
    private final Node end;

    public Automata (Node start, Node end)
    {
        this.start = start;
        this.end = end;
    }

    public Node Start ()
    {
        return start;
    }

    public Node End ()
    {
        return end;
    }
}
