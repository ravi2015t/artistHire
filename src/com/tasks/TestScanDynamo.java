package com.tasks;

import com.models.PendingEventsSearch;
import com.models.RecommendationResults;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.daemonservices.WeddingPlannerExecutor;

public class TestScanDynamo {
/*	static AmazonDynamoDBClient client = new AmazonDynamoDBClient();
	static DynamoDB dynamoDB = new DynamoDB(getclient(client));
*/
	private static final Properties awsCredentialsFile = WeddingPlannerExecutor
			.getPropertiesFile("AwsCredentials.properties");
	static BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsCredentialsFile.getProperty("accessKey"),
			awsCredentialsFile.getProperty("secretKey"));

	static AmazonDynamoDBClient client = new AmazonDynamoDBClient(awsCreds);
	static DynamoDB dynamoDB = new DynamoDB(client);

	public static AmazonDynamoDBClient getclient(AmazonDynamoDBClient client) {
		client.withEndpoint("http://localhost:8000");
		return client;
	}

	//static String tableName = "vendor";
	static DynamoDBMapper mapper = new DynamoDBMapper(client);

	public static void main(String[] args) throws Exception {

		// findProductsForPriceLessThanZero();
		FindVendorsWithinBudget(mapper, 2);
	}

	/*
	 * private static void findProductsForPriceLessThanZero() {
	 * 
	 * Table table = dynamoDB.getTable(tableName);
	 * 
	 * Map<String, Object> expressionAttributeValues = new HashMap<String,
	 * Object>(); expressionAttributeValues.put(":pr", 20000);
	 * expressionAttributeValues.put(":pr1", "photographer");
	 * 
	 * ItemCollection<ScanOutcome> items = table.scan(
	 * "Price < :pr and type = :pr1", //FilterExpression
	 * "firstname, lastname, username, Price, rating", //ProjectionExpression
	 * null, //ExpressionAttributeNames - not used in this example
	 * expressionAttributeValues);
	 * 
	 * System.out.println("Scan of " + tableName +
	 * " for items with a price less than 100."); Iterator<Item> iterator =
	 * items.iterator(); while (iterator.hasNext()) {
	 * System.out.println(iterator.next().toJSONPretty()); } }
	 */ private static void FindVendorsWithinBudget(DynamoDBMapper mapper, int numberOfThreads)
			throws Exception {

		System.out.println("Find all search Results based on type");
		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val1", new AttributeValue().withS("foo3"));
		
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
				.withFilterExpression("usr = :val1").withExpressionAttributeValues(eav);

		List<PendingEventsSearch> scanResult = mapper.parallelScan(PendingEventsSearch.class, scanExpression,
				numberOfThreads);
		for (PendingEventsSearch results : scanResult) {
			System.out.println(results);
		}
		    
		//return scanResult;
	}
	}


