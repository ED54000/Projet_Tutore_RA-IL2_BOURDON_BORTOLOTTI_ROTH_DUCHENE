package steering_astar.Steering;

import entites.enemies.Ennemy;
import laby.ModeleLabyrinth;

public class AvoidBehavior extends Behavior {

    private static final double MAX_SEE_AHEAD = 1.25;
    private static final double BASE_AVOID_WEIGHT = 4.5;
    private static final int NUM_FEELERS = 3;

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


        // Si la longueur est petit
        if (velocity.magnitude() < 0.0001) {
            return new Vector2D(0, 0);
        }

        //creation des feelers
        Vector2D[] feelers = createFeelers(position, velocity);

        double closestDist = Double.MAX_VALUE;
        Vector2D closestPoint = null;


        //Pour chaque feeler
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

        // Calcul de la force d'évitement
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
        feelers[1] = position.add(rotateVector(normalized, angleOffset).scale(MAX_SEE_AHEAD));
        feelers[2] = position.add(rotateVector(normalized, -angleOffset).scale(MAX_SEE_AHEAD));

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
        Vector2D awayFromObstacle = position.subtract(obstaclePoint);
        Vector2D perpendicularVector = new Vector2D(-velocity.getY(), velocity.getX()).normalize();
        return awayFromObstacle.normalize().add(perpendicularVector).normalize();
    }
}