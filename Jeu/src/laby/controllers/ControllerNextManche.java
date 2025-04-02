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
        ModeleLabyrinth.setLogs("Manche "+nbManches);

        // Si le jeu est en mode simple
        if(SimpleMode.getSimpleMode()) {
            for(Ennemy e: laby.enemies) {
                ModeleLabyrinth.updateSprite(e);
            }
        }
    }
}
