package fr.maitrephoenix.newnovel.activity.lecture;

import java.util.ArrayList;

import fr.maitrephoenix.newnovel.Commun;
import fr.maitrephoenix.newnovel.accesInternet.ConnexionAvecString;
import fr.maitrephoenix.newnovel.objet.Chapitre;
import fr.maitrephoenix.newnovel.objet.SplitIndication;

public class RecupChapitre implements ConnexionAvecString.Listeners {
    private final LectureActivity lectureActivity;
    private final Chapitre chapitre;
    private String texte = "";
    private boolean textRecup = false;
    private ConnexionAvecString connexion;

    public RecupChapitre(Chapitre chapitre, LectureActivity lectureActivity) {
        this.chapitre = chapitre;
        this.lectureActivity = lectureActivity;
        if(chapitre.getNovel().getSiteTrad().getMethodeObtention().equals("HTML")){
            connexion = new ConnexionAvecString(this);
            connexion.execute(chapitre.getLien());
        }
    }


    @Override
    public void onPostExecute(String resultat) {
        if(resultat == null || resultat.equals("")){
            lectureActivity.erreurChargement();
        }else{
            splitPage(resultat, chapitre.getNovel().getSiteTrad().getSplitIndicationLecture());

            if(textRecup){
                lectureActivity.editTexte(texte);
            }
            else{
                lectureActivity.erreurChargement();
            }
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

    public void stopConnexion(){
        if(connexion != null){
            connexion.stopConnexion();
        }
    }
}
