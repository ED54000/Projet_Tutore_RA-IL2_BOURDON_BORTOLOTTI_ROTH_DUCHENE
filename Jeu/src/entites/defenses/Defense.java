package entites.defenses;

import entites.Entity;
import steering_astar.Steering.Vector2D;

public abstract class Defense extends Entity {

    public Defense(double x, double y, int damage, double range, double health, String sprite, String name, double attackSpeed) {
        super(new Vector2D(x, y), damage, range, sprite, health, name, attackSpeed);
    }
}

