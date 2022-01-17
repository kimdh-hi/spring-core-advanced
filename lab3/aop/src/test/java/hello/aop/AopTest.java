package hello.aop;

import hello.aop.order.OrderRepository;
import hello.aop.order.OrderService;
import hello.aop.order.aop.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

//@Import({AspectV1.class})
//@Import({AspectV2.class})
//@Import({AspectV3.class})
//@Import({AspectV4.class})
//@Import({AspectV5.LogAspect.class, AspectV5.TxAspect.class})
@Import({AspectV6.class})
@Slf4j
@SpringBootTest
public class AopTest {

    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    void aopInfo() {
        log.info("isAopProxy orderService={}", AopUtils.isAopProxy(orderService));
        log.info("isAopProxy orderRepository={}", AopUtils.isAopProxy(orderRepository));
    }

    @Test
    void success() {
        orderService.orderItem("hello");
    }

    @Test
    void exception() {
        Assertions.assertThrows(IllegalStateException.class, () -> orderService.orderItem("ex"));
    }
}
