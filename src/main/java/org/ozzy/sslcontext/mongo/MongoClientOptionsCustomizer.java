package org.ozzy.sslcontext.mongo;

import com.mongodb.MongoClientOptions;

import org.ozzy.sslcontext.config.BeanCustomizer;
import org.ozzy.sslcontext.ssl.Base64TrustingSocketFactory;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoClientOptionsFactoryBean;
/**
 * Expects cfenv.processor.icdmongo.enabled=true
 *         cfenv.processor.icdmongo.cert=Base64EncodedSSLTrustedCert
 */
@Configuration
@ConditionalOnProperty(name="cfenv.processor.icdmongo.enabled", havingValue="true")
public class MongoClientOptionsCustomizer implements BeanCustomizer {

    @Value("${cfenv.processor.icdmongo.cert}")
    String cert;

    @Override
    public Class getType() {
        return MongoClientOptions.class;
    }

    @Override
    public Object postProcessBeforeInit(Object original) {
        try{
            MongoClientOptions o = (MongoClientOptions)original;
            return MongoClientOptions.builder(o).sslEnabled(true).socketFactory(new Base64TrustingSocketFactory(cert)).build();
        }catch(Exception e){
            throw new FatalBeanException("Unable to add SSL to MongoOptions bean",e);
        }
    }

    @Override
    public Object postProcessAfterInit(Object original) {
        return original;
    }

    //Create default bean to customize if the user code didn't have one.
    @Bean
    @ConditionalOnMissingBean
    public MongoClientOptions mongoClientOptions() {
        try {
            final MongoClientOptionsFactoryBean bean = new MongoClientOptionsFactoryBean();
            bean.afterPropertiesSet();
            MongoClientOptions mco = bean.getObject();
            return mco;
        } catch (final Exception e) {
            throw new BeanCreationException(e.getMessage(), e);
        }
    }

}