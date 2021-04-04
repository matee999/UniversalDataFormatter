package app.views;

import java.io.File;

import app.controllers.SetupStageController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class SetupStage extends Stage {
	
	private SetupStageController controller;
	
	private static boolean storageValid = true;
	
	public SetupStage() {
		controller = new SetupStageController();
		
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(10));
		
		setTitle("Setup");

		VBox content = new VBox(20);
		HBox fileRow = new HBox(15);
		
		final CheckBox autoIncrementBox = new CheckBox("Use auto-increment:");
		autoIncrementBox.setSelected(controller.isAutoIncrementEnabled());
		autoIncrementBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected,
					Boolean isNowSelected) {
				controller.setAutoIncrementEnabled(isNowSelected);
			}
		});
		HBox autoIncrementRow = new HBox(15);
		autoIncrementRow.setPadding(new Insets(10, 0, 0, 0));
		autoIncrementRow.getChildren().add(autoIncrementBox);
		
		HBox maxEntityRow = new HBox(15);
		maxEntityRow.setPadding(new Insets(5, 0, 0, 0));
		maxEntityRow.getChildren().add(new Label("Maximum entities per file: "));
		final TextField maxEntityField = new TextField("" + controller.getEntityLimitPerFile());
		maxEntityRow.getChildren().add(maxEntityField);
		
		content.getChildren().add(fileRow);
		fileRow.getChildren().add(new Label("Storage: "));
		final Label pathLabel = new Label();
		pathLabel.setText(controller.getStoragePath());
		fileRow.getChildren().add(pathLabel);
		Button editPathButton = new Button("Change storage folder");
		fileRow.getChildren().add(editPathButton);
		editPathButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				DirectoryChooser chooser = new DirectoryChooser();
				chooser.setTitle("JavaFX Projects");
				File defaultDirectory = new File(controller.getStoragePath());
				chooser.setInitialDirectory(defaultDirectory);
				File selectedDirectory = chooser.showDialog(SetupStage.this);
				if (selectedDirectory != null) {
					pathLabel.setText(selectedDirectory.getPath());
					SetupStage.this.sizeToScene();
					try {
						controller.setStoragePath(selectedDirectory.getAbsolutePath());
						autoIncrementBox.setSelected(controller.isAutoIncrementEnabled());
						maxEntityField.setText("" + controller.getEntityLimitPerFile());
						storageValid = true;
					} catch (Exception e) {
						showErrorAlert("Illegal storage location");
						storageValid = false;
					}
				}
			}
		});
		
		content.getChildren().add(autoIncrementRow);
		content.getChildren().add(maxEntityRow);
		content.getChildren().add(new Separator());
		
		HBox buttonRow = new HBox(20);
		buttonRow.setPadding(new Insets(10, 0, 0, 0));
		Button okayButton = new Button("Confirm");
		Button exitButton = new Button("Exit");
		buttonRow.getChildren().add(okayButton);
		buttonRow.getChildren().add(exitButton);
		
		okayButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (storageValid) {
					try {
					controller.setEntityLimitPerFile(Integer.parseInt(maxEntityField.getText()));
					MainStage main = new MainStage();
					SetupStage.this.close();
					main.show();
					} catch (NumberFormatException e) {
						showErrorAlert("Broj nije validan");
					}
				}
			}
		});
		exitButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Platform.exit();
			}
		});
		
		pane.setTop(new Label(""));
		pane.setCenter(content);
		pane.setBottom(buttonRow);
		
		Scene scene = new Scene(pane);
		setScene(scene);
		SetupStage.this.sizeToScene();
	}
	
	private void showErrorAlert(String message) {
		final Alert errorAlert = new Alert(AlertType.ERROR);
		errorAlert.setTitle("Error");
		errorAlert.setHeaderText(message);

		errorAlert.show();
	}
}
