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

    private static final PseudoClass ERROR =
        PseudoClass.getPseudoClass("error");

    private final Stage stage;
    private final TextField searchBar;

    public WindowLauncher(Stage stage, CommandProcessor processor) {
        this.stage = Objects.requireNonNull(stage);
        Objects.requireNonNull(processor);

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
        scene.getStylesheets().add(
            Objects.requireNonNull(
                getClass().getResource("/ui/launcher.css")
            ).toExternalForm()
        );

        // Handle Enter
        searchBar.setOnAction(event -> {
            executeCommand(processor);
        });

        // Clear error appearance when typing
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
                stage.close();
            }
        });

        stage.setScene(scene);
    }

    private void executeCommand(CommandProcessor processor) {
        String input = searchBar.getText().strip();

        if (input.isEmpty()) {
            return;
        }

        try {
            processor.process(input);
            stage.close();

        } catch (IllegalArgumentException | IllegalStateException e) {
            searchBar.clear();
            searchBar.setPromptText("Error: " + e.getMessage());
            searchBar.pseudoClassStateChanged(ERROR, true);
        }
    }

    public void show() {
        stage.show();
        stage.centerOnScreen();
        Platform.runLater(searchBar::requestFocus);
    }
}
