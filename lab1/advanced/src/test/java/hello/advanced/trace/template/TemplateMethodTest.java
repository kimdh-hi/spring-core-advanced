package hello.advanced.trace.template;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class TemplateMethodTest {

    @Test
    void test() {
        AbstractTemplate subClass1 = new SubClass1();
        subClass1.execute();

        SubClass2 subClass2 = new SubClass2();
        subClass2.execute();
    }

    @Test
    void 익명_내부_클래스_사용 () {
        AbstractTemplate template1 = new AbstractTemplate() {
            @Override
            protected void call() {
                log.info("logic1 processing ...");
            }
        };
        template1.execute();

        AbstractTemplate template2 = new AbstractTemplate() {
            @Override
            protected void call() {
                log.info("logic1 processing ...");
            }
        };
        template2.execute();

    }
}
