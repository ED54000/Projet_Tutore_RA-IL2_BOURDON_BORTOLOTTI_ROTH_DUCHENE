package entites.enemies;

import javafx.scene.image.Image;
import laby.ModeleLabyrinth;
import moteur.MoteurJeu;
import moteur.SimpleMode;
import steering_astar.Steering.Vector2D;


public class Giant extends Ennemy {

    public Giant(Vector2D position, String name) {
        super(position,
                200 + (Math.random() - 0.5) * 40,
                1.33 + (Math.random() - 0.75),
                30 + (Math.random() - 0.5) * 10,
                500,
                1.5, 1, name,"/giant", "Normal");

        // Si on est en mode simple
        if(SimpleMode.getSimpleMode() && !ModeleLabyrinth.getSimulation()) {
            this.setSprite(SimpleMode.addTextToImage("" + (int)this.getHealth(), new Image("/gray.png")));
        }
    }
}
