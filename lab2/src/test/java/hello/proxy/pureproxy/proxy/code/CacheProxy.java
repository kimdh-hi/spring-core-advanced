package hello.proxy.pureproxy.proxy.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CacheProxy implements Subject {

    private final RealSubject realSubject;
    private String cacheData;

    public CacheProxy(RealSubject realSubject) {
        this.realSubject = realSubject;
    }

    @Override
    public String operation() {
        log.info("CacheProxy 호출");
        if (cacheData == null) {
            cacheData = realSubject.operation();
        }
        return cacheData;
    }
}
