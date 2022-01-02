package hello.advanced.trace.strategy.code;

import hello.advanced.trace.strategy.code.strategy.ContextV1;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic1;
import org.junit.jupiter.api.Test;

public class ContextV1Test {

    @Test
    void strategyV1() {
        StrategyLogic1 strategy1 = new StrategyLogic1();
        ContextV1 contextV1 = new ContextV1(strategy1);

        contextV1.execute();
    }
}
