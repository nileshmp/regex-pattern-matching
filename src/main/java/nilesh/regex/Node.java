package nilesh.regex;

import nilesh.regex.utils.StateCounter;
import nilesh.regex.utils.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Node
{
    private final String state;
    private Map<Character, Set<Node>> transitionMap;
    private boolean isEnd;
    private boolean isStart;

    public Node (boolean isEnd)
    {
        this(isEnd, StateCounter.next());
    }

    public Node (boolean isEnd, String state)
    {
        this(isEnd, false, state);
    }

    public Node (boolean isEnd, boolean isStart, String state)
    {
        this.isEnd = isEnd;
        this.isStart = isStart;
        this.state = state;
        transitionMap = new HashMap<>();
    }

    public boolean isEnd ()
    {
        return isEnd;
    }

    public void setEnd (boolean end)
    {
        isEnd = end;
    }

    public String state ()
    {
        return state;
    }

    public boolean isStart ()
    {
        return this.isStart;
    }

    public void setStartNode (boolean start)
    {
        this.isStart = start;
    }

    public Map<Character, Set<Node>> getTransitions ()
    {
        return this.transitionMap;
    }

    public void transition (char value, Node node)
    {
        Set<Node> nodes = transitionMap.getOrDefault(value, new HashSet<>());
        nodes.add(node);
        transitionMap.put(value, nodes);
    }

    public void transition (Character value, Collection<Node> possibleTransitions)
    {
        Set<Node> nodes = transitionMap.getOrDefault(value, new HashSet<>());
        nodes.addAll(possibleTransitions);
        transitionMap.put(value, nodes);
    }

    public Node nextTransition (char currChar)
    {
        // is it possible to transition to the given path form the current node
        if (transitionMap.containsKey(currChar))
            return transitionMap.get(currChar).iterator().next();
        else {
            Set<Node> nodes = transitionMap.getOrDefault('\0', new HashSet<>());
            if (nodes.isEmpty())
                return null;
            else {
                for (Node node : nodes) {
                    Node transition = node.nextTransition(currChar);
                    if (transition == null)
                        continue;
                    return transition;
                }
                return null;
            }
        }
    }

    public boolean canTransitionToEnd ()
    {
        if (isEnd())
            return true;
        else {
            Set<Node> nodes = transitionMap.getOrDefault('\0', new HashSet<>());
            if (nodes == null || nodes.isEmpty())
                return false;
            else {
                for (Node node : nodes) {
                    boolean canTransitionToEnd = node.canTransitionToEnd();
                    if (!canTransitionToEnd)
                        continue;
                    return canTransitionToEnd;
                }
                return false;
            }
        }
    }

    public Set<Node> epsilonTransitions ()
    {
        return this.transitionMap.getOrDefault('\0', new HashSet<>());
    }

    public Set<Node> transitions (Character value)
    {
        return this.transitionMap.getOrDefault(value, new HashSet<>());
    }

    @Override
    public boolean equals (Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Node node = (Node)o;
        return isEnd() == node.isEnd() &&
            isStart() == node.isStart() &&
            Objects.equals(transitionMap, node.transitionMap) &&
            StringUtils.equals(state, node.state);
    }

    public Node union (Node node)
    {
        String collect = this.state() + "-" + node.state;
        Node unionNode = Nodes.getOrNewNode(
            (this.isEnd() || node.isEnd()),
            (node.isStart() || this.isStart()),
            collect);
        unionNode.setStartNode(node.isStart() || this.isStart());
        Map<Character, Set<Node>> transitions = this.getTransitions();
        for (Character character : transitions.keySet()) {
            unionNode.transition(character, transitions.get(character));
        }
        transitions = node.getTransitions();
        for (Character character : transitions.keySet()) {
            unionNode.transition(character, transitions.get(character));
        }
        //TODO when you union 2 nodes you have to discard the previous nodes.
        return unionNode;
    }
//    public void clearEpsilonTransitions ()
//    {
//        this.transitionMap.remove('\0');
//    }
//    @Override
//    public String toString ()
//    {
//        StringBuffer buffer = new StringBuffer();
//        buffer.append("\t");
//        buffer.append("Node{");
//        buffer.append("\n");
//        buffer.append("\t\t");
//        buffer.append("state='" + state + '\'');
//        buffer.append("\n");
//        buffer.append("\t\t");
//        buffer.append("isEnd = " + isEnd);
//        buffer.append("\n");
//        buffer.append("\t\t");
//        buffer.append("isStart = " + isStart);
//        buffer.append("\n");
//        for (Character character : transitionMap.keySet()) {
//            buffer.append("\t\t");
//            buffer.append("Character = ");
//            buffer.append(character == '\0' ? "epsilon" : character);
//            buffer.append("\n");
//            buffer.append("\t\t");
//            buffer.append("Value: [");
//            buffer.append("\n");
//            buffer.append("\t\t");
//            buffer.append(transitionMap.get(character).stream().map(node -> node.toString()).collect(Collectors.joining()));
//            buffer.append("\n");
//            buffer.append("]");
//        }
//        buffer.append("\n");
//        buffer.append("\t\t");
//        buffer.append("}");
//        buffer.append("\n");
//        return buffer.toString();
//    }
}
