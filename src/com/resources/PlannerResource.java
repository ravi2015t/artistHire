package com.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.daemonservices.ElasticSearchHose;
import com.daemonservices.WeddingPlannerExecutor;
import com.tasks.FetchTweetsTask.Location;

@Path("/planner")

public class PlannerResource {
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
		return null;
      }
	
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
	public List<Location> getMyWedding(
			@PathParam("username") String userName)
			{
				return null;
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
		
			return null;
		}
	
	@POST
	@Path("/user/bookEvent")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response bookEvent(String msg){
		
			return null;
		}
	
	
	}
