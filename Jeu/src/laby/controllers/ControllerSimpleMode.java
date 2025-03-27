package laby.controllers;

import javafx.event.EventHandler;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import laby.ModeleLabyrinth;
import laby.views.ViewLabyrinth;
import moteur.MoteurJeu;
import moteur.SimpleMode;

public class ControllerSimpleMode implements EventHandler<MouseEvent> {

    private ModeleLabyrinth laby;
    private ViewLabyrinth viewLabyrinth;
    private ToggleButton toggleButton;
    private SimpleMode simpleMode;

    public ControllerSimpleMode(ModeleLabyrinth laby, ViewLabyrinth viewLabyrinth, ToggleButton toggleButton, SimpleMode simpleMode) {
        this.laby = laby;
        this.viewLabyrinth = viewLabyrinth;
        this.toggleButton = toggleButton;
        this.simpleMode = simpleMode;
    }

    @Override
    public void handle(MouseEvent event) {
        if (toggleButton.isSelected()) {
            System.out.println("Mode simple activé!");
            simpleMode.enableSimpleMode(viewLabyrinth,laby);
            toggleButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;"); // Le bouton passe en vert pour montrer que le mode simple est activé
        } else {
            System.out.println("Mode simple désactivé");
            simpleMode.disableSimpleMode(viewLabyrinth,laby);
            toggleButton.setStyle(""); // Reset au style par défaut
        }
    }
}