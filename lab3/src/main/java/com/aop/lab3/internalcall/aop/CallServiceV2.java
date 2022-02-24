package com.aop.lab3.internalcall.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV2 {

    private final ApplicationContext applicationContext;

    public CallServiceV2(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void external() {
        log.info("called external!!");

        CallServiceV2 bean = applicationContext.getBean(CallServiceV2.class);
        bean.internal();
    }

    public void internal() {
        log.info("called internal!!");
    }
}
