package module.iosApp.data;

import core.support.annotation.Data;

@Data
public class ListSectionsObject {
	

	public String whereYouLive = "";
	public String oceans = "";

	
	public ListSectionsObject withDefaultValues() {
		ListSectionsObject list = new ListSectionsObject();
		list.whereYouLive = "North America";
		list.oceans = "Pacific";
		return list;
	}
}