package entites.defenses;

import entites.enemies.Ennemy;

public abstract class ActiveDefense extends Defense {

    private final long attackSpeed;

    private double health;

    // L'ennemi sur lequel la défense est focus
    private Ennemy target;
    private long lastAttackTime;

    public ActiveDefense(double x, double y, double health, int damage, double range, long attackSpeed) {
        super(x, y, damage, range, health);
        this.attackSpeed = attackSpeed;
        this.health = health;
        this.target = null;
        this.lastAttackTime = 0;
    }

    /**
     * Attaque un ennemi
     * @param target l'ennemi à attaquer
     */
    @Override
    public void attack(Ennemy target) {
        // On récupère le temps actuel en millisecondes
        long currentTime = System.currentTimeMillis();

        // Si le temps écoulé depuis la dernière attaque est supérieur ou égal à l'attackSpeed
        if(currentTime - lastAttackTime >= 1000 / attackSpeed) {
            // On met à jour le temps de la dernière attaque
            lastAttackTime = currentTime;
            // On attaque l'ennemi
            target.takeDamage(this.getDamages()*getBonus(getType(), target.getType()) + this.getDamages());
            System.out.println("Attaque de " + this +"de type : "+this.getType()+ " sur " + target+" de type : "+target.getType());
            System.out.println("Dégâts infligés : " + (this.getDamages()*getBonus(getType(), target.getType()) + this.getDamages()));
            System.out.println("Vie de l'ennemi : " + target.getHealth());
            System.out.println("=====================================");
        }
        else {
            // Sinon, on ne fait rien
            return;
        }
    }

    public Ennemy getTarget() {
        return target;
    }

    public void setTarget(Ennemy target) {
        this.target = target;
    }
}
