package laby.controllers;

import entites.enemies.Ennemy;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import laby.ModeleLabyrinth;
import laby.views.ViewGraphique;

import java.util.*;
import java.util.stream.Collectors;

public class ControllerGraphique implements EventHandler<MouseEvent> {

    ModeleLabyrinth laby;
    double score = 0;
    HashMap<String, List<Double>> donnees;
    int manche = 0;  // Comptabilise le nombre de manches

    // Liste des types d'ennemis que l'on souhaite suivre
    private static final List<String> TYPES_ENNEMIS = Arrays.asList("Giant", "Ninja", "Berserker", "Druide");

    public ControllerGraphique(ModeleLabyrinth laby) {
        this.laby = laby;
        this.donnees = new HashMap<>();
    }

    @Override
    public void handle(MouseEvent mouseEvent) {

        // Grouper les ennemis par type
        Map<Class<? extends Ennemy>, List<Ennemy>> ennemisParType = laby.enemies.stream()
                .collect(Collectors.groupingBy(Ennemy::getClass));

        // Mettre à jour les données pour chaque type d'ennemi
        for (Map.Entry<Class<? extends Ennemy>, List<Ennemy>> entry : ennemisParType.entrySet()) {
            String nomType = entry.getKey().getSimpleName();  // Utilise le nom de la classe pour le nom de la courbe

            // Si la courbe n'existe pas encore, on la crée
            if (!donnees.containsKey(nomType)) {
                donnees.put(nomType, new ArrayList<>());
            }

            // Ajouter le nombre d'ennemis de ce type pour cette manche
            donnees.get(nomType).add((double) entry.getValue().size());
        }

        // Ajouter les types d'ennemis non présents dans cette manche avec une valeur de 0
        for (String type : TYPES_ENNEMIS) {
            // Si ce type n'existe pas dans les données, on l'ajoute avec une valeur de 0
            if (!donnees.containsKey(type)) {
                donnees.put(type, new ArrayList<>());
            }
            // Si ce type n'a pas été ajouté pour cette manche, on ajoute 0
            if (donnees.get(type).size() <= manche) {
                donnees.get(type).add(0.0);
            }
        }

        System.out.println("Données graphiques : " + donnees);

        // Mise à jour du graphique avec les nouvelles données
        laby.setDonneesGraphique(donnees);

        // Incrémenter le nombre de manches
        manche++;

        // Afficher la vue du graphique
        laby.setGraphique(true);
    }

    public void setScore(double bestScore) {
        this.score = bestScore;
    }
}
