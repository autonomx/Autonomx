package modules.main_rest.Panels;

import org.json.JSONException;

import common.objects.UserObject;
import core.apiCore.interfaces.restApiInterface;
import core.helpers.Helper;
import core.support.configReader.Config;
import core.support.objects.ApiObject;
import core.support.objects.TestObject;
import modules.main_rest.StrapiRest;

public class UserPanel {

	StrapiRest manager;

	public UserPanel(StrapiRest manager) {
		this.manager = manager;
	}

	public void createUser(UserObject user) {

		Config.putValue("personUsername", user.username().get());
		Config.putValue("personEmail", user.email().get());
		Config.putValue("personPassword", user.password().get());
		Config.putValue("personConfirmed", Boolean.toString(user.confirmed().get()));

		ApiObject companyAPI = TestObject.getApiDef("createUser");
		restApiInterface.RestfullApiInterface(companyAPI);
	}

	public void deleteUser() {

		ApiObject api = TestObject.getApiDef("deleteUser");
		restApiInterface.RestfullApiInterface(api);
	}

	/**
	 * deletes all companies with prefix
	 *runApiContaining("name", "zzz_","getUsers",
	 * "id","userId","deleteUser") get all users with name containing
	 * zzz_, then gets id of these user, stores them in userId variable and
	 * calls deleteUser
	 * @param prefix
	 * @throws JSONException
	 */
	public void deleteAllUsers(String prefix) throws JSONException {
		Helper.runApiContaining("username", prefix, "getUsers", "id", "userId", "deleteUser");
	}

	public UserObject loginAndCreateUser() {
		UserObject user = UserObject.user().withAdminLogin();
		manager.login.login(user);

		// create user through api
		 user = UserObject.user().withDefaultUser();
		manager.user.createUser(user);
		return user;
	}
}