package mains;

import laby.ModeleLabyrinth;

import java.io.IOException;

public class MainSimulation {
    private static final double FPS = 100;
    private static final double dureeFPS = 1000 / (FPS + 1);

    public static void main(String[] args) {
        try {
            ModeleLabyrinth jeu = new ModeleLabyrinth();
            jeu.setSimulation(true);
            jeu.creerLabyrinthe("Ressources/Labyrinthe3.txt", jeu.createEnnemies(50), 1000, 45);

            // Lancer la simulation dans un thread séparé
            Thread simulationThread = new Thread(() -> {
                long lastUpdateTime = System.nanoTime();

                while (!jeu.etreFini()) {
                    long currentTime = System.nanoTime();
                    double elapsedTimeInSeconds = (currentTime - lastUpdateTime) / 1_000_000_000.0;

                    jeu.update(elapsedTimeInSeconds);
                    lastUpdateTime = currentTime;

                    // Ajouter un petit délai pour éviter une consommation excessive du CPU
                    try {
                        Thread.sleep((long) dureeFPS);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.err.println("Simulation interrompue.");
                        return;
                    }
                }

                System.out.println(jeu.getLogs());
                System.out.println("Simulation terminée.");
            });

            simulationThread.start();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }
}
