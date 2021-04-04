package formatter.data_manipulation.finder.matchers.attribute_matchers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import formatter.data_manipulation.finder.EntityMatcher;

/**
 * Matcher for {@link formatter.models.Entity} attribute values
 */
public class EntityAttributeValueMatcher implements EntityMatcher {

	private Collection<Object> values;

	private Object value;

	/**
	 * Main constructor for initialization
	 * @param values	entity attribute values
	 * @param value		value for matching
	 */
	public EntityAttributeValueMatcher(Collection<Object> values, Object value) {
		this.values = values;
		this.value = value;
	}

	@Override
	public boolean matches() {
		try {
			List<String> valueStrings = new ArrayList<String>();
			for (Object value : values)
				valueStrings.add(value != null ? value.toString() : "null");
			
			return valueStrings.contains(this.value.toString());
			
		} catch (Exception e) {
			return false;
		}
	}

}
