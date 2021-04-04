package formatter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import formatter.data_integrity.IdValidator;
import formatter.exceptions.IllegalIdentifierException;
import formatter.models.Entity;

/**
 * Persister for doing CRUD operation on {@link formatter.models.Entity}s
 */
class EntityPersister {

	/**
	 * Active {@link formatter.DataFormatter}
	 */
	private DataFormatter formatter;

	/**
	 * Configuration for {@link formatter.DataFormatter}
	 */
	private UDFConfigurator configurator;

	/**
	 * Main constructor for initialization
	 */
	public EntityPersister(DataFormatter formatter, UDFConfigurator configurator) {
		this.formatter = formatter;
		this.configurator = configurator;
	}

	/**
	 * <p>
	 * Deletes the given {@link formatter.models.Entity} from storage
	 * </p>
	 * 
	 * @param entityToDelete entity for deletion
	 * @param cascade        when cascade is active, all Entity's children will be
	 *                       deleted too
	 * @return success whether the operation succeeded
	 */
	public boolean deleteEntity(Entity entityToDelete, boolean cascade) {
		List<Entity> entities = getAllEntities();

		// Move children to the root level
		if (!cascade && entityToDelete.getChildren() != null) {
			List<Entity> children = new ArrayList<Entity>();

			for (Entity entity : entities) {
				if (entity.getId() != entityToDelete.getId() || entity.getChildren() == null)
					continue;

				for (Entity child : entity.getChildren().values())
					children.add(child);
			}
			entities.addAll(children);
		}

		// Remove parent references to this entity
		for (Entity entity : entities) {
			if (entity.getChildren() != null && entity.getChildren().containsValue(entityToDelete)) {
				entity.getChildren().values().remove(entityToDelete);

				if (entity.getChildren().values().isEmpty())
					entity.setChildren(null);

				break;
			}
		}

		entities.remove(entityToDelete);

		try {
			saveAll(entities);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * <p>
	 * Deletes the given {@link formatter.models.Entity} from storage
	 * </p>
	 * 
	 * @param entitiesToDelete entities for deletion
	 * @param cascade          when cascade is active, all Entity's children will be
	 *                         deleted too
	 * @return success whether the operation succeeded
	 */
	public boolean deleteEntities(List<Entity> entitiesToDelete, boolean cascade) {
		List<Entity> entities = getAllEntities();
		List<Entity> children = new ArrayList<Entity>();

		// Move children to the root level
		if (!cascade) {
			for (Entity entity : entities) {
				for (Entity forDelete : entitiesToDelete) {
					if (entity.getId() != forDelete.getId() || entity.getChildren() == null)
						continue;

					for (Entity child : entity.getChildren().values())
						children.add(child);
				}
			}
			entities.addAll(children);
		}

		// Removes child entities
		for (Entity entity : entities) {
			for (Entity forDelete : entitiesToDelete) {
				if (entity.getChildren() != null && entity.getChildren().containsValue(forDelete)) {
					entity.getChildren().values().remove(forDelete);

					if (entity.getChildren().values().isEmpty())
						entity.setChildren(null);
				}
			}
		}

		// Remove parent entities
		Iterator<Entity> iterator = entities.iterator();
		while (iterator.hasNext()) {
			Entity entity = iterator.next();

			if (entitiesToDelete.contains(entity))
				iterator.remove();
		}

		try {
			saveAll(entities);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * <p>
	 * Creates an {@link formatter.models.Entity} from parameters and auto-generates
	 * unique identifier
	 * </p>
	 * 
	 * @param name       entity name
	 * @param attributes entity attributes, can be null
	 * @param children   entity children, can be null
	 * @return success whether the operation succeeded
	 * @throws Exception error occurred during creation
	 */
	public Entity createEntity(String name, Map<String, Object> attributes, Map<String, Entity> children)
			throws Exception {
		int lastGeneratedId = generateId();
		Entity entity = new Entity(lastGeneratedId, name, attributes, children);

		if (children != null) {
			Collection<Entity> childrenEntities = children.values();
			for (Entity child : childrenEntities) {
				child.setId(lastGeneratedId + 1);
				lastGeneratedId++;
			}
		}

		save(entity);

		return entity;
	}

	/**
	 * <p>
	 * Creates an {@link formatter.models.Entity} from parameters and checks unique
	 * identifier's validity
	 * </p>
	 * 
	 * @param id         entity unique identifier
	 * @param name       entity name
	 * @param attributes entity attributes, can be null
	 * @param children   entity children, can be null
	 * @return success whether the operation succeeded
	 * @throws Exception error occurred during creation
	 */
	public Entity createEntity(int id, String name, Map<String, Object> attributes, Map<String, Entity> children)
			throws Exception {
		boolean idValid = verifyIdAvailable(id);

		if (!idValid)
			throw new IllegalIdentifierException();

		if (children != null) {
			Collection<Entity> childrenEntities = children.values();
			for (Entity child : childrenEntities) {
				if (child.getId() == id || !verifyIdAvailable(child.getId()))
					throw new IllegalIdentifierException();
			}
		}

		Entity entity = new Entity(id, name, attributes, children);
		save(entity);

		return entity;
	}

	/**
	 * <p>
	 * Updates an existing {@link formatter.models.Entity}
	 * </p>
	 * 
	 * @param entity         updated entity instance
	 * @param addingChildren new children have been added and id validity must be
	 *                       done
	 * @throws Exception error occurred during creation
	 */
	public void updateEntity(Entity entity, boolean addingChildren) throws Exception {
		List<Entity> entities = getAllEntities();
		int indexToUpdate = -1;
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i).getId() == entity.getId()) {
				if (addingChildren && entity.getChildren() != null) {
					if (formatter.isAutoIncrementEnabled()) {
						Collection<Entity> childrenEntities = entity.getChildren().values();
						int lastGeneratedId = generateId();
						for (Entity child : childrenEntities) {
							child.setId(lastGeneratedId);
							lastGeneratedId++;
						}
					} else {
						Collection<Entity> childrenEntities = entity.getChildren().values();
						List<Integer> newIdsForChildren = new ArrayList<Integer>();
						for (Entity child : childrenEntities) {
							if (newIdsForChildren.contains(child.getId()) || !verifyIdAvailable(child.getId()))
								throw new IllegalIdentifierException();
							else
								newIdsForChildren.add(child.getId());
						}
					}
				}
				indexToUpdate = i;
			}
		}
		// It's not a parent
		if (indexToUpdate == -1) {
			Entity parent = null;
			String childKey = null;
			for (Entity e : entities) {
				if (e.getChildren() != null) {
					for (Map.Entry<String, Entity> entry : e.getChildren().entrySet()) {
						if (entry.getValue().equals(entity)) {
							childKey = entry.getKey();
							parent = e;
						}
					}
				}
			}

			parent.getChildren().put(childKey, entity);
		} else {
			entities.set(indexToUpdate, entity);
		}
		saveAll(entities);
	}

	/**
	 * <p>
	 * Returns all {@link formatter.models.Entity}s from storage
	 * </p>
	 * 
	 * @return entities entities from storage
	 */
	public List<Entity> getAllEntities() {
		List<Entity> entities = new ArrayList<Entity>();
		String savePath = configurator.getStoragePath();
		String[] files = new File(savePath).list();
		int ignoreFileNumber = 0;
		for (String file : files) {
			if (file.contains(".DS_Store"))
				ignoreFileNumber++;
			if (file.contains("application.properties"))
				ignoreFileNumber++;
		}

		if (files == null)
			return entities;

		// Ignore properties file
		int fileCount = files.length - ignoreFileNumber;

		for (int i = 0; i < fileCount; i++) {
			try {
				List<Entity> data = formatter
						.read(new File(savePath, "entities" + (i + 1) + formatter.getDataFormatExtension()));
				entities.addAll(data);
			} catch (Exception e) {
				entities.addAll(new ArrayList<Entity>());
			}
		}

		return entities;
	}

	private boolean verifyIdAvailable(int id) {
		return IdValidator.verifyIdAvailable(id, getAllEntities());
	}

	private int generateId() {
		return IdValidator.generateId(getAllEntities());
	}

	private void save(Entity entity) throws Exception {
		List<Entity> entities = getAllEntities();

		entities.add(entity);
		saveAll(entities);
	}

	private void saveAll(List<Entity> entities) throws Exception {
		// Clear old files
		deleteAllEntityFiles();

		int fileNumber = 1;
		int entityLimitPerFile = configurator.getEntityLimitPerFile();
		for (int i = 0; i < entities.size(); i += entityLimitPerFile) {
			int end = i + entityLimitPerFile;
			formatter.save(entities.subList(i, end > entities.size() ? entities.size() : end), new File(
					configurator.getStoragePath(), "entities" + fileNumber + formatter.getDataFormatExtension()));

			fileNumber++;
		}
	}

	private void deleteAllEntityFiles() {
		File saveDirectory = new File(configurator.getStoragePath());
		for (File file : saveDirectory.listFiles()) {
			if (!file.getName().equals("application.properties"))
				file.delete();
		}
	}
}
