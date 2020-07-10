package nilesh.regex;

import org.junit.Test;

import java.util.Arrays;

public class DFAMinimizerTest
{
    @Test
    public void test ()
    {
        States states = new States();
        Node node1 = Nodes.newNode(false, true, "1");
        Node node2 = Nodes.newNode(false, "2");
        Node node3 = Nodes.newNode(true, "3");
        Node node4 = Nodes.newNode(false, "4");
        Node node5 = Nodes.newNode(false, "5");
        Node node6 = Nodes.newNode(false, "6");
        Node node7 = Nodes.newNode(false, "7");
        Node node8 = Nodes.newNode(false, "8");
        State state = new State(node1);
        state.SetValue(new Value('a').setPossibleTransitions(Arrays.asList(node2)));
        state.SetValue(new Value('b').setPossibleTransitions(Arrays.asList(node6)));
        states.add(state);
        state = new State(node2);
        state.SetValue(new Value('a').setPossibleTransitions(Arrays.asList(node7)));
        state.SetValue(new Value('b').setPossibleTransitions(Arrays.asList(node3)));
        states.add(state);
        state = new State(node3);
        state.SetValue(new Value('a').setPossibleTransitions(Arrays.asList(node1)));
        state.SetValue(new Value('b').setPossibleTransitions(Arrays.asList(node3)));
        states.add(state);
        state = new State(node5);
        state.SetValue(new Value('a').setPossibleTransitions(Arrays.asList(node8)));
        state.SetValue(new Value('b').setPossibleTransitions(Arrays.asList(node6)));
        states.add(state);
        state = new State(node6);
        state.SetValue(new Value('a').setPossibleTransitions(Arrays.asList(node3)));
        state.SetValue(new Value('b').setPossibleTransitions(Arrays.asList(node7)));
        states.add(state);
        state = new State(node7);
        state.SetValue(new Value('a').setPossibleTransitions(Arrays.asList(node7)));
        state.SetValue(new Value('b').setPossibleTransitions(Arrays.asList(node5)));
        states.add(state);
        state = new State(node8);
        state.SetValue(new Value('a').setPossibleTransitions(Arrays.asList(node7)));
        state.SetValue(new Value('b').setPossibleTransitions(Arrays.asList(node3)));
        states.add(state);
        DFAMinimizer dfaMinimizer = new DFAMinimizer(states);
        Automata automata = dfaMinimizer.minimize();
        System.out.println(automata);
    }
}