package com.example.chemin_interface.laby;

import com.example.chemin_interface.entites.defenses.Bomb;
import com.example.chemin_interface.entites.defenses.Canon;
import com.example.chemin_interface.entites.defenses.Defense;
import com.example.chemin_interface.entites.enemies.*;
import com.example.chemin_interface.steering_astar.Astar.Astar;
import com.example.chemin_interface.steering_astar.Steering.BezierCurveFromAStarPoints;
import com.example.chemin_interface.steering_astar.Steering.PathfollowingBehavior;
import com.example.chemin_interface.steering_astar.Steering.Vector2D;
import javafx.scene.canvas.Canvas;
import com.example.chemin_interface.moteur.Jeu;

import java.io.*;
import java.util.*;

public class ModeleLabyrinth implements Jeu, Subject {

    public static final char START = 'S';
    public static final char END = 'E';
    public static final char ROAD = '.';
    public static final char TREE = '#';
    public static final char CANON = 'C';
    public static final char BOMB = 'B';
    private final ArrayList<String> BEHAVIOURS = new ArrayList<>(Arrays.asList("Normal", "Fugitive", "Kamikaze" /*,"Healer"*/));


    public ArrayList<Ennemy> enemies = new ArrayList<>();
    public ArrayList<Defense> defenses = new ArrayList<>();

    //labyrinthe
    private char[][] cases;
    private double Xstart, Ystart, Xend, Yend;

    private Map<String, ArrayList<Vector2D>> BehavioursMap = new HashMap<>() ;

    private ArrayList<Observer> observateurs;

    //constructeur vide
    public ModeleLabyrinth() {
        this.observateurs = new ArrayList<>();
    }

    public void creerLabyrinthe(String fichier, int nbEnnemies, int nbManches) throws IOException {
        //ouvrire le fichier
        FileReader fr = new FileReader(fichier);
        BufferedReader br = new BufferedReader(fr);

        int nbLignes, nbColonnes;
        nbLignes = Integer.parseInt(br.readLine());
        nbColonnes = Integer.parseInt(br.readLine());

        //création du labyrinthe vide
        this.cases = new char[nbLignes][nbColonnes];

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
                        System.out.println(numLigne+ " : "+ colonne);
                        this.cases[numLigne][colonne] = START;
                        this.Xstart = colonne;
                        this.Ystart = numLigne;
                        System.out.println("Départ : " + Xstart +","+ Ystart);
                        //createEnnemies(nbEnnemies, colonne, numLigne);
                        break;
                    case END:
                        //ajouter le point d'arrivée
                        this.cases[numLigne][colonne] = END;
                        this.Xend = colonne;
                        this.Yend = numLigne;
                        break;
                    case CANON:
                        //ajouter un canon
                        this.cases[numLigne][colonne] = CANON;
                        this.defenses.add(new Canon(new Vector2D(colonne, numLigne)));
                        break;
                    case BOMB:
                        //ajouter une bombe
                        this.cases[numLigne][colonne] = BOMB;
                        this.defenses.add(new Bomb(new Vector2D(colonne, numLigne)));
                        break;
                }
            }
            numLigne++;
            ligne = br.readLine();
        }
        createEnnemies(nbEnnemies);
        br.close();
    }

    private void createEnnemies(int nbEnnemies) { // colonne, numLigne
        Astar astar = new Astar();

        ArrayList<Vector2D> aStarNormal = BezierCurveFromAStarPoints.getBezierCurve(
                astar.aStarSearch(this.getCases(), this.getLength(), this.getLengthY(),
                        new Vector2D(this.getYstart(), this.getXstart()),
                        new Vector2D(this.getYend(), this.getXend()), BEHAVIOURS.get(0)));
        BehavioursMap.put(BEHAVIOURS.get(0), aStarNormal);
        ArrayList<Vector2D> aStarFugitive = BezierCurveFromAStarPoints.getBezierCurve(
                astar.aStarSearch(this.getCases(), this.getLength(), this.getLengthY(),
                        new Vector2D(this.getYstart(), this.getXstart()),
                        new Vector2D(this.getYend(), this.getXend()), BEHAVIOURS.get(1)));
        BehavioursMap.put(BEHAVIOURS.get(1), aStarFugitive);
        ArrayList<Vector2D> aStarKamikaze = BezierCurveFromAStarPoints.getBezierCurve(
                astar.aStarSearch(this.getCases(), this.getLength(), this.getLengthY(),
                        new Vector2D(this.getYstart(), this.getXstart()),
                        new Vector2D(this.getYend(), this.getXend()), BEHAVIOURS.get(2)));
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
                    Giant giant = new Giant(new Vector2D(this.getXstart() + Math.random(), this.getYstart() + Math.random()));
                    giant.setBehavior(new PathfollowingBehavior(aStarNormal));
                    this.enemies.add(giant);
                    nbGiant++;
                    break;
                case 1:
                    Ninja ninja = new Ninja(new Vector2D(this.getXstart() + Math.random(), this.getYstart() + Math.random() ));
                    ninja.setBehavior(new PathfollowingBehavior(aStarFugitive));
                    this.enemies.add(ninja);
                    nbNinja++;
                    break;
                case 2 :
                    Berserker berserker = new Berserker(new Vector2D(this.getXstart() + Math.random(), this.getYstart() + Math.random()));
                    berserker.setBehavior(new PathfollowingBehavior(aStarKamikaze));
                    this.enemies.add(berserker);
                    nbBerserker++;
                    break;
                case 3 :
                    nbDruides++;
                    break;
            }
        }
        for (int i = 0; i < nbDruides; i++) {
            Druide druide = new Druide(new Vector2D(this.getXstart() + Math.random(), this.getYstart() + Math.random()));
            ArrayList<Vector2D> aStarHealer = null;
            if(nbGiant > nbBerserker && nbGiant > nbNinja){
                aStarHealer = aStarNormal;
            } else if(nbNinja > nbGiant && nbNinja > nbBerserker){
                aStarHealer = aStarFugitive;
            } else if(nbBerserker > nbGiant && nbBerserker > nbNinja){
                aStarHealer = aStarKamikaze;
            }
            druide.setBehavior(new PathfollowingBehavior(aStarHealer));
            this.enemies.add(druide);
        }
    }

    @Override
    public void update(double secondes) {
        for (Ennemy ennemy : this.enemies) {
            //si l'ennemi est sur la case de départ
            if (ennemy.getDistanceToArrival() == ennemy.getDistanceStartToArrival()) {

            }
            while (ennemy.getTimeSpawn() != 0) {
                ennemy.setTimeSpawn(ennemy.getTimeSpawn() - 1);
            }
//            ennemy.move(secondes);
            ennemy.update();
            notifyObserver();
        }
    }

    @Override
    public void init(Canvas canvas) {
        notifyObserver(); //utilité ?
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

    public double getXstart() {
        return Xstart;
    }

    public double getYstart() {
        return Ystart;
    }

    public double getXend() {
        return Xend;
    }

    public double getYend() {
        return Yend;
    }

    public ArrayList<String> getBehaviours() {
        return BEHAVIOURS;
    }

    public Map<String,ArrayList<Vector2D>> getBehavioursMap(){
        return BehavioursMap;
    }
}