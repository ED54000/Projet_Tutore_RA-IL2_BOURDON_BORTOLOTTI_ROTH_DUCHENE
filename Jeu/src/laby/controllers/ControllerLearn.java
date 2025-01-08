package laby.controllers;

import entites.defenses.Bomb;
import entites.defenses.Canon;
import entites.defenses.Defense;
import entites.enemies.*;
import evolution.EnnemyEvolution;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import laby.ModeleLabyrinth;
import steering_astar.Steering.PathfollowingBehavior;
import steering_astar.Steering.Vector2D;

import java.util.Arrays;

public class ControllerLearn implements EventHandler<MouseEvent> {

    ModeleLabyrinth laby;

    public ControllerLearn(ModeleLabyrinth laby) {
        this.laby = laby;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        // On remet les ennemis au début pour éviter les attaques
        for(Ennemy e : laby.getEnnemyEndOfManche()){
            e.setToStart(laby);
        }
        // On fait évoluer les ennemis et les défenses
        laby.enemies = EnnemyEvolution.evoluer(laby.getEnnemyEndOfManche());
        laby.defenses = laby.getDefenseEndOfManche();


        //on remet les valeurs par défaut
        laby.refreshEnnemyArrived();
        laby.refreshDeadEnemies();
        laby.refreshEnnemyEndOfManche();

        laby.refreshDefenseEndOfManche();
        laby.refreshDeadDefenses();

        //TODO : on remet les valeurs par défaut pour les defenses en attendant de les faire évoluer
        for (Defense d : laby.defenses) {
            d.setDead(false);
            if (d instanceof Canon) {
                d.setHealth(300);
            }
            if (d instanceof Bomb) {
                d.setHealth(1000);
            }
            if (d instanceof entites.defenses.Archer) {
                d.setHealth(200);
            }
        }

            // On va compter le nombre d'ennemis pour chaque comportement
            int nbNinja = 0;
            int nbGiant = 0;
            int nbHealer = 0;
            int nbBerserker = 0;
            for(Ennemy e : laby.enemies){

                if(e instanceof Ninja){
                    e.setBehavior("Fugitive");
                    nbNinja++;
                }
                if(e instanceof Giant){
                    e.setBehavior("Normal");
                    nbGiant++;
                }
                if(e instanceof Druide){
                    e.setBehavior("Healer");
                    nbHealer++;
                }
                if(e instanceof Berserker){
                    e.setBehavior("Kamikaze");
                    nbBerserker++;
                }
            }
            System.out.println("Ninja : "+nbNinja+" Giant : "+nbGiant+" Healer : "+nbHealer+" Berserker : "+nbBerserker);
            laby.createBehaviours(laby.getCases());
            for (Ennemy e : laby.enemies) {
                if (e.getBehavior().equals("Healer")) {
                  e.setBehaviorPath(new PathfollowingBehavior(laby.getNewHealerAStar(nbHealer, nbGiant, nbBerserker, nbNinja)));
                } else {
                    e.setBehaviorPath(new PathfollowingBehavior(laby.getBehavioursMap().get(e.getBehavior())));
                }
                e.setDead(false);
                e.setArrived(false);
            }


        int c = 0;
        for (Ennemy e : laby.enemies) {
            if (e.getHealth() < 0 ){
                e.setHealth(e.getHealth()*-1);
            }
            System.out.println("Ennemy " + c + " après évolution : " + e.getName() + " type:" + e.getType() + " vie" + e.getHealth() + " vitesse :" + e.getSpeed() + " dégâts :" + e.getDamages() + " distance arrivée :" + e.getDistanceToArrival() + " behavior :" + e.getBehavior());
            c++;
        }

        //clear les logs si ce n'est pas une simulation
        if (!laby.estSimulation()) {
            VBox parentVBox = (VBox) ((Button) mouseEvent.getSource()).getParent();
            parentVBox.getChildren().clear();

            parentVBox.getChildren().add(new Label("Learned"));

            Button nextManche = new Button("Next Manche");
            nextManche.setOnMouseClicked(new ControllerNextManche(laby));
            parentVBox.getChildren().add(nextManche);
        }
    }
}
