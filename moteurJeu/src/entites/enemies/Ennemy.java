package entites.enemies;

import entites.Entity;
import entites.defenses.Defense;

public abstract class Ennemy extends Entity {

    private double speed;
    private double attackSpeed;
    private int distanceToArrival;  //distanceToArrival sera calculé par A* dans le jeu
    private int distanceStartToArrival;
    private String killerType;
    private double health;
    private static int timeSpawn = 0;
    private String behavior;
    private boolean isArrived;
    private int survivalTime;
    private String name;

    public Ennemy(double x, double y, double health,double speed, double damages, double attackSpeed, double range, int distanceToArrival, String name) {
        super(x, y, damages, range);
        this.speed = speed;
        this.attackSpeed = attackSpeed;
        this.distanceToArrival = distanceToArrival;
        this.distanceStartToArrival = distanceToArrival;
        this.killerType = null;
        this.health = health;
        this.isArrived = false;
        this.behavior = "Normal";

        this.name = name;
        timeSpawn++;
    }

    public void takeDamage(double damage) {
        health -= damage;
    }

    public void attack(Defense target) {
        if (target != null) {
            target.takeDamage(getDamages()*getBonus(getType(), target.getType()));
        }
    }

    public void move(double secondes) {
        //attendre une seconde
        this.setX(this.getX() + this.speed);
        this.distanceToArrival -= this.speed;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public int getDistanceToArrival() {
        return distanceToArrival;
    }

    public int getDistanceStartToArrival() {
        return distanceStartToArrival;
    }

    public int getTimeSpawn() {
        return timeSpawn;
    }

    public void setTimeSpawn(int i) {
        timeSpawn = i;
    }

    public String getBehavior() {
        return behavior;
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
}

