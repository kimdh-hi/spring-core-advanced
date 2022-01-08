package hello.proxy.jdkdynamic;

import hello.proxy.jdkdynamic.code.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

@Slf4j
public class JdkDynamicProxyTest {

    @Test
    void jdkDynamic1() {
        AInterface target = new AImpl();
        TimeInvocationHandler invocationHandler = new TimeInvocationHandler(target);

        AInterface proxy = (AInterface) Proxy.newProxyInstance(target.getClass().getClassLoader(), new Class[]{AInterface.class}, invocationHandler);
        proxy.call();
    }

    @Test
    void jdkDynamic2() {
        BInterface target = new BImpl();
        TimeInvocationHandler invocationHandler = new TimeInvocationHandler(target);

        BInterface proxy = (BInterface) Proxy.newProxyInstance(target.getClass().getClassLoader(), new Class[]{BInterface.class}, invocationHandler);
        proxy.call();
    }
}
