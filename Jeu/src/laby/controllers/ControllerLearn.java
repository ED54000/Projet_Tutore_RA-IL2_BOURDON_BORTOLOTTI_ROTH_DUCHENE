package laby.controllers;

import entites.defenses.Bomb;
import entites.defenses.Canon;
import entites.defenses.Defense;
import entites.enemies.*;
import evolution.EnnemyEvolution;
import evolution.Evolution;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import laby.ModeleLabyrinth;
import steering_astar.Steering.PathfollowingBehavior;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ControllerLearn implements EventHandler<MouseEvent> {

    ModeleLabyrinth laby;

    public ControllerLearn(ModeleLabyrinth laby) {
        this.laby = laby;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        // On fait évoluer les ennemis et les défenses
        //création d'une HashMap avec pour clé l'ennemi et pour valeur son score
        // On fait évoluer les ennemis par type
        Evolution evolution = new Evolution();

        Map<Class<? extends Ennemy>, List<Ennemy>> ennemisParType = Evolution.startStats.keySet()
                .stream()
                .collect(Collectors.groupingBy(Ennemy::getClass));

        //System.out.println("Ennemis par type : " + ennemisParType);

        for (Map.Entry<Class<? extends Ennemy>, List<Ennemy>> entry : ennemisParType.entrySet()) {

            List<Ennemy> ennemisType = entry.getValue();

            // Création d'une HashMap contenant uniquement ce type d'ennemi
            HashMap<Ennemy, Double> stats = new HashMap<>();
            for (Ennemy ennemy : ennemisType) {
                stats.put(ennemy, evolution.getScore(ennemy));
            }

            // Évolution pour ce type d'ennemi
            ArrayList<Ennemy> newPopulation = evolution.evolve(stats);

            laby.enemies.addAll(newPopulation);
            System.out.println("Nouvelle population : "+laby.enemies);
        }
        //on replace les ennemis au début
        for(Ennemy e : laby.enemies){
            e.setToStart(laby);
        }

        //on remet les valeurs par défaut
        laby.refreshEnnemyArrived();
        laby.refreshDeadEnemies();
        laby.refreshEnnemyEndOfManche();

        laby.refreshDefenseEndOfManche();
        laby.refreshDeadDefenses();

        //TODO : on remet les valeurs par défaut pour les defenses en attendant de les faire évoluer
        for (Defense d : laby.defenses) {
            d.setLastAttackCount(0);
            d.setIsDead(false);
            if (d instanceof Canon) {
                d.setHealth(300);
            }
            if (d instanceof Bomb) {
                d.setHealth(1000);
            }
            if (d instanceof entites.defenses.Archer) {
                d.setHealth(200);
            }
        }

        // On va compter le nombre d'ennemis pour chaque comportement
        int nbNinja = 0;
        int nbGiant = 0;
        int nbHealer = 0;
        int nbBerserker = 0;
        for(Ennemy e : laby.enemies){
            e.setIsDead(false);
            if(e instanceof Ninja){
                e.setBehavior("Fugitive");
                nbNinja++;
            }
            if(e instanceof Giant){
                e.setBehavior("Normal");
                nbGiant++;
            }
            if(e instanceof Druide){
                e.setBehavior("Healer");
                nbHealer++;
            }
            if(e instanceof Berserker){
                e.setBehavior("Kamikaze");
                nbBerserker++;
            }
        }
        System.out.println("Ninja : "+nbNinja+" Giant : "+nbGiant+" Healer : "+nbHealer+" Berserker : "+nbBerserker);
        laby.createBehaviours(laby.getCases());
        for (Ennemy e : laby.enemies) {
            e.setLastAttackCount(0);
            if (e.getBehavior().equals("Healer")) {
              e.setBehaviorPath(new PathfollowingBehavior(laby.getNewHealerAStar(nbHealer, nbGiant, nbBerserker, nbNinja)));
            } else {
                e.setBehaviorPath(new PathfollowingBehavior(laby.getBehavioursMap().get(e.getBehavior())));
            }
            e.setArrived(false);
        }

        for (Ennemy e : laby.enemies) {
            if (e.getHealth() < 0 ){
                e.setHealth(e.getHealth()*-1);
            }
        }

        //clear les logs si ce n'est pas une simulation
        // À l'intérieur de la méthode handle, avant de vider les logs
        if (!laby.estSimulation()) {
            VBox parentVBox = (VBox) ((Button) mouseEvent.getSource()).getParent();
            parentVBox.getChildren().clear();

            parentVBox.getChildren().add(new Label("Learned"));

            // En-tête pour l'évolution des Géants
            Label giantEvolutionHeader = new Label("Détails de l'évolution des Géants :");
            giantEvolutionHeader.setStyle("-fx-font-weight: bold;");
            parentVBox.getChildren().add(giantEvolutionHeader);

            // Mémoriser les anciennes statistiques des Géants
            Map<Ennemy, Double> anciensGiants = new HashMap<>();
            for (Ennemy e : Evolution.startStats.keySet()) {
                if (e instanceof Giant) {
                    anciensGiants.put(e, e.getHealth());
                }
            }

            // Afficher l'évolution de la santé pour chaque Géant
            for (Ennemy e : laby.enemies) {
                if (e instanceof Giant) {
                    Double ancienneSante = anciensGiants.get(e);

                    if (ancienneSante != null) {
                        Label detailGiant = new Label(String.format(
                                "Géant - Santé initiale : %.0f -> Nouvelle santé : %.0f",
                                ancienneSante,
                                e.getHealth()
                        ));
                        parentVBox.getChildren().add(detailGiant);
                    }
                }
            }

            // Bouton pour la prochaine manche
            Button nextManche = new Button("Prochaine Manche");
            nextManche.setOnMouseClicked(new ControllerNextManche(laby));
            parentVBox.getChildren().add(nextManche);
        }

        // On sauvegarde les statistiques des ennemis
        EnnemyEvolution.saveStartStats(laby.enemies);

    }
}
