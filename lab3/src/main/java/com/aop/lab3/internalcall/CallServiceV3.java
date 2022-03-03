package com.aop.lab3.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV3 {

    private final InternalCallService internalCallService;

    public CallServiceV3(InternalCallService internalCallService) {
        this.internalCallService = internalCallService;
    }

    public void external() {
        log.info("called external!!");
        internalCallService.internal();
    }

}

