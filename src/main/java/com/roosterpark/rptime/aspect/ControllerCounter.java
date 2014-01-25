package com.roosterpark.rptime.aspect;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ControllerCounter {
	private static final Logger LOGGER = LoggerFactory.getLogger(ControllerCounter.class);
    private Map<String, AtomicInteger> serviceCalls;
    private Map<String, AtomicInteger> webCalls;
    
    public ControllerCounter() {
        LOGGER.debug("Instantiating ControllerCounter...");
        serviceCalls = new HashMap<>();
        webCalls = new HashMap<>();
    }
    /**
    * A join point is in the web layer if the method is defined
    * in a type in the com.xyz.someapp.web package or any sub-package
    * under that.
    */
    @Pointcut("within(com.roosterpark.rptime.service.*)")
    private void inServiceLayer() {}
    
    @Pointcut("within(com.roosterpark.rptime.web.*)")
    private void inWebLayer() {}
    
    @Before("inServiceLayer()")
    public void countServiceCall(JoinPoint jp) {
        String key = jp.getSignature().toShortString();
        LOGGER.debug("Name {}", key);
        // TODO replace map with memcache (distributed, might be a performance hit)
        if(!serviceCalls.containsKey(key)) {
            serviceCalls.put(key, new AtomicInteger());
        }
        serviceCalls.get(key).incrementAndGet();
    }
    
    @Before("inWebLayer()")
    public void countWebCall(JoinPoint jp) {
        LOGGER.debug("Executing web call, {}", jp);
    }
    
    public Map<String, AtomicInteger> getServiceCalls() {
        return serviceCalls;
    }
    
    public Map<String, AtomicInteger> getWebCalls() {
        return webCalls;
    }
}
