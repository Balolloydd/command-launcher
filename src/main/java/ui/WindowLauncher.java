package ui;

import app.CommandProcessor;

import java.util.Objects;

import javafx.application.Platform;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class WindowLauncher {
    private static final PseudoClass ERROR = PseudoClass.getPseudoClass("error");

    private final Stage stage;
    private final TextField searchBar;
    private final CommandProcessor processor;

    public WindowLauncher(Stage stage, CommandProcessor processor) {
        this.stage = Objects.requireNonNull(stage);
        this.processor = Objects.requireNonNull(processor);

        // Configure window
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);

        // Create search bar
        searchBar = new TextField();
        searchBar.setPromptText("Type a command...");
        searchBar.getStyleClass().add("search-bar");
        searchBar.setPrefHeight(64);

        // Create layout
        StackPane root = new StackPane(searchBar);
        root.setPadding(new Insets(24));

        // Create scene
        Scene scene = new Scene(root, 728, 112);
        scene.setFill(Color.TRANSPARENT);

        // Apply CSS
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/ui/launcher.css")).toExternalForm());

        // Handle Enter
        searchBar.setOnAction(event -> executeCommand());

        // Reset error appearance when typing
        searchBar.textProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (!newValue.isEmpty()) {
                    searchBar.pseudoClassStateChanged(ERROR, false);
                    searchBar.setPromptText("Type a command...");
                }
            }
        );

        // Handle Escape
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                hide();
            }
        });

        stage.setScene(scene);
    }

    // Execute user command
    private void executeCommand() {
        String input = searchBar.getText().strip();

        if (input.isEmpty()) {
            return;
        }

        try {
            processor.process(input);
            hide();
        } catch (IllegalArgumentException | IllegalStateException e) {
            searchBar.clear();
            searchBar.setPromptText("Error: " + e.getMessage());
            searchBar.pseudoClassStateChanged(ERROR, true);
        }
    }

    // Display search window
    public void show() {
        searchBar.clear();
        searchBar.setPromptText("Type a command...");
        searchBar.pseudoClassStateChanged(ERROR, false);

        stage.show();
        stage.centerOnScreen();
        stage.toFront();
        stage.requestFocus();

        Platform.runLater(searchBar::requestFocus);
    }

    // Hide search window
    public void hide() {
        stage.hide();
    }

    // Toggle search window visibility
    public void toggle() {
        if (stage.isShowing()) {
            hide();
        } else {
            show();
        }
    }
}