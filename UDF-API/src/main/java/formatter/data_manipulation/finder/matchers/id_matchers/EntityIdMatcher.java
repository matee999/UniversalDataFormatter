package formatter.data_manipulation.finder.matchers.id_matchers;

import formatter.data_manipulation.finder.EntityMatcher;

/**
 * Matcher for {@link formatter.models.Entity} unique identifiers
 */
public class EntityIdMatcher implements EntityMatcher {
	
	private int id; 
	
	private Object other;

	/**
	 * Main constructor for initialization
	 * @param id		entity unique identifier
	 * @param other		value for matching
	 */
	public EntityIdMatcher(int id, Object other) {
		this.id = id;
		this.other = other;
	}
	
	@Override
	public boolean matches() {
		try {
			int otherId = Integer.parseInt((String) other);
			return id == otherId;
		} catch (Exception e) {
			return false;
		}
	}
}
