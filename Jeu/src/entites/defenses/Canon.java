package entites.defenses;

import entites.enemies.Ennemy;

public class Canon extends ActiveDefense {
    public Canon(double x, double y, String name) {
        super(x, y, 300, 30, 2.5, 5, "/canon.png", name);
    }
}
