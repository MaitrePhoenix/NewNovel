package fr.maitrephoenix.newnovel.stokage;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SettingsStockage {

    //get settings from stockage
    public static String getSettings() {
        return BasicStockage.readFileInterne("settings.json");
    }

    public static String creerFichierSettings() {
        JSONObject jsonObject  = new JSONObject();
        try {
            jsonObject.put("Version", "1.0");
            jsonObject.put("Theme", MODE_NIGHT_FOLLOW_SYSTEM);
            JSONArray jsonArray = new JSONArray();
            jsonObject.put("SiteDesac", jsonArray);
            return jsonObject.toString();
        } catch (JSONException ignored) {}
        return "";
    }

    //set settings in stockage
    public static void setSettings(String settings) {
        BasicStockage.writeFileInterne("settings.json", settings);
    }

    public static void saveTheme(int theme) {
        String settings = getSettings();
        if (settings == null || settings.equals("")) {
            settings = creerFichierSettings();
        }
        try {
            JSONObject jsonObject = new JSONObject(settings);
            jsonObject.put("Theme", theme);
            setSettings(jsonObject.toString());
        } catch (JSONException ignored) {}
    }

    public static int getTheme() {
        String settings = getSettings();
        if (settings == null || settings.equals("")) {
            try {
                JSONObject jsonObject  = new JSONObject(settings);
                if((jsonObject.getString("Version")).equals("1.0")) {
                    return jsonObject.getInt("Theme");
                }
            } catch (JSONException ignored) {}
        }
        return MODE_NIGHT_FOLLOW_SYSTEM;
    }

    public static String getAllSave() {
        String favori = BasicStockage.readFileInterne("favori.json");
        String chapitreLu = BasicStockage.readFileInterne("chapitreLu.json");
        if(favori == null || favori.equals("")) {
            try {
                favori = (new JSONObject()).put("Version", "1.0").put("Liste des Novels", new JSONArray()).toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(chapitreLu == null || chapitreLu.equals("")) {
            try {
                chapitreLu = (new JSONObject()).put("Version", "1.0").put("Liste des Novels", new JSONArray()).toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            JSONObject jsonObject  = new JSONObject();
            jsonObject.put("Version", "1.0");
            jsonObject.put("Favori", new JSONObject(favori));
            jsonObject.put("ChapitreLu", new JSONObject(chapitreLu));
            return jsonObject.toString();
        } catch (JSONException ignored) {}
        return "";
    }

    public static void overwriteAllSave(String s) {
        if(s.equals("")) {
            BasicStockage.writeFileInterne("favori.json", "");
            BasicStockage.writeFileInterne("chapitreLu.json", "");
        }
        try {
            JSONObject jsonObject = new JSONObject(s);
            if((jsonObject.getString("Version")).equals("1.0")) {
                BasicStockage.writeFileInterne("favori.json", jsonObject.getJSONObject("Favori").toString());
                BasicStockage.writeFileInterne("chapitreLu.json", jsonObject.getJSONObject("ChapitreLu").toString());
            }
        } catch (JSONException ignored) {}
    }

    public static ArrayList<String> getSiteExclu(){
        String settings = getSettings();
        ArrayList<String> arrayList = new ArrayList<>();
        if (settings == null || settings.equals("")) {
            creerFichierSettings();
        }
        else{
            try {
                JSONObject jsonObject  = new JSONObject(settings);
                if((jsonObject.getString("Version")).equals("1.0")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("SiteDesac");
                    for(int i = 0; i < jsonArray.length(); i++) {
                        arrayList.add(jsonArray.getString(i));
                    }
                }
            } catch (JSONException ignored) {}
        }
        return arrayList;
    }

    public static void setSiteExclu(ArrayList<String> siteExclu) {
        String settings = getSettings();
        if (settings == null || settings.equals("")) {
            settings = creerFichierSettings();
        }
        try {
            JSONObject jsonObject  = new JSONObject(settings);
            jsonObject.put("SiteDesac", new JSONArray(siteExclu));
            setSettings(jsonObject.toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
