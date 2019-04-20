package org.ozzy.sslcontext.config;

/**
 * Simple interface for typed bean customizers. 
 * Will only be invoked for beans of type 'getType()'
 */
public interface BeanCustomizer {
    public Class getType();
    public Object postProcessBeforeInit(Object original);
    public Object postProcessAfterInit(Object original);
}