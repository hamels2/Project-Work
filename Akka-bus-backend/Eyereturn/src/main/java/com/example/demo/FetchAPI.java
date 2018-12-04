package com.example.demo;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.Charsets;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import akka.actor.AbstractActor;
import akka.actor.Props;

public class FetchAPI extends AbstractActor {
    private HttpClient client;

    public FetchAPI() {
        client = new DefaultHttpClient();

    }
    static Props props() {

        return Props.create(FetchAPI.class, () -> new FetchAPI());
    }

    public JSONObject fetchBus(String id) {
        
        String body;
        HttpGet get = new HttpGet(
                "http://webservices.nextbus.com/service/publicXMLFeed?command=predictions&a=ttc&stopId=" + id);
        try {
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            Header encodingHeader = entity.getContentEncoding();
            Charset encoding = encodingHeader == null ? StandardCharsets.UTF_8 : 
            Charsets.toCharset(encodingHeader.getValue());
            body = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            JSONObject json = XML.toJSONObject(body);
            return json;

        } catch (IOException e) {
			body = "{unable to get next bus info for that ID}";
			e.printStackTrace();
        }
        return new JSONObject(body);
    }

    public JSONObject fetchWeather(){
        String body;
        HttpGet get = new HttpGet("https://query.yahooapis.com/v1/public/yql?q=select%20item.condition%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22toronto%2C%20on%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys");
        try {
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            Header encodingHeader = entity.getContentEncoding();
            Charset encoding = encodingHeader == null ? StandardCharsets.UTF_8 : 
            Charsets.toCharset(encodingHeader.getValue());
            body = EntityUtils.toString(entity, StandardCharsets.UTF_8);

        } catch (IOException e) {
			body = "{unable to get weather info}";
			e.printStackTrace();
        }
        return new JSONObject(body);
    }
    public String fetchAll(String id){
        JSONObject bus = fetchBus(id);
        JSONObject weather = fetchWeather();
        JSONObject weatherConditions;
        JSONObject merger;
        try{
            weatherConditions = weather.getJSONObject("query").getJSONObject("results").getJSONObject("channel")
            .getJSONObject("item").getJSONObject("condition");
        }
        catch(Exception e){
            weatherConditions = weather;

        }

        try{ //format the the two apis
            JSONArray pred = bus.getJSONObject("body").getJSONArray("predictions");
            int i=0;
           while(!pred.getJSONObject(i).has("direction")){ //loop through until json object with predictions is found
                i++;
           }
           JSONArray dir = pred.getJSONObject(i).getJSONObject("direction").getJSONArray("prediction");
            merger = dir.getJSONObject(0).append("Weather: ", weatherConditions); //append the weather api to the bus api json
            // remove unecessary info
            merger.remove("dirTag");
            merger.remove("tripTag");
            merger.remove("block");
            merger.remove("branch");
            merger.remove("dirTag");
            merger.remove("epochTime");
            merger.remove("vehicle");
        }
        catch(Exception e){
            merger = bus.append("Weather", weatherConditions);
        }



        return merger.toString();
        
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
        .match(String.class, i -> {
            if(i.equals("bus")){
                getSender().tell(fetchBus("2343"), getSelf()); //used for testing just the bus api
            }
            else if(i.equals("weather")){
                getSender().tell(fetchWeather(), getSelf()); //used for testing just the weather api
            }
            else{
                getSender().tell(fetchAll(i), getSelf()); // run fetchAll
            }
            
          })
        
        .build();
    }

 
}