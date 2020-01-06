package org.acme;

import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * GreetingService
 */
/* ApplicationScoped es el ciclo de vida como el Singleton
   Por cada ejecucion de la aplicacion hay una unica instancia.
   RequestScoped = Se genera una instancia por cada Request que se genera.
   SessionScoped = Permite tener un Objeto por Session.
*/
@ApplicationScoped
public class GreetingService {

    @ConfigProperty(name = "greetings.message")
    String msg;

    public String toUpperCase() {
        return msg.toUpperCase();
    }
}