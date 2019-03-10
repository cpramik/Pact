package org.pact.java.example;

import org.junit.runner.RunWith;

import au.com.dius.pact.provider.junit.PactRunner;
import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.junit.loader.PactBrokerAuth;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit.target.HttpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;

@RunWith(PactRunner.class) // Say JUnit to run tests with custom Runner
@Provider("userservice") // Set up name of tested provider
//@PactFolder("pacts")

 @PactBroker(host = "https://cpramik.pact.dius.com.au", authentication
 = @PactBrokerAuth(username = "rkoi3a2zofejmvwdqrlnoa", password =
 "cl1273g7akvjzzyyhn712"))
public class ContractTestJunit4 {
	
	@TestTarget
	public final Target target = new HttpTarget("http", "localhost", 8080, "/");
	
	@State("test GET") 
	  public void toGetState() { 
		  System.out.println("GET REQUEST STATE EXECUTED--------------------------------");
	  }
	 
}
