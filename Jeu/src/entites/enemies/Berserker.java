package entites.enemies;

import steering_astar.Steering.Vector2D;

public class Berserker extends Ennemy {

    public Berserker(Vector2D position, String name) {

        super(position, 75, 3, 75, 1, 0.75, 1, name, "/berserker.png", "Kamikaze");
        this.setHealth(this.getHealth() + (Math.random() - 0.5) * 10);
        this.setSpeed(this.getSpeed() + (Math.random() - 0.5));
        this.setDamages(this.getDamages() + (Math.random() - 0.5) * 10);
        this.setAttackSpeed(this.getAttackSpeed() + (Math.random() - 0.5) * 0.2);


    }
}
