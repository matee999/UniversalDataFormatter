package formatter;

import java.io.File;
import java.util.List;
import java.util.Map;

import formatter.data_manipulation.finder.FinderProperties;
import formatter.models.Entity;

/**
* Abstract data formatter which handles all operations on {@link formatter.models.Entity}s
*/
public abstract class DataFormatter {
	
	/**
	 * Configuration for {@link formatter.DataFormatter}
	 */
	private UDFConfigurator configurator;
	
	/**
	 * Finder for {@link formatter.models.Entity} searching
	 */
	private EntityFinder finder;
	
	/**
	 * Persister for doing CRUD operation on {@link formatter.models.Entity}s
	 */
	private EntityPersister persister;

	/**
	 * Main constructor for initialization
	 */
	public DataFormatter() {
		this.configurator = new UDFConfigurator(this);
		this.finder = new EntityFinder(this);
		this.persister = new EntityPersister(this, configurator);
	}

	/**
	 * <p>Deletes the given {@link formatter.models.Entity} from storage</p>
	 * @param entityToDelete 	entity for deletion
	 * @param cascade	when cascade is active, all Entity's children will be deleted too
	 * @return success	whether the operation succeeded
	 */
	public boolean deleteEntity(Entity entityToDelete, boolean cascade) {
		return persister.deleteEntity(entityToDelete, cascade);
	}
	
	/**
	 * <p>Deletes the given {@link formatter.models.Entity} from storage</p>
	 * @param entitiesToDelete 	entities for deletion
	 * @param cascade	when cascade is active, all Entity's children will be deleted too
	 * @return success	whether the operation succeeded
	 */
	public boolean deleteEntities(List<Entity> entitiesToDelete, boolean cascade) {
		return persister.deleteEntities(entitiesToDelete, cascade);
	}

	/**
	 * <p>Creates an {@link formatter.models.Entity} from parameters and auto-generates unique identifier</p>
	 * @param name			entity name
	 * @param attributes	entity attributes, can be null
	 * @param children		entity children, can be null
	 * @return success		whether the operation succeeded
	 * @throws Exception	error occurred during creation
	 */
	public Entity createEntity(String name, Map<String, Object> attributes, Map<String, Entity> children)
			throws Exception {
		return persister.createEntity(name, attributes, children);
	}
	
	/**
	 * <p>Creates an {@link formatter.models.Entity} from parameters and auto-generates unique identifier</p>
	 * @param name			entity name
	 * @param attributes	entity attributes, can be null
	 * @return success		whether the operation succeeded
	 * @throws Exception	error occurred during creation
	 */
	public Entity createEntity(String name, Map<String, Object> attributes)
			throws Exception {
		return persister.createEntity(name, attributes, null);
	}

	/**
	 * <p>Creates an {@link formatter.models.Entity} from parameters and checks unique identifier's validity</p>
	 * @param id			entity unique identifier
	 * @param name			entity name
	 * @param attributes	entity attributes, can be null
	 * @param children		entity children, can be null
	 * @return success		whether the operation succeeded
	 * @throws Exception	error occurred during creation
	 */
	public Entity createEntity(String id, String name, Map<String, Object> attributes, Map<String, Entity> children)
			throws Exception {
		int parsedId = Integer.parseInt(id);
		return persister.createEntity(parsedId, name, attributes, children);
	}
	
	/**
	 * <p>Creates an {@link formatter.models.Entity} from parameters and checks unique identifier's validity</p>
	 * @param id			entity unique identifier
	 * @param name			entity name
	 * @param attributes	entity attributes, can be null
	 * @return success		whether the operation succeeded
	 * @throws Exception	error occurred during creation
	 */
	public Entity createEntity(String id, String name, Map<String, Object> attributes)
			throws Exception {
		int parsedId = Integer.parseInt(id);
		return persister.createEntity(parsedId, name, attributes, null);
	}
	
	/**
	 * <p>Updates an existing {@link formatter.models.Entity}</p>
	 * @param entity	updated entity instance
	 * @throws Exception	error occurred during creation
	 */
	public void updateEntity(Entity entity, boolean addingChildren) throws Exception {
		persister.updateEntity(entity, addingChildren);
	}

	/**
	 * <p>Returns all {@link formatter.models.Entity}s from storage</p>
	 * @return entities	entities from storage
	 */
	public List<Entity> getAllEntities() {
		return persister.getAllEntities();
	}

	/**
	 * <p>Returns {@link formatter.models.Entity}s from storage that fulfill search requirements.
	 * Use {@link formatter.data_manipulation.finder.FinderProperties} for search keys</p>
	 * @param searchData	search information for making the query
	 * @return entities		filtered entities from storage
	 */
	public List<Entity> searchForEntities(Map<FinderProperties, Object> searchData) {
		return finder.getEntities(searchData);
	}

	/**
	 * <p>Returns all {@link formatter.models.Entity}s from storage</p>
	 * @return entities		filtered entities from storage
	 */
	public int getEntityLimitPerFile() {
		return configurator.getEntityLimitPerFile();
	}

	/**
	 * <p>Sets the maximum number of {@link formatter.models.Entity}s per file</p>
	 * @param entityLimitPerFile	maximum number of entities per file
	 */
	public void setEntityLimitPerFile(int entityLimitPerFile) {
		configurator.setEntityLimitPerFile(entityLimitPerFile);
	}
	
	/**
	 * <p>Returns storage location of stored {@link formatter.models.Entity}s</p>
	 * @return storagePath	storage location
	 */
	public String getStoragePath() {
		return configurator.getStoragePath();
	}
	
	/**
	 * <p>Sets storage location of stored {@link formatter.models.Entity}s</p>
	 * @param storagePath	storage location
	 * @throws Exception	illegal storage location
	 */
	public void setStoragePath(String storagePath) throws Exception {
		configurator.setStoragePath(storagePath);
	}

	/**
	 * <p>Returns currently active data format information</p>
	 * @return info	information text about active data format
	 */
	public String getInfoText() {
		return "Currently wokring with " + getDataFormatName() + " files using " + getDataFormatExtension()
				+ " extension.";
	}
	
	/**
	 * <p>Returns whether unique identifiers get automatically generated or must be
	 * provided along with {@link formatter.models.Entity} details</p>
	 * @return autoIncrementEnabled	is auto-increment enabled
	 */
	public boolean isAutoIncrementEnabled() {
		return configurator.isAutoIncrementIds();
	}
	
	/**
	 * <p>Sets storage location of stored {@link formatter.models.Entity}s</p>
	 * @param enabled	enable auto increment system
	 */
	public void setAutoIncrementEnabled(boolean enabled) {
		configurator.setAutoIncrementIds(enabled);
	}

	/**
	 * <p>Saves provided {@link formatter.models.Entity}s to file</p>
	 * @param entities	 entities to save
	 * @param file		 file in which the entities will be stored
	 * @throws Exception error occurred during file writing
	 */
	abstract void save(List<Entity> entities, File file) throws Exception;

	/**
	 * <p>Returns stored {@link formatter.models.Entity}s from file</p>
	 * @param file		 file from which to read entities
	 * @return entities	 stored entities
	 * @throws Exception error occurred during file reading
	 */
	protected abstract List<Entity> read(File file) throws Exception;

	/**
	 * <p>Returns data format's file extension</p>
	 * @return extension	data format extension
	 */
	protected abstract String getDataFormatExtension();

	/**
	 * <p>Returns data format's name</p>
	 * @return name	data format name
	 */
	protected abstract String getDataFormatName();
}
