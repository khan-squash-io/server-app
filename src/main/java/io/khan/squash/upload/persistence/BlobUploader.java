package io.khan.squash.upload.persistence;

import java.io.File;
import java.io.IOException;

import org.apache.commons.codec.Charsets;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.fileupload.FileItemStream;
import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.domain.Blob;

import com.google.common.io.ByteSource;

public class BlobUploader {

	private static final String CONTAINER = "khan-squash";

	private Configurations configs = new Configurations();

	public void  put(FileItemStream fileItemStream) throws IOException, ConfigurationException {
		Configuration config = configs.properties(new File("AwsCredentials.properties"));

		String accesskeyid = config.getString("accesskeyid");
		String secretkey = config.getString("secretkey");

		// get a context with amazon that offers the portable BlobStore API
		BlobStoreContext context = ContextBuilder.newBuilder("aws-s3").credentials(accesskeyid, secretkey)
				.buildView(BlobStoreContext.class);
		// Access the BlobStore
		BlobStore blobStore = context.getBlobStore();

		// Create a Container
		blobStore.createContainerInLocation(null, CONTAINER);

		// Create a Blob
		ByteSource payload = ByteSource.wrap("blob-content".getBytes(Charsets.UTF_8));
		Blob blob = blobStore.blobBuilder("test") // you can use folders via
													// blobBuilder(folderName +
													// "/sushi.jpg")
				.payload(payload).contentLength(payload.size()).build();

		// Upload the Blob
		blobStore.putBlob(CONTAINER, blob);

		// Don't forget to close the context when you're done!
		context.close();

	}
}
