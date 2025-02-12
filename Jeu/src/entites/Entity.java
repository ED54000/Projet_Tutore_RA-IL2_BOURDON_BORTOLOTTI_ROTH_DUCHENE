package entites;

import javafx.scene.image.Image;
import entites.enemies.Ennemy;
import laby.ModeleLabyrinth;
import steering_astar.Steering.Vector2D;

public abstract class Entity {

    protected Vector2D position;
    private String type;
    private double damages;
    private double range;
    private Image sprite = null;
    protected double health;
    private boolean isDead = false;
    private String name;
    private double attackSpeed;
    private long lastAttackCount = 0;

    public Entity(Vector2D position, double damages, double range, String sprite, double health, String name, double attackSpeed) {
        this.position = position;
        this.damages = damages;
        this.range = range;
        if(!ModeleLabyrinth.getSimulation()) {
            this.sprite = new Image(sprite);
        }
        this.health = health;
        this.name = name;
        this.attackSpeed = attackSpeed;

        //génère un type aléatoire
        int randomNumber = (int) (Math.random() * 3) + 1;
        switch (randomNumber) {
            case 1:
                this.type = "Fire";
                break;
            case 2:
                this.type = "Water";
                break;
            case 3:
                this.type = "Plant";
                break;
        }
    }

    //retourne le bonus de dégâts en fonction des types
    public double getBonus(String AttackerType, String TargetType) {
        if (AttackerType != null && TargetType != null) {
            switch (AttackerType) {
                case "Fire":
                    if (TargetType.equals("Plant")) {
                        return 1.3;
                    } else if (TargetType.equals("Water")) {
                        return 0.7;
                    } else {
                        return 1;
                    }
                case "Water":
                    if (TargetType.equals("Fire")) {
                        return 1.3;
                    } else if (TargetType.equals("Plant")) {
                        return 0.7;
                    } else {
                        return 1;
                    }
                case "Plant":
                    if (TargetType.equals("Water")) {
                        return 1.3;
                    } else if (TargetType.equals("Fire")) {
                        return 0.7;
                    } else {
                        return 1;
                    }
            }
            return 0;
        }
        return 1;
    }

    /**
     * Prendre des dégâts
     * @param damage les dégâts à prendre
     */
    public void takeDamage(double damage) {
        health -= Math.abs(damage);
        if (this.health <= 0) {
            isDead = true;
        }
    }

    /**
     * Vérifie si une entite est dans la portée de l'entite courante
     *
     * @param target l'entite à vérifier
     * @return true si l'entite à vérifier est dans la portée de l'entite courante, false sinon
     */
    public boolean isInRange(Entity target) {

        double targetX,targetY,entityX,entityY;

        if (this instanceof Ennemy) {
            entityX = ((Ennemy) this).getPositionReel().getX();
            entityY = ((Ennemy) this).getPositionReel().getY();
            if (target instanceof Ennemy) {
                targetX = ((Ennemy) target).getPositionReel().getX();
                targetY = ((Ennemy) target).getPositionReel().getY();
            } else {
                targetX = target.getPosition().getX();
                targetY = target.getPosition().getY();
            }
        } else {
            targetX = ((Ennemy) target).getPositionReel().getX();
            targetY = ((Ennemy) target).getPositionReel().getY();
            entityX = this.getPosition().getX();
            entityY = this.getPosition().getY();
        }

        // Calculer la distance au carré entre les positions
        double deltaX = targetX - entityX;
        double deltaY = targetY - entityY;
        double distanceSquared = deltaX * deltaX + deltaY * deltaY;

        double rangeInPixelsSquared = this.getRange() * this.getRange();

        // Vérification si l'ennemi est dans la portée
        return distanceSquared <= rangeInPixelsSquared;
    }

    public synchronized void attack(Entity target, double speedTime){
        // On récupère le temps actuel en millisecondes
        // Si le temps écoulé depuis la dernière attaque est supérieur ou égal à l'attackSpeed
        if(lastAttackCount >= attackSpeed * speedTime ) {
            // On met à jour le temps de la dernière attaque
            lastAttackCount = 0;
            // On attaque la défense
            System.out.println("=====================================");
            target.takeDamage(this.getDamages()*getBonus(getType(), target.getType()) + this.getDamages());
            System.out.println("Attaque de " + this.getName() +" de type : "+this.getType()+ " sur " + target.getName()+" de type : "+target.getType());
            System.out.println("Dégâts infligés : " + (this.getDamages()*getBonus(getType(), target.getType()) + this.getDamages()));
            System.out.println("Vie de " + target.getName() + " : " + target.getHealth());
            System.out.println("Target mort : "+ target.getIsDead());
            System.out.println("=====================================");
        }
        // Sinon, on ne fait rien
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public double getDamages() {
        return damages;
    }

    protected void setDamages(double damages) {
        this.damages = damages;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public Image getImage() { return sprite; }

    public double getHealth() {
        return this.health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public boolean getIsDead() {
        return this.isDead;
    }

    public void setIsDead(boolean b) {
        this.isDead = b;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(double attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public long getLastAttackCount() {
        return lastAttackCount;
    }

    public void setLastAttackCount(long lastAttackCount) {
        this.lastAttackCount = lastAttackCount;
    }

    public void setSprite(Object o) {
        this.sprite = (Image) o;
    }
}