package entites.defenses;

import entites.enemies.Ennemy;

public class Canon extends ActiveDefense {
    public Canon(double x, double y, String name) {
        super(x, y, 300, 20, 2.5, 500, "/canon.png", name);
    }
}
