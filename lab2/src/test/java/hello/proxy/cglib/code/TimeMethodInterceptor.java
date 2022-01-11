package hello.proxy.cglib.code;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

@Slf4j
public class TimeMethodInterceptor implements MethodInterceptor {

    private final Object target;

    public TimeMethodInterceptor(Object target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        log.info("TimeInvocation start");
        long start = System.currentTimeMillis();

        Object result = methodProxy.invoke(target, args); // Method 보다는 MethodProxy 를 사용하는 것이 성능상 유리

        long end = System.currentTimeMillis();

        log.info("TimeInvocation end, time = {}", end-start);
        return result;
    }
}
