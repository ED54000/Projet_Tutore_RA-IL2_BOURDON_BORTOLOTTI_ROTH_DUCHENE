package steering_astar.Steering;

import entites.enemies.Ennemy;
import laby.ModeleLabyrinth;

public class AvoidBehavior extends Behavior {

    private static final double MAX_SEE_AHEAD = 30.0;
    private static final double BASE_AVOID_WEIGHT = 5.0;
    private static final int NUM_FEELERS = 7;

    public AvoidBehavior(Vector2D target) {
        this.setTarget(target);
        if (ModeleLabyrinth.getLabyrinth().getUseAstar()){
            this.setWeight(BASE_AVOID_WEIGHT/2);
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
            // Un obstacle est dans la trajectoire du feeler
            if (isObstacleInPath(ennemy)) {
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

        double angleOffset = Math.PI / 4;
        feelers[1] = position.add(rotateVector(normalized, Math.PI / 4).scale(MAX_SEE_AHEAD* 0.75));
        feelers[2] = position.add(rotateVector(normalized, -Math.PI / 4).scale(MAX_SEE_AHEAD* 0.75));
        
        // Feelers supplémentaires
        feelers[3] = position.add(rotateVector(normalized, Math.PI / 2).scale(MAX_SEE_AHEAD * 0.75));
        feelers[4] = position.add(rotateVector(normalized, -Math.PI / 2 ).scale(MAX_SEE_AHEAD * 0.75));

        feelers[5] = position.add(rotateVector(normalized, Math.PI / 3 ).scale(MAX_SEE_AHEAD * 0.25));
        feelers[6] = position.add(rotateVector(normalized, -Math.PI / 3 ).scale(MAX_SEE_AHEAD * 0.25));

        return feelers;
    }

    private Vector2D rotateVector(Vector2D v, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        return new Vector2D(
                v.getX() * cos - v.getY() * sin,
                v.getX() * sin + v.getY() * cos
        );
    }

    private boolean isObstacleInPath(Ennemy ennemy) {
        return ennemy.getPosition().add(ennemy.getVelocity().normalize().scale(MAX_SEE_AHEAD)).isObstacle();
    }

    private Vector2D calculateAvoidanceForce(Vector2D position, Vector2D obstaclePoint, Vector2D velocity) {
        // Calcul d'une force qui pousse perpendiculairement à l'obstacle
        Vector2D awayFromObstacle = position.subtract(obstaclePoint);
        Vector2D perpendicularVector = new Vector2D(-velocity.getY(), velocity.getX()).normalize();
        return awayFromObstacle.normalize().add(perpendicularVector).normalize();
    }
}
