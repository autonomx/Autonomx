package module.serviceUiIntegration.panel;

/**
 * Rebuild or clean project after adding new panel or removing to generate associated files
 */
import org.json.JSONException;

import configManager.ConfigVariable;
import core.apiCore.interfaces.RestApiInterface;
import core.helpers.Helper;
import core.support.annotation.Panel;
import core.support.configReader.Config;
import core.support.objects.ServiceObject;
import core.support.objects.TestObject;
import data.Data;
import io.restassured.response.Response;
import module.common.data.CommonUser;
import moduleManager.module.serviceUiIntegration.PanelManager;
import serviceManager.Service;

@Panel
public class UserPanel {

	// ensure the proper panel manager is imported, containing project "test.module name2".
	//eg. moduleManager.module.<test.module name2>.PanelManager
	PanelManager manager;
	public UserPanel(PanelManager manager) {
		this.manager = manager;
	}

	// Actions
	//--------------------------------------------------------------------------------------------------------	
	/**
	 * call create user rest api interface with using csv keyword
	 * csv file located at autonomx -> resources -> api -> keywords -> Keyword_RestApi.csv
	 * @param user
	 */
	public void createUser(CommonUser user) {

		Config.putValue("personUsername", user.username);
		Config.putValue("personEmail", user.email);
		Config.putValue("personPassword", user.password);
		Config.putValue("personConfirmed", Boolean.toString(user.confirmed));
		
		ServiceObject userAPI = TestObject.getApiDef("createUser");
		RestApiInterface.RestfullApiInterface(userAPI);
	}
	
	/**
	 * call create user rest api interface without using csv keyword
	 * @param user
	 * @return 
	 */
	public Response createUserUsingServiceObject(CommonUser user) {
		ConfigVariable.setValue("personUsername", user.username);
		ConfigVariable.setValue("personEmail", user.email);
		ConfigVariable.setValue("personPassword", user.password);
		ConfigVariable.setValue("personConfirmed", Boolean.toString(user.confirmed));
		
		ServiceObject userAPI = Service.create()
		.withUriPath("/content-manager/collection-types/plugins::users-permissions.user")
		.withContentType("application/json")
		.withMethod("POST")
		.withRequestHeaders("Authorization: Bearer <@accessTokenAdmin>")
		.withRequestBody("{\"confirmed\":<@personConfirmed>,\"blocked\":false,\"username\":\"<@personUsername>\",\"email\":\"<@personEmail>\",\"password\":\"<@personPassword>\"}");
		
		return RestApiInterface.RestfullApiInterface(userAPI);
	}

	public void deleteUser() {

		ServiceObject api = TestObject.getApiDef("deleteUser");
		RestApiInterface.RestfullApiInterface(api);
	}

	/**
	 * deletes all companies with prefix
	 *runApiContaining("name2", "zzz_","getUsers",
	 * "id","userId","deleteUser") get all users with name2 containing
	 * zzz_, Then gets id of these users, stores them in userId variable And
	 * calls deleteUser
	 * @param prefix
	 * @throws JSONException
	 */
	public void deleteAllUsers(String prefix) throws JSONException {
		Helper.runApiContaining("$.results.*.username", prefix, "getUsers", "$.results.*.id", "userLoginId", "deleteUser");
	}

	/**
	 * csv file located at autonomx -> resources -> api -> keywords -> Keyword_RestApi.csv
	 * @return
	 */
	public CommonUser loginAndCreateUserWithCsvKey() {
		CommonUser user = Data.common.commonuser().withAdminLogin();
		manager.login.login(user);

		// create user through api
		user = Data.common.commonuser().withDefaultUser(); 
		manager.user.createUser(user);
		return user;
	}
	
	public CommonUser loginAndCreateUserWithoutCsvKey() {
		CommonUser user = Data.common.commonuser().withAdminLogin();
		manager.login.login(user);

		// create user through api
		user = Data.common.commonuser().withDefaultUser(); 
		manager.user.createUserUsingServiceObject(user);
		return user;
	}
}