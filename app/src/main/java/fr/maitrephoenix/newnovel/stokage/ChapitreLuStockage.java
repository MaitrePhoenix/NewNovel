package fr.maitrephoenix.newnovel.stokage;

import static fr.maitrephoenix.newnovel.stokage.BasicStockage.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.maitrephoenix.newnovel.objet.Chapitre;

public class ChapitreLuStockage {

    public static void marquerLu(Chapitre chapitre) {
        String contenu = readFileInterne("chapitreLu.json");
        if (contenu != null && !contenu.equals("")) {
            try {
                JSONObject jsonObject  = new JSONObject(contenu);
                if((jsonObject.getString("Version")).equals("1.0")) {
                    JSONObject jsonObjectListeNovel = jsonObject.getJSONObject("Liste des Novels");
                    if (!jsonObjectListeNovel.isNull(chapitre.getNovel().getNom())) {
                        JSONArray listeChapitreLu = jsonObjectListeNovel.getJSONArray(chapitre.getNovel().getNom());
                        for (int i = 0; i < listeChapitreLu.length(); i++) {
                            if(listeChapitreLu.getString(i).split("-").length == 2){
                                if(chapitre.getNumero()>= Integer.parseInt(listeChapitreLu.getString(i).split("-")[0]) && chapitre.getNumero()<= Integer.parseInt(listeChapitreLu.getString(i).split("-")[1])){
                                    return;
                                }
                            }
                            if (listeChapitreLu.getInt(i) == chapitre.getNumero()) {
                                return;
                            }
                        }
                        listeChapitreLu.put(chapitre.getNumero());
                        jsonObjectListeNovel.put(chapitre.getNovel().getNom(), listeChapitreLu);
                        jsonObject.put("Liste des Novels", jsonObjectListeNovel);
                    } else {
                        JSONArray jsonArray = new JSONArray();
                        jsonArray.put(chapitre.getNumero());
                        jsonObjectListeNovel.put(chapitre.getNovel().getNom(), jsonArray);
                        jsonObject.put("Liste des Novels", jsonObjectListeNovel);
                    }
                    writeFileInterne("chapitreLu.json", jsonObject.toString());
                    return;
                }
            } catch (JSONException e) {}
        }
        //si le contenu est null ou que le contenu n'est pas un json
        JSONObject jsonObject  = new JSONObject();
        try {
            jsonObject.put("Version", "1.0");
            jsonObject.put("Liste des Novels", new JSONObject().put(chapitre.getNovel().getNom(), new JSONArray().put(chapitre.getNumero())));
            writeFileInterne("chapitreLu.json", jsonObject.toString());
        } catch (JSONException e) {}
    }

    public static boolean estLu(Chapitre chapitre) {
        String contenu = readFileInterne("chapitreLu.json");
        if (contenu != null && !contenu.equals("")) {
            try {
                JSONObject jsonObject  = new JSONObject(contenu);
                if((jsonObject.getString("Version")).equals("1.0")) {
                    jsonObject = jsonObject.getJSONObject("Liste des Novels");

                    JSONArray listeChapitreLu = new JSONArray(jsonObject.get(chapitre.getNovel().getNom()).toString());

                    for (int i = 0; i < listeChapitreLu.length(); i++) {
                        try {
                            if(listeChapitreLu.getString(i).split("-").length == 2){
                                if(chapitre.getNumero()>= Integer.parseInt(listeChapitreLu.getString(i).split("-")[0]) && chapitre.getNumero()<= Integer.parseInt(listeChapitreLu.getString(i).split("-")[1])){
                                    return true;
                                }
                            }
                            else if (listeChapitreLu.getInt(i) == chapitre.getNumero()) {
                                return true;
                            }
                        } catch (JSONException e) {}
                    }
                }
            } catch (JSONException e) {}
        }
        return false;
    }
}
