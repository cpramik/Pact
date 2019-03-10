package org.pact.java.example;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class UserResource {

	private Service testService;
	
	public UserResource(Service service) {
		testService = service;
	}
	
	public void getUsers(RoutingContext context) {
		System.out.println(Thread.currentThread().getName() + "-----------------------------");
		HttpServerResponse response = context.response();
		response.putHeader("Content-Type", "application/json");
		response.setStatusCode(200);
		response.end(new JsonObject().put("userId", testService.getUsers().userId).put("address", "asdas").put("name", "pramik").encode());
	}
	
	public void getUserById(RoutingContext context) {
		HttpServerResponse response = context.response();
		response.putHeader("Content-Type", "application/json");
		response.setStatusCode(200);
		DateFormat source = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		String date = source.format(new Date());
		
		response.end(new JsonObject()
				.put("userId", context.pathParam("userId"))
				.put("phone", "24352354353")
				.put("name", "pramik")
				.put("role", "ADMIN")
				.put("lastLogin", date)
				.put("friends", new JsonArray()
						.add(new JsonObject()
								.put("id", "2")
								.put("name", "Ronal Smith"))
						.add(new JsonObject()
								.put("id", "3")
								.put("name", "Matt Spencer")
								)
						).encode()
				);
		
	}
	
	public void createUser(RoutingContext context) {
		HttpServerResponse response = context.response();
		response.setStatusCode(201);
		response.putHeader("Content-Type", "application/json");
		response.end(new JsonObject().put("id", 111).encode());
	}
}
