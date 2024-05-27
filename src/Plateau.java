public class Plateau {
    /**
     * Tableau de cases
     */
    public Case cases[][];

    /**
     * Valeur d'une case interdite (les cases aux bords)
     */
    public static final int INT = -2;

    /**
     * Valeur d'une case contenant une bombe
     */
    public static final int BOMBE = -1;

    public Plateau() {
        // On alloue l'espace pour un tableau contenant deux colonnes et deux lignes
        // supplémentaires. Cela simplifie la recherche de voisins d'une case
        this.cases = new Case[PartieMain.TAILLE_TABLEAU + 2][PartieMain.TAILLE_TABLEAU + 2];

        // On initialise chaque case
        for (int x = 0; x < PartieMain.TAILLE_TABLEAU + 2; x++) {
            for (int y = 0; y < PartieMain.TAILLE_TABLEAU + 2; y++) {
                this.cases[x][y] = new Case();
            }
        }

        // On donne la valeur interdite aux cases du bord
        for (int i = 0; i < PartieMain.TAILLE_TABLEAU + 2; i++) {
            cases[0][i].contenu = INT;
            cases[PartieMain.TAILLE_TABLEAU + 1][i].contenu = INT;
            cases[i][0].contenu = INT;
            cases[i][PartieMain.TAILLE_TABLEAU + 1].contenu = INT;
        }
    }

    /**
     * Révèle une case
     * 
     * @param x L'abscisse de la case
     * @param y L'ordonnée de la case
     */
    public void reveler(int x, int y) {
        this.cases[x][y].reveler();
    }

    /**
     * Calcule la distance entre deux cases avec une norme un peu bizarre
     * 
     * @param x1 Abscisse de la première case
     * @param y1 Ordonnée de la première case
     * @param x2 Abscisse de la deuxième case
     * @param y2 Ordonnée de la deuxième case
     * @return La distance entre les deux cases
     */
    static int distance(int x1, int y1, int x2, int y2) {
        return Math.max(Math.abs(x1 - x2), Math.abs(y2 - y1));
    }

    /**
     * Remplit le plateau en laissant un espace vide autour de la première position
     * 
     * @param x L'abscisse de la position
     * @param y L'ordonnée de la position
     */
    public void remplir(int x, int y) {
        int compteur_bombes = 0;

        // On compte le nombre de cases disponibles pour voir si on peut placer toutes
        // les bombes
        int cases_disponibles = 0;

        for (int x_d = 1; x_d < PartieMain.TAILLE_TABLEAU + 1; x_d++) {
            for (int y_d = 1; y_d < PartieMain.TAILLE_TABLEAU + 1; y_d++) {
                if (distance(x_d, y_d, x, y) > 2) {
                    cases_disponibles++;
                }
            }
        }

        // Si il n'y a pas assez de place, on quitte la partie
        if (cases_disponibles < PartieMain.NOMBRE_BOMBES) {
            System.out.println("Il n'y a pas assez de place");
            System.exit(1);
        }

        // On ajoute des bombes suffisamment loin de la case cliquée
        while (compteur_bombes < PartieMain.NOMBRE_BOMBES) {
            int x_b = (int) (Math.random() * (PartieMain.TAILLE_TABLEAU));
            int y_b = (int) (Math.random() * (PartieMain.TAILLE_TABLEAU));

            // On demande que la distance soit bien supérieure à deux cases, et qu'il n'y
            // ait rien dessus
            if ((distance(x_b + 1, y_b + 1, x, y) > 2) & (this.cases[x_b + 1][y_b + 1].contenu != BOMBE)) {

                this.cases[x_b + 1][y_b + 1].contenu = BOMBE;
                compteur_bombes++;
            }
        }

        // Maintenant que le plateau a été généré, on remplit pour chaque case le nombre
        // de bombes dans son voisinage
        for (int x_c = 1; x_c < PartieMain.TAILLE_TABLEAU + 1; x_c++) {
            for (int y_c = 1; y_c < PartieMain.TAILLE_TABLEAU + 1; y_c++) {
                if (this.cases[x_c][y_c].contenu != BOMBE) {
                    int compteur = 0; // Nombre de bombes dans le voisinage de la case

                    // Exploration des cases adjacentes
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            // Si on n'est ni sur une bombe ni sur un bord
                            if (((dx != 0) | (dy != 0)) & this.cases[x_c + dx][y_c + dy].contenu != INT) {

                                if (this.cases[x_c + dx][y_c + dy].contenu == BOMBE) {
                                    compteur++;
                                }

                            }
                        }
                    }
                    this.cases[x_c][y_c].contenu = compteur;
                }
            }
        }

        // Une fois que chaque case a été définie, on explore le plateau autour de la
        // position de départ
        explorer(x, y);
    }

    /**
     * Explore récursivement le plateau à partir d'une position de départ
     * 
     * @param x L'abscisse de la position
     * @param y L'ordonnée de la position
     */
    public void explorer(int x, int y) {
        // Si il s'agit d'une des cases au bord ou une case déjà explorée, on ne fait
        // rien
        if (cases[x][y].exploree | cases[x][y].contenu == INT) {
            return;
        }

        // On a exploré la case
        cases[x][y].exploree = true;
        cases[x][y].reveler();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {

                if (((dy != 0) | (dx != 0)) & (cases[x + dx][y + dy].contenu != INT)) {
                    cases[x + dx][y + dy].reveler();

                    // On continue à explorer uniquement si la case ne contient pas de bombes à
                    // proximité
                    if (cases[x + dx][y + dy].contenu == 0) {
                        explorer(x + dx, y + dy);
                    }
                }

            }
        }
    }

    /**
     * Affiche le plateau dans la console
     */
    public void print_board() {
        for (int x = 0; x < PartieMain.TAILLE_TABLEAU + 2; x++) {
            for (int y = 0; y < PartieMain.TAILLE_TABLEAU + 2; y++) {
                System.out.printf("%3d", this.cases[y][x].contenu);
            }
            System.out.println();
        }
    }

    /**
     * Calcul de l'état de la partie
     * 
     * @return 0 si la partie est perdue, 1 si elle est en cours, 2 si elle est
     *         gagnée
     */
    public int state() {
        int nombre_defusees_correctes = 0;

        for (int x = 1; x < PartieMain.TAILLE_TABLEAU + 1; x++) {
            for (int y = 1; y < PartieMain.TAILLE_TABLEAU + 1; y++) {

                if (this.cases[x][y].contenu == BOMBE) {
                    // Si une bombe a été affichée et non défussée, la partie est perdue
                    if (this.cases[x][y].visible) {
                        return 0;
                    }

                    // Si elle a été défusée, on l'ajoute aux bombes devinées correctement
                    if (this.cases[x][y].defusee) {
                        nombre_defusees_correctes++;
                    }
                }

            }
        }

        // Si on a défusé le bon nombre de bombes, la partie est gagnée
        if (nombre_defusees_correctes == PartieMain.NOMBRE_BOMBES) {
            return 2;
        }

        // Si aucune des conditions n'est vérifiée, la partie est en cours
        return 1;
    }
}
