package entites.defenses;

import entites.enemies.Ennemy;

public class Canon extends ActiveDefense {
    public Canon(double x, double y) {
        super(x, y, 100, 10, 2.5, 20, "/canon.png");
    }
}
