package fr.maitrephoenix.newnovel;

import android.text.Html;

import fr.maitrephoenix.newnovel.objet.Chapitre;
import fr.maitrephoenix.newnovel.objet.Novel;

public class Commun {
    private static boolean listeNovelCharge = false;
    private static Chapitre chapitreOuvert;
    private static Novel novelOuvert;
    private static boolean chargementHorsLigne = false;

    private static boolean stopTelechargement = false;

    public static Novel getNovelOuvert() {
        return novelOuvert;
    }

    public static void setNovelOuvert(Novel novelOuvert) {
        Commun.novelOuvert = novelOuvert;
    }

    public static Chapitre getchapitreOuvert() {
        return chapitreOuvert;
    }

    public static void setchapitreOuvert(Chapitre getChapitreOuvert) {
        chapitreOuvert = getChapitreOuvert;
    }


    public static String corrigerTexte(String texte) {
        String textTemp = "";
        //retirer les scripts
        String[] texteSplit = texte.split("<script");
        if(texteSplit.length > 1){
            textTemp = texteSplit[0];
            for(String partieSplit : texteSplit){
                if(partieSplit.contains("</script>")){
                    texteSplit = partieSplit.split("</script>");
                    textTemp += texteSplit[1];
                }
            }
            if(!textTemp.equals("")){
                texte = textTemp;
            }
        }

        //retirer les styles
        texteSplit = texte.split("<style type=\"");
        if(texteSplit.length > 1){
            textTemp = "";
            for(String partieSplit : texteSplit){
                if(partieSplit.contains("</style>")){
                    texteSplit = partieSplit.split("</style>");
                    textTemp += texteSplit[1];
                }
            }
            if(!textTemp.equals("")){
                texte = textTemp;
            }
        }

        //remplace les caractères spéciaux et corrige les retour à la ligne
        texteSplit = texte.split("<br>");
        if(texteSplit.length == 1){
            texte = ""+Html.fromHtml(texte, Html.FROM_HTML_MODE_LEGACY);
        }
        else{
            texte = "";
            for (String s : texteSplit) {
                if(!(s.equals("")||s.equals(" "))) {
                    texte += "\n"+ Html.fromHtml(s, Html.FROM_HTML_MODE_LEGACY);
                }
            }
        }

        //suppression des retours à la ligne avant et après le texte
        while (true) {
            texteSplit = texte.split("\n");

            if(!(texteSplit[0].trim().equals("") || texteSplit[0].equals(" ")) || texteSplit.length == 1){
                break;
            }
            else{
                texte = "";
                for (int i = 1; i < texteSplit.length; i++) {
                    if(i != 1){
                        texte += "\n";
                    }
                    texte += texteSplit[i];
                }
            }
        }
        while (true){
            texteSplit = texte.split("\n");
            if(!(texteSplit[texteSplit.length-1].trim().equals("") || texteSplit[texteSplit.length-1].equals(" ")) || texteSplit.length == 1){
                break;
            }
            else{
                texte = "";
                for (int i = 0; i < texteSplit.length-1; i++) {
                    if(i != 0){
                        texte += "\n";
                    }
                    texte += texteSplit[i];
                }
            }
        }

        return texte;
    }

    public static boolean getchargementNovelFini() {
        return listeNovelCharge;
    }

    public static void chargementNovelFini() {
        listeNovelCharge = true;
    }

    public static boolean isChargementHorsLigne() {
        return chargementHorsLigne;
    }

    public static void setChargementHorsLigne(boolean chargementHorsLigne) {
        Commun.chargementHorsLigne = chargementHorsLigne;
    }

    public static boolean getStopTelechargement() {
        return stopTelechargement;
    }

    public static void setStopTelechargement(boolean stopTelechargement) {
        Commun.stopTelechargement = stopTelechargement;
    }
}
