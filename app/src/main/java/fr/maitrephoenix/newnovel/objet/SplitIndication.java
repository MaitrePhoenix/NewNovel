package fr.maitrephoenix.newnovel.objet;

import java.io.Serializable;
import java.util.ArrayList;

public class SplitIndication implements Serializable {
    private String text;
    private ArrayList<String> textMultiple = new ArrayList<String>();
    private boolean possibleMultiple;
    //si 0 alors on prend la partie avant, si [x] alors on prend la partie [x] et les parties après et si -1 on conserve tout
    private int partieAConserver;
    //indique si la partie 0 est un élément utile à récupérer
    private String indication;
    //ne sert que si liste = true
    private ArrayList<SplitIndication> ListeSousSplit = new ArrayList<SplitIndication>();
    private boolean allowNothingFound = false;


    public SplitIndication(String text, int partieAConserver, String indication) {
        this.text = text;
        this.partieAConserver = partieAConserver;
        this.indication = indication;
        this.possibleMultiple = false;
    }

    public SplitIndication(String text, int partieAConserver, String indication, boolean allowNothingFound) {
        this.text = text;
        this.partieAConserver = partieAConserver;
        this.indication = indication;
        this.possibleMultiple = false;
        this.allowNothingFound = allowNothingFound;
    }

    public SplitIndication(ArrayList<String> textMultiple, int partieAConserver, String indication) {
        this.textMultiple = textMultiple;
        //si -1 on conserve tout  dans une liste, si 0 conserve l'élément avent text, si 1 on conserve tout après text
        this.partieAConserver = partieAConserver;
        //indique si la partie avant text est à conserver
        this.indication = indication;
        this.possibleMultiple = true;
    }

    public String getText() {
        return text;
    }

    public int getpartieAConserver() {
        return partieAConserver;
    }

    public String getIndication() {
        return indication;
    }

    public ArrayList<SplitIndication> getListeSousSplit() {
        return ListeSousSplit;
    }

    public void addSousSplit(SplitIndication splitIndication) {
        ListeSousSplit.add(splitIndication);
    }

    public boolean getPossibleMultiple() {
        return possibleMultiple;
    }

    public ArrayList<String> getTextMultiple() {
        return textMultiple;
    }

    public boolean getAUneListe() {
        return(this.getListeSousSplit().size() > 0);
    }

    public boolean isAllowNothingFound() {
        return allowNothingFound;
    }
}
