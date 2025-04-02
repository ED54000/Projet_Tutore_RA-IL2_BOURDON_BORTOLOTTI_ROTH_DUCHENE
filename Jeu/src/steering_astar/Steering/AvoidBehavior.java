package steering_astar.Steering;

import entites.enemies.Ennemy;
import laby.ModeleLabyrinth;

public class AvoidBehavior extends Behavior {

    private static final double MAX_SEE_AHEAD = 1.0;
    private static final double BASE_AVOID_WEIGHT = 2.0;
    private static final int NUM_FEELERS = 5;

    public AvoidBehavior(Vector2D target) {
        this.setTarget(target);
        if (ModeleLabyrinth.getLabyrinth().getUseAstar()) {
            this.setWeight(BASE_AVOID_WEIGHT / 2);
        } else {
            this.setWeight(BASE_AVOID_WEIGHT);
        }
    }

    @Override
    public Vector2D calculateForce(Ennemy ennemy) {
        Vector2D position = ennemy.getPositionReel();
        Vector2D velocity = ennemy.getVelocity();

        if (velocity.magnitude() < 0.0001) {
            return new Vector2D(0, 0);
        }

        Vector2D[] feelers = createFeelers(position, velocity);
        Vector2D closestPoint = null;
        double closestDist = Double.MAX_VALUE;

        for (Vector2D feeler : feelers) {
            if (isObstacleAtPoint(feeler)) {
                double dist = calculateDistance(position, feeler);
                if (dist < closestDist) {
                    closestDist = dist;
                    closestPoint = feeler;
                }
            }
        }

        if (closestPoint == null) {
            return new Vector2D(0, 0);
        }

        return calculateAvoidanceForce(position, closestPoint, velocity);
    }

    private double calculateDistance(Vector2D a, Vector2D b) {
        double dx = b.getX() - a.getX();
        double dy = b.getY() - a.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    private Vector2D[] createFeelers(Vector2D position, Vector2D velocity) {
        Vector2D[] feelers = new Vector2D[NUM_FEELERS];
        Vector2D normalized = velocity.normalize();

        feelers[0] = position.add(normalized.scale(MAX_SEE_AHEAD));

        feelers[1] = position.add(rotateVector(normalized, Math.PI / 6).scale(MAX_SEE_AHEAD * 0.75));
        feelers[2] = position.add(rotateVector(normalized, -Math.PI / 6).scale(MAX_SEE_AHEAD * 0.75));


        feelers[3] = position.add(rotateVector(normalized, Math.PI / 3).scale(MAX_SEE_AHEAD * 0.25));
        feelers[4] = position.add(rotateVector(normalized, -Math.PI / 3).scale(MAX_SEE_AHEAD * 0.25));

        return feelers;
    }

    // Rotation d'un vecteur d'un angle donné
    private Vector2D rotateVector(Vector2D v, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        return new Vector2D(
                v.getX() * cos - v.getY() * sin,
                v.getX() * sin + v.getY() * cos
        );
    }


    private boolean isObstacleAtPoint(Vector2D point) {
        if (point == null) {
            return false;
        }

        // Utilisation de la méthode isObstacle() sur le point

        return point.isObstacle();
    }


    // Calcul de la force d'évitement combinant éloignement de l'obstacle et suivi du mur
    private Vector2D calculateAvoidanceForce(Vector2D position, Vector2D obstaclePoint, Vector2D velocity) {
        Vector2D away = position.subtract(obstaclePoint).normalize();
        Vector2D currentDirection = velocity.normalize();

        // Calcul des deux tangentes possibles (haut et bas)
        Vector2D tangentUp = new Vector2D(-away.getY(), away.getX()).normalize();
        Vector2D tangentDown = new Vector2D(away.getY(), -away.getX()).normalize();

        // Calcul du produit scalaire
        double dotUp = tangentUp.getX() * currentDirection.getX() + tangentUp.getY() * currentDirection.getY();
        double dotDown = tangentDown.getX() * currentDirection.getX() + tangentDown.getY() * currentDirection.getY();

        // Choisir la tangente qui s'aligne le mieux avec la direction actuelle
        Vector2D chosenTangent = (dotUp > dotDown) ? tangentUp : tangentDown;

        // Calcul de l'angle avec le produit scalaire manuel
        double dotProduct = currentDirection.getX() * away.getX() + currentDirection.getY() * away.getY();
        double angle = Math.abs(Math.acos(Math.max(-1, Math.min(1, dotProduct))));

        double weightAway = 0.4 * (1 - Math.cos(angle));
        double weightTangent = 0.6;

        return away.scale(weightAway).add(chosenTangent.scale(weightTangent)).normalize();
    }
}