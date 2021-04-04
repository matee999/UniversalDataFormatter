package formatter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import formatter.data_manipulation.finder.FinderProperties;
import formatter.data_manipulation.finder.matchers.attribute_matchers.EntityAttributeValueMatcher;
import formatter.data_manipulation.finder.matchers.complex_matchers.EntityChildValueForKeyMatcher;
import formatter.data_manipulation.finder.matchers.id_matchers.EntityIdMatcher;
import formatter.data_manipulation.finder.matchers.key_matchers.EntityKeyExistsMatcher;
import formatter.data_manipulation.finder.matchers.name_matchers.EntityNameEndsWithMatcher;
import formatter.data_manipulation.finder.matchers.name_matchers.EntityNameEqualsMatcher;
import formatter.data_manipulation.finder.matchers.name_matchers.EntityNameStartsWithMatcher;
import formatter.models.Entity;

/**
 * Finder for {@link formatter.models.Entity} searching
 */
class EntityFinder {

	/**
	 * Active {@link formatter.DataFormatter}
	 */
	private DataFormatter formatter;

	/**
	 * Main constructor for initialization
	 */
	public EntityFinder(DataFormatter formatter) {
		this.formatter = formatter;
	}

	/**
	 * <p>Returns {@link formatter.models.Entity}s from storage that fulfill search requirements.
	 * Use {@link formatter.data_manipulation.finder.FinderProperties} for search keys</p>
	 * @param searchData	search information for making the query
	 * @return entities		filtered entities from storage
	 */
	public List<Entity> getEntities(Map<FinderProperties, Object> searchData) {
		List<Entity> entities = formatter.getAllEntities();
		List<Entity> children = new ArrayList<Entity>();

		for (Entity entity : entities) {
			if (entity.getChildren() == null)
				continue;

			children.addAll(entity.getChildren().values());
		}
		entities.addAll(children);

		Iterator<Entity> iterator = entities.iterator();

		while (iterator.hasNext()) {
			Entity entity = iterator.next();

			if (searchData.containsKey(FinderProperties.ID_EQUALS)) {
				if (!new EntityIdMatcher(entity.getId(), searchData.get(FinderProperties.ID_EQUALS)).matches()) {
					iterator.remove();
					continue;
				}
			}

			if (searchData.containsKey(FinderProperties.NAME_EQUALS)) {
				if (!new EntityNameEqualsMatcher(entity.getName(), searchData.get(FinderProperties.NAME_EQUALS))
						.matches()) {
					iterator.remove();
					continue;
				}
			}

			if (searchData.containsKey(FinderProperties.NAME_STARTS_WITH)) {
				if (!new EntityNameStartsWithMatcher(entity.getName(),
						searchData.get(FinderProperties.NAME_STARTS_WITH)).matches()) {
					iterator.remove();
					continue;
				}
			}

			if (searchData.containsKey(FinderProperties.NAME_ENDS_WITH)) {
				if (!new EntityNameEndsWithMatcher(entity.getName(), searchData.get(FinderProperties.NAME_ENDS_WITH))
						.matches()) {
					iterator.remove();
					continue;
				}
			}

			if (searchData.containsKey(FinderProperties.CONTAINS_ATTRIBUTE_KEY)) {
				if (entity.getAttributes() == null || !new EntityKeyExistsMatcher(entity.getAttributes().keySet(),
						searchData.get(FinderProperties.CONTAINS_ATTRIBUTE_KEY)).matches()) {
					iterator.remove();
					continue;
				}
			}
			
			if (searchData.containsKey(FinderProperties.CONTAINS_ATTRIBUTE_VALUE)) {
				if (entity.getAttributes() == null || !new EntityAttributeValueMatcher(entity.getAttributes().values(),
						searchData.get(FinderProperties.CONTAINS_ATTRIBUTE_VALUE)).matches()) {
					iterator.remove();
					continue;
				}
			}

			if (searchData.containsKey(FinderProperties.CONTAINS_CHILD_KEY)) {
				if (entity.getChildren() == null || !new EntityKeyExistsMatcher(entity.getChildren().keySet(),
						searchData.get(FinderProperties.CONTAINS_CHILD_KEY)).matches()) {
					iterator.remove();
					continue;
				}
			}

			if (searchData.containsKey(FinderProperties.CONTAINS_CHILD_KEY_WITH_ATTRIBUTE_VALUE)) {
				if (!new EntityChildValueForKeyMatcher(entity,
						searchData.get(FinderProperties.CONTAINS_CHILD_KEY_WITH_ATTRIBUTE_VALUE)).matches()) {
					iterator.remove();
					continue;
				}
			}
		}

		return entities;
	}
}
