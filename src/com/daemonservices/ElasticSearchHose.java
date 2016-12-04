package com.daemonservices;

import java.io.IOException;
import java.util.Properties;

import com.models.Tweet;

import io.searchbox.client.JestClient;
import io.searchbox.core.Index;


public class ElasticSearchHose {
	private static final Properties awsCredentialsFile = WeddingPlannerExecutor.
			getPropertiesFile("AwsCredentials.properties");
	
	public void indexTweet(Tweet t) {
		JestClient esClient = WeddingPlannerExecutor.getESClient();
		Index index = new Index.Builder(t).index(awsCredentialsFile.getProperty("index-name")).
				type(awsCredentialsFile.getProperty("mapping-name")).build();
		  System.out.println("Indexing twettttt---------------");
                  System.out.println(t.getTweet());
		try {
			esClient.execute(index);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
