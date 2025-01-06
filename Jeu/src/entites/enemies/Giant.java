package entites.enemies;

import steering_astar.Steering.Vector2D;

public class Giant extends Ennemy {

    public Giant(Vector2D position, String name) {
        super(position, 200, 2, 50, 0.5, 1, 1, name);
    }
}
