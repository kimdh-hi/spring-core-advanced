package com.aop.lab3.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InternalCallService {

    public void internal() {
        log.info("called internal!!");
    }

}

