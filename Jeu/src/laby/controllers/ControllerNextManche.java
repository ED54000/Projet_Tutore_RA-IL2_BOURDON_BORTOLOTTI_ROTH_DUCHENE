package laby.controllers;

import entites.enemies.Ennemy;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import laby.ModeleLabyrinth;
import moteur.MoteurJeu;
import moteur.SimpleMode;
import steering_astar.Steering.PathfollowingBehavior;
import steering_astar.Steering.Vector2D;

import java.io.IOException;

public class ControllerNextManche implements EventHandler<MouseEvent> {

    ModeleLabyrinth laby;

    public ControllerNextManche(ModeleLabyrinth laby) {
        this.laby = laby;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        //System.out.println("Clique manche suivante");
        // On met à jour le time de départ de la manche
        laby.setStartTime();
        laby.setPauseManche(false);
        //clear les logs si on est pas en simulation
        if (!laby.getSimulation()) {
            VBox parentVBox = (VBox) ((Button) mouseEvent.getSource()).getParent();
            parentVBox.getChildren().clear();
            //laby.setLogs("Manche "+nbManches);
            //parentVBox.getChildren().add(new Label("Manche " + nbManches));
        }
        int nbManches = laby.getNbManches()+1;
        laby.setNbManches(nbManches);
        System.out.println("===========================================");
        System.out.println("Fin de la manche");
        System.out.println("Nouvelle manche : " + nbManches);
        ModeleLabyrinth.setLogs("Manche "+nbManches);

        // Affiche les ennemis en jeu
        System.out.println("Ennemis en jeu : ");
        for(Ennemy e: laby.enemies) {
            System.out.println(e + "(" + e.getName() + ")");
            //Affiche ses waypoints
            System.out.println("Waypoints : ");
            for (Vector2D waypoint : ((PathfollowingBehavior) e.getListBehavior().get(0)).getCheckpoints()) {
                System.out.println(waypoint.div(41));
            }
        }

        // Si le jeu est en mode simple
        if(SimpleMode.getSimpleMode()) {
            for(Ennemy e: laby.enemies) {
                ModeleLabyrinth.updateSprite(e);
            }
        }
    }
}
