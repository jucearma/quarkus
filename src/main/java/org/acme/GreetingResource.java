package org.acme;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
@Path("/hello")
public class GreetingResource {

    Logger logger = Logger.getLogger(GreetingResource.class);

    @Inject
    GreetingService service;

    @Inject
    @RestClient
    WorldClockService worldClockService;

    

    @POST
    @Path("/createBeer")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createBeer(@Valid Beer beer){
        System.out.println(beer);
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return service.toUpperCase();
    }

    @GET
    @Path("/getBeers")
    @Produces(MediaType.APPLICATION_JSON)
    public Beer getBeer() {
        logger.debug("Hello from log");
        return new Beer("Alhambra",300);
    }

    @GET
    @Path("/getNow")
    @Produces(MediaType.APPLICATION_JSON)
    public WorldClock getNow() {  
        WorldClockHeaders worldClockHeaders = new WorldClockHeaders();
        worldClockHeaders.logger = "DEBUG";      
        return worldClockService.getNow(worldClockHeaders);
    }

    @GET
    @Path("/calculate")
    @Produces(MediaType.APPLICATION_JSON)
    public int calculate() {  
        return 100;
    }

    @GET
    @Path("/getNowNative")
    @Produces(MediaType.APPLICATION_JSON)
    public WorldClock getNowNative() {  
       return ClientBuilder.newClient().target("http://worldclockapi.com")
       .path("/api/json/cet/now")
       .request()
       .get(WorldClock.class);
    }
}