Main Site: https://autonomx.io/

Documentation: https://docs.autonomx.io

Autonomx Core: https://github.com/autonomx/AutonomxCore

# Autonomx

Autonomx provides a complete testing platform for UI (Web, iOS, Android, Win) and API testing. It provides a feature rich and viable testing solution for end to end testing. They’re designed to be fast, scalable, reliable and adaptable to any requirements for ever growing projects.  

# Overview

* Open source UI automation testing framework based on Webdriver/Appium, TestNG/Junit, with maven integration. 
* Unifies mobile and web testing, using a common, version controlled code base (Autonomx Core)
* Each testing project is treated as a client for the Autonomx Core, meaning one central code base for all UI testing projects
* A client can have multiple test projects, as well as multiple platforms (web, Android, iOS, Win), associated with it.
* Modular design. Each project/component is treated as a module,  fully capable of interacting with one another. This allows for multi component and multiplatform testing. Eg. Create user through component A (API), validate in component B (web), do action in component C (Android), validate results in component D (iOS)
* All interaction with the UI are through utility functions called Helpers, which are stable and optimized set of wrapper functions, removing inconsistencies in coding styles and enforcing testing best practices 
* Integrates seamlessly with the API testing framework for end to end testing
* Detailed reports through ExtentTest reports 


# Prerequisites:

### Web:
* Java jdk-1.8 or higher
* maven 3.3.x or higher (maven will automatically download by running setup.bat/sh script)
* https://docs.autonomx.io/prerequisites

### Android:
* appium "npm install -g appium"
* Android SDK (for Android execution | with _ANDROID_HOME_ and _PATH_ configured)
* https://docs.autonomx.io/prerequisites/android
* https://docs.autonomx.io/getting-started/android-tests

### iOS:
* appium
* XCode and an iPhone Simulator (for iOS execution)
* https://docs.autonomx.io/prerequisites/ios
* https://docs.autonomx.io/getting-started/ios-tests

### IDE Setup
* For Intellij IDE, navigate to Intellij Setup Page: https://docs.autonomx.io/getting-started/ide/intellij
* For Eclipse IDE, navigate to Eclipse Setup Page: https://docs.autonomx.io/getting-started/ide/eclipse

# Getting Started:

* Download the latest release of Autonomx based on your project needs
* Run the setup: 
* setup.bat (windows), setup.sh (osx, linux)
* This will download maven if not installed, and all the required dependencies
* Navigate to runner//. eg. runner/mac/restTests.sh
* The run scripts are generated from testng testSuites by runner/generateScripts.sh. Each script is associated with a suite of tests
* https://docs.autonomx.io/quick-start

# Reporting

* Extent Reports is used for BDD style reporting of the test results

![alt text](https://blobscdn.gitbook.com/v0/b/gitbook-28427.appspot.com/o/assets%2F-LZhvc5eykluSdIwbEC_%2F-LdCijjMRz-r0ZZjwm8f%2F-LdCil5VspIaJnHPXUmm%2Fimage.png?alt=media&token=8b975eea-f238-4bd7-bf13-76a576517a14)

# Web Tests
* https://docs.autonomx.io/getting-started/web-tests
* Example project: Autonomx ▸ ⁨automation⁩ ▸ ⁨src⁩ ▸ ⁨main⁩ ▸ ⁨java⁩ ▸ ⁨modules⁩ ▸ ⁨webApp⁩
* Setup locators
	webApp ▸ LoginPanel.java
		
```java

	 public static class elements {
            public static EnhancedBy USER_NAME_FIELD = Element.byCss("[placeholder='John Doe']", "username field");
            public static EnhancedBy PASSWORD_FIELD = Element.byCss("#password", "password field");
            public static EnhancedBy LOGIN_SUBMIT = Element.byCss("[type='submit']", "submit button");
            public static EnhancedBy LOGOUT_BUTTON = Element.byCss("[href*='logout']", "logout button");
            public static EnhancedBy MAIN_SITE = Element.byCss(".main-site", "main site button");
            public static EnhancedBy ERROR_MESSAGE = Element.byCss("[class*='InputErrors']", "input errors");

            public static EnhancedBy LOADING_INDICATOR = Element.byCss("[class*='Loading']", "loading indicator");

        }

	
```
* Define actions
	webApp ▸ LoginPanel.java
```java 
		/**
      * enter login info and click login button
      * 
      * @param user
      */
     public void login(User user) {
         setLoginFields(user);
         Helper.form.formSubmit(elements.LOGIN_SUBMIT, MainPanel.elements.ADMIN_LOGO, elements.LOADING_INDICATOR);

     }

     public void loginError(User user) {
         setLoginFields(user);
         Helper.form.formSubmit(elements.LOGIN_SUBMIT, elements.ERROR_MESSAGE);
     }

     public void relogin(User user) {
         manager.main.logout();
         login(user);
     }

     public void setLoginFields(User user) {
         Helper.form.setField(elements.USER_NAME_FIELD, user.username().get());
         Helper.form.setField(elements.PASSWORD_FIELD, user.password().get());
     }
```
* Define objects
	* autonomx⁩ ▸ ⁨automation⁩ ▸ ⁨src⁩ ▸ ⁨main⁩ ▸ ⁨java⁩ ▸ ⁨module ▸ webApp ▸ user.csv
	* We are going to use the csv file to setup our data. For more info Csv
	 ![alt text](https://blobscdn.gitbook.com/v0/b/gitbook-28427.appspot.com/o/assets%2F-LZhvc5eykluSdIwbEC_%2F-Lbz0m5YFq7lUruvn4Bq%2F-Lbz18vf1jgQKNjEp4Ey%2Fimage.png?alt=media&token=6d7c7cb6-f5b8-4eab-b35d-81fae69b2fe4)	
	

* setup test

```java 

     @BeforeMethod
     public void beforeMethod() throws Exception {
        setupWebDriver(app.webApp.getWebDriver());
    }

    @Test
    public void validate_user_Login() {
        UserObject user = UserObject.user().withAdminLogin();
        TestLog.When("I login with admin user");
        app.strapi.login.login(user);

        TestLog.Then("I verify admin logo is displayed");
        Helper.verifyElementIsDisplayed(MainPanel.elements.ADMIN_LOGO);

        TestLog.When("I logout");
        app.strapi.main.logout();

        TestLog.Then("I should see the login panel");
        Helper.verifyElementIsDisplayed(LoginPanel.elements.LOGIN_SUBMIT);
    }
	
```

# Service Level Tests
 * https://docs.autonomx.io/service-level-testing
 	* Service level testing encompasses any backend, service, api, and database level testing
 	* These tests are compromised of: request, response, and verification
 	* Since these follow the same template, we have opted for using csv file to write the tests
 	* 1 line 1 complete test 
 	* This allows us to add lots of tests to each csv file, covering large number of permutations
 
 * Add Test cases in CSV file at apiTestData/testCases
 * Run tests using the runner at apiTestData/runner/<os>/apiRunner
 * CSV files will run in parallel
 * Parallel run value can be set at automation/resources/properties.property "parallel_test_count"
  ![alt text](https://blobscdn.gitbook.com/v0/b/gitbook-28427.appspot.com/o/assets%2F-LZhvc5eykluSdIwbEC_%2F-LcZtOaQshb5y9jBNTv4%2F-LcZtovj2NX0VVwc5weJ%2Fimage.png?alt=media&token=fc5ed2ae-8d03-4170-ab2b-ef95d4c83760)	

# Service UI Integration
* https://docs.autonomx.io/service-level-testing/service-ui-integration
* We can run our service test with the UI based tests
* There are 2 methods of achieving this goal
	* Creating a service object and calling the service interface directly
	``` java 
	public Response loginWithServiceObject(CommonUser user) {
		
		ServiceObject loginApi = Service.create()
				.withUriPath("/auth/local")
				.withContentType("application/json")
				.withMethod("POST")
				.withRequestBody("{" + 
						"\"identifier\": \""+ user.username +"\",\r\n" + 
						"\"password\": \"" +user.password + "\"" + 
						"}")
				.withOutputParams(
						"user.role.id:<$roles>;"
						+ "jwt:<$accessTokenAdmin>;"
						+ "user.id:<$userId>");
				
		return RestApiInterface.RestfullApiInterface(loginApi);
	}
	```
	* Creating a service keyword in a csv file and calling the test case name
	![alt text](https://blobscdn.gitbook.com/v0/b/gitbook-28427.appspot.com/o/assets%2F-LZhvc5eykluSdIwbEC_%2F-Lr2tMRfr9spslKkUR1c%2F-Lr2u7t4K_FWlspqTn78%2Fimage.png?alt=media&token=f9352116-b196-41ec-b638-a65e44f8b97f
	``` java)
	
	public void login(CommonUser user) {
		Service.getToken
				.withUsername(user.username)
				.withPassword(user.password)
				.build();
	}
	```
	
# Script Runner
* https://app.gitbook.com/@autonomx/s/autonomx/script-runner-1/installation
* Script Server will run the test suites on a web browser
* Script Server will run the tests defined in Test Suite 
 ![alt text](https://blobscdn.gitbook.com/v0/b/gitbook-28427.appspot.com/o/assets%2F-LZhvc5eykluSdIwbEC_%2F-LZreOiuGev2RX9keLwq%2F-LZrstTAfjFPl8M060SX%2Fimage.png?alt=media&token=211189ce-6708-4b5f-8fd6-b35ef7fb4a36)
