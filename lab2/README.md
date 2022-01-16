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

***

<br>

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
  - **컴포넌트 스캔**에 의해 생성되는 빈에는 프록시 적용이 불가능하다. (직접 등록하는 빈에 대해서만 프록시가 빈을 등록되도록 설정 가능하다.)

<br>

***

<br>

## 빈 후처리기 BeanPostProcessor
- 컴포넌트 스캔에 의해 등록되는 빈에 대해서는 프록시를 적용할 수 없었다.
- 등록되는 모든 빈에 대해서 조작이 가능한 `BeanPostProcessor`를 이용해서 컴포넌트 스캔에 의해 등록되는 빈에도 프록시를 적용한다.
- `BeanPostProcessor` 인터페이스를 구현하면 스프링 컨테이너에 등록될 빈 객체와 빈의 이름을 받는 메서드를 오버라이딩 할 수 있다.
```java
@Nullable
default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    return bean;
}

default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    return bean;
}
```
- `bean`이 프록시 적용 대상이라면 `ProxyFactory`를 이용해서 프록시를 생성하고 생성된 프록시를 리턴한다.
```java
public class PackageLogTracePostProcessor implements BeanPostProcessor {

   private final Advisor advisor;

    public PackageLogTracePostProcessor(String basePackage, Advisor advisor) {
        this.advisor = advisor;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        ProxyFactory proxyFactory = new ProxyFactory(bean);
        proxyFactory.addAdvisor(advisor);
        return proxyFactory.getProxy();
    }
}
```
- 구현한 `BeanPostProcessor`를 스프링 컨테이너에 올려주면 등록되는 모든 빈에 대해서 적용 가능하다.
- Springboot가 올리는 빈에도 모두 적용되기 때문에 특정 패키지나 클래스로 적용범위를 제한하고 사용해야 한다.

++ 이전에는 프록시를 적용하고자 하는 Bean마다 설정 클래스에서 실제객체 대신 프록시를 반환하도록 설정해줘야 했다. 이제는 프록시를 생성하는 부분을 설정클래스가 아닌 빈후처리기에 맡기므로 설정클래스에 프록시 생성을 위한 반복되는 코드가 사라졌다.

<br>

## AnnotationAwareAspectJAutoProxyCreator 자동 프록시 생성기
- 빈 후처리기를 직접 만들어서 Bean으로 등록함으로 컴포넌트 스캔 대상 클래스에도 프록시를 적용할 수 있게 되었다.
- 빈 후처리기는 생성되는 모든 Bean에 적용되기 때문에 명시적으로 제한을 해줘야 한다. 예제의 경우 패키지로 범위를 제한했다.
- 이는 조금 중복된 작업이 될 수 있다. 빈 후처리기에서 프록시를 생성할 때 `Advisor` 를 생성하는데 여기에는 `Pointcut`과 `Advice`가 포함된다. 사실 이 두 객체만 있다면 이 빈 후처리기가 어디에 적용되어 대신 프록시를 생성하고 반환하며 어떤 기능을 수행할 지 알 수 있다.
- Spring 은 이 작업을 자동으로 제공한다. Bean으로 등록된 `Advisor`에 해당하는 프록시를 자동으로 만들어준다. 

의존성 추가
```groovy
implementation 'org.springframework.boot:spring-boot-starter-aop'
```
Advisor 등록
```java
/**
 * Advisor의 Pointcut을 기반으로 프록시 생성 여부를 판단하고 프록시를 생성해서 등록한다.
 */
@Bean
public Advisor advisor1(LogTrace logTrace) {
    NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
    pointcut.setMappedNames("request*", "order*", "orderItem*");

    LogTraceAdvice advice = new LogTraceAdvice(logTrace);
    return new DefaultPointcutAdvisor(pointcut, advice);
}
```

<br>

## @Aspect AOP 적용
- 이전까지 직접 정의한 `Pointcut`과 `Advice`로 `Advisor`를 만들어서 Bean으로 등록하는 것으로 프록시 생성을 처리했다.
- 이제 실무에서 사용하는 어노테이션 기반 방법을 적용해보자. (`@Aspect`)
- `@Aspect` 를 클래스 레벨에 붙여준다.
  - 프록시 자동 생성기(빈 후처리기)는 `@Aspect`를 찾고 해당 클래스의 포인트컷, 어드바이스 정보로 어드바이저를 생성하고 Bean으로 등록한다.
- `@Aspect` 가 붙은 클래스에서 포인트컷과 어드바이스에 대한 정의를 한다.
```java
@Aspect // 프록시 자동 생성기(빈 후처리기)는 @Aspect 를 찾고 이를 토대로 Advisor를 생성 및 빈 등록 기능을 한다.
public class LogTraceAspect {
    
  @Around("execution(* hello.proxy.app..*(..))") // 포인트컷
  public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
    // ==== Advice 로직 ==== // 

    // .... 프록시 부가기능 .... //  
    Object result = joinPoint.proceed(); // 실제객체 호출
    // .... 프록시 부가기능 .... //  

    return result;
  }
}
```