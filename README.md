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

* Java jdk-1.8 or higher
* Apache Maven 3 or higher
  Maven references:
* http://maven.apache.org/guides/getting-started/maven-in-five-minutes.html
* http://www.tutorialspoint.com/maven/maven_environment_setup.htm

#Appium parallel test execution
Here you'll find out how to execute tests in parallel using Appium and TestNG.

### What you need
1. Java JDK (with _JAVA_HOME_ and _PATH_ configured)
2. IDE (and import this project using Maven)
3. Android SDK (for Android execution | with _ANDROID_HOME_ and _PATH_ configured)
4. Android AVD created (or Genymotion)
5. XCode and an iPhone Simulator (for iOS execution)
6. Appium installed through npm

### Android

#### Configurations
To execute the examples over the Android platform you'll need:
* Android SDK
* Updated _Build Tools_, _Platform Tools_ and, at least, one _System Image (Android Version)_
* Configure Android Path on your environment variables
   * ANDROID_HOME: root android sdk directory
   * PATH: ANDROID_HOME + the following paths = _platform-tools_, _tools_, _tools/bin_ 
* And Android Virtual Device
   * AVD or Genymotion
   

#### Inspect elements on Android
You can use the [uiautomatorviewer](https://developer.android.com/training/testing/ui-testing/uiautomator-testing.html) to inspect elements on Android devices.
 or you can use [Appium Desktop](https://github.com/appium/appium-desktop)

### iOS

#### Configurations
To execute the examples over the iOS platform you'l need:
* MacOS machine :-)
* Xcode installed
* iPhone simulator (I recommend, for these tests iOS version > 10)
* Follow all the steps on [https://github.com/appium/appium-xcuitest-driver](https://github.com/appium/appium-xcuitest-driver)


#### Inspect elements on iOS
You also can use [Appium Desktop](https://github.com/appium/appium-desktop)
or you can use the [Macaca App Inspector](https://macacajs.github.io/app-inspector/)


# Execution:

* Clone the repository.
* Navigate to runner/<mac,win,linux)>/<test suite of your choice>. eg. runner/mac/restTests.sh
* The run scripts are generated from testng testSuites by runner/generateScripts.sh. Each script is associated with a suite of tests

# Reporting

* Extent report is used for reporting

# Getting Started

* Use IDE of choice: Eclipse, IntelliJ, NetBeans...

# Web Tests
 * Example project: ⁨Autonomx⁩ ▸ ⁨automation⁩ ▸ ⁨src⁩ ▸ ⁨main⁩ ▸ ⁨java⁩ ▸ ⁨modules⁩ ▸ ⁨webApp⁩
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
	* ⁨Autonomx⁩ ▸ ⁨automation⁩ ▸ ⁨src⁩ ▸ ⁨main⁩ ▸ ⁨java⁩ ▸ ⁨common⁩ ▸ ⁨objects⁩
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
 	* Set root uri path at apiTestData/apiConfig.property. eg. UriPath = http://45.76.245.149:1337
	* Additional properties such as database access can be set there
 * Add Test cases in CSV file at apiTestData/testCases
 * Run tests using the runner at apiTestData/runner/<os>/apiRunner
 * CSV files will run in parallel
 * Parallel run value can be set at automation/resources/properties.property "parallel_test_count"


