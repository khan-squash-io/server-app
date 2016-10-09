package io.khan.squash.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import io.khan.squash.model.Media;
import io.khan.squash.upload.persistence.S3FileUploader;
import spark.Request;
import spark.Response;

public class MediaService {

	final S3FileUploader fileUpLoader = new S3FileUploader();

	public Media upload(Request request, Response response) {
		Media media = new Media();
		try {

			boolean isMultipart = ServletFileUpload.isMultipartContent(request.raw());
			if (isMultipart) {

				String phoneNumber = request.params(":phone_number");
				System.out.println("adding media with phone number : " + phoneNumber);
				media.setId("added new file for phone number : " + phoneNumber);
/*
				String type = (String) request.headers("content-type");

				if (type != null && type.startsWith("multipart/form-data")) {
					try {
						int boundaryIndex = type.indexOf("boundary=");
						byte[] boundary = (type.substring(boundaryIndex + 9)).getBytes();

						// Construct a MultiPartStream with request.in as
						// InputStream
						MultipartStream multipartStream = new MultipartStream(request.raw().getInputStream(), boundary);
						
						boolean nextPart = multipartStream.skipPreamble();
						while (nextPart) {
							String header = multipartStream.readHeaders();
							System.out.println("");
							System.out.println("Headers:");
							System.out.println(header);
							System.out.println("Body:");
							multipartStream.readBodyData(System.out);
							System.out.println("");
							nextPart = multipartStream.readBoundary();
						}
					
					} catch (Exception e) {
						System.out.println(e.toString());
					}

				}
 				*/
				ServletFileUpload upload = new ServletFileUpload();
				FileItemIterator iter = upload.getItemIterator(request.raw());

				while (iter.hasNext()) {
					FileItemStream item = iter.next();
					String name = item.getFieldName();
					InputStream stream = item.openStream();
					if (item.isFormField()) {
						System.out.println(
								"Form field " + name + " with value " + Streams.asString(stream) + " detected.");
					} else {
						System.out.println("processing non field");
						try {
							System.out.println("result = " + fileUpLoader.put(item));
						} catch (Exception e) {
							System.out.println("error pushing file up to S3, : " + e.toString());
						}
					}
				}

			}
		} catch (Exception e) {
			System.out.println("error :" + e.toString());
		}
		return media;
	}

	public Media delete(Request request, Response response) {

		String id = request.params(":id");
		System.out.println("deleting media with id : " + id);
		Media media = new Media();
		media.setId("deleted id : " + id);
		return media;
	}

	public List<Media> list(Request request, Response response) {
		List<Media> mediaItems = new ArrayList<Media>();
		Media media = new Media();
		media.setId("test");
		mediaItems.add(media);
		return mediaItems;

	}

}
