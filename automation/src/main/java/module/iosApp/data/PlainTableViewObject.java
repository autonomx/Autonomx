package module.iosApp.data;

import core.support.annotation.Data;

@Data
public class PlainTableViewObject {

	private static final String NAME = "auto";
	private static final String USER_NAME = "auto user";
	private static final String EMAIL_ADDRESS = "test123@email.com";
	private static final String PASSWORD = "password123";
	
	public String name = "";
	public String username = "";
	public String emailAddress = "";
	public String password = "";


	public PlainTableViewObject withDefaultValues() {
		PlainTableViewObject view = new PlainTableViewObject();
		view.name = NAME;
		view.username = USER_NAME;
		view.emailAddress = EMAIL_ADDRESS;
		view.password = PASSWORD;

		return view;
	}
}