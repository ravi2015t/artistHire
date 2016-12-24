package com.daemonservices;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.Settings.Builder;

import com.tasks.FetchTweetsTask;
import com.tasks.ReceiveProcessImage;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.mapping.PutMapping;

public class WeddingPlannerExecutor implements ServletContextListener {

	private static final Properties awsCredentialsFile = WeddingPlannerExecutor
			.loadPropertiesFile("AwsCredentials.properties");
	private static final Properties twitterConfFile = WeddingPlannerExecutor
			.loadPropertiesFile("TwitterConfig.properties");
	private static final JestClient esClient = WeddingPlannerExecutor.initializeESClient();
	private static final int numOfThreads = 6;

	private ExecutorService exeService;

	public WeddingPlannerExecutor() {
		this.exeService = Executors.newFixedThreadPool(WeddingPlannerExecutor.numOfThreads);
	}

	public static JestClient getESClient() {
		return WeddingPlannerExecutor.esClient;
	}

	public static Properties getPropertiesFile(String name) {
		if (name.equals("TwitterConfig.properties")) {
			return WeddingPlannerExecutor.twitterConfFile;
		} else if (name.equals("AwsCredentials.properties")) {
			return WeddingPlannerExecutor.awsCredentialsFile;
		}

		return null;
	}

	private static Properties loadPropertiesFile(String propFilePath) {
		Properties prop = new Properties();
		try {
			InputStream is = FetchTweetsTask.class.getClassLoader().getResourceAsStream(propFilePath);
			prop.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}

	private static JestClient initializeESClient() {
		System.out.println("Initializing Jest Client!!!");
		JestClientFactory factory = new JestClientFactory();
		factory.setHttpClientConfig(new HttpClientConfig.Builder(awsCredentialsFile.getProperty("es-endPoint"))
				.multiThreaded(true).build());
		System.out.println("Intialization done");
		return factory.getObject();

	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// Shuts all tasks immediately
		System.out.println("Shutting Down!!");
		this.exeService.shutdownNow();

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		this.exeService.execute(new ReceiveProcessImage());

	}

	public static void main(String[] args) {
		WeddingPlannerExecutor e = new WeddingPlannerExecutor();
		e.contextInitialized(null);
	}
}
