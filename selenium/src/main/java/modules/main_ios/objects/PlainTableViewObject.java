package modules.main_ios.objects;

import org.inferred.freebuilder.FreeBuilder;

import com.google.common.base.Optional;

@FreeBuilder
public abstract class PlainTableViewObject {

	public static final String NAME = "auto";
	public static final String USER_NAME = "auto user";
	public static final String EMAIL_ADDRESS = "test123@email.com";
	public static final String PASSWORD = "password123";
	
	public abstract Optional<String> name();
	public abstract Optional<String> username();
	public abstract Optional<String> emailAddress();
	public abstract Optional<String> password();

	

	public abstract Builder toBuilder();
	
	public static PlainTableViewObject form() {
		return new PlainTableViewObject.Builder().buildPartial();
	}

	public static class Builder extends PlainTableViewObject_Builder {

	}

	public PlainTableViewObject withDefaultValues() {
		return new PlainTableViewObject.Builder()
				.name(NAME).username(USER_NAME).emailAddress(EMAIL_ADDRESS).password(PASSWORD)		
				.buildPartial();
	}
}