package com.aop.lab3.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class CallServiceV0Test {

    @Autowired
    CallServiceV0 callServiceV0;

    @DisplayName("external 호출 테스트")
    @Test
    void externalCallTest() {
        callServiceV0.external();
    }

    @DisplayName("internal 호출 테스트")
    @Test
    void internalCallTest() {
        callServiceV0.internal();
    }
}