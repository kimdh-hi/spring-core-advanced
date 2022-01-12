package hello.proxy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.PatternMatchUtils;

public class TempTest {

    @Test
    void test1() {
        String[] patterns = {"/test*"};
        final String tc1 = "/test";
        final String tc2 = "/test/test1";
        final String tc3 = "/wrong";
        final String tc4 = "/wrong/wrong1";

       Assertions.assertTrue(PatternMatchUtils.simpleMatch(patterns, tc1));
       Assertions.assertTrue(PatternMatchUtils.simpleMatch(patterns, tc2));
       Assertions.assertFalse(PatternMatchUtils.simpleMatch(patterns, tc3));
       Assertions.assertFalse(PatternMatchUtils.simpleMatch(patterns, tc4));
    }
}
