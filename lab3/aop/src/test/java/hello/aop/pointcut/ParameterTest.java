package hello.aop.pointcut;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import hello.aop.member.annotation.MethodAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(ParameterTest.ParameterAspect.class)
@SpringBootTest
@Slf4j
public class ParameterTest {

    @Autowired
    MemberService memberService;

    @Test
    void call() {
        log.info("MemberService Proxy = {}", memberService.getClass());
        memberService.hello("hello");
    }

    @Slf4j
    @Aspect
    static class ParameterAspect {

        @Pointcut("execution(* hello.aop.member..*.*(..))")
        private void allMember(){}

        @Around("allMember()")
        public Object advice1(ProceedingJoinPoint joinPoint) throws Throwable {
            Object arg = joinPoint.getArgs()[0];
            log.info("[logArgs1] {} arg={}", joinPoint.getSignature(), arg);
            return joinPoint.proceed();
        }

        @Around("allMember() && args(arg, ..)")
        public Object advice2(ProceedingJoinPoint joinPoint, Object arg) throws Throwable {
            log.info("[logArgs2] {} arg={}", joinPoint.getSignature(), arg);
            return joinPoint.proceed();
        }

        @Before("allMember() && args(arg, ..)")
        public void advice3(JoinPoint joinPoint, String arg) throws Throwable {
            log.info("[logArgs3] {} arg={}", joinPoint.getSignature(), arg);
        }

        /**
         * this
         * @param obj : 스프링 컨테이너에 등록된 객체 (프록시객체)를 받는다.
         */
        @Before("allMember() && this(obj)")
        public void argsThis(JoinPoint joinPoint, MemberService obj) {
            log.info("[argsThis] {} arg={}", joinPoint.getSignature(), obj.getClass());
        }

        /**
         * target
         * @param obj : 실제객체를 받는다.
         */
        @Before("allMember() && target(obj)")
        public void argsTarget(JoinPoint joinPoint, MemberService obj) {
            log.info("[argsTarget] {} arg={}", joinPoint.getSignature(), obj.getClass());
        }

        /**
         * 파라미터에 어노테이션이 있는 경우 해당 어노테이션의 값에 접근
         */
        @Before("allMember() && @annotation(annotation)")
        public void argsAnnotation(JoinPoint joinPoint, MethodAop annotation) {
            log.info("[argsAnnotation] {} annotationValue={}", joinPoint.getSignature(), annotation.value());
        }

    }
}
