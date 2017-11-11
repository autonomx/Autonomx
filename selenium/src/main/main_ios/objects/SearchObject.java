package main.main_ios.objects;

public class SearchObject {

	public static final String DEFAULT_SEARCH_TERM = "automation";
	
	/**
	 * object
	 */
	public String searchTerm;
	/**
	 * Predefined objects
	 * @return
	 */
	public SearchObject withDefaultSearchTerm() {
		return withSearchTerm(DEFAULT_SEARCH_TERM);
	}

	// define variable getters
	
	public SearchObject withSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
		return this;
	}
}