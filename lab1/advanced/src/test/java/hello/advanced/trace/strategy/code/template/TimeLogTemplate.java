package hello.advanced.trace.strategy.code.template;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeLogTemplate {

    public void execute(Callback callback) {
        log.info("start");
        long start = System.currentTimeMillis();

        callback.call();

        long end = System.currentTimeMillis();
        long spendTime = end - start;
        log.info("end, time = {}", spendTime);
    }
}
