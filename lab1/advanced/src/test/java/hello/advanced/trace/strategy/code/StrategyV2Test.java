package hello.advanced.trace.strategy.code;

import hello.advanced.trace.strategy.code.strategy.ContextV2;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic1;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class StrategyV2Test {

    @Test
    void test1() {
        ContextV2 context1 = new ContextV2();
        context1.execute(new StrategyLogic1());
        context1.execute(new StrategyLogic2());
    }

    @Test
    void test2() {
        ContextV2 context1 = new ContextV2();
        context1.execute(() -> log.info("logic1 processing ..."));
        context1.execute(() -> log.info("logic2 processing ..."));
    }
}
