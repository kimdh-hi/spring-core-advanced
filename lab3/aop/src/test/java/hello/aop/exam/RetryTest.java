package hello.aop.exam;

import hello.aop.exam.aop.RetryAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(RetryAspect.class)
@Slf4j
@SpringBootTest
public class RetryTest {

    @Autowired
    ExamRepository examRepository;

    @Test
    void test() {
        for (int i=1;i<=5;i++) {
            String result = examRepository.save(String.valueOf(i));
            log.info("result={}, itemId={}", result, i);
        }
    }
}
