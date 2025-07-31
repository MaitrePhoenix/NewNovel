package fr.maitrephoenix.newnovel.stokage;

import static fr.maitrephoenix.newnovel.stokage.BasicStockage.readFileInterne;
import static fr.maitrephoenix.newnovel.stokage.BasicStockage.writeFileInterne;

import org.json.JSONException;
import org.json.JSONObject;

import fr.maitrephoenix.newnovel.objet.Chapitre;
import fr.maitrephoenix.newnovel.objet.Novel;

public class PositionChapitreStockage {


    public static void setPositionChapitre(Chapitre chapitre, int position) {
        String contenu = readFileInterne("positionChapitre.json");
        if (contenu != null && !contenu.equals("")) {
            try {
                JSONObject jsonObject  = new JSONObject(contenu);
                if((jsonObject.getString("Version")).equals("1.0")) {
                    JSONObject jsonObjectListeNovel = jsonObject.getJSONObject("Liste des Novels");

                    JSONObject jsonObjectChapitre = new JSONObject();
                    jsonObjectChapitre.put(chapitre.getNumero() + "", position);
                    jsonObjectListeNovel.put(chapitre.getNovel().getNom(), jsonObjectChapitre);
                    jsonObject.put("Liste des Novels", jsonObjectListeNovel);

                    writeFileInterne("positionChapitre.json", jsonObject.toString());
                    return;
                }
            } catch (JSONException e) {
                return;
            }
        }
        //si le contenu est null ou que le contenu n'est pas un json
        JSONObject jsonObject  = new JSONObject();
        try {
            jsonObject.put("Version", "1.0");
            JSONObject jsonObjectListeNovel = new JSONObject();
            JSONObject jsonObjectChapitre = new JSONObject();
            jsonObjectChapitre.put(chapitre.getNumero() + "", position);
            jsonObjectListeNovel.put(chapitre.getNovel().getNom(), jsonObjectChapitre);
            jsonObject.put("Liste des Novels", jsonObjectListeNovel);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        writeFileInterne("positionChapitre.json", jsonObject.toString());
    }

    public static int getPositionChapitre(Chapitre chapitre) {
        String contenu = readFileInterne("positionChapitre.json");
        if (contenu != null && !contenu.equals("")) {
            try {
                JSONObject jsonObject  = new JSONObject(contenu);
                if((jsonObject.getString("Version")).equals("1.0")) {
                    JSONObject jsonObjectListeNovel = jsonObject.getJSONObject("Liste des Novels");
                    if (!jsonObjectListeNovel.isNull(chapitre.getNovel().getNom())) {
                        JSONObject jsonObjectChapitre = jsonObjectListeNovel.getJSONObject(chapitre.getNovel().getNom());
                        if (!jsonObjectChapitre.isNull(chapitre.getNumero() + "")) {
                            return jsonObjectChapitre.getInt(chapitre.getNumero() + "");
                        }
                    }
                }
            } catch (JSONException e) {
                return 0;
            }
        }
        return 0;
    }

    public static int getLastChapitreLu(Novel novel) {
        String contenu = readFileInterne("positionChapitre.json");
        if (contenu != null && !contenu.equals("")) {
            try {
                JSONObject jsonObject  = new JSONObject(contenu);
                if((jsonObject.getString("Version")).equals("1.0")) {
                    JSONObject jsonObjectListeNovel = jsonObject.getJSONObject("Liste des Novels");
                    String nom = novel.getNom();
                    if (!jsonObjectListeNovel.isNull(nom)) {
                        JSONObject jsonObjectChapitre = jsonObjectListeNovel.getJSONObject(nom);

                        return Integer.parseInt(jsonObjectChapitre.keys().next());
                    }
                }
            } catch (JSONException e) {
                return 0;
            }
        }
        return 1;
    }
}
