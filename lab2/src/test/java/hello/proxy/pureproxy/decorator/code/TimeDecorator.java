package hello.proxy.pureproxy.decorator.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeDecorator implements Component{

    private final Component component;

    public TimeDecorator(Component component) {
        this.component = component;
    }

    @Override
    public String operation() {
          log.info("TimeDecorator 호출");
        long start = System.currentTimeMillis();

        String result = component.operation();

        long end = System.currentTimeMillis();
        log.info("소요시간 = {}ms", end-start);

        return result;
    }
}
