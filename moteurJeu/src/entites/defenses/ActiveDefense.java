package entites.defenses;

import entites.enemies.Ennemy;

public abstract class ActiveDefense extends Defense {

    private double attackSpeed;

    private double health;

    public ActiveDefense(double x, double y, int health, int damage, double range, double attackSpeed) {
        super(x, y, damage, range);
        this.attackSpeed = attackSpeed;
        this.health = health;
    }

    @Override
    public void attack(Ennemy target) {
        // On détecte si un ennemi est dans la portée de la défense avec une formule de distance euclidienne
        if (((target.getX() - this.getX()) * (target.getX() - this.getX()) +
                (target.getY() - this.getY()) * (target.getY() - this.getY()) <= this.getRange() * this.getRange())) {
            // On attaque l'ennemi
            target.takeDamage(this.getDamages()*getBonus(getType(), target.getType()));
        }
    }

    public void takeDamage(double damage) {
        health -= damage;
    }

}
