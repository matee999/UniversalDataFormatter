package app.data_holder;

public class ChildEntityDataHolder {

	private String childKey;
	private String id;
	private String name;
	
	public ChildEntityDataHolder(String childKey, String id, String name) {
		this.childKey = childKey;
		this.id = id;
		this.name = name;
	}
	
	public String getChildKey() {
		return childKey;
	}

	public void setChildKey(String childKey) {
		this.childKey = childKey;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
