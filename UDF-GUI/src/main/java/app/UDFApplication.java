package app;

import app.views.SetupStage;
import javafx.application.Application;
import javafx.stage.Stage;

public class UDFApplication extends Application {
	
	public static void start() {
		launch();
	}

	@Override
    public void start(Stage primaryStage) {
		SetupStage setupStage = new SetupStage();
		setupStage.show();
    }
}
