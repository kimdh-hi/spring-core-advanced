package hello.advanced.trace.strategy.code.strategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StrategyLogic2 implements Strategy{

    @Override
    public void call() {
        // 비즈니스 로직 실행
        log.info("logic2 processing ...");
    }
}
