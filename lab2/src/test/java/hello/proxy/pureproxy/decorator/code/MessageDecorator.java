package hello.proxy.pureproxy.decorator.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageDecorator implements Component{

    private final RealComponent realComponent;

    public MessageDecorator(RealComponent realComponent) {
        this.realComponent = realComponent;
    }

    @Override
    public String operation() {
        log.info("MessageDecorator 호출");
        String result = realComponent.operation();
        String decorateResult = "****" + result + "****"; // 리턴값에 대한 데코레이팅

        return decorateResult;
    }
}
