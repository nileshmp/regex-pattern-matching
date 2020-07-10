package nilesh.regex.utils;

public class RegexUtil
{
    public static String sanitize (String regex)
    {
        regex = regex.toLowerCase();
        regex = removeLeadingMetacharacters(regex);
        // if the regex is valid, open and close bracket count to be equal
        validEnclosingBrackets(regex);
        validExpression(regex);
        return regex;
    }

    private static void validExpression (String regex)
    {
        // join sign has to be followed by a character (non meta character)
        char[] chars = regex.toCharArray();
        boolean foundUnion = false;
        for (char currChar : chars) {
            if (currChar == '|') {
                foundUnion = true;
            }
            else if (foundUnion && currChar == '(')
                continue;
            else if (foundUnion && (Character.isDigit(currChar) || Character.isAlphabetic(currChar))) {
                break;
            }
            else if (foundUnion && (!(Character.isDigit(currChar) || Character.isAlphabetic(currChar))))
                throw new RuntimeException("Invalid input expression");
        }
    }

    private static String removeLeadingMetacharacters (String regex)
    {
        // remove the starting operators as they are of no use
        char[] chars = regex.toCharArray();
        int count = 0;
        for (char currChar : chars) {
            if (currChar == '*' || currChar == '+' || currChar == '?') {
                count++;
                continue;
            }
            else {
                break;
            }
        }
        regex = regex.substring(count);
        return regex;
    }

    private static void validEnclosingBrackets (String regex)
    {
        char[] chars;
        chars = regex.toCharArray();
        int bracketCount = 0;
        for (char currChar : chars) {
            if (currChar == '(')
                bracketCount++;
            else if (currChar == ')')
                bracketCount--;
        }
        if (bracketCount != 0)
            throw new RuntimeException("Invalid input expression");
    }

    public static String textBetweenBrackets (char[] chars, int start)
    {
        int bracketCount = 0;
        StringBuilder builder = new StringBuilder(chars.length);
        for (int count = start; count < chars.length; count++) {
            if (chars[count] == '(') {
                if (bracketCount != 0) {
                    builder.append(chars[count]);
                }
                bracketCount++;
            }
            else if (chars[count] == ')') {
                bracketCount--;
                if (bracketCount != 0) {
                    builder.append(chars[count]);
                }
            }
            else
                builder.append(chars[count]);
            if (bracketCount == 0)
                break;
        }
        return builder.toString();
    }
}
