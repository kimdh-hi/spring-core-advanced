package com.aop.lab3.internalcall;

import com.aop.lab3.internalcall.aop.CallLogAspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@Import({CallLogAspect.class})
@SpringBootTest
class CallServiceV3Test {

    @Autowired
    CallServiceV3 callServiceV3;

    /**
     * 내부 호출이 발생하는 구조 자체를 변경한다. (가장 권장되는 방식)
     * 내부호출이 발생하는 메서드를 별도의 bean으로 관리하여 내부호출이 아닌 외부호출이 되도록 한다.
     *
     * CallServiceV3 -> InternalServiceV3
     */

    @Test
    void test() {
        callServiceV3.external();
    }
}