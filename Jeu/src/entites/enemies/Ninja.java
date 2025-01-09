package entites.enemies;

import steering_astar.Steering.Vector2D;

public class Ninja extends Ennemy{

    public Ninja(Vector2D position, String name) {
        super(position,
                80 + (Math.random() - 0.5) * 20,
                4 + (Math.random() - 0.5),
                50 + (Math.random() - 0.5) * 10,
                1.5 + (Math.random() - 0.5) * 0.2,
                0.35, 1, name,"/ninja.png", "Fugitive");
    }
}
