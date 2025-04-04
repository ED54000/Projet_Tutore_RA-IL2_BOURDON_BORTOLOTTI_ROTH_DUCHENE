package entites.enemies;

import entites.Entity;
import javafx.scene.image.Image;
import laby.ModeleLabyrinth;
import moteur.MoteurJeu;
import moteur.SimpleMode;
import steering_astar.Astar.Astar;
import steering_astar.Steering.AvoidBehavior;
import steering_astar.Steering.Behavior;
import steering_astar.Steering.PathfollowingBehavior;
import steering_astar.Steering.Vector2D;

import java.util.ArrayList;
import java.util.List;

public abstract class Ennemy extends Entity {

    private double speed;
    private int distanceToArrival;  //distanceToArrival sera calculé par A* dans le jeu
    private int distanceStartToArrival;
    private double distanceTraveled;
    private String killerType;
    private static int timeSpawn = 0;
    private String behaviorString;
    private boolean isArrived;
    private long survivalTime = 0;
    private Vector2D positionReel;
    private Vector2D velocity;
    private final double healthBase;
    private List<Behavior> listBehaviors = new ArrayList<>();
    private boolean isHeal;
    private Image spriteHeal = null;

    public Ennemy(Vector2D position, double health, double speed, double damages, double attackSpeed, double range, int distanceToArrival, String name, String sprite, String behavior) {
        super(position, damages, range, sprite, health, name, attackSpeed);
        this.speed = speed;
        if (!ModeleLabyrinth.getSimulation()) {
            this.spriteHeal = new Image(sprite+"_heal.png");
        }
        this.healthBase = health;
        //this.positionReel = position.divide(ModeleLabyrinth.getTailleCase());
        this.distanceToArrival = distanceToArrival;
        this.distanceStartToArrival = distanceToArrival;
        this.distanceTraveled = 0;
        this.killerType = null;
        this.isArrived = false;
        this.behaviorString = behavior;
        this.listBehaviors.add(new AvoidBehavior(new Vector2D(0, 0)));
        this.velocity = new Vector2D(0, 0);
        timeSpawn++;
    }

    public ArrayList<Vector2D> calculerChemin(char[][] grid, Vector2D startCoordinate) {
        Astar astar = Astar.getAStar();
        return astar.aStarSearch(grid, grid.length, grid[0].length,
                startCoordinate,
                new Vector2D(ModeleLabyrinth.getYArrival(), ModeleLabyrinth.getXArrival()),
                this.getBehaviorString(),
                false);
    }

    public void healDamage(Ennemy target, double heal, double speedTime) {
        // Si le temps écoulé depuis le dernier heal est supérieur ou égal à l'attackSpeed
        if (this.getLastAttackCount() >= this.getAttackSpeed() * speedTime) {
            if (this.getAttackSpeed() <= 0) {
                this.setAttackSpeed(1);
            }

            // On met à jour le temps du dernier heal
            this.setLastAttackCount(0);
            if (target.health + Math.abs(heal) <= target.healthBase) {
                // On heal
                target.health += Math.abs(heal);
                ModeleLabyrinth.setLogs(target.getName());
                // Si le jeu est en mode simple et pas en simulation
                if(SimpleMode.getSimpleMode() && !ModeleLabyrinth.getSimulationEvolution()) {
                    // On met à jour le sprite de l'ennemi (sa vie)
                    ModeleLabyrinth.updateSprite(target);
                }else {
                    target.setIsHeal(true);
                }
            }
        }
    }

    /***
     * methode definissant la nouvelle position de l'agent grace a sa velocite, calculee selon le
     * comportement de l'agent
     */
    public void update() {
        Vector2D totalForce = new Vector2D(0, 0);

        for (Behavior behavior : listBehaviors) {
            Vector2D steeringForce = behavior.calculateForce(this).scale(behavior.getWeight());
            totalForce = totalForce.add(steeringForce);
        }

        double distance = velocity.magnitude();
        distanceTraveled += distance;

        velocity = velocity.add(totalForce).normalize().scale(speed);

        position = position.add(velocity);
        positionReel = position.divide(ModeleLabyrinth.getTailleCase());
    }

    public int getDistanceToArrival() {
        return distanceToArrival;
    }

    public boolean getIsArrived() {
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

    public void setArrived(boolean res) {
        this.isArrived = res;
    }

    public Vector2D getVelocity() { return velocity; }

    public List<Behavior> getListBehavior() {
        return listBehaviors;
    }

    public void setBehavior(Behavior behavior) {
        for (Behavior b : listBehaviors) {
            if (b instanceof PathfollowingBehavior && behavior instanceof PathfollowingBehavior) {
                listBehaviors.remove(b);
                break;
            }
        }
        this.listBehaviors.addFirst(behavior);
    }

    public String getBehaviorString() {
        return behaviorString;
    }

    public void setBehaviorString(String behavior) {
        this.behaviorString = behavior;
    }

    public void setDistanceToArrival(ArrayList<Vector2D> vector2DS) {
        this.distanceToArrival = vector2DS.size();
    }

    public void setDistanceStartToArrival(ArrayList<Vector2D> vector2DS) {
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

    public void setToStart(ModeleLabyrinth modeleLabyrinth) {
        double XstartRandom = Math.random() * 1.5;
        double YstartRandom = Math.random() * 1.5;
        this.setPositionReel(new Vector2D(modeleLabyrinth.getXstart() + XstartRandom, modeleLabyrinth.getYstart() + YstartRandom));
        this.setPosition(new Vector2D(modeleLabyrinth.getXstartRender() + XstartRandom, modeleLabyrinth.getYstartRender() + YstartRandom));
    }

    public boolean getIsHeal() {
        return isHeal;
    }

    public void setIsHeal(boolean heal) {
        isHeal = heal;
    }

    public Image getSpriteHeal() {
        return spriteHeal;
    }

    public double getDistanceTraveled() {
        return this.distanceTraveled;
    }
}

