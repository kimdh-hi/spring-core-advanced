package hello.advanced.trace.threadlocal.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FieldService {
    private String store;

    public String logic(String name) {
        log.info("저장 name={} -> store={}", name, store);
        this.store = name;
        sleep(1000); // 저장하는데 1초가 경과한다고 가정 (1초를 sleep 하는 동안 다른 쓰레드를 끼워넣어서 동시성 이슈가 발생하는지 확인하기 위함)

        log.info("조회 store={}", store);
        return store;
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
