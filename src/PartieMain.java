import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PartieMain {

    public static final int TAILLE_TABLEAU = 20; // Nombre de cases par côté
    public static final int TAILLE_CASE = 40; // Taille de chaque case (en pixels)
    public static final int NOMBRE_BOMBES = 30; // Nombre de bombes à déminer
    public static final int Y_OFFSET = 37; // Offset Y pour affichage

    public static Plateau board; // Plateau de jeu
    public static int nb_clicks = 0; // Nombre de clicks effectués

    // Les couleurs utilisée pour l'affichage
    public static final Color GREEN = new Color(102, 204, 0);
    public static final Color LIGHT_GREEN = new Color(178, 255, 102);
    public static final Color MARRON = new Color(255, 204, 153);
    public static final Color LIGHT_MARRON = new Color(255, 229, 204);

    public static void main(String[] argv) throws Exception {
        // Création d'une fenêtre
        MyFrame fenetre = new MyFrame();

        // Quitter le programme à la fermeture de la fenêtre
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Détermination de la taille de la fenêtre
        fenetre.setSize(TAILLE_TABLEAU * TAILLE_CASE, TAILLE_TABLEAU * TAILLE_CASE + Y_OFFSET);

        // Afficher la fenêtre au centre de l'écran
        fenetre.setLocationRelativeTo(null);

        // Affichage de la fenêtre
        fenetre.setVisible(true);

        // Création d'un système pour gérer les inputs à la souris
        fenetre.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

                // Coordonnées de la case correspondant au click
                int X = e.getX() / TAILLE_CASE + 1;
                int Y = (e.getY() - Y_OFFSET) / TAILLE_CASE + 1;
                nb_clicks++;

                if (e.getButton() == MouseEvent.BUTTON1) {
                    // Click gauche

                    // Dans le cas ou il s'agit du premier click, on essaye de créer un grand espace
                    // pour faciliter le jeu
                    if (nb_clicks == 1) {
                        // On calcule le contenu de la zone une fois qu'on connaît la position du
                        // premier clic
                        board.remplir(X, Y);
                    } else {
                        if (board.cases[X][Y].contenu == 0) {
                            // Si il n'y a rien, on explore à partir de la case
                            board.explorer(X, Y);
                        } else if (!board.cases[X][Y].defusee) {
                            // Si il y a une bombe, on la révèle
                            board.reveler(X, Y);
                        }
                    }
                }

                if (e.getButton() == MouseEvent.BUTTON3) {
                    // Click droit
                    if (nb_clicks == 1) {
                        // Cela n'a pas de sens de défuser un plateau de jeu vide
                        nb_clicks--;
                    } else if (!board.cases[X][Y].visible) {
                        board.cases[X][Y].defuser();
                    }
                }

                fenetre.repaint();
            }

            // Il est obligatoire d'override toutes les méthodes
            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }
        });

        // Création du plateau de jeu
        board = new Plateau();
    }

    static void paint_board(Graphics2D g) {
        for (int x = 0; x < TAILLE_TABLEAU; x++) {
            for (int y = 0; y < TAILLE_TABLEAU; y++) {

                // Si la case a été défusée, on l'affiche en bleu
                if (PartieMain.board.cases[x + 1][y + 1].defusee) {
                    g.setColor(Color.BLUE);
                    g.fillRect(x * TAILLE_CASE, y * TAILLE_CASE + Y_OFFSET, TAILLE_CASE, TAILLE_CASE);
                    continue;
                }

                if (PartieMain.board.cases[x + 1][y + 1].visible == false) {
                    // Si une case n'est pas visible, on affiche de l'herbe verte

                    if ((x + y) % 2 == 1) {
                        g.setColor(GREEN);
                    } else {
                        g.setColor(LIGHT_GREEN);
                    }

                } else {

                    // Sinon, on affiche de la terre marron
                    if ((x + y) % 2 == 1) {
                        g.setColor(MARRON);
                    } else {
                        g.setColor(LIGHT_MARRON);
                    }

                    // Si il y a une bombe, on l'affiche en rouge
                    if (PartieMain.board.cases[x + 1][y + 1].contenu == Plateau.BOMBE) {
                        g.setColor(Color.RED);
                    }

                }

                g.fillRect(x * TAILLE_CASE, y * TAILLE_CASE + Y_OFFSET, TAILLE_CASE, TAILLE_CASE);

                // Si la case est visible et qu'elle est au voisinage d'une bombe, on affiche le
                // nombre de bombes autour d'elle
                if ((PartieMain.board.cases[x + 1][y + 1].contenu != 0)
                        & (PartieMain.board.cases[x + 1][y + 1].visible == true)) {
                    g.setColor(Color.BLACK);
                    g.drawString(Integer.toString(PartieMain.board.cases[x + 1][y + 1].contenu),
                            (int) ((x + 0.5) * TAILLE_CASE), (int) ((y + 0.5) * TAILLE_CASE) + Y_OFFSET);
                }
            }
        }

        // Calcul de l'état de la partie
        int etat = PartieMain.board.state();

        if (etat == 0) {
            System.out.println("C'est perdu");
        } else if (etat == 2) {
            System.out.println("C'est gagné");
        }

        // Si la partie n'est pas en cours, on arrête le jeu
        if (etat != 1) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            System.exit(0);
        }

    }

    static class MyFrame extends JFrame {
        public void paint(Graphics g1) {
            Graphics2D g = (Graphics2D) g1;
            PartieMain.paint_board(g);
        }
    }
}
