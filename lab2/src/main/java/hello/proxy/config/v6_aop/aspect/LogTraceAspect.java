package hello.proxy.config.v6_aop.aspect;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.lang.reflect.Method;

@Slf4j
@Aspect // 프록시 자동 생성기(빈 후처리기)는 @Aspect 를 찾고 이를 토대로 Advisor를 생성 및 빈 등록 기능을 한다.
public class LogTraceAspect {

    private final LogTrace logTrace;

    public LogTraceAspect(LogTrace logTrace) {
        this.logTrace = logTrace;
    }

    /**
     * 포인트컷과 어드바이스를 함께 정의해서 한 개 어드바이저를 생성하고 Bean으로 등록한다.
     * Bean으로 등록된 어드바이저의 포인트컷을 기준으로 프록시를 생성하는 것.
     */
    @Around("execution(* hello.proxy.app..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        TraceStatus status = null;
        try {
            String message = joinPoint.getSignature().toShortString();

            status = logTrace.begin(message);
            Object result = joinPoint.proceed();
            logTrace.end(status);

            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
