package app;

import javafx.application.Application;
import javafx.stage.Stage;

import ui.WindowLauncher;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        CommandProcessor processor = CommandList.createProcessor();

        WindowLauncher window = new WindowLauncher(
            primaryStage,
            processor
        );

        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}