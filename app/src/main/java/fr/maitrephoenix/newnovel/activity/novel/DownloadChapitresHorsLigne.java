package fr.maitrephoenix.newnovel.activity.novel;

import android.os.AsyncTask;

import java.util.ArrayList;

import fr.maitrephoenix.newnovel.Commun;
import fr.maitrephoenix.newnovel.accesInternet.ConnexionPourDownload;
import fr.maitrephoenix.newnovel.objet.Chapitre;
import fr.maitrephoenix.newnovel.objet.SplitIndication;
import fr.maitrephoenix.newnovel.stokage.TelechargementStockage;

public class DownloadChapitresHorsLigne implements ConnexionPourDownload.Listeners{
    private final NovelActivity novelActivity;
    private final ArrayList<Chapitre> chapitres;
    private String texte = "";
    private boolean textRecup = false;
    private static ArrayList<AsyncTask> listeConnexion = new ArrayList<>();

    public DownloadChapitresHorsLigne(ArrayList<Chapitre> chapitres, NovelActivity novelActivity) {
        this.chapitres = chapitres;
        this.novelActivity = novelActivity;
        if(chapitres.get(0).getNovel().getSiteTrad().getMethodeObtention().equals("HTML")){
            for (Chapitre unChapitre : chapitres) {
                listeConnexion.add(new ConnexionPourDownload(this).execute(unChapitre));
            }
        }
    }


    @Override
    public void onPostExecute(Chapitre chapitre) {
        if(Commun.getStopTelechargement()){
            return;
        }

        if(chapitre == null || chapitre.getPage().equals("")){
            novelActivity.erreurChargement();
        }else{
            splitPage(chapitre.getPage(), chapitre.getNovel().getSiteTrad().getSplitIndicationLecture());


            if(textRecup){
                chapitre.setPage(texte);
                TelechargementStockage.addChapitreHorsLigne(chapitre);
            }
            else{
                novelActivity.erreurChargement();
            }
            texte = "";
        }
    }

    private void splitPage(String page, ArrayList<SplitIndication> listeSplitIndication) {
        String[] pageSplits;
        for (SplitIndication splitIndication : listeSplitIndication) {
            if (!page.equals("")){
                //si jamais plusieurs split différent sont utilisé pour un même site
                if (splitIndication.getPossibleMultiple()){
                    for (int i = 0; i+1 < splitIndication.getTextMultiple().size(); i++) {
                        page = String.join(splitIndication.getTextMultiple().get(i+1), page.split(splitIndication.getTextMultiple().get(i)));
                    }
                    pageSplits = page.split(splitIndication.getTextMultiple().get(splitIndication.getTextMultiple().size()-1));
                }
                else{
                    pageSplits = page.split(splitIndication.getText());
                }
                if(splitIndication.getAUneListe()){
                    //pour chaque partie split, sauf la première qui est vide
                    boolean passerPremiere = false;
                    for (String pageSplit : pageSplits) {
                        if(passerPremiere){
                            splitPage(pageSplit, splitIndication.getListeSousSplit());
                        }
                        else{
                            passerPremiere = true;
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

                if(splitIndication.getIndication().equals("texte")){
                    texte += Commun.corrigerTexte(pageSplits[0]);
                    textRecup = true;
                }
            }
        }
    }

    public static void stopConnexion(){
        for(AsyncTask connexion : listeConnexion){
            ((ConnexionPourDownload) connexion).stopConnexion();
            connexion.cancel(true);
        }
    }
}
