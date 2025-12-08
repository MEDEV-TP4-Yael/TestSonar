/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.projet.jeudedame;

import java.util.ArrayList;

/**
 *
 * @author vadimdigonnet
 */
public class Plateau {
    ArrayList<Pion> maListePionBlanc = new ArrayList<>();
    ArrayList<Pion> maListePionNoir = new ArrayList<>();

    
    
    public void creerPlateau(){
        for (int i = 0; i<=10;i++){
            for(int j=0;j<=4;j++){
                if (i%2==0 && j%2==0){
                    maListePionBlanc.add(new Pion(new Point2D(i,j),"blanc",this));
                }else if(i%2==1 && j%2==1){
                    maListePionBlanc.add(new Pion(new Point2D(i,j),"blanc",this));
                }        
                
            }
        }
        for (int i = 0; i<=10;i++){
            for(int j=6;j<=9;j++){
                if (i%2==0 && j%2==0){
                    maListePionNoir.add(new Pion(new Point2D(i,j),"blanc",this));
                }else if(i%2==1 && j%2==1){
                    maListePionNoir.add(new Pion(new Point2D(i,j),"blanc",this));
                }        
                
            }
        }
    }
    
    public void affichePlateau(){
        String[][] plateau = new String[50][50];
        Point2D a = new Point2D();
        for (int i=0;i<50;i++){
            for( int j=0; j<50;j++){
                plateau[i][j]=". ";
            }
        }
        for (Pion p : maListePionBlanc){
            int x =p.getPosition().getX();
            int y =p.getPosition().getY();
            plateau[x][y] = "B ";    
        }
        for (Pion p : maListePionNoir){
            int x =p.getPosition().getX();
            int y =p.getPosition().getY();
            plateau[x][y] = "N ";
        }
    }

    public ArrayList<Pion> getMaListePionBlanc() {
        return maListePionBlanc;
    }

    public void setMaListePionBlanc(ArrayList<Pion> maListePionBlanc) {
        this.maListePionBlanc = maListePionBlanc;
    }

    public ArrayList<Pion> getMaListePionNoir() {
        return maListePionNoir;
    }

    public void setMaListePionNoir(ArrayList<Pion> maListePionNoir) {
        this.maListePionNoir = maListePionNoir;
    }
    
    public boolean estDansLePlateau(int x, int y) {
    return x >= 0 && x < 8 && y >= 0 && y < 8;
}

   public boolean caseVide(int x, int y) {
    if (!estDansLePlateau(x, y)) {
        return false;
    }
    // Vérifie qu'aucun pion blanc n'est sur cette case
    for (Pion p : getMaListePionBlanc()) {
        if (p.getPosition().getX() == x && p.getPosition().getY() == y) {
            return false;
        }
    }

    // Vérifie qu'aucun pion noir n'est sur cette case
    for (Pion p : getMaListePionNoir()) {
        if (p.getPosition().getX() == x && p.getPosition().getY() == y) {
            return false;
        }
    }
    return true; 
}
    
}
