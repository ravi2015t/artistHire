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
import com.models.RecommendationResults;

public class ComputeRecommendation {
	private static final Properties awsCredentialsFile = WeddingPlannerExecutor
			.getPropertiesFile("AwsCredentials.properties");
	static BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsCredentialsFile.getProperty("accessKey"),
			awsCredentialsFile.getProperty("secretKey"));
	static int NUM_THREADS = 2;
	static AmazonDynamoDBClient client = new AmazonDynamoDBClient(awsCreds);
	 DynamoDB dynamoDB = new DynamoDB(client);
	 static DynamoDBMapper mapper = new DynamoDBMapper(client);

	public static  List<RecommendationResults> FindVendorsWithinBudget(String catgry, Long limit) throws Exception {

		System.out.println("Find all search Results based on type");
		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val1", new AttributeValue().withN(limit.toString()));
		eav.put(":val2", new AttributeValue().withS(catgry));

		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
				.withFilterExpression("price < :val1 and category = :val2").withExpressionAttributeValues(eav);

		List<RecommendationResults> scanResult = mapper.parallelScan(RecommendationResults.class, scanExpression,
				NUM_THREADS);
		
		scanResult = new ArrayList<RecommendationResults>(scanResult);
		
		for (RecommendationResults results : scanResult) {
			System.out.println(results);
		}
		 Collections.sort(scanResult, new Comparator<RecommendationResults>() {
			    @Override
				public int compare(RecommendationResults cr1,RecommendationResults cr2) {
					
					 return  Double.compare(cr2.getRating(), cr1.getRating());
				}
				
			});
		   
		return scanResult.subList(0, 4);
	}
	
	public static void main (String args[])
	{
		Long limit = (long) 25000;
		List<RecommendationResults> photoresults = new ArrayList<RecommendationResults>();
		try {
			photoresults = FindVendorsWithinBudget("photographer", limit);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
