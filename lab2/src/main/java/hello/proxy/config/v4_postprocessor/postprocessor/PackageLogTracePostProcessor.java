package hello.proxy.config.v4_postprocessor.postprocessor;

import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class PackageLogTracePostProcessor implements BeanPostProcessor {

   private final String basePackage;
   private final Advisor advisor;

    public PackageLogTracePostProcessor(String basePackage, Advisor advisor) {
        this.basePackage = basePackage;
        this.advisor = advisor;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        String packageName = bean.getClass().getPackageName();
        if (!packageName.startsWith(basePackage)) return bean;

        ProxyFactory proxyFactory = new ProxyFactory(bean);
        proxyFactory.addAdvisor(advisor);
        return proxyFactory.getProxy();
    }
}
