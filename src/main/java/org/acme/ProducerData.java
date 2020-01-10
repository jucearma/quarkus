package org.acme;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.reactivex.Flowable;

/**
 * ProducerData
 */
@ApplicationScoped
 public class ProducerData {

    Random random = new Random();

    //@Outgoing("my-in-memory")
    public Flowable<Integer> generate(){
        /* Por defecto Reactiamente se van a generar cada medio segundo 
            un entero de forma aleatoria y se va enviar al Stream definido
            el cual se denomina my-in-memory.
            Este Stream sera dconsumido por un cliente definido en la clase CunsumerData
        */
        return Flowable.interval(500, TimeUnit.MILLISECONDS)
                        .map(tick -> random.nextInt(100));
    }
    
}