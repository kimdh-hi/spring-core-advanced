package com.aop.lab3.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CallServiceV0 {

    public void external() {
        log.info("called external!!");
        internal();
    }

    public void internal() {
        log.info("called internal!!");
    }
}
