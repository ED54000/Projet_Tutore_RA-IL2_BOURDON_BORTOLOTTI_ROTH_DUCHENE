package laby;

import entites.defenses.*;
import entites.enemies.*;
import evolution.EnnemyEvolution;
import evolution.EnnemyEvolutionv2;
import evolution.Evolution;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import laby.controllers.ControllerLearn;
import laby.controllers.ControllerNextManche;
import moteur.Jeu;
import steering_astar.Steering.Behavior;
import steering_astar.Steering.PathfollowingBehavior;
import steering_astar.Steering.SeekBehavior;
import steering_astar.Steering.Vector2D;
import steering_astar.Astar.*;

import java.awt.*;
import java.io.*;

import java.sql.SQLOutput;
import java.util.*;
import java.util.List;

public class ModeleLabyrinth implements Jeu, Subject {

    public static final char START = 'S';
    public static final char END = 'E';
    public static final char ROAD = '.';
    public static final char TREE = '#';
    public static final char CANON = 'C';
    public static final char ARCHER = 'A';
    public static final char BOMB = 'B';

    private int nbManches = 1;
    private int limManches;
    // Nombre d'ennemis qui doivent arriver à la fin pour gagner
    public int nbEnnemiesToWin;
    private int nbEnnemiesArrived;
    private long startTime;

    private static boolean useAstar;

    //entités
    public ArrayList<Ennemy> enemies = new ArrayList<>();
    public ArrayList<Defense> defenses = new ArrayList<>();
    public ArrayList<Ennemy> deadEnemies = new ArrayList<>();
    public ArrayList<Defense> deadDefenses = new ArrayList<>();
    private ArrayList<Defense> defensesEndOfManche = new ArrayList<>();
    private ArrayList<Ennemy> ennemiesEndOfManche = new ArrayList<>();
    private ArrayList<Ennemy> ennemiesArrived = new ArrayList<>();
    private ArrayList<Ennemy> enemiesToRemove = new ArrayList<>();
    private HashMap<Giant, Double> ennemyScore = new HashMap<>();
    private ArrayList<Giant> ennemiesEvolved = new ArrayList<>();
    private static int nbArcher, nbCanon, nbBomb, nbGiant, nbNinja, nbBerserker, nbDruides = 0;


    //si le jeu est avec le main simulation
    private static boolean simulation = false;
    private static boolean simulationEvolution = false;


    static Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();


    //labyrinthe
    private static ModeleLabyrinth instance;
    private static char[][] cases;
    private int XstartRender, YstartRender, XArrivalRender, YArrivalRender;
    private static int XArrival, YArrival, Xstart, Ystart;
    private ArrayList<Observer> observateurs;

    private String logs = "";

    private boolean pause = false;
    private boolean end = false;
    private long endTime;

    //constructeur vide
    public ModeleLabyrinth() {
        this.observateurs = new ArrayList<>();
        this.nbEnnemiesArrived = 0;
    }

    /**
     * Crée un labyrinthe à partir d'un fichier
     *
     * @param fichier         le fichier contenant le labyrinthe
     * @param ennemies        le nombre d'ennemis
     * @param limManches      le nombre de manches
     * @param nbEnnemiesToWin le nombre d'ennemis à atteindre l'arrivée pour gagner
     * @throws IOException si le fichier n'existe pas
     */
    public void creerLabyrinthe(String fichier, ArrayList<Ennemy> ennemies, int limManches, int nbEnnemiesToWin) throws IOException {
        this.enemies = ennemies;

        this.limManches = limManches;
        //ouvrir le fichier
        FileReader fr = new FileReader(fichier);
        BufferedReader br = new BufferedReader(fr);

        int nbLignes, nbColonnes;
        nbLignes = Integer.parseInt(br.readLine());
        nbColonnes = Integer.parseInt(br.readLine());

        //création du labyrinthe vide
        cases = new char[nbLignes][nbColonnes];

        // Nombre d'ennemis qui doivent arriver à la fin pour gagner
        this.nbEnnemiesToWin = nbEnnemiesToWin;

        //lecture des cases
        String ligne = br.readLine();

        //stocke les indices
        int numLigne = 0;

        //parcours le fichier
        while (ligne != null) {
            for (int colonne = 0; colonne < ligne.length(); colonne++) {
                char c = ligne.charAt(colonne);
                switch (c) {
                    case TREE:
                        //ajouter un mur
                        this.cases[numLigne][colonne] = TREE;
                        break;
                    case ROAD:
                        // ajouter une route
                        this.cases[numLigne][colonne] = ROAD;
                        break;
                    case START:
                        //ajouter le point de départ
                        this.cases[numLigne][colonne] = START;
                        this.Xstart = colonne;
                        this.Ystart = numLigne;
                        this.XstartRender = colonne * getTailleCase();
                        this.YstartRender = numLigne * getTailleCase();
                        break;
                    case END:
                        //ajouter le point d'arrivée
                        this.cases[numLigne][colonne] = END;
                        this.XArrival = colonne;
                        this.YArrival = numLigne;
                        this.XArrivalRender = colonne * getTailleCase();
                        this.YArrivalRender = numLigne * getTailleCase();
                        break;
                    case CANON:
                        //ajouter un canon
                        this.cases[numLigne][colonne] = CANON;
                        this.defenses.add(new Canon(colonne, numLigne, "Canon" + this.nbCanon));
                        this.nbCanon++;
                        break;
                    case BOMB:
                        //ajouter une bombe
                        this.cases[numLigne][colonne] = BOMB;
                        this.defenses.add(new Bomb(colonne, numLigne, "Bomb" + this.nbBomb));
                        this.nbBomb++;
                        break;
                    case ARCHER:
                        //ajouter un archer
                        this.cases[numLigne][colonne] = ARCHER;
                        this.defenses.add(new Archer(colonne, numLigne, "Archer" + this.nbArcher));
                        this.nbArcher++;
                        break;
                }
            }
            numLigne++;
            ligne = br.readLine();
        }
        setAllEnnemiesStats(this.enemies);
        br.close();
    }

    private void setAllEnnemiesStats(ArrayList<Ennemy> ennemies) {
        for (Ennemy e : ennemies) {
            e.setPositionReel(e.getPosition().divide(ModeleLabyrinth.getTailleCase()));

            e.setToStart(this);

            if (useAstar) {
                if (e instanceof Druide) {
                    ArrayList<Vector2D> aStarHealerPath = getNewHealerAStar(nbGiant, nbBerserker, nbNinja);
                    e.setBehavior(new PathfollowingBehavior(aStarHealerPath));
                    e.setDistanceStartToArrival(aStarHealerPath);
                } else {
                    ArrayList<Vector2D> astarPath = e.calculerChemin(cases,getStart());
                    e.setBehavior(new PathfollowingBehavior(astarPath));
                    e.setDistanceStartToArrival(astarPath);
                }
            } else {
                e.setBehavior(new SeekBehavior(new Vector2D(XArrivalRender, YArrivalRender)));
            }
        }
    }

    public static ArrayList<Ennemy> createEnnemies(int nbEnnemies) {
        ArrayList<Ennemy> ennemies = new ArrayList<>();
        for (int i = 0; i < nbEnnemies; i++) {
            //crée un ennemi au hasard
            int random = (int) (Math.random() * 4);
            switch (random) {
                case 0:
                    Giant giant = new Giant(new Vector2D(0, 0), "Giant " + nbGiant);
                    ennemies.add(giant);
                    nbGiant++;
                    break;
                case 1:
                    Ninja ninja = new Ninja(new Vector2D(0, 0), "Ninja " + nbNinja);
                    ennemies.add(ninja);
                    nbNinja++;
                    break;
                case 2:
                    Berserker berserker = new Berserker(new Vector2D(0, 0), "Berseker " + nbBerserker);
                    ennemies.add(berserker);
                    nbBerserker++;
                    break;
                case 3:
                    Druide druide = new Druide(new Vector2D(0, 0), "Druide " + nbDruides);
                    ennemies.add(druide);
                    nbDruides++;
                    break;
            }
        }

        // On sauvegarde les statistiques des ennemis
        Evolution.saveStartStats(ennemies);
        //System.out.println("on a sauvegardé les stats au start de la liste d'ennemis suivante : " + this.enemies + "on les affiche");
        // On parcourt la map pour afficher chaque couple clé valeur
        Map<Ennemy, double[]> map = Evolution.startStats;
        for (Map.Entry<Ennemy, double[]> entry : map.entrySet()) {
            Ennemy enemy = entry.getKey();
            double[] values = entry.getValue();

            System.out.println("==========================================");
            System.out.print("Enemy: " + enemy + " -> Values: ");
            for (double value : values) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
        return ennemies;
    }

    public ArrayList<Vector2D> getNewHealerAStar(int nbGiant, int nbBerserker, int nbNinja) {
        ArrayList<Vector2D> aStarHealer = new ArrayList<>();

        int classEnemyMax = Math.max(nbGiant, Math.max(nbBerserker, nbNinja));
        for (Ennemy enemy : enemies) {
            if ((classEnemyMax == nbGiant && enemy instanceof Giant) ||
                    (classEnemyMax == nbNinja && enemy instanceof Ninja) ||
                    (classEnemyMax == nbBerserker && enemy instanceof Berserker)) {
                aStarHealer = new ArrayList<>(enemy.calculerChemin(cases,getStart()));
                break;
            }
        }
        if (aStarHealer.isEmpty() && !enemies.isEmpty()) {
            aStarHealer = enemies.getFirst().calculerChemin(cases,getStart());
        }
        return aStarHealer;
    }

    /**
     * Met à jour le modèle du jeu
     *
     * @param secondes temps ecoule depuis la derniere mise a jour
     */
    @Override
    public void update(double secondes) {
        //Vérification de la fin du jeu
        if (this.nbManches == this.limManches) {
            setLogs("Fin du jeu car le nombre limite de manches a été atteint");
            this.end = true;
            return;
        }
        // Vérification de la fin d'une manche
        if (enemies.isEmpty() && !this.pause) {
            handleEndOfManche();
            return;
        }
        updateCombat(secondes);
        removeDeadEntities();
        for (Ennemy e : enemies) {
            e.setLastAttackCount(e.getLastAttackCount() + 1);
        }
        for (Defense d : defenses) {
            d.setLastAttackCount(d.getLastAttackCount() + 1);
        }
        //on gère le déplacement des ennemis en vérifiant s ils sont arrivés
        updateEnemyPositions();
        notifyObserver();
    }

    private void updateCombat(double secondes) {
        // Gestion de l'attaque des ennemis
        Iterator<Ennemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Ennemy enemy = enemyIterator.next();
            if (enemy instanceof Druide) {
                handleDruideCombat(enemy, secondes);
            } else if (enemy instanceof Berserker) {
                handleBerserkerCombat(enemy, secondes);
            } else {
                handleNormalEnemyCombat(enemy, secondes);
            }
        }

        // Gestion de l'attaque des defenses
        Iterator<Defense> defenseIterator = defenses.iterator();
        while (defenseIterator.hasNext()) {
            Defense defense = defenseIterator.next();
            if (defense instanceof ActiveDefense) {
                handleActiveDefenseCombat((ActiveDefense) defense, secondes);
            } else {
                handlePassiveDefenseCombat((PassiveDefense) defense, secondes);
            }
        }
    }

    private void updateEnemyPositions() {
        Iterator<Ennemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext() && !this.pause) {
            Ennemy enemy = enemyIterator.next();
            if (hasReachedArrival(enemy)) {
                handleEnemyArrival(enemy);
                enemyIterator.remove();
                System.out.println("Liste des ennemis a la fin : " + enemies);
            } else {
                enemy.update();
            }
        }
    }

    private boolean hasReachedArrival(Ennemy enemy) {
        return Math.abs(enemy.getPosition().getX() - XArrivalRender) <= 10 &&
                Math.abs(enemy.getPosition().getY() - YArrivalRender) <= 10 &&
                !enemy.getIsArrived() &&
                !deadEnemies.contains(enemy);
    }

    private void handleEnemyArrival(Ennemy enemy) {
        enemy.setArrived(true);
        nbEnnemiesArrived++;

        enemy.setPosition(new Vector2D(XArrival, YArrival));
        enemy.setSurvivalTime(System.currentTimeMillis() - startTime);

        System.out.println("Nombre d'ennemis arrivés : " + this.nbEnnemiesArrived);
        System.out.println("Le " + enemy.getName() + " est arrivé");
        setLogs("Le " + enemy.getName() + " est arrivé");

        ennemiesArrived.add(enemy);

        if (nbEnnemiesArrived >= nbEnnemiesToWin + 1) {
            setLogs("Fin du jeu car assez d'ennemis ont atteint l'arrivée");
            this.end = true;
        }
    }

    private void removeDeadEntities() {
        //On retire les défenses mortes
        Iterator<Defense> defenseIterator = defenses.iterator();
        while (defenseIterator.hasNext()) {
            Defense d = defenseIterator.next();
            // Si la défense est morte, on la retire de la liste des défenses
            if (d.getIsDead() && !deadDefenses.contains(d)) {
                deadDefenses.add(d);
                defenseIterator.remove();
                towerIsDestroyed();
                setLogs("La défense : " + d.getName() + " à été détruite");
            }
        }
        // On retire les ennemis morts
        Iterator<Ennemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Ennemy e = enemyIterator.next();
            if (e.getIsDead() && !deadEnemies.contains(e)) {
                deadEnemies.add(e);
                //enemies.remove(e);
                enemyIterator.remove();
                System.out.println(e.getName() + " est mort !");
                setLogs(e.getName() + " est mort. Coup dur !");
                System.out.println("Liste des ennemis a la fin : " + enemies);
            }
        }
    }

    private void handleDruideCombat(Ennemy enemy, double secondes) {
        for (Ennemy enemyTarget : enemies) {
            // Tous les ennemis a portée sont soignés
            if (enemy.isInRange(enemyTarget) && !this.getPause()) {
                enemy.healDamage(enemyTarget, enemy.getDamages(), secondes);
            }
        }
    }

    private void handleBerserkerCombat(Ennemy enemy, double secondes) {
        for (Defense defense : defenses) {
            if (enemy.isInRange(defense)) {
                // On l'attaque
                enemy.attack(defense, secondes);
                // le berserker se suicide après avoir attaqué
                enemy.takeDamage(1000);
                // On met à jour le temps de survie
                enemy.setSurvivalTime(System.currentTimeMillis() - startTime);
            }
        }
    }

    private void handleNormalEnemyCombat(Ennemy enemy, double secondes) {
        for (Defense defense : defenses) {
            if (enemy.isInRange(defense)) {
                // On l'attaque
                enemy.attack(defense, secondes);
            }
        }
    }

    private void handleActiveDefenseCombat(ActiveDefense defense, double secondes) {
        Ennemy enemyTarget = defense.getTarget();
        // Si la defense focus déja un ennemi
        if (enemyTarget != null) {
            // On vérifie si l'ennemi est toujours dans la portée de la défense
            if (defense.isInRange(enemyTarget)) {
                // Si l'ennemi n'est pas mort
                if (!enemyTarget.getIsDead()) {
                    // On l'attaque
                    defense.attack(enemyTarget, secondes);
                }
                // Si l'ennemi est mort on set son killerType
                if (enemyTarget.getIsDead() && !deadEnemies.contains(enemyTarget)) {
                    enemyTarget.setKillerType(defense.getType());
                    // On retire la cible de la défense
                    defense.setTarget(null);
                    // On met à jour le temps de survie de l'ennemi
                    enemyTarget.setSurvivalTime(System.currentTimeMillis() - startTime);
                }
            }
            // Si l'ennemi n'est plus dans la portée de la défense
            else {
                // On retire la cible de la défense
                defense.setTarget(null);
            }
        }
        // Si la défense n'a pas de cible
        else {
            // On cherche un ennemi à attaquer
            Ennemy enemy = getCloserEnnemy(defense);
            // Si on a trouvé un ennemi et qu'il est dans la range de la défense
            if (enemy != null && defense.isInRange(enemy)) {
                // On l'attaque
                defense.attack(enemy, secondes);
                // On set la cible de la défense
                defense.setTarget(enemy);
                // Si l'ennemi est mort, on set son killerType
                if (enemy.getIsDead() && !deadEnemies.contains(enemy)) {
                    enemy.setKillerType(defense.getType());
                    // On retire la cible de la défense
                    defense.setTarget(null);
                    // On met à jour le temps de survie de l'enemy
                    enemy.setSurvivalTime(System.currentTimeMillis() - startTime);
                }
            }
        }
    }

    private void handlePassiveDefenseCombat(PassiveDefense defense, double secondes) {
        // On parcourt les ennemis
        Iterator<Ennemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Ennemy e = enemyIterator.next();
            // Si l'enemy est dans la portée de la défense
            if (defense.isInRange(e) && !deadDefenses.contains(defense) && !((PassiveDefense) defense).getAttacked()) {
                // Cela active la defense
                // On attaque l'ennemi
                defense.attack(e, secondes);
                defense.setAttacked(true);
                // Si l'ennemi est mort, on set son killerType
                if (e.getIsDead() && !deadEnemies.contains(e)) {
                    e.setKillerType(defense.getType());
                    // Et on met à jour son temps de survie
                    e.setSurvivalTime(System.currentTimeMillis() - startTime);
                }
                // La défense s'autodétruit après avoir attaqué
                defense.takeDamage(10000);
                setLogs("La défense : " + defense.getType() + " à été détruite");
            }
        }
    }

    private void handleEndOfManche() {
        System.out.println("Fin de la manche " + nbManches);
        this.pause = true;
        // On réactive toutes les défenses passives
        for (Defense defense : defenses) {
            if (defense instanceof PassiveDefense) {
                ((PassiveDefense) defense).setAttacked(false);
            }
        }

        //On ajoute les ennemis de la manche dans une liste
        this.ennemiesEndOfManche.addAll(deadEnemies);
        this.ennemiesEndOfManche.addAll(ennemiesArrived);
        //On ajoute les defenses de la manche dans une liste
        this.defensesEndOfManche.addAll(deadDefenses);
        this.defensesEndOfManche.addAll(defenses);
        //on calcule la distance de chaque ennemi à l'arrivée
        calculateFinalDistances();

        System.out.println("Ennemis en fin de manche : " + ennemiesEndOfManche);
        setLogs("Manche " + nbManches + " terminée");

        //dans le cas ou on est en simulation
        if (this.getSimulation()) {
            handleSimulation();
        }

    }

    private void handleSimulation() {
        if (simulation) {
            //simuler un appui sur le bouton learn
            MouseEvent fakeClickEvent = new MouseEvent(
                    MouseEvent.MOUSE_CLICKED,
                    0, 0,
                    0, 0,
                    MouseButton.PRIMARY,
                    1,
                    false, false, false, false,
                    false, false, false, false,
                    false, false, null
            );
            new ControllerLearn(this).handle(fakeClickEvent);
            new ControllerNextManche(this).handle(fakeClickEvent);
        } /*else if (simulationEvolution) {
            EnnemyEvolutionv2 evolution = new EnnemyEvolutionv2();
            double score = evolution.getScore(ennemiesEndOfManche.get(0));
            this.defenses = this.getDefenseEndOfManche();
            System.out.println("Defenses toutes neuve : " + this.defenses);
            // On sauvegarde les score de l'ennemi dans une map avec l'ennemi comme clé et le score comme valeur
            ennemyScore.put((Giant) ennemiesEndOfManche.get(0), score);
            this.end = true;
        }
        */
    }

    private void calculateFinalDistances() {
        int c = 0;
        for (Ennemy e : ennemiesEndOfManche) {
            char[][] copyGrid = new char[cases.length][];
            for (int i = 0; i < cases.length; i++) {
                copyGrid[i] = cases[i].clone();
                for (int j = 0; j < copyGrid.length; j++) {
                    if (copyGrid[i][j] == 'B' || copyGrid[i][j] == 'C' || copyGrid[i][j] == 'A') {
                        copyGrid[i][j] = '.';
                    }
                }
            }
            int posYReel = (int) Math.ceil(e.getPositionReel().getY());
            int posXReel = (int) Math.ceil(e.getPositionReel().getX());
            if (posYReel < 0) {
                posYReel = 0;
            }
            if (posXReel < 0) {
                posXReel = 0;
            }

            if (posYReel >= copyGrid.length) {
                posYReel = copyGrid.length;
            }
            if (posXReel >= copyGrid.length) {
                posXReel = copyGrid.length;
            }
            if (e.getIsArrived()) {
                e.setDistanceToArrival(new ArrayList<>());
            } else {
                if (copyGrid[posYReel][posXReel] == '#') {
                    int[] newCoord = moveEnemyToClosestValidPoint(copyGrid, posXReel, posYReel);
                    posYReel = newCoord[0];
                    posXReel = newCoord[1];
                }
                copyGrid[posYReel][posXReel] = 'S';

                e.setDistanceToArrival(Astar.getAStar().aStarSearch(copyGrid, this.getLength(), this.getLengthY(),
                        new Vector2D(posYReel, posXReel),
                        new Vector2D(this.getYArrival(), this.getXArrival()), e.getBehaviorString(), true));
            }
            System.out.println("Ennemy " + c + " fin de manche : " + e.getName() + " type:" + e.getType() + " vie" + e.getHealth() + " vitesse :" + e.getSpeed() + " dégâts :" + e.getDamages() + " distance arrivée :" + e.getDistanceToArrival() + " behavior :" + e.getBehaviorString() + "survivalTime : " + e.getSurvivalTime());

            c++;
        }
    }

    @Override
    public void init(Canvas canvas) {
        notifyObserver();//utilité ?
    }

    @Override
    public boolean etreFini() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    @Override
    public void registerObserver(Observer obs) {
        this.observateurs.add(obs);
    }

    @Override
    public void deleteObserver(Observer obs) {
        this.observateurs.remove(obs);
    }

    @Override
    public void notifyObserver() {
        for (Observer obs : this.observateurs) {
            obs.update(this);
        }
    }

    public static int getTailleCase() {
        int largeurDisponible = (screenSize.width / 7) * 6;
        int taskbarSize = 2;
        int tailleCaseHorizontale = largeurDisponible / getLengthY();
        int tailleCaseVerticale = screenSize.height / getLength();
        int tailleCase = Math.min(tailleCaseHorizontale, tailleCaseVerticale);

        // Vérifier les débordements
        if (tailleCase * getLengthY() > screenSize.width || tailleCase * getLength() > screenSize.height) {
            tailleCase /= 2;
        }

        return tailleCase - taskbarSize;
    }

    /**
     * Retourne l'ennemi le plus proche d'une défense
     *
     * @param defense la défense à étudier
     * @return l'ennemi le plus proche
     */
    public Ennemy getCloserEnnemy(ActiveDefense defense) {
        Ennemy closerEnnemy = null;
        double minDistance = Double.MAX_VALUE;
        for (Ennemy ennemy : enemies) {
            double ennemyX = ennemy.getPositionReel().getX();
            double ennemyY = ennemy.getPositionReel().getY();
            Vector2D defensePosition = defense.getPosition();
            double distance = Math.sqrt(Math.pow(ennemyX - defensePosition.getX(), 2)
                    - Math.pow(ennemyY - defensePosition.getY(), 2));
            if (distance < minDistance) {
                minDistance = distance;
                closerEnnemy = ennemy;
            }
        }
        return closerEnnemy;
    }

    public void towerIsDestroyed() {
        System.out.println("La défense  est morte !");

        for (Ennemy ennemy : enemies) {
            char[][] copyGrid = new char[cases.length][];
            for (int i = 0; i < cases.length; i++) {
                copyGrid[i] = cases[i].clone();
            }
            for (Defense defense : deadDefenses) {
                Vector2D position = defense.getPosition();
                copyGrid[(int) position.getY()][(int) position.getX()] = '.';

            }

            if (useAstar) {
                int ennemyPosY = (int) Math.ceil(ennemy.getPositionReel().getY());
                int ennemyPosX = (int) Math.ceil(ennemy.getPositionReel().getX());
                if (ennemyPosY < 0) {
                    ennemyPosY = 0;
                }
                if (ennemyPosX < 0) {
                    ennemyPosX = 0;
                }
                if (ennemyPosX > copyGrid[0].length - 1) {
                    ennemyPosX = copyGrid[0].length - 1;
                }
                if (ennemyPosY > copyGrid.length - 1) {
                    ennemyPosY = copyGrid.length - 1;
                }
                char charCourant = copyGrid[ennemyPosY][ennemyPosX];
                if (charCourant == '#') {
                    int[] newCoord = moveEnemyToClosestValidPoint(copyGrid, ennemyPosX, ennemyPosY);
                    ennemyPosY = newCoord[0];
                    ennemyPosX = newCoord[1];
                }
                copyGrid[ennemyPosY][ennemyPosX] = 'S';
                if (!(ennemyPosY == YArrival && ennemyPosX == XArrival)) {
                    ennemy.resetPathFollowingBehavior(ennemy.calculerChemin(copyGrid, new Vector2D(ennemyPosY,ennemyPosX)));
                }
            }
        }
    }

    public void creerLabyrinthePour1(String fichier, int numIndividu) throws IOException {
        simulationEvolution = true;
        this.limManches = 2;
        //ouvrir le fichier
        FileReader fr = new FileReader(fichier);
        BufferedReader br = new BufferedReader(fr);

        int nbLignes, nbColonnes;
        nbLignes = Integer.parseInt(br.readLine());
        nbColonnes = Integer.parseInt(br.readLine());

        //création du labyrinthe vide
        cases = new char[nbLignes][nbColonnes];

        // Nombre d'ennemis qui doivent arriver à la fin pour gagner
        this.nbEnnemiesToWin = 50;

        //lecture des cases
        String ligne = br.readLine();

        //stocke les indices
        int numLigne = 0;

        //parcours le fichier
        while (ligne != null) {
            for (int colonne = 0; colonne < ligne.length(); colonne++) {
                char c = ligne.charAt(colonne);
                switch (c) {
                    case TREE:
                        //ajouter un mur
                        cases[numLigne][colonne] = TREE;
                        break;
                    case ROAD:
                        // ajouter une route
                        cases[numLigne][colonne] = ROAD;
                        break;
                    case START:
                        //ajouter le point de départ
                        cases[numLigne][colonne] = START;
                        Xstart = colonne;
                        Ystart = numLigne;
                        this.XstartRender = colonne * getTailleCase();
                        this.YstartRender = numLigne * getTailleCase();
                        break;
                    case END:
                        //ajouter le point d'arrivée
                        cases[numLigne][colonne] = END;
                        XArrival = colonne;
                        YArrival = numLigne;
                        XArrivalRender = colonne * getTailleCase();
                        YArrivalRender = numLigne * getTailleCase();
                        break;
                    case CANON:
                        //ajouter un canon
                        cases[numLigne][colonne] = CANON;
                        this.defenses.add(new Canon(colonne, numLigne, "Canon" + this.nbCanon));
                        this.nbCanon++;
                        break;
                    case BOMB:
                        //ajouter une bombe
                        this.cases[numLigne][colonne] = BOMB;
                        this.defenses.add(new Bomb(colonne, numLigne, "Bomb" + this.nbBomb));
                        this.nbBomb++;
                        break;
                    case ARCHER:
                        //ajouter un archer
                        this.cases[numLigne][colonne] = ARCHER;
                        this.defenses.add(new Archer(colonne, numLigne, "Archer" + this.nbArcher));
                        this.nbArcher++;
                        break;
                }
            }
            numLigne++;
            ligne = br.readLine();
        }
        refresh(numIndividu);

        br.close();
    }

    public void refresh(int i) {
        System.out.println("ennemies evolved : " + this.ennemiesEvolved);
        if (this.ennemiesEvolved.isEmpty()) {
            System.out.println("Liste des ennemies : " + this.enemies);
            Giant e = new Giant(new Vector2D(this.XstartRender + Math.random() * 1.5, this.YstartRender + Math.random() * 1.5), "Giant" + i);
            e.calculerChemin(cases,getStart());
            this.enemies.add(e);
        } else {
            Giant e = this.ennemiesEvolved.get(i);
            e.setName(e.getName() + i);
            e.setToStart(this);
            e.calculerChemin(cases,getStart());

            this.enemies.add(e);

        }

        refreshEnnemyArrived();
        refreshDeadEnemies();
        refreshEnnemyEndOfManche();

        refreshDeadDefenses();
        refreshDefenseEndOfManche();

        this.setPause(false);
    }

    public int[] moveEnemyToClosestValidPoint(char[][] grid, int ennemiPosX, int ennemiPosY) {
        List<Integer> direction = Arrays.asList(-1, 0, 1);

        int currentX = ennemiPosX;
        int currentY = ennemiPosY;

        while (currentX == ennemiPosX && currentY == ennemiPosY) {
            for (int i = 0; i < direction.size(); i++) {
                for (int j = 0; j < direction.size(); j++) {
                    int absY = Math.abs(ennemiPosY + direction.get(i));
                    int absX = Math.abs(ennemiPosX + direction.get(j));
                    if (grid[absY][absX] == '.') {
                        ennemiPosY = absY;
                        ennemiPosX = absX;
                    }
                }
            }
            if (currentX == ennemiPosX && currentY == ennemiPosY) {
                direction.addFirst(direction.getFirst() - 1);
                direction.addLast(direction.getLast() + 1);
            }
        }
        return new int[]{ennemiPosY, ennemiPosX};
    }

    public static Dimension getScreenSize() {
        return screenSize;
    }

    public static int getLength() {
        return cases.length;
    }

    public static int getLengthY() {
        return cases[0].length;
    }

    public char getCase(int x, int y) {
        return cases[x][y];
    }

    public static char[][] getCases() {
        return cases;
    }

    public String getLogs() {
        return logs;
    }

    public void setLogs(String log) {
        logs = log;
    }

    public boolean getPause() {
        return this.pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public static double getXstart() {
        return Xstart;
    }

    public static double getYstart() {
        return Ystart;
    }

    public static Vector2D getStart() {
        return new Vector2D(Ystart, Xstart);
    }

    public static double getXArrival() {
        return XArrival;
    }

    public static double getYArrival() {
        return YArrival;
    }

    public int getNbManches() {
        return nbManches;
    }

    public void setNbManches(int nbManches) {
        this.nbManches = nbManches;
    }

    public ArrayList<Ennemy> getEnnemyEndOfManche() {
        return ennemiesEndOfManche;
    }

    public ArrayList<Defense> getDefenseEndOfManche() {
        return defensesEndOfManche;
    }

    /**
     * Pour vider la liste des deadEnemies a chaque nouvelle manche
     **/
    public void refreshDeadEnemies() {
        this.deadEnemies.clear();
    }

    public void refreshDeadDefenses() {
        this.deadDefenses.clear();
    }

    public void refreshEnnemyEndOfManche() {
        this.ennemiesEndOfManche = new ArrayList<>();
    }

    public void refreshDefenseEndOfManche() {
        this.defensesEndOfManche = new ArrayList<>();
    }

    public int getXstartRender() {
        return XstartRender;
    }

    public int getYstartRender() {
        return YstartRender;
    }

    public void refreshEnnemyArrived() {
        this.nbEnnemiesArrived = 0;
        this.ennemiesArrived = new ArrayList<>();
    }

    public static boolean getSimulation() {
        return simulation || simulationEvolution;
    }

    public void setSimulation(boolean b) {
        simulation = b;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public void setStartTime() {
        this.startTime = System.currentTimeMillis();
    }

    public long getEndTime() {
        return this.endTime;
    }

    public void setEndTime() {
        this.endTime = System.currentTimeMillis();
    }

    public HashMap getScore() {
        return ennemyScore;
    }

    public void setEnnemiesEvolved(ArrayList<Giant> evolve) {
        this.ennemiesEvolved = evolve;
    }

    public void refreshEnnemiesScore() {
        this.ennemyScore = new HashMap<>();
    }

    public void setDefenses(ArrayList<Defense> defenseEndOfManche) {
        this.defenses = defenseEndOfManche;
    }

    public boolean getUseAstar() {
        return useAstar;
    }

    public void setUseAstar(boolean useAstar) {
        this.useAstar = useAstar;
    }

    /**
     * Obtient l'instance unique du labyrinthe (pattern Singleton)
     *
     * @return l'instance unique du ModeleLabyrinth
     */
    public static ModeleLabyrinth getLabyrinth() {
        if (instance == null) {
            instance = new ModeleLabyrinth();
        }
        return instance;
    }
}