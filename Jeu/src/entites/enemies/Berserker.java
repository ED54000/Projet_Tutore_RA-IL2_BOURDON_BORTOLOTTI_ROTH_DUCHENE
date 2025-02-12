package entites.enemies;

import steering_astar.Steering.Vector2D;

public class Berserker extends Ennemy {

    public Berserker(Vector2D position, String name) {
        super(position,
                75 + (Math.random() - 0.5) * 10,
                1.75 + (Math.random() - 0.5),
                75 + (Math.random() - 0.5) * 10,
                1000,
                0.75, 1, name, "/berserker.png", "Kamikaze");
    }
}
