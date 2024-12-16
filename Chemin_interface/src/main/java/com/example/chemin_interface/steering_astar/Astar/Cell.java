package com.example.chemin_interface.steering_astar.Astar;

import com.example.chemin_interface.steering_astar.Steering.Vector2D;

public class Cell {
    public Vector2D parent;

    public double f, g, h;
    Cell()
    {
        parent = new Vector2D(-1, -1);
        f = -1;
        g = -1;
        h = -1;
    }

    public Cell(Vector2D parent, double f, double g, double h) {
        this.parent = parent;
        this.f = f;
        this.g = g;
        this.h = h;
    }

}