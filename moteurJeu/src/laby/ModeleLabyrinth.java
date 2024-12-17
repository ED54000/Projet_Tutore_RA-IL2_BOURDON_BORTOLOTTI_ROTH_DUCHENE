package laby;

import entites.defenses.Bomb;
import entites.defenses.Canon;
import entites.defenses.Defense;
import entites.enemies.Ennemy;
import entites.enemies.Giant;
import entites.enemies.Ninja;
import javafx.scene.canvas.Canvas;
import laby.controllers.ControllerStart;
import laby.views.ViewLabyrinth;
import moteur.Jeu;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class ModeleLabyrinth implements Jeu, Subject {

    public static final char START = 'S';
    public static final char END = 'E';
    public static final char ROAD = '.';
    public static final char TREE = '#';
    public static final char CANON = 'C';
    public static final char BOMB = 'B';

    // Nombre d'ennemis qui doivent arriver à la fin pour gagner
    public int nbEnnemiesToWin;

    public ArrayList<Ennemy> enemies = new ArrayList<>();
    public ArrayList<Defense> defenses = new ArrayList<>();

    public ArrayList<Ennemy> deadEnemies = new ArrayList<>();
    public ArrayList<Defense> deadDefenses = new ArrayList<>();

    //labyrinthe
    private char[][] cases;
    private int XArrival, YArrival;

    private ArrayList<Observer> observateurs;

    private String logs = "";
    private int nbEnnemiesArrived;

    private int nbManches;

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
                switch (c){
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
                        createEnnemies(nbEnnemies, colonne, numLigne);
                        break;
                    case END:
                        //ajouter le point d'arrivée
                        this.cases[numLigne][colonne] = END;
                        this.XArrival = colonne;
                        this.YArrival = numLigne;
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
        br.close();
    }

    private void createEnnemies(int nbEnnemies, int colonne, int numLigne) {
        for (int i = 0; i < nbEnnemies; i++) {
           //crée un ennemi au hasard
            int random = (int) (Math.random() * 2);
            switch (random) {
                case 0:
                    Giant giant = new Giant(colonne+Math.random(), numLigne+Math.random(), "Giant"+i);
                    this.enemies.add(giant);
                    break;
                case 1:
                    Ninja ninja = new Ninja(colonne+Math.random(), numLigne+Math.random(), "Ninja"+i);
                    this.enemies.add(ninja);
                    break;
            }
        }
        this.enemies = new ArrayList<>(); //a enlever

    }

    @Override
    public void update(double secondes) {
        Iterator<Ennemy> iterator = this.enemies.iterator();

        // Vérification de la fin d'une manche
        if (enemies.isEmpty()) {
            System.out.println("Manche terminée");
            setLogs("Manche terminée");
            deadEnemies.clear();
            //TODO : lancer la prochaine manche
        }

        while (iterator.hasNext()) {
            Ennemy ennemy = iterator.next();
            ennemy.move(secondes);

            if (ennemy.isDead() && !deadEnemies.contains(ennemy)) {
                deadEnemies.add(ennemy);
                enemies.remove(ennemy);
                iterator.remove();
                setLogs(ennemy.getName() + " is dead");
                continue;
            }

            // Vérification d'arriver
            if ((int)(ennemy.getX()) == XArrival && (int)(ennemy.getY()) == YArrival && !ennemy.isArrived()) {
                ennemy.setArrived(true);
                this.nbEnnemiesArrived++;
                setLogs("Le " + ennemy.getName() + " est arrivé");

                if (this.nbEnnemiesArrived == this.enemies.size()) { //changer par le bombre d'ennemies nécessaire pour perdre
                    setLogs("Ta perdu bouuh !");
                }
            }
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

    public void setLogs(String val){
        logs = val;
    }

    public String getLogs(){
        return logs;
    }
}