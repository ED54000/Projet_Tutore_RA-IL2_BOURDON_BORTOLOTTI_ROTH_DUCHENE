package steering_astar.Steering;

import entites.enemies.Ennemy;
import laby.ModeleLabyrinth;

public class AvoidBehavior extends Behavior {
    // Augmentation de la distance de détection
    private static final double MAX_SEE_AHEAD = 2.0;
    // Réduction du poids de base pour éviter la panique
    private static final double BASE_AVOID_WEIGHT = 1.5;
    // Augmentation du nombre de feelers pour une meilleure détection
    private static final int NUM_FEELERS = 7;

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
    private boolean isObstacleAtPoint(Vector2D point) {
        if (point == null) {
            return false;
        }
        return point.isObstacle();
    }

    private Vector2D[] createFeelers(Vector2D position, Vector2D velocity) {
        Vector2D[] feelers = new Vector2D[NUM_FEELERS];
        Vector2D normalized = velocity.normalize();

        // Feeler central plus long pour voir plus loin devant
        feelers[0] = position.add(normalized.scale(MAX_SEE_AHEAD));

        // Feelers latéraux plus courts mais plus nombreux
        feelers[1] = position.add(rotateVector(normalized, Math.PI / 8).scale(MAX_SEE_AHEAD * 0.8));
        feelers[2] = position.add(rotateVector(normalized, -Math.PI / 8).scale(MAX_SEE_AHEAD * 0.8));

        feelers[3] = position.add(rotateVector(normalized, Math.PI / 4).scale(MAX_SEE_AHEAD * 0.6));
        feelers[4] = position.add(rotateVector(normalized, -Math.PI / 4).scale(MAX_SEE_AHEAD * 0.6));

        feelers[5] = position.add(rotateVector(normalized, Math.PI / 2.5).scale(MAX_SEE_AHEAD * 0.4));
        feelers[6] = position.add(rotateVector(normalized, -Math.PI / 2.5).scale(MAX_SEE_AHEAD * 0.4));

        return feelers;
    }

    private Vector2D calculateAvoidanceForce(Vector2D position, Vector2D obstaclePoint, Vector2D velocity) {
        Vector2D away = position.subtract(obstaclePoint).normalize();
        Vector2D currentDirection = velocity.normalize();

        Vector2D tangentUp = new Vector2D(-away.getY(), away.getX()).normalize();
        Vector2D tangentDown = new Vector2D(away.getY(), -away.getX()).normalize();

        // Calcul du produit scalaire
        double dotUp = tangentUp.getX() * currentDirection.getX() + tangentUp.getY() * currentDirection.getY();
        double dotDown = tangentDown.getX() * currentDirection.getX() + tangentDown.getY() * currentDirection.getY();

        Vector2D chosenTangent = (dotUp > dotDown) ? tangentUp : tangentDown;

        // Ajustement des poids pour une meilleure évitement
        double distance = position.subtract(obstaclePoint).magnitude();
        double weightAway = 0.7 * (1 / (distance + 0.1)); // Plus fort quand proche
        double weightTangent = 0.3;

        return away.scale(weightAway).add(chosenTangent.scale(weightTangent)).normalize();
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
}