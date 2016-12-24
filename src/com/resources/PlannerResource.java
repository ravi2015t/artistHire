package com.resources;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.daemonservices.WeddingPlannerExecutor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.models.ConfirmedEvent;
import com.models.ImageDetails;
import com.models.PendingEventsSearch;
import com.models.PlanWedding;
import com.models.RateVendor;
import com.models.RecommendationResults;
import com.models.Request;
import com.models.User;
import com.models.Vendor;
import com.tasks.ComputePendingEvents;
import com.tasks.ComputePendingEventsVendor;
import com.tasks.ComputeRecommendation;
import com.tasks.FetchTweetsTask.Location;
import com.tasks.SendImagetoSQS;

@Path("/planner")

public class PlannerResource {
	static String userTable = "user";
	static String vendor = "vendor";
	static String SUFFIX = "/";
	static String planWedding = "planWeddding";
	static String confirmedEvents = "confirmEvents";
	static String tobeApprovedEvents = "approveEvents";
	static String marketAvgPrice = "marketPrice";

	private static final Properties awsCredentialsFile = WeddingPlannerExecutor
			.getPropertiesFile("AwsCredentials.properties");
	static BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsCredentialsFile.getProperty("accessKey"),
			awsCredentialsFile.getProperty("secretKey"));

	static AmazonDynamoDBClient client = new AmazonDynamoDBClient(awsCreds);

	static DynamoDB dynamoDB = new DynamoDB(client);

	private static String indexName = WeddingPlannerExecutor.getPropertiesFile("AwsCredentials.properties")
			.getProperty("index-name");

	@POST
	@Path("/userSignup")
	@Consumes(MediaType.TEXT_PLAIN)
	public Response signupUser(String msg) {
		System.out.println("MESG" + msg);
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

	@GET
	@Path("/recommend/{budget}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<RecommendationResults> searchVendors(@PathParam("budget") int budget) {
		Table table = dynamoDB.getTable("marketAvgPrice");
		Item item = table.getItem("username", "administrator", "photographer, florist, makeupartist", null);
		Long pValue = (Long) item.get("photographer");
		Long fValue = (Long) item.get("florist");
		Long mValue = (Long) item.get("makeupartist");

		double pParts = (pValue + fValue + mValue) / pValue;
		double fParts = (pValue + fValue + mValue) / fValue;
		double mParts = (pValue + fValue + mValue) / mValue;

		List<RecommendationResults> photoresults = new ArrayList<RecommendationResults>();
		List<RecommendationResults> floristresults = new ArrayList<RecommendationResults>();
		List<RecommendationResults> makeresults = new ArrayList<RecommendationResults>();

		ComputeRecommendation cr = new ComputeRecommendation();
		try {
			photoresults = cr.FindVendorsWithinBudget("photographer", Math.round(pParts * budget));
			floristresults = cr.FindVendorsWithinBudget("florist", Math.round(fParts * budget));
			makeresults = cr.FindVendorsWithinBudget("makeupartist", Math.round(mParts * budget));

		} catch (Exception e) {
			System.out.println("Exception while getting Recommendation");
			e.printStackTrace();
		}

		photoresults.addAll(floristresults);
		photoresults.addAll(makeresults);

		return photoresults;

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

			Item item = table.getItem("username", userName, "date, price, vendor, Address", null);

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
	@Path("/userAwaitingApproval/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<PendingEventsSearch> getAwaitingEvents(@PathParam("username") String userName) {

		List<PendingEventsSearch> searchResults = new ArrayList<PendingEventsSearch>();

		ComputePendingEvents cr = new ComputePendingEvents();
		try {
			searchResults = cr.FindVendorsWithinBudget("userName");

		} catch (Exception e) {
			System.out.println("Exception while getting Recommendation");
			e.printStackTrace();
		}
		return searchResults;

	}

	@GET
	@Path("/vendorPendingRequest/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<PendingEventsSearch> getPendingEvents(@PathParam("username") String userName) {

		List<PendingEventsSearch> searchResults = new ArrayList<PendingEventsSearch>();
		ComputePendingEventsVendor cr = new ComputePendingEventsVendor();
		try {
			searchResults = cr.FindVendorsWithinBudget("userName");

		} catch (Exception e) {
			System.out.println("Exception while getting Recommendation");
			e.printStackTrace();
		}
		return searchResults;

	}

	@POST
	@Path("/confirmEventVendor")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response confirmEventVendor(String msg) {
		Gson gson = new Gson();
		Table table = dynamoDB.getTable(confirmedEvents);

		try {
			ConfirmedEvent ce = gson.fromJson(msg, ConfirmedEvent.class);
			Item item = new Item().withPrimaryKey("username", ce.getUsr() + ce.getArtist())
					.withString("artist", ce.getArtist()).withString("usr", ce.getUsr())
					.withString("date", ce.getDate())
					.withMap("venue", new ValueMap().withString("street", ce.getVenue().getStreet())
							.withString("city", ce.getVenue().getCity()).withString("state", ce.getVenue().getState())
							.withNumber("zip", ce.getVenue().getZip()))
					.withString("category", ce.getCategory());
			PutItemSpec pit = new PutItemSpec().withItem(item)
					.withConditionExpression("attribute_not_exists(username)");

			table.putItem(pit);

			// Delete the Pending Items List
			Table table2 = dynamoDB.getTable(tobeApprovedEvents);
			DeleteItemSpec deleteItemSpec = new DeleteItemSpec().withPrimaryKey("username",
					ce.getArtist() + ce.getUsr());
			DeleteItemOutcome outcome = table2.deleteItem(deleteItemSpec);
			System.out.println("Printing item that was deleted...");
			System.out.println(outcome.getItem().toJSONPretty());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(200).entity("Failure").build();

		}

		return Response.status(200).entity("Success").build();

	}

	@POST
	@Path("/vendorPhotographerSignup")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response signupPhotographer(String msg) {
		Gson gson = new Gson();

		Table table = dynamoDB.getTable(vendor);
		// Table table2 = dynamoDB.getTable(marketAvgPrice);

		try {
			Vendor ven = gson.fromJson(msg, Vendor.class);
			Item item = new Item().withPrimaryKey("username", ven.getUsername())
					.withString("firstname", ven.getFirstname()).withString("lastname", ven.getLastname())
					.withString("password", ven.getPassword())
					.withMap("Address",
							new ValueMap().withString("street", ven.getAddress().getStreet())
									.withString("city", ven.getAddress().getCity())
									.withString("state", ven.getAddress().getState())
									.withNumber("zip", ven.getAddress().getZip()))
					.withString("phoneNumber", ven.getPhoneNumber()).withString("category", ven.getCategory())
					.withNumber("rating", 0).withLong("price", ven.getPrice()).withInt("nusers", 0);
			PutItemSpec pit = new PutItemSpec().withItem(item)
					.withConditionExpression("attribute_not_exists(username)");

			table.putItem(pit);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(200).entity("Failure").build();

		}

		return Response.status(200).entity("Success").build();

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
					.withString("firstname", ven.getFirstname()).withString("lastname", ven.getLastname())
					.withString("password", ven.getPassword())
					.withMap("Address",
							new ValueMap().withString("street", ven.getAddress().getStreet())
									.withString("city", ven.getAddress().getCity())
									.withString("state", ven.getAddress().getState())
									.withNumber("zip", ven.getAddress().getZip()))
					.withString("phoneNumber", ven.getPhoneNumber()).withString("category", ven.getCategory())
					.withNumber("rating", 0).withLong("price", ven.getPrice()).withInt("nusers", 0);
			PutItemSpec pit = new PutItemSpec().withItem(item)
					.withConditionExpression("attribute_not_exists(username)");

			table.putItem(pit);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(200).entity("Failure").build();
		}

		return Response.status(200).entity("Success").build();
	}

	@POST
	@Path("/vendorMakeUpartistSignup")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response signupCaterer(String msg) {
		Gson gson = new Gson();

		Table table = dynamoDB.getTable(vendor);

		try {
			Vendor ven = gson.fromJson(msg, Vendor.class);
			Item item = new Item().withPrimaryKey("username", ven.getUsername())
					.withString("firstname", ven.getFirstname()).withString("lastname", ven.getLastname())
					.withString("password", ven.getPassword())
					.withMap("Address",
							new ValueMap().withString("street", ven.getAddress().getStreet())
									.withString("city", ven.getAddress().getCity())
									.withString("state", ven.getAddress().getState())
									.withNumber("zip", ven.getAddress().getZip()))
					.withString("phoneNumber", ven.getPhoneNumber()).withString("category", ven.getCategory())
					.withNumber("rating", 0).withLong("price", ven.getPrice()).withInt("nusers", 0);
			PutItemSpec pit = new PutItemSpec().withItem(item)
					.withConditionExpression("attribute_not_exists(username)");

			table.putItem(pit);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(200).entity("Failure").build();
		}

		return Response.status(200).entity("Success").build();
	}

	@POST
	@Path("/rateVendor")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response rateVendor(String msg) {
		Gson gson = new Gson();

		Table table = dynamoDB.getTable(vendor);
		String response = "";
		try {
			System.out.println("RECEIVED iNPUT" + msg);
			RateVendor ven = gson.fromJson(msg, RateVendor.class);
			System.out.println("afgter mapping" + ven.getRating() + ven.getUsername());

			Item item = table.getItem("username", ven.getUsername(), "rating, nusers", null);
			System.out.println("Printing item after retrieving it rateVendor....");
			System.out.println(item.toJSONPretty());
			response = item.toJSON();
			JsonObject jobj = new Gson().fromJson(response, JsonObject.class);
			double rating = jobj.get("rating").getAsDouble();
			int nusers = jobj.get("nusers").getAsInt();
			rating = ((rating * nusers) + ven.getRating()) / (nusers + 1);
			nusers += 1;
			int usr = 1;

			UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("username", ven.getUsername())
					.withUpdateExpression("set #a=:val1 add #na :val2")
					.withNameMap(new NameMap().with("#a", "rating").with("#na", "nusers"))
					.withValueMap(new ValueMap().withNumber(":val1", rating).withNumber(":val2", usr))
					.withReturnValues(ReturnValue.ALL_NEW);
			UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
			System.out.println(outcome.getItem().toJSONPretty());

		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();

			return Response.status(200).entity("Failure").build();
		}

		return Response.status(200).entity("Success").build();
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
					.withNumber("budget", plan.getBudget()).withMap("Address",
							new ValueMap().withString("street", plan.getAddress().getStreet())
									.withString("city", plan.getAddress().getCity())
									.withString("state", plan.getAddress().getState())
									.withNumber("zip", plan.getAddress().getZip()));
			table.putItem(item);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(200).entity("Failure").build();
		}
		return Response.status(200).entity("Success").build();
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
		Table table = dynamoDB.getTable(tobeApprovedEvents);

		try {
			Request rq = gson.fromJson(msg, Request.class);
			// Send Notification to vendor. SNS should be implemented.
			Item item = new Item().withPrimaryKey("username", rq.getVendor() + rq.getUsr())
					.withString("date", rq.getDate().toString())
					.withMap("Address",
							new ValueMap().withString("street", rq.getAddress().getStreet())
									.withString("city", rq.getAddress().getCity())
									.withString("state", rq.getAddress().getState())
									.withNumber("zip", rq.getAddress().getZip()))
					.withString("usr", rq.getUsr()).withString("vendor", rq.getVendor());

			table.putItem(item);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(200).entity("Failure").build();
		}

		return Response.status(200).entity("Success").build();
	}

	// to upload image after retrieving from queue.

	// set file name and album name in fileDetail
	// send /profilepic/filename if its a profile pic

	@POST
	@Path("/userUpload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadProfileFile(@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {

		ImageDetails imd = new ImageDetails();
		SendImagetoSQS sImg = new SendImagetoSQS();
		ObjectMapper mapper = new ObjectMapper();
		File file = null;

		try { // check the file location

			String fileName = fileDetail.getFileName();
			File fileN = new File(fileName);
			file = File.createTempFile(fileN.getName(), ".png");
			Image image = ImageIO.read(uploadedInputStream);

			BufferedImage bi = createResizedCopy(image, 180, 180, true);
			ImageIO.write(bi, "png", file);

			imd.setPath(fileName);
			System.out.println("FILE NAME" + fileName);
			imd.setName(file.getAbsolutePath());
			System.out.println(file.getName());
			String jsonInString = mapper.writeValueAsString(imd);
			sImg.sendtoQ(jsonInString);
		} catch (Exception ex) {
			System.out.println("Exception while uploading picture");
			ex.printStackTrace();
			return Response.status(200).entity("Failure").build();
		}
		return Response.status(200).entity("Success").build();
	}

	public static BufferedImage createResizedCopy(Image originalImage, int scaledWidth, int scaledHeight,
			boolean preserveAlpha) {
		int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
		BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
		Graphics2D g = scaledBI.createGraphics();
		if (preserveAlpha) {
			g.setComposite(AlphaComposite.Src);
		}
		g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
		g.dispose();
		return scaledBI;
	}

	private void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) {

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

	@GET
	@Path("/vendorProfilePic/{username}")
	@Produces("image/png")
	public Response getImage(@PathParam(value = "username") String userName) {

		AmazonS3 s3client = new AmazonS3Client(awsCreds);
		String bucketName = "artist-hire";

		// ObjectListing listing = s3client.listObjects(bucketName,
		// "user/profile_pictures/user2/");
		try {
			ObjectListing listing = s3client.listObjects(bucketName, "vendor/profile/" + userName);
			GetObjectRequest request;
			for (S3ObjectSummary objectSummary : listing.getObjectSummaries()) {
				System.out.println(objectSummary.getKey());
				request = new GetObjectRequest(bucketName, objectSummary.getKey());
				S3Object object = s3client.getObject(request);
				InputStream objectContent = object.getObjectContent();

				return Response.ok(objectContent).build();
			}
		} catch (Exception e) {
			return Response.noContent().build();
		}

		return Response.noContent().build();

	}

	// use this call to get details of the pics in S3. use these paths to query

	@GET
	@Path("/vendorAlbumPicKeys/{username}")
	@Produces("image/png")
	public Response getAlbumImageKeys(@PathParam(value = "username") String userName) {

		AmazonS3 s3client = new AmazonS3Client(awsCreds);
		String bucketName = "artist-hire";

		// ObjectListing listing = s3client.listObjects(bucketName,
		// "user/profile_pictures/user2/");
		try {
			ObjectListing listing = s3client.listObjects(bucketName, "vendor/albums/" + userName);
			GetObjectRequest request;
			String keys = "";
			for (S3ObjectSummary objectSummary : listing.getObjectSummaries()) {
				System.out.println(objectSummary.getKey());
				keys = keys + objectSummary.getKey() + ";";

			}
			return Response.ok(keys).build();
		} catch (Exception e) {
			return Response.noContent().build();
		}

	}

	@GET
	@Path("/getvendorAlbumPic/{key}")
	@Produces("image/png")
	public Response getAlbumImage(@PathParam(value = "username") String key) {

		AmazonS3 s3client = new AmazonS3Client(awsCreds);
		String bucketName = "artist-hire";

		try {
			GetObjectRequest request;
			request = new GetObjectRequest(bucketName, key);
			S3Object object = s3client.getObject(request);
			InputStream objectContent = object.getObjectContent();
			return Response.ok(objectContent).build();
		}

		catch (Exception e) {
			return Response.noContent().build();
		}

	}

}