package app.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import app.data_holder.ChildEntityDataHolder;
import app.views.MainStageListener;
import formatter.DataFormatter;
import formatter.data_manipulation.finder.FinderProperties;
import formatter.data_manipulation.finder.NestedEntityAttributeFinder;
import formatter.manager.UDFManager;
import formatter.models.Entity;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class MainStageController {

	private DataFormatter formatter;
	private MainStageListener view;

	public MainStageController(MainStageListener view) {
		this.view = view;
		this.formatter = UDFManager.getFormatter();

		refresh();
	}

	public void onCreateEntityClicked(String id, String name, Map<String, Object> attributes) throws Exception {
		formatter.createEntity(id, name, attributes);

		refresh();
	}

	public void onCreateEntityClicked(String name, Map<String, Object> attributes) throws Exception {
		formatter.createEntity(name, attributes);

		refresh();
	}

	public void onCreateEntityClicked(String id, String name, Map<String, Object> attributes,
			List<ChildEntityDataHolder> childEntities) throws Exception {
		Map<String, Entity> children = new HashMap<String, Entity>();
		for (ChildEntityDataHolder child : childEntities)
			children.put(child.getChildKey(), new Entity(Integer.parseInt(child.getId()), child.getName(), null, null));

		formatter.createEntity(id, name, attributes, children);

		refresh();
	}

	public void onCreateEntityClicked(String name, Map<String, Object> attributes,
			List<ChildEntityDataHolder> childEntities) throws Exception {
		Map<String, Entity> children = new HashMap<String, Entity>();
		for (ChildEntityDataHolder child : childEntities)
			children.put(child.getChildKey(), new Entity(child.getName(), null, null));

		formatter.createEntity(name, attributes, children);

		refresh();
	}

	public void onCreateChildEntityClicked(Entity parent, String keyForChild, String id, String name,
			Map<String, Object> attributes) throws Exception {
		Entity child = new Entity(name, attributes, null);

		if (parent.getChildren() == null)
			parent.setChildren(new HashMap<String, Entity>());

		if (parent.getChildren().containsKey(keyForChild))
			throw new Exception();

		parent.getChildren().put(keyForChild, child);
		formatter.updateEntity(parent, true);

		refresh();
	}

	public void onCreateChildEntityClicked(Entity parent, String keyForChild, String name,
			Map<String, Object> attributes) throws Exception {
		Entity child = new Entity(name, attributes, null);

		if (parent.getChildren() == null)
			parent.setChildren(new HashMap<String, Entity>());

		if (parent.getChildren().containsKey(keyForChild))
			throw new Exception();

		parent.getChildren().put(keyForChild, child);
		formatter.updateEntity(parent, true);

		refresh();
	}

	public void onUpdateEntityClicked(Entity toUpdate, String name, Map<String, Object> attributes,
			List<Integer> childEntityIdsToDelete) throws Exception {
		if (!childEntityIdsToDelete.isEmpty()) {
			List<String> keysToDelete = new ArrayList<String>();
			for (String childKey : toUpdate.getChildren().keySet()) {
				if (childEntityIdsToDelete.contains(toUpdate.getChildren().get(childKey).getId()))
					keysToDelete.add(childKey);
			}
			
			for (String keyToDelete : keysToDelete)
				toUpdate.getChildren().remove(keyToDelete);
			
			if (toUpdate.getChildren().isEmpty())
				toUpdate.setChildren(null);
		}
		toUpdate.setName(name);
		toUpdate.setAttributes(attributes);

		formatter.updateEntity(toUpdate, false);

		onSearchQueryChanged();
	}

	public void onDeleteEntityClicked(Entity entity, boolean cascade) {
		formatter.deleteEntity(entity, cascade);

		onSearchQueryChanged();
	}

	public void onDeleteEntitiesClicked(List<Entity> entities, boolean cascade) {
		formatter.deleteEntities(entities, cascade);

		onSearchQueryChanged();
	}

	public void onTableSelectionChanged() {
		if (view.getSelectedEntities().size() > 1)
			enableButtonsForMultipleSelection();
		else if (!view.getSelectedEntities().isEmpty()) {
			Entity selected = view.getSelectedEntities().get(0);
			List<Entity> entities = formatter.getAllEntities();
			boolean isAChild = false;
			for (Entity entity : entities)
				if (entity.getChildren() != null && entity.getChildren().values().contains(selected))
					isAChild = true;

			enableButtonsForSingleSelection(!isAChild);
		} else
			enableButtonsForNoSelection();
	}

	public void onSearchQueryChanged() {
		Map<FinderProperties, Object> searchMap = new HashMap<FinderProperties, Object>();

		String idSearchText = view.getIdSearchText();
		if (!idSearchText.isBlank())
			searchMap.put(FinderProperties.ID_EQUALS, idSearchText);

		String nameEqualsSearchText = view.getNameEqualsSearchText();
		if (!nameEqualsSearchText.isBlank())
			searchMap.put(FinderProperties.NAME_EQUALS, nameEqualsSearchText);

		String nameStartsWithSearchText = view.getNameStartsWithSearchText();
		if (!nameStartsWithSearchText.isBlank())
			searchMap.put(FinderProperties.NAME_STARTS_WITH, nameStartsWithSearchText);

		String nameEndsWithSearchText = view.getNameEndsWithSearchText();
		if (!nameEndsWithSearchText.isBlank())
			searchMap.put(FinderProperties.NAME_ENDS_WITH, nameEndsWithSearchText);

		String containsAttributeKeySearchText = view.getContainsAttributeKeySearchText();
		if (!containsAttributeKeySearchText.isBlank())
			searchMap.put(FinderProperties.CONTAINS_ATTRIBUTE_KEY, containsAttributeKeySearchText);

		String containsAttributeValueSearchText = view.getContainsAttributeValueSearchText();
		if (!containsAttributeValueSearchText.isBlank())
			searchMap.put(FinderProperties.CONTAINS_ATTRIBUTE_VALUE, containsAttributeValueSearchText);

		String containsChildKeyWithAttributeValuePartOneSearchText = view
				.getContainsChildKeyWithAttributeValuePartOneSearchText();
		String containsChildKeyWithAttributeValuePartTwoSearchText = view
				.getContainsChildKeyWithAttributeValuePartTwoSearchText();
		String containsChildKeyWithAttributeValuePartThreeSearchText = view
				.getContainsChildKeyWithAttributeValuePartThreeSearchText();

		if (!containsChildKeyWithAttributeValuePartOneSearchText.isBlank()
				&& !containsChildKeyWithAttributeValuePartTwoSearchText.isBlank()
				&& !containsChildKeyWithAttributeValuePartThreeSearchText.isBlank()) {
			searchMap.put(FinderProperties.CONTAINS_CHILD_KEY_WITH_ATTRIBUTE_VALUE,
					new NestedEntityAttributeFinder(containsChildKeyWithAttributeValuePartOneSearchText,
							containsChildKeyWithAttributeValuePartTwoSearchText,
							containsChildKeyWithAttributeValuePartThreeSearchText));
		} else if (!containsChildKeyWithAttributeValuePartOneSearchText.isBlank()) {
			searchMap.put(FinderProperties.CONTAINS_CHILD_KEY, containsChildKeyWithAttributeValuePartOneSearchText);
		}

		// If query is empty, return all entities
		if (!searchMap.isEmpty())
			refresh(formatter.searchForEntities(searchMap));
		else
			refresh();
	}

	public List<TableColumn<Entity, ?>> populateCommonColumns() {
		List<TableColumn<Entity, ?>> columns = new ArrayList<TableColumn<Entity, ?>>();
		TableColumn<Entity, Integer> idCol = new TableColumn<Entity, Integer>("Id");
		idCol.setCellValueFactory(new PropertyValueFactory<Entity, Integer>("id"));

		TableColumn<Entity, String> nameCol = new TableColumn<Entity, String>("Name");
		nameCol.setCellValueFactory(new PropertyValueFactory<Entity, String>("name"));

		columns.add(idCol);
		columns.add(nameCol);

		return columns;
	}

	public List<TableColumn<Entity, ?>> populateCustomColumns() {
		List<TableColumn<Entity, ?>> columns = new ArrayList<TableColumn<Entity, ?>>();

		List<String> attributeColumnNames = new ArrayList<String>();
		List<String> childrenColumnNames = new ArrayList<String>();

		for (Entity entity : view.getTableEntities()) {
			if (entity.getAttributes() != null) {
				for (Map.Entry<String, Object> entry : entity.getAttributes().entrySet()) {
					final String key = entry.getKey();
					if (!attributeColumnNames.contains(key)) {
						TableColumn<Entity, Object> attributeCol = new TableColumn<Entity, Object>(
								"Attribute - " + key);
						attributeCol.setCellValueFactory(
								new Callback<TableColumn.CellDataFeatures<Entity, Object>, ObservableValue<Object>>() {
									public ObservableValue<Object> call(
											TableColumn.CellDataFeatures<Entity, Object> param) {
										Map<String, Object> attributes = param.getValue().getAttributes();
										if (attributes == null || attributes.get(key) == null)
											return null;

										Object attribute = attributes.get(key);
										return new SimpleObjectProperty<Object>(attribute);
									}
								});

						columns.add(attributeCol);
						attributeColumnNames.add(key);
					}
				}
			}
			if (entity.getChildren() != null) {
				for (Map.Entry<String, Entity> entry : entity.getChildren().entrySet()) {
					final String key = entry.getKey();
					if (!childrenColumnNames.contains(key)) {
						TableColumn<Entity, String> childCol = new TableColumn<Entity, String>("Child - " + key);
						childCol.setCellValueFactory(
								new Callback<TableColumn.CellDataFeatures<Entity, String>, ObservableValue<String>>() {
									public ObservableValue<String> call(
											TableColumn.CellDataFeatures<Entity, String> param) {
										Map<String, Entity> children = param.getValue().getChildren();
										if (children == null || children.get(key) == null)
											return null;

										Entity child = children.get(key);
										return new SimpleStringProperty(
												child.getName() + " (Id " + child.getId() + ")");
									}
								});

						columns.add(childCol);
						childrenColumnNames.add(key);
					}
				}
			}
		}
		return columns;
	}

	public List<RadioButton> getSortRadioButtons(String activeSortColumnName) {
		ToggleGroup group = new ToggleGroup();

		List<RadioButton> buttons = new ArrayList<RadioButton>();
		List<TableColumn<Entity, ?>> columns = view.getTableColumns();
		boolean selectionSet = false;

		for (final TableColumn<Entity, ?> column : columns) {
			RadioButton button = new RadioButton();
			button.setText(column.getText());
			button.setUserData(column.getId());
			button.setToggleGroup(group);
			button.selectedProperty().addListener(new ChangeListener<Boolean>() {
				public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected,
						Boolean isNowSelected) {
					if (isNowSelected) {
						onSortTypeChanged(column);
					}
				}
			});

			// Try sorting by previous column
			if (activeSortColumnName != null && button.getText().equals(activeSortColumnName)) {
				button.setSelected(true);
				selectionSet = true;
			}

			buttons.add(button);
		}

		// Default sort by Id
		if (!selectionSet)
			for (final RadioButton button : buttons)
				if (button.getText().equals("Id"))
					button.setSelected(true);

		return buttons;
	}

	public void onSortTypeChanged(TableColumn<Entity, ?> column) {
		view.sortTableByColumn(column);
	}

	public String getInfoText() {
		return formatter.getInfoText();
	}

	public boolean showIdInputField() {
		return !formatter.isAutoIncrementEnabled();
	}

	private void refresh() {
		refresh(formatter.getAllEntities());
	}

	private void refresh(List<Entity> data) {
		Set<Entity> dataSet = new HashSet<Entity>(data);
		String activeSortColumnName = view.getActiveSortColumnName();

		view.clearTable();
		view.clearTableSelection();

		List<Entity> children = new ArrayList<Entity>();

		for (Entity entity : dataSet) {
			if (entity.getChildren() == null)
				continue;

			children.addAll(entity.getChildren().values());
		}
		dataSet.addAll(children);

		view.addEntitiesToTable(new ArrayList<Entity>(dataSet));

		List<TableColumn<Entity, ?>> columns = new ArrayList<TableColumn<Entity, ?>>();
		columns.addAll(populateCommonColumns());
		columns.addAll(populateCustomColumns());

		view.populateColumns(columns);

		enableButtonsForNoSelection();
		view.setSortRadioButtons(getSortRadioButtons(activeSortColumnName));
	}

	private void enableButtonsForNoSelection() {
		view.enableCreateChildButton(false);
		view.enableUpdateButton(false);
		view.enableDeleteButton(false);
		view.enableDeleteMultipleButton(false);
	}

	private void enableButtonsForSingleSelection(boolean enableChildCreation) {
		view.enableCreateChildButton(enableChildCreation);
		view.enableUpdateButton(true);
		view.enableDeleteButton(true);
		view.enableDeleteMultipleButton(false);
	}

	private void enableButtonsForMultipleSelection() {
		view.enableCreateChildButton(false);
		view.enableUpdateButton(false);
		view.enableDeleteButton(false);
		view.enableDeleteMultipleButton(true);
	}
}
