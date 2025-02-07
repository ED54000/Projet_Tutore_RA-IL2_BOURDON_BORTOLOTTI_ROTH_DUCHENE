package entites.enemies;

import steering_astar.Steering.Vector2D;

public class Druide extends Ennemy {

    public Druide(Vector2D position, String name) {
        super(position,
                100 + (Math.random() - 0.5) * 20,
                3 + (Math.random() - 0.5),
                10 + (Math.random() - 0.5) * 2,
                800+ (Math.random() - 0.5) * 0.2,
                2.5, 1,name,"/druide.png", "Healer");
    }
}



