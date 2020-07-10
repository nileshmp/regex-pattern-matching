package nilesh.regex;

import java.util.Stack;

import static nilesh.regex.Nodes.fromEpsilon;
import static nilesh.regex.utils.AutomataUtil.fromChar;
import static nilesh.regex.utils.RegexUtil.textBetweenBrackets;

public class EpsilonNFA
{
    public Automata epsilonNFA (String regex)
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
                if (unionOccurrence > 0) {
                    join(stack);
                }
                unionOccurrence = stack.size();
            }
            else if (currChar == '(') {
                // read ahead till the end of bracket and call epsilonNFA
                String expression = textBetweenBrackets(chars, count);
                count += expression.length() + 1;
                stack.push(epsilonNFA(expression));
            }
            else {
                stack.push(fromChar(currChar));
            }
            // handles cases like a|b, d|f...
            if ((unionOccurrence > 0) && (stack.size() == unionOccurrence + 1) && isEndOf(count, chars)) {
                join(stack);
            }
            // handles cases like a|b*, a|b*c
            if ((unionOccurrence > 0) && (stack.size() == unionOccurrence + 2)) {
                Automata top = stack.pop();
                join(stack);
                stack.push(top);
            }
        }
        return link(stack);
    }

    private void join (Stack<Automata> stack)
    {
        Automata second = stack.pop();
        Automata first = stack.pop();
        stack.push(first.join(second));
    }

    private boolean isEndOf (int count, char[] chars)
    {
        return count == chars.length - 1;
    }

    private Automata zeroOrMore (Automata automata)
    {
        Node epsilonStart = fromEpsilon(false);
        epsilonStart.transition('\0', automata.Start());
        Node epsilonEnd = fromEpsilon(true);
        automata.End().transition('\0', epsilonEnd);
        automata.End().setEnd(false);
        // link the epsilon nodes
        epsilonStart.transition('\0', epsilonEnd);
        automata.End().transition('\0', automata.Start());
        return new Automata(epsilonStart, epsilonEnd);
    }

    private Automata OneOrMore (Automata automata)
    {
        Node epsilonStart = fromEpsilon(false);
        epsilonStart.transition('\0', automata.Start());
        Node epsilonEnd = fromEpsilon(true);
        automata.End().transition('\0', epsilonEnd);
        automata.End().setEnd(false);
        // link the epsilon nodes
        automata.End().transition('\0', automata.Start());
        return new Automata(epsilonStart, epsilonEnd);
    }

    private Automata zeroOrOne (Automata automata)
    {
        Node epsilonStart = fromEpsilon(false);
        epsilonStart.transition('\0', automata.Start());
        Node epsilonEnd = fromEpsilon(true);
        automata.End().transition('\0', epsilonEnd);
        automata.End().setEnd(false);
        // link the epsilon nodes
        epsilonStart.transition('\0', epsilonEnd);
        return new Automata(epsilonStart, epsilonEnd);
    }

    private Automata link (Stack<Automata> stack)
    {
        while (stack.size() > 1) {
            Automata end = stack.pop();
            Automata start = stack.pop();
            Node endStartNode = end.Start();
            start.End().transition('\0', endStartNode);
            endStartNode.setStartNode(false);
            start.End().setEnd(false);
            stack.push(new Automata(start.Start(), end.End()));
        }
        Automata automata = stack.pop();
        automata.Start().setStartNode(true);
        return automata;
    }
}
