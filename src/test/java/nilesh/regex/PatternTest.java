package nilesh.regex;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PatternTest
{
    @Test
    public void should_match_one_or_more ()
    {
        String regex = "a+b";
        Pattern pattern = Pattern.Compile(regex);
        boolean matched = pattern.match("ab");
        assertTrue(matched);
        matched = pattern.match("aaaab");
        assertTrue(matched);
        matched = pattern.match("abb");
        assertFalse(matched);
        matched = pattern.match("b");
        assertFalse(matched);
        regex = "a(bc)+";
        pattern = Pattern.Compile(regex);
        matched = pattern.match("abc");
        assertTrue(matched);
        matched = pattern.match("abcbcbcbc");
        assertTrue(matched);
        matched = pattern.match("a");
        assertFalse(matched);
        matched = pattern.match("bc");
        assertFalse(matched);
        matched = pattern.match("ab");
        assertFalse(matched);
    }

    @Test
    public void should_match_zero_or_more ()
    {
        String regex = "a*b";
        Pattern pattern = Pattern.Compile(regex);
        boolean matched = pattern.match("ab");
        assertTrue(matched);
        matched = pattern.match("aaaaab");
        assertTrue(matched);
        matched = pattern.match("abb");
        assertFalse(matched);
        matched = pattern.match("b");
        assertTrue(matched);
    }

    @Test
    public void should_match_zero_or_one ()
    {
        String regex = "a?b";
        Pattern pattern = Pattern.Compile(regex);
        boolean matched = pattern.match("ab");
        assertTrue(matched);
        matched = pattern.match("aaaaab");
        assertFalse(matched);
        matched = pattern.match("abb");
        assertFalse(matched);
        matched = pattern.match("b");
        assertTrue(matched);
        regex = "a?(b+c)*?d?e";
        pattern = Pattern.Compile(regex);
        matched = pattern.match("abcde");
        assertTrue(matched);
        matched = pattern.match("e");
        assertTrue(matched);
        matched = pattern.match("bce");
        assertTrue(matched);
        matched = pattern.match("ce");
        assertFalse(matched);
        matched = pattern.match("aabcde");
        assertFalse(matched);
        regex = "a*b+c?d";
        pattern = Pattern.Compile(regex);
        matched = pattern.match("abcd");
        assertTrue(matched);
        matched = pattern.match("bd");
        assertTrue(matched);
        matched = pattern.match("aaaaabbbbbbd");
        assertTrue(matched);
    }

    @Test
    public void should_match_union ()
    {
        String regex = "a|b";
        Pattern pattern = Pattern.Compile(regex);
        boolean matched = pattern.match("a");
        assertTrue(matched);
        matched = pattern.match("b");
        assertTrue(matched);
        matched = pattern.match("ab");
        assertFalse(matched);
        matched = pattern.match("aabbb");
        assertFalse(matched);
        regex = "a|b*";
        pattern = Pattern.Compile(regex);
        matched = pattern.match("a");
        assertTrue(matched);
        matched = pattern.match("b");
        assertTrue(matched);
        matched = pattern.match("bbbb");
        assertTrue(matched);
        matched = pattern.match("");
        assertTrue(matched);
        regex = "a|b*c";
        pattern = Pattern.Compile(regex);
        matched = pattern.match("ac");
        assertTrue(matched);
        matched = pattern.match("bc");
        assertTrue(matched);
        matched = pattern.match("bbbbc");
        assertTrue(matched);
        matched = pattern.match("aabbb");
        assertFalse(matched);
        matched = pattern.match("abc");
        assertFalse(matched);
        regex = "a|b|c";
        pattern = Pattern.Compile(regex);
        matched = pattern.match("a");
        assertTrue(matched);
        matched = pattern.match("aa");
        assertFalse(matched);
        matched = pattern.match("b");
        assertTrue(matched);
        matched = pattern.match("bb");
        assertFalse(matched);
        matched = pattern.match("c");
        assertTrue(matched);
        matched = pattern.match("cc");
        assertFalse(matched);
        matched = pattern.match("bc");
        assertFalse(matched);
        matched = pattern.match("cb");
        assertFalse(matched);
        matched = pattern.match("ba");
        assertFalse(matched);
        matched = pattern.match("ca");
        assertFalse(matched);
        regex = "(a|b)*c";
        pattern = Pattern.Compile(regex);
        matched = pattern.match("ac");
        assertTrue(matched);
        matched = pattern.match("aaaaaac");
        assertTrue(matched);
        matched = pattern.match("bc");
        assertTrue(matched);
        matched = pattern.match("bbbbbbbc");
        assertTrue(matched);
        matched = pattern.match("c");
        assertTrue(matched);
    }

    @Test
    public void test_able_to_build_FSA_for_One_Or_More_single_character_match_cases ()
    {
        String regex = "b+";
        Pattern pattern = Pattern.Compile(regex);
        boolean matched = pattern.match("bbbb");
        assertTrue(matched);
        matched = pattern.match("b");
        assertTrue(matched);
        regex = "+b";
        pattern = Pattern.Compile(regex);
        matched = pattern.match("b");
        assertTrue(matched);
        matched = pattern.match("bbbb");
        assertFalse(matched);
    }

    @Test
    public void should_match_one_or_more_1 ()
    {
        String regex = "a+b*";
        Pattern pattern = Pattern.Compile(regex);
        boolean matched = pattern.match("ab");
        assertTrue(matched);
        matched = pattern.match("aaaaab");
        assertTrue(matched);
        matched = pattern.match("aaaaabbbbbbb");
        assertTrue(matched);
        matched = pattern.match("abbbbbb");
        assertTrue(matched);
        matched = pattern.match("a");
        assertTrue(matched);
        matched = pattern.match("b");
        assertFalse(matched);
    }

    @Test
    public void test_able_to_build_FSA_for_alphabet_only_expression ()
    {
        String regex = "abcd";
        Pattern pattern = Pattern.Compile(regex);
        boolean matched = pattern.match("bc");
        assertFalse(matched);
        matched = pattern.match("cd");
        assertFalse(matched);
        matched = pattern.match("abc");
        assertFalse(matched);
        matched = pattern.match("abd");
        assertFalse(matched);
        matched = pattern.match("acd");
        assertFalse(matched);
        matched = pattern.match("abcd");
        assertTrue(matched);
    }

    @Test
    public void test_able_to_build_FSA_for_combination_regular_expressions ()
    {
        String regex = "A+B*C";
        Pattern pattern = Pattern.Compile(regex);
        boolean actual = pattern.match("bc");
        assertFalse(actual);
        actual = pattern.match("abc");
        assertTrue(actual);
        actual = pattern.match("aaaabc");
        assertTrue(actual);
        actual = pattern.match("abbbbc");
        assertTrue(actual);
        actual = pattern.match("aaaabbbbc");
        assertTrue(actual);
        actual = pattern.match("c");
        assertFalse(actual);
        actual = pattern.match("b");
        assertFalse(actual);
        actual = pattern.match("bbbbbc");
        assertFalse(actual);
        regex = "abc+d*e";
        pattern = Pattern.Compile(regex);
        ;
        actual = pattern.match("abcde");
        assertTrue(actual);
        actual = pattern.match("abce");
        assertTrue(actual);
        actual = pattern.match("abde");
        assertFalse(actual);
        regex = "abc+(d*e)";
        pattern = Pattern.Compile(regex);
        actual = pattern.match("abcde");
        assertTrue(actual);
        actual = pattern.match("abce");
        assertTrue(actual);
        actual = pattern.match("abde");
        assertFalse(actual);
    }

    @Test
    public void test_able_to_build_FSA_for_expression_containing_brackets ()
    {
        String regex = "(d*e)";
        Pattern pattern = Pattern.Compile(regex);
        boolean actual = pattern.match("dddde");
        assertTrue(actual);
        actual = pattern.match("de");
        assertTrue(actual);
        actual = pattern.match("e");
        assertTrue(actual);
        actual = pattern.match("d");
        assertFalse(actual);
        regex = "(((d*e)))";
        pattern = Pattern.Compile(regex);
        actual = pattern.match("dddde");
        assertTrue(actual);
        actual = pattern.match("de");
        assertTrue(actual);
        actual = pattern.match("e");
        assertTrue(actual);
        actual = pattern.match("d");
        assertFalse(actual);
        regex = "a+(b((d*e)))";
        pattern = Pattern.Compile(regex);
        actual = pattern.match("abe");
        assertTrue(actual);
        actual = pattern.match("abde");
        assertTrue(actual);
        actual = pattern.match("abddde");
        assertTrue(actual);
        actual = pattern.match("aaabddde");
        assertTrue(actual);
        actual = pattern.match("ade");
        assertFalse(actual);
        actual = pattern.match("bde");
        assertFalse(actual);
        regex = "ab(c+d)*e";
        pattern = Pattern.Compile(regex);
        actual = pattern.match("abcde");
        assertTrue(actual);
        actual = pattern.match("abe");
        assertTrue(actual);
        actual = pattern.match("abcccccde");
        assertTrue(actual);
        actual = pattern.match("abccdccccdcde");
        assertTrue(actual);
        actual = pattern.match("abce");
        assertFalse(actual);
        actual = pattern.match("abde");
        assertFalse(actual);
        actual = pattern.match("abcdde");
        assertFalse(actual);
        regex = "ab(c+d)?e";
        pattern = Pattern.Compile(regex);
        actual = pattern.match("abcde");
        assertTrue(actual);
        actual = pattern.match("abe");
        assertTrue(actual);
        actual = pattern.match("abcdcde");
        assertFalse(actual);
        actual = pattern.match("aabcde");
        assertFalse(actual);
        actual = pattern.match("abbcde");
        assertFalse(actual);
        actual = pattern.match("abcd");
        assertFalse(actual);
        actual = pattern.match("abcdee");
        assertFalse(actual);
    }
}