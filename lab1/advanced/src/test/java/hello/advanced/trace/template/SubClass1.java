package hello.advanced.trace.template;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubClass1 extends AbstractTemplate {

    @Override
    protected void call() {
        log.info("logic1 processing ...");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
