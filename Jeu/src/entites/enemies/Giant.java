package entites.enemies;

import javafx.scene.image.Image;
import moteur.MoteurJeu;
import steering_astar.Steering.Vector2D;

public class Giant extends Ennemy {

    public Giant(Vector2D position, String name) {
        super(position,
                200 + (Math.random() - 0.5) * 40,
                1.33 + (Math.random() - 0.5),
                30 + (Math.random() - 0.5) * 10,
                500,
                1.5, 1, name,"/giant.png", "Normal");

        // Si on est en mode simple
        if(MoteurJeu.getSimpleMode()) {
            this.setSprite(new Image("/gray.png"));
        }
    }
}
