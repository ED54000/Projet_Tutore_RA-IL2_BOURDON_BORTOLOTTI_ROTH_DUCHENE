package entites.defenses;

import entites.enemies.Ennemy;

public abstract class ActiveDefense extends Defense {

    private final long attackSpeed;

    private double health;

    // L'ennemi sur lequel la défense est focus
    private Ennemy target;

    // Indique si la méthode attack est en cours d'exécution
    private boolean attackInProgress;

    private long lastAttackTime;

    public ActiveDefense(double x, double y, int health, int damage, double range, long attackSpeed) {
        super(x, y, damage, range);
        this.attackSpeed = attackSpeed;
        this.health = health;
        this.target = null;
        this.attackInProgress = false;
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

        /*// Si un autre Thread est déjà en train d'attaquer
        if (attackInProgress) {
            // On ne fait rien
            return;
        }

        synchronized (this) { // Synchronisation pour éviter les problèmes de concurrence sur "attackInProgress"
            if(attackInProgress) { // double vérification, obligatoire
                return;
            }
            // Ici on est sûr que l'attaque n'est pas en cours
            attackInProgress = true;
        }

        try {
            // On attaque l'ennemi
            target.takeDamage(this.getDamages()*getBonus(getType(), target.getType()) + this.getDamages());
            // Plus l'attackSpeed est grand, plus la défense attaque vite (attends peu)
            Thread.sleep((long) (1000 / attackSpeed));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            synchronized (this) {
                attackInProgress = false; // On libère l'accès à la méthode
            }
        }*/
    }

    public void takeDamage(double damage) {
        health -= damage;
    }

    public Ennemy getTarget() {
        return target;
    }

    public boolean getAttackInProgress() {
        return attackInProgress;
    }

    public void setTarget(Ennemy target) {
        this.target = target;
    }
}
