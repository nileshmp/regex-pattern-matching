package nilesh.regex.utils;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.stream.IntStream;

public class StringUtils
{
//    public static String unique (String state)
//    {
//        Set<Character> values = new HashSet<>();
//        for (Character character : state.toCharArray()) {
//            values.add(character);
//        }
//        return values.stream().map(character -> Character.toString(character)).sorted().collect(Collectors.joining());
//    }

    public static boolean equals (String current, String next)
    {
        IntStream currentSorted = current.chars().sorted();
        IntStream nextSorted = next.chars().sorted();
        return Arrays.equals(currentSorted.toArray(), nextSorted.toArray());
    }

    public static boolean contains (LinkedHashSet<String> endStates, String nextState)
    {
        String[] splits = nextState.split("-");
        for (String split : splits) {
            for (String endState : endStates) {
                if (endState.contains(split))
                    return true;
            }
        }
        return false;
    }
}
