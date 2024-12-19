package laby.controllers;

import entites.enemies.Ennemy;
import entites.enemies.Giant;
import evolution.EnnemyEvolution;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import laby.ModeleLabyrinth;
import steering_astar.Steering.PathfollowingBehavior;
import steering_astar.Steering.Vector2D;

public class ControllerLearn implements EventHandler<MouseEvent> {

    ModeleLabyrinth laby;

    public ControllerLearn(ModeleLabyrinth laby) {
        this.laby = laby;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        //clear les logs
        VBox parentVBox = (VBox) ((Button) mouseEvent.getSource()).getParent();
        parentVBox.getChildren().clear();

        // On fait évoluer les ennemis
        laby.enemies = EnnemyEvolution.evoluer(laby.getEnnemyEndOfManche());

        // On va compter le nombre d'ennemis pour chaque comportement
        int nbNinja = 0;
        int nbGiant = 0;
        int nbHealer = 0;
        int nbBerserker = 0;
        for(Ennemy e : laby.enemies){
            if(e.getBehavior() == "Ninja"){
                nbNinja++;
            }
            if(e.getBehavior() == "Giant"){
                nbGiant++;
            }
            if(e.getBehavior() == "Druide"){
                nbHealer++;
            }
            if(e.getBehavior() == "Berserker"){
                nbBerserker++;
            }
        }
        System.out.println("Nombre de Ninjas : "+nbNinja);
        System.out.println("Nombre de Giants : "+nbGiant);
        System.out.println("Nombre de Healers : "+nbHealer);
        System.out.println("Nombre de Berserkers : "+nbBerserker);

        for(Ennemy e : laby.enemies){
            if(e.getBehavior() == "Healer"){
                // On doit passer en paramètres le nombre d'ennemis de chaque type pour que le comportement puisse être adapté
                laby.getNewHealerAStar(nbHealer, nbGiant, nbBerserker, nbNinja);
            }
            // On les place à la position de départ
            e.setPosition(new Vector2D(laby.getXstart(), laby.getYstart()));
            // On leur donne un aStar en fonction de leur comportement
            e.setBehaviorPath(new PathfollowingBehavior(laby.getBehavioursMap().get(e.getBehavior())));

        }
        System.out.println("nombre d'ennemis après évolution : "+laby.enemies.size());

        int c = 0;
        for (Ennemy e: laby.enemies) {
            System.out.println("Ennemy "+c+" après évolution : "+e.getName()+" type:"+e.getType()+" vie"+e.getHealth()+" vitesse :"+e.getSpeed()+" dégâts :"+e.getDamages()+" distance arrivée :"+e.getDistanceToArrival()+" behavior :"+e.getBehavior());
            c++;
        }

        /*
        System.out.println(laby.enemies.get(0)+ "Statistiques : ");
        System.out.println("Vie : "+laby.enemies.get(0).getHealth());
        System.out.println("Dégâts : "+laby.enemies.get(0).getDamages());
        System.out.println("Vitesse : "+laby.enemies.get(0).getSpeed());
        System.out.println("Type : "+laby.enemies.get(0).getType());
        System.out.println("Distance a l'arrivée : "+laby.enemies.get(0).getDistanceToArrival());
        System.out.println("Killer type : "+laby.enemies.get(0).getKillerType());
        System.out.println("Behavior : "+laby.enemies.get(0).getBehavior());
        System.out.println("=====================================");

         */

        /*
        laby.createBehaviours();
        Giant giant = new Giant(new Vector2D(laby.getXstart(), laby.getYstart()), "NewGiant");
        System.out.println(new PathfollowingBehavior(laby.getBehavioursMap().get(laby.getBehaviours().get(0))));
        giant.setBehaviorPath(new PathfollowingBehavior(laby.getBehavioursMap().get(laby.getBehaviours().get(0))));
        laby.enemies.add(giant);
         */

        //laby.setIterator(laby.enemies);
        //laby.setLogs("Learned");



        parentVBox.getChildren().add(new Label("Learned"));

        Button nextManche = new Button("Next Manche");
        nextManche.setOnMouseClicked(new ControllerNextManche(laby));
        parentVBox.getChildren().add(nextManche);

        //laby.setPause(false); //???
    }
}
