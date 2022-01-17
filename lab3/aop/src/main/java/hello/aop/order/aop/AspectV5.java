package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

/**
 * Advice 순서지정 (@Order)
 * - @Order로 순서 지정이 가능하지만 Aspect단위 (클래스 단위)에만 적용 가능하다.
 * - 각 Advice가 정의되는 Aspect 내 메서드에서는 @Order가 적용되지 않는다.
 */
public class AspectV5 {

    @Order(2)
    @Aspect
    @Slf4j
    public static class LogAspect {
        @Around("hello.aop.order.aop.Pointcuts.allOrder()")
        public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[log] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }
    }

    @Order(1)
    @Aspect
    @Slf4j
    public static class TxAspect {
        @Around("hello.aop.order.aop.Pointcuts.orderAndService()")
        public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
            try {
                log.info("[트랜잭션 시작] {}", joinPoint.getSignature());
                Object result = joinPoint.proceed();
                log.info("[트랜잭션 커밋] {}", joinPoint.getSignature());
                return result;
            } catch (Exception ex) {
                log.info("[트랜잭션 롤백] {}", joinPoint.getSignature());
                throw ex;
            } finally {
                log.info("[리소스 릴리즈] {}", joinPoint.getSignature());
            }
        }
    }
}
