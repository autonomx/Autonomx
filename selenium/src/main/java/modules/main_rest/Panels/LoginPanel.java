package modules.main_rest.Panels;

import common.objects.UserObject;
import core.apiCore.interfaces.restApiInterface;
import core.helpers.Helper;
import core.support.configReader.Config;
import core.support.objects.ApiObject;
import core.support.objects.TestObject;
import modules.main_rest.StrapiRest;

public class LoginPanel {

	StrapiRest manager;

	public LoginPanel(StrapiRest manager) {
		this.manager = manager;
	}

	public void login(UserObject user) {
		Config.putValue("username", user.username().get());
		Config.putValue("password", user.password().get());

		ApiObject login = TestObject.getApiDef("getToken");
		restApiInterface.RestfullApiInterface(login);
	}
}