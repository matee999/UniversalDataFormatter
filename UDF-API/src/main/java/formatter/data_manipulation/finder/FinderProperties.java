package formatter.data_manipulation.finder;

/**
 *  Finder properties which are used to find
 *  {@link formatter.models.Entity}s by their data properties
 */
public enum FinderProperties {
    /**
     * {@link formatter.models.Entity}s unique identifier
     */
	ID_EQUALS,
    /**
     * {@link formatter.models.Entity}s name
     */
	NAME_EQUALS, 
    /**
     * {@link formatter.models.Entity}s prefix
     */
	NAME_STARTS_WITH, 
    /**
     * {@link formatter.models.Entity}s suffix
     */
	NAME_ENDS_WITH, 
    /**
     * {@link formatter.models.Entity}s attribute key
     */
	CONTAINS_ATTRIBUTE_KEY,
    /**
     * {@link formatter.models.Entity}s attribute value
     */
	CONTAINS_ATTRIBUTE_VALUE,
    /**
     * {@link formatter.models.Entity}s child key
     */
	CONTAINS_CHILD_KEY, 
    /**
     * {@link formatter.models.Entity}s attribute value for child with key
     */
	CONTAINS_CHILD_KEY_WITH_ATTRIBUTE_VALUE
}
