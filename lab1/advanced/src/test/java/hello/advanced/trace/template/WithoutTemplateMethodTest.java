package hello.advanced.trace.template;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class WithoutTemplateMethodTest {

    public class TestA {
        public void logic() throws InterruptedException {
            log.info("start");
            long start = System.currentTimeMillis();

            log.info("TestA logic1 processing ...");

            long end = System.currentTimeMillis();
            long spendTime = end - start;
            log.info("end, time = {}", spendTime);
        }
    }

    public class TestB {
        public void logic() throws InterruptedException {
            log.info("start");
            long start = System.currentTimeMillis();

            log.info("TestB logic processing ...");

            long end = System.currentTimeMillis();
            long spendTime = end - start;
            log.info("end, time = {}", spendTime);
        }
    }

    @Test
    void test() throws InterruptedException {
        TestA testA = new TestA();
        TestB testB = new TestB();

        testA.logic();
        testB.logic();
    }
}
