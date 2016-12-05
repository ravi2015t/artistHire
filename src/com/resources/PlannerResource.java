package com.resources;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.daemonservices.ElasticSearchHose;
import com.daemonservices.WeddingPlannerExecutor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.models.ConfirmedEvent;
import com.models.PlanWedding;
import com.models.SNSmodel;
import com.models.Tweet;
import com.models.User;
import com.tasks.FetchTweetsTask.Location;

import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

@Path("/planner")

public class PlannerResource {
	static String userTable = "User";
	static String planWedding= "planWeddding";
	static String confirmedEvents = "confirmEvents"; 
	   
	static AmazonDynamoDBClient client = new AmazonDynamoDBClient();

	  
	  //  static DynamoDB dynamoDB = new DynamoDB(new AmazonDynamoDBClient(
	    //        new ProfileCredentialsProvider()));
		static DynamoDB dynamoDB = new DynamoDB(getclient(client));
	    public static AmazonDynamoDBClient getclient(AmazonDynamoDBClient client)
	    {
	    	 client.withEndpoint("http://localhost:8000"); 
	         return client;
	    }



	private static String indexName = WeddingPlannerExecutor.getPropertiesFile("AwsCredentials.properties")
			.getProperty("index-name");
	private static String mappingName = WeddingPlannerExecutor.getPropertiesFile("AwsCredentials.properties")
			.getProperty("mapping-name");
	ElasticSearchHose hose = new ElasticSearchHose();
	
	/*@GET
	@Path("{query_term}/{from}/{size}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Location> getTweets(
			@PathParam("query_term") String queryTerm,
			@PathParam("size") int size,
			@PathParam("from") int from) {
		JestClient client = WeddingPlannerExecutor.getESClient();
		System.out.println("QUERIES--------------------");
                System.out.println(queryTerm);
		System.out.println(from);
		System.out.println(size);
		String query = "{\n" +
	            "    \"query\": {\n" +
	            "                \"query_string\" : {\n" +
	            "                    \"query\" : \" "+ queryTerm +"\",\n" +
	            					  "\"default_field\" : \"tweet\"" + 
	            "                }\n" +
	            "    },\n" +
	            "	 \"from\" : "+ from + ",\n" +
	            "     \"size\" : "+ size + "\n" +
	            "}";

		Search search = new Search.Builder(query)
                // multiple index or types can be added.
                .addIndex(indexName)
                .addType(mappingName)
                .build();
                System.out.println("After search---------------------------");
		SearchResult sr = null;
		try {
			sr = client.execute(search);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(sr != null) {
                         System.out.println("Returning Nul ##################################");
			JsonObject results = sr.getJsonObject();
			return parseJsonToTweet(results);
		}
		return null;
	}

	private List<Location> parseJsonToTweet(JsonObject results) {
		List<Location> allTweets = new ArrayList<Location>();
		if(results != null && !results.isJsonNull() && results.isJsonObject()) {
			JsonObject obj = results.getAsJsonObject("hits");
			JsonArray arr = obj.getAsJsonArray("hits");
			for(int i = 0; i < arr.size(); i++) {
				JsonObject o = arr.get(i).getAsJsonObject();
				JsonObject oo = o.getAsJsonObject("_source");
				Location temp = new Location();
				temp.latitude = oo.getAsJsonPrimitive("latitude").getAsDouble();
				temp.longitude = oo.getAsJsonPrimitive("longitude").getAsDouble();
				temp.sentiment = oo.getAsJsonPrimitive("sentiment").getAsString();
				allTweets.add(temp);
			}
		}
		return allTweets;
	}
*/	
	@POST
	@Path("/user/signup")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response signupUser(String msg){
		ObjectMapper mapper = new ObjectMapper();
		Table table = dynamoDB.getTable(userTable);
		
        try {
			User user = mapper.readValue(msg, User.class);
			 Item item = new Item()
		                .withPrimaryKey("username", user.getUsername())
		                .withString("firstName", user.getFirstname())
		                .withString("lastName", user.getLastname())
		                .withMap( "Address", 
		                    new ValueMap()
		                    .withString("street", user.getAddress().getSrteet())
		                    .withString("city", user.getAddress().getCity())
		                    .withString("state", user.getAddress().getState())
		                    .withNumber ("zip", user.getAddress().getZip()))
		                .withString("phoneNumber", user.getPhoneNumber());
		                 table.putItem(item);
  
			
        }
        catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return   Response.status(200).build();
    
	
	}
		/*ObjectMapper mapper = new ObjectMapper();
        try {
			SNSmodel snsmod = mapper.readValue(msg, SNSmodel.class);
			System.out.println("SUBSCRIPTION URL"+ snsmod.getSubscribeURL());
			if (snsmod.getType().equals("Notification")) {
				//TODO: Do something with the Message and Subject.
				//Just log the subject (if it exists) and the message.
				//String logMsgAndSubject = ">>Notification received from topic " + msg.getTopicArn();
				if (snsmod.getMessage() != null) {
					  System.out.println("MESSAGE" + snsmod.getMessage());				 
		              System.out.println("Received message in sns receiver: " + snsmod.getMessage());
		              Tweet t = mapper.readValue(snsmod.getMessage(), Tweet.class);
		              if(t != null) {
		                  System.out.println("Send tweet to Queue**********");
		  				  hose.indexTweet(t);
						}
		          }
				
			}
	       else if (snsmod.getType().equals("SubscriptionConfirmation"))
			{
	       //TODO: You should make sure that this subscription is from the topic you expect. Compare topicARN to your list of topics 
	       //that you want to enable to add this endpoint as a subscription.
	        	
	       //Confirm the subscription by going to the subscribeURL location 
	       //and capture the return value (XML message body as a string)
	       Scanner sc = new Scanner(new URL(snsmod.getSubscribeURL()).openStream());
	       StringBuilder sb = new StringBuilder();
	       while (sc.hasNextLine()) {
	         sb.append(sc.nextLine());
	       }
	      System.out.println(">>Subscription confirmation (" + snsmod.getSubscribeURL() +") Return value: " + sb.toString());
	       //TODO: Process the return value to ensure the endpoint is subscribed.
	     
			}

		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Response.status(200).build();
*/			
	
	@GET
	@Path("/user/getProfile/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Location> getProfile(
			@PathParam("username") String userName)
			{
				return null;
			}
	@GET
	@Path("/vendor/getProfile/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Location> getProfileVendor(
			@PathParam("username") String userName)
			{
				return null;
			}
	
	@GET
	@Path("/user/MyWedding/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getMyWedding(
			@PathParam("username") String userName)
			{
	    Table table = dynamoDB.getTable(confirmedEvents);
        String response = "";
        try {
        	//Should be Implementing as a LIST

            Item item = table.getItem("username", userName,  "date, price, Vendor, Address", null);

            System.out.println("Printing item after retrieving it....");
            System.out.println(item.toJSONPretty());
            response = item.toJSON();
            
        } catch (Exception e) {
            System.err.println("GetItem failed.");
            System.err.println(e.getMessage());
        }   
        
        return response;
				
			}
	
	
	@POST
	@Path("/vendor/photographer/signup")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response signupPhotographer(String msg){
		
			return null;
		}
    
	@POST
	@Path("/vendor/florist/signup")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response signupFlorist(String msg){
		
			return null;
		}
	    	
	@POST
	@Path("/vendor/caterer/signup")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response signupCaterer(String msg){
		
			return null;
		}
	
	@POST
	@Path("/user/registerWedding")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response registerWedding(String msg){
		ObjectMapper mapper = new ObjectMapper();
		Table table = dynamoDB.getTable(planWedding);
		
        try {
			PlanWedding plan = mapper.readValue(msg, PlanWedding.class);
			   Item item = new Item()
                       .withPrimaryKey("username", "")
                       .withString("date", plan.getDate().toString())
                       .withNumber("budget", plan.getBudget())
                       .withMap( "Address", 
                           new ValueMap()
                           .withString("street", plan.getAddress().getSrteet())
		                    .withString("city", plan.getAddress().getCity())
		                    .withString("state", plan.getAddress().getState())
		                    .withNumber ("zip", plan.getAddress().getZip()))
		                .withString("preferenceOrder", plan.getPreferenceOrder());
			   table.putItem(item);
			            
             
			
        }
        catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return   Response.status(200).build();
    
	
	
		
		}
	
	@POST
	@Path("/user/bookEvent")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response bookEvent(String msg){
		ObjectMapper mapper = new ObjectMapper();
		Table table = dynamoDB.getTable(planWedding);
		
        try {
			ConfirmedEvent ce = mapper.readValue(msg, ConfirmedEvent.class);
		
		    Item item = new Item()
                    .withPrimaryKey("username", "rambit")
                    .withString("date", ce.getDate().toString())
                    .withNumber("price", ce.getPrice())
                    .withMap( "Address", 
                        new ValueMap()
                        .withString("street", ce.getVenue().getSrteet())
                        .withString("city", ce.getVenue().getCity())
                        .withString("state", ce.getVenue().getState())
                        .withNumber("zip", ce.getVenue().getZip()))
                    .withString("Vendor", ce.getArtist())
                    .withString("Name", "");
                    
		
        table.putItem(item);
        
        
		
    }
    catch (JsonParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (JsonMappingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
	return   Response.status(200).build();

	}
	
	}
