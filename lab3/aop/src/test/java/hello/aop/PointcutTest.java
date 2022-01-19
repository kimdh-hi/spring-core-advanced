package hello.aop;

import hello.aop.test.TestClass;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class PointcutTest {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method method;

    @BeforeEach
    void init() throws NoSuchMethodException {
        method = TestClass.class.getMethod("test", String.class);
    }

    @Test
    void showMethodInfo() {
        log.info("method = {}", method);
    }

    @DisplayName("한 개 메서드와 정확하게 매칭 (조건스킵X)")
    @Test
    void exactMatching() {
        pointcut.setExpression("execution(public String hello.aop.test.TestClass.test(String))");
        assertThat(pointcut.matches(method, TestClass.class)).isTrue();
    }

    @DisplayName("모든 Joinpoint와 매칭")
    @Test
    void allMatching() {
        pointcut.setExpression("execution(* *(..))");
        assertThat(pointcut.matches(method, TestClass.class)).isTrue();
    }

    @DisplayName("선언타입(클래스) 이하 모든 메서드와 매칭")
    @Test
    void declareTypeMatching() {
        pointcut.setExpression("execution(* hello.aop.test.TestClass.*(..))");
        assertThat(pointcut.matches(method, TestClass.class)).isTrue();
    }

    @DisplayName("특정 선언타입(클래스) 이하 메서드 이름 패턴 매칭")
    @Test
    void declareTypeMatching2() {
        pointcut.setExpression("execution(* hello.aop.test.TestClass.*es*(..))");
        assertThat(pointcut.matches(method, TestClass.class)).isTrue();
    }

    @DisplayName("특정 패키지 이하 모든 Jointpoint와 매칭 (실패)")
    @Test
    void packageNameMatchingFailureTest() {
        pointcut.setExpression("execution(* hello.aop.*.*(..))");
        assertThat(pointcut.matches(method, TestClass.class)).isFalse();
    }

    @DisplayName("특정 패키지 이하 모든 Jointpoint와 매칭 (성공)")
    @Test
    void packageNameMatchingTest() {
        pointcut.setExpression("execution(* hello.aop..*.*(..))");
        assertThat(pointcut.matches(method, TestClass.class)).isTrue();
    }


}
