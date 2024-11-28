package entites.defenses;

import entites.enemies.Ennemy;

public abstract class PassiveDefense extends Defense {

        public PassiveDefense(double x, double y, int damage, double range) {
            super(x, y, damage, range);
        }

        public void takeDamage(double damage) {

        }
}
