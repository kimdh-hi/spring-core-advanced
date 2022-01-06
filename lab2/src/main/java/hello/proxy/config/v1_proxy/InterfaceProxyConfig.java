package hello.proxy.config.v1_proxy;

import hello.proxy.config.v1_proxy.interface_proxy.OrderControllerV1InterfaceProxy;
import hello.proxy.config.v1_proxy.interface_proxy.OrderRepositoryV1InterfaceProxy;
import hello.proxy.config.v1_proxy.interface_proxy.OrderServiceV1InterfaceProxy;
import hello.proxy.app.v1.*;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 클라이언트의 요청을 proxy가 받도록 proxy를 스프링 빈으로 등록
 * 실제객체는 스프링 빈으로 등록하지 않을 뿐 proxy 빈 생성시 함께 생성하기 때문에 사용 가능함 (스프링 빈으로 등록되지 않는 것 뿐)
 */

@Configuration
public class InterfaceProxyConfig {

    @Bean
    public OrderControllerV1 orderController(LogTrace trace) {
        OrderControllerV1Impl controller = new OrderControllerV1Impl(orderService(trace));

        return new OrderControllerV1InterfaceProxy(controller, trace);
    }

    @Bean
    public OrderServiceV1 orderService(LogTrace trace) {
        OrderServiceV1Impl service = new OrderServiceV1Impl(orderRepository(trace));

        return new OrderServiceV1InterfaceProxy(service, trace);
    }

    @Bean
    public OrderRepositoryV1 orderRepository(LogTrace trace) {
        OrderRepositoryV1Impl orderRepository = new OrderRepositoryV1Impl();

        return new OrderRepositoryV1InterfaceProxy(orderRepository, trace);
    }
}
