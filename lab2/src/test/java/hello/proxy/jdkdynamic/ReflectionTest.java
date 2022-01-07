package hello.proxy.jdkdynamic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class ReflectionTest {

    @Test
    void noReflection() {
        Hello hello = new Hello();

        // 메서드를 호출하는 부분 앞뒤로 반복되는 부분이 있음.
        // 반복되는 부분을 모듈화하고자 하는데 중간에 서로 다른 메서드를 호출하는 부분이 문제

        log.info("start");
        String resultA = hello.helloA();
        log.info("end, result = {}", resultA);

        log.info("start");
        String resultB = hello.helloB();
        log.info("end, result = {}", resultB);
    }

    @Test
    void reflection1() throws Exception{
        Class helloClass = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello"); // 클래스 메타정보 획득
        Hello hello = new Hello(); // 메서드가 싫행될 타겟 인스턴스 생성

        log.info("start");
        Method helloA = helloClass.getMethod("helloA"); // 메서드 메타정보 획득
        Object resultA = helloA.invoke(hello); // 메서드 실행 (실행될 타겟 인스턴스를 지정해줘야 함)
        log.info("end, result = {}", resultA);

        log.info("start");
        Method helloB = helloClass.getMethod("helloB");
        Object resultB = helloB.invoke(hello);
        log.info("end, result = {}", resultB);
    }

    @Test
    void reflection2() throws Exception{
        Class helloClass = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");
        Hello hello = new Hello();

        Method helloA = helloClass.getMethod("helloA");
        Method helloB = helloClass.getMethod("helloB");

        dynamicCall(helloA, hello);
        dynamicCall(helloB, hello);
    }

    // 공통부분 모듈화
    private void dynamicCall(Method method, Object target) throws InvocationTargetException, IllegalAccessException {
        log.info("start");
        Object result = method.invoke(target);
        log.info("end, result = {}", result);
    }

    @Slf4j
    static class Hello {
        public String helloA() {
            log.info("helloA 호출");
            return "helloA";
        }
        public String helloB() {
            log.info("helloB 호출");
            return "helloB";
        }

    }
}
