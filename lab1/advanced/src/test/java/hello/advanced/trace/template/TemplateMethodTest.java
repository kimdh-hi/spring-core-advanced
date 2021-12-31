package hello.advanced.trace.template;

import org.junit.jupiter.api.Test;

public class TemplateMethodTest {

    @Test
    void test() {
        AbstractTemplate subClass1 = new SubClass1();
        subClass1.execute();

        SubClass2 subClass2 = new SubClass2();
        subClass2.execute();
    }
}
