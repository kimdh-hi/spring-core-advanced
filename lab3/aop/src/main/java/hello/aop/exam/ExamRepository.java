package hello.aop.exam;

import hello.aop.exam.annotation.Retry;
import hello.aop.exam.annotation.Trace;
import org.springframework.stereotype.Repository;

@Repository
public class ExamRepository {

    private static int sequence = 0;

    // 5번째 요청마다 예외를 발생시킨다.
    @Retry
    @Trace
    public String save(String itemId) {
        ++sequence;
        if (sequence%5 == 0) {
            throw new IllegalStateException("예외 발생");
        }
        return "ok";
    }
}
