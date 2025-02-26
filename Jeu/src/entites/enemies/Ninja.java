package entites.enemies;

import javafx.scene.image.Image;
import laby.ModeleLabyrinth;
import moteur.MoteurJeu;
import steering_astar.Steering.Vector2D;

import static moteur.MoteurJeu.addTextToImage;

public class Ninja extends Ennemy{

    public Ninja(Vector2D position, String name) {
        super(position,
                80 + (Math.random() - 0.5) * 20,
                2.5 + (Math.random() - 0.5),
                50 + (Math.random() - 0.5) * 10,
                700,
                0.35, 1, name,"/ninja", "Fugitive");

        // Si on est en mode simple
        if(MoteurJeu.getSimpleMode() && !ModeleLabyrinth.getSimulation()) {
            this.setSprite(addTextToImage("" + (int)this.getHealth(), new Image("/blue.png")));
        }
    }
}
