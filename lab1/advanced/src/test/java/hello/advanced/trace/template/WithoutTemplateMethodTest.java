package hello.advanced.trace.template;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class WithoutTemplateMethodTest {

    public class TestA {
        public void logic1() {
            log.info("start");
            long start = System.currentTimeMillis();

            log.info("logic1 processing ...");
            sleep(500);

            long end = System.currentTimeMillis();
            long spendTime = end - start;
            log.info("end, time = {}", spendTime);
        }

        public void logic2() {
            log.info("start");
            long start = System.currentTimeMillis();

            log.info("logic2 processing ...");
            sleep(500);

            long end = System.currentTimeMillis();
            long spendTime = end - start;
            log.info("end, time = {}", spendTime);
        }

        private void sleep(int millis) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    @Test
    void test() {
        TestA testA = new TestA();

        testA.logic1();
        testA.logic2();
    }
}
