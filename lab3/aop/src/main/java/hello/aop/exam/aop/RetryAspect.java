package hello.aop.exam.aop;

import hello.aop.exam.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@Slf4j
public class RetryAspect {

    @Around("@annotation(retry)")
    public Object doRetry(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable {
        log.info("[Retry] {} retry={}", joinPoint.getSignature(), retry);

        int maxRetry = retry.value();
        Exception exceptionHolder = null;

        for (int retryCnt=1; retryCnt<=maxRetry; retryCnt++) { // 재시도
            try {
                return joinPoint.proceed(); // target 호출시 예외가 없다면 그대로 반환
            } catch (Exception e) {
                exceptionHolder = e; // 예외가 발생했다면 발생한 예외를 보관
            }
        }
        throw exceptionHolder; // 최대 재시도 횟수를 넘어선 경우 예외발생
    }
}
