package io.khan.squash.upload.persistence;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.io.IOUtils;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class S3FileUploader {

	private static final String bucketName = "cdn.khan.squash.io";

	public String put(FileItemStream fileItemStream) throws IOException {
		String result = "";
		try {

			AmazonS3 s3 = new AmazonS3Client(new ClasspathPropertiesFileCredentialsProvider());

			// work around to set content length accurately
			byte[] bytes = IOUtils.toByteArray(fileItemStream.openStream());
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);

			ObjectMetadata omd = new ObjectMetadata();
			omd.setContentType(fileItemStream.getContentType());
			omd.setContentLength(bytes.length);
			omd.setHeader("filename", fileItemStream.getName());

			
			String keyName = UUID.randomUUID() +".mp4";
			
			S3Object s3Object = new S3Object();
			s3Object.setObjectContent(bis);
			s3.putObject(new PutObjectRequest(bucketName, keyName, bis, omd)
					.withCannedAcl(CannedAccessControlList.PublicRead));

			s3Object.close();
			result = keyName;
		} catch (AmazonServiceException ase) {
			System.out
					.println("Caught an AmazonServiceException, which means your request made it to Amazon S3, but was "
							+ "rejected with an error response for some reason.");

			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());

			result = "Error-" + ase.getMessage();
		} catch (AmazonClientException ace) {
			System.out.println(
					"Caught an AmazonClientException, which means the client encountered an internal error while "
							+ "trying to communicate with S3, such as not being able to access the network.");

			result = "Error-" + ace.getMessage();
		} catch (Exception e) {
			result = "Error-" + e.getMessage();
		}
		return result;
	}
}