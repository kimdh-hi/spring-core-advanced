package hello.advanced.trace.strategy.code.strategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StrategyLogic1 implements Strategy{

    @Override
    public void call() {
        // 비즈니스 로직 실행
        log.info("logic1 processing ...");
    }
}
