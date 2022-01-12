## 프록시 패턴 적용
- 기존 템플릿 패턴, 전략 패턴 등을 이용해서 부가기능과 비즈니스 로직, 변하지 않는 부분과 변하는 부분을 분리해냈지만 여전히 비즈니스 로직과 관계없는 기능을 위해 원본코드를 변경해야 했다.
- 부가기능에 해당하는 로그추적기 기능을 프록시 패턴을 이용해서 원본코드의 추가,변경 없이 적용했다.

<br>

### 프록시 패턴 적용 v1 (인터페이스가 있는 구현 클래스에 적용)
- 실제 기능이 있는 객체(실제객체)를 구현체로 만들고 같은 인터페이스를 구현하는 프록시를 구현한다.
- 프록시에서는 로그추적기 기능을 수행하고 실제객체를 호출한다. (반드시 실제 객체에 대한 참조를 갖는다.)
- 클라이언트의 요청은 프록시가 받고 프록시에서 실제객체를 호출하고, 실제객체는 다음 객체의 프록시를 호춣도록 한다.
  - 직접 프록시가 스프링 빈으로 등록되도록 설정하고 실제 객체에 대한 생성도 함께 한다.
- 위 방법은 프록시를 만들고자 하는 인터페이스마다 프록시를 생성해야 한다는 단점이 있다. 프록시에서 수행하고자 하는 기능은 같고 실제 호출하는 객체만 다르더라도 프록시 클래스를 각각 따로 정의해줘야 하기 때문이다.

<br>

### 프록시 패턴 적용 v2 - JDK가 제공하는 동적 프록시 기술 (InvocationHandler)
- 리플렉션을 이용해서 인터페이스마다 프록시를 위한 구현체를 구현해야 하는 문제를 해결한다.
- `InvocationHandler`는 프록시가 수행해야 하는 로직을 정의한다.
```java
Proxy.newProxyInstance(실제객체.getClass().getClassLoader(), new Class[]{인터페이스.class}, invocationHandler);
```
- 실제객체에 대한 메타정보를 이용해서 자동으로 프록시 클래스를 생성할 수 있다. 중복되는 기능의 프록시 클래스를 직접 정의하지 않아도 된다.
- v1과 v2는 인터페이스를 반드시 사용해야 한다는 단점이 있다.


<br>

### 프록시 패턴 적용 v3 - CGLIB가 제공하는 동적 프록시 기술 (MethodInterceptor)
- 이전 방법들의 경우 프록시 생성을 위해 반드시 인터페이스를 필요로 했다. 구체가 클래스만 있는 경우 프록시 클래스 생성이 불가능하다.
- CGLIB는 구체 클래스만으로 동적으로 프록시를 생성할 수 있도록 도와준다.
```java
ConcreteClass concreteClass = new ConcreteClass();
Enhancer enhancer = new Enhancer();
enhancer.setSupplerClass(ConcreteClass.class); // 프록시를 생성할 구체 클래스를 부모클래스로 지정 (상속을 통해 프록시를 생성)
enhancer.setCallback(new TestMethodInterceptor(concreteClass)); // 프록시가 수행할 로직 (MethodInterceptor 를 구현하는 클래스)
ConcreteClass concreteClass = (ConcreteClass) enhancer.create(); // 프록시 생성

concreteService.call(); // 프록시 호출
```


<br>
<br>

***

## 프록시 팩토리

### Spring이 제공하는 동적 프록시 기술 - 프록시 팩토리
- JDK동적프록시, CGLIB동적프록시 두 동적 프록시 라이브러리는 각기 다른 방식으로 사용해야 한다.
  - JDK =>`InvocationHandler`, CGLIB => `MethodInterceptor`
- 스프링이 제공하는 프록시 팩토리를 사용하면 인터페이스가 있는 경우, 구체클래스만 있는 경우를 구분해서 알맞은 방식을 통해 동적으로 프록시를 생성하고 프록시의 로직을 `Advice`에 공통으로 두어 위 문제를 해결한다.
- `Advice` 정의 (`org.aopalliance.intercept.MethodInterceptor` 구현)
```java
public TestAdvice implements MethodInterceptor {
  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable{
        // ==== 프록시 로직 ==== //
        Object result = invocation.proceed();
        // ==== 프록시 로직 ==== //
        return result;
  }
}
```
- 프록시 생성
```java
// 타겟인스턴스를 기반으로 프록시를 생성한다. (인터페이스가 있는 경우 JDK동적프록시를, 구체클래스만 있는 경우 CGLIB를 사용) 
ProxyFactory proxyFactory = new ProxyFactory(타겟인스턴스);
proxyFactory.addAdvice(new TestAdvice()); // Advice 지정 (프록시가 수행할 로직)
Object proxy = proxyFactory.getProxy(); // 프록시 생성
```
- 프록시 팩토리를 이용해서 동적으로 프록시를 생성하는 작업을 상당부분 스프링이 해줘서 많은 부분이 추상화됐다.
- 아직 남아았는 문제는 다음 두 가지이다. 
  - 설정클래스에서 프록시 팩토리로부터 동적으로 프록시를 생성하는 부분에 반복되고 복잡한 코드가 보인다.
    타겟생성 -> 프록시 팩토리 생성, 타겟지정 -> 어드바이저 생성 및 지정 -> 프록시 생성
  - 컴포넌트 스캔에 의해 생성되는 빈에는 프록시 적용이 불가능하다. (직접 등록하는 빈에 대해서만 동적 프록시 생성이 가능하다.)

<br>
<br>

***


