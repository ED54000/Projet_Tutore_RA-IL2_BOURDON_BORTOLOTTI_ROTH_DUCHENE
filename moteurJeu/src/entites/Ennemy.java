package entites;

public class Ennemy extends Entity {

    private int health;
    private double speed;
    private int damage;
    private double attackSpeed;
    private double range;
    private double distanceToArrival;
    private String killerType;

    public Ennemy(double x, double y, String type, int health, double speed, int damage, double attackSpeed, double range, double distanceToArrival, String killerType) {
        super(x, y, type);
        this.health = health;
        this.speed = speed;
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.range = range;
        this.distanceToArrival = distanceToArrival;
        this.killerType = killerType;
    }
    

}
