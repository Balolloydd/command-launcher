package app;

import hotkey.GlobalHotkeyManager;
import tray.SystemTrayManager;
import ui.WindowLauncher;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Main extends Application {
    private GlobalHotkeyManager hotkeyManager;
    private SystemTrayManager trayManager;

    @Override
    public void start(Stage primaryStage) {
        // Keep JavaFX running when its window is hidden
        Platform.setImplicitExit(false);

        // Initialize command processing
        CommandProcessor processor = CommandList.createProcessor();

        // Create the search window
        WindowLauncher window = new WindowLauncher(primaryStage, processor);

        // Configure system tray callbacks
        trayManager = new SystemTrayManager(() -> Platform.runLater(window::show), () -> Platform.runLater(Platform::exit));

        try {
            trayManager.start();
        } catch (IllegalStateException e) {
            System.err.println("System tray initialization failed: " + e.getMessage());

            Platform.exit();
            return;
        }

        // Configure global hotkey
        hotkeyManager = new GlobalHotkeyManager(() -> Platform.runLater(window::toggle));

        try {
            hotkeyManager.start();
        } catch (IllegalStateException e) {
            System.err.println("Global shortcut initialization failed:");
            e.printStackTrace();
        }

        // Display search bar on application startup
        window.show();
    }

    @Override
    public void stop() {
        // Unregister Windows global hotkey
        if (hotkeyManager != null) {
            hotkeyManager.close();
        }

        // Remove tray icon
        if (trayManager != null) {
            trayManager.close();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}   