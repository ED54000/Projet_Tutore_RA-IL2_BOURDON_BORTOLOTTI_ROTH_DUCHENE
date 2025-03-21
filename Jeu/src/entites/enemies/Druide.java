package entites.enemies;

import javafx.scene.image.Image;
import laby.ModeleLabyrinth;
import moteur.MoteurJeu;
import steering_astar.Steering.Vector2D;

import static moteur.MoteurJeu.addTextToImage;

public class Druide extends Ennemy {

    public Druide(Vector2D position, String name) {
        super(position,
                100 + (Math.random() - 0.5) * 20,
                1.75 + (Math.random() - 0.5),
                10 + (Math.random() - 0.5) * 2,
                800+ (Math.random() - 0.5) * 0.2,
                2.5, 1,name,"/druide", "Healer");

        // Si on est en mode simple
        if(MoteurJeu.getSimpleMode() && !ModeleLabyrinth.getSimulation()) {
            this.setSprite(addTextToImage("" + (int)this.getHealth(), new Image("/green.png")));
        }
    }
}



