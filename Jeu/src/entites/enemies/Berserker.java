package entites.enemies;

import javafx.scene.image.Image;
import laby.ModeleLabyrinth;
import moteur.MoteurJeu;
import steering_astar.Steering.Vector2D;

import static moteur.MoteurJeu.addTextToImage;

public class Berserker extends Ennemy {

    public Berserker(Vector2D position, String name) {
        super(position,
                75 + (Math.random() - 0.5) * 10,
                1.75 + (Math.random() - 0.5),
                75 + (Math.random() - 0.5) * 10,
                1000,
                0.75, 1, name, "/berserker", "Kamikaze");

        // Si on est en mode simple
        if(MoteurJeu.getSimpleMode() && !ModeleLabyrinth.getSimulation()) {
            this.setSprite(addTextToImage("" + (int)this.getHealth(), new Image("/red.png")));
        }
    }
}
