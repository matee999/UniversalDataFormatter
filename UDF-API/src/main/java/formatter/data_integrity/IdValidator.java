package formatter.data_integrity;

import java.util.List;

import formatter.models.Entity;

/**
 * Validation of {@link formatter.models.Entity} unique identifiers
 */
public class IdValidator {

	/**
	 * <p>Returns new unique identifier based on existing {@link formatter.models.Entity}s</p>
	 * @param entities	existing entities with unique identifiers set
	 * @return id		unique identifier
	 */
	public static int generateId(List<Entity> entities) {
		int maxId = -1;
		
		for (Entity entity : entities) {
			if (entity.getId() > maxId)
				maxId = entity.getId();
			
			if (entity.getChildren() != null) {
				for (Entity child : entity.getChildren().values())
					if (child.getId() > maxId)
						maxId = child.getId();
			}
		}
		
		return maxId + 1;
	}
	
//	/**
//	 * <p>Returns new unique identifier based on existing {@link formatter.models.Entity}s</p>
//	 * @param entities		existing entities with unique identifiers set
//	 * @param includeIds	unique identifiers which will be viewed as they are taken
//	 * @return id			unique identifier
//	 */
//	public static int generateId(List<Entity> entities, List<Integer> includeIds) {
//		int maxId = -1;
//		
//		for (Integer id : includeIds)
//			if (id > maxId)
//				maxId = id;
//		
//		for (Entity entity : entities) {
//			if (entity.getId() > maxId)
//				maxId = entity.getId();
//			
//			if (entity.getChildren() != null) {
//				for (Entity child : entity.getChildren().values())
//					if (child.getId() > maxId)
//						maxId = child.getId();
//			}
//		}
//		
//		return maxId + 1;
//	}
	
	/**
	 * <p>Verifies that given unique identifier is really unique
	 * based on existing {@link formatter.models.Entity}s</p>
	 * @param id		unique identifier to verify
	 * @param entities	existing entities with unique identifiers set
	 * @return valid	unique identifier validity
	 */
	public static boolean verifyIdAvailable(int id, List<Entity> entities) {
		boolean available = true;
		
		for (Entity entity : entities) {
			if (entity.getId() == id)
				available = false;
			
			if (entity.getChildren() != null) {
				for (Entity child : entity.getChildren().values())
					if (child.getId() == id)
						available = false;
			}
		}
		
		return available;
	}
}
