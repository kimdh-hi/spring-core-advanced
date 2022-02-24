package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ExecutionTest {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method method;

    @BeforeEach
    void init() throws NoSuchMethodException {
        method = MemberServiceImpl.class.getMethod("hello", String.class);
    }

    @Test
    void printMethod() {
        // public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        log.info("method = {}", method);
    }

    /**
     * AspectJ 포인트컷 문법 (? => 생략가능)
     * execution(접근지시자? 반환타입 선언타입(패키지까지)? 메서드이름(파라미터) 예외?)
     */
    @Test
    void exactMatch() {
        pointcut.setExpression("execution(public String hello.aop.member.MemberServiceImpl.hello(String))");
        assertThat(pointcut.matches(method, MemberServiceImpl.class)).isTrue();
    }

    /**
     * 모든 Joinpoint에 매칭 (스킵가능한 것은 모두 스킵)
     * 스킵불가: 반한타입, 메서드이름, 파라미터
     * 모두 매칭: *
     * 파라미터의 경우 ..을 사용 => 파라미터의 개수에 상관없이 매칭.
     */
    @Test
    void allMatch_allSkip() {
        pointcut.setExpression("execution(* *(..))");
        assertThat(pointcut.matches(method, MemberServiceImpl.class)).isTrue();
    }

    /**
     * 메서드이름 패턴매칭
     * 모든 패키지에서 메서드 이름이 'll'를 포함하는 경우 매칭
     */
    @Test
    void methodNamePatternMatching() {
        pointcut.setExpression("execution(* *ll*(..))");
        assertThat(pointcut.matches(method, MemberServiceImpl.class)).isTrue();
    }

    /**
     * 패키지이름 패턴매칭1
     * hello.aop.member의 모든 스프링 빈과 매칭
     */
    @Test
    void packageNamePatternMatching1() {
        pointcut.setExpression("execution(* hello.aop.member.*.*(..))");
        assertThat(pointcut.matches(method, MemberServiceImpl.class)).isTrue();
    }

    /**
     * 패키지이름 패턴매칭 실패
     * hello.aop 이하 모든 스프링 빈과 매칭하고자 함
     * hello.aop.*.* : 직관적으로 생각했을 때 될 것 같지만 안 된다.
     *                 만약 hello.aop 바로 아래 Spring bean으로 등록된 클래스의 메서드가 있었다면 매칭됐을 것이다.
     */
    @Test
    void packageNamePatternMatchingFalse() throws NoSuchMethodException {
        pointcut.setExpression("execution(* hello.aop.*.*(..))");
        assertThat(pointcut.matches(method, MemberServiceImpl.class)).isFalse();
    }

    /**
     * 패키지이름 패턴매칭2
     * 특정 패키지 이하 패키지의 패키징에 적용하고자 하는 경우
     * hello.aop..*.* : hello.aop 이하 모든 패키이의 모든 메서드와 매칭 (두 번째 *는 메서드에 대한 조건임을 주의)
     */
    @Test
    void packageNamePatternMatching2() {
        pointcut.setExpression("execution(* hello.aop..*.*(..))");
        assertThat(pointcut.matches(method, MemberServiceImpl.class)).isTrue();
    }

    /**
     * 부모타입으로 매칭
     */
    @Test
    void typeMatchSuperType() {
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
        assertThat(pointcut.matches(method, MemberServiceImpl.class)).isTrue();
    }

    /**
     * 부모타입으로 매칭 실패 테스트
     * 부모타입에 있는 메서드만 매칭에 성공
     */
    @Test
    void typeMatchSuperTypeFailureTest() throws NoSuchMethodException {
        Method testMethod = MemberServiceImpl.class.getMethod("internal", String.class);
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
        assertThat(pointcut.matches(testMethod, MemberServiceImpl.class)).isFalse();
    }

    /**
     * 파라미터 매칭
     * 모든 Jointpoint 중 String타입 파라미터 하나를 받는 메서드에 매칭
     */
    @Test
    void parameterMatching() {
        pointcut.setExpression("execution(* *(String))");
        assertThat(pointcut.matches(method, MemberServiceImpl.class)).isTrue();
    }

    /**
     * 파라미터 매칭
     * 모든 Joinpoint 중 파라미터를 받지 않는 메서드에 매칭
     */
    @Test
    void noParameterMatching() {
        pointcut.setExpression("execution(* *())");
        assertThat(pointcut.matches(method, MemberServiceImpl.class)).isFalse();
    }

    /**
     * 파라미터 매칭
     * 타입에 관계없이 하나의 파라미터를 받는 메서드에 매칭
     */
    @Test
    void onlyOneParameterMatching() {
        pointcut.setExpression("execution(* *(*))");
        assertThat(pointcut.matches(method, MemberServiceImpl.class)).isTrue();
    }

    /**
     * 파라미터 매칭
     * 첫 번째 파라미터의 타입은 강제하고 뒤에 오는 파라미터의 타입과 개수에 관계없이 매칭
     */
    @Test
    void parameterMatchingComplex() {
        pointcut.setExpression("execution(* *(String, ..))");
        assertThat(pointcut.matches(method, MemberServiceImpl.class)).isTrue();
    }
}
