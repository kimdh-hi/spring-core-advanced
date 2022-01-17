package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class AspectV3 {

    @Pointcut("execution(* hello.aop.order..*(..))")
    private void allOrder(){}

    // 클래스 이름이 Service로 끝나는 클래스에 적용
    @Pointcut("execution(* *..*Service.*(..)))")
    private void allService(){}

    // hello.aop.order 패키지 하위 클래스명이 Service로 끝나는 클래스에 적용
    @Around("allOrder() && allService()")
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



    @Around("allOrder()")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[log] {}", joinPoint.getSignature());
        return joinPoint.proceed();
    }
}
