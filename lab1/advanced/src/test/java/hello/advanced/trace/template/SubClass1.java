package hello.advanced.trace.template;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubClass1 extends AbstractTemplate {

    @Override
    protected void call() {
        log.info("logic1 processing ...");
    }
}
