package hello.advanced.trace.strategy.code.strategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContextV2 {

    public void execute(Strategy strategy) {
        log.info("start");
        long start = System.currentTimeMillis();

        strategy.call();

        long end = System.currentTimeMillis();
        long spendTime = end - start;
        log.info("end, time = {}", spendTime);
    }
}
