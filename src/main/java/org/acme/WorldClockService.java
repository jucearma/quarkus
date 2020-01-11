package org.acme;

import java.util.concurrent.CompletionStage;

import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 * WorldClockService
 */
@Path("/api")
@RegisterRestClient
public interface WorldClockService {

    @GET
    @Path("/json/cet/now")
    @Produces(MediaType.APPLICATION_JSON)
    @ClientHeaderParam(name="X-Logger", value = "DEBUG")
    WorldClock getNow(@BeanParam WorldClockHeaders worldClockHeaders);

    /* Retornar el tiempo del Uso Horario que se esta pasando por parametro 
      Aqui se indca con CompletionStage que se haga la llamada en otro Thread
      y ejecute una accion cuando reciba el resultado
    */
    @GET
    @Path("/json/{where}/now")
    @Produces(MediaType.APPLICATION_JSON)    
    CompletionStage<WorldClock> getNowAsincrono(@PathParam("where") String where);
}