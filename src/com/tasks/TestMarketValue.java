package com.tasks;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.google.gson.Gson;
import com.models.MarketPrice;
import com.models.Vendor;

public class TestMarketValue {
	static AmazonDynamoDBClient client = new AmazonDynamoDBClient();
	static DynamoDB dynamoDB = new DynamoDB(getclient(client));

	public static AmazonDynamoDBClient getclient(AmazonDynamoDBClient client) {
		client.withEndpoint("http://localhost:8000");
		return client;
	}

	public static void main(String[] args) {
		
		Table table = dynamoDB.getTable("market");

		try {
		/*	Item item = new Item().withPrimaryKey("username", "administrator")
					.withLong("photographer", 25000)
					.withLong("florist", 15000)
					.withLong("makeupartist", 19000);
			table.putItem(item);
*/

Item			item = table.getItem("username", "administrator", "photographer, florist, makeupartist", null);
			System.out.println("Printing item after retrieving it....");
			System.out.println(item.toJSONPretty());
			Gson gson = new Gson();
		System.out.println("sdfs"+ item.getJSON("photographer"));
			/*MarketPrice mr = gson.fromJson(item.toString(), MarketPrice.class);
			
			
			System.out.println("photograpger" + mr.getPhotographer()+mr.getFlorist()+mr.getMakeupArtist());
			*/
		/*	String msg = "{\"makeupartist\" : 19000,\"florist\" : 15000,\"photographer\" : 25000}";
			MarketPrice mr = gson.fromJson(msg, MarketPrice.class);
			System.out.println("photograpger" + mr.getPhotographer()+mr.getFlorist()+mr.getMakeupArtist());
		*/	
			
		} catch (Exception e) {
			System.err.println("GetItem failed.");
			System.err.println(e.getMessage());
		}

		
		
	}

}
