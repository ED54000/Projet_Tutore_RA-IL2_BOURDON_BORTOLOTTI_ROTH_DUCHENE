package mains;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.stage.Stage;
import laby.ModeleLabyrinth;

public class MainSimulation extends Application {
    private double FPS = 100;
    private double dureeFPS = 1000 / (FPS + 1);

    @Override
    public void start(Stage stage) throws Exception {
        ModeleLabyrinth jeu = new ModeleLabyrinth();
        jeu.setSimulation(true);
        jeu.creerLabyrinthe("Ressources/Labyrinthe3.txt", 2, 1000, 1200);

        // stocke la derniere mise e jour
        final LongProperty lastUpdateTime = new SimpleLongProperty(0);

        // timer pour boucle de jeu
        final AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long timestamp) {
                //fin du jeu
                if (jeu.etreFini()) {
                    this.stop();
                    return;
                }

                // si jamais passe dans la boucle, initialise le temps
                if (lastUpdateTime.get() == 0) {
                    lastUpdateTime.set(timestamp);
                }

                // mesure le temps ecoule depuis la derniere mise a jour
                long elapsedTime = timestamp - lastUpdateTime.get();
                double dureeEnMilliSecondes = elapsedTime / 1_000_000.0;


                // si le temps ecoule depasse le neAcessaire pour FPS souhaite
                if (dureeEnMilliSecondes > dureeFPS) {
                    // met a jour le jeu
                    jeu.update(dureeEnMilliSecondes / 1_000.);

                    // dessine le jeu
                    //ViewLabyrinth.dessinerJeu(jeu, canvas);
                    //notifier observateurs

                    // ajoute la duree dans les statistiques
                    //System.out.println(elapsedTime);

                    // met a jour la date de derniere mise a jour
                    lastUpdateTime.set(timestamp);
                }

            }
        };
        // lance l'animation
        timer.start();
    }
    /*
    @Override
    public void start(Stage stage) throws Exception {
        ModeleLabyrinth jeu = new ModeleLabyrinth();
        jeu.setSimulation(true);
        jeu.creerLabyrinthe("Ressources/Labyrinthe3.txt", 2, 1000, 1200);

        // Lancer la simulation dans un thread séparé
        new Thread(() -> {
            long lastUpdateTime = System.nanoTime();

            while (!jeu.etreFini()) {
                // Calculer le temps écoulé depuis la dernière mise à jour
                long currentTime = System.nanoTime();
                double elapsedTimeInSeconds = (currentTime - lastUpdateTime) / 1_000_000_000.0;

                // Mettre à jour la simulation
                jeu.update(elapsedTimeInSeconds);

                // Mettre à jour le temps de la dernière mise à jour
                lastUpdateTime = currentTime;
            }

            System.out.println("Simulation terminée.");
        }).start();
    }

     */
}
