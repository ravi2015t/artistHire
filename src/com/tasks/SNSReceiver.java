package com.tasks;
                        
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.ConfirmSubscriptionRequest;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.daemonservices.ElasticSearchHose;
import com.daemonservices.WeddingPlannerExecutor;
import com.models.Tweet;

import org.codehaus.jackson.map.ObjectMapper;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;
// should be started as a separate thread

@SuppressWarnings("unused")
public class SNSReceiver implements Runnable {

    private static final Properties awsCredentialsFile = WeddingPlannerExecutor.
			getPropertiesFile("AwsCredentials.properties");

    // Shared queue for notifications from HTTP server
    static BlockingQueue<Map<String, String>> messageQueue = new LinkedBlockingQueue<Map<String, String>>();
    ElasticSearchHose hose = new ElasticSearchHose();
    ObjectMapper mapper = new ObjectMapper();
    
    // Receiver loop
    public void run() {

      try{
    	BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsCredentialsFile.getProperty("accessKey"), awsCredentialsFile.getProperty("secretKey"));

        AmazonSNSClient service = new AmazonSNSClient(awsCreds);
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
    	service.setRegion(usWest2);
   
        URL url = new URL("http://169.254.169.254/latest/meta-data/public-hostname");
	    URLConnection conn = url.openConnection();
	    Scanner s = new Scanner(conn.getInputStream());
        String publicIp="";
        if (s.hasNext()) {
	        publicIp = s.next();
	       System.out.println("Public IP" + publicIp);
	      } 
        // Subscribe to topic
        SubscribeRequest subscribeReq = new SubscribeRequest()
            .withTopicArn("arn:aws:sns:us-west-2:940369824216:twitter")
            .withProtocol("http")
           // .withEndpoint("http://" + publicIp + "/rest/tweets/post/" );
            .withEndpoint("http://twirrmap555-env.us-west-2.elasticbeanstalk.com/rest/tweets/post");
        service.subscribe(subscribeReq);
        System.out.println("After subscription");

    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		
    	}
    }


}
