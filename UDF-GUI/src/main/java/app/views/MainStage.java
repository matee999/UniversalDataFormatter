package app.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import app.controllers.MainStageController;
import app.data_holder.ChildEntityDataHolder;
import formatter.models.Entity;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainStage extends Stage implements MainStageListener {

	private MainStageController controller;

	private TableView<Entity> entityTableView = new TableView<Entity>();
	private List<TableColumn<Entity, ?>> cols = new ArrayList<TableColumn<Entity, ?>>();

	private Button createEntityButton = new Button("Create entity");
	private Button createChildEntityButton = new Button("Create child entity");
	private Button updateEntityButton = new Button("Update entity");
	private Button deleteEntityButton = new Button("Delete entity");
	private Button deleteEntitiesButton = new Button("Delete entities");

	private VBox sortBar = new VBox(30);

	private CheckBox sortAscDescCheckBox = new CheckBox("Sort descending");

	private TextField idSearchField = new TextField();
	private TextField nameEqualsSearchField = new TextField();
	private TextField nameStartsWithSearchField = new TextField();
	private TextField nameEndsWithSearchField = new TextField();
	private TextField containAttributeKeySearchField = new TextField();
	private TextField containAttributeValueSearchField = new TextField();
	private TextField containChildKeyWithAttributeValueSearchFieldPartOne = new TextField();
	private TextField containChildKeyWithAttributeValueSearchFieldPartTwo = new TextField();
	private TextField containChildKeyWithAttributeValueSearchFieldPartThree = new TextField();

	public MainStage() {
		controller = new MainStageController(this);

		setTitle("Universal Data Formatter - UDF");
		
		Scene scene = new Scene(setupView());
		setMinWidth(1200);
		setMinHeight(800);
		setScene(scene);
	}
	
	private BorderPane setupView() {
		entityTableView.getColumns().addAll(cols);
		entityTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		entityTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Entity>() {
			public void changed(ObservableValue<? extends Entity> observable, Entity oldValue, Entity newValue) {
				controller.onTableSelectionChanged();
			}
		});

		deleteEntityButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Entity selected = getSelectedEntities().get(0);

				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirm");
				alert.setHeaderText("Delete selected Entity?");

				CheckBox cascadeDeleteCheckBox = new CheckBox("Cascade delete children?");
				cascadeDeleteCheckBox.setDisable(selected.getChildren() == null);
				alert.getDialogPane().setContent(cascadeDeleteCheckBox);

				Optional<ButtonType> result = alert.showAndWait();

				if ((result.isPresent()) && (result.get() == ButtonType.OK))
					controller.onDeleteEntityClicked(selected, cascadeDeleteCheckBox.isSelected());
			}
		});

		deleteEntitiesButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				List<Entity> selected = getSelectedEntities();

				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirm");
				alert.setHeaderText("Delete selected Entities?");

				CheckBox cascadeDeleteCheckBox = new CheckBox("Cascade delete children?");
				alert.getDialogPane().setContent(cascadeDeleteCheckBox);

				Optional<ButtonType> result = alert.showAndWait();

				if ((result.isPresent()) && (result.get() == ButtonType.OK))
					controller.onDeleteEntitiesClicked(selected, cascadeDeleteCheckBox.isSelected());
			}
		});

		createEntityButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				showCreateEntityDialog();
			}
		});

		createChildEntityButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				showCreateChildEntityDialog();
			}
		});
		
		updateEntityButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				showUpdateEntityDialog();
			}
		});

		sortAscDescCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected,
					Boolean isNowSelected) {
				controller.onSortTypeChanged(entityTableView.getSortOrder().get(0));
			}
		});

		BorderPane borderPane = new BorderPane();
		borderPane.setCenter(entityTableView);

		HBox root = new HBox(30);

		VBox verticalBar = new VBox(30);
		verticalBar.setPadding(new Insets(20, 20, 20, 20));
		verticalBar.setAlignment(Pos.CENTER);
		verticalBar.getChildren().add(new Label("CRUD Operations"));
		verticalBar.getChildren().add(new Separator());
		verticalBar.getChildren().add(createEntityButton);
		verticalBar.getChildren().add(createChildEntityButton);
		verticalBar.getChildren().add(updateEntityButton);
		verticalBar.getChildren().add(deleteEntityButton);
		verticalBar.getChildren().add(deleteEntitiesButton);

		ScrollPane pane = new ScrollPane();
		pane.setStyle("-fx-background-color:transparent;");
		VBox sortBarHolder = new VBox(30);
		pane.setContent(sortBarHolder);
		sortBarHolder.setPadding(new Insets(20, 20, 20, 0));
		sortBarHolder.setAlignment(Pos.CENTER);
		sortBarHolder.getChildren().add(new Label("Sort Operations"));
		sortBarHolder.getChildren().add(new Separator());
		sortBarHolder.getChildren().add(sortBar);
		sortBarHolder.getChildren().add(new Separator());
		sortBarHolder.getChildren().add(sortAscDescCheckBox);

		root.getChildren().add(verticalBar);
		Separator verticalSeparator = new Separator();
		verticalSeparator.setOrientation(Orientation.VERTICAL);
		root.getChildren().add(verticalSeparator);
		root.getChildren().add(pane);

		BorderPane rootHolder = new BorderPane();
		rootHolder.setCenter(root);
		Separator horizontalSeparator = new Separator();
		horizontalSeparator.setOrientation(Orientation.HORIZONTAL);
		rootHolder.setBottom(horizontalSeparator);

		borderPane.setRight(rootHolder);

		HBox bottomBar = new HBox(30);
		bottomBar.setPadding(new Insets(20, 20, 20, 20));
		bottomBar.getChildren().add(new Label(controller.getInfoText()));
		borderPane.setBottom(bottomBar);

		TitledPane searchTitlePane = new TitledPane();
		searchTitlePane.setText("Search");
		VBox topBar = new VBox(5);
		HBox rowOne = new HBox(15);
		rowOne.setPadding(new Insets(10, 10, 10, 10));
		rowOne.getChildren().add(new Label("Id:"));
		idSearchField.setPromptText("Entity id");
		idSearchField.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				controller.onSearchQueryChanged();

				Platform.runLater(new Runnable() {
					public void run() {
						idSearchField.requestFocus();
					}
				});
			}
		});
		rowOne.getChildren().add(idSearchField);
		rowOne.getChildren().add(new Label("Name equals:"));
		nameEqualsSearchField.setPromptText("Entity name");
		nameEqualsSearchField.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				controller.onSearchQueryChanged();

				Platform.runLater(new Runnable() {
					public void run() {
						nameEqualsSearchField.requestFocus();
					}
				});
			}
		});
		rowOne.getChildren().add(nameEqualsSearchField);
		rowOne.getChildren().add(new Label("Name starts with:"));
		nameStartsWithSearchField.setPromptText("Entity name");
		rowOne.getChildren().add(nameStartsWithSearchField);
		nameStartsWithSearchField.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				controller.onSearchQueryChanged();

				Platform.runLater(new Runnable() {
					public void run() {
						nameStartsWithSearchField.requestFocus();
					}
				});
			}
		});
		rowOne.getChildren().add(new Label("Name ends with:"));
		nameEndsWithSearchField.setPromptText("Entity name");
		nameEndsWithSearchField.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				controller.onSearchQueryChanged();

				Platform.runLater(new Runnable() {
					public void run() {
						nameEndsWithSearchField.requestFocus();
					}
				});
			}
		});
		rowOne.getChildren().add(nameEndsWithSearchField);

		HBox rowTwo = new HBox(15);
		rowTwo.setPadding(new Insets(10, 10, 10, 10));
		rowTwo.getChildren().add(new Label("Contains attribute key:"));
		containAttributeKeySearchField.setPromptText("Attribute key");
		rowTwo.getChildren().add(containAttributeKeySearchField);
		containAttributeKeySearchField.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				controller.onSearchQueryChanged();

				Platform.runLater(new Runnable() {
					public void run() {
						containAttributeKeySearchField.requestFocus();
					}
				});
			}
		});
		rowTwo.getChildren().add(new Label("Contains attribute value:"));
		containAttributeValueSearchField.setPromptText("Attribute value");
		containAttributeValueSearchField.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				controller.onSearchQueryChanged();

				Platform.runLater(new Runnable() {
					public void run() {
						containAttributeValueSearchField.requestFocus();
					}
				});
			}
		});
		rowTwo.getChildren().add(containAttributeValueSearchField);

		HBox rowThree = new HBox(15);
		rowThree.setPadding(new Insets(10, 10, 10, 10));
		rowThree.getChildren().add(new Label("Contains child key with attribute value:"));
		containChildKeyWithAttributeValueSearchFieldPartOne.setPromptText("Child key");
		containChildKeyWithAttributeValueSearchFieldPartOne.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				controller.onSearchQueryChanged();

				Platform.runLater(new Runnable() {
					public void run() {
						containChildKeyWithAttributeValueSearchFieldPartOne.requestFocus();
					}
				});
			}
		});
		rowThree.getChildren().add(containChildKeyWithAttributeValueSearchFieldPartOne);
		containChildKeyWithAttributeValueSearchFieldPartTwo.setPromptText("Nested child attribute key (optional)");
		containChildKeyWithAttributeValueSearchFieldPartTwo.setMinWidth(220);
		containChildKeyWithAttributeValueSearchFieldPartTwo.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				controller.onSearchQueryChanged();

				Platform.runLater(new Runnable() {
					public void run() {
						containChildKeyWithAttributeValueSearchFieldPartTwo.requestFocus();
					}
				});
			}
		});
		rowThree.getChildren().add(containChildKeyWithAttributeValueSearchFieldPartTwo);
		containChildKeyWithAttributeValueSearchFieldPartThree.setPromptText("Nested child attribute value (optional)");
		containChildKeyWithAttributeValueSearchFieldPartThree.setMinWidth(230);
		containChildKeyWithAttributeValueSearchFieldPartThree.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				controller.onSearchQueryChanged();

				Platform.runLater(new Runnable() {
					public void run() {
						containChildKeyWithAttributeValueSearchFieldPartThree.requestFocus();
					}
				});
			}
		});
		rowThree.getChildren().add(containChildKeyWithAttributeValueSearchFieldPartThree);

		topBar.getChildren().add(rowOne);
		topBar.getChildren().add(rowTwo);
		topBar.getChildren().add(rowThree);

		searchTitlePane.setContent(topBar);
		borderPane.setTop(searchTitlePane);
		
		return borderPane;
	}

	private void showErrorAlert(String message) {
		final Alert errorAlert = new Alert(AlertType.ERROR);
		errorAlert.setTitle("Error");
		errorAlert.setHeaderText(message);

		errorAlert.show();
	}

	private void showCreateEntityDialog() {
		final Alert alert = new Alert(AlertType.INFORMATION);
		alert.getDialogPane().setMaxHeight(600);
		alert.getDialogPane().setMinWidth(450);
		alert.setTitle("New entity");
		alert.setHeaderText("Create new Entity");
		ScrollPane scrollPane = new ScrollPane();
		final VBox content = new VBox(15);
		scrollPane.setContent(content);
		Label idLabel = new Label("Id:");
		Label nameLabel = new Label("Name:");
		HBox rowOne = new HBox(10);
		TextField idTextField = new TextField();
		TextField nameTextField = new TextField();

		if (controller.showIdInputField()) {
			rowOne.getChildren().add(idLabel);
			rowOne.getChildren().add(idTextField);
		}
		rowOne.getChildren().add(nameLabel);
		rowOne.getChildren().add(nameTextField);
		content.getChildren().add(rowOne);

		final VBox attributeContent = new VBox(15);
		HBox rowTwo = new HBox();
		Button newAttributeButton = new Button("New attribute");
		rowTwo.getChildren().add(newAttributeButton);
		content.getChildren().add(rowTwo);

		final List<TextField> attributeKeyTextFields = new ArrayList<TextField>();
		final List<TextField> attributeValueTextFields = new ArrayList<TextField>();
		newAttributeButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				HBox newRow = new HBox(10);
				newRow.getChildren().add(new Label("Key:"));
				TextField keyField = new TextField();
				attributeKeyTextFields.add(keyField);
				newRow.getChildren().add(keyField);
				TextField valueField = new TextField();
				attributeValueTextFields.add(valueField);
				newRow.getChildren().add(new Label("Value:"));
				newRow.getChildren().add(valueField);
				attributeContent.getChildren().add(newRow);

				alert.getDialogPane().getScene().getWindow().sizeToScene();
			}
		});

		final VBox childrenContent = new VBox(15);
		HBox rowThree = new HBox();
		Button newChildButton = new Button("New child");
		rowThree.getChildren().add(newChildButton);
		content.getChildren().add(rowThree);

		final List<TextField> childIdTextFields = new ArrayList<TextField>();
		final List<TextField> childNameTextFields = new ArrayList<TextField>();
		final List<TextField> childKeyTextFields = new ArrayList<TextField>();
		newChildButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				HBox newRow = new HBox(10);
				TextField keyField = new TextField();
				childKeyTextFields.add(keyField);
				newRow.getChildren().add(new Label("Key:"));
				newRow.getChildren().add(keyField);
				if (controller.showIdInputField()) {
					newRow.getChildren().add(new Label("Id:"));
					TextField idField = new TextField();
					childIdTextFields.add(idField);
					newRow.getChildren().add(idField);
				}
				TextField nameField = new TextField();
				childNameTextFields.add(nameField);
				newRow.getChildren().add(new Label("Name:"));
				newRow.getChildren().add(nameField);
				childrenContent.getChildren().add(newRow);

				alert.getDialogPane().getScene().getWindow().sizeToScene();
			}
		});

		content.getChildren().add(new Label("Attributes:"));
		content.getChildren().add(newAttributeButton);
		content.getChildren().add(attributeContent);
		content.getChildren().add(new Separator());
		content.getChildren().add(new Label("Children:"));
		content.getChildren().add(newChildButton);
		content.getChildren().add(childrenContent);

		alert.getDialogPane().setContent(scrollPane);

		Optional<ButtonType> result = alert.showAndWait();

		if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
			Map<String, Object> attributes = new HashMap<String, Object>();
			for (int i = 0; i < attributeKeyTextFields.size(); i++) {
				String value = attributeValueTextFields.get(i).getText();
				if (!attributeKeyTextFields.get(i).getText().isBlank()) {
					attributes.put(attributeKeyTextFields.get(i).getText(),
							value.isBlank() || value.equals("null") ? null : value);
				}
			}
			List<ChildEntityDataHolder> childEntities = new ArrayList<ChildEntityDataHolder>();
			for (int i = 0; i < childNameTextFields.size(); i++) {
				if (!childKeyTextFields.get(i).getText().isBlank() && !childNameTextFields.get(i).getText().isBlank()) {
					childEntities.add(new ChildEntityDataHolder(childKeyTextFields.get(i).getText(),
							childIdTextFields.size() > i ? childIdTextFields.get(i).getText() : "",
							childNameTextFields.get(i).getText()));
				}
			}

			try {
				if (!controller.showIdInputField()) {
					if (childEntities.isEmpty())
						controller.onCreateEntityClicked(nameTextField.getText(),
								attributes.isEmpty() ? null : attributes);
					else
						controller.onCreateEntityClicked(nameTextField.getText(),
								attributes.isEmpty() ? null : attributes, childEntities);
				} else {
					if (childEntities.isEmpty())
						controller.onCreateEntityClicked(idTextField.getText(), nameTextField.getText(),
								attributes.isEmpty() ? null : attributes);
					else
						controller.onCreateEntityClicked(idTextField.getText(), nameTextField.getText(),
								attributes.isEmpty() ? null : attributes, childEntities);
				}
			} catch (Exception e) {
				showErrorAlert("Error creating entity");
			}
		}
	}

	private void showCreateChildEntityDialog() {
		final Alert alert = new Alert(AlertType.INFORMATION);
		alert.getDialogPane().setMaxHeight(600);
		alert.getDialogPane().setMinWidth(450);
		alert.setTitle("New child entity");
		alert.setHeaderText("Create new child Entity");
		ScrollPane scrollPane = new ScrollPane();
		final VBox content = new VBox(15);
		scrollPane.setContent(content);
		Label idLabel = new Label("Id:");
		Label nameLabel = new Label("Name:");
		HBox rowOne = new HBox(10);
		TextField idTextField = new TextField();
		TextField nameTextField = new TextField();

		if (controller.showIdInputField()) {
			rowOne.getChildren().add(idLabel);
			rowOne.getChildren().add(idTextField);
		}
		Entity parent = entityTableView.getSelectionModel().getSelectedItem();
		HBox headerBox = new HBox(15);
		headerBox.getChildren().add(new Label("Parent Id: " + parent.getId()));
		TextField keyForChildTextField = new TextField();
		content.getChildren().add(headerBox);
		content.getChildren().add(new Label("Key for child:"));
		content.getChildren().add(keyForChildTextField);
		rowOne.getChildren().add(nameLabel);
		rowOne.getChildren().add(nameTextField);
		final VBox attributeContent = new VBox(15);
		content.getChildren().add(rowOne);

		HBox rowTwo = new HBox();
		rowTwo.getChildren().add(new Label("Attributes:"));
		Button newAttributeButton = new Button("New attribute");
		rowTwo.getChildren().add(newAttributeButton);
		content.getChildren().add(rowTwo);

		final List<TextField> attributeKeyTextFields = new ArrayList<TextField>();
		final List<TextField> attributeValueTextFields = new ArrayList<TextField>();
		newAttributeButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				HBox newRow = new HBox(10);
				newRow.getChildren().add(new Label("Key:"));
				TextField keyField = new TextField();
				attributeKeyTextFields.add(keyField);
				newRow.getChildren().add(keyField);
				TextField valueField = new TextField();
				attributeValueTextFields.add(valueField);
				newRow.getChildren().add(new Label("Value:"));
				newRow.getChildren().add(valueField);
				attributeContent.getChildren().add(newRow);

				alert.getDialogPane().getScene().getWindow().sizeToScene();
			}
		});
		content.getChildren().add(newAttributeButton);
		content.getChildren().add(attributeContent);

		alert.getDialogPane().setContent(scrollPane);

		Optional<ButtonType> result = alert.showAndWait();

		if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
			Map<String, Object> attributes = new HashMap<String, Object>();
			for (int i = 0; i < attributeKeyTextFields.size(); i++) {
				String value = attributeValueTextFields.get(i).getText();
				if (!attributeKeyTextFields.get(i).getText().isBlank()) {
					attributes.put(attributeKeyTextFields.get(i).getText(),
							value.isBlank() || value.equals("null") ? null : value);
				}
			}

			try {
				if (idTextField.getText().isBlank())
					controller.onCreateChildEntityClicked(parent, keyForChildTextField.getText(),
							nameTextField.getText(), attributes.isEmpty() ? null : attributes);
				else
					controller.onCreateChildEntityClicked(parent, keyForChildTextField.getText(), idTextField.getText(),
							nameTextField.getText(), attributes.isEmpty() ? null : attributes);
			} catch (Exception e) {
				showErrorAlert("Error creating child entity");
			}
		}
	}

	private void showUpdateEntityDialog() {
		final Alert alert = new Alert(AlertType.INFORMATION);
		alert.getDialogPane().setMaxHeight(600);
		alert.getDialogPane().setMinWidth(450);
		alert.setTitle("Update entity");
		alert.setHeaderText("Update selected Entity");
		Entity selected = entityTableView.getSelectionModel().getSelectedItem();
		ScrollPane scrollPane = new ScrollPane();
		final VBox content = new VBox(15);
		scrollPane.setContent(content);
		Label nameLabel = new Label("Name:");
		HBox rowOne = new HBox(10);
		TextField nameTextField = new TextField();
		nameTextField.setText(selected.getName());

		HBox headerBox = new HBox(15);
		headerBox.getChildren().add(new Label("Selected Entity Id: " + selected.getId()));
		content.getChildren().add(headerBox);
		rowOne.getChildren().add(nameLabel);
		rowOne.getChildren().add(nameTextField);
		final VBox attributeContent = new VBox(15);
		content.getChildren().add(rowOne);

		Button newAttributeButton = new Button("New attribute");
		

		final List<TextField> attributeKeyTextFields = new ArrayList<TextField>();
		final List<TextField> attributeValueTextFields = new ArrayList<TextField>();

		if (selected.getAttributes() != null) {
			for (Map.Entry<String, Object> entry : selected.getAttributes().entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();

				HBox newRow = new HBox(10);
				TextField keyField = new TextField(key);
				attributeKeyTextFields.add(keyField);
				TextField valueField = new TextField(value != null ? value.toString() : "");
				newRow.getChildren().add(new Label("Key:"));
				newRow.getChildren().add(keyField);
				attributeValueTextFields.add(valueField);
				newRow.getChildren().add(new Label("Value:"));
				newRow.getChildren().add(valueField);
				attributeContent.getChildren().add(newRow);
			}
			alert.getDialogPane().getScene().getWindow().sizeToScene();
		}

		newAttributeButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				HBox newRow = new HBox(10);
				newRow.getChildren().add(new Label("Key:"));
				TextField keyField = new TextField();
				attributeKeyTextFields.add(keyField);
				newRow.getChildren().add(keyField);
				TextField valueField = new TextField();
				attributeValueTextFields.add(valueField);
				newRow.getChildren().add(new Label("Value:"));
				newRow.getChildren().add(valueField);
				attributeContent.getChildren().add(newRow);

				alert.getDialogPane().getScene().getWindow().sizeToScene();
			}
		});
		
		final VBox childrenContent = new VBox(15);
		final List<Integer> deletedChildrenIds = new ArrayList<Integer>();
		
		if (selected.getChildren() != null) {
			for (final Entity child : selected.getChildren().values()) {
				final HBox row = new HBox(15);
				String key = "";
				for (String k : selected.getChildren().keySet())
					if (selected.getChildren().get(k).equals(child))
						key = k;
				
				if (key.isBlank())
					continue;
				
				final Label childKeyLabel = new Label("Child key: " + key);
				final Button deleteButton = new Button("Delete child");
				deleteButton.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						childrenContent.getChildren().remove(row);
						deletedChildrenIds.add(child.getId());
						alert.getDialogPane().getScene().getWindow().sizeToScene();
					}
				});
				
				row.getChildren().add(childKeyLabel);
				row.getChildren().add(deleteButton);
				childrenContent.getChildren().add(row);
			}
			alert.getDialogPane().getScene().getWindow().sizeToScene();
		}
		
		content.getChildren().add(new Label("Attributes:"));
		content.getChildren().add(newAttributeButton);
		content.getChildren().add(attributeContent);
		content.getChildren().add(new Separator());
		content.getChildren().add(new Label("Children:"));
		content.getChildren().add(childrenContent);

		alert.getDialogPane().setContent(scrollPane);

		Optional<ButtonType> result = alert.showAndWait();

		if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
			Map<String, Object> attributes = new HashMap<String, Object>();
			for (int i = 0; i < attributeKeyTextFields.size(); i++) {
				String value = attributeValueTextFields.get(i).getText();
				if (!attributeKeyTextFields.get(i).getText().isBlank()) {
					attributes.put(attributeKeyTextFields.get(i).getText(),
							value.isBlank() || value.equals("null") ? null : value);
				}
			}

			try {
				controller.onUpdateEntityClicked(selected, nameTextField.getText(),
						attributes.isEmpty() ? null : attributes, deletedChildrenIds);
			} catch (Exception e) {
				e.printStackTrace();
				showErrorAlert("Error updating entity");
			}
		}
	}

	// Listener methods:

	public List<Entity> getTableEntities() {
		return entityTableView.getItems();
	}

	public void clearTable() {
		entityTableView.getItems().clear();
	}

	public void clearTableSelection() {
		entityTableView.getSelectionModel().clearSelection();
	}

	public void addEntitiesToTable(List<Entity> entities) {
		entityTableView.getItems().addAll(entities);
	}

	public void enableCreateChildButton(boolean enable) {
		createChildEntityButton.setDisable(!enable);
	}

	public void enableUpdateButton(boolean enable) {
		updateEntityButton.setDisable(!enable);
	}

	public void enableDeleteButton(boolean enable) {
		deleteEntityButton.setDisable(!enable);
	}

	public void enableDeleteMultipleButton(boolean enable) {
		deleteEntitiesButton.setDisable(!enable);
	}

	public List<Entity> getSelectedEntities() {
		return entityTableView.getSelectionModel().getSelectedItems();
	}

	public List<TableColumn<Entity, ?>> getTableColumns() {
		return entityTableView.getColumns();
	}

	public void setSortRadioButtons(List<RadioButton> radioButtons) {
		sortBar.getChildren().clear();
		sortBar.getChildren().addAll(radioButtons);
	}

	public void populateColumns(List<TableColumn<Entity, ?>> columns) {
		entityTableView.getColumns().clear();
		entityTableView.getColumns().addAll(columns);
		entityTableView.refresh();
	}

	public void sortTableByColumn(TableColumn<Entity, ?> column) {
		entityTableView.getSortOrder().clear();
		column.setSortType(
				!sortAscDescCheckBox.isSelected() ? TableColumn.SortType.ASCENDING : TableColumn.SortType.DESCENDING);
		entityTableView.getSortOrder().add(column);
		entityTableView.getOnSort();
		entityTableView.sort();
	}

	public String getActiveSortColumnName() {
		if (!entityTableView.getSortOrder().isEmpty())
			return entityTableView.getSortOrder().get(0).getText();
		else
			return null;
	}

	public String getIdSearchText() {
		return idSearchField.getText();
	}

	public String getNameEqualsSearchText() {
		return nameEqualsSearchField.getText();
	}

	public String getNameStartsWithSearchText() {
		return nameStartsWithSearchField.getText();
	}

	public String getNameEndsWithSearchText() {
		return nameEndsWithSearchField.getText();
	}

	public String getContainsAttributeKeySearchText() {
		return containAttributeKeySearchField.getText();
	}

	public String getContainsAttributeValueSearchText() {
		return containAttributeValueSearchField.getText();
	}

	public String getContainsChildKeyWithAttributeValuePartOneSearchText() {
		return containChildKeyWithAttributeValueSearchFieldPartOne.getText();
	}

	public String getContainsChildKeyWithAttributeValuePartTwoSearchText() {
		return containChildKeyWithAttributeValueSearchFieldPartTwo.getText();
	}

	public String getContainsChildKeyWithAttributeValuePartThreeSearchText() {
		return containChildKeyWithAttributeValueSearchFieldPartThree.getText();
	}
}
