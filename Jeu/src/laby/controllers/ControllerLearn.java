package laby.controllers;

import entites.defenses.Archer;
import entites.defenses.Bomb;
import entites.defenses.Canon;
import entites.defenses.Defense;
import entites.enemies.*;
import evolution.EnnemyEvolution;
import evolution.EvolutionSteering;
import javafx.application.Platform;
import evolution.EvolutionGroupe;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import laby.ModeleLabyrinth;
import laby.views.ViewLoading;
import steering_astar.Steering.PathfollowingBehavior;
import steering_astar.Steering.Vector2D;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static evolution.EvolutionGroupe.refreshEnnemiesAndAdd;
import static evolution.EvolutionSteering.refreshEnnemies;

public class ControllerLearn implements EventHandler<MouseEvent> {

    private ModeleLabyrinth laby;
    private ArrayList<ArrayList<Ennemy>> groupes = new ArrayList<>();

    //    private ControllerGraphique controllerGraphique;
    private HashMap<ArrayList<Ennemy>, Double> stats;

    public ControllerLearn(ModeleLabyrinth laby) {
        this.laby = laby;

    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        // Affichage de la popup de chargement
        Stage stage = (Stage) ((Button) mouseEvent.getSource()).getScene().getWindow();
        ViewLoading viewLoading = new ViewLoading(stage);
        System.out.println("Affichage de la popup évolution ...");
        Platform.runLater(viewLoading::show); // Affiche la popup sur le thread JavaFX

        // On set evolving a true
        laby.setEvolving(true);

        // Crée le VBox pour les détails de l'évolution
        VBox parentVBox = new VBox();
        if (!ModeleLabyrinth.getSimulation()) {
            parentVBox = (VBox) ((Button) mouseEvent.getSource()).getParent();
            parentVBox.getChildren().clear();
        }

        // Exécute l'évolution des ennemis dans un thread en arrière-plan
        VBox finalParentVBox = parentVBox;
        VBox finalParentVBox1 = parentVBox;
        new Thread(() -> {
            // On fait évoluer les ennemis
            EvolutionSteering evolution = new EvolutionSteering();
            evolution.nbchekpoints = 2;
            if (laby.getNbManches() < 2) {
                for (int i = 0; i < 40; i++) { //50 groupes d'un géant
                    groupes.add(new ArrayList<>(List.of(new Giant(new Vector2D(0, 0), "Giant"+i))));
                }
            }

            // On ajoute les nouveaux groupes à la map
            stats = new HashMap<>();
            System.out.println("Groupes : " + groupes);
            //for (ArrayList<Ennemy> groupe : groupes) {
            //    stats.put(groupe, 0.0);
            //}
            for (int i = 0; i < groupes.size(); i++) {
                ArrayList<Ennemy> groupe = groupes.get(i);
                stats.put(groupe, 0.0);
            }

            System.out.println("Stats : " + stats);

            double score = evolution.getScore(laby.getEnnemyEndOfManche());
            for (Ennemy ennemy : laby.getEnnemyEndOfManche()) {
                refreshEnnemies(ennemy, laby);
            }

            try {
                laby.setSimulationEvolution(true);
                stats = evolution.evaluate(stats);
                stats.put(new ArrayList<>(List.of(laby.getEnnemyEndOfManche().get(0))), score);
                groupes = evolution.evolve(stats);
                laby.setSimulationEvolution(false);
                //crée un copie de la première liste des ennemis de groupes
                ArrayList<Ennemy> ennemies = new ArrayList<>(groupes.get(0));
                laby.enemies = ennemies;

                for (ArrayList<Ennemy> groupe : groupes) {
                    Ennemy ennemy = groupe.get(0);
                    //Affiche les waypoints
                    System.out.println("Waypoints de " + ennemy + "(" + stats.get(groupe) + ")");
                    for (Vector2D waypoint : ((PathfollowingBehavior) ennemy.getListBehavior().get(0)).getCheckpoints()) {
                        System.out.println(waypoint.div(41));
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Séparer les ennemis par type pour affichage
            Map<Class<? extends Ennemy>, List<Ennemy>> ennemisParType = laby.enemies.stream()
                    .collect(Collectors.groupingBy(Ennemy::getClass));

            // Ajouter les détails de l'évolution à l'interface utilisateur
            HashMap<ArrayList<Ennemy>, Double> finalStats = stats;
            Platform.runLater(() -> {
                for (Map.Entry<Class<? extends Ennemy>, List<Ennemy>> entry : ennemisParType.entrySet()) {
                    Label EvolutionHeader = new Label("Détails de l'évolution des : " + entry.getKey().getSimpleName());
                    EvolutionHeader.setStyle("""
                            -fx-font-weight: bold;
                            -fx-font-size: 12px;
                            -fx-padding: 5 0 3 0;
                            """);
                    finalParentVBox.getChildren().add(EvolutionHeader);

                    finalParentVBox.setStyle("""
                                -fx-background-color: #f5f5f5;
                            -fx-padding: 5;
                            -fx-spacing: 3;
                            """);

                    // Afficher l'évolution pour chaque ennemi
                    for (int i = 0; i < entry.getValue().size(); i++) {
                        Ennemy e = entry.getValue().get(i);
                        Ennemy ancien = entry.getValue().get(i);

                        Label detailGiant = new Label(String.format(
                                "%s - Santé: %.0f→%.0f | Vitesse: %.1f→%.1f | Dégâts: %.0f→%.0f",
                                e.getName(),
                                ancien.getHealth(), e.getHealth(),
                                ancien.getSpeed(), e.getSpeed(),
                                ancien.getDamages(), e.getDamages()
                        ));
                        detailGiant.setStyle("""
                                 -fx-padding: 3 8;
                                -fx-background-color: white;
                                -fx-background-radius: 3;
                                -fx-border-color: #e0e0e0;
                                -fx-border-radius: 3;
                                -fx-font-family: 'Segoe UI', sans-serif;
                                -fx-font-size: 11px;
                                """);
                        detailGiant.setMaxWidth(Double.MAX_VALUE);
                        finalParentVBox.getChildren().add(detailGiant);
                    }
                }

                // On set evolving a false
                laby.setEvolving(false);

                // Après avoir ajouté tous les détails, fermer la popup de chargement
                viewLoading.close();

                // Remettre les ennemis au début et actualiser les valeurs
                for (Ennemy e : laby.enemies) {
                    e.setToStart(laby);
                }

                // Mettre à jour les défenses et ennemis
                laby.refreshEnnemyArrived();
                laby.refreshDeadEnemies();
                laby.refreshEnnemyEndOfManche();

                laby.refreshDeadDefenses();
                laby.defenses = laby.getDefenseEndOfManche();
                for (Defense d : laby.defenses) {
                    d.setLastAttackCount(0);
                    d.setIsDead(false);
                    if (d instanceof Canon) {
                        d.setHealth(300);
                    }
                    if (d instanceof Bomb) {
                        d.setHealth(1000);
                    }
                    if (d instanceof Archer) {
                        d.setHealth(200);
                    }
                }
                laby.refreshDefenseEndOfManche();

                if (laby.getUseAstar()) {
                    // On va compter le nombre d'ennemis pour chaque comportement
                    int nbNinja = 0;
                    int nbGiant = 0;
                    int nbHealer = 0;
                    int nbBerserker = 0;
                    for (Ennemy e : laby.enemies) {
                        e.setIsDead(false);
                        if (e instanceof Ninja) {
                            e.setBehaviorString("Fugitive");
                            nbNinja++;
                        }
                        if (e instanceof Giant) {
                            e.setBehaviorString("Normal");
                            nbGiant++;
                        }
                        if (e instanceof Druide) {
                            e.setBehaviorString("Healer");
                            nbHealer++;
                        }
                        if (e instanceof Berserker) {
                            e.setBehaviorString("Kamikaze");
                            nbBerserker++;
                        }
                    }
                    System.out.println("Ninja : " + nbNinja + " Giant : " + nbGiant + " Healer : " + nbHealer + " Berserker : " + nbBerserker);

                    // Actualiser les comportements des ennemis
                    for (Ennemy e : laby.enemies) {
                        ArrayList<Vector2D> newPathToFollow;
                        if (e.getBehaviorString().equals("Healer")) {
                            newPathToFollow = laby.getNewHealerAStar(nbGiant, nbBerserker, nbNinja);
                        } else {
                            newPathToFollow = e.calculerChemin(ModeleLabyrinth.getCases(), new Vector2D(ModeleLabyrinth.getYstart(), ModeleLabyrinth.getXstart()));
                        }
                        e.setBehavior(new PathfollowingBehavior(newPathToFollow));
                        e.setArrived(false);
                    }
                }

                //for (Ennemy e : laby.enemies) {
                //    if (e.getHealth() < 0) {
                //        e.setHealth(e.getHealth() * -1);
                //    }
                //}

                // Sauvegarder les statistiques des ennemis
                //EnnemyEvolution.saveStartStats(laby.enemies);


                if (!ModeleLabyrinth.getSimulation()) {
                    // Bouton pour la prochaine manche
                    Button nextManche = new Button(
                            "Next Manche");
                    nextManche.setStyle("""
                        -fx-background-color: #4CAF50;
                        -fx-text-fill: white;
                        -fx-font-size: 11px;
                        -fx-padding: 3 10;
                        -fx-background-radius: 3;
                        -fx-cursor: hand;
                                    """);

                    nextManche.setOnMouseEntered(e ->
                            nextManche.setStyle(nextManche.getStyle() + "-fx-background-color: #45a049;"));
                    nextManche.setOnMouseExited(e ->

                            nextManche.setStyle(nextManche.getStyle() + "-fx-background-color: #4CAF50;"));

                    nextManche.setOnMouseClicked(new ControllerNextManche(laby));

                    finalParentVBox1.getChildren().add(0,nextManche);
                    ModeleLabyrinth.setLogs("");
                }
            });
        }).start(); // Lancer la tâche en arrière-plan

    }

    public HashMap<ArrayList<Ennemy>, Double> getStats() {
        return stats;
    }

    public void setStats(HashMap<ArrayList<Ennemy>, Double> stats) {
        this.stats = stats;
    }
}
