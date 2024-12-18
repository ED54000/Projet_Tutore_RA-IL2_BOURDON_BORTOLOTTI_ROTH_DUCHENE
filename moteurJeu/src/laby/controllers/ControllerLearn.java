package laby.controllers;

import entites.enemies.Ennemy;
import entites.enemies.Giant;
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


        //TODO : faire l'apprentissage
        laby.createBehaviours();
        Giant giant = new Giant(new Vector2D(laby.getXstart(), laby.getYstart()), "NewGiant");
        System.out.println(new PathfollowingBehavior(laby.getBehavioursMap().get(laby.getBehaviours().get(0))));
        giant.setBehaviorPath(new PathfollowingBehavior(laby.getBehavioursMap().get(laby.getBehaviours().get(0))));

        //laby.setIterator(laby.enemies);
        //laby.setLogs("Learned");



        parentVBox.getChildren().add(new Label("Learned"));
        laby.enemies.add(giant);

        Button nextManche = new Button("Next Manche");
        nextManche.setOnMouseClicked(new ControllerNextManche(laby));
        parentVBox.getChildren().add(nextManche);

        //laby.setPause(false); //???
    }
}
