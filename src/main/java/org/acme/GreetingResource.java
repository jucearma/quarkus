package org.acme;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.reactivestreams.Publisher;
import io.reactivex.Flowable;
import io.smallrye.reactive.messaging.annotations.Channel;
import io.smallrye.reactive.messaging.annotations.Emitter;

@Path("/hello")
@RequestScoped
public class GreetingResource {

    Logger logger = Logger.getLogger(GreetingResource.class);

    private AtomicInteger atomicInteger = new AtomicInteger();

    @Inject
    JsonWebToken token;

    @Inject
    @Claim(standard = Claims.sub)
    String sub;

    @Inject
    GreetingService service;

    @Inject
    DeveloperRepository developerRepository;

    @Inject
    @RestClient
    WorldClockService worldClockService;

    @Inject
    EntityManager em;

    @Inject @Channel("generated-temperature")
    Emitter<Integer> emmiter;

    @GET
    @Path("/emit/{dato}")
    public void emit(@PathParam("dato") Integer dato) {
        /* En el metodo GET va a recibir un dato lo toma y lo
            envia al Stream denominado my-in-memory
        */
        emmiter.send(dato);
    }

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/createDeveloper")    
    public Response createDev(@Valid Developer dev){
        em.persist(dev);
        return Response.created(URI.create("/dev/"+dev.getId()))
        .build();
    }

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/createDeveloperPanache")    
    public Response createDevPanache(@Valid DeveloperPanache dev){
        dev.persist();
        return Response.created(URI.create("/dev/"+dev.id))
        .build();
    }

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/createDevRepository")
    public Response createDevRepository(@Valid Developer dev){
        developerRepository.create(dev);
        return Response.created(URI.create("/dev/"+dev.getId()))
        .build();
    }

    @GET
    @Path("/getToken")
    @RolesAllowed("Suscriber")
    @Produces(MediaType.TEXT_PLAIN)
    public String getToken() {
        /* Hora vamos a agregarle RolesAllowed, solamente los usuarios que como grupo en su Token 
           tienen Suscriber son los unicos que pueden acceder a este EndPoint
        */
        return token.getTokenID();
    }

    @GET
    @Path("/getSub")
    @RolesAllowed("Suscriber")
    @Produces(MediaType.TEXT_PLAIN)
    public String getSub() {
        /* Hora vamos a agregarle RolesAllowed, solamente los usuarios que como grupo en su Token 
           tienen Suscriber son los unicos que pueden acceder a este EndPoint
        */
        return sub;
    }

    @GET
    @Path("/getDeveloper/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Developer getDeveloper(@PathParam("id") Integer id) {
        return em.find(Developer.class, id);
    }

    @GET
    @Path("/getDeveloperPanache/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public DeveloperPanache getDeveloperPanache(@PathParam("id") Long id) {
       return DeveloperPanache.findById(id);
    }

    @GET
    @Path("/getDevelopersPanache/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DeveloperPanache> getDevsPanache() {
       return DeveloperPanache.findAll().list();
    }

    @GET
    @Path("/findByNamePanache/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public DeveloperPanache findByName(@NotNull @PathParam("name") String name) {
       return DeveloperPanache.find("name", name).firstResult();
    }

    @GET
    @Path("/findByNameRepository/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Developer findByNameRepository(@NotNull @PathParam("name") String name) {
       return developerRepository.findByName(name);
    }

    @GET
    @Path("/findByNameAgePanache/{name}/{age}")
    @Produces(MediaType.APPLICATION_JSON)
    public DeveloperPanache findByNameAge(@NotNull @PathParam("name") String name, @PathParam("age") Integer age ) {
       return DeveloperPanache.find("name = ?1 and age = ?2", name, age).firstResult();
    }

    /* Con CompletionStage indicamos que cuando llegue una Request crea otro Thread 
        ejecute esa logica y cuando termine devuelve la respuesta al Thread encargado de 
        gestionar la Request.
        Este se basa en RxJava: Reactive Extension for the JVM
    */
    @GET
    @Path("/programacionReactiva")
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<String> programacionReactiva(){
        /* Investigar como contruir Streams en Paralelo y despues hacer Joins de estos */
        return ReactiveStreams.of("h","e","l","l","o")
        .map(String::toUpperCase)// Toma el Stream y Transformalo a Mayuscula
        .limit(2) // Limito el Stream a 2
        .toList() // Luego pasalo a una Lista
        .run() // Y ahora ejecuta estas Operaciones, osea ir iterando en este array y luego transformando a mayuscula
        //.thenApplyAsync( )// Cuando termine este Stream pase algo en otro Stream
        .thenApply(list -> list.toString()); // Luegp cuando termines toma esa Lista y devuelve un List.toString()
    }

    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS) // SERVER_SENT_EVENTS envia eventos concurrentes a los clientes tipo Apache Kafka
    public Publisher<String> streamReactiva(){ // Ir publicando poco a poco eventos
        /* Vamos a crear eventos infinitos que cada medio segundo tiene un contador lo incrementa en 1 lo transforma en String y lo devuelve */
        return Flowable.interval(500,TimeUnit.MILLISECONDS) // Toma en intervalos
        .map(s -> atomicInteger.getAndIncrement()) // Implementamos un contador que nos este retornando numeros incrementalmente
        .map(i -> Integer.toString(i)); // Este entero retornado lo pasamos a String
    }

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
    @Path("/getNowAsincrono")
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<List<WorldClock>> getNowAsincrono() {    
        //WorldClock cet =  worldClockService.getNowAsincrono("cet");
        //WorldClock gmt =  worldClockService.getNowAsincrono("gmt");
        /* En este caso en vez e llevar a cabo los dos llamados uno por uno, mejor se hara un llamado 
           en paralelo y cuando hayan terminado los dos llamados posterior a esto se genera la lista en paralelo        
           Lo que haremos es devoler un CompletionStage y hacer un llamado en paralelo al gmt
           cuando acaban de ejecutarse los dos 
           Entonces, cuando el worldClockService.getNowAsincrono("cet") y el worldClockService.getNowAsincrono("gmt")
           terminen de ejecutarse en paralelo lo que hago es ejecutar en otro Thread la concatenacion
           de los dos y finalmente devolver el resultado al cliente. 
           Como conclusion podemos observar que usando el Microprofile RestClient es muy sencillo empezar
           a tratar de forma asincrona nuestros datos 
           */
        CompletionStage<WorldClock> cet =  worldClockService.getNowAsincrono("cet");
        return cet.thenCombine(worldClockService.getNowAsincrono("gmt"), (cetResult, gmtResult) -> Arrays.asList(cetResult, gmtResult));
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