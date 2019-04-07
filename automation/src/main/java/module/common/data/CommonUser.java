package module.common.data;


import core.helpers.Helper;
import core.support.annotation.Data;

@Data
public class CommonUser {

	private static final String ADMIN_USER = "autoAdmin1";
	private static final String ADMIN_PASSWORD = "autoPass1";
	private static final String USER_PREFIX = "zzz_";

	/**
	 * variables
	 */
	public String name = "";
	public String username = "";
	public String password = "";
	public String email = "";
	public String language = "";
	public Boolean acceptAds;
	public Boolean registerVerify;
	public Boolean confirmed;
	public Boolean blocked;
	public String loginId = "";
	
	
	/**
	 * Predefined objects
	 * 
	 * @return
	 */
	public CommonUser withAdminLogin() {
		CommonUser user = new CommonUser();
		user.username = ADMIN_USER;
		user.password = ADMIN_PASSWORD;
		return user;
	}
	
	public CommonUser withDefaultUser() {
		String rand = Helper.generateRandomString(5);
		CommonUser user = new CommonUser();
		user.name = USER_PREFIX + "auto" + rand;
		user.username = USER_PREFIX + "autoUser" + rand;
		user.email = "autoUser" + rand + "@email.com";
		user.password = USER_PREFIX + "autoPass" + rand;
		user.language = "Java";
		user.acceptAds = true;
		user.confirmed = true;
		user.blocked = false;
		
		return user;
	}
	
	public CommonUser withEdittUser() {
		String rand = Helper.generateRandomString(5);
		CommonUser user = new CommonUser();
		user.username = USER_PREFIX + "editUser" + rand;
		user.password = USER_PREFIX + "editPass" + rand;
		user.confirmed = true;
		user.blocked = false;
		
		return user;
	}
}