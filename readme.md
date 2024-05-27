# Démineur

## Comment jouer

Pour jouer au démineur, il suffit de lancer le `main` de `PartieMain.java`. Celui-ci ouvre une fenêtre contenant une partie vierge. Un premier clic gauche fera apparaître les cases, en étant certain qu'on a pas cliqué sur une bombre. Ensuite, il suffit d'un click droit pour défuser une bombe. La case s'affichera alors en bleu. La partie s'arrête lorsque toutes les bombes ont été correctement défusées, ou lorsqu'une bombe a explosé (pas le droit à l'erreur, c'est la guerre).

## Fonctionnement du code

### PartieMain.java

Le fichier `PartieMain.java` contient surtout des instructions liées à l'affichage et aux commandes de jeu, notamment la détection de clics. A chaque fois que le programme retrace la zone de jeu, il évalue également son état pour voir si la partie est perdue ou gagnée.

### Plateau.java

Le fichier `Plateau.java` définit la classe `Plateau`, contenant notamment un tableau d'objets de la classe `Case`. Le fichier définit donc le fonctionnement du jeu. La création du plateau se fait en prenant en compte les bordures, il fait donc deux cases de plus en longueur et en largeur, mais cela simplifie le code en évitant la détection de bord. Ces cases contiennent donc la valeur `INT` pour interdit.

Le remplissage du tableau ne se fait qu'une fois le premier click sur une case détecté. Cela permet de laisser un peu de place au joueur, plutôt que de risquer de tomber d'entrée de jeu sur une bombe ou sur une case individuelle. Une fois le premier clic enregistré, on disperse aléatoirement des bombes à plus de deux cases de la position. Pour finir, on assigne le nombre de bombes voisines à chaque case.

Le coeur du jeu se situe dans la fonction récursive `explorer`. Celle-ci prend en entrée une position de départ (abscisse ordonnée), et révèle la case correspondante si elle n'a pas déjà été explorée. Ensuite, le processus se répète avec les cases voisines, tant qu'elles sont vides. L'exploration s'arrête quand on est arrivé aux bords du plateau ou que toutes les cases explorées contiennent une valeur autre que 0.

### Case.java

Le fichier `Case.java` est très simple et définit le contenu d'une case individuelle (nombres de bombes à proximité), ainsi que son état (visible, explorée ou défusée).
