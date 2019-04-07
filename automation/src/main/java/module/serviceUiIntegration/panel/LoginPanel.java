package module.serviceUiIntegration.panel;

import core.apiCore.interfaces.RestApiInterface;
import core.support.annotation.Panel;
import core.support.configReader.Config;
import core.support.objects.ServiceObject;
import core.support.objects.TestObject;
import module.common.data.CommonUser;
import moduleManager.module.serviceUiIntegration.PanelManager;
import serviceManager.Service;

@Panel
public class LoginPanel {

	// ensure the proper panel manager is imported, containing project "test.module name2".
	//eg. moduleManager.module.<test.module name2>.PanelManager
	PanelManager manager;
	public LoginPanel(PanelManager manager) {
		this.manager = manager;
	}

	// Actions
	//--------------------------------------------------------------------------------------------------------	
	public void login3(CommonUser user) {
		Config.putValue("username", user.username);
		Config.putValue("password", user.password);

		ServiceObject login = TestObject.getApiDef("GetToken");
		RestApiInterface.RestfullApiInterface(login);
	}
	
	public void login(CommonUser user) {
		Service.getToken
				.withUsername(user.username)
				.withPassword(user.password)
				.build();
	}
}