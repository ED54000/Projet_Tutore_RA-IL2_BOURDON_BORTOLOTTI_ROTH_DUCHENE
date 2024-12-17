package entites.defenses;

import entites.enemies.Ennemy;

public abstract class PassiveDefense extends Defense {

    public PassiveDefense(double x, double y, int damage, double range) {
            super(x, y, damage, range);
        }

    @Override
    public void attack(Ennemy target) {
        // On détecte si un ennemi est dans la portée de la défense avec une formule de distance euclidienne
        if (((target.getX() - this.getX()) * (target.getX() - this.getX()) +
                (target.getY() - this.getY()) * (target.getY() - this.getY()) <= this.getRange() * this.getRange())) {
            target.takeDamage(getDamages()*getBonus(getType(), target.getType()));
        }
        // C'est une défense passive, donc après avoir attaqué, elle s'autodétruit
        this.takeDamage(10000);
    }
    public void takeDamage(double damage) {
    }
}
