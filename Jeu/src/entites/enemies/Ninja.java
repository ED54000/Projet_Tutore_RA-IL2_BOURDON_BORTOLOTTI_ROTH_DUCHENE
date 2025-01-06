package entites.enemies;

import entites.defenses.Defense;
import steering_astar.Steering.Vector2D;

public class Ninja extends Ennemy{

    public Ninja(Vector2D position, String name) {
        super(position, 80, 4, 50, 1.5, 0.35, 1, name);
    }
}
