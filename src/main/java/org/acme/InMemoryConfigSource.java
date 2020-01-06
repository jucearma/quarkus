package org.acme;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.microprofile.config.spi.ConfigSource;

/**
 * InMemoryConfigSource
 */
public class InMemoryConfigSource implements ConfigSource {

    private Map<String, String> prop = new HashMap<>();

    public InMemoryConfigSource(){
        this.prop.put("greetings.message", "Memory Hello");
    }

    /* Le damos una mayor prioridad a esta configuracion que la 
       dada al application.properties
    */
    @Override    
    public int getOrdinal() {
        // TODO Auto-generated method stub
        return 900;
    }
    
    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "InMemoryConfigSource";
    }

    /* Este metodo devuelve todas las propiedades cargadas,
       por ejemplo, si es una base de datos la conexion y obtencion de valores
    */
    @Override
    public Map<String, String> getProperties() {
        // TODO Auto-generated method stub
        return prop;
    }

    @Override
    public String getValue(String propertyName) {
        // TODO Auto-generated method stub
        return prop.get(propertyName);
     }
}