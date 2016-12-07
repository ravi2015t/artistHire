package com.tasks;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.daemonservices.WeddingPlannerExecutor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.models.ImageDetails;

public class ReceiveProcessImage implements Runnable{
	private static final Properties awsCredentialsFile = WeddingPlannerExecutor.
			getPropertiesFile("AwsCredentials.properties");
	BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsCredentialsFile.getProperty("accessKey"), awsCredentialsFile.getProperty("secretKey"));
	ObjectMapper mapper = new ObjectMapper();
    
    AmazonSQS sqs = getclient(awsCreds);
    String myQueueUrl = awsCredentialsFile.getProperty("myQueueUrl");
   
    
    public AmazonSQSClient getclient(BasicAWSCredentials awsCreds )
    {
    AmazonSQS sqsS = new AmazonSQSClient(awsCreds);
    Region usWest2 = Region.getRegion(Regions.US_WEST_2);
    sqsS.setRegion(usWest2);
    return (AmazonSQSClient) sqsS;
    	
    }
    @Override
	public void run() {
		System.out.println("URL" + myQueueUrl);	
		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(myQueueUrl);
		for(;;)
		{
		List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
		if(messages.size()>0)
		{
        for (Message message : messages) {
            System.out.println("  Message");
            System.out.println("    MessageId:     " + message.getMessageId());
            System.out.println("    ReceiptHandle: " + message.getReceiptHandle());
            System.out.println("    MD5OfBody:     " + message.getMD5OfBody());
            System.out.println("    Body:          " + message.getBody());
            
          try {
        	  
        	ImageDetails imd = mapper.readValue(message.getBody(), ImageDetails.class);
            if(imd!=null)
            {
            	//Push it to s3
            	
            }
          }
        
		catch (IOException e) {
			System.out.println("Failed while sending messages");
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Failed while sending messages");
			e.printStackTrace();
			}
        	}
		}
		else
		{
		 continue;
		}
	}

  }
}