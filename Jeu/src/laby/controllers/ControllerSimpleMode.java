package laby.controllers;

import javafx.event.EventHandler;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import laby.ModeleLabyrinth;
import laby.views.ViewLabyrinth;
import moteur.MoteurJeu;

public class ControllerSimpleMode implements EventHandler<MouseEvent> {

    private ModeleLabyrinth laby;
    private ViewLabyrinth viewLabyrinth;
    private ToggleButton toggleButton;

    public ControllerSimpleMode(ModeleLabyrinth laby, ViewLabyrinth viewLabyrinth, ToggleButton toggleButton) {
        this.laby = laby;
        this.viewLabyrinth = viewLabyrinth;
        this.toggleButton = toggleButton;
    }

    @Override
    public void handle(MouseEvent event) {
        if (toggleButton.isSelected()) {
            System.out.println("Mode simple activé!");
            MoteurJeu moteurJeu = new MoteurJeu();
            moteurJeu.enableSimpleMode(viewLabyrinth);
            toggleButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;"); // Le bouton passe en vert pour montrer que le mode simple est activé
        } else {
            System.out.println("Mode simple désactivé");
            MoteurJeu moteurJeu = new MoteurJeu();
            moteurJeu.disableSimpleMode(viewLabyrinth);
            toggleButton.setStyle(""); // Reset au style par défaut
        }
    }
}