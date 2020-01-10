package org.acme;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;

/**
 * ConsumerData
 */
@ApplicationScoped
public class ConsumerData {

    /* tempreature es el Stream que voy a recibir cuando algo dentro de Kafka
       El Emmiter va a poner una temperatura dentro de Kafka y este Consumer 
       Asincronamente detecta este cambio dentro de Kafka y lo recoge e imprime por consola o lo que deseemos
    */

    @Incoming("temperature") // Este metodo se va a ajecutar cuando se reciba un mensaje del Stream my-in-memory
    public void printRandom(int randomNumber){
        /* Como se sabe que el productor genera enteros entonces esto se recibira como parametro */
        /* Podriamos hacer un server site y enviarlo al cliente */
        System.out.println(randomNumber);

    }
}