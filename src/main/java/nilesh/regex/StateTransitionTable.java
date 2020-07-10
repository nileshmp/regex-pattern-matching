package nilesh.regex;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class StateTransitionTable
{
    private Map<String, Map<Character, String>> table;
    private Collection<Character> values;
    private String startState;
    private Set<String> endStates = new HashSet<>();

    public StateTransitionTable (States dfsStates)
    {
        startState = dfsStates.startState().node().state();
        // create a 2 dimensional data structure map of map array
        // removes unreachable states
        table = removeUnreachableStates(constructMap(dfsStates));
    }

    private Map<String, Map<Character, String>> constructMap (States dfsStates)
    {
        // initialise values for convenience
        values = dfsStates.list().iterator().next().values().stream().map(value -> value.value()).collect(
            Collectors.toList());
        Map<String, Map<Character, String>> table = new HashMap<>();
        for (State state : dfsStates.list()) {
            Map<Character, String> valueMap = new HashMap<>();
            for (Value value : state.values()) {
                if (!value.possibleTransitions().isEmpty()) {
                    String valueState = value.possibleTransitions().iterator().next().state();
                    if (!valueState.isEmpty())
                        valueMap.put(value.value(), valueState);
                }
            }
            Node node = state.node();
            if (node.isEnd())
                endStates.add(node.state());
            table.put(node.state(), valueMap);
        }
        return table;
    }

    public String get (String state, char value)
    {
        if (table.containsKey(state) && table.get(state).containsKey(value))
            return table.get(state).get(value);
        else
            return "";
    }

    public Map<Character, String> get (String state)
    {
        return this.table.get(state);
    }

    public Collection<Character> values ()
    {
        return this.values;
    }

    public Set<String> keys ()
    {
        return this.table.keySet();
    }

    public boolean isStart (String stateValue)
    {
        return startState.equalsIgnoreCase(stateValue);
    }

    public boolean isEnd (String stateValue)
    {
        return endStates.contains(stateValue);
    }

    public Map<String, Map<Character, String>> getTable ()
    {
        return this.table;
    }

    public boolean containsStartState (String value)
    {
        return value.contains(startState);
    }

    public boolean containsEndState (String value)
    {
        for (String endState : endStates) {
            if (value.contains(endState))
                return true;
        }
        return false;
    }

    private Map<String, Map<Character, String>> removeUnreachableStates (Map<String, Map<Character, String>> table)
    {
        Set<String> reachableStates = new HashSet<>();
        reachableStates.add(startState);
        Map<Character, String> valueMap = table.get(startState);
        traverseValueMap(reachableStates, valueMap, table);
        return constructTable(reachableStates, table);
    }

    private Map<String, Map<Character, String>> constructTable (Set<String> reachableStates,
                                                                Map<String, Map<Character, String>> table)
    {
        Map<String, Map<Character, String>> reachableStateTable = new HashMap<>();
        for (String reachableState : reachableStates) {
            reachableStateTable.put(reachableState, table.get(reachableState));
        }
        return reachableStateTable;
    }

    private void traverseValueMap (Set<String> reachableStates,
                                   Map<Character, String> valueMap,
                                   Map<String, Map<Character, String>> table)
    {
        for (String reachableState : valueMap.values()) {
            if (reachableStates.contains(reachableState))
                continue;
            reachableStates.add(reachableState);
            traverseValueMap(reachableStates, table.get(reachableState), table);
        }
    }
}
