package entites.enemies;

import entites.Entity;
import laby.ModeleLabyrinth;
import steering_astar.Steering.AvoidBehavior;
import steering_astar.Steering.Behavior;
import steering_astar.Steering.Vector2D;

import java.util.ArrayList;

public abstract class Ennemy extends Entity {

    private double speed;
    private int distanceToArrival;  //distanceToArrival sera calculé par A* dans le jeu
    private int distanceStartToArrival;
    private String killerType;
    private static int timeSpawn = 0;
    private Behavior behaviorPath;
    private String behavior;
    private boolean isArrived;
    private long survivalTime;
    private Vector2D positionReel;
    private Vector2D velocity;
    private final double healthBase;

    public Ennemy(Vector2D position, double health, double speed, double damages, double attackSpeed, double range, int distanceToArrival, String name, String sprite, String behavior) {
        super(position, damages, range, sprite, health, name, attackSpeed);
        this.speed = speed;
        this.healthBase = health;
        this.positionReel = position.divide(ModeleLabyrinth.getTailleCase());
        this.distanceToArrival = distanceToArrival;
        this.distanceStartToArrival = distanceToArrival;
        this.killerType = null;
        this.isArrived = false;
        this.behaviorPath = null;
        this.behavior = behavior;
        this.velocity = new Vector2D(0, 0);
        timeSpawn++;
    }

    public void healDamage(Ennemy target, double heal, double speedTime){
        // On récupère le temps actuel en millisecondes

        // Si le temps écoulé depuis le dernier heal est supérieur ou égal à l'attackSpeed
        if(this.getLastAttackCount() >=  this.getAttackSpeed() * speedTime) {
            if (this.getAttackSpeed() <= 0) {
                this.setAttackSpeed(1);
            }

            // On met à jour le temps du dernier heal
            this.setLastAttackCount(0);
            if (target.health + Math.abs(heal) <= target.healthBase) {
                // On heal
                target.health += Math.abs(heal);
                System.out.println("Soin de " + this.getName() + " sur " + target.getName());
                System.out.println("Montant de soin : " + Math.abs(heal));
                System.out.println("Vie de " + target.getName() + " : " + target.getHealth());
                System.out.println("Vie de base de " + target.getName() + " : " + target.healthBase);
                System.out.println("=====================================");
            }
        }
    }

    /***
     * methode definissant la nouvelle position de l'agent grace a sa velocite, calculee selon le
     * comportement de l'agent
     */
    public void update() {
        //System.out.println(behaviorPath);
        if (behaviorPath != null) {
            Vector2D steeringForce = behaviorPath.calculateForce(this);
            velocity = (velocity.add(steeringForce)).normalize().scale(speed);
            if (position.add(velocity.scale(AvoidBehavior.getMAX_SEE_AHEAD())).isObstacle()){
                AvoidBehavior avoid = new AvoidBehavior(position.getClosestCaseCenter());
                Vector2D avoidanceForce = avoid.calculateForce(this);
                velocity = (velocity.add(avoidanceForce)).normalize();
            }
            position = position.add(velocity);
            positionReel = position.divide(ModeleLabyrinth.getTailleCase());
        }
    }

    public int getDistanceToArrival() {
        return distanceToArrival;
    }

    public boolean isItArrived() {
        return isArrived;
    }

    public long getSurvivalTime() {
        return survivalTime;
    }

    public double getSpeed() {
        return speed;
    }

    public double getDamages() {
        return super.getDamages();
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setDamages(double damages) {
        super.setDamages(damages);
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
   //     System.out.println(vector2DS.size());
        this.distanceStartToArrival = vector2DS.size();
    }

    public int getDistanceStartToArrival() {
        return distanceStartToArrival;
    }

    public Vector2D getPositionReel() {
        return positionReel;
    }

    public void setPositionReel(Vector2D positionReel) {
        this.positionReel = positionReel;
    }
    public void setSurvivalTime(long survivalTime) {
        this.survivalTime = survivalTime;
    }

    public void setToStart(ModeleLabyrinth modeleLabyrinth){
        double XstartRandom =  Math.random()*1.5;
        double YstartRandom =  Math.random()*1.5;
        this.setPositionReel(new Vector2D(modeleLabyrinth.getXstart() + XstartRandom, modeleLabyrinth.getYstart() + YstartRandom));
        this.setPosition(new Vector2D(modeleLabyrinth.getXstartRender() + XstartRandom, modeleLabyrinth.getYstartRender() + YstartRandom));
    }
}

