package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

/**
 * within
 * 선언타입 부분만 매칭조건을 사용
 *
 * 주의 - execution에서 타입부분만 사용 것과 거의 비슷하지만 부모타입으로 매칭할 수 없다는 것에 주의해야 한다. (서브클래스의 메서드라면 반드시 서브클래스 타입으로 매칭해야 한다.)
 */
public class WithinTest {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method method;

    @BeforeEach
    void init() throws NoSuchMethodException {
        method = MemberServiceImpl.class.getMethod("hello", String.class);
    }

    @DisplayName("정확히 한 개 타입과 매칭")
    @Test
    void exactMatch() {
        pointcut.setExpression("within(hello.aop.member.MemberServiceImpl)");
        Assertions.assertThat(pointcut.matches(method, MemberServiceImpl.class)).isTrue();
    }

    @DisplayName("타입이름 패턴매칭")
    @Test
    void typePatternMatch() {
        pointcut.setExpression("within(hello.aop.member.*Service*)");
        Assertions.assertThat(pointcut.matches(method, MemberServiceImpl.class)).isTrue();
    }

    @DisplayName("특정 패키지 이하 경로를 매칭")
    @Test
    void packageMatch() {
        pointcut.setExpression("within(hello.aop..*)");
        Assertions.assertThat(pointcut.matches(method, MemberServiceImpl.class)).isTrue();
    }

    @DisplayName("부모타입으로 매칭 실패 테스트")
    @Test
    void superTypeMatchingFailureTest() {
        pointcut.setExpression("within(hello.aop.member.MemberService)");
        Assertions.assertThat(pointcut.matches(method, MemberServiceImpl.class)).isFalse();
    }

    @DisplayName("부모타입으로 매칭 성공 (execution)")
    @Test
    void superTypeMatchingSuccessTest() {
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
        Assertions.assertThat(pointcut.matches(method, MemberServiceImpl.class)).isTrue();
    }
}
