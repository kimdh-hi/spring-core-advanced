package com.aop.lab3.internalcall.aop;

import com.aop.lab3.internalcall.CallServiceV2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import({CallLogAspect.class})
@Slf4j
@SpringBootTest
class CallServiceV2Test {

    @Autowired
    CallServiceV2 callServiceV2;


    @Test
    void test() {
        callServiceV2.external();
    }
}