package org.ozzy.sslcontext.config;

public class SslConfig {
    private String trustedCert;

    public String getTrustedCert() {
        return trustedCert;
    }

    public void setTrustedCert(String cert) {
        this.trustedCert = cert;
    }
}