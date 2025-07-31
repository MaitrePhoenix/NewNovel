package fr.maitrephoenix.newnovel.activity.main;

import android.os.AsyncTask;
import android.text.Html;

import java.util.ArrayList;

import fr.maitrephoenix.newnovel.ListeNovel;
import fr.maitrephoenix.newnovel.ListeSiteTrad;
import fr.maitrephoenix.newnovel.activity.ConnexionAvecIndication;
import fr.maitrephoenix.newnovel.objet.ConnexionIndication;
import fr.maitrephoenix.newnovel.objet.Novel;
import fr.maitrephoenix.newnovel.objet.SiteTrad;
import fr.maitrephoenix.newnovel.objet.SplitIndication;
import fr.maitrephoenix.newnovel.stokage.SettingsStockage;

public class RecupNovel implements ConnexionAvecIndication.Listeners {
    private final ArrayList<String> listeNovelNom = new ArrayList<>();
    private final ArrayList<String> listeNovelLien = new ArrayList<>();
    private final ArrayList<String> listeNovelImage = new ArrayList<>();
    private final ArrayList<String> novelExclu = new ArrayList<>();
    private final ArrayList<AsyncTask> listeConnexion = new ArrayList<>();

    private final MainActivity mainActivity;
    private boolean tousLesSitesAnalyses = false;
    private static boolean arretForce = false;

    public RecupNovel(MainActivity mainActivity){
        this.mainActivity = mainActivity;

        //réinitialise le nombre de connexion à 0
        ConnexionIndication.forcerAllConnexionFinie();

        initialiserNovelExclu();
        obtenirNovels();
    }

    private void initialiserNovelExclu(){
        novelExclu.add("Altina the Sword Princess");
    }

    //ajoute les novels dans listeNovels
    private void obtenirNovels(){
        for (SiteTrad siteTrad : ListeSiteTrad.getListeSiteTrad()) {
            if(SettingsStockage.getSiteExclu().contains(siteTrad.getNom())){
                continue;
            }
            //récupérer les novels en fonction de leur type
            //si le type du serveur est HTML
            if(siteTrad.getMethodeObtention().equals("HTML")){
                //récupérer cahque liste de novel
                for (String lienListeNovel : siteTrad.getListeLienListeNovel()) {
                    //si la liste des novels est séparé en plusieurs pages du même nom, avec juste le nombre qui change
                    String[] lienNovelSplit = lienListeNovel.split("\\[nombre]");
                    if(lienNovelSplit.length != 1){
                        ConnexionIndication ci = new ConnexionIndication(lienNovelSplit[0]+1+lienNovelSplit[1], lienNovelSplit, siteTrad);
                        lancerConnexion(ci);
                    }
                    else{
                        ConnexionIndication ci = new ConnexionIndication(lienListeNovel, siteTrad);
                        lancerConnexion(ci);
                    }
                }
            }
        }
        tousLesSitesAnalyses = true;
        if(allChargementFini()){
            mainActivity.finChargement();
        }
    }

    int i = 0;

    //split une page (ou une partie de page) en fonction des indications
    private boolean splitPage(String page, ArrayList<SplitIndication> listeSplitIndication) {
        String[] pageSplits;
        boolean continuer = false;
        for (SplitIndication splitIndication : listeSplitIndication) {
            if (!page.equals("")) {
                pageSplits = page.split(splitIndication.getText());
                if(splitIndication.getAUneListe()){
                    //pour chaque partie split, sauf la première qui est vide
                    boolean passerPremiere = true;
                    for (String pageSplit : pageSplits) {
                        if(passerPremiere){
                            passerPremiere = false;
                        }
                        else{
                            //si au mininmum un on trouve un novel, on continue
                            if(splitPage(pageSplit, splitIndication.getListeSousSplit())){
                                continuer = true;
                            }
                        }
                    }
                }
                if(splitIndication.getpartieAConserver()==0){
                    page = pageSplits[0];
                }
                else if(splitIndication.getpartieAConserver()==-1){
                }
                else{
                    page = "";
                    if(pageSplits.length == 1){
                        page = pageSplits[0];
                    }
                    for (int i = splitIndication.getpartieAConserver(); i < pageSplits.length; i++) {
                        //pour tous sauf le dernier
                        if(i+1 != pageSplits.length){
                            page += pageSplits[i]+splitIndication.getText();
                        }
                        else{
                            page += pageSplits[i];
                        }
                    }
                }

                if(!pageSplits[0].equals(page)){
                    switch (splitIndication.getIndication()) {
                        case "lien":
                            listeNovelLien.add(pageSplits[0]);
                            continuer = true;
                            break;
                        case "titre":
                            pageSplits[0] = String.valueOf(Html.fromHtml(pageSplits[0], Html.FROM_HTML_MODE_LEGACY));
                            listeNovelNom.add(pageSplits[0]);
                            continuer = true;
                            break;
                        case "image":
                            listeNovelImage.add(pageSplits[0]);
                            continuer = true;
                            break;
                    }
                }
            }
        }
        return continuer;
    }

    @Override
    public void onPostExecute(ConnexionIndication resultat) {
        convertirPageEnListeNovel(resultat);
    }

    private void convertirPageEnListeNovel(ConnexionIndication resultat) {
        if(resultat != null){
            boolean continu = splitPage(resultat.getPage(), resultat.getSiteTrad().getSplitIndicationListeNovel());
            if (continu && resultat.getSuite()){
                resultat.incrementeNumeroPage();
                String[] lienSplit = resultat.getLienSplit();
                resultat.setLien(lienSplit[0] + resultat.getNumeroPage() + lienSplit[1]);
                lancerConnexion(resultat);
            }
            else{
                ConnexionIndication.connexionFinie();
            }
            //ajouter les novels obtenu à ListeNovel
            int j = 0;
            Novel novel;
            for (String lien : listeNovelLien) {
                if(j<listeNovelNom.size()){
                    if(!novelExclu.contains(listeNovelNom.get(j))){
                        novel = new Novel(listeNovelNom.get(j), lien, resultat.getSiteTrad().getNom());
                        if(j<listeNovelImage.size()){
                            novel.setImageLien(listeNovelImage.get(j));
                        }
                        ListeNovel.addNovel(novel);
                        mainActivity.addNovel(novel);
                    }
                }
                j++;
            }
            listeNovelLien.clear();
            listeNovelNom.clear();
            listeNovelImage.clear();

        }
        else{
            ConnexionIndication.connexionFinie();
        }
        if(allChargementFini()){
            arretForce = false;
            mainActivity.finChargement();
        }
    }

    private boolean allChargementFini(){
        return (ConnexionIndication.getConnexionNonFinie() == 0 && tousLesSitesAnalyses);
    }

    public void lancerConnexion(ConnexionIndication indications){
         listeConnexion.add(new ConnexionAvecIndication(this).execute(indications));
    }

    public void arreterChargement(){
        if(!allChargementFini()){
            ConnexionIndication.forcerAllConnexionFinie();
            for (AsyncTask connexion : listeConnexion) {
                ((ConnexionAvecIndication) connexion).stopConnexion();
                connexion.cancel(true);
            }
            arretForce = true;
        }
    }

    /**
     * Force à relancer le chargement
     */
    public static void forceRechargementListeNovel(){
        arretForce = true;
    }

    public boolean getArretForce(){
        return arretForce;
    }
}