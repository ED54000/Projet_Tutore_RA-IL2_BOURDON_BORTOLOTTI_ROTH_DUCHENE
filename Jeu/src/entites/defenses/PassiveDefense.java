package entites.defenses;

import entites.enemies.Ennemy;
import laby.ModeleLabyrinth;

public abstract class PassiveDefense extends Defense {

    //si la defense a deja attaqué
    private boolean attacked = false;

    public PassiveDefense(double x, double y, int damage, double range, double health, String sprite, String name) {
            super(x, y, damage, range, health, sprite, name);
        }

    /**
     * Attaque un ennemi
     * @param target l'ennemi à attaquer
     */
    @Override
    public void attack(Ennemy target) {
        // Si la défense a déjà attaqué, on ne fait rien
        if(attacked){
            return;
        }
        // Sinon, on attaque l'ennemi
        System.out.println("Bonus : "+ getBonus(getType(), target.getType()) );
        target.takeDamage(getDamages()*getBonus(getType(), target.getType()));
        System.out.println("Attaque de " + this.getName() + " de type : "
                + this.getType() +" sur " + target.getName() + " de type "+ target.getType()+
                "\nDégâts infligés : " + this.getDamages() +
                "\nVie de l'ennemi :  "+ target.getHealth());
    }

    /**
     * Prendre des dégâts
     * @param damage les dégâts à prendre
     */
    public void takeDamage(double damage) {
        this.setHealth(this.getHealth()- damage);
        if (this.getHealth() <= 0) {
            this.setDead(true);
        }
    }

    /**
     * Appliques si la défense a déjà attaqué ou non
     * @param attacked true si la défense a déjà attaqué, false sinon
     */
    public void setAttacked(boolean attacked) {
        this.attacked = attacked;
    }

    /**
     * Retourne si la défense a déjà attaqué ou non
     * @return true si la défense a déjà attaqué, false sinon
     */
    public boolean isAttacked() {
        return this.attacked;
    }

}
