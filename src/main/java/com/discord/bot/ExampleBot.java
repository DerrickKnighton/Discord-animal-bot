package com.discord.bot;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;

public final class ExampleBot {
	
	  public static Map<String, String> animalApiMap;
	  static {
	      animalApiMap = new HashMap<>();
	      animalApiMap.put("!cat", "https://catfact.ninja/fact");
	      animalApiMap.put("!dog", "https://dog-facts-api.herokuapp.com/api/v1/resources/dogs?number=1");
	  
	  }
	  
	  boolean killStream = false;
	  
	  //private static RestTemplate restTemplate;

	  public static void main(final String[] args) {
	    final String token = "OTM5Nzg0NTUzMzc0MDQ0MTgw.Yf94kQ.xFZ3Z0B-3uJhAkXXb7RkfCje-yA";
	    final DiscordClient client = DiscordClient.create(token);
	    final GatewayDiscordClient gateway = client.login().block();
	    gateway.on(MessageCreateEvent.class).subscribe(event -> {
	      final Message message = event.getMessage();
	      if(message.getContent().contains("!")) {
	      final MessageChannel channel = message.getChannel().block();
	      callApi(message.getContent(),channel);
	      }
	    });

	    gateway.onDisconnect().block();
	  }
	  
	  public static Message callApi(String animal, MessageChannel channel){
		  
		  String apiUrl = "";
		  String apiResponse = "";
				  
		  // try catch if key isnt in map
		  try {
			  apiUrl = animalApiMap.get(animal);
			  apiResponse = sendGET(apiUrl);
		  }
		  catch(Exception ignore){
			  return channel.createMessage("I do not have functionality for your response!").block();
		  }
		  
		  return channel.createMessage(apiResponse.toString()).block();
	  }
	
	  
	  private static String sendGET(String GET_URL) throws IOException {
			URL obj = new URL(GET_URL);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			int responseCode = con.getResponseCode();
			System.out.println("GET Response Code :: " + responseCode);
			if (responseCode == HttpURLConnection.HTTP_OK) { // success
				BufferedReader in = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				// print result
				return response.toString();
			} else {
				return "GET request not worked";
			}

		}
	}
