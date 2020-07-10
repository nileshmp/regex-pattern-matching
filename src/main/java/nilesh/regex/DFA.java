package nilesh.regex;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DFA
{
    private Automata automata;
    private String regex;

    public States convertToDFA (Automata automata, String regex)
    {
        this.automata = automata;
        this.regex = regex;
        Set<Node> nodes = dfaStates(automata);
        return buildDFAStateTransitionTable(nodes);
    }

    private States buildDFAStateTransitionTable (Set<Node> nodes)
    {
        Nodes.resetState();
        String startState = this.automata.Start().state();
        String endState = this.automata.End().state();
        Set<Node> epsilonNodes = new HashSet<>();
        for (Node node : nodes) {
            Set<Node> epsilonTransitions = new HashSet<>();
            epsilonNodes(epsilonTransitions, node);
            String collect = epsilonTransitions.stream().map(tempNode -> tempNode.state()).sorted().collect(Collectors.joining(
                "-"));
            Node newNode = Nodes.getOrNewNode(collect.contains(endState), collect.contains(startState), collect);
            epsilonNodes.add(newNode);
        }
        States states = new States();
        String values = extractValues(regex);
        List<Character> valueList = values.chars().mapToObj(c -> (char)c).collect(Collectors.toList());
        for (Node epsilonNode : epsilonNodes) {
            State state = new State(epsilonNode);
            state.values(valueList);
            for (Value value : state.values()) {
                Set<Node> transitions = computeTransitions(value, epsilonNode, nodes);
                String collect = transitions.stream().map(currNode -> currNode.state()).sorted().collect(Collectors.joining(
                    "-"));
                Node orNewNode = Nodes.getOrNewNode(collect.contains(endState), collect.contains(startState), collect);
                value.setPossibleTransition(orNewNode);
            }
            //
            if (state.node().isStart())
                states.setStartState(state);
            states.add(state);
        }
        return states;
    }

    private Set<Node> computeTransitions (Value value, Node node, Set<Node> nodes)
    {
        // get all transitions for the node
        Set<Node> possibleTransitions = new LinkedHashSet<>();
        String[] values = node.state().split("-");
        for (String splitValue : values) {
            Node nodeWithTransitions = nodes.stream().filter(currNode -> splitValue.equalsIgnoreCase(currNode.state())).collect(
                Collectors.toList()).get(0);
            Set<Node> valueTransitions = nodeWithTransitions.transitions(value.value());
            possibleTransitions.addAll(valueTransitions);
            Set<Node> valueEpsilonTransitions = new HashSet<>();
            for (Node valueTransition : valueTransitions) {
                epsilonNodes(valueEpsilonTransitions, valueTransition);
            }
            possibleTransitions.addAll(valueEpsilonTransitions);
        }
        return possibleTransitions;
    }

    private Set<Node> dfaStates (Automata automata)
    {
        States states = new States();
        Node start = automata.Start();
        states.add(start);
        Set<Node> nodeSet = new HashSet<>();
        traverse(nodeSet, start);
        return nodeSet;
    }

    private void traverse (Set<Node> nodes, Node node)
    {
        if (nodes.contains(node))
            return;
        else {
            nodes.add(node);
            Map<Character, Set<Node>> transitions = node.getTransitions();
            for (Set<Node> valueNodes : transitions.values()) {
                for (Node value : valueNodes) {
                    traverse(nodes, value);
                }
            }
        }
    }

    private String extractValues (String regex)
    {
        StringBuilder builder = new StringBuilder(regex.length());
        char[] chars = regex.toCharArray();
        for (char currChar : chars) {
            if (Character.isAlphabetic(currChar) || Character.isDigit(currChar))
                builder.append(currChar);
        }
        return builder.toString();
    }

    private void epsilonNodes (Set<Node> epsilonNodes, Node node)
    {
        // recursively look for epsilon nodes, stop if that node is already traversed
        Set<Node> epsilonTransitions = node.epsilonTransitions();
        epsilonTransitions = new HashSet<>(epsilonTransitions);
        for (Node epsilonTransition : epsilonTransitions) {
            epsilonNodes(epsilonNodes, epsilonTransition);
        }
        if (!epsilonNodes.contains(node))
            epsilonNodes.add(node);
    }
}
