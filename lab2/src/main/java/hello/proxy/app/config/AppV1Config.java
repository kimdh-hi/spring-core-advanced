package hello.proxy.app.config;

import hello.proxy.app.v1.OrderControllerV1;
import hello.proxy.app.v1.OrderControllerV1Impl;
import hello.proxy.app.v1.OrderRepositoryV1Impl;
import hello.proxy.app.v1.OrderServiceV1Impl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppV1Config {

    @Bean
    public OrderControllerV1 orderController() {
        return new OrderControllerV1Impl(orderService());
    }

    @Bean
    public OrderServiceV1Impl orderService() {
        return new OrderServiceV1Impl(orderRepository());
    }

    @Bean
    public OrderRepositoryV1Impl orderRepository() {
        return new OrderRepositoryV1Impl();
    }
}
