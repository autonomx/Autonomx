package module.iosApp.objects;

import org.inferred.freebuilder.FreeBuilder;

import com.google.common.base.Optional;

@FreeBuilder
public abstract class ListSectionsObject {
	

	public abstract Optional<String> whereYouLive();
	public abstract Optional<String> oceans();

	

	public abstract Builder toBuilder();

	public static ListSectionsObject form() {
		return new ListSectionsObject.Builder().buildPartial();
	}
	
	public static class Builder extends ListSectionsObject_Builder {

	}

	public ListSectionsObject withDefaultValues() {
		return new ListSectionsObject.Builder()
				.whereYouLive("North America").oceans("Pacific")			
				.buildPartial();
	}
}