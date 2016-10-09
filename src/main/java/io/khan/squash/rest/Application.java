package io.khan.squash.rest;

import static io.khan.squash.utils.JsonUtil.json;
import static spark.Spark.after;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;

import io.khan.squash.service.MediaService;
import spark.route.RouteOverview;
import spark.servlet.SparkApplication;

public class Application implements SparkApplication {

	final MediaService mediaService = new MediaService();

	@Override
	public void init() {
		
		// media services

		get("/1/media/:phone_number/", (req, res) -> mediaService.list(req, res), json());

		get("/1/media/:phone_number/:id", (req, res) -> mediaService.list(req, res), json());

		post("/1/media/:phone_number", "multipart/form-data", (req, res) -> mediaService.upload(req, res), json());

		delete("/1/media/delete/:phone_number/:id", (req, res) -> mediaService.delete(req, res), json());

		after((req, res) -> {
			res.type("application/json");
		});
		
		RouteOverview.enableRouteOverview();
	}
}