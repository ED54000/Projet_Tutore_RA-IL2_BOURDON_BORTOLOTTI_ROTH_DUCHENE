package laby;

import entites.defenses.*;
import entites.enemies.*;
import evolution.EnnemyEvolution;
import evolution.EnnemyEvolutionv2;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import laby.controllers.ControllerLearn;
import laby.controllers.ControllerNextManche;
import moteur.Jeu;
import steering_astar.Steering.PathfollowingBehavior;
import steering_astar.Steering.Vector2D;
import steering_astar.Astar.*;

import java.awt.*;
import java.io.*;

import java.util.*;

public class ModeleLabyrinth implements Jeu, Subject {

    public static final char START = 'S';
    public static final char END = 'E';
    public static final char ROAD = '.';
    public static final char TREE = '#';
    public static final char CANON = 'C';
    public static final char ARCHER = 'A';
    public static final char BOMB = 'B';
    private final ArrayList<String> BEHAVIOURS = new ArrayList<>(Arrays.asList("Normal", "Fugitive", "Kamikaze" /*,"Healer"*/));

    private int nbManches = 1;
    private int limManches;
    // Nombre d'ennemis qui doivent arriver à la fin pour gagner
    public int nbEnnemiesToWin;
    private int nbEnnemiesArrived;
    private long startTime;


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
    private int nbArcher, nbCanon, nbBomb = 0;


    //si le jeu est avec le main simulation
    private boolean simulation = false;
    private boolean simulationEvolution = false;


    static Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();


    //labyrinthe
    private static char[][] cases;
    private int Xstart, Ystart, XArrival, YArrival, XstartRender, YstartRender, XArrivalRender, YArrivalRender;
    private Map<String, ArrayList<Vector2D>> BehavioursMap = new HashMap<>();
    private ArrayList<Observer> observateurs;

    private String logs = "";

    private boolean pause = false;
    private boolean end = false;
    private Astar astar = new Astar();
    private long endTime;
    private ArrayList<Vector2D> aStarHealer;

    //constructeur vide
    public ModeleLabyrinth() {
        this.observateurs = new ArrayList<>();
        this.nbEnnemiesArrived = 0;
    }

    /**
     * Crée un labyrinthe à partir d'un fichier
     *
     * @param fichier         le fichier contenant le labyrinthe
     * @param ennemies      le nombre d'ennemis
     * @param limManches      le nombre de manches
     * @param nbEnnemiesToWin le nombre d'ennemis à atteindre l'arrivée pour gagner
     * @throws IOException si le fichier n'existe pas
     */
    public void creerLabyrinthe(String fichier, ArrayList<Ennemy> ennemies, int limManches, int nbEnnemiesToWin) throws IOException {
        this.simulation = simulation;
        this.enemies = ennemies;

        this.limManches = limManches;
        //ouvrire le fichier
        FileReader fr = new FileReader(fichier);
        BufferedReader br = new BufferedReader(fr);

        int nbLignes, nbColonnes;
        nbLignes = Integer.parseInt(br.readLine());
        nbColonnes = Integer.parseInt(br.readLine());

        //création du labyrinthe vide
        this.cases = new char[nbLignes][nbColonnes];

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
        //createEnnemies(nbEnnemies);
        setAllEnnemiesStats(this.enemies);

        br.close();
    }

    private void setAllEnnemiesStats(ArrayList<Ennemy> ennemies) {
        createBehaviours(this.getCases());
        for (Ennemy e : ennemies) {
            e.setPositionReel(e.getPosition().divide(ModeleLabyrinth.getTailleCase()));

            e.setToStart(this);
            if (e instanceof Giant){
                e.setBehaviorPath(new PathfollowingBehavior(this.BehavioursMap.get(BEHAVIOURS.get(0))));
                e.setDistanceStartToArrival(this.BehavioursMap.get(BEHAVIOURS.get(0)));
            }
            if (e instanceof Ninja){
                e.setBehaviorPath(new PathfollowingBehavior(this.BehavioursMap.get(BEHAVIOURS.get(1))));
                e.setDistanceStartToArrival(this.BehavioursMap.get(BEHAVIOURS.get(1)));
            }
            if (e instanceof Berserker){
                e.setBehaviorPath(new PathfollowingBehavior(this.BehavioursMap.get(BEHAVIOURS.get(2))));
                e.setDistanceStartToArrival(this.BehavioursMap.get(BEHAVIOURS.get(2)));
            }
            if (e instanceof Druide){
                e.setBehaviorPath(new PathfollowingBehavior(aStarHealer));
                e.setDistanceStartToArrival(aStarHealer);
            }
        }
    }

    public ArrayList<Ennemy> createEnnemies(int nbEnnemies) {
        ArrayList<Ennemy> ennemies = new ArrayList<>();
        //createBehaviours(this.getCases());

        int nbGiant = 1;
        int nbNinja = 1;
        int nbBerserker = 1;
        int nbDruides = 1;

        for (int i = 0; i < nbEnnemies; i++) {
            //crée un ennemi au hasard
            int random = (int) (Math.random() * 4);
            switch (random) {
                case 0:
                    Giant giant = new Giant(new Vector2D(0, 0), "Giant " + nbGiant);
                    //giant.setBehaviorPath(new PathfollowingBehavior(this.BehavioursMap.get(BEHAVIOURS.get(0))));
                    //giant.setDistanceStartToArrival(this.BehavioursMap.get(BEHAVIOURS.get(0)));
                    ennemies.add(giant);
                    nbGiant++;
                    break;
                case 1:
                    Ninja ninja = new Ninja(new Vector2D(0,0), "Ninja " + nbNinja);
                    //ninja.setBehaviorPath(new PathfollowingBehavior(this.BehavioursMap.get(BEHAVIOURS.get(1))));
                    //ninja.setDistanceStartToArrival(this.BehavioursMap.get(BEHAVIOURS.get(1)));
                    ennemies.add(ninja);
                    nbNinja++;
                    break;
                case 2:
                    Berserker berserker = new Berserker(new Vector2D(0, 0), "Berseker " + nbBerserker);
                    //berserker.setBehaviorPath(new PathfollowingBehavior(this.BehavioursMap.get(BEHAVIOURS.get(2))));
                    //berserker.setDistanceStartToArrival(this.BehavioursMap.get(BEHAVIOURS.get(2)));
                    ennemies.add(berserker);
                    nbBerserker++;
                    break;
                case 3:
                    nbDruides++;
                    break;
            }
        }
        for (int i = 1; i < nbDruides; i++) {
            Druide druide = new Druide(new Vector2D(0, 0), "Druide " + i);
            //ArrayList<Vector2D> aStarHealer = getNewHealerAStar(nbDruides, nbGiant, nbBerserker, nbNinja);
            this.aStarHealer = getNewHealerAStar(nbDruides, nbGiant, nbBerserker, nbNinja);
            //druide.setBehaviorPath(new PathfollowingBehavior(aStarHealer));
            //druide.setDistanceStartToArrival(aStarHealer);
            ennemies.add(druide);
        }

        // On sauvegarde les statistiques des ennemis
        EnnemyEvolution.saveStartStats(ennemies);
        System.out.println("on a sauvegardé les stats au start de la liste d'ennemis suivante : " + this.enemies + "on les affiche");
        // On parcourt la map pour afficher chaque couple clé valeur
        Map<Ennemy, double[]> map = EnnemyEvolution.startStats;
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

    public ArrayList<Vector2D> getNewHealerAStar(int nbDruides, int nbGiant, int nbBerserker, int nbNinja) {
        ArrayList<Vector2D> aStarHealer = null;

        if (nbGiant >= nbBerserker && nbGiant >= nbNinja) {
            aStarHealer = this.BehavioursMap.get(BEHAVIOURS.getFirst());
        } else if (nbNinja > nbGiant && nbNinja >= nbBerserker) {
            aStarHealer = this.BehavioursMap.get(BEHAVIOURS.get(1));
        } else if (nbBerserker > nbGiant && nbBerserker > nbNinja) {
            aStarHealer = this.BehavioursMap.get(BEHAVIOURS.get(2));
        }

        return aStarHealer;
    }

    public void createBehaviours(char[][] grid) {
        ArrayList<Vector2D> aStarNormal =
                astar.aStarSearch(grid, this.getLength(), this.getLengthY(),
                        new Vector2D(this.getYstart(), this.getXstart()),
                        new Vector2D(this.getYArrival(), this.getXArrival()), BEHAVIOURS.get(0), false);
        BehavioursMap.put(BEHAVIOURS.get(0), aStarNormal);
        ArrayList<Vector2D> aStarFugitive =
                astar.aStarSearch(grid, this.getLength(), this.getLengthY(),
                        new Vector2D(this.getYstart(), this.getXstart()),
                        new Vector2D(this.getYArrival(), this.getXArrival()), BEHAVIOURS.get(1), false);
        BehavioursMap.put(BEHAVIOURS.get(1), aStarFugitive);
        ArrayList<Vector2D> aStarKamikaze =
                astar.aStarSearch(grid, this.getLength(), this.getLengthY(),
                        new Vector2D(this.getYstart(), this.getXstart()),
                        new Vector2D(this.getYArrival(), this.getXArrival()), BEHAVIOURS.get(2), false);
        BehavioursMap.put(BEHAVIOURS.get(2), aStarKamikaze);
    }


    /**
     * Met à jour le modèle du jeu
     *
     * @param secondes temps ecoule depuis la derniere mise a jour
     */
    @Override
    public void update(double secondes) {

        //System.out.println(this.enemies);
        //Vérification de la fin du jeu
        if (this.nbManches == this.limManches) {
            setLogs("Fin du jeu car le nombre limite de manches a été atteint");
            this.end = true;
            return;
        }

        // Vérification de la fin d'une manche
        if (enemies.isEmpty() && !this.pause) {
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

            //   System.out.println("Defenses morte : " + deadDefenses);
            //   System.out.println("Defenses tout court : " + defenses);

            //on calcule la distance de chaque ennemi à l'arrivée
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
                if (e.isArrived()) {
                    e.setDistanceToArrival(new ArrayList<>());
                } else {
                    copyGrid[posYReel][posXReel] = 'S';
                    e.setDistanceToArrival(astar.aStarSearch(copyGrid, this.getLength(), this.getLengthY(),
                            new Vector2D(posYReel, posXReel),
                            new Vector2D(this.getYArrival(), this.getXArrival()), e.getBehavior(), true));
                }
                System.out.println("Ennemy " + c + " fin de manche : " + e.getName() + " type:" + e.getType() + " vie" + e.getHealth() + " vitesse :" + e.getSpeed() + " dégâts :" + e.getDamages() + " distance arrivée :" + e.getDistanceToArrival() + " behavior :" + e.getBehavior() + "survivalTime : " + e.getSurvivalTime());

                c++;
            }
            System.out.println("ennemis en fin de manche : " + ennemiesEndOfManche);
            setLogs("Manche " + nbManches + " terminée");

            //dans le cas ou on est en simulation
            if (this.simulation) {
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
            }

            if (this.simulationEvolution) {
                EnnemyEvolutionv2 evolution = new EnnemyEvolutionv2();
                double score = evolution.getScore(ennemiesEndOfManche.get(0));
                this.defenses = this.getDefenseEndOfManche();
                System.out.println("Defenses toutes neuve : " + this.defenses);
                // On sauvegarde les score de l'ennemi dans une map avec l'ennemi comme clé et le score comme valeur
                ennemyScore.put((Giant) ennemiesEndOfManche.get(0), score);
                this.end = true;
            }
        }

        // On gère les attaques des ennemis
        for (Ennemy ennemi : enemies) {
            // Si l'ennemi est un druide
            if (ennemi instanceof Druide) {
                // On vérifie pour chaque ennemi si il est a portée du druide
                for (Ennemy ennemiTarget : enemies) {
                    // Tous les ennemis a portée sont soignés
                    if (ennemi.isInRange(ennemiTarget) && !this.isPause()) {
                        ennemi.healDamage(ennemiTarget, ennemi.getDamages(), secondes);
                    }
                }
            } else if (ennemi instanceof Berserker) {
                // On vérifie si une défense est dans la portée de l'ennemi
                for (Defense defense : defenses) {
                    if (ennemi.isInRange(defense)) {
                        // On l'attaque
                        ennemi.attack(defense, secondes);
                        // le berserker se suicide après avoir attaqué
                        ennemi.takeDamage(1000);
                        // On met à jour le temps de survie
                        ennemi.setSurvivalTime(System.currentTimeMillis() - startTime);
                    }
                }
            }
            // Sinon
            else {
                // On vérifie si une défense est dans la portée de l'ennemi
                for (Defense defense : defenses) {
                    if (ennemi.isInRange(defense)) {
                        // On l'attaque
                        ennemi.attack(defense, secondes);
                    }
                }
            }
        }

        // On gère les attaques des défenses
        for (Defense defense : defenses) {
            // Si c'est une défense active
            if (defense instanceof ActiveDefense) {
                Ennemy ennemyTarget = ((ActiveDefense) defense).getTarget();
                // Si la defense focus déja un ennemi
                if (ennemyTarget != null) {
                    // On vérifie si l'ennemi est toujours dans la portée de la défense
                    if (defense.isInRange(ennemyTarget)) {
                        // Si l'ennemi n'est pas mort
                        if (!ennemyTarget.isDead()) {
                            // On l'attaque
                            defense.attack(ennemyTarget, secondes);
                        }
                        // Si l'ennemi est mort on set son killerType
                        if (ennemyTarget.isDead() && !deadEnemies.contains(ennemyTarget)) {
                            ennemyTarget.setKillerType(defense.getType());
                            // On retire la cible de la défense
                            ((ActiveDefense) defense).setTarget(null);
                            // On met à jour le temps de survie de l'ennemi
                            ennemyTarget.setSurvivalTime(System.currentTimeMillis() - startTime);
                        }
                    }
                    // Si l'ennemi n'est plus dans la portée de la défense
                    else {
                        // On retire la cible de la défense
                        ((ActiveDefense) defense).setTarget(null);
                    }
                }
                // Si la défense n'a pas de cible
                else {
                    // On cherche un ennemi à attaquer
                    Ennemy ennemy = getCloserEnnemy((ActiveDefense) defense);
                    // Si on a trouvé un ennemi et qu'il est dans la range de la défense
                    if (ennemy != null && defense.isInRange(ennemy)) {
                        // On l'attaque
                        defense.attack(ennemy, secondes);
                        // On set la cible de la défense
                        ((ActiveDefense) defense).setTarget(ennemy);
                        // Si l'ennemi est mort, on set son killerType
                        if (ennemy.isDead() && !deadEnemies.contains(ennemy)) {
                            ennemy.setKillerType(defense.getType());
                            // On retire la cible de la défense
                            ((ActiveDefense) defense).setTarget(null);
                            // On met à jour le temps de survie de l'ennemi
                            ennemy.setSurvivalTime(System.currentTimeMillis() - startTime);
                        }
                    }
                }
            }
            // Si la defense est passive
            else {
                // On parcourt les ennemis
                for (Ennemy e : enemies) {
                    // Si l'ennemi est dans la portée de la défense
                    if (defense.isInRange(e) && !deadDefenses.contains(defense) && !((PassiveDefense) defense).isAttacked()) {
                        // Cela active la defense
                        // On attaque l'ennemi
                        defense.attack(e, secondes);
                        ((PassiveDefense) defense).setAttacked(true);
                        // Si l'ennemi est mort, on set son killerType
                        if (e.isDead() && !deadEnemies.contains(e)) {
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
        }

        //On retire les défenses mortes
        for (Defense d : defenses) {
            // Si la défense est morte, on la retire de la liste des défenses
            if (d.isDead() && !deadDefenses.contains(d)) {
                deadDefenses.add(d);
                towerIsDestroyed();
                setLogs("La défense : " + d.getName() + " à été détruite");
            }
        }
        defenses.removeAll(deadDefenses);
        // On retire les ennemis morts
        ArrayList<Ennemy> enemiesDead = new ArrayList<>();
        for (Ennemy e : enemies) {
            if (e.isDead() && !deadEnemies.contains(e)) {
                deadEnemies.add(e);
                enemiesDead.add(e);
                setLogs(e.getName() + " est mort. Coup dur !");
            }
        }
        enemies.removeAll(enemiesDead);

        for (Ennemy e : enemies) {
            e.setLastAttackCount(e.getLastAttackCount() + 1);
        }

        for (Defense d : defenses) {
            d.setLastAttackCount(d.getLastAttackCount() + 1);
        }

        //on gère le déplacement des ennemis en vérifiant si ils sont arrivés
        ArrayList<Ennemy> enemiesToRemove = new ArrayList<>();
        Iterator<Ennemy> iterator = this.enemies.iterator();
        while (iterator.hasNext() && !this.pause) {
            Ennemy ennemy = iterator.next();
            //vérification d'arrivée
            if ((int) (ennemy.getPosition().getX()) >= XArrivalRender - 10 && (int) (ennemy.getPosition().getX()) <= XArrivalRender + 10 &&
                    (int) (ennemy.getPosition().getY()) >= YArrivalRender - 10 && (int) (ennemy.getPosition().getY()) <= YArrivalRender + 10 &&
                    !ennemy.isArrived() && !deadEnemies.contains(ennemy)
            ) {
                ennemy.setArrived(true);
                this.nbEnnemiesArrived++;
                System.out.println("Nombre d'ennemis arrivés : " + this.nbEnnemiesArrived);
                System.out.println("Le " + ennemy.getName() + " est arrivé");
                System.out.println("Liste des ennemis a la fin : " + enemies);

                setLogs("Le " + ennemy.getName() + " est arrivé");
                //si les ennemis ont gagné
                if (this.nbEnnemiesArrived == this.nbEnnemiesToWin + 1) {
                    setLogs("Fin du jeu car assez d'ennemis ont atteint l'arrivée");
                    this.end = true;
                }
                // On met à jour le temps de survie de l'ennemi
                ennemy.setSurvivalTime(System.currentTimeMillis() - startTime);
                enemiesToRemove.add(ennemy);
                ennemiesArrived.add(ennemy);
            }
            ennemy.update();
            notifyObserver();
        }
        this.enemies.removeAll(enemiesToRemove);
        notifyObserver();
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

    public char[][] getCases() {
        return this.cases;
    }

    public void setLogs(String log) {
        logs = log;
    }

    public String getLogs() {
        return logs;
    }

    public boolean isPause() {
        return this.pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
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

    public double getXstart() {
        return Xstart;
    }

    public double getYstart() {
        return Ystart;
    }

    public double getXArrival() {
        return XArrival;
    }

    public double getYArrival() {
        return YArrival;
    }

    public ArrayList<String> getBehaviours() {
        return BEHAVIOURS;
    }

    public Map<String, ArrayList<Vector2D>> getBehavioursMap() {
        return BehavioursMap;
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

    public void setSimulation(boolean b) {
        this.simulation = b;
    }

    public boolean estSimulation() {
        if (this.simulation || this.simulationEvolution) {
            return true;
        }
        return false;
    }

    public void towerIsDestroyed() {
        System.out.println("La défense  est morte !");
        char[][] copyGrid = new char[cases.length][];
        for (int i = 0; i < cases.length; i++) {
            copyGrid[i] = cases[i].clone();
        }
        for (Defense defense : deadDefenses) {
            Vector2D position = defense.getPosition();
            copyGrid[(int) position.getY()][(int) position.getX()] = '.';

        }
        for (Ennemy ennemy : enemies) {
            int ennemiPosY = (int) Math.ceil(ennemy.getPositionReel().getY());
            int ennemyPosX = (int) Math.ceil(ennemy.getPositionReel().getX());
            if (ennemiPosY < 0) {
                ennemiPosY = 0;
            }
            if (ennemyPosX < 0) {
                ennemyPosX = 0;
            }

            copyGrid[ennemiPosY][ennemyPosX] = 'S';
            if (!(ennemiPosY == YArrival && ennemyPosX == XArrival)) {
                Astar newAstar = new Astar();
                ArrayList<Vector2D> path = newAstar.aStarSearch(copyGrid, copyGrid.length, copyGrid[0].length,
                        new Vector2D(ennemiPosY, ennemyPosX),
                        new Vector2D(this.getYArrival(), this.getXArrival()), ennemy.getBehavior(), false);
                ennemy.setBehaviorPath(new PathfollowingBehavior(path));
                BehavioursMap.put(ennemy.getBehavior(), path);
            }
        }
    }

    public void setStartTime() {
        this.startTime = System.currentTimeMillis();
    }

    public long getStartTime() {
        return this.startTime;
    }

    public void setEndTime() {
        this.endTime = System.currentTimeMillis();
    }

    public long getEndTime() {
        return this.endTime;
    }

    public void creerLabyrinthePour1(String fichier, int numIndividu) throws IOException {
        this.simulationEvolution = true;
        this.limManches = 2;
        //ouvrir le fichier
        FileReader fr = new FileReader(fichier);
        BufferedReader br = new BufferedReader(fr);

        int nbLignes, nbColonnes;
        nbLignes = Integer.parseInt(br.readLine());
        nbColonnes = Integer.parseInt(br.readLine());

        //création du labyrinthe vide
        this.cases = new char[nbLignes][nbColonnes];

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
        refresh(numIndividu);

        br.close();
    }

    public HashMap getScore() {
        return ennemyScore;
    }

    public void refresh(int i) {
        createBehaviours(this.getCases());
        System.out.println("ennemies evolved : " + this.ennemiesEvolved);
        if (this.ennemiesEvolved.isEmpty()) {
            System.out.println("Liste des ennemies : " + this.enemies);
            Giant e = new Giant(new Vector2D(this.XstartRender + Math.random() * 1.5, this.YstartRender + Math.random() * 1.5), "Giant" + i);
            e.setBehaviorPath(new PathfollowingBehavior(this.BehavioursMap.get(BEHAVIOURS.get(0))));
            e.setDistanceStartToArrival(this.BehavioursMap.get(BEHAVIOURS.get(0)));
            this.enemies.add(e);
        } else {
            Giant e = this.ennemiesEvolved.get(i);
            e.setName(e.getName() + i);
            e.setToStart(this);

            e.setBehaviorPath(new PathfollowingBehavior(this.BehavioursMap.get(e.getBehavior())));
            e.setDistanceStartToArrival(this.BehavioursMap.get(e.getBehavior()));

            this.enemies.add(e);

        }

        refreshEnnemyArrived();
        refreshDeadEnemies();
        refreshEnnemyEndOfManche();

        refreshDeadDefenses();
        refreshDefenseEndOfManche();

        this.setPause(false);
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
}
