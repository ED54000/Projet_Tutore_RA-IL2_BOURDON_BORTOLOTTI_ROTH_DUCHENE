package entites.enemies;

import steering_astar.Steering.Vector2D;

public class Berserker extends Ennemy{

    public Berserker(Vector2D position, String name) {
        super(position, 100, 3, 75, 1, 0.25, 1, name);
    }
}
