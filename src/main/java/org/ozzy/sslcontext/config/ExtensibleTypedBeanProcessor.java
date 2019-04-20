package org.ozzy.sslcontext.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Simple attempt to avoid having every bean post processor processe every bean.
 * Instead, this one will only process beans it knows it has BeanCustomizers for.
 */
class TypedBeanPostProcessorImpl implements BeanPostProcessor{

    Map<String, BeanCustomizer> customizers;
    public TypedBeanPostProcessorImpl(List<BeanCustomizer> customizerList){
        customizers = new HashMap<>();
        customizerList.stream().forEach(bc -> customizers.put(bc.getType().getCanonicalName(), bc));
    }

    public Object postProcessAfterInitialization(Object bean, String beanName)
    throws BeansException {
        String key = bean.getClass().getCanonicalName();
        if(customizers.containsKey(key)){
            bean = customizers.get(key).postProcessAfterInit(bean);
        }
        return bean;
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName)
        throws BeansException {
        String key = bean.getClass().getCanonicalName();
        if(customizers.containsKey(key)){
            bean = customizers.get(key).postProcessBeforeInit(bean);
        }
        return bean;
    }
}

@Configuration
public class ExtensibleTypedBeanProcessor {
    @Bean
    public BeanPostProcessor getBpp(List<BeanCustomizer> customizerList){
        return new TypedBeanPostProcessorImpl(customizerList);
    }
}