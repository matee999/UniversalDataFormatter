package formatter.data_manipulation.finder;

/**
 *  Holder class for combining search information for nested {@link formatter.models.Entity}s
 */
public class NestedEntityAttributeFinder {
	
	private String childKey;
	private String childAttributeKey;
	
	private Object childAttributeValue;

	/**
	 * Main constructor for initialization
	 * @param childKey			entity's child key
	 * @param childAttributeKey	entity's child attribute key
	 * @param childValue		entity's child attribute value for key
	 */
	public NestedEntityAttributeFinder(String childKey, String childAttributeKey, Object childValue) {
		this.childKey = childKey;
		this.childAttributeKey  = childAttributeKey;
		this.childAttributeValue = childValue;
	}

	/**
	 * <p>Returns child {@link formatter.models.Entity} key</p>
	 * @return childKey	child key
	 */
	public String getChildKey() {
		return childKey;
	}

	/**
	 * <p>Sets the child {@link formatter.models.Entity} key</p>
	 * @param childKey	child key
	 */
	public void setChildKey(String childKey) {
		this.childKey = childKey;
	}

	/**
	 * <p>Returns child {@link formatter.models.Entity} attribute key</p>
	 * @return childAttributeKey	child attribute key
	 */
	public String getChildAttributeKey() {
		return childAttributeKey;
	}

	/**
	 * <p>Sets the child {@link formatter.models.Entity} attribute key</p>
	 * @param childAttributeKey	child attribute key
	 */
	public void setChildAttributeKey(String childAttributeKey) {
		this.childAttributeKey = childAttributeKey;
	}

	/**
	 * <p>Returns child {@link formatter.models.Entity} attribute value for key</p>
	 * @return childAttributeValue	child attribute value for key
	 */
	public Object getChildAttributeValue() {
		return childAttributeValue;
	}

	/**
	 * <p>Sets the child {@link formatter.models.Entity} attribute value for key</p>
	 * @param childAttributeValue	child attribute value for key
	 */
	public void setChildAttributeValue(Object childAttributeValue) {
		this.childAttributeValue = childAttributeValue;
	}
}
