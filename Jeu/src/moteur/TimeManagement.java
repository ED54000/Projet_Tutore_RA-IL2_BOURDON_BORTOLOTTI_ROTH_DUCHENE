package moteur;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.ToggleButton;
import javafx.util.Duration;
import laby.ModeleLabyrinth;

import static moteur.MoteurJeu.*;

public class TimeManagement {
    private static double dureeFPS = 1000 / (FPS + 1);
    private static Timeline timeline;
    private static final FrameStats frameStats = new FrameStats();
    private static Jeu jeu;
    private static ModeleLabyrinth modeleLabyrinth;
    private static ToggleButton speedButton, slowButton, pauseButton;

    public TimeManagement(Jeu jeu, ModeleLabyrinth laby, ToggleButton speedButton, ToggleButton slowButton, ToggleButton pauseButton) {
        TimeManagement.jeu = jeu;
        modeleLabyrinth = laby;
        this.speedButton = speedButton;
        this.slowButton = slowButton;
        this.pauseButton = pauseButton;
    }

    public static void modifySpeed(int speed, ToggleButton buttonSelected, ToggleButton toggleButton) {
        if (buttonSelected.isSelected()) {
            modifyFPS(speed);
            buttonSelected.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

            toggleButton.setSelected(false);
            pauseButton.setSelected(false);

            toggleButton.setStyle("");
            pauseButton.setStyle("");

            modeleLabyrinth.setPause(false);
        } else {
            modifyFPS(BASE_FPS);
            buttonSelected.setStyle("");
        }
    }

    public static void modifyFPS(double newFPS) {
        if (newFPS <= 0) return; // Sécurité : éviter une division par zéro

        dureeFPS = 1000.0 / newFPS; // Convertir FPS en durée (ms)

        // Si le timeline existe déjà, on l'arrête avant de le recréer
        if (timeline != null) {
            timeline.stop();
        }

        // Création d'un nouveau Timeline avec le nouvel intervalle
        timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(Duration.millis(dureeFPS), event -> {
            if (jeu.etreFini()) {
                timeline.stop();
                return;
            }

            jeu.update(dureeFPS / 1000.0);

            // ViewLabyrinth.dessinerJeu(jeu, canvas);
            // Notifier les observateurs

            frameStats.addFrame((long) (dureeFPS * 1_000_000)); // Convertir en ns
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public static void pause() {
        if (pauseButton.isSelected()) {
            modeleLabyrinth.setPause(true);
            pauseButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

            speedButton.setSelected(false);
            slowButton.setSelected(false);

            speedButton.setStyle("");
            slowButton.setStyle("");
        } else {
            modeleLabyrinth.setPause(false);
            pauseButton.setStyle("");
        }
        setFPS(BASE_FPS);
    }

    /**
     * frame par secondes
     *
     * @param FPSSouhaitees nombre de frames par secondes souhaitees
     */
    public static void setFPS(int FPSSouhaitees) {
        FPS = FPSSouhaitees;
        dureeFPS = 1000 / (FPS + 1);
    }

}
