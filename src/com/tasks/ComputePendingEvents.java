package com.tasks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.daemonservices.WeddingPlannerExecutor;
import com.models.PendingEventsSearch;
import com.models.RecommendationResults;

public class ComputePendingEvents {
	private static final Properties awsCredentialsFile = WeddingPlannerExecutor
			.getPropertiesFile("AwsCredentials.properties");
	static BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsCredentialsFile.getProperty("accessKey"),
			awsCredentialsFile.getProperty("secretKey"));
	static int NUM_THREADS = 2;
	static AmazonDynamoDBClient client = new AmazonDynamoDBClient(awsCreds);
	DynamoDB dynamoDB = new DynamoDB(client);
	DynamoDBMapper mapper = new DynamoDBMapper(client);

	public  List<PendingEventsSearch> FindVendorsWithinBudget(String name) throws Exception {

		System.out.println("Find all search Results based on type");
		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val1", new AttributeValue().withS(name));
		
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
				.withFilterExpression("usr = :val1").withExpressionAttributeValues(eav);

		List<PendingEventsSearch> scanResult = mapper.parallelScan(PendingEventsSearch.class, scanExpression,
				NUM_THREADS);
		for (PendingEventsSearch results : scanResult) {
			System.out.println(results);
		}
		    
		return scanResult;
	}
	
	
}
