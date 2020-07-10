package nilesh.regex;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DFAMinimizer
{
    private final StateTransitionTable transitionTable;
    private final States dfsStates;
    private LinkedHashSet<String> endStates;

    public DFAMinimizer (States dfsStates)
    {
        transitionTable = new StateTransitionTable(dfsStates);
        Nodes.resetState();
        this.dfsStates = buildStates(transitionTable);
    }

    private States buildStates (StateTransitionTable transitionTable)
    {
        States states = new States();
        for (String stateValue : transitionTable.keys()) {
            boolean isStart = transitionTable.isStart(stateValue);
            boolean isEnd = transitionTable.isEnd(stateValue);
            State state = new State(Nodes.getOrNewNode(isEnd, isStart, stateValue));
            Map<Character, String> transitions = transitionTable.get(stateValue);
            for (Character character : transitions.keySet()) {
                Value value = new Value(character);
                String stateValue1 = transitions.get(character);
                isStart = transitionTable.isStart(stateValue1);
                isEnd = transitionTable.isEnd(stateValue1);
                value.setPossibleTransition(Nodes.getOrNewNode(isEnd, isStart, stateValue1));
                state.SetValue(value);
            }
            if (state.node().isStart())
                states.setStartState(state);
            states.add(state);
        }
        return states;
    }

    public Automata minimize ()
    {
        // TODO - remove non-reachable states
        LinkedHashSet<LinkedHashSet<String>> equivalentState = createZeroEquivalentState(dfsStates);
        int sizeBefore = equivalentState.size();
        int sizeAfter = Integer.MAX_VALUE;
        while (sizeBefore != sizeAfter) {
            sizeBefore = equivalentState.size();
            nextEquivalentState(equivalentState);
            sizeAfter = equivalentState.size();
        }
        Map<String, Map<Character, String>> transitionTable = rebuildTransitionTable(equivalentState);
        return buildAutomata(transitionTable);
    }

    private Map<String, Map<Character, String>> rebuildTransitionTable (LinkedHashSet<LinkedHashSet<String>> equivalentState)
    {
        Map<String, Map<Character, String>> table = transitionTable.getTable();
        Map<String, Map<Character, String>> newTable = new HashMap<>();
        Map<String, String> concanatedValues = new Hashtable<>();
        for (LinkedHashSet<String> mergeableStates : equivalentState) {
            String collect = mergeableStates.stream().collect(Collectors.joining("-"));
            for (String mergeableState : mergeableStates) {
                concanatedValues.put(mergeableState, collect);
            }
        }
        for (String key : table.keySet()) {
            String value = concanatedValues.getOrDefault(key, key);
            Map<Character, String> valueMap = table.get(key);
            Map<Character, String> newValueMap = new HashMap<>();
            for (Character character : valueMap.keySet()) {
                String charValue = valueMap.get(character);
                charValue = concanatedValues.getOrDefault(charValue, charValue);
                newValueMap.put(character, charValue);
            }
            newTable.put(value, newValueMap);
        }
        return newTable;
    }

    private Automata buildAutomata (Map<String, Map<Character, String>> equivalentState)
    {
        Nodes.resetState();
        Node startNode = null;
        for (String value : equivalentState.keySet()) {
            Node node = buildNode(value, equivalentState.get(value));
            if (node.isStart())
                startNode = node;
        }
        return new Automata(startNode, null);
    }

    private Node buildNode (String value, Map<Character, String> characterStringMap)
    {
        boolean isStart = this.transitionTable.containsStartState(value);
        boolean isEnd = this.transitionTable.containsEndState(value);
        Node node = Nodes.getOrNewNode(isEnd, isStart, value);
        for (Character character : characterStringMap.keySet()) {
            String innerNodeValue = characterStringMap.get(character);
            isStart = this.transitionTable.containsStartState(innerNodeValue);
            isEnd = this.transitionTable.containsEndState(innerNodeValue);
            node.transition(character, Nodes.getOrNewNode(isEnd, isStart, innerNodeValue));
        }
        return node;
    }

    private LinkedHashSet<LinkedHashSet<String>> createZeroEquivalentState (States dfsStates)
    {
        endStates = new LinkedHashSet<>();
        LinkedHashSet<String> otherStates = new LinkedHashSet<>();
        for (State state : dfsStates.list()) {
            if (state.node().isEnd())
                endStates.add(state.node().state());
            else
                otherStates.add(state.node().state());
        }
        LinkedHashSet<LinkedHashSet<String>> setOfSets = new LinkedHashSet<>();
        setOfSets.add(endStates);
        setOfSets.add(otherStates);
        return setOfSets;
    }

    private void nextEquivalentState (LinkedHashSet<LinkedHashSet<String>> equivalentState)
    {
        List<LinkedHashSet<String>> equivalentStateList = new LinkedList<>(equivalentState);
        for (int count = 0; count < equivalentStateList.size(); count++) {
            LinkedHashSet<String> innerSet = equivalentStateList.get(count);
            LinkedHashSet<String> separatedSet = separateSets(innerSet, equivalentState);
            innerSet.removeAll(separatedSet);
            if (separatedSet.size() > 0)
                equivalentStateList.add(separatedSet);
        }
//        int count = 0;
//        while(count < equivalentStateList.size()) {
//            int tempCount = 0;
//            for (LinkedHashSet<String> innerSet : equivalentStateList) {
//                if(tempCount < count) {
//                    tempCount++;
//                    continue;
//                }
//                LinkedHashSet<String> separatedSet = separateSets(innerSet, equivalentState);
//                innerSet.removeAll(separatedSet);
//                if (separatedSet.size() > 0)
//                    equivalentStateList.add(separatedSet);
//                count++;
//                tempCount++;
//            }
//        }
        equivalentState.clear();
        equivalentState.addAll(equivalentStateList);
    }

    private LinkedHashSet<String> separateSets (LinkedHashSet<String> innerSet,
                                                Set<LinkedHashSet<String>> equivalentState)
    {
        LinkedHashSet<String> separatedSet = new LinkedHashSet<>();
        if (innerSet.size() >= 2) {
            Enumeration<String> enumeration = Collections.enumeration(innerSet);
            String first = enumeration.nextElement();
            while (enumeration.hasMoreElements()) {
                String second = enumeration.nextElement();
                boolean doesNotBelong = belong2SameGroup(first, second, equivalentState);
                if (doesNotBelong)
                    continue;
                else {
                    separatedSet.add(second);
                }
            }
        }
        return separatedSet;
    }

    private boolean belong2SameGroup (String first,
                                      String second, Set<LinkedHashSet<String>> equivalentState)
    {
        if (first == second)
            return true;
        Map<Character, String> firstTransitions = transitionTable.get(first);
        Map<Character, String> secondTransitions = transitionTable.get(second);
        if (firstTransitions.size() == secondTransitions.size()) {
            if (!firstTransitions.keySet().containsAll(secondTransitions.keySet())) {
                return false;
            }
        }
        else
            return false;
        for (LinkedHashSet<String> set : equivalentState) {
            for (Character value : transitionTable.values()) {
                // handling cases where there might not be a state with respect to that SetValue
                String state1 = transitionTable.get(first, value);
                String state2 = transitionTable.get(second, value);
                boolean containsFirst = false;
                boolean containsSecond = false;
                if (set.contains(state1)) {
                    containsFirst = true;
                }
                if (set.contains(state2)) {
                    containsSecond = true;
                }
                // if both are true continue;
                if (containsFirst && containsSecond) {
                    // break the for loop
                    return true;
                }
                else if (!containsFirst && !containsSecond) {
                    continue;
                }
                else if (!containsFirst || !containsSecond) {
                    return false;
                }
            } // while ends
        } // for ends
        return false;
    }
//    private String belong2SameGroup (String first,
//                                     String second, LinkedHashSet<LinkedHashSet<String>> equivalentState)
//    {
//        if (first == second)
//            return "";
//        Map<Character, String> firstTransitions = transitionTable.get(first);
//        Map<Character, String> secondTransitions = transitionTable.get(second);
//        if (firstTransitions.size() == secondTransitions.size()) {
//            if(!firstTransitions.keySet().containsAll(secondTransitions.keySet()))
//            {
//                return "";
//            }
//        }
//        else
//            return "";
//
//        for (LinkedHashSet<String> set : equivalentState) {
//            for (Character value : transitionTable.values()) {
//                // handling cases where there might not be a state with respect to that SetValue
//                String state1 = transitionTable.get(first, value);
//                String state2 = transitionTable.get(second, value);
//                boolean containsFirst = false;
//                boolean containsSecond = false;
//                if (set.contains(state1)) {
//                    containsFirst = true;
//                }
//                if (set.contains(state2)) {
//                    containsSecond = true;
//                }
//                // if both are true continue;
//                if (containsFirst && containsSecond) {
//                    // break the for loop
//                    return "";
//                }
//                else if (!containsFirst && !containsSecond) {
//                    continue;
//                }
//                else if (!containsFirst || !containsSecond) {
//                    return second;
//                }
//            } // while ends
//        } // for ends
//        return "";
//    }
}
