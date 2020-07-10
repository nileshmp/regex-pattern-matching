package nilesh.regex.utils;

import nilesh.regex.Node;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CollectionUtils
{
    public static Set<Node> subsume (Set<Node> values)
    {
        if (values.size() <= 1)
            return values;
        Node[] nodes = new Node[values.size()];
        values.toArray(nodes);
        Set<Node> setNodes = new HashSet<>();
        Set<IndexPair> equalIndexPairs = new HashSet<>();
        for (int count = 0; count < nodes.length; count++) {
            Subsumable subsumable = null;
            for (int innerCount = 0; innerCount < nodes.length; innerCount++) {
                // skip checking the same elements
                if (count == innerCount)
                    continue;
                subsumable = subsume(nodes[count], nodes[innerCount]);
                if (subsumable == Subsumable.Contains)
                    break;
                else if (subsumable == Subsumable.Equal) {
                    equalIndexPairs.add(new IndexPair(count, innerCount));
                    break;
                }
            }
            if (subsumable == Subsumable.DoesNotContain) {
                setNodes.add(nodes[count]);
            }
        }
        for (IndexPair equalIndexPair : equalIndexPairs) {
            setNodes.add(nodes[equalIndexPair.getCount()]);
        }
        return setNodes;
    }

    private static Subsumable subsume (Node first, Node next)
    {
        Set<String> firstStates = new HashSet<>(Arrays.asList(first.state().split("-")));
        Set<String> nextStates = new HashSet<>(Arrays.asList(next.state().split("-")));
        if (nextStates.containsAll(firstStates) && firstStates.containsAll(nextStates))
            return Subsumable.Equal;
        if (nextStates.containsAll(firstStates))
            return Subsumable.Contains;
        return Subsumable.DoesNotContain;
    }
}