package org.acme;

import java.util.HashMap;
import java.util.Map;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

/**
 * HelloWorldQuarkusTestResourceLifeCicleManager
 */
public class HelloWorldQuarkusTestResourceLifeCicleManager implements QuarkusTestResourceLifecycleManager {

    @Override
    public Map<String, String> start() {
        // TODO Auto-generated method stub
        System.out.println("Se van a ejecutar los Test");
        Map<String,String> conf = new HashMap<>();
        conf.put("greetings.message", "Aloha test");
        return conf;
    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub
        System.out.println("Ya se han ejecutado los Test");
    }

    
}