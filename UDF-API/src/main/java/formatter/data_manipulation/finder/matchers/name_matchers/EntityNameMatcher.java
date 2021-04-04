package formatter.data_manipulation.finder.matchers.name_matchers;

import formatter.data_manipulation.finder.EntityMatcher;

/**
 * Matcher for {@link formatter.models.Entity} names
 */
public abstract class EntityNameMatcher implements EntityMatcher {
	
	protected String name;
	
	protected Object other;
	
	/**
	 * Main constructor for initialization
	 * @param name		entity name
	 * @param other		value for matching
	 */
	public EntityNameMatcher(String name, Object other) {
		this.name = name;
		this.other = other;
	}
}
