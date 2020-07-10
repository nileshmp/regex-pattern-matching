package nilesh.regex;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static nilesh.regex.Nodes.fromEpsilon;

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

    public Automata join (Automata another)
    {
        Node epsilonStart = fromEpsilon(false);
        epsilonStart.transition('\0', this.Start());
        epsilonStart.transition('\0', another.Start());
        Node epsilonEnd = fromEpsilon(true);
        this.End().transition('\0', epsilonEnd);
        another.End().transition('\0', epsilonEnd);
        this.End().setEnd(false);
        another.End().setEnd(false);
        return new Automata(epsilonStart, epsilonEnd);
    }

    private void nodes (Set<Node> nodes, Node node)
    {
        if (nodes.contains(node))
            return;
        nodes.add(node);
        Map<Character, Set<Node>> transitions = node.getTransitions();
        for (Set<Node> value : transitions.values()) {
            for (Node innerNode : value) {
                nodes(nodes, innerNode);
            }
        }
    }

    @Override
    public String toString ()
    {
        Set<Node> nodes = new HashSet<>();
        nodes(nodes, this.Start());
        StringBuffer buffer = new StringBuffer();
        buffer.append("Automata");
        buffer.append("\n");
        buffer.append("{");
        buffer.append("\n");
        for (Node node : nodes) {
            buffer.append("\t");
            buffer.append("Node{");
            buffer.append("\n");
            buffer.append("\t\t");
            buffer.append("state='" + node.state() + '\'');
            buffer.append("\n");
            buffer.append("\t\t");
            buffer.append("isEnd = " + node.isEnd());
            buffer.append("\n");
            buffer.append("\t\t");
            buffer.append("isStart = " + node.isStart());
            buffer.append("\n");
            Map<Character, Set<Node>> transitions = node.getTransitions();
            for (Character character : transitions.keySet()) {
                buffer.append("\t\t");
                buffer.append("Value = ");
                buffer.append(character == '\0' ? "epsilon" : character);
                buffer.append("\n");
                buffer.append("\t\t");
                buffer.append("Transitions: " +
                    transitions.get(character).stream().map(currNode -> currNode.state()).collect(Collectors.joining(",")));
                buffer.append("\n");
            }
            buffer.append("\t");
            buffer.append("}");
            buffer.append("\n");
        }
        buffer.append("}");
        return buffer.toString();
    }
}
