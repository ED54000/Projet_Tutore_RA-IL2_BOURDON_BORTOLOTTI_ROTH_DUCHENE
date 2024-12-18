package laby;

import entites.defenses.ActiveDefense;
import entites.defenses.Bomb;
import entites.defenses.Canon;
import entites.defenses.Defense;
import entites.enemies.*;
import javafx.scene.canvas.Canvas;
import laby.controllers.ControllerStart;
import laby.views.ViewLabyrinth;
import moteur.Jeu;
import steering_astar.Steering.BezierCurveFromAStarPoints;
import steering_astar.Steering.PathfollowingBehavior;
import steering_astar.Steering.Vector2D;
import steering_astar.Astar.*;

import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.Iterator;

public class ModeleLabyrinth implements Jeu, Subject {

    public static final char START = 'S';
    public static final char END = 'E';
    public static final char ROAD = '.';
    public static final char TREE = '#';
    public static final char CANON = 'C';
    public static final char BOMB = 'B';
    private final ArrayList<String> BEHAVIOURS = new ArrayList<>(Arrays.asList("Normal", "Fugitive", "Kamikaze" /*,"Healer"*/));

    // Nombre d'ennemis qui doivent arriver à la fin pour gagner
    public int nbEnnemiesToWin;

    public ArrayList<Ennemy> enemies = new ArrayList<>();
    public ArrayList<Defense> defenses = new ArrayList<>();

    public ArrayList<Ennemy> deadEnemies = new ArrayList<>();
    public ArrayList<Defense> deadDefenses = new ArrayList<>();

    //labyrinthe
    private char[][] cases;
    private int Xstart, Ystart, XArrival, YArrival;
    private Map<String, ArrayList<Vector2D>> BehavioursMap = new HashMap<>();
    private ArrayList<Observer> observateurs;

    private String logs = "";
    private int nbEnnemiesArrived;

    private boolean pause = false;

    //constructeur vide
    public ModeleLabyrinth() {
        this.observateurs = new ArrayList<>();
    }

    public void creerLabyrinthe(String fichier, int nbEnnemies, int nbManches, int nbEnnemiesToWin) throws IOException {
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
                        this.Xstart = colonne * ViewLabyrinth.getTailleCase();
                        this.Ystart = numLigne * ViewLabyrinth.getTailleCase();
//                      createEnnemies(nbEnnemies, colonne, numLigne);
                        break;
                    case END:
                        //ajouter le point d'arrivée
                        this.cases[numLigne][colonne] = END;
                        this.XArrival = colonne * ViewLabyrinth.getTailleCase();
                        this.YArrival = numLigne * ViewLabyrinth.getTailleCase();

                        System.out.println("XArrival : " + XArrival + " YArrival : " + YArrival);
                        break;
                    case CANON:
                        //ajouter un canon
                        this.cases[numLigne][colonne] = CANON;
                        this.defenses.add(new Canon(colonne, numLigne));
                        break;
                    case BOMB:
                        //ajouter une bombe
                        this.cases[numLigne][colonne] = BOMB;
                        this.defenses.add(new Bomb(colonne, numLigne));
                        break;
                }
            }
            numLigne++;
            ligne = br.readLine();
        }
        createEnnemies(nbEnnemies);
        br.close();
    }

    private void createEnnemies(int nbEnnemies) {
        Astar astar = new Astar();

        ArrayList<Vector2D> aStarNormal = BezierCurveFromAStarPoints.getBezierCurve(
                astar.aStarSearch(this.getCases(), this.getLength(), this.getLengthY(),
                        new Vector2D(this.getYstart(), this.getXstart()),
                        new Vector2D(this.getYArrival(), this.getXArrival()), BEHAVIOURS.get(0)));
        BehavioursMap.put(BEHAVIOURS.get(0), aStarNormal);
        ArrayList<Vector2D> aStarFugitive = BezierCurveFromAStarPoints.getBezierCurve(
                astar.aStarSearch(this.getCases(), this.getLength(), this.getLengthY(),
                        new Vector2D(this.getYstart(), this.getXstart()),
                        new Vector2D(this.getYArrival(), this.getXArrival()), BEHAVIOURS.get(1)));
        BehavioursMap.put(BEHAVIOURS.get(1), aStarFugitive);
        ArrayList<Vector2D> aStarKamikaze = BezierCurveFromAStarPoints.getBezierCurve(
                astar.aStarSearch(this.getCases(), this.getLength(), this.getLengthY(),
                        new Vector2D(this.getYstart(), this.getXstart()),
                        new Vector2D(this.getYArrival(), this.getXArrival()), BEHAVIOURS.get(2)));
        BehavioursMap.put(BEHAVIOURS.get(2), aStarKamikaze);
        int nbGiant = 0;
        int nbNinja = 0;
        int nbBerserker = 0;
        int nbDruides = 0;
        for (int i = 0; i < nbEnnemies; i++) {
            //crée un ennemi au hasard
            int random = (int) (Math.random() * 4);
            switch (random) {
                case 0:
                    Giant giant = new Giant(new Vector2D(this.getXstart() + Math.random(), this.getYstart() + Math.random()), "Giant "+nbGiant);
                    giant.setBehaviorPath(new PathfollowingBehavior(aStarNormal));
                    this.enemies.add(giant);
                    nbGiant++;
                    break;
                case 1:
                    Ninja ninja = new Ninja(new Vector2D(this.getXstart() + Math.random(), this.getYstart() + Math.random()), "Ninja "+nbNinja);
                    ninja.setBehaviorPath(new PathfollowingBehavior(aStarFugitive));
                    this.enemies.add(ninja);
                    nbNinja++;
                    break;
                case 2:
                    Berserker berserker = new Berserker(new Vector2D(this.getXstart() + Math.random(), this.getYstart() + Math.random()), "Berseker "+nbBerserker);
                    berserker.setBehaviorPath(new PathfollowingBehavior(aStarKamikaze));
                    this.enemies.add(berserker);
                    nbBerserker++;
                    break;
                case 3:
                    nbDruides++;
                    break;
            }
        }
        for (int i = 0; i < nbDruides; i++) {
            Druide druide = new Druide(new Vector2D(this.getXstart() + Math.random(), this.getYstart() + Math.random()), "Druide "+i);
            ArrayList<Vector2D> aStarHealer = null;
            if (nbGiant >= nbBerserker && nbGiant >= nbNinja) {
                aStarHealer = aStarNormal;
            } else if (nbNinja > nbGiant && nbNinja >= nbBerserker) {
                aStarHealer = aStarFugitive;
            } else if (nbBerserker > nbGiant && nbBerserker > nbNinja) {
                aStarHealer = aStarKamikaze;
            }
            druide.setBehaviorPath(new PathfollowingBehavior(aStarHealer));
            this.enemies.add(druide);
        }
    }



    @Override
    public void update(double secondes) {
        // Vérification de la fin d'une manche
        if (enemies.isEmpty() && !this.pause) {
            this.pause = true;
            System.out.println("Manche terminée");
            setLogs("Manche terminée");
            deadEnemies.clear();
            //TODO : lancer la prochaine manche
        }

        // On gère les attaques des défenses
        for (Defense defense : defenses) {
            // Si c'est une défense active
            if (defense instanceof entites.defenses.ActiveDefense) {
                // Si la defense focus déja un ennemi
                if (((ActiveDefense) defense).getTarget() != null) {
                    // On vérifie si l'ennemi est toujours dans la portée de la défense
                    if (defense.isInRange(((ActiveDefense) defense).getTarget())) {
                        // On l'attaque
                        defense.attack(((ActiveDefense) defense).getTarget());
                        setLogs("Attaque de " + defense.getClass() + " sur " + ((ActiveDefense) defense).getTarget() + "pv restants:" + ((ActiveDefense) defense).getTarget().getHealth());
                        // Si l'ennemi est mort, on le retire de la liste des ennemis
                        if (((ActiveDefense) defense).getTarget().isDead() && !deadEnemies.contains(((ActiveDefense) defense).getTarget())) {
                            deadEnemies.add(((ActiveDefense) defense).getTarget());
                            enemies.remove(((ActiveDefense) defense).getTarget());
                            setLogs(((ActiveDefense) defense).getTarget().getName() + " est mort. Coup dur !");
                            // On retire la cible de la défense
                            ((ActiveDefense) defense).setTarget(null);
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
                        defense.attack(ennemy);
                        // On set la cible de la défense
                        ((ActiveDefense) defense).setTarget(ennemy);
                        setLogs("Attaque de " + defense.getClass() + " sur " + ennemy.getName() + "pv restants:" + ennemy.getHealth());
                        // Si l'ennemi est mort, on le retire de la liste des ennemis
                        if (ennemy.isDead() && !deadEnemies.contains(ennemy)) {
                            deadEnemies.add(ennemy);
                            enemies.remove(ennemy);
                            setLogs(ennemy.getName() + " is dead");
                            // On retire la cible de la défense
                            ((ActiveDefense) defense).setTarget(null);
                        }
                    }
                }
            }
            // Si la defense est passive
            else {

            }
        }

        Iterator<Ennemy> iterator = this.enemies.iterator();
        while (iterator.hasNext() && !this.pause) {
            Ennemy ennemy = iterator.next();

            //vérification d'arrivée
            if ((int) (ennemy.getPosition().getX()) == XArrival && (int) (ennemy.getPosition().getY()) == YArrival && !ennemy.isArrived() && !deadEnemies.contains(ennemy)) {
                ennemy.setArrived(true);
                this.nbEnnemiesArrived++;
                setLogs("Le " + ennemy.getName() + " est arrivé");

                if (this.nbEnnemiesArrived == this.enemies.size()) { //changer par le bombre d'ennemies nécessaire pour perdre
                    setLogs("Ta perdu bouuh !");
                }
            }
            //ennemy.move(secondes);
            ennemy.update();

        }
        notifyObserver();

    }

    @Override
    public void init(Canvas canvas) {
        notifyObserver();//utilité ?
    }

    @Override
    public boolean etreFini() {
        return false;
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

    public int getLength() {
        return this.cases.length;
    }

    public int getLengthY() {
        return this.cases[0].length;
    }

    public char getCase(int x, int y) {
        return this.cases[x][y];
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
            double distance = Math.sqrt(Math.pow(ennemy.getPosition().getX() - defense.getPosition().getX(), 2) - Math.pow(ennemy.getPosition().getY() - defense.getPosition().getY(), 2));
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


}