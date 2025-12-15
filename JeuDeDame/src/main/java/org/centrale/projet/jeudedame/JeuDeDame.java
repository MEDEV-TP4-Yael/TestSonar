/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package org.centrale.projet.jeudedame;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class JeuDeDame {

    Logger logger = Logger.getLogger(getClass().getName());

    private Plateau plateau;
    private String joueurCourant; // "Blanc" ou "Noir"
    private static final String blanc = "Blanc"; 
    private static final String noir = "Noir";

    public JeuDeDame() {
        this.plateau = new Plateau();
        demarrerNouvellePartie();
    }

    public void demarrerNouvellePartie() {
        plateau.vider();

        // Damier 10x10 : lignes 0..9, colonnes 0..9
        // Pions noirs en haut : lignes 0 à 3
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 10; x++) {
                if ((x + y) % 2 == 1) { // cases foncées
                    Pion p = new Pion(new Point2D(x, y), noir, plateau);
                    plateau.getMaListePionNoir().add(p);
                }
            }
        }

        // Pions blancs en bas : lignes 6 à 9
        for (int y = 6; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                if ((x + y) % 2 == 1) {
                    Pion p = new Pion(new Point2D(x, y), blanc, plateau);
                    plateau.getMaListePionBlanc().add(p);
                }
            }
        }

        joueurCourant = blanc; 
    }

    public String getJoueurCourant() {
        return joueurCourant;
    }

    private void changerJoueur() {
        if (blanc.equals(joueurCourant)) {
            joueurCourant = noir;
        } else {
            joueurCourant = blanc;
        }
    }

    // ---------- Pions qui doivent manger ----------
    /**
     * Retourne la liste des pions du joueur courant qui ont au moins une prise
     * possible.
     */
    public List<Pion> getPionsQuiDoiventManger() {
    List<Pion> resultat = new ArrayList<>();

    List<Pion> listePions;
    if (blanc.equals(joueurCourant)) {
        listePions = plateau.getMaListePionBlanc();
    } else {
        listePions = plateau.getMaListePionNoir();
    }

    for (Pion p : listePions) {
        if (p.doitManger()) {   // ⭐ maintenant doitManger() renvoie boolean
            resultat.add(p);
        }
    }

    return resultat;
}


    public boolean jouerCoup(Point2D origine, Point2D destination) {
    Pion pion = plateau.getPion(origine);
    logger.info("Test pion = " + pion);
    
    if (pion == null) {
        logger.info("Pas de pion sur la case d'origine");
        return false;
    }

    if (!pion.getCouleur().equals(joueurCourant)) {
        logger.info("Ce n'est pas le tour de cette couleur");
        return false;
    }

    List<Pion> pionsObliges = getPionsQuiDoiventManger();
    boolean doitManger = !pionsObliges.isEmpty();
    logger.info("il doit = " + doitManger);

    int dx = destination.getX() - origine.getX();
    int dy = destination.getY() - origine.getY();

    boolean mouvementSimple = isMouvementSimple(dx,dy);
    boolean mouvementPrise  = isMouvementPrise(dx,dy);

    // 4) S'il doit manger, on refuse les mouvements simples
    if (doitManger && !mouvementPrise) {
        logger.info("Un pion doit manger, déplacement simple interdit");
        return false;
    }

    // 5) Destination doit être dans le plateau et vide
    if (!plateau.estDansLePlateau(destination.getX(), destination.getY())
            || !plateau.caseVide(destination.getX(), destination.getY())) {
        logger.info("Destination hors plateau ou occupée");
        return false;
    }

    // 6) Mouvement simple (sans prise)
    if (mouvementSimple) {
        if (!doitManger) {
            // deplace(dx, dy) est un déplacement RELATIF
            boolean ok = pion.deplace(dx, dy);
            if (ok) {
                changerJoueur();
                logger.info("Déplacement simple effectué");
                return true;
            } else {
                logger.info("Déplacement simple refusé par deplace()");
                return false;
            }
        } else {
            return false;
        }
    }

    // 7) Mouvement de prise
    if (mouvementPrise) {
        int xInter = (origine.getX() + destination.getX()) / 2;
        int yInter = (origine.getY() + destination.getY()) / 2;
        Point2D posInter = new Point2D(xInter, yInter);

        Pion pionAdverse = plateau.getPion(posInter);
        if (pionAdverse == null) {
            logger.info("Aucun pion à manger sur la case intermédiaire");
            return false;
        }
        if (pionAdverse.getCouleur().equals(joueurCourant)) {
            logger.info("Le pion intermédiaire est de la même couleur");
            return false;
        }

        // Déplacement du pion (relatif)
        boolean ok = pion.deplace(dx, dy);
        if (!ok) {
            logger.info("Le déplacement de prise a échoué");
            return false;
        }

        // Retirer le pion mangé
        plateau.enleverPion(pionAdverse);

        // 8) Enchaînement de prises possible ?
        if (pion.doitManger()) {  
            logger.info("Le même pion peut encore manger");
            // même joueur rejoue ce pion
            return true;
        } else {
            changerJoueur();
            return true;
        }
    }

    // 9) Sinon, mouvement illégal
    logger.info("Mouvement ni simple ni de prise");
    return false;
}



    // ---------- Fin de partie ----------
    /**
     * Fin de partie si un des joueurs n'a plus de pions ou ne peut plus jouer.
     */
    public boolean estFinDePartie() {
        boolean blancSansPion = plateau.getMaListePionBlanc().isEmpty();
        boolean noirSansPion = plateau.getMaListePionNoir().isEmpty();

        return blancSansPion || noirSansPion
    }

    public Plateau getPlateau() {
        return plateau;
    }
    
    public boolean isMouvementSimple(int dx, int dy){
    return Math.abs(dx) == 1 && Math.abs(dy) == 1;
    }
    
    public boolean isMouvementPrise(int dx,int dy){
    return Math.abs(dx) == 2 && Math.abs(dy) == 2;}
    
}
