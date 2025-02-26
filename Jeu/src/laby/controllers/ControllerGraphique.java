package laby.controllers;

import javafx.scene.input.MouseEvent;
import laby.ModeleLabyrinth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControllerGraphique implements javafx.event.EventHandler<MouseEvent> {

    private final ModeleLabyrinth laby;
    private double score = 0;
    private final HashMap<String, List<Double>> donnees;

    public ControllerGraphique(ModeleLabyrinth laby) {
        this.laby = laby;
        this.donnees = new HashMap<>();
        this.donnees.put("Score du groupe", new ArrayList<>());
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        // Récupérer la liste actuelle des scores et ajouter le nouveau score
        List<Double> scores = donnees.get("Score du groupe");
        scores.add(score);  // On ajoute le score actuel à la liste

        // Mettre à jour les données du graphique
        laby.setDonneesGraphique(donnees);
        laby.setGraphique(true);
    }

    public void setScore(double bestScore) {
        this.score = bestScore;
    }
}
