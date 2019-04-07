package module.serviceUiIntegration.panel;

/**
 * Rebuild or clean project after adding new panel or removing to generate associated files
 */
import org.json.JSONException;

import core.apiCore.interfaces.RestApiInterface;
import core.helpers.Helper;
import core.support.annotation.Panel;
import core.support.configReader.Config;
import core.support.objects.ServiceObject;
import core.support.objects.TestObject;
import data.Data;
import module.common.data.CommonUser;
import moduleManager.module.serviceUiIntegration.PanelManager;

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
	public void createUser(CommonUser user) {

		Config.putValue("personUsername", user.username);
		Config.putValue("personEmail", user.email);
		Config.putValue("personPassword", user.password);
		Config.putValue("personConfirmed", Boolean.toString(user.confirmed));

		ServiceObject companyAPI = TestObject.getApiDef("createUser");
		RestApiInterface.RestfullApiInterface(companyAPI);
	}

	public void deleteUser() {

		ServiceObject api = TestObject.getApiDef("deleteUser");
		RestApiInterface.RestfullApiInterface(api);
	}

	/**
	 * deletes all companies with prefix
	 *runApiContaining("name2", "zzz_","getUsers",
	 * "id","userId","deleteUser") get all users with name2 containing
	 * zzz_, Then gets id of these user, stores them in userId variable And
	 * calls deleteUser
	 * @param prefix
	 * @throws JSONException
	 */
	public void deleteAllUsers(String prefix) throws JSONException {
		Helper.runApiContaining("username", prefix, "getUsers", "id", "userId", "deleteUser");
	}

	public CommonUser loginAndCreateUser() {
		CommonUser user = Data.common.commonuser().withAdminLogin();
		manager.login.login(user);

		// create user through api
		user = Data.common.commonuser().withDefaultUser(); 
		manager.user.createUser(user);
		return user;
	}
}