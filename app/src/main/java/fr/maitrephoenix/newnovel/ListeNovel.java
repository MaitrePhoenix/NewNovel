package fr.maitrephoenix.newnovel;

import java.util.ArrayList;

import fr.maitrephoenix.newnovel.objet.Novel;

public class ListeNovel {
    static ArrayList<Novel> listeNovel = new ArrayList<>();

    public static ArrayList<Novel> getListeNovel() {
        return listeNovel;
    }

    public static void addNovel(Novel novel) {
        for(Novel novelElement : listeNovel){
            if(novelElement.getNom().equals(novel.getNom()) && novelElement.getNomSiteTrad().equals(novel.getNomSiteTrad())){
                return;
            }
        }
        if(!listeNovel.contains(novel)) {
            listeNovel.add(novel);
        }
    }

    public static Novel getNovel(String nom) {
        for (Novel novel : listeNovel) {
            if (novel.getNom().equals(nom)) {
                return novel;
            }
        }
        return null;
    }

    public static ArrayList<Novel> getNovels() {
        return listeNovel;
    }

    public static void clear() {
        listeNovel.clear();
    }
}
