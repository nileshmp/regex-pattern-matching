package nilesh.regex;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Its a single ton repository of nodes, basically the idea being the reuse of nodes if they exists.
 */
public class Nodes
{
    static Set<Node> nodes = new HashSet<>();

    public static void resetState ()
    {
        nodes = new HashSet<>();
    }

    public static Node fromEpsilon (boolean isEnd)
    {
        return newNode(isEnd);
    }

    public static Node newNode (boolean isEnd)
    {
        Node node = new Node(isEnd);
        nodes.add(node);
        return node;
    }

    public static Node newNode (boolean isEnd, String state)
    {
        Node node = new Node(isEnd, state);
        nodes.add(node);
        return node;
    }

    public static Node newNode (boolean isEnd, boolean isStart, String state)
    {
        Node node = new Node(isEnd, isStart, state);
        nodes.add(node);
        return node;
    }

    public static Node getOrNewNode (boolean isEnd, String state)
    {
        Set<Node> filteredNodes = nodes.stream().filter(node -> ((node.state().equalsIgnoreCase(state)) &&
            (node.isEnd() == isEnd))).collect(
            Collectors.toSet());
        if (filteredNodes.isEmpty())
            return newNode(isEnd, state);
        else
            return filteredNodes.iterator().next();
    }

    public static Node getOrNewNode (boolean isEnd, boolean isStart, String state)
    {
        Set<Node> filteredNodes = nodes.stream().filter(node -> ((node.state().equalsIgnoreCase(state)) &&
            (node.isEnd() == isEnd) && (node.isStart() == isStart))).collect(
            Collectors.toSet());
        if (filteredNodes.isEmpty())
            return newNode(isEnd, isStart, state);
        else
            return filteredNodes.iterator().next();
    }
//    public static Node get (String state)
//    {
//        Set<Node> filteredNodes = nodes.stream().filter(node -> ((node.state().equalsIgnoreCase(state)))).collect(
//            Collectors.toSet());
//        return filteredNodes.iterator().next();
//    }
}
