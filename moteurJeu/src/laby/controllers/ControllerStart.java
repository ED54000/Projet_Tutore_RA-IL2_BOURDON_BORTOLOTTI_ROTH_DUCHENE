package laby.controllers;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import laby.ModeleLabyrinth;
import moteur.MoteurJeu;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ControllerStart implements EventHandler<MouseEvent> {
    private ModeleLabyrinth laby;
    private ComboBox<String> labyrinthComboBox;
    private TextField enemiesField;
    private TextField roundsField;

    public ControllerStart(ModeleLabyrinth laby, ComboBox<String> labyrinthComboBox, TextField enemiesField, TextField roundsField) {
        this.laby = laby;
        this.labyrinthComboBox = labyrinthComboBox;
        this.enemiesField = enemiesField;
        this.roundsField = roundsField;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        Map<String, String> labyrinthMap = new HashMap<>();
        labyrinthMap.put("Petit", "Ressources/Labyrinthe1.txt");
        labyrinthMap.put("Grand", "Ressources/Labyrinthe2.txt");
        labyrinthMap.put("Large", "Ressources/Labyrinthe3.txt");
        // Pas besoin de lookup, les composants sont accessibles directement
        System.out.println(labyrinthComboBox.getValue());
        System.out.println(enemiesField.getText());
        System.out.println(roundsField.getText());

        // Fermer la fenêtre
        Stage dialogStage = (Stage) ((Button) mouseEvent.getSource()).getScene().getWindow();
        dialogStage.close();

        // Créer le labyrinthe
        try {
            laby.creerLabyrinthe(labyrinthMap.get(labyrinthComboBox.getValue()), Integer.parseInt(enemiesField.getText()), Integer.parseInt(roundsField.getText()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //startJeu(dialogStage);
    }
}
