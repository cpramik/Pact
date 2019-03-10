package org.pact.java.example;

import org.apache.http.HttpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import au.com.dius.pact.model.Interaction;
import au.com.dius.pact.model.Pact;
import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;

@Provider("userservice") // Set up name of tested provider
@PactFolder("pacts")
public class ProviderTest {

	@BeforeEach
	void setupTestTarget(PactVerificationContext context) {
		context.setTarget(new HttpTestTarget("localhost", 8081, "/"));
	}

	@TestTemplate
	@ExtendWith(PactVerificationInvocationContextProvider.class)
	void pactVerificationTestTemplate(Pact pact, Interaction interaction, HttpRequest request,
			PactVerificationContext context) {
		context.verifyInteraction();
	}
	
	  @State("GET all users") 
	  public void getAllUsers() { 
		  System.out.println("GET all users STATE EXECUTED--------------------------------");
	  }
	  
	  @State("User 1 exists") 
	  public void user1Exist() { 
		  System.out.println("User 1 exists STATE EXECUTED--------------------------------");
	  }
	  
	  @State("Create New User") 
	  public void createNewUser() { 
		  System.out.println("Create New User STATE EXECUTED--------------------------------");
	  }
	  
	/*
	 * @State("User 2 does not exist") public void user2DoesNotExist() { System.out.
	 * println("User 2 does not exist STATE EXECUTED--------------------------------"
	 * ); }
	 */
	 
}
