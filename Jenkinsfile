pipeline {
	agent any
	stages{
	    
	    stage('Compile & Testing Stage'){
	        steps {
    		        sh './gradlew clean test'
	    		}
	    }
	    
	    stage('Publish Pact'){
	        steps{
	            sh './gradlew pactPublish -Dpact.consumer.version=${GIT_COMMIT} -Dpact.tag=${BRANCH_NAME}'
	        }

	    }



	}

}