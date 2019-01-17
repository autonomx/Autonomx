package common.objects;

import org.inferred.freebuilder.FreeBuilder;

import com.google.common.base.Optional;

import core.helpers.Helper;

@FreeBuilder
public abstract class UserObject {

	public static final String ADMIN_USER = "autoAdmin1";
	public static final String ADMIN_PASSWORD = "autoPass1";

	public static final String USER_PREFIX = "zzz_";

	/**
	 * object
	 */
	public abstract Optional<String> name();

	public abstract Optional<String> username();

	public abstract Optional<String> password();

	public abstract Optional<String> email();
	
	public abstract Optional<String> language();
	
	public abstract Optional<Boolean> acceptAds();
	
	public abstract Optional<Boolean> registerVeirfy();


	public abstract Optional<Boolean> confirmed();

	public abstract Optional<Boolean> blocked();

	public abstract Optional<String> loginId();

	public abstract Builder toBuilder();

	public static class Builder extends UserObject_Builder {
	}

	public static UserObject user() {
		return new UserObject.Builder().buildPartial();
	}

	public UserObject setUserName(String username) {
		return this.toBuilder().username(username).buildPartial();
	}

	public UserObject setPassword(String password) {
		return this.toBuilder().password(password).buildPartial();
	}

	/**
	 * Predefined objects
	 * 
	 * @return
	 */
	public UserObject withAdminLogin() {
		return new UserObject.Builder().username(ADMIN_USER).password(ADMIN_PASSWORD).buildPartial();
	}
	
	public UserObject withDefaultUser() {
		String rand = Helper.generateRandomString(5);
		return new UserObject.Builder()
				.name(USER_PREFIX + "auto" + rand)
				.username(USER_PREFIX + "autoUser" + rand)
				.email("autoUser" + rand + "@email.com")
				.password(USER_PREFIX + "autoPass" + rand)
				.language("Java")
				.acceptAds(true)
				.confirmed(true)
				.blocked(false)
				.buildPartial();
	}
	
	public UserObject withEdittUser() {
		String rand = Helper.generateRandomString(5);
		return new UserObject.Builder()
				.username(USER_PREFIX + "editUser" + rand)
				.email("")
				.password(USER_PREFIX + "editPass" + rand)
				.confirmed(true)
				.blocked(false)
				.buildPartial();
	}
}