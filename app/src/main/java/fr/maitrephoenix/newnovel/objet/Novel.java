package fr.maitrephoenix.newnovel.objet;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

import fr.maitrephoenix.newnovel.ListeSiteTrad;

public class Novel implements Serializable, Comparable<Novel> {
    private String nom = "";
    private String synopsis = "";
    private String classification = "";
    private String auteur = "";
    private String imageLien = "";
    private Bitmap imageBitmap = null;
    private String lien = "";
    private String siteTrad = "";
    private ArrayList<Chapitre> listeChapitre = new ArrayList<>();

    public Novel(String nom, String lien, String siteTrad) {
        this.nom = nom;
        this.lien = lien;
        this.siteTrad = siteTrad;
    }

    public String getNom() {
        return nom;
    }

    public String getImageLien() {
        return imageLien;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public String getLien() {
        return lien;
    }

    public SiteTrad getSiteTrad() {
        return ListeSiteTrad.getUnSiteTrad(siteTrad);
    }

    public String getNomSiteTrad() {
        return siteTrad;
    }

    public void setImageLien(String image) {
        this.imageLien = image;
    }

    public void setImageBitmap(Bitmap image) {
        this.imageBitmap = image;
    }

    public ArrayList<Chapitre> getListeChapitres() {
        return listeChapitre;
    }

    public int getChapitrePosition(int numero){
        for(int i = 0; i < listeChapitre.size(); i++){
            if(listeChapitre.get(i).getNumero() == numero){
                return i;
            }
        }
        return -1;
    }

    public void addChapitre(Chapitre chapitre) {
        listeChapitre.add(chapitre);
    }

    public boolean chapitreExiste(int numero){
        for(Chapitre chapitre : listeChapitre){
            if(chapitre.getNumero() == numero){
                return true;
            }
        }
        return false;
    }

    @Override
    public int compareTo(Novel novel) {
        return nom.compareTo(novel.getNom());
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getAuteur() {
        return auteur;
    }

    public void addAuteur(String auteur) {
        if(this.auteur.equals("")){
            this.auteur = auteur;
        }
        else{
            this.auteur += ", " + auteur;
        }
    }
}
