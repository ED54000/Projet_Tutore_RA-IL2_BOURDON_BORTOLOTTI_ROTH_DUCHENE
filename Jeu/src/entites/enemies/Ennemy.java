package entites.enemies;

import entites.Entity;
import entites.defenses.Defense;
import javafx.scene.image.Image;
import steering_astar.Steering.Behavior;
import steering_astar.Steering.Vector2D;

import java.util.ArrayList;

public abstract class Ennemy extends Entity {

    private double speed;
    private double attackSpeed;
    private long lastAttackTime;
    private int distanceToArrival;  //distanceToArrival sera calculé par A* dans le jeu
    private int distanceStartToArrival;
    private String killerType;
    private double health;
    private static int timeSpawn = 0;
    private Behavior behaviorPath;
    private String behavior;
    private boolean isArrived;
    private int survivalTime;
    private String name;
    //private String sprite;
    private Vector2D velocity;

    public Ennemy(Vector2D position, double health,double speed, double damages, double attackSpeed, double range, int distanceToArrival, String name) {
        super(position, damages, range);
        this.speed = speed;
        this.attackSpeed = attackSpeed;
        this.distanceToArrival = distanceToArrival;
        this.distanceStartToArrival = distanceToArrival;
        this.killerType = null;
        this.health = health;
        this.isArrived = false;
        this.behaviorPath = null;
        this.behavior = "Normal";
        this.velocity = new Vector2D(0, 0);
        this.name = name;
        //this.sprite = sprite;
        this.lastAttackTime = 0;
        timeSpawn++;
    }

    public void takeDamage(double damage) {
        health -= damage;
    }

    public void healDamage(Ennemy target, double heal){
        health += heal;
        System.out.println("Soin de " + this + " sur " + target);
        System.out.println("Montant de soin : " + heal);
        System.out.println("Vie de " + target + " : " + target.getHealth());
        System.out.println("=====================================");
    }

    public void attack(Defense target){
        // On récupère le temps actuel en millisecondes
        long currentTime = System.currentTimeMillis();

        // Si le temps écoulé depuis la dernière attaque est supérieur ou égal à l'attackSpeed
        if(currentTime - lastAttackTime >= 1000 / attackSpeed) {
            // On met à jour le temps de la dernière attaque
            lastAttackTime = currentTime;
            // On attaque la défense
            target.takeDamage(this.getDamages()*getBonus(getType(), target.getType()) + this.getDamages());
            System.out.println("Attaque de " + this +"de type : "+this.getType()+ " sur " + target+" de type : "+target.getType());
            System.out.println("Dégâts infligés : " + (this.getDamages()*getBonus(getType(), target.getType()) + this.getDamages()));
            System.out.println("Vie de la défense : " + target.getHealth());
            System.out.println("=====================================");
        }
        // Sinon, on ne fait rien
    }

    /***
     * methode definissant la nouvelle position de l'agent grace a sa velocite, calculee selon le
     * comportement de l'agent
     */
    public void update() {
        //System.out.println(behaviorPath);
        if (behaviorPath != null) {
            Vector2D steeringForce = behaviorPath.calculateForce(this);
            velocity = velocity.add(steeringForce).normalize().scale(speed);
            position = position.add(velocity);
        }
    }

    public boolean isDead() {
        return health <= 0;
    }

    public int getDistanceToArrival() {
        return distanceToArrival;
    }

    public boolean isItArrived() {
        return isArrived;
    }

    public int getSurvivalTime() {
        return survivalTime;
    }

    public double getHealth() {
        return health;
    }

    public double getSpeed() {
        return speed;
    }

    public double getDamages() {
        return super.getDamages();
    }

    public double getAttackSpeed() {
        return attackSpeed;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setDamages(double damages) {
        super.setDamages(damages);
    }

    public void setAttackSpeed(double attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public void setRange(double range) {
        super.setRange(range);
    }

    public void setType(String type) {
        super.setType(type);
    }

    public String getKillerType() {
        return killerType;
    }

    public void setKillerType(String killerType) {
        this.killerType = killerType;
    }

    public String getName() {
        return name;
    }

    public boolean isArrived() {
        return isArrived;
    }

    public void setArrived(boolean res) {
        this.isArrived = res;
    }

    public Vector2D getVelocity() { return velocity; }

    public double getMaxSpeed() { return speed; }

    public Behavior getBehaviorPath() { return behaviorPath; }

    public void setBehaviorPath(Behavior behaviorPath) {
        this.behaviorPath = behaviorPath;
    }

    public String getBehavior() {
        return behavior;
    }

    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }

    public void setDistanceToArrival(ArrayList<Vector2D> vector2DS) {
        this.distanceToArrival = vector2DS.size();
    }

    public void setDistanceStartToArrival(ArrayList<Vector2D> vector2DS) {
        this.distanceStartToArrival = vector2DS.size();
    }

    public long getLastAttackTime(){
        return this.lastAttackTime;
    }

    public void setLastAttackTime(long time){
        this.lastAttackTime = time;
    }

    public int getDistanceStartToArrival() {
        return distanceStartToArrival;
    }

    /*
    public String getImage() {
        return sprite;
    }

     */
}

