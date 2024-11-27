package entites.defenses;

import entites.enemies.Ennemy;

public abstract class ActiveDefense extends Defense {

    private double attackSpeed;

    public ActiveDefense(double x, double y, int health, int damage, double range, double attackSpeed) {
        super(x, y, health, damage, range);
        this.attackSpeed = attackSpeed;
    }

    @Override
    public abstract void attack(Ennemy target);

}
