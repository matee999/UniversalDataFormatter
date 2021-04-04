package formatter.data_manipulation.finder.matchers.complex_matchers;

import formatter.data_manipulation.finder.EntityMatcher;
import formatter.data_manipulation.finder.NestedEntityAttributeFinder;
import formatter.models.Entity;

/**
 * Matcher for {@link formatter.models.Entity} child attribute value for given child and attribute keys
 */
public class EntityChildValueForKeyMatcher implements EntityMatcher {

	private Entity parent;

	private Object value;

	/**
	 * Main constructor for initialization
	 * @param parent	entity containing data
	 * @param value		value for matching
	 */
	public EntityChildValueForKeyMatcher(Entity parent, Object value) {
		this.parent = parent;
		this.value = value;
	}

	@Override
	public boolean matches() {
		try {
			if (parent.getChildren() == null)
				return false;

			NestedEntityAttributeFinder data = (NestedEntityAttributeFinder) value;
			Entity child = parent.getChildren().get(data.getChildKey());

			if (child == null || child.getAttributes() == null)
				return false;

			if (!child.getAttributes().containsKey(data.getChildAttributeKey()))
				return false;

			return child.getAttributes().get(data.getChildAttributeKey()).toString()
					.equals(data.getChildAttributeValue().toString());
		} catch (Exception e) {
			return false;
		}
	}

}
