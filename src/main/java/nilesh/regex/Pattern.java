package nilesh.regex;

import static nilesh.regex.utils.RegexUtil.sanitize;

public class Pattern
{
    private final DFA DFA;
    private States dfaStates;
    private String regex;
    private Automata rootAutomata;
    private EpsilonNFA epsilonNFA;

    public Pattern (String regex)
    {
        this.regex = regex;
        this.epsilonNFA = new EpsilonNFA();
        this.DFA = new DFA();
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
        Automata automata = epsilonNFA.epsilonNFA(this.regex);
//        System.out.println(automata);
        this.dfaStates = DFA.convertToDFA(automata, regex);
//        System.out.println(dfaStates);
        // minimize DFS
        DFAMinimizer dfaMinimizer = new DFAMinimizer(this.dfaStates);
        automata = dfaMinimizer.minimize();
//        System.out.println(automata);
        return automata;
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
}
