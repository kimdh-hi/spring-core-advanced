package hello.proxy.cglib;


import hello.proxy.cglib.code.TimeMethodInterceptor;
import hello.proxy.common.service.ConcreteService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;

@Slf4j
public class CglibTest {

    @Test
    void cglib() {
        ConcreteService target = new ConcreteService();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(ConcreteService.class); // 프록시를 생성할 대상 구체클래스 설정
        enhancer.setCallback(new TimeMethodInterceptor(target)); // 프록시가 수행할 로직 (MethodInterceptor 를 구현하는 클래스)
        ConcreteService concreteService = (ConcreteService) enhancer.create(); // 프록시 생성

        concreteService.call(); // 프록시 호출
    }
}
