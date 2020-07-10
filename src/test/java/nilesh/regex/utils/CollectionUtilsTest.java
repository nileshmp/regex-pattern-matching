package nilesh.regex.utils;

import nilesh.regex.Node;
import nilesh.regex.Nodes;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class CollectionUtilsTest
{
    @Test
    public void test_if_set_subsumeable ()
    {
        Set<Node> nodes = new HashSet<>();
        nodes.add(Nodes.newNode(false, "1"));
        nodes.add(Nodes.newNode(false, "2"));
        nodes.add(Nodes.newNode(false, "3"));
        nodes.add(Nodes.newNode(false, "1-2"));
        nodes.add(Nodes.newNode(false, "3-2"));
        nodes.add(Nodes.newNode(false, "4-3-2-1"));
        nodes.add(Nodes.newNode(false, "4"));
        Node node5 = Nodes.newNode(false, "5");
        nodes.add(node5);
        Node node1234 = Nodes.newNode(false, "1-2-3-4");
        nodes.add(node1234);
        Set<Node> subsume = CollectionUtils.subsume(nodes);
        subsume.stream().forEach(node -> System.out.println(String.format(
            "state: %s, hash: %s",
            node.state(),
            node.hashCode())));
        Assert.assertEquals(2, subsume.size());
        Assert.assertTrue(subsume.contains(node1234));
        Assert.assertTrue(subsume.contains(node5));
    }
}