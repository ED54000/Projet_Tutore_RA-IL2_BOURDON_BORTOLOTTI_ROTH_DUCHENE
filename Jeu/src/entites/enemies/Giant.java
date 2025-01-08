package entites.enemies;

import steering_astar.Steering.Vector2D;

public class Giant extends Ennemy {

    public Giant(Vector2D position, String name) {

        super(position, 200, 2, 30, 0.5, 1.5, 1, name,"/giant.png", "Normal");
        this.setHealth(this.getHealth() - 20 + Math.random() * 40);
        this.setSpeed(this.getSpeed() + (Math.random() - 0.5));
        this.setDamages(this.getDamages() + (Math.random() - 0.5) * 10);
        this.setAttackSpeed(this.getAttackSpeed() + (Math.random() - 0.5) * 0.2);

    }
}
