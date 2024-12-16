public abstract class Entity {

    private double x = 0;
    private double y = 0;
    private String type = "";

    public Entity(double x, double y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
