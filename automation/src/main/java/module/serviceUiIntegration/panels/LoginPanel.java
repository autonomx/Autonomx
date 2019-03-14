package module.serviceUiIntegration.panels;

import common.objects.UserObject;
import core.apiCore.interfaces.RestApiInterface;
import core.support.annotation.Panel;
import core.support.configReader.Config;
import core.support.objects.ApiObject;
import core.support.objects.TestObject;
import moduleManager.module.serviceUiIntegration.PanelManager;

@Panel
public class LoginPanel {

	// ensure the proper panel manager is imported, containing project "module name".
	//eg. moduleManager.module.<module name>.PanelManager
	PanelManager manager;
	public LoginPanel(PanelManager manager) {
		this.manager = manager;
	}

	// Actions
	//--------------------------------------------------------------------------------------------------------	
	public void login(UserObject user) {
		Config.putValue("username", user.username().get());
		Config.putValue("password", user.password().get());

		ApiObject login = TestObject.getApiDef("getToken");
		RestApiInterface.RestfullApiInterface(login);
	}
}