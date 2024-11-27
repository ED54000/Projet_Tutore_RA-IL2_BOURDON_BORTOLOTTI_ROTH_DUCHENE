package com.example.steering_astar.Steering;

import java.util.ArrayList;
;

public class BezierCurveFromAStarPoints {
    public static ArrayList<Vector2D> getBezierCurve(ArrayList<Vector2D> inputPoints) {
        ArrayList<Vector2D> outputPoints = new ArrayList<>();

        for (int i = 0; i + 3 < inputPoints.size(); i += 3) {
            Vector2D p0 = inputPoints.get(i);
            Vector2D p1 = inputPoints.get(i + 1);
            Vector2D p2 = inputPoints.get(i + 2);
            Vector2D p3 = inputPoints.get(i + 3);


            for (double t = 0.0; t <= 1.0; t += 0.01) {
                double x = Math.pow(1 - t, 3) * p0.x
                        + 3 * Math.pow(1 - t, 2) * t * p1.x
                        + 3 * (1 - t) * Math.pow(t, 2) * p2.x
                        + Math.pow(t, 3) * p3.x;

                double y = Math.pow(1 - t, 3) * p0.y
                        + 3 * Math.pow(1 - t, 2) * t * p1.y
                        + 3 * (1 - t) * Math.pow(t, 2) * p2.y
                        + Math.pow(t, 3) * p3.y;

                outputPoints.add(new Vector2D(x, y));
            }
        }

        return outputPoints;
    }

}
