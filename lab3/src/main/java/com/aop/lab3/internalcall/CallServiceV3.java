package com.aop.lab3.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV3 {

    private final InternalServiceV3 internalServiceV3;

    public CallServiceV3(InternalServiceV3 internalServiceV3) {
        this.internalServiceV3 = internalServiceV3;
    }

    public void external() {
        log.info("called external!!");
        internalServiceV3.internal();
    }

}
