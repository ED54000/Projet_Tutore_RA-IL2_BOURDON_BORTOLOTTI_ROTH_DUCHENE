import java.util.Random;

public class Ennemy extends Entity {

    private double health;
    private double speed;
    private double damages;
    private double attackSpeed;
    private double range;
    private double distanceToArrival;
    private String killerType;
    private String behavior;
    private int tempsSurvie;
    private boolean isArrived = false;


    public Ennemy(double x, double y, String type, int health, double speed, int damages, double attackSpeed, double range, String behavior) {
        super(x, y, type);
        this.health = health;
        this.speed = speed;
        this.damages = damages;
        this.attackSpeed = attackSpeed;
        this.range = range;
        this.distanceToArrival = 0;
        // On tire au sort le killerType
        Random random = new Random();
        int randomint = random.nextInt(3);
        if(randomint == 0)
            this.killerType = "Fire";
        else if(randomint == 1)
            this.killerType = "Water";
        else
            this.killerType = "Plant";
        this.behavior = behavior;
        double randomDouble = Math.random()*100;
        this.distanceToArrival = (int) randomDouble;
        randomDouble = Math.random()*100;
        this.tempsSurvie = (int) randomDouble;
        randomDouble = Math.random()*2;
        randomint = (int) randomDouble;
        if(randomint == 0)
            this.isArrived = false;
        else
            this.isArrived = true;
    }

    public double getSpeed() {
        return speed;
    }

    public double getDamages() {
        return damages;
    }

    public double getAttackSpeed() {
        return attackSpeed;
    }

    public double getRange() {
        return range;
    }



    public double getDistanceToArrival() {
        return distanceToArrival;
    }

    public double getTempsSurvie() {
        return tempsSurvie;
    }

    public String toString(){
        return "Ennemy: "+this.getType()+", vie : "+this.health+", vitesse : "+this.speed+", dégats : "+this.damages+", vitesse d'attaque : "+this.attackSpeed+", portée : "+this.range+" distanceToArrival : "+this.distanceToArrival+" temps survie : "+this.tempsSurvie+" comportement : "+this.behavior+" est arrivé : "+this.isArrived+"\n";
    }

    public boolean isArrived() {
        return isArrived;
    }

    public double getHealth() {
        return health;
    }

    public String getBehavior() {
        return behavior;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setDamages(double damages) {
        this.damages = damages;
    }

    public void setAttackSpeed(double attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public void setType(String type) {
        super.setType(type);
    }

    public String getKillerType() {
        return killerType;
    }
}
