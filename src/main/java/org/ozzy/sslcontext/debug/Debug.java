package org.ozzy.sslcontext.debug;

import java.util.Map;

import com.mongodb.client.MongoDatabase;

import org.ozzy.sslcontext.config.SslConfig;
import org.ozzy.sslcontext.config.SslcontextConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.stereotype.Component;

@Component
public class Debug implements CommandLineRunner{

    @Autowired
    SslcontextConfig cfg;

    @Autowired
    ApplicationContext ctx;

    @Autowired
    MongoDbFactory f;

    final String configName;
    public Debug(@Value("${sslcontext.chosen}") final String name){
        configName = name;
    }

    private SslConfig getConfig(){
        return ctx.getBean(configName,SslConfig.class);
    }

    @Override
    public void run(String... args) throws Exception {

            System.out.println("Debug dump of ctx config ::");
            Map<String,SslConfig> cfgs = cfg.getContexts();
            if(cfgs==null){
                System.out.println("No configs (null)");
            }else if (cfgs.size()==0){
                System.out.println("No configs");
            }else{
                for(Map.Entry<String,SslConfig> e : cfgs.entrySet()){
                    System.out.println("Name: "+e.getKey());
                    System.out.println("Value: "+e.getValue().getTrustedCert());
                }
            }
        
            System.out.println("Debug dump of chosen ::");
            SslConfig chosen = getConfig();
            System.out.println("Chosen: "+chosen.getTrustedCert());

            System.out.println("Debug dump of mongo ::");
            MongoDatabase md = f.getDb();
            System.out.println(md.getName());
        
    }

}