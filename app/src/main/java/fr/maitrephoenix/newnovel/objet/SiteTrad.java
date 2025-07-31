package fr.maitrephoenix.newnovel.objet;

import java.io.Serializable;
import java.util.ArrayList;

public class SiteTrad implements Serializable {
    private String nom;
    private String lien;
    private String methodeObtention;
    private String texteChargement = "";
    private boolean aTrier = false;

    //contient la liste des liens menant vers les liste de novels du site
    private ArrayList<String> listeLienListeNovel = new ArrayList<String>();
    private ArrayList<SplitIndication> splitIndicationListeNovel = new ArrayList<SplitIndication>();

    private ArrayList<SplitIndication> splitIndicationContenuNovel = new ArrayList<SplitIndication>();

    private ArrayList<SplitIndication> splitIndicationLecture = new ArrayList<SplitIndication>();

    public SiteTrad(String nom, String lien, String methodeObtention, String texteChargement) {
        this.nom = nom;
        this.lien = lien;
        this.methodeObtention = methodeObtention;
        this.texteChargement = texteChargement;
    }

    public SiteTrad(String nom, String lien, String methodeObtention, String texteChargement, boolean aTrier) {
        this.nom = nom;
        this.lien = lien;
        this.methodeObtention = methodeObtention;
        this.texteChargement = texteChargement;
        this.aTrier = aTrier;
    }

    public String getNom() {
        return nom;
    }

    public String getLien() {
        return lien;
    }

    public String getMethodeObtention() {
        return methodeObtention;
    }

    public ArrayList<String> getListeLienListeNovel() {
        return listeLienListeNovel;
    }

    public void addLienListeNovel(String lien){
        listeLienListeNovel.add(lien);
    }

    public ArrayList<SplitIndication> getSplitIndicationListeNovel() {
        return splitIndicationListeNovel;
    }

    public void addSplitIndicationListeNovel(SplitIndication splitIndication) {
        splitIndicationListeNovel.add(splitIndication);
    }

    public ArrayList<SplitIndication> getSplitIndicationContenuNovel() {
        return splitIndicationContenuNovel;
    }

    public void addSplitIndicationContenuNovel(SplitIndication splitIndication) {
        splitIndicationContenuNovel.add(splitIndication);
    }

    public ArrayList<SplitIndication> getSplitIndicationLecture() {
        return splitIndicationLecture;
    }

    public void addSplitIndicationLecture(SplitIndication splitIndication) {
        splitIndicationLecture.add(splitIndication);
    }

    public String getTexteChargement() {
        return texteChargement;
    }

    public boolean isATrier() {
        return aTrier;
    }
}
