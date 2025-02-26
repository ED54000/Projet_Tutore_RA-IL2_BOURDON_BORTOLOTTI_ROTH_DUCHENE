package laby.controllers;

import entites.defenses.Archer;
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
import steering_astar.Steering.Vector2D;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static evolution.Evolution.refreshEnnemiesAndAdd;

public class ControllerLearn implements EventHandler<MouseEvent> {

    ModeleLabyrinth laby;
    ArrayList<ArrayList<Ennemy>> groupes = new ArrayList<>();

    public ControllerLearn(ModeleLabyrinth laby) {
        this.laby = laby;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        // a optimiser pour par répéter la condition
        VBox parentVBox = new VBox();
        if (!ModeleLabyrinth.getSimulation()){
            parentVBox = (VBox) ((Button) mouseEvent.getSource()).getParent();
            parentVBox.getChildren().clear();
        }

        // On fait évoluer les ennemis
        Evolution evolution = new Evolution();
        if (laby.getNbManches()<2){
            // nombre de groupes
            for (int i = 0; i < 20; i++) {
                groupes.add(laby.createEnnemies(laby.getEnnemyEndOfManche().size()));
            }
        }

        // On ajoute les nouveaux groupes a la map
        HashMap<ArrayList<Ennemy>, Double> stats = new HashMap<>();
        for (ArrayList<Ennemy> groupe : groupes) {
            stats.put(groupe, 0.0);
        }

        //Création d'une liste des ennemies de cette partie tout neuf
        ArrayList<Ennemy> copieGroupe = new ArrayList<>();
        for (Ennemy ennemy : laby.getEnnemyEndOfManche()) {
            refreshEnnemiesAndAdd(ennemy, laby, copieGroupe);
        }

        try {
            stats = evolution.evaluate(stats);
            stats.put(copieGroupe, evolution.getScore(laby.getEnnemyEndOfManche()));
            groupes = evolution.evolve(stats);
            laby.enemies = groupes.get(0);
            System.out.println("Après l'évolution");
            for (Ennemy ennemy : laby.enemies) {
                System.out.println(ennemy.getName() + " : " + ennemy.getHealth() + " " + ennemy.getDamages() + " " + ennemy.getSpeed());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //on crée ennemisParType
        Map<Class<? extends Ennemy>, List<Ennemy>> ennemisParType = laby.enemies.stream()
                .collect(Collectors.groupingBy(Ennemy::getClass));

        for (Map.Entry<Class<? extends Ennemy>, List<Ennemy>> entry : ennemisParType.entrySet()) {
            // En-tête
            Label EvolutionHeader = new Label("Détails de l'évolution des : " + entry.getKey().getSimpleName());
            EvolutionHeader.setStyle("""
                -fx-font-weight: bold;
                -fx-font-size: 12px;
                -fx-padding: 5 0 3 0;
            """);
            parentVBox.getChildren().add(EvolutionHeader);

            // Configurer le style du VBox parent
                        parentVBox.setStyle("""
                -fx-background-color: #f5f5f5;
                -fx-padding: 5;
                -fx-spacing: 3;
            """);

            // Afficher l'évolution pour chaque Géant
            for(int i = 0; i < entry.getValue().size(); i++) {
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
                parentVBox.getChildren().add(detailGiant);
            }
        }
        if (!ModeleLabyrinth.getSimulation()) {
            // Bouton pour la prochaine manche
            Button nextManche = new Button("Next Manche");
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
            parentVBox.getChildren().add(nextManche);
            ModeleLabyrinth.setLogs("");
        }

        //on replace les ennemis au début
        for(Ennemy e : laby.enemies){
            e.setToStart(laby);
        }

        //on remet les valeurs par défaut
        laby.refreshEnnemyArrived();
        laby.refreshDeadEnemies();
        laby.refreshEnnemyEndOfManche();

        laby.refreshDeadDefenses();
        laby.defenses = laby.getDefenseEndOfManche();

        System.out.println("Défensess : " + laby.defenses.size());
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
            if (d instanceof Archer) {
                d.setHealth(200);
            }
        }
        laby.refreshDefenseEndOfManche();

        // On va compter le nombre d'ennemis pour chaque comportement
        int nbNinja = 0;
        int nbGiant = 0;
        int nbHealer = 0;
        int nbBerserker = 0;
        for(Ennemy e : laby.enemies){
            e.setIsDead(false);
            if(e instanceof Ninja){
                e.setBehaviorString("Fugitive");
                nbNinja++;
            }
            if(e instanceof Giant){
                e.setBehaviorString("Normal");
                nbGiant++;
            }
            if(e instanceof Druide){
                e.setBehaviorString("Healer");
                nbHealer++;
            }
            if(e instanceof Berserker){
                e.setBehaviorString("Kamikaze");
                nbBerserker++;
            }
        }
        System.out.println("Ninja : "+nbNinja+" Giant : "+nbGiant+" Healer : "+nbHealer+" Berserker : "+nbBerserker);
        //laby.createBehaviours(laby.getCases());

        for (Ennemy e : laby.enemies) {
            e.setLastAttackCount(0);
            ArrayList<Vector2D> newPathToFollow;
            if (e.getBehaviorString().equals("Healer")) {
                newPathToFollow = laby.getNewHealerAStar(nbGiant, nbBerserker, nbNinja);
            } else {
                newPathToFollow = e.calculerChemin(ModeleLabyrinth.getCases(),new Vector2D(ModeleLabyrinth.getYstart(), ModeleLabyrinth.getXstart()));
            }
            e.setBehavior(new PathfollowingBehavior(newPathToFollow));
            e.setArrived(false);
        }

        for (Ennemy e : laby.enemies) {
            if (e.getHealth() < 0 ){
                e.setHealth(e.getHealth()*-1);
            }


        }



        // On sauvegarde les statistiques des ennemis
        EnnemyEvolution.saveStartStats(laby.enemies);

    }
}
