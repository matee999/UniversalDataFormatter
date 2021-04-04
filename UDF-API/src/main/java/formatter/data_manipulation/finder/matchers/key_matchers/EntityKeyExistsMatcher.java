package formatter.data_manipulation.finder.matchers.key_matchers;

import java.util.Set;

import formatter.data_manipulation.finder.EntityMatcher;

/**
 * Matcher for {@link formatter.models.Entity} attribute and child keys
 */
public class EntityKeyExistsMatcher implements EntityMatcher {

	protected Set<String> keys;
	
	protected Object other;

	/**
	 * Main constructor for initialization
	 * @param keys			entity attribute or child keys
	 * @param searchKey		value for matching
	 */
	public EntityKeyExistsMatcher(Set<String> keys, Object searchKey) {
		this.keys = keys;
		this.other = searchKey;
	}

	@Override
	public boolean matches() {
		try {
			String searchKey = (String) other;
			boolean keyExists = false;

			for (String key : keys) {
				if (key.equals(searchKey))
					keyExists = true;
			}
			
			return keyExists;
		} catch (Exception e) {
			return false;
		}
	}
}
