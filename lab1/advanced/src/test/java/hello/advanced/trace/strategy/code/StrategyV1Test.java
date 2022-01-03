package hello.advanced.trace.strategy.code;

import hello.advanced.trace.strategy.code.strategy.ContextV1;
import hello.advanced.trace.strategy.code.strategy.Strategy;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic1;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class StrategyV1Test {

    @Test
    void test1 () {

        ContextV1 context1 = new ContextV1(new StrategyLogic1());
        context1.execute();

        ContextV1 context2 = new ContextV1(new StrategyLogic2());
        context2.execute();

    }

    @Test
    void test2() {

        ContextV1 context1 = new ContextV1(new Strategy() {
            @Override
            public void call() {
                log.info("logic3 processing ...");
            }
        });
        context1.execute();

        ContextV1 context2 = new ContextV1(() -> log.info("logic4 processing ..."));
        context2.execute();
    }
}
