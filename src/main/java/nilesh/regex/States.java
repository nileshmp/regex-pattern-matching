package nilesh.regex;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class States
{
    private Map<String, State> stateMap;
    private State startState;

    public States ()
    {
        this.stateMap = new HashMap<>();
    }

    public void add (Node node)
    {
        this.stateMap.put(node.state(), new State(node));
    }

    public void add (State state)
    {
        this.stateMap.put(state.node().state(), state);
    }

    public Collection<State> list ()
    {
        return this.stateMap.values();
    }

    /**
     * Iterates over each state and sets the SetValue derived from the string.
     */
    public void setValues (String values)
    {
        List<Character> valueList = values.chars().mapToObj(c -> (char)c).collect(Collectors.toList());
        for (State state : this.stateMap.values()) {
            state.values(valueList);
        }
    }

    public State state (Node node)
    {
        for (State state : this.stateMap.values()) {
            if (node == state.node())
                return state;
        }
        return null;
    }

    public State get (String key)
    {
        return this.stateMap.get(key);
    }

    public Node node (String collect)
    {
        for (State state : stateMap.values()) {
            Node node = state.node();
            if (node.state().equalsIgnoreCase(collect)) {
                return node;
            }
            else {
                for (Value value : state.values()) {
                    for (Node valueNode : value.possibleTransitions()) {
                        if (valueNode.state().equalsIgnoreCase(collect)) {
                            return valueNode;
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public String toString ()
    {
        // get the startState state
        // from there call the
        StringBuffer buffer = new StringBuffer();
        for (State value : stateMap.values()) {
            buffer.append(value.toString());
            buffer.append("\n");
        }
        return buffer.toString();
    }

    public void setStartState (State dfsState)
    {
        this.startState = dfsState;
    }

    public State startState ()
    {
        return this.startState;
    }
}
