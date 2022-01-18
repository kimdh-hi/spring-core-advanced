package hello.aop.member.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) // 클래스 레벨
@Retention(RetentionPolicy.RUNTIME) // 컴파일 후 런타임까지 어노테이션이 살아? 있도록 (런타임에 동적으로 어노태이션을 사용하기 위함)
public @interface ClassAop {
}
