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
    private static final String BUTTON_STYLE = """
            -fx-background-color: #4CAF50;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-padding: 8px 16px;
            -fx-background-radius: 5px;
            -fx-cursor: hand;
            -fx-font-weight: bold;
            -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);
            """;

    private static final String BUTTON_SELECTED_STYLE = """
            -fx-background-color: #34495e;
            -fx-text-fill: #ecf0f1;
            -fx-font-size: 14px;
            -fx-padding: 8px 16px;
            -fx-background-radius: 5px;
            -fx-cursor: hand;
            -fx-font-weight: bold;
            -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);
            """;

    public static void modifySpeed(int speed, ToggleButton buttonSelected, ToggleButton toggleButton) {
        if (buttonSelected.isSelected()) {
            modifyFPS(speed);
            buttonSelected.setStyle(BUTTON_SELECTED_STYLE);

            toggleButton.setSelected(false);
            pauseButton.setSelected(false);

            toggleButton.setStyle(BUTTON_STYLE);
            pauseButton.setStyle(BUTTON_STYLE);

            modeleLabyrinth.setPause(false);
        } else {
            modifyFPS(BASE_FPS);
            buttonSelected.setStyle(BUTTON_STYLE);
        }
    }

    public static void pause() {
        if (pauseButton.isSelected()) {
            modeleLabyrinth.setPause(true);
            pauseButton.setStyle(BUTTON_SELECTED_STYLE);

            speedButton.setSelected(false);
            slowButton.setSelected(false);

            speedButton.setStyle(BUTTON_STYLE);
            slowButton.setStyle(BUTTON_STYLE);
        } else {
            modeleLabyrinth.setPause(false);
            pauseButton.setStyle(BUTTON_STYLE);
        }
        setFPS(BASE_FPS);
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
