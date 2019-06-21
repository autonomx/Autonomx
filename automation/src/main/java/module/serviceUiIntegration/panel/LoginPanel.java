package module.serviceUiIntegration.panel;

import core.support.annotation.Panel;
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
	public void login(CommonUser user) {
		Service.getToken
				.withUsername(user.username)
				.withPassword(user.password)
				.build();
	}
}