package com.resources;

//
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//import org.codehaus.jackson.map.JsonSerializer;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.daemonservices.ElasticSearchHose;
import com.daemonservices.WeddingPlannerExecutor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.models.ConfirmedEvent;
import com.models.ImageDetails;
import com.models.PlanWedding;
import com.models.RateVendor;
import com.models.Request;
import com.models.SNSmodel;
import com.models.SearchResults;
import com.models.Tweet;
import com.models.User;
import com.models.Vendor;
import com.tasks.FetchTweetsTask.Location;
import com.tasks.SendImagetoSQS;

import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

@Path("/planner")

public class PlannerResource {
	static String userTable = "user";
	static String vendor = "vendor";

	static String planWedding = "planWeddding";
	static String confirmedEvents = "confirmEvents";
	static String tobeApprovedEventsUser = "approveEventsUser";
	static String tobeApprovedEventsVendor = "approveEventsVendor";

	private static final Properties awsCredentialsFile = WeddingPlannerExecutor
			.getPropertiesFile("AwsCredentials.properties");
	static BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsCredentialsFile.getProperty("accessKey"),
			awsCredentialsFile.getProperty("secretKey"));

	static AmazonDynamoDBClient client = new AmazonDynamoDBClient(awsCreds);

	static DynamoDB dynamoDB = new DynamoDB(client);

	// new ProfileCredentialsProvider()));
	// static DynamoDB dynamoDB = new DynamoDB(getclient(client));
	/*
	 * public static AmazonDynamoDBClient getclient(AmazonDynamoDBClient client)
	 * { client.withEndpoint("http://localhost:8000"); return client; }
	 */
	private static String indexName = WeddingPlannerExecutor.getPropertiesFile("AwsCredentials.properties")
			.getProperty("index-name");
	private static String mappingName = WeddingPlannerExecutor.getPropertiesFile("AwsCredentials.properties")
			.getProperty("mapping-name");
	ElasticSearchHose hose = new ElasticSearchHose();

	/*
	 * @GET
	 * 
	 * @Path("{query_term}/{from}/{size}")
	 * 
	 * @Produces(MediaType.APPLICATION_JSON) public List<Location> getTweets(
	 * 
	 * @PathParam("query_term") String queryTerm,
	 * 
	 * @PathParam("size") int size,
	 * 
	 * @PathParam("from") int from) { JestClient client =
	 * WeddingPlannerExecutor.getESClient();
	 * System.out.println("QUERIES--------------------");
	 * System.out.println(queryTerm); System.out.println(from);
	 * System.out.println(size); String query = "{\n" + "    \"query\": {\n" +
	 * "                \"query_string\" : {\n" +
	 * "                    \"query\" : \" "+ queryTerm +"\",\n" +
	 * "\"default_field\" : \"tweet\"" + "                }\n" + "    },\n" +
	 * "	 \"from\" : "+ from + ",\n" + "     \"size\" : "+ size + "\n" + "}";
	 * 
	 * Search search = new Search.Builder(query) // multiple index or types can
	 * be added. .addIndex(indexName) .addType(mappingName) .build();
	 * System.out.println("After search---------------------------");
	 * SearchResult sr = null; try { sr = client.execute(search); } catch
	 * (IOException e) { e.printStackTrace(); }
	 * 
	 * if(sr != null) {
	 * System.out.println("Returning Nul ##################################");
	 * JsonObject results = sr.getJsonObject(); return
	 * parseJsonToTweet(results); } return null; }
	 * 
	 * private List<Location> parseJsonToTweet(JsonObject results) {
	 * List<Location> allTweets = new ArrayList<Location>(); if(results != null
	 * && !results.isJsonNull() && results.isJsonObject()) { JsonObject obj =
	 * results.getAsJsonObject("hits"); JsonArray arr =
	 * obj.getAsJsonArray("hits"); for(int i = 0; i < arr.size(); i++) {
	 * JsonObject o = arr.get(i).getAsJsonObject(); JsonObject oo =
	 * o.getAsJsonObject("_source"); Location temp = new Location();
	 * temp.latitude = oo.getAsJsonPrimitive("latitude").getAsDouble();
	 * temp.longitude = oo.getAsJsonPrimitive("longitude").getAsDouble();
	 * temp.sentiment = oo.getAsJsonPrimitive("sentiment").getAsString();
	 * allTweets.add(temp); } } return allTweets; }
	 */
	@POST
	@Path("/userSignup")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response signupUser(String msg) {
		Gson gson = new Gson();
		Table table = dynamoDB.getTable(userTable);

		try {
			User user = gson.fromJson(msg, User.class);
			Item item = new Item().withPrimaryKey("username", user.getUsername())
					.withString("firstname", user.getFirstname()).withString("password", user.getPassword())
					.withString("lastname", user.getLastname())
					.withMap("Address",
							new ValueMap().withString("street", user.getAddress().getStreet())
									.withString("city", user.getAddress().getCity())
									.withString("state", user.getAddress().getState())
									.withNumber("zip", user.getAddress().getZip()))
					.withString("phoneNumber", user.getPhoneNumber());
			PutItemSpec pit = new PutItemSpec().withItem(item)
					.withConditionExpression("attribute_not_exists(username)");
			table.putItem(pit);

		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
			return Response.status(200).entity("Failure").build();
	
		}
		return Response.status(200).entity("Success").build();

	}

	@GET
	@Path("/userLogin/{username}/{password}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean LoginUser(@PathParam("username") String userName, @PathParam("password") String Password) {
		System.out.println("Username" + userName + "Password" + Password);
		boolean isUser = false;
		String response = "";
		Table table = dynamoDB.getTable(userTable);
		try {

			Item item = table.getItem("username", userName, "password", null);
			System.out.println("Printing item after retrieving it....");
			System.out.println(item.toJSONPretty());
			response = item.toJSON();
			JsonObject jobj = new Gson().fromJson(response, JsonObject.class);
			String result = jobj.get("password").toString();
			System.out.println("RESULT" + result);
			if (result.equals("\"" + Password + "\"")) {
				isUser = true;
			}
		} catch (Exception e) {
			System.err.println("GetItem failed.");
			System.err.println(e.getMessage());
		}

		return isUser;
	}

	@GET
	@Path("/vendorLogin/{username}/{password}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean LoginVendor(@PathParam("username") String userName, @PathParam("password") String Password) {
		boolean isUser = false;
		String response = "";
		Table table = dynamoDB.getTable(vendor);
		try {

			Item item = table.getItem("username", userName, "password", null);
			System.out.println("Printing item after retrieving it....");
			System.out.println(item.toJSONPretty());
			response = item.toJSON();
			JsonObject jobj = new Gson().fromJson(response, JsonObject.class);
			String result = jobj.get("password").toString();
			if (result.equals("\"" + Password + "\"")) {
				isUser = true;
			}
		} catch (Exception e) {
			System.err.println("GetItem failed.");
			System.err.println(e.getMessage());
		}

		return isUser;
	}

	/*
	 * ObjectMapper mapper = new ObjectMapper(); try { SNSmodel snsmod =
	 * mapper.readValue(msg, SNSmodel.class);
	 * System.out.println("SUBSCRIPTION URL"+ snsmod.getSubscribeURL()); if
	 * (snsmod.getType().equals("Notification")) { //TODO: Do something with the
	 * Message and Subject. //Just log the subject (if it exists) and the
	 * message. //String logMsgAndSubject =
	 * ">>Notification received from topic " + msg.getTopicArn(); if
	 * (snsmod.getMessage() != null) { System.out.println("MESSAGE" +
	 * snsmod.getMessage());
	 * System.out.println("Received message in sns receiver: " +
	 * snsmod.getMessage()); Tweet t = mapper.readValue(snsmod.getMessage(),
	 * Tweet.class); if(t != null) {
	 * System.out.println("Send tweet to Queue**********"); hose.indexTweet(t);
	 * } }
	 * 
	 * } else if (snsmod.getType().equals("SubscriptionConfirmation")) { //TODO:
	 * You should make sure that this subscription is from the topic you expect.
	 * Compare topicARN to your list of topics //that you want to enable to add
	 * this endpoint as a subscription.
	 * 
	 * //Confirm the subscription by going to the subscribeURL location //and
	 * capture the return value (XML message body as a string) Scanner sc = new
	 * Scanner(new URL(snsmod.getSubscribeURL()).openStream()); StringBuilder sb
	 * = new StringBuilder(); while (sc.hasNextLine()) {
	 * sb.append(sc.nextLine()); }
	 * System.out.println(">>Subscription confirmation (" +
	 * snsmod.getSubscribeURL() +") Return value: " + sb.toString()); //TODO:
	 * Process the return value to ensure the endpoint is subscribed.
	 * 
	 * }
	 * 
	 * } catch (JsonParseException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } catch (JsonMappingException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } catch (IOException e) {
	 * // TODO Auto-generated catch block e.printStackTrace(); }
	 * 
	 * return Response.status(200).build();
	 */

	@GET
	@Path("/usergetProfile/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getProfile(@PathParam("username") String userName) {
		Table table = dynamoDB.getTable(userTable);
		String response = "";
		try {
			// Should be Implementing as a LIST

			Item item = table.getItem("username", userName, "firstname, lastname, phoneNumber, Address", null);

			System.out.println("Printing item after retrieving it....");
			System.out.println(item.toJSONPretty());
			response = item.toJSON();

		} catch (Exception e) {
			System.err.println("GetItem failed.");
			System.err.println(e.getMessage());
		}

		return response;

	}
	// code =0 no preference

	@GET
	@Path("{budget}/{vendor1}/{vendor2}/{vendor2}/{code}")
	@Produces(MediaType.APPLICATION_JSON)
	public String searchVendors(@PathParam("budget") int budget, @PathParam("vendor1") String vendor1,
			@PathParam("vendor2") String vendor2, @PathParam("vendor3") String vendor3, @PathParam("code") int code) {

		// 0/1 knapsack based on input. It's straight forward if user chooses
		// only one vendor.

		return null;

	}

	@GET
	@Path("/vendorGetProfile/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getProfileVendor(@PathParam("username") String userName) {
		Table table = dynamoDB.getTable(vendor);
		String response = "";
		try {
			Item item = table.getItem("username", userName,
					"firstname, secondname, phoneNumber, Address, rating, nusers", null);

			System.out.println("Printing item after retrieving it....");
			System.out.println(item.toJSONPretty());
			response = item.toJSON();

		} catch (Exception e) {
			System.err.println("GetItem failed.");
			System.err.println(e.getMessage());
		}

		return response;
	}

	@GET
	@Path("/userSearchVendors/{query}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Location> getSearchResults(@PathParam("username") String userName) {
		return null;
	}

	@GET
	@Path("/userMyWedding/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getMyWedding(@PathParam("username") String userName) {
		Table table = dynamoDB.getTable(confirmedEvents);
		String response = "";
		try {
			// will return all the documents that match with the username.
			// might have to implement a query instead of retrieve

			Item item = table.getItem("username", userName, "date, price, Vendor, Address", null);

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
	@Path("/vendorPhotographerSignup")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response signupPhotographer(String msg) {
		Gson gson = new Gson();

		Table table = dynamoDB.getTable(vendor);

		try {
			Vendor ven = gson.fromJson(msg, Vendor.class);
			Item item = new Item().withPrimaryKey("username", ven.getUsername())
					.withString("firstName", ven.getFirstname()).withString("lastName", ven.getLastname())
					.withString("password", ven.getPassword())
					.withMap("Address",
							new ValueMap().withString("street", ven.getAddress().getStreet())
									.withString("city", ven.getAddress().getCity())
									.withString("state", ven.getAddress().getState())
									.withNumber("zip", ven.getAddress().getZip()))
					.withString("phoneNumber", ven.getPhoneNumber())
					.withString("type", ven.getType())
					.withNumber("rating", 0).withLong("price", ven.getPrice())
					.withInt("nusers", 0);
			PutItemSpec pit = new PutItemSpec().withItem(item)
					.withConditionExpression("attribute_not_exists(username)");

			table.putItem(pit);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Response.status(200).build();

	}

	@POST
	@Path("/vendorFloristSignup")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response signupFlorist(String msg) {
		Gson gson = new Gson();

		Table table = dynamoDB.getTable(vendor);

		try {
			Vendor ven = gson.fromJson(msg, Vendor.class);
			Item item = new Item().withPrimaryKey("username", ven.getUsername())
					.withString("firstName", ven.getFirstname()).withString("lastName", ven.getLastname())
					.withString("password", ven.getPassword())
					.withMap("Address",
							new ValueMap().withString("street", ven.getAddress().getStreet())
									.withString("city", ven.getAddress().getCity())
									.withString("state", ven.getAddress().getState())
									.withNumber("zip", ven.getAddress().getZip()))
					.withString("phoneNumber", ven.getPhoneNumber())
					.withString("type", ven.getType())
					.withNumber("rating", 0).withLong("price", ven.getPrice())
					.withInt("nusers", 0);
			PutItemSpec pit = new PutItemSpec().withItem(item)
					.withConditionExpression("attribute_not_exists(username)");

			table.putItem(pit);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Response.status(200).build();
	}

	@POST
	@Path("/vendorCatererSignup")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response signupCaterer(String msg) {
		Gson gson = new Gson();

		Table table = dynamoDB.getTable(vendor);

		try {
			Vendor ven = gson.fromJson(msg, Vendor.class);
			Item item = new Item().withPrimaryKey("username", ven.getUsername())
					.withString("firstName", ven.getFirstname()).withString("lastName", ven.getLastname())
					.withString("password", ven.getPassword())
					.withMap("Address",
							new ValueMap().withString("street", ven.getAddress().getStreet())
									.withString("city", ven.getAddress().getCity())
									.withString("state", ven.getAddress().getState())
									.withNumber("zip", ven.getAddress().getZip()))
					.withString("phoneNumber", ven.getPhoneNumber())
					.withString("type", ven.getType())
					.withNumber("rating", 0)
					.withLong("price", ven.getPrice())
					.withInt("nusers", 0);
			PutItemSpec pit = new PutItemSpec().withItem(item)
					.withConditionExpression("attribute_not_exists(username)");

			table.putItem(pit);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Response.status(200).build();
	}

	@POST
	@Path("/rateVendor")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response rateVendor(String msg) {
		Gson gson = new Gson();

		Table table = dynamoDB.getTable(vendor);
		String response = "";
		try {
			RateVendor ven = gson.fromJson(msg, RateVendor.class);

			Item item = table.getItem("username", ven.getUsername(), "rating", "nusers", null);
			System.out.println("Printing item after retrieving it....");
			System.out.println(item.toJSONPretty());
			response = item.toJSON();
			JsonObject jobj = new Gson().fromJson(response, JsonObject.class);
			double rating = jobj.get("rating").getAsDouble();
			int nusers = jobj.get("nusers").getAsInt();
			rating = ((rating * nusers) + ven.getRating()) / (nusers + 1);
			nusers += 1;

			UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("username", ven.getUsername())
					.withUpdateExpression("add #a :val1 add #na=:val2")
					.withNameMap(new NameMap().with("#a", "rating").with("#na", "nusers"))
					.withValueMap(new ValueMap().withNumber(":val1", rating).withNumber(":val2", nusers))
					.withReturnValues(ReturnValue.ALL_NEW);
			UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
			 System.out.println(outcome.getItem().toJSONPretty());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Response.status(200).build();

	}

	@POST
	@Path("/userRegisterWedding")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response registerWedding(String msg) {
		Gson gson = new Gson();
		Table table = dynamoDB.getTable(planWedding);

		try {
			PlanWedding plan = gson.fromJson(msg, PlanWedding.class);
			Item item = new Item().withPrimaryKey("username", "").withString("date", plan.getDate().toString())
					.withNumber("budget", plan.getBudget())
					.withMap("Address",
							new ValueMap().withString("street", plan.getAddress().getStreet())
									.withString("city", plan.getAddress().getCity())
									.withString("state", plan.getAddress().getState())
									.withNumber("zip", plan.getAddress().getZip()))
					.withString("preferenceOrder", plan.getPreferenceOrder());
			table.putItem(item);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Response.status(200).build();

	}

	/*
	 * Gets called when user clicks on book event choosing an artist. username
	 * is the username of customer and vendor is the username of the vendor. two
	 * tabls to maintain requests for user and vendor separately Once its
	 * approved should push it to confirmed Events and delete here
	 */
	@POST
	@Path("/userBookEvent")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response bookEvent(String msg) {
		Gson gson = new Gson();
		Table table = dynamoDB.getTable(tobeApprovedEventsVendor);
		Table table2 = dynamoDB.getTable(tobeApprovedEventsUser);

		try {
			Request rq = gson.fromJson(msg, Request.class);
			// Send Notification to vendor. SNS should be implemented.
			Item item = new Item().withPrimaryKey("Vendor", rq.getVendor()).withString("date", rq.getDate().toString())

					.withMap("Address",
							new ValueMap().withString("street", rq.getAddress().getStreet())
									.withString("city", rq.getAddress().getCity())
									.withString("state", rq.getAddress().getState())
									.withNumber("zip", rq.getAddress().getZip()))
					.withString("user", rq.getUser()).withString("Name", "");

			table.putItem(item);

			item = new Item().withPrimaryKey("user", rq.getUser()).withString("date", rq.getDate().toString())

					.withMap("Address",
							new ValueMap().withString("street", rq.getAddress().getStreet())
									.withString("city", rq.getAddress().getCity())
									.withString("state", rq.getAddress().getState())
									.withNumber("zip", rq.getAddress().getZip()))
					.withString("user", rq.getVendor()).withString("Name", "");

			table2.putItem(item);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Response.status(200).build();

	}

	// to upload image after retrieving from queue.

	// set file name and album name in fileDetail
	// send /profilepic/filename if its a profile pic
	/*
	 * @POST
	 * 
	 * @Path("/userUpload")
	 * 
	 * @Consumes(MediaType.MULTIPART_FORM_DATA) public Response
	 * uploadProfileFile(@FormDataParam("file") InputStream uploadedInputStream,
	 * 
	 * @FormDataParam("file") FormDataContentDisposition fileDetail) {
	 * 
	 * ImageDetails imd = new ImageDetails(); SendImagetoSQS sImg = new
	 * SendImagetoSQS(); ObjectMapper mapper = new ObjectMapper();
	 * 
	 * try { // check the file location String uploadedFileLocation =
	 * "d://uploaded/" + fileDetail.getFileName();
	 * 
	 * writeToFile(uploadedInputStream, uploadedFileLocation);
	 * imd.setPath(uploadedFileLocation); imd.setName(fileDetail.getFileName());
	 * String jsonInString = mapper.writeValueAsString(imd);
	 * sImg.sendtoQ(jsonInString); } catch (Exception ex) {
	 * System.out.println("Exception while uploading picture");
	 * ex.printStackTrace(); } return Response.status(200).build();
	 * 
	 * }
	 * 
	 */ private void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) {

		try {
			OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];

			out = new FileOutputStream(new File(uploadedFileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

}
