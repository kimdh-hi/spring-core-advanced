
### 로그 추적기

#### v1
- Contoller - Service - Reposiotry 각 계층에 도달할 때마다 새로운 TraceId가 생성된다.
- 매번 새로운 TraceId 객체가 생성되기 때문에 같은 요청에 대해 같은 ID값을 가지지 않고 메서드 호출의 깊이 또한 표현하지 못한다.

#### v2
- Controller - Service - Repository 로 이어지는 메서드 호출에서 같은 TraceId를 갖도록 함
- 메서드를 호출할 때 현재 TraceId를 넘겨주는 방식
- 메서드 호출이 Controller부터 시작되지 않는다면? => null을 넘기고 새로운 TraceId를 생성하는 등의 작업이 필요함
- 일단 한 요청이 같은 ID를 갖고 메서드 호출의 깊이를 갖도록 함
- but, 거의 동시에 요청이 왔을 때 TraceId가 겹치고 메서드 깊이가 이어지는 등의 문제가 있음 (동기화 문제)

#### v3
- 필드를 이용해서 동기화를 시도
- 스프링 빈으로 등록되어 싱글톤으로 관리되는 클래스에 필드를 이용해서 TraceId를 동기화 함
- 현재 필드에 TraceId가 있다면 메서드 호출의 깊이(level)을 증가시키고 없다면 새로 생성하는 방식
- 싱글톤의 경우 필드 동기화를 사용할 때 동시성 이슈가 발생할 수 있음
  ++ 외부에서 로깅을 위한 클래스를 빈으로 등록하도록 함. (구현체 변경시 유연하게 변경가능)

#### v4
- ThreadLocal을 이용한 필드 동기화 시 동시성 이슈 해결
- 싱글톤 클래스인 경우 한 필드에 여러 쓰레드가 동시에 접근하더라도 쓰레드마다 독립적인 저장소인 ThreadLocal을 이용해서 동시성 이슈가 발생하지 않도록 함

#### v4 - 메서드 템플릿 적용
- 비즈니스 로직 외 로그추적을 시작하고 죵료하는 등의 부가적인 기능을 위한 코드가 중복됨
- 로깅을 위한 부분을 템플릿으로 만들어서 중복을 제거
```java
@GetMapping("/v4/request")
public String request(String itemId) {

    AbstractTemplate<String> template = new AbstractTemplate<>(trace) {
    @Override
    protected String call() {
          orderService.orderItem(itemId);
              return "ok";
          }
    };
    return template.execute("OrderController.request");
}
```

#### v5 - 콜백 템플릿 적용
- 전략패턴 + 콜백을 적용
- 인터페이스를 사용한 유연한 구조와 람다를 사용한 깔끔한 코드가 됨
```java
@GetMapping("/v5/request")
public String request(String itemId) {

    return template.execute("OrderController.request", () -> {
        orderService.orderItem(itemId);
        return "ok";
    });
}
```