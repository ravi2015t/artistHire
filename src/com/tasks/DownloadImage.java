package com.tasks;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class DownloadImage {		
	/*
	 * get function is used to retreive images based on the folderName
	 * folderName = user/profile_pictures/user_id/ folderName =
	 * vendor/profile_pictures/vendor_id/ folderName = vendor/albums/vendor_id/
	 * 
	 */
	@SuppressWarnings("null")
	public static List<S3ObjectInputStream> get(String folderName) throws FileNotFoundException, IOException {
		AWSCredentials credentials = new ProfileCredentialsProvider().getCredentials();
		AmazonS3 s3client = new AmazonS3Client(credentials);
		String bucketName = "artist-hire";

		final ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName);
		ListObjectsV2Result result;
		GetObjectRequest request;
		List<S3ObjectInputStream> listOfImages = null;
		// int i = 0;
		do {
			result = s3client.listObjectsV2(req);

			for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
				request = new GetObjectRequest(bucketName, objectSummary.getKey());
				S3Object object = s3client.getObject(request);
				// S3ObjectInputStream objectContent =
				// object.getObjectContent();
				// IOUtils.copy(objectContent, new
				// FileOutputStream("C://Users//Shashank Pavan//Desktop//test" +
				// i + ".png"));
				// i++;
				listOfImages.add(object.getObjectContent());
			}
			req.setContinuationToken(result.getNextContinuationToken());
		} while (result.isTruncated() == true);
		return listOfImages;
	}
}
