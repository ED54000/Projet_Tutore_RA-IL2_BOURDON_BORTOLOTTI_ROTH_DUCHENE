# Projet Tutore RA-IL2
## Étudiants
Florian BORTOLOTTI florian.bortolotti1@etu.univ-lorraine.fr \
Tristan ROTH tristan.roth3@etu.univ-lorraine.fr \
Marin BOURDON marin.bourdon2@etu.univ-lorraine.fr \
Eloi DUCHÊNE eloi.duchene8@etu.univ-lorraine.fr 
## Liens utiles
https://trello.com/b/qyPhaAyy/ra-il2-2024-bortolotti-bourdon-roth-duchene

## Informations sur le projet
 - Le projet est réalisé en Java version 21

## Itération 1 
 - Tag : [iteration_1](https://github.com/ED54000/Projet_Tutore_RA-IL2_BOURDON_BORTOLOTTI_ROTH_DUCHENE/releases/tag/It%C3%A9ration_1)
 - Réalisations :
     - Prototype algorithme évolutionnaire 
	 - Passage du diagramme de classe général de draw.io vers un fichier PlantUML
     - Interface graphique
	 - Création des classes Ennemis, Défenses et les autres classes associées
	 - Création de la pop-up de début de partie
	 - Prototype de Steering behaviours avec la Javadoc associée 
	 - Fusion de A* et de Steering behaviours
	 - Courbe de Bézier
	 - Tests de A*
     - Prototype de l'algorithme de A* avec la Javadoc associée

    
## Itération 2 
- Tag : [iteration_2](https://github.com/ED54000/Projet_Tutore_RA-IL2_BOURDON_BORTOLOTTI_ROTH_DUCHENE/releases/tag/It%C3%A9ration_2)
- Réalisations :
  - Fusion de l'interface graphique avec le prototype d'algorithme évolutionnaire
  - Panneau de logs
  - Gestion des manches et apprentissage
  - Sprites
  - Attaque des ennemis
  - Apprentissage et début de l'évolution des agents
  - Fusion de steering behaviours et A* avec l'interface graphique

- L'application permet de : 
	- Lancer une partie en choisissant son labyrinthe, le nombre d'ennemis, le nombre de manches, et le nombre d'ennemis qui passe pour gagner.
	- Observer ensuite le déroulement d'une partie avec des ennemis se déplaçant en fonction de leur comportement et se faisant attaquer par les défenses dans un labyrinthe bien modélisé à l'aide de sprites.
	- Faire évoluer les vagues d'ennemis à la fin de chaque manche et observer les changements effectués jusqu'à la fin du jeu.
 	- L'évolution n'est pas encore au point. Cela génère des ennemis avec un comportement qui peut ne pas être le leur.

 
 ## Itération 3 
 - Tag :[iteration_3](https://github.com/ED54000/Projet_Tutore_RA-IL2_BOURDON_BORTOLOTTI_ROTH_DUCHENE/releases/tag/It%C3%A9ration_3)
- Réalisations : 
	- Evolution logs
	- Simulation 
	- Amélioration des formules de score pour l'évolution
	- Modification de l'évolution : on reproduit les ennemis enfants sur les stats de naissance des parents plutôt que sur les stats de fin de manche.
	- Attaque des ennemis sur les défense
	- Ajout comportement pour éviter les obstacles dans steering behaviors
	- Refactoring de la structure du code des entités (ennemis et défenses)
	- Résolutions de bugs
	- Sprites des ennemis
	- Recalcul du chemin lorsqu'une défense est détruite 

À la fin de cette itération, nous pouvons : 

Lancer une partie en choisissant son labyrinthe, le nombre d'ennemis, le nombre de manches, et le nombre d'ennemies qui doivent passer pour gagner.
Observer ensuite le déroulement d'une partie avec des ennemis se déplaçant en fonction de leur comportement et se faisant attaquer par les défenses dans un labyrinthe bien modélisé à l'aide de sprites. Les ennemis peuvent également attaquer les défenses afin de les détruire. Lorsqu'une défense est détruite, le chemin est recalculé pour chaque agent et donc ils s'adaptent au changements de l'environnement au cours d'une manche. Voir le déroulement de la manche dans les logs (quel ennemi/défense meurt quand, quel ennemi arrive à la fin). 
Faire évoluer les vagues d'ennemis à la fin de chaque manche et observer les changements effectués jusqu'à la fin du jeu.


 ## Itération 4 
  - Tag :[iteration_4](https://github.com/ED54000/Projet_Tutore_RA-IL2_BOURDON_BORTOLOTTI_ROTH_DUCHENE/releases/tag/It%C3%A9ration_4)
- Réalisations : 
	- Evolution logs
	- Evolution par groupes
	- Génération de graphiques
	- Amélioration des formules de score pour l'évolution
	- Modification de l'évolution : on reproduit les ennemis enfants sur les stats de naissance des parents plutôt que sur les stats de fin de manche.
	- Simulation
	- Evolution pour un agent seul
	- Refactoring de la structure du code
	- Ajout comportement pour éviter les obstacles dans steering behaviors
	- Résolution de bugs


À la fin de cette itération, nous pouvons : 

Lancer une partie en choisissant son labyrinthe, le nombre d'ennemis, le nombre de manches, le nombre d'ennemies qui doivent passer pour gagner 
et si A* est utilisé ou non.
Observer ensuite le déroulement d'une partie avec des ennemis se déplaçant en fonction de leur comportement (si A* est utilisé) et se faisant attaquer 
par les défenses dans un labyrinthe bien modélisé à l'aide de sprites. Les ennemis peuvent également attaquer les défenses afin de les détruire. 
Lorsqu'une défense est détruite, si A* est utilisé, le chemin est recalculé pour chaque agent et ils s'adaptent donc aux changements de l'environnement 
au cours d'une manche. Voir le déroulement de la manche dans les logs (quel ennemi/défense meurt quand, quel ennemi arrive à la fin). 
Faire évoluer les groupes d'ennemies à chaque manche pour essayer d'optimiser la composition des groupes afin de gagner la partie.


 ## Itération 5
  - Tag :[iteration_5](https://github.com/ED54000/Projet_Tutore_RA-IL2_BOURDON_BORTOLOTTI_ROTH_DUCHENE/releases/tag/It%C3%A9ration_5)
- Réalisations : 
	- Refonte logs
	- Amélioration fonction de score
	- Génération de graphique à chaque manche (population)
	- Génération de graphique en direct (suivi de la vie de chaque ennemi d'une population)
	- Ajout d'un "mode simple" avec des sprites différents, et un environnement graphique globalement allégé pour mieux comprendre ce qu'il se passe. Activation/désactivation via un bouton grâce à MVC.
	- Ajout d'une ViewLoading qui permet d'afficher une barre de chargement pendant que l'algorithme d'évolution s'exécute (pas encore terminé).
	- Modification de la vitesse du jeu 
	- Ajout de la sélection d'un labyrinthe non répertorié 
	- Ajout d'animation lors d'une touche ou d'un soin
	- Résolution de bugs
	- Ajout d'un arrival behavior


À la fin de cette itération, nous pouvons : 

Lancer une partie en choisissant son labyrinthe, le nombre d'ennemis, le nombre de manches, le nombre d'ennemies qui doivent passer pour gagner 
et si A* est utilisé ou non.
Observer ensuite le déroulement d'une partie avec des ennemis se déplaçant en fonction de leur comportement (si A* est utilisé) et se faisant attaquer 
par les défenses dans un labyrinthe bien modélisé à l'aide de sprites. Les ennemis peuvent également attaquer les défenses afin de les détruire. 
Lorsqu'une défense est détruite, si A* est utilisé, le chemin est recalculé pour chaque agent et ils s'adaptent donc aux changements de l'environnement 
au cours d'une manche. Pouvoir choisir un mode simple qui simplifie l'affichage. Un graphique avec les points de vie de chaque ennemi est affiché en temps réel.
Voir le déroulement de la manche dans les logs (les pv de chaque entité en temps réel, quel ennemi/défense meurt quand, quel ennemi arrive à la fin). 
Faire évoluer les groupes d'ennemies à chaque manche pour essayer d'optimiser la composition des groupes afin de gagner la partie.
Afficher un graphique représantant l'évolution de la composition du groupe d'ennemi au cours des manches.


## Itération 6
  - Tag :[iteration_6](https://github.com/ED54000/Projet_Tutore_RA-IL2_BOURDON_BORTOLOTTI_ROTH_DUCHENE/releases/tag/It%C3%A9ration_6)
- Réalisations : 
	- Amélioration fonction de score
	- Première version de l’évolution de steering behaviors
	- Ajout d’une fenêtre de chargement lors de l’évolution
	- Ajout d’une fenêtre de fin de partie
	- Refonte graphique du menu de démarrage
	- Ajout de configurations de test
    	- Ajout d’une fenêtre séparée pour afficher les graphiques
	- Création d’un bouton "aide" qui donne des informations sur le jeu
	- Résolution de bugs


À la fin de cette itération, nous pouvons : 

Lancer une partie en choisissant son labyrinthe, le nombre d'ennemis, le nombre de manches, le nombre d'ennemies qui doivent passer pour gagner 
et si A* est utilisé ou non.
Observer ensuite le déroulement d'une partie avec des ennemis se déplaçant en fonction de leur comportement (si A* est utilisé) et se faisant attaquer 
par les défenses dans un labyrinthe bien modélisé à l'aide de sprites. Les ennemis peuvent également attaquer les défenses afin de les détruire. 
Lorsqu'une défense est détruite, si A* est utilisé, le chemin est recalculé pour chaque agent et ils s'adaptent donc aux changements de l'environnement 
au cours d'une manche. Pouvoir choisir un mode simple qui simplifie l'affichage. Un graphique avec les points de vie de chaque ennemi est affiché en temps réel.
Voir le déroulement de la manche dans les logs (les pv de chaque entité en temps réel, quel ennemi/défense meurt quand, quel ennemi arrive à la fin). 
Faire évoluer les groupes d'ennemies à chaque manche pour essayer d'optimiser la composition des groupes afin de gagner la partie.
Afficher un graphique représantant l'évolution de la composition du groupe d'ennemi au cours des manches.
Afficher un graphique représantant la vie de chaque ennemi en temps réel au cours d'une manche.
Afficher une fenêtre d'aide donnant des informations sur le jeu.
Choisir parmi différentes configurations préfaites pour pouvoir observer différentes situations d'apprentissage.


## Itération 7
  - Tag :[iteration_7](https://github.com/ED54000/Projet_Tutore_RA-IL2_BOURDON_BORTOLOTTI_ROTH_DUCHENE/releases/tag/It%C3%A9ration_7)
- Réalisations : 

	- Évolution de steering behaviors
	- Ajout de la possibilité de recommencer la partie
	- Modification des sprites des défenses
	- Ajout des sprites des défenses en mode simple
	- Affichage en temps réel du nombre d’ennemis restant pour atteindre l’objectif
	- Ajout d’un graphique pour afficher le nombre d’ennemis qui arrivent
	- Résolution de bugs



À la fin de cette itération, nous pouvons : 

Lancer une partie en choisissant son labyrinthe, le nombre d'ennemis, le nombre de manches, le nombre d'ennemies qui doivent passer pour gagner 
et si A* est utilisé ou non.
Observer ensuite le déroulement d'une partie avec des ennemis se déplaçant en fonction de leur comportement (si A* est utilisé) et se faisant attaquer 
par les défenses dans un labyrinthe bien modélisé à l'aide de sprites. Les ennemis peuvent également attaquer les défenses afin de les détruire. 
Lorsqu'une défense est détruite, si A* est utilisé, le chemin est recalculé pour chaque agent et ils s'adaptent donc aux changements de l'environnement 
au cours d'une manche. Pouvoir choisir un mode simple qui simplifie l'affichage. Un graphique avec les points de vie de chaque ennemi est affiché en temps réel.
Voir le déroulement de la manche dans les logs (les pv de chaque entité en temps réel, quel ennemi/défense meurt quand, quel ennemi arrive à la fin). 
Faire évoluer les groupes d'ennemies à chaque manche pour essayer d'optimiser la composition des groupes afin de gagner la partie.
Afficher un graphique représentant l'évolution de la composition du groupe d'ennemi au cours des manches.
Afficher un graphique représentant la vie de chaque ennemi en temps réel au cours d'une manche.
Afficher une fenêtre d'aide donnant des informations sur le jeu.
Choisir parmi différentes configurations pré faites pour pouvoir observer différentes situations d'apprentissage.
Afficher un graphique représentant le nombre d'ennemis qui arrivent en temps réel au cours d'une manche, ainsi que l'objectif.
Recommencer la partie depuis un écran de fin de partie.
Faire évoluer un ennemi au cours des manches en cherchant à obtenir le meilleur chemin possible, en plaçant des points de passage.
