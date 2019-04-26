package org.ozzy.sslcontext.config;

import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.ozzy.sslcontext.ssl.Base64TrustingTrustManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("sslcontext")
public class SslcontextConfig {

    private static final Logger LOG = Logger.getLogger(SslcontextConfig.class.getName());

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
                try{
                    SSLContext ctx = SSLContext.getInstance("TLS");
                    Base64TrustingTrustManager tm = new Base64TrustingTrustManager(e.getValue().getTrustedCert());
                    ctx.init(null, new TrustManager[] { tm }, null);
                    LOG.info("Creating ssl beans for id "+e.getKey());
                    beanFactory.registerSingleton(e.getKey(), ctx);
                    beanFactory.registerSingleton(e.getKey()+".factory", ctx.getSocketFactory());
                }catch(Exception ex){
                    LOG.log(Level.SEVERE, "Error creating SSLContxt for context id "+e.getKey(),(Throwable)ex);
                    throw new RuntimeException("Unable to create SSLContext using supplied cert for context "+e.getKey(), ex);
                }
            }
        }else{
            if(enabled){
                System.out.println("Error no ssl contexts are configured.");
            }
        }
    }

}
