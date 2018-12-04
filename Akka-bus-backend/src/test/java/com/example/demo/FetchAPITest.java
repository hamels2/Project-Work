package com.example.demo;

import java.util.concurrent.CompletableFuture;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static akka.pattern.PatternsCS.ask;
import static org.junit.Assert.assertTrue;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import org.json.JSONObject;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FetchAPITest {
	ActorSystem system = ActorSystem.create();
	ActorRef fetchRef = system.actorOf(FetchAPI.props(), "fetchActor");
	@Test
	public void fetchWeatherTest(){ //test getting the weather api
		CompletableFuture<Object> future = ask(fetchRef, "weather", 100000).toCompletableFuture();
		JSONObject weather = (JSONObject)future.join();
		assertTrue(!weather.toString().equals("{unable to get weather info}"));
	}
	@Test
	public void fetchBusTest(){ // test getting the bus api
		CompletableFuture<Object> future = ask(fetchRef, "bus", 100000).toCompletableFuture();
		JSONObject bus = (JSONObject)future.join();
		assertTrue(!bus.toString().equals("{unable to get next bus info for that ID}"));
	}
	@Test
	public void fetchAllTest(){ //test the fetchALL method
		CompletableFuture<Object> future = ask(fetchRef, "1920", 100000).toCompletableFuture();
		String fetch = (String)future.join();
		assertTrue(fetch.contains("Weather")&&fetch.contains("seconds"));
	}

	@Test
	public void fetchAllTest_WrongID(){ // test the fetchALL method with a non existent bus id
		CompletableFuture<Object> future = ask(fetchRef, "ab", 100000).toCompletableFuture();
		String fetch = (String)future.join();
		assertTrue(fetch.contains("Weather")&&fetch.contains("Error"));
	}

}
