package fr.maitrephoenix.newnovel.objet;

import java.io.Serializable;

public class Chapitre implements Serializable {
    String lien;
    String titre;
    int numero;
    String page;
    Novel novel;

    public Chapitre(String lien, String titre, Novel novel, int numero) {
        this.lien = lien;
        this.titre = titre;
        this.novel = novel;
        this.numero = numero;
    }

    public String getLien() {
        return lien;
    }

    public String getTitre() {
        return titre;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getNumero() {
        return numero;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPage() {
        return page;
    }

    public Novel getNovel() {
        return novel;
    }
}
