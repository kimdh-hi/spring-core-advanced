package hello.proxy.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 프록시 팩토리에 의해 생성되는 동적프록시에 의해 호출된다.
 * 인터페이스가 있는 경우 JDK 동적프록시에 의해 프록시가 생섣되고 InvocationHandler가 Advice를 호출한다. (proxyTargetClass가 fasle인 경우)
 * 구체클래스 기반으로 생성된 CGLIB 동적 프록시의 경우 MethodHandler가 Advice를 호출한다.
 */
@Slf4j
public class TimeAdvice implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        log.info("TimeInvocation start");
        long start = System.currentTimeMillis();

//        Object result = method.invoke(target, args);
        Object result = invocation.proceed(); // 실제객체 호출

        long end = System.currentTimeMillis();
        log.info("TimeInvocation end, time = {}", end-start);
        return result;
    }
}
