package nilesh.regex.utils;

import nilesh.regex.Automata;
import nilesh.regex.Node;
import nilesh.regex.Nodes;

public class AutomataUtil
{
    public static Automata fromChar (char currChar)
    {
        Node start = Nodes.newNode(false);
        Node end = Nodes.newNode(true);
        start.transition(currChar, end);
        return new Automata(start, end);
    }
}
