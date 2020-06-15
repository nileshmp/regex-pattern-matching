package nilesh.regex;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Node
{
    private final Map<Character, Set<Node>> transitionMap;

    private boolean isEnd;

    public Node (boolean isEnd)
    {
        this.isEnd = isEnd;
        transitionMap = new HashMap<>();
    }

    public void transition (Node node, char value)
    {
        Set<Node> nodes = transitionMap.getOrDefault(value, new HashSet<>());
        nodes.add(node);
        transitionMap.put(value, nodes);
    }

    public boolean isEnd ()
    {
        return isEnd;
    }

    public void End (boolean end)
    {
        isEnd = end;
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
                    if(transition == null)
                        continue;
                    return transition;
                }
                return null;
            }
        }
    }

    public boolean canTransitionToEnd()
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
                    if(!canTransitionToEnd)
                        continue;
                    return canTransitionToEnd;
                }
                return false;
            }
        }
    }
}
