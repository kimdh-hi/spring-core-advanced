package hello.proxy.jdkdynamic.code;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 동적으로 생성된 프록시가 실행할 로직
 */
@Slf4j
public class TimeInvocationHandler implements InvocationHandler {

    private final Object target;

    public TimeInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("TimeInvocation start");
        long start = System.currentTimeMillis();
        Object result = method.invoke(target);
        long end = System.currentTimeMillis();

        log.info("TimeInvocation end, time = {}", end-start);
        return result;
    }
}
