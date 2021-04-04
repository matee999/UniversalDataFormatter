package formatter.models;

import java.util.Map;
import java.util.Objects;

/**
* Entity model for storing object attributes
* and its children entities
*/
public class Entity {
	
	public static final int UNINITIALIZED_ID = -1;

	private int id = UNINITIALIZED_ID;

	private String name;

	private Map<String, Object> attributes;
	private Map<String, Entity> children;

	/**
	 * Empty constructor which is called for generating entities
	 */
	public Entity() {}

	/**
	 * Main constructor for creating and saving entities
	 * @param id			unique identifier
	 * @param name			name
	 * @param attributes	attributes
	 * @param children		children
	 */
	public Entity(int id, String name, Map<String, Object> attributes, Map<String, Entity> children) {
		this.id = id;
		this.name = name;
		this.attributes = attributes;
		this.children = children;
	}
	
	/**
	 * Constructor without an unique identifier
	 * @param name			name
	 * @param attributes	attributes
	 * @param children		children
	 */
	public Entity(String name, Map<String, Object> attributes, Map<String, Entity> children) {
		this.name = name;
		this.attributes = attributes;
		this.children = children;
	}

	/**
	 * <p>Returns the Entity's unique identifier</p>
	 * @return unique identifier
	 */
	public int getId() {
		return id;
	}

	/**
	 * <p>Sets the unique identifier for given Entity</p>
	 * @param id unique identifier, not null
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * <p>Returns the Entity's name</p>
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * <p>Sets the name for given Entity</p>
	 * @param name name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * <p>Returns the Entity's attributes</p>
	 * @return attributes, can be null
	 */
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	/**
	 * <p>Sets the attributes for given Entity</p>
	 * @param attributes attributes, can be null
	 */
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	/**
	 * <p>Returns the Entity's children</p>
	 * <b>IMPORTANT:</b> Only first level child nesting is possible 
	 * @return children, can be null
	 */
	public Map<String, Entity> getChildren() {
		return children;
	}

	/**
	 * <p>Sets the children for given Entity</p>
	 * <b>IMPORTANT:</b> Only first level child nesting is possible 
	 * @param children children, can be null
	 */
	public void setChildren(Map<String, Entity> children) {
		this.children = children;
	}

	@Override
	/**
	 * <p>String representation of Entity object</p>
	 * @return string
	 */
	public String toString() {
		return "Entity [id=" + id + ", name=" + name + ", attributes=" + attributes + ", children=" + children + "]";
	}

	@Override
	/**
	 * <p>Compares two Entities using their unique identifiers (Id)</p>
	 * @return equality
	 */
	public boolean equals(Object o) {

		if (o == this)
			return true;
		if (!(o instanceof Entity)) {
			return false;
		}
		Entity entity = (Entity) o;

		return id == entity.id;
	}

	@Override
	/**
	 * <p>Creates Entity's hash using its unique identifier</p>
	 * @return hashCode
	 */
	public int hashCode() {
		return Objects.hash(id);
	}
}
