package com.tasks;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import com.daemonservices.WeddingPlannerExecutor;

public class FetchTweetsTask implements Runnable {
	private static final Properties twiterProp = WeddingPlannerExecutor.
			getPropertiesFile("TwitterConfig.properties");

	public static class Location {
		public double latitude;
		public double longitude;
		public String sentiment;
	}
	List<String> tracks;
	
	public FetchTweetsTask(String clientName, String ... keywordsToTrack) {
		this.tracks = new ArrayList<String>();
		if(keywordsToTrack != null) {
			for(String s : keywordsToTrack) {
				this.tracks.add(s);
			}
		}
	}
	
	private Response connectWithTwitter() {
		OAuthService service = new ServiceBuilder()
                .provider(TwitterApi.class)
                .apiKey(FetchTweetsTask.twiterProp.getProperty("consumer-key"))
                .apiSecret(FetchTweetsTask.twiterProp.getProperty("consumer-secret"))
                .build();

        Token accessToken = new Token(FetchTweetsTask.twiterProp.getProperty("access-token"), 
				FetchTweetsTask.twiterProp.getProperty("access-secret"));

        System.out.println("Trying to plea Twitter for Tweets....");
        OAuthRequest request = new OAuthRequest(Verb.POST, "https://stream.twitter.com/1.1/statuses/filter.json");
        request.addHeader("version", "HTTP/1.1");
        request.addHeader("host", "stream.twitter.com");
        request.setConnectionKeepAlive(true);
        request.addHeader("user-agent", "Twitter Stream Reader");
        request.addBodyParameter("track", this.convertArrayToDelimiterSepString(','));
        service.signRequest(accessToken, request);
        Response response = request.send();
        
        if(response != null) {
        	System.out.println("Connection Established...");
        }
        return response;
	}
	
	private String convertArrayToDelimiterSepString(char delimiter) {
		StringBuilder sb = new StringBuilder();
		for(String s : this.tracks) {
			sb.append(s);
			sb.append(delimiter);
		}
		
		return sb.substring(0, sb.length()-1).toString();
	}
	@Override
	public void run() {
        Response response = this.connectWithTwitter();

        BufferedReader br = new BufferedReader(new InputStreamReader(response.getStream()));
        String tweetMessage;
        QueueService serv = new QueueService();
        try {
			while ((tweetMessage = br.readLine()) != null) {
			
				
				serv.sendtoQ(tweetMessage);
				
			}
		} catch ( Exception e) {
			e.printStackTrace();
		}
	}
}
