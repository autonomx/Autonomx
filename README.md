Documentation: https://docs.autonomx.io

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
* https://app.gitbook.com/@autonomx/s/autonomx/getting-started/android-tests

### iOS:
* appium
* XCode and an iPhone Simulator (for iOS execution)
* https://docs.autonomx.io/prerequisites/ios
* https://app.gitbook.com/@autonomx/s/autonomx/getting-started/ios-tests

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
		public void login(UserObject user) {
			setLoginFields(user);
			Helper.form.formSubmit(elements.LOGIN_SUBMIT, MainPanel.elements.ADMIN_LOGO, elements.LOADING_INDICATOR);

		}

		public void loginError(UserObject user) {
			setLoginFields(user);
			Helper.form.formSubmit(elements.LOGIN_SUBMIT, elements.ERROR_MESSAGE);
		}

		public void relogin(UserObject user) {
			manager.main.logout();
			login(user);
		}

		public void setLoginFields(UserObject user) {
			Helper.form.setField(elements.USER_NAME_FIELD, user.username().get());
			Helper.form.setField(elements.PASSWORD_FIELD, user.password().get());
		}
```
* Define objects
	* ⁨Autonomx ▸ ⁨automation⁩ ▸ ⁨src⁩ ▸ ⁨main⁩ ▸ ⁨java⁩ ▸ ⁨common⁩ ▸ ⁨objects⁩
```java
	/**
	 * object
	 */
	public abstract Optional<String> name();

	public abstract Optional<String> username();

	public abstract Optional<String> password();

	public abstract Optional<String> email();
	
		/**
	 * Predefined objects
	 * 
	 * @return
	 */
	public UserObject withAdminLogin() {
		return new UserObject.Builder().username(ADMIN_USER).password(ADMIN_PASSWORD).buildPartial();
	}
	
```
* setup test

```java 

	@BeforeMethod
	public void beforeMethod() throws Exception {
		setupWebDriver(app.strapi.getDriver());
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

# Api Tests
 * Setup Config
 	* Set root uri path at apiTestData/apiConfig.property. eg. UriPath = http://demo.autonomx.io
	* Additional properties such as database access can be set there
 * Add Test cases in CSV file at apiTestData/testCases
 * Run tests using the runner at apiTestData/runner/<os>/apiRunner
 * CSV files will run in parallel
 * Parallel run value can be set at automation/resources/properties.property "parallel_test_count"
