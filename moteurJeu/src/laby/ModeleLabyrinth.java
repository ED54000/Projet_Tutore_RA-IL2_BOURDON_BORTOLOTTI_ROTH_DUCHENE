package laby;

import entites.defenses.Bomb;
import entites.defenses.Canon;
import entites.defenses.Defense;
import entites.enemies.Ennemy;
import entites.enemies.Giant;
import javafx.scene.canvas.Canvas;
import laby.controllers.ControllerStart;
import laby.views.ViewLabyrinth;
import moteur.Jeu;

import java.io.*;
import java.util.ArrayList;

public class ModeleLabyrinth implements Jeu, Subject {

    public static final char START = 'S';
    public static final char END = 'E';
    public static final char ROAD = '.';
    public static final char TREE = '#';
    public static final char CANON = 'C';
    public static final char BOMB = 'B';

    public ArrayList<Ennemy> enemies = new ArrayList<>();
    public ArrayList<Defense> defenses = new ArrayList<>();

    //labyrinthe
    private char[][] cases;

    private ArrayList<Observer> observateurs;

    //constructeur vide
    public ModeleLabyrinth() {
        this.observateurs = new ArrayList<>();
    }

    public void creerLabyrinthe(String fichier, int nbEnnemies, int nbManches) throws IOException {
        this.observateurs = new ArrayList<>();
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
        System.out.println(enemies.size());
        br.close();
    }

    private void createEnnemies(int nbEnnemies, int colonne, int numLigne) {
        for (int i = 0; i < nbEnnemies; i++) {
           //crée un ennemi au hasard
            int random = (int) (Math.random() * 2);
            switch (random) {
                case 0:
                    Giant giant = new Giant(colonne, numLigne);
                    this.enemies.add(giant);
                    break;
                case 1:
                    Giant giant1 = new Giant(colonne, numLigne);
                    this.enemies.add(giant1);
                    break;
            }
        }
    }

    /*public ModeleLabyrinth(String fichier) throws IOException {
        this.observateurs = new ArrayList<>();
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
                        Giant giant = new Giant(colonne, numLigne);
                        this.enemies.add(giant);
                        break;
                    case END:
                        //ajouter le point d'arrivée
                        this.cases[numLigne][colonne] = END;
                        break;
                    case CANON:
                        //ajouter un canon
                        this.cases[numLigne][colonne] = CANON;
                        //this.defenses.add(new Canon(colonne, numLigne));
                        break;
                    case BOMB:
                        //ajouter une bombe
                        this.cases[numLigne][colonne] = BOMB;
                        //this.defenses.add(new Bombe(colonne, numLigne));
                        break;
                }
            }
            numLigne++;
            ligne = br.readLine();
        }
        br.close();
    }

     */

    @Override
    public void update(double secondes) {
        for (Ennemy ennemy : this.enemies) {
            ennemy.update(secondes);
        }
        notifyObserver();
    }

    @Override
    public void init(Canvas canvas) {
        //création des controleurs*
        ControllerStart controllerStart = new ControllerStart(this);
        // création des vues
        ViewLabyrinth viewLabyrinth = new ViewLabyrinth(this, canvas);

        registerObserver(viewLabyrinth);

        notifyObserver();
        System.out.println("init");
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
}