package com.tasks;

import java.util.Properties;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.daemonservices.WeddingPlannerExecutor;

public class SendImagetoSQS {
	
	private static final Properties awsCredentialsFile = WeddingPlannerExecutor.
			getPropertiesFile("AwsCredentials.properties");


    public void sendtoQ(String t) throws Exception {
    	

        BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsCredentialsFile.getProperty("accessKey"), awsCredentialsFile.getProperty("secretKey"));
        

        AmazonSQS sqs = new AmazonSQSClient(awsCreds);
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        sqs.setRegion(usWest2);

        System.out.println("===========================================");
        System.out.println("Getting Started with Amazon SQS");
        System.out.println("===========================================\n");
        try {
        	//configure myQueueUrl
              String myQueueUrl = awsCredentialsFile.getProperty("myQueueUrl");
              System.out.println("q url" + myQueueUrl);
              
            
            // Send a message
            System.out.println("Sending a message to MyQueue.\n");
            
            sqs.sendMessage(new SendMessageRequest(myQueueUrl, t));

            } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered " +
                    "a serious internal problem while trying to communicate with SQS, such as not " +
                    "being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }

}
