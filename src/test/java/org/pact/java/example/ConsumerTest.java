package org.pact.java.example;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.FastDateFormat;
import org.hamcrest.Matchers;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactFolder;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import io.pactfoundation.consumer.dsl.LambdaDsl;
import io.vertx.core.json.JsonObject;

@PactFolder("pacts")
public class ConsumerTest {
	private static final LocalDateTime LAST_LOGIN = LocalDateTime.of(2018, 10, 16, 12, 34, 12);
	
	@ClassRule
	public static RandomPortRule randomPort = new RandomPortRule();
	
	@Rule
	public PactProviderRuleMk2 mockProvider = new PactProviderRuleMk2("userservice", "localhost", randomPort.getPort(), this);
	
	@Pact(consumer="test_consumer")
	public RequestResponsePact pactGetAllUsers(PactDslWithProvider builder) {
		Map<String, String> headers = new HashMap<>(1);
		headers.put("Content-Type", "application/json");
		
		  DslPart body = new PactDslJsonBody() .stringType("name", "pramik")
		  .integerType("userId", 100).stringType("address", "asdas");
		
		return builder
				.given("GET all users")
				.uponReceiving("A request to /users")
				.path("/users")
				.method("GET")
				.willRespondWith()
				.status(200)
				.headers(headers)
				.body(body)
				.toPact();
				
	}
	
	@Pact(consumer="test_consumer")
	public RequestResponsePact pactUserExists(PactDslWithProvider builder) {
		DslPart body = LambdaDsl.newJsonBody(o -> o.stringType("name", "contract test")
				.timestamp("lastLogin", "yyyy-MM-dd'T'HH:mm:ss", Date.from(LAST_LOGIN.atZone(ZoneId.systemDefault()).toInstant()))
				.stringMatcher("role", "ADMIN|USER", "ADMIN")
				.minArrayLike("friends", 0,2, friend -> friend
						.stringType("id", "2")
						.stringType("name", "a friend"))).build();
				
		return builder
				.given("User 1 exists")
				.uponReceiving("A request to /users/1")
				.path("/users/1")
				.method("GET")
				.willRespondWith()
				.status(200)
				.body(body)
				.toPact();
		
	}
	
	/*
	 * @Pact(consumer="test_consumer") public RequestResponsePact
	 * pactUserDoesNotExist(PactDslWithProvider builder) { return builder
	 * .given("User 2 does not exist") .uponReceiving("A request to /users/2")
	 * .path("/users/2") .method("GET") .willRespondWith() .status(404) .toPact();
	 * 
	 * }
	 */
	
	@Pact(consumer="test_consumer")
	public RequestResponsePact pactCreateUser(PactDslWithProvider builder) {
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		
		DslPart requestBody = LambdaDsl.newJsonBody(o -> o.stringType("name", "user1")
				.stringType("phone", "123456789")).build();
		DslPart responseBody = LambdaDsl.newJsonBody(o -> o.numberType("id", 200)).build();
		
		return builder
				.given("Create New User")
				.uponReceiving("A request to create user on /users")
				.path("/users")
				.method("POST")
				.headers(headers)
				.body(requestBody)
				.willRespondWith()
				.status(201)
				.body(responseBody)
				.toPact();
				
	}
	
	
	@Test
    @PactVerification(fragment="pactGetAllUsers")
    public  void runTest() {  	
        //System.setProperty("pact.rootDir","pacts");  // Change output dir for generated pact-files
        RestAssured
        .given()
        .port(randomPort.getPort())
        .contentType(ContentType.JSON)
        .get("/users")
        .then()
        .statusCode(200)
        .assertThat()
        .body("userId", Matchers.equalTo(100))
        .and()
        .body("name", Matchers.equalTo("pramik"))
        .and()
        .body("address", Matchers.isA(String.class));
     
    }
	
	@Test
	@PactVerification(fragment="pactUserExists")
	public void userExists() {
		
		 RestAssured
	        .given()
	        .port(randomPort.getPort())
	        .contentType(ContentType.JSON)
	        .get("/users/1")
	        .then()
	        .statusCode(200)
	        .assertThat()
	        .body("lastLogin", Matchers.equalTo(LAST_LOGIN.toString()))
		 	.and()
		 	.body("name", Matchers.equalTo("contract test"))
		 	.and()
		 	.body("role", Matchers.equalTo("ADMIN"))
		 	.and()
		 	.body("friends", Matchers.hasSize(2));
	}
	
	@Test
	@PactVerification(fragment="pactCreateUser")
	public void createNewUser() {
		RestAssured
        .given()
        .port(randomPort.getPort())
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(new JsonObject().put("name", "user2").put("phone", "324234324").encode())
        .when()
        .post("/users")
        .then()
        .statusCode(201)
        .body("id", Matchers.equalTo(200))
        .and()
        .body("id", Matchers.isA(Integer.class));
	}
	
	/*
	 * @Test
	 * 
	 * @PactVerification(fragment = "pactUserDoesNotExist") public void
	 * userDoesNotExist() { RestAssured .given() .port(randomPort.getPort())
	 * .contentType(ContentType.JSON) .get("/users/2") .then() .statusCode(404); }
	 */
}
