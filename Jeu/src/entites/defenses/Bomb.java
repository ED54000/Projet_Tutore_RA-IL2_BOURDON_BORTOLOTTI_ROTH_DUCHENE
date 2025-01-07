package entites.defenses;

import entites.enemies.Ennemy;

public class Bomb extends PassiveDefense{
    public Bomb(double x, double y, String name) {
        super(x, y, 100, 1, 1000,"/bomb.png", name);
    }
}
