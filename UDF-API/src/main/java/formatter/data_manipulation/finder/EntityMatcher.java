package formatter.data_manipulation.finder;

/**
 * Matcher for {@link formatter.models.Entity} searching
 */
public interface EntityMatcher {
	
	/**
	 * <p>Returns whether {@link formatter.models.Entity} values match</p>
	 * @return matches	entity data matches
	 */
	public boolean matches();
}
