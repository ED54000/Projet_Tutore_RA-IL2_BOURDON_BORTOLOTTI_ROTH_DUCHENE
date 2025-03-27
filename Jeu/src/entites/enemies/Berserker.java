package entites.enemies;

import javafx.scene.image.Image;
import laby.ModeleLabyrinth;
import moteur.MoteurJeu;
import moteur.SimpleMode;
import steering_astar.Steering.Vector2D;

public class Berserker extends Ennemy {

    public Berserker(Vector2D position, String name) {
        super(position,
                900 + (Math.random() - 0.5) * 10,
                1.75 + (Math.random() - 0.5),
                1000 + (Math.random() - 0.5) * 10,
                1000,
                0.75, 1, name, "/berserker", "Kamikaze");

        // Si on est en mode simple
        if(SimpleMode.getSimpleMode() && !ModeleLabyrinth.getSimulation()) {
            this.setSprite(SimpleMode.addTextToImage("" + (int)this.getHealth(), new Image("/red.png")));
        }
    }
}
