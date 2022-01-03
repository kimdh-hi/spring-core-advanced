package hello.advanced.trace.strategy.code;

import hello.advanced.trace.strategy.code.template.TimeLogTemplate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class TemplateCallbackV1Test {

    @Test
    void test1() {
        TimeLogTemplate template = new TimeLogTemplate();
        template.execute(() -> log.info("1번 로직 실행"));
        template.execute(() -> log.info("2번 로직 실행"));
    }
}
