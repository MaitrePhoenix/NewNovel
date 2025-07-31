package fr.maitrephoenix.newnovel.stokage;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import fr.maitrephoenix.newnovel.objet.Chapitre;
import fr.maitrephoenix.newnovel.objet.Novel;

public class TelechargementStockage {
    private static final String charAutorise = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!'éàèù&ç ,.;-_()[]{}=+%€";

    public static List<String> getAllChapFromNovelHorsLigne(String nomNovel) {
        return BasicStockage.getFileFromFolders("/horsLigne/" + nomNovel);
    }

    public static void addChapitreHorsLigne(Chapitre chapitre) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("texte", chapitre.getPage());
            jsonObject.put("titre", chapitre.getTitre());
            jsonObject.put("numero", chapitre.getNumero());

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        BasicStockage.createFileInFolder(BasicStockage.fusionLienDossier(new String[]{"horsLigne", replaceCharNotAutorise(chapitre.getNovel().getNom())}), replaceCharNotAutorise(chapitre.getTitre())+".json", jsonObject.toString());
    }

    public static void addNovelHorsLigne(Novel novel) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nom", novel.getNom());
            jsonObject.put("synopsis", novel.getSynopsis());
            jsonObject.put("lien", novel.getLien());
            jsonObject.put("siteTrad", novel.getNomSiteTrad());
            jsonObject.put("auteur", novel.getAuteur());
            jsonObject.put("classification", novel.getClassification());
            BasicStockage.createFileInFolder(BasicStockage.fusionLienDossier(new String[]{"horsLigne", replaceCharNotAutorise(novel.getNom())}), "infoNovel.json", jsonObject.toString());
            if(novel.getImageLien() != null && !novel.getImageLien().equals("")){
                Picasso.get().load(novel.getImageLien())
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                BasicStockage.saveImageInterne("imageNovel", BasicStockage.fusionLienDossier(new String[]{"horsLigne", replaceCharNotAutorise(novel.getNom())}), bitmap);
                            }
                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {}
                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {}
                        });
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    //replace all char not autorise by espace
    public static String replaceCharNotAutorise(String text){
        String newText = "";
        for (int i = 0; i < text.length(); i++) {
            if(charAutorise.contains(String.valueOf(text.charAt(i)))){
                newText += text.charAt(i);
            } else if (text.charAt(i) == '|'){
                newText += ",";
            } else{
                newText += " ";
            }
        }
        return newText.trim();
    }

    public static ArrayList<Novel> getAllNovelHorsLigne() {
        ArrayList<String> listeFolderNovelHorsLigne =  BasicStockage.getFolderFromFolders("/horsLigne");
        ArrayList<Novel> listeNovel = new ArrayList<>();
        for (String folderNovel : listeFolderNovelHorsLigne) {
            try {
                JSONObject jsonNovel = new JSONObject(BasicStockage.readFileInterne("/horsLigne/" + folderNovel + "/", "infoNovel.json"));
                Novel novel = new Novel(jsonNovel.getString("nom"), jsonNovel.getString("lien"), jsonNovel.getString("siteTrad"));
                novel.setSynopsis(jsonNovel.getString("synopsis"));
                novel.addAuteur(jsonNovel.getString("auteur"));
                novel.setClassification(jsonNovel.getString("classification"));
                novel.setImageBitmap(BasicStockage.getImageInterne("/horsLigne/"+folderNovel+"/", "imageNovel.png"));
                listeNovel.add(novel);
            } catch (RuntimeException e) {
                Novel novel = new Novel(folderNovel, "", "");
                novel.setSynopsis("Erreur : lors de la récupération du novel");
                listeNovel.add(novel);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return listeNovel;
    }

    public static ArrayList<Chapitre> getChapitreHorsLigne(Novel novel){
        ArrayList<String> listeFileChapitreHorsLigne =  BasicStockage.getFileFromFolders("horsLigne/"+replaceCharNotAutorise(novel.getNom()));
        ArrayList<Chapitre> listeChapitre = new ArrayList<>();
        for (String fileChapitre : listeFileChapitreHorsLigne) {
            try {
                if(!(fileChapitre.equals("imageNovel.png")||fileChapitre.equals("infoNovel.json"))){
                    JSONObject jsonChapitre = new JSONObject(BasicStockage.readFileInterne("/horsLigne/" + replaceCharNotAutorise(novel.getNom()) + "/", fileChapitre));
                    Chapitre chapitre = new Chapitre("", jsonChapitre.getString("titre"), novel, jsonChapitre.getInt("numero"));
                    chapitre.setPage(jsonChapitre.getString("texte"));
                    listeChapitre.add(chapitre);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        listeChapitre.sort(Comparator.comparingInt(Chapitre::getNumero));
        return listeChapitre;
    }

    public static void delNovelTelecharge(Novel novel){
        BasicStockage.deleteDirectoryOrFile("horsLigne/"+replaceCharNotAutorise(novel.getNom()));
    }
}
