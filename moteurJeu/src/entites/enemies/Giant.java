package entites.enemies;

import entites.defenses.Defense;

public class Giant extends Ennemy {

    public Giant(double x, double y) {
        super(x, y, 100, 0.05, 10, 1, 1, Integer.MAX_VALUE);
    }

    @Override
    public void attack(Defense target) {
        if (target != null) {
            target.takeDamage(getDamage()*getBonus(getType(), target.getType()));
        }
    }
}
