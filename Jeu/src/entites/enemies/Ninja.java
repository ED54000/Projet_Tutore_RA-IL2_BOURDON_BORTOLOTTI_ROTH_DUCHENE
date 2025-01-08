package entites.enemies;

import entites.defenses.Defense;
import steering_astar.Steering.Vector2D;

public class Ninja extends Ennemy{

    public Ninja(Vector2D position, String name) {
        super(position, 80, 4, 50, 1.5, 0.35, 1, name,"/ninja.png", "Fugitive");
        this.setHealth(this.getHealth() + (Math.random() - 0.5) * 20);
        this.setSpeed(this.getSpeed() + (Math.random() - 0.5));
        this.setDamages(this.getDamages() + (Math.random() - 0.5) * 20);
        this.setAttackSpeed(this.getAttackSpeed() + (Math.random() - 0.5) * 0.2);



    }
}
