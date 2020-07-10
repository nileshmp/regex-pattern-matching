package nilesh.regex;

import java.util.Collection;
import java.util.LinkedList;

public class Value
{
    private Character character;
    private Collection<Node> transitions;

    public Value (Character character)
    {
        this.character = character;
        transitions = new LinkedList<>();
    }

    public Character value ()
    {
        return this.character;
    }

    public Value setPossibleTransitions (Collection<Node> transitions)
    {
        this.transitions = transitions;
        return this;
    }

    public Value addPossibleTransitions (Collection<Node> transitions)
    {
        this.transitions.addAll(transitions);
        return this;
    }

    public Value setPossibleTransition (Node transition)
    {
        this.transitions.add(transition);
        return this;
    }

    public Collection<Node> possibleTransitions ()
    {
        return this.transitions;
    }
}
