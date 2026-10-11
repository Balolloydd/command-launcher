package app;

import hotkey.GlobalHotkeyManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import ui.WindowLauncher;

public class Main extends Application {
    private GlobalHotkeyManager hotkeyManager;

    @Override
    public void start(Stage primaryStage) {
        Platform.setImplicitExit(false);

        CommandProcessor processor = CommandList.createProcessor();
        WindowLauncher window = new WindowLauncher(primaryStage, processor);

        hotkeyManager = new GlobalHotkeyManager(() -> Platform.runLater(window::toggle));
        try {
            hotkeyManager.start();
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
            Platform.setImplicitExit(true);
        }

        window.show();
    }

    @Override
    public void stop() {
        if (hotkeyManager != null) {
            hotkeyManager.close();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
} 