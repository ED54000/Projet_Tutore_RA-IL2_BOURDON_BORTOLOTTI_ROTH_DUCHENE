package entites.defenses;

import entites.enemies.Ennemy;

public abstract class PassiveDefense extends Defense {

    public PassiveDefense(double x, double y, int damage, double range) {
            super(x, y, damage, range);
        }

    /**
     * Attaque un ennemi
     * @param target l'ennemi à attaquer
     */
    @Override
    public void attack(Ennemy target) {
        target.takeDamage(getDamages()*getBonus(getType(), target.getType()));
        // C'est une défense passive, donc après avoir attaqué, elle s'autodétruit
        this.takeDamage(10000);
    }

    /**
     * Prendre des dégâts
     * @param damage les dégâts à prendre
     */
    public void takeDamage(double damage) {
    }
}
