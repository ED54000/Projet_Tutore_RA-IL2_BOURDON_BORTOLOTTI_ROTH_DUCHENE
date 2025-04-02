package steering_astar.Steering;

import entites.enemies.Ennemy;
import laby.ModeleLabyrinth;

public class AvoidBehavior extends Behavior {
    // Augmentation de la distance de détection
    private static final double MAX_SEE_AHEAD = 2.0;
    // Réduction du poids de base pour éviter la panique
    private static final double BASE_AVOID_WEIGHT = 10;
    // Augmentation du nombre de feelers pour une meilleure détection
    private static final int NUM_FEELERS = 7;
    private Vector2D arrivalPoint;

    public AvoidBehavior(Vector2D target) {
        this.setTarget(target);
        this.arrivalPoint = target;
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
        if (point == null || isArrivalPoint(point)) {
            return false;
        }
        return point.isObstacle();
    }

    private boolean isArrivalPoint(Vector2D point) {
        return Math.abs(point.getX() - arrivalPoint.getX()) < 0.5 &&
               Math.abs(point.getY() - arrivalPoint.getY()) < 0.5;
    }

    private Vector2D[] createFeelers(Vector2D position, Vector2D velocity) {
        Vector2D[] feelers = new Vector2D[NUM_FEELERS];
        Vector2D normalized = velocity.normalize();

        // Feeler central plus long pour voir plus loin devant
        feelers[0] = position.add(normalized.scale(MAX_SEE_AHEAD));

        // Feelers latéraux plus courts mais plus nombreux
        feelers[1] = position.add(rotateVector(normalized, Math.PI / 6).scale(MAX_SEE_AHEAD * 0.2));
        feelers[2] = position.add(rotateVector(normalized, -Math.PI / 6).scale(MAX_SEE_AHEAD * 0.2));
        feelers[3] = position.add(rotateVector(normalized, Math.PI / 4).scale(MAX_SEE_AHEAD * 0.2));
        feelers[4] = position.add(rotateVector(normalized, -Math.PI / 4).scale(MAX_SEE_AHEAD * 0.2));
        feelers[5] = position.add(rotateVector(normalized, Math.PI / 3).scale(MAX_SEE_AHEAD * 0.2));
        feelers[6] = position.add(rotateVector(normalized, -Math.PI / 3).scale(MAX_SEE_AHEAD * 0.2));

        return feelers;
    }

    private Vector2D calculateAvoidanceForce(Vector2D position, Vector2D obstaclePoint, Vector2D velocity) {
        // Calcul du vecteur perpendiculaire à l'obstacle
        Vector2D away = position.subtract(obstaclePoint).normalize();

        // Calcul des deux directions perpendiculaires possibles
        Vector2D perpRight = new Vector2D(-away.getY(), away.getX());
        Vector2D perpLeft = new Vector2D(away.getY(), -away.getX());

        // Choix de la direction perpendiculaire en fonction de la vélocité actuelle
        Vector2D currentDir = velocity.normalize();
        double dotRight = perpRight.getX() * currentDir.getX() + perpRight.getY() * currentDir.getY();
        double dotLeft = perpLeft.getX() * currentDir.getX() + perpLeft.getY() * currentDir.getY();

        // Retourne la direction perpendiculaire qui correspond le mieux à la direction actuelle
        return (dotRight > dotLeft) ? perpRight.normalize() : perpLeft.normalize();
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

