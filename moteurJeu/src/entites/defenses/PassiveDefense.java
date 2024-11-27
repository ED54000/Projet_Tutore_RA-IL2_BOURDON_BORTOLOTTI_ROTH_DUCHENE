package entites.defenses;

import entites.enemies.Ennemy;

public abstract class PassiveDefense extends Defense {

        public PassiveDefense(double x, double y, int health, int damage, double range) {
            super(x, y, health, damage, range);
        }

    @Override
    public abstract void attack(Ennemy target);
}
