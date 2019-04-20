package org.ozzy.sslcontext.config;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("sslcontext")
public class SslcontextConfig {

    private Map<String, SslConfig> contexts;
    private boolean enabled;

    public Map<String, SslConfig> getContexts() {
        return contexts;
    }

    public void setContexts(Map<String, SslConfig> contexts) {
        this.contexts = contexts;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    } 

    @Autowired
    private ConfigurableBeanFactory beanFactory;

    @PostConstruct
    public void init() {
        if(enabled && contexts!=null){
            for (Map.Entry<String, SslConfig> e : contexts.entrySet()) {
                //Create ssl context from SslConfig, and publish as bean with name e.getKey()
                beanFactory.registerSingleton(e.getKey(), e.getValue());
            }
        }else{
            if(enabled){
                System.out.println("Error no ssl contexts are configured.");
            }
        }
    }

}
