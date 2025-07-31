package fr.maitrephoenix.newnovel.activity.novel;

import android.os.AsyncTask;
import android.text.Html;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.maitrephoenix.newnovel.Commun;
import fr.maitrephoenix.newnovel.activity.ConnexionAvecIndication;
import fr.maitrephoenix.newnovel.objet.Chapitre;
import fr.maitrephoenix.newnovel.objet.ConnexionIndication;
import fr.maitrephoenix.newnovel.objet.Novel;
import fr.maitrephoenix.newnovel.objet.SplitIndication;

public class ContenuNovel implements ConnexionAvecIndication.Listeners {
    private final NovelActivity novelActivity;
    private final Novel novel;
    private boolean synopsisRecup = false;
    private boolean classificationRecup = false;
    private final ArrayList<String> listeLien = new ArrayList<>();
    private final ArrayList<String> listeTitre = new ArrayList<>();
    private boolean chapitreTrouve = false;
    private AsyncTask connexion;

    public ContenuNovel(Novel novel, NovelActivity novelActivity) {
        this.novel = novel;
        this.novelActivity = novelActivity;
        if(novel.getSiteTrad().getMethodeObtention().equals("HTML")){
            String[] lienNovelSplit = novel.getLien().split("\\[nombre]");
            if(lienNovelSplit.length != 1){
                ConnexionIndication ci = new ConnexionIndication(lienNovelSplit[0]+1+lienNovelSplit[1], lienNovelSplit, novel.getSiteTrad());
                //indiquer le connexion finie pour pas que ca ne pose problème avec la liste des novels (cela ne sert à rien pour la liste des chapitres de toutes facon)
                ConnexionIndication.connexionFinie();
                connexion = new ConnexionAvecIndication(this).execute(ci);
            }
            else{
                ConnexionIndication ci = new ConnexionIndication(novel.getLien(), novel.getSiteTrad());
                //indiquer le connexion finie pour pas que ca ne pose problème avec la liste des novels (cela ne sert à rien pour la liste des chapitres de toutes facon)
                ConnexionIndication.connexionFinie();
                connexion = new ConnexionAvecIndication(this).execute(ci);
            }
        }
    }

    private boolean splitPage(String page, ArrayList<SplitIndication> listeSplitIndication) {
        String[] pageSplits;
        boolean continuer = false;
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
                //dans le cas où aucune séparation n'a été trouvé et que la split indication autorise que rien ne sois trouvé :
                else if (splitIndication.isAllowNothingFound() && pageSplits.length == 1){
                    page = pageSplits[0];
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


                switch (splitIndication.getIndication()) {
                    case "lien":
                        listeLien.add(pageSplits[0]);
                        continuer = true;
                        break;

                    case "titre":
                        //remplace les caractères spéciaux
                        pageSplits[0] = String.valueOf(Html.fromHtml(pageSplits[0], Html.FROM_HTML_MODE_LEGACY));
                        listeTitre.add(pageSplits[0]);
                        continuer = true;
                        break;

                    case "auteur/traducteur":
                        String auteurTraducteur = Commun.corrigerTexte(pageSplits[0]);
                        novelActivity.addAuteurTraducteur(auteurTraducteur);
                        novel.addAuteur(auteurTraducteur);
                        break;

                    case "synopsis":
                        if(!synopsisRecup){
                            String synopsis = Commun.corrigerTexte(pageSplits[0]);
                            novelActivity.setSynopsis(synopsis);
                            novel.setSynopsis(synopsis);
                            synopsisRecup = true;
                        }
                        break;

                    case "classification":
                        if(!classificationRecup){
                            String classification = Commun.corrigerTexte(pageSplits[0]);
                            novelActivity.setClassification(classification);
                            novel.setClassification(classification);
                            classificationRecup = true;
                        }
                        break;
                    case "parution":
                        novelActivity.setRythmeParution(Commun.corrigerTexte(pageSplits[0]));
                        break;
                }
            }
        }
        return continuer;
    }

    @Override
    public void onPostExecute(ConnexionIndication resultat) {
        if(resultat == null){
            novelActivity.erreurChargement();
        }else{
            boolean continu = splitPage(resultat.getPage(), novel.getSiteTrad().getSplitIndicationContenuNovel());
            if (continu && resultat.getSuite()){
                resultat.incrementeNumeroPage();
                String[] lienSplit = resultat.getLienSplit();
                resultat.setLien(lienSplit[0] + resultat.getNumeroPage() + lienSplit[1]);
                connexion = new ConnexionAvecIndication(this).execute(resultat);
            }


            //créer la liste des chapitres
            int j = 0;
            ArrayList<Chapitre> listeChapitre = new ArrayList<>();
            for (String lien : listeLien) {
                if(j<listeTitre.size()){
                    chapitreTrouve = true;
                    Chapitre chapitre = new Chapitre(lien, listeTitre.get(j), novel, j+1);
                    listeChapitre.add(chapitre);
                }
                j++;
            }

            //filtrer dans le cas où les chapitres sont dans le désordre
            if(novel.getSiteTrad().isATrier()) {
                Collections.sort(listeChapitre, (s1, s2) -> {
                    Pattern pattern = Pattern.compile("\\d+");
                    Matcher matcher1 = pattern.matcher(s1.getTitre());
                    Matcher matcher2 = pattern.matcher(s2.getTitre());
                    if (matcher1.find() && matcher2.find()) {
                        int n1 = Integer.parseInt(matcher1.group());
                        int n2 = Integer.parseInt(matcher2.group());
                        return n1 - n2;
                    }
                    return 0;
                });
            }

            int i = 1;
            for (Chapitre chapitre : listeChapitre) {
                chapitre.setNumero(i);
                novel.addChapitre(chapitre);
                novelActivity.addChapitre(chapitre);
                i++;
            }

            listeLien.clear();
            listeTitre.clear();

            if(!chapitreTrouve){
                novelActivity.aucunChapitreTrouve();
            }
            if(!synopsisRecup){
                novelActivity.setSynopsis("Aucun synopsis trouvé");
            }

            novelActivity.positionnerAChapLu();
        }
    }

    public void stopConnexion(){
        if(connexion != null){
            ((ConnexionAvecIndication) connexion).stopConnexion();
        }
    }
}
