package entites.enemies;

import steering_astar.Steering.Vector2D;

public class Druide extends Ennemy {
    public Druide(Vector2D position, String name) {

        super(position, 100, 3, 10, 1, 2.5, 1,name,"/druide.png", "Healer");
        this.setHealth(this.getHealth() + (Math.random() - 0.5) * 20);
        this.setSpeed(this.getSpeed() + (Math.random() - 0.5));
        this.setDamages(this.getDamages() + (Math.random() - 0.5) * 2);
        this.setAttackSpeed(this.getAttackSpeed() + (Math.random() - 0.5) * 0.2);

    }
}



