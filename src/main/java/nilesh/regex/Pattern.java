package nilesh.regex;

import java.util.Stack;

import static nilesh.regex.RegexUtil.sanitize;
import static nilesh.regex.RegexUtil.textBetweenBrackets;

public class Pattern
{
    private String regex;
    private Automata rootAutomata;

    public Pattern (String regex)
    {
        this.regex = regex;
    }

    public static Pattern Compile (String regex)
    {
        Pattern pattern = new Pattern(regex);
        pattern.rootAutomata = pattern.compile();
        return pattern;
    }

    private Automata compile ()
    {
        this.regex = sanitize(this.regex);
        return NFA(this.regex);
    }

    private Automata NFA (String regex)
    {
        int unionOccurrence = 0;
        Stack<Automata> stack = new Stack();
        char[] chars = regex.toCharArray();
        for (int count = 0; count < chars.length; count++) {
            char currChar = chars[count];
            if (currChar == '*') {
                stack.push(zeroOrMore(stack.pop()));
            }
            else if (currChar == '+') {
                stack.push(OneOrMore(stack.pop()));
            }
            else if (currChar == '?') {
                stack.push(zeroOrOne(stack.pop()));
            }
            else if (currChar == '|') {
                // do nothing just mark the occurrence
                if(unionOccurrence > 0)
                {
                    union(stack);
                }
                unionOccurrence = stack.size();
            }
            else if (currChar == '(') {
                // read ahead till the end of bracket and call NFA
                String expression = textBetweenBrackets(chars, count);
                count += expression.length() + 1;
                stack.push(NFA(expression));
            }
            else {
                stack.push(fromChar(currChar));
            }

            // handles cases like a|b, d|f...
            if ((unionOccurrence > 0) && (stack.size() == unionOccurrence + 1) && isEndOf(count, chars)) {
                union(stack);
            }

            // handles cases like a|b*, a|b*c
            if ((unionOccurrence > 0) && (stack.size() == unionOccurrence + 2))
            {
                Automata top = stack.pop();
                union(stack);
                stack.push(top);
            }
        }
        return concat(stack);
    }

    private void union (Stack<Automata> stack)
    {
        Automata second = stack.pop();
        Automata first = stack.pop();
        stack.push(union(first, second));
    }

    private boolean isEndOf (int count, char[] chars)
    {
        return count == chars.length-1;
    }

    public boolean match (String language)
    {
        Node start = this.rootAutomata.Start();
        char[] chars = language.toCharArray();
        Node currNode = start;
        for (char currChar : chars) {
            currNode = currNode.nextTransition(currChar);
            if (currNode == null)
                return false;
        }
        return currNode.canTransitionToEnd();
    }

    private Automata fromChar (char currChar)
    {
        Node start = new Node(false);
        Node end = new Node(true);
        start.transition(end, currChar);
        return new Automata(start, end);
    }

    private Node fromEpsilon (boolean isEnd)
    {
        return new Node(isEnd);
    }

    private Automata zeroOrMore (Automata automata)
    {
        Node epsilonStart = fromEpsilon(false);
        epsilonStart.transition(automata.Start(), '\0');
        Node epsilonEnd = fromEpsilon(true);
        automata.End().transition(epsilonEnd, '\0');
        automata.End().End(false);
        // link the epsilon nodes
        epsilonStart.transition(epsilonEnd, '\0');
        automata.End().transition(automata.Start(), '\0');

        return new Automata(epsilonStart, epsilonEnd);
    }

    private Automata OneOrMore (Automata automata)
    {
        Node epsilonStart = fromEpsilon(false);
        epsilonStart.transition(automata.Start(), '\0');
        Node epsilonEnd = fromEpsilon(true);
        automata.End().transition(epsilonEnd, '\0');
        automata.End().End(false);
        // link the epsilon nodes
        automata.End().transition(automata.Start(), '\0');

        return new Automata(epsilonStart, epsilonEnd);
    }

    private Automata zeroOrOne (Automata automata)
    {
        Node epsilonStart = fromEpsilon(false);
        epsilonStart.transition(automata.Start(), '\0');
        Node epsilonEnd = fromEpsilon(true);
        automata.End().transition(epsilonEnd, '\0');
        automata.End().End(false);
        // link the epsilon nodes
        epsilonStart.transition(epsilonEnd, '\0');

        return new Automata(epsilonStart, epsilonEnd);
    }

    private Automata union (Automata first, Automata second)
    {
        Node epsilonStart = fromEpsilon(false);

        epsilonStart.transition(first.Start(), '\0');
        epsilonStart.transition(second.Start(), '\0');

        Node epsilonEnd = fromEpsilon(true);

        first.End().transition(epsilonEnd, '\0');
        second.End().transition(epsilonEnd, '\0');

        first.End().End(false);
        second.End().End(false);
        return new Automata(epsilonStart, epsilonEnd);
    }

    private Automata concat (Stack<Automata> stack)
    {
        while (stack.size() > 1) {
            Automata end = stack.pop();
            Automata start = stack.pop();

            start.End().transition(end.Start(), '\0');
            start.End().End(false);
            stack.push(new Automata(start.Start(), end.End()));
        }
        return stack.pop();
    }
}
