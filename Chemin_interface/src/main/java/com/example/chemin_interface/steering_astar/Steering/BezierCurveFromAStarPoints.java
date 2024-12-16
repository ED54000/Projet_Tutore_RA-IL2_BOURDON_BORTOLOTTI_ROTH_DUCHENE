package com.example.chemin_interface.steering_astar.Steering;

import java.util.ArrayList;

public class BezierCurveFromAStarPoints {
    public static ArrayList<Vector2D> getBezierCurve(ArrayList<Vector2D> inputPoints) {
        ArrayList<Vector2D> outputPoints = new ArrayList<>();

        if (inputPoints.size() < 2) {
            return outputPoints; // Pas assez de points pour tracer une courbe.
        }

        // Points de contrôle pour assurer des transitions fluides
        ArrayList<Vector2D> controlPoints = generateControlPoints(inputPoints);

        // Générer des points sur la courbe avec interpolation Bézier
        for (int i = 0; i + 3 < controlPoints.size(); i += 3) {
            Vector2D p0 = controlPoints.get(i);
            Vector2D p1 = controlPoints.get(i + 1);
            Vector2D p2 = controlPoints.get(i + 2);
            Vector2D p3 = controlPoints.get(i + 3);

            outputPoints.addAll(generateBezierSegment(p0, p1, p2, p3));
        }

        if (controlPoints.size() % 3 != 1) {
            // Ajouter un dernier segment direct entre les deux derniers points
            Vector2D p0 = controlPoints.get(controlPoints.size() - 2);
            Vector2D p3 = controlPoints.get(controlPoints.size() - 1);

            outputPoints.addAll(generateBezierSegment(p0, p0, p0, p3));
        }

        return outputPoints;
    }

    // Génère les points de contrôle intermédiaires pour adoucir les transitions
    private static ArrayList<Vector2D> generateControlPoints(ArrayList<Vector2D> inputPoints) {
        ArrayList<Vector2D> controlPoints = new ArrayList<>();
        controlPoints.add(inputPoints.get(0)); // Premier point inchangé

        for (int i = 1; i < inputPoints.size() - 1; i++) {
            Vector2D prev = inputPoints.get(i - 1);
            Vector2D curr = inputPoints.get(i);
            Vector2D next = inputPoints.get(i + 1);

            // Ajustement des points de contrôle pour des virages fluides
            Vector2D controlBefore = curr.add(prev).scale(0.5);
            Vector2D controlAfter = curr.add(next).scale(0.5);

            controlPoints.add(controlBefore);
            controlPoints.add(curr); // Point principal reste dans la liste
            controlPoints.add(controlAfter);
        }

        controlPoints.add(inputPoints.get(inputPoints.size() - 1)); // Dernier point inchangé
        return controlPoints;
    }

    // Génère des points le long d'un segment Bézier (courbe fluide et uniforme)
    private static ArrayList<Vector2D> generateBezierSegment(Vector2D p0, Vector2D p1, Vector2D p2, Vector2D p3) {
        ArrayList<Vector2D> segmentPoints = new ArrayList<>();

        // Étape fixe pour un déplacement uniforme
        final double step = 0.05; // Ajustez pour plus ou moins de détails
        for (double t = 0.0; t <= 1.0; t += step) {
            double x = Math.pow(1 - t, 3) * p0.x
                    + 3 * Math.pow(1 - t, 2) * t * p1.x
                    + 3 * (1 - t) * Math.pow(t, 2) * p2.x
                    + Math.pow(t, 3) * p3.x;

            double y = Math.pow(1 - t, 3) * p0.y
                    + 3 * Math.pow(1 - t, 2) * t * p1.y
                    + 3 * (1 - t) * Math.pow(t, 2) * p2.y
                    + Math.pow(t, 3) * p3.y;

            segmentPoints.add(new Vector2D(x, y));
        }

        // Ajouter manuellement le dernier point si nécessaire
        if (!segmentPoints.contains(p3)) {
            segmentPoints.add(p3);
        }

        return segmentPoints;
    }

}