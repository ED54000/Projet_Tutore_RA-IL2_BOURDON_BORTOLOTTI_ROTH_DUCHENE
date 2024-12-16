package entites.defenses;

import entites.enemies.Ennemy;

public class Bomb extends PassiveDefense{
    public Bomb(double x, double y) {
        super(x, y, 50, 1);
    }

    @Override
    public void attack(Ennemy target) {
        if (target != null) {
            target.takeDamage(getDamages()*getBonus(getType(), target.getType()));
        }
    }
}
