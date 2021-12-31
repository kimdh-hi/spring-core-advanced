package hello.advanced.trace.template;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractTemplate {

    public void execute() {
        log.info("start");
        long start = System.currentTimeMillis();

        call();

        long end = System.currentTimeMillis();
        long spendTime = end - start;
        log.info("end, time = {}", spendTime);
    }

    protected abstract void call();
}
