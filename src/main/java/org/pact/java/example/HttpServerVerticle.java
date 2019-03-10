package org.pact.java.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.Router;

public class HttpServerVerticle extends AbstractVerticle{

	@Override
	public void start() {
		vertx.createHttpServer(new HttpServerOptions()).requestHandler(requestHandler())
		.listen(8081, result -> {
			if (result.succeeded()) {
				System.out.println("Server started---------------");
			} else {
				System.out.println("Server failed to start---------------");
			}
		});
	}
	
	private Handler<HttpServerRequest> requestHandler() {
		Router router = Router.router(vertx);
		registerTestUserURI(router);
		return router::accept;
	}
	
	private void registerTestUserURI(Router router) {
		UserResource resource = new UserResource(new UserService());
		router.get("/users").blockingHandler(resource::getUsers, false);
		router.get("/users/:userId").handler(resource::getUserById);
		router.post("/users").handler(resource::createUser);
	}
}
