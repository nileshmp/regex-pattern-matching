package nilesh.regex;

import org.junit.Assert;
import org.junit.Test;

public class RegexUtilTest
{
    @Test
    public void should_remove_leading_metacharacters()
    {
        String sanitized = RegexUtil.sanitize("+b");
        Assert.assertEquals("b", sanitized);

        sanitized = RegexUtil.sanitize("b+");
        Assert.assertEquals("b+", sanitized);

        sanitized = RegexUtil.sanitize("*+b+");
        Assert.assertEquals("b+", sanitized);

        sanitized = RegexUtil.sanitize("a");
        Assert.assertEquals("a", sanitized);

        sanitized = RegexUtil.sanitize("a|b");
        Assert.assertEquals("a|b", sanitized);
    }

    @Test(expected = RuntimeException.class)
    public void should_throw_exception()
    {
        RegexUtil.sanitize("(a");
    }

    @Test(expected = RuntimeException.class)
    public void should_throw_exception_for_invalid_union_follower()
    {
        RegexUtil.sanitize("a|(*b)");
    }

    @Test
    public void should_extract_text_between_brackets()
    {
        String sanitized = RegexUtil.textBetweenBrackets("(d*e)".toCharArray(), 0);
        Assert.assertEquals("d*e", sanitized);

        sanitized = RegexUtil.textBetweenBrackets("(((d*e)))".toCharArray(), 0);
        Assert.assertEquals("((d*e))", sanitized);

        sanitized = RegexUtil.textBetweenBrackets("(((d*e)))".toCharArray(), 1);
        Assert.assertEquals("(d*e)", sanitized);


        sanitized = RegexUtil.textBetweenBrackets("(((d*e)))".toCharArray(), 2);
        Assert.assertEquals("d*e", sanitized);

        sanitized = RegexUtil.textBetweenBrackets("a*b(((d*e)*f))".toCharArray(), 3);
        Assert.assertEquals("((d*e)*f)", sanitized);
    }
}