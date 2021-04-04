package formatter.data_manipulation.finder.matchers.name_matchers;

/**
 * Matcher for {@link formatter.models.Entity} name prefixes
 */
public class EntityNameStartsWithMatcher extends EntityNameMatcher {

	/**
	 * Main constructor for initialization
	 * @param name		entity name
	 * @param other		value for matching
	 */
	public EntityNameStartsWithMatcher(String name, Object other) {
		super(name, other);
	}

	@Override
	public boolean matches() {
		try {
			String otherName = (String) other;
			return name.startsWith(otherName);
		} catch (Exception e) {
			return false;
		}
	}
}
