package fr.maitrephoenix.newnovel.stokage;

import static fr.maitrephoenix.newnovel.stokage.BasicStockage.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.maitrephoenix.newnovel.objet.Novel;

public class FavoriStockage {

    public static void ajouterFavori(Novel novel) {
        String contenu = readFileInterne("favori.json");
        if (contenu != null && !contenu.equals("")) {
            try {
                JSONObject jsonObject  = new JSONObject(contenu);
                if(((String) jsonObject.get("Version")).equals("1.0")) {
                    JSONArray jsonArrayListeNovel = jsonObject.getJSONArray("Liste des Novels");
                    jsonArrayListeNovel.put(new JSONObject().put("Nom", novel.getNom()).put("Lien", novel.getLien()).put("Image", novel.getImageLien()).put("SiteTrad", novel.getNomSiteTrad()));
                    jsonObject.put("Liste des Novels", jsonArrayListeNovel);
                    writeFileInterne("favori.json", jsonObject.toString());
                    return;
                }
            } catch (JSONException e) {}
        }
        //si le contenu est null ou que le contenu n'est pas un json
        JSONObject jsonObject  = new JSONObject();
        try {
            jsonObject.put("Version", "1.0");
            jsonObject.put("Liste des Novels", new JSONArray().put(new JSONObject().put("Nom", novel.getNom()).put("Lien", novel.getLien()).put("Image", novel.getImageLien()).put("SiteTrad", novel.getNomSiteTrad())));
            writeFileInterne("favori.json", jsonObject.toString());
        } catch (JSONException e) {}
    }

    public static void supprimerFavori(Novel novel) {
        String contenu = readFileInterne("favori.json");
        if (contenu != null && !contenu.equals("")) {
            try {
                JSONObject jsonObject  = new JSONObject(contenu);
                if((jsonObject.getString("Version")).equals("1.0")) {
                    JSONArray jsonArrayListeNovel = jsonObject.getJSONArray("Liste des Novels");
                    for (int i = 0; i < jsonArrayListeNovel.length(); i++) {
                        if(jsonArrayListeNovel.getJSONObject(i).getString("Lien").equals(novel.getLien())) {
                            jsonArrayListeNovel.remove(i);
                            jsonObject.put("Liste des Novels", jsonArrayListeNovel);
                            writeFileInterne("favori.json", jsonObject.toString());
                            return;
                        }
                    }
                    writeFileInterne("favori.json", jsonObject.toString());
                    return;
                }
            } catch (JSONException e) {}
        }
    }

    public static boolean estFavori(String lienNovel) {
        String contenu = readFileInterne("favori.json");
        if (contenu != null && !contenu.equals("")) {
            try {
                JSONObject jsonObject  = new JSONObject(contenu);
                if((jsonObject.getString("Version")).equals("1.0")) {
                    JSONArray jsonArrayListeNovel = jsonObject.getJSONArray("Liste des Novels");
                    for (int i = 0; i < jsonArrayListeNovel.length(); i++) {
                        if(jsonArrayListeNovel.getJSONObject(i).getString("Lien").equals(lienNovel)) {
                            return true;
                        }
                    }
                }
            } catch (JSONException e) {}
        }
        return false;
    }

    /**
     * Retourne la liste des favoris avec le plus récent en premeir
     * @return
     */
    public static ArrayList<Novel> getFavoris() {
        String contenu = readFileInterne("favori.json");
        ArrayList<Novel> listeNovel = new ArrayList<>();
        if (contenu != null && !contenu.equals("")) {
            try {
                JSONObject jsonObject  = new JSONObject(contenu);
                if((jsonObject.getString("Version")).equals("1.0")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("Liste des Novels");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject unNovelJSON = jsonArray.getJSONObject(jsonArray.length() - i - 1);
                        try {
                            Novel novel = new Novel(unNovelJSON.getString("Nom"), unNovelJSON.getString("Lien"), unNovelJSON.getString("SiteTrad"));
                            novel.setImageLien(unNovelJSON.getString("Image"));
                            listeNovel.add(novel);
                        } catch (JSONException e) {}
                    }
                    return listeNovel;
                }
            } catch (JSONException e) {}
        }
        return listeNovel;
    }
}
