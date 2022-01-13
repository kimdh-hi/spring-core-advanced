package hello.proxy.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BasicTest {

    @Test
    void basicConfig() {
        ApplicationContext context = new AnnotationConfigApplicationContext(BeanPostProcessorTestConfig.class);
        B b = context.getBean("a", B.class);
        b.call();

        Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> context.getBean(A.class));
    }


    @Configuration
    static class BeanPostProcessorTestConfig {
        @Bean // A만 빈으로 등록
        public A a() {
            return new A();
        }

        @Bean // 빈후처리기 등록
        public AToBPostProcessor postProcessor() {
            return new AToBPostProcessor();
        }
    }

    @Slf4j
    static class AToBPostProcessor implements BeanPostProcessor {
        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            log.info("bean={}, beanName={}", bean, beanName);

            // 등록되는 빈이 A 타입이라면 B의 객체를 생성해서 등록하도록 처리 (Hooking)
            // 등록되는 빈의 이름은 그대로 'a' 이지만 빈의 실제 객체는 B 타입이 된다.
            if (bean instanceof A) return new B();
            return bean;
        }
    }

    @Slf4j
    static class A {
        public void call() {
            log.info("Test A");
        }
    }

    @Slf4j
    static class B {
        public void call() {
            log.info("Test B");
        }
    }
}
