package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpEntities;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import static akka.pattern.PatternsCS.ask;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.apache.http.client.*;
@SpringBootApplication
public class Server extends AllDirectives {
	
	public static void main(String[] args) throws IOException {
		SpringApplication.run(Server.class, args);
		ActorSystem system = ActorSystem.create();
		ActorRef fetchRef = system.actorOf(FetchAPI.props(), "fetchActor");
		final Server app = new Server();

		final Http http = Http.get(system);
		final ActorMaterializer materializer = ActorMaterializer.create(system);

		final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = app.createRoute(system, fetchRef).flow(system, materializer);
		final CompletionStage<ServerBinding> binding = http.bindAndHandle(routeFlow, ConnectHttp.toHost("localhost", 8080), materializer);

		System.out.println("Type RETURN to exit");
		System.in.read();
		
		binding
		.thenCompose(ServerBinding::unbind)
		.thenAccept(unbound -> system.terminate());
	}
	public Route createRoute(ActorSystem system, ActorRef fetchRef) {
		
		
		// This handler generates responses to `/bus?name=XXX` requests
		Route busRoute =
		  parameter("id", id -> {
				CompletableFuture<Object> future = ask(fetchRef, 
				id, 100000).toCompletableFuture();
				String s = (String)future.join();
				
			return complete(s);
		  });
	
		return
	
		  // only handle GET requests
		  get(() -> route(
			// matches the empty path
			pathSingleSlash(() ->
			  complete(HttpEntities.create(ContentTypes.TEXT_HTML_UTF8, "<html><body>please provide a bus id</body></html>"))
			),
			path("bus", () ->
			  // uses the route defined above
			  busRoute
			)
		  ));
	  }
	
}
