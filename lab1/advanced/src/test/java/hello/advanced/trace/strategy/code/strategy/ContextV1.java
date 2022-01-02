package hello.advanced.trace.strategy.code.strategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContextV1 {

    private final Strategy strategy;

    public ContextV1(Strategy strategy) {
        this.strategy = strategy;
    }

    public void execute() {
        log.info("start");
        long start = System.currentTimeMillis();

        strategy.call();

        long end = System.currentTimeMillis();
        long spendTime = end - start;
        log.info("end, time = {}", spendTime);
    }
}
