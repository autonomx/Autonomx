package module.serviceUiIntegration.panel;

import core.apiCore.interfaces.RestApiInterface;
import core.support.annotation.Panel;
import core.support.objects.ServiceObject;
import io.restassured.response.Response;
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
	/**
	 * the scope of the stored token value depends on the where its called.
	 *  eg. at before class (the scope is class level )
	 *      at before method ( the scope is test level )
	 *      at before suite ( the scope is suite level )
	 * @param user
	 */
	public void login(CommonUser user) {
		Service.getToken
				.withUsername(user.username)
				.withPassword(user.password)
				.build();
	}
	
	public Response loginWithServiceObject(CommonUser user) {
		
		ServiceObject loginApi = Service.create()
				.withUriPath("/auth/local")
				.withContentType("application/json")
				.withMethod("POST")
				.withRequestBody("{" + 
						"\"identifier\": \"" + user.username +"\",\r\n" + 
						"\"password\": \"" + user.password + "\"" + 
						"}")
				.withOutputParams(
						"user.role.id:<$roles>;"
						+ "jwt:<$accessTokenAdmin>;"
						+ "user.id:<$userLoginId>");
				
		return RestApiInterface.RestfullApiInterface(loginApi);
	}
}