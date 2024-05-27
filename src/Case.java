public class Case {
    /**
     * True si la case a été révélée
     */
    public boolean visible;

    /**
     * Informe sur le contenu de la case:
     * -2 si il s'agit d'un bord
     * -1 si il y a une bombe
     * Le nombre de bombes adjacentes sinon
     */
    public int contenu;

    /**
     * True si la case a déjà été explorée (pas nécessairement visible).
     * Par exemple, une bombe peut avoir été explorée par le programme sans avoir
     * été dévoilée. En effet, lorsque l'on clique dans une zone vide, toutes les
     * cases adjacentes qui ne contiennent pas de bombe ou n'en sont pas à proximité
     * sont explorées et affichées, sauf les bombes.
     */
    public boolean exploree = false;

    /**
     * True si la case a été défusée (qu'il y ait une bombe ou pas)
     */
    public boolean defusee = false;

    /**
     * Crée une case invisible sans contenu
     */
    public Case() {
        this.visible = false;
        this.exploree = false;
        this.defusee = false;
        this.contenu = 0;
    }

    /**
     * Révèle la case pour l'affichage
     */
    public void reveler() {
        this.visible = true;
    }

    /**
     * Inverse l'état de défusage
     */
    public void defuser() {
        this.defusee = !this.defusee;
    }
}
