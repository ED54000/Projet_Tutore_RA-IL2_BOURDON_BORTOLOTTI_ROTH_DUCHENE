package laby.controllers;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import laby.ModeleLabyrinth;
import moteur.MoteurJeu;

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
        laby.setPause(false);
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

        // Si le jeu est en mode simple
        if(MoteurJeu.getSimpleMode()) {
            // On rappelle la méthode du mode simple

        }
    }
}
