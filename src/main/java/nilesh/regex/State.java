package nilesh.regex;

import nilesh.regex.utils.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class State
{
    private Node node;
    private Map<Character, Value> valueMap;

    public State (Node node)
    {
        this.node = node;
        this.valueMap = new LinkedHashMap();
    }

    public void values (List<Character> valueList)
    {
        for (Character character : valueList) {
            this.valueMap.put(character, new Value(character));
        }
    }

    public Collection<Value> values ()
    {
        return this.valueMap.values();
    }

    public Node node ()
    {
        return this.node;
    }

    public Value getValue (Character character)
    {
        return this.valueMap.get(character);
    }

    public void SetValue (Value value)
    {
        this.valueMap.put(value.value(), value);
    }

    public State union (States dfsStates, Node node, State state, String endState)
    {
        State union = new State(node);
        for (Value value : this.values()) {
            Set<Node> values = new HashSet<>();
            values.addAll(value.possibleTransitions());
            values.addAll(state.getValue(value.value()).possibleTransitions());
            // check if the current state is already part of the nodes state e.g: "1-2".contains"1" or 2-3-4-5.contains"3-4"
            Set<Node> subsumed = CollectionUtils.subsume(values);
            // combine them to a single node...
            Collection<Node> valueNodes = combine(dfsStates, subsumed, endState);
            union.SetValue(new Value(value.value()).addPossibleTransitions(valueNodes));
        }
        return union;
    }

    private Collection<Node> combine (States dfsStates, Set<Node> values, String endState)
    {
        if (values.isEmpty())
            return Collections.EMPTY_SET;
        String collect = values.stream().map(currNode -> currNode.state()).collect(Collectors.joining("-"));
        // check if node with same SetValue exists in the table, if so use the same
        Node nodePresent = dfsStates.node(collect);
        if (Objects.nonNull(nodePresent)) {
            return new HashSet<Node>()
            {{
                add(nodePresent);
            }};
        }
        // handling case if the node with same SetValue not present
        else {
            return new HashSet<Node>()
            {{
                add(Nodes.getOrNewNode(collect.contains(endState), collect));
            }};
        }
    }

    @Override
    public boolean equals (Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        State state = (State)o;
        return Objects.equals(node, state.node) &&
            Objects.equals(valueMap, state.valueMap);
    }

    @Override
    public int hashCode ()
    {
        return Objects.hash(node, valueMap);
    }

    @Override
    public String toString ()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("State {");
        buffer.append("\n");
        buffer.append("\tState-value: " + node.state());
        buffer.append("\n");
        buffer.append("\tisStart: " + node.isStart());
        buffer.append("\n");
        buffer.append("\tisEnd: " + node.isEnd());
        buffer.append("\n");
        buffer.append("\t\t[");
        buffer.append("\n");
        for (Value value : valueMap.values()) {
            buffer.append("\t\t\tValue: " + value.value() + ", transitions: " +
                value.possibleTransitions().stream().map(node -> node.state()).collect(Collectors.joining(",")));
            buffer.append("\n");
        }
        buffer.append("\t\t]");
        buffer.append("\n");
        buffer.append("}");
        return buffer.toString();
    }

    public Node toNode ()
    {
        Node node = new Node(this.node().isEnd(), this.node().isStart(), this.node().state());
        for (Value value : this.values()) {
            node.transition(value.value(), value.possibleTransitions().iterator().next());
        }
        return node;
    }
}
