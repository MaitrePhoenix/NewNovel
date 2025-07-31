package fr.maitrephoenix.newnovel.activity.novel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import fr.maitrephoenix.newnovel.Commun;
import fr.maitrephoenix.newnovel.R;
import fr.maitrephoenix.newnovel.activity.lecture.LectureActivity;
import fr.maitrephoenix.newnovel.arrayAdapter.ChapitreArrayAdapter;
import fr.maitrephoenix.newnovel.objet.Chapitre;
import fr.maitrephoenix.newnovel.objet.Novel;
import fr.maitrephoenix.newnovel.stokage.ChapitreLuStockage;
import fr.maitrephoenix.newnovel.stokage.FavoriStockage;
import fr.maitrephoenix.newnovel.stokage.PositionChapitreStockage;
import fr.maitrephoenix.newnovel.stokage.TelechargementStockage;

public class NovelActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private ListView listeChapitreLayout;
    private ChapitreArrayAdapter adapterChapitreList;
    private boolean synopsisAgrandi = false;
    private Novel novel;
    private ContenuNovel contenuNovel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel);

        ImageButton boutonFavori = findViewById(R.id.boutonFavori);
        boutonFavori.setOnClickListener(this);
        ImageButton boutonDownload = findViewById(R.id.boutonDownload);
        boutonDownload.setOnClickListener(view -> showPopupDownload());
        listeChapitreLayout = findViewById(R.id.ListChapitre);
        listeChapitreLayout.setOnItemClickListener(this);
        TextView synopsis = findViewById(R.id.synopsis);
        synopsis.setOnClickListener(this);
        TextView lireSuite = findViewById(R.id.lireSuite);
        lireSuite.setOnClickListener(this);

        novel = Commun.getNovelOuvert();

        ((TextView)findViewById(R.id.Titre)).setText(novel.getNom());

        if(FavoriStockage.estFavori(novel.getLien())){
            boutonFavori.setImageResource(R.mipmap.favori_icone);
        }

        if(Commun.isChargementHorsLigne()){
            findViewById(R.id.boutonDownload).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.Team)).setText("Site/Team : "+novel.getNomSiteTrad());
            if(novel.getSynopsis() != null && !novel.getSynopsis().equals("")) {
                ((TextView) findViewById(R.id.synopsis)).setText(novel.getSynopsis());
                findViewById(R.id.lireSuite).setVisibility(View.VISIBLE);
            }
            if(novel.getClassification() != null && !novel.getClassification().equals("")){
                ((TextView)findViewById(R.id.classification)).setText("Classification : " + novel.getClassification());
                findViewById(R.id.classification).setVisibility(View.VISIBLE);
            }
            if(novel.getAuteur() != null && !novel.getAuteur().equals("")){
                ((TextView)findViewById(R.id.traducteurAuteur)).setText("Auteur/traducteur : "+novel.getAuteur());
                findViewById(R.id.traducteurAuteur).setVisibility(View.VISIBLE);
            }
            ArrayList<Chapitre> listeChapitre = TelechargementStockage.getChapitreHorsLigne(novel);
            for (Chapitre c: listeChapitre) {
                novel.addChapitre(c);
            }
            adapterChapitreList = new ChapitreArrayAdapter(this, listeChapitre);
            listeChapitreLayout.setAdapter(adapterChapitreList);
            ((ImageView) findViewById(R.id.image)).setImageBitmap(novel.getImageBitmap());
            positionnerAChapLu();
        }
        else{
            if(novel.getSiteTrad() == null){
                ((TextView)findViewById(R.id.Team)).setText("Attention : La Team de traduction n'est pas reconnue");
            }
            else {
                ((TextView)findViewById(R.id.Team)).setText("Site/Team : "+novel.getNomSiteTrad());
                ((TextView)findViewById(R.id.synopsis)).setText(novel.getSiteTrad().getTexteChargement());

                contenuNovel = new ContenuNovel(novel, this);

                adapterChapitreList = new ChapitreArrayAdapter(this, new ArrayList());

                // setAdapter attache l'adaptateur à la vue
                listeChapitreLayout.setAdapter(adapterChapitreList);
            }

            if(!novel.getImageLien().equals("")){
                ImageView imageNovel = findViewById(R.id.image);
                Picasso.get().load(novel.getImageLien()).into(imageNovel);
            }
            else{
                findViewById(R.id.image).setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (listeChapitreLayout != null) {
            adapterChapitreList.notifyDataSetChanged();
        }
    }

    public void erreurChargement(){
        ((TextView)findViewById(R.id.synopsis)).setText("Erreur lors du chargement");
    }

    public void setSynopsis(String synopsis){
        ((TextView)findViewById(R.id.synopsis)).setText(synopsis);
        findViewById(R.id.lireSuite).setVisibility(View.VISIBLE);
    }

    public void setClassification(String classification){
        ((TextView)findViewById(R.id.classification)).setText("Classification : "+classification);
        findViewById(R.id.classification).setVisibility(View.VISIBLE);
    }

    public void setRythmeParution(String rythmeParution){
        ((TextView)findViewById(R.id.rythmeParution)).setText("Rythme de parution : "+rythmeParution);
        findViewById(R.id.rythmeParution).setVisibility(View.VISIBLE);
    }

    public void addAuteurTraducteur(String auteurTraducteur){
        if(((TextView)findViewById(R.id.traducteurAuteur)).getText().equals("")){
            ((TextView)findViewById(R.id.traducteurAuteur)).setText("Auteur/traducteur : "+auteurTraducteur);
            findViewById(R.id.traducteurAuteur).setVisibility(View.VISIBLE);
        }
        else{
            //retire le \n pour le remettre à la fin après avoir ajouté le nouveau traducteur/auteur
            ((TextView)findViewById(R.id.traducteurAuteur)).setText((((TextView)findViewById(R.id.traducteurAuteur)).getText()).toString().split("\n")[0]+", "+auteurTraducteur+"\n");
        }
    }

    public void aucunChapitreTrouve(){
        findViewById(R.id.messageAucunChapitre).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.messageAucunChapitre)).setText("\n\nAucun chapitre n'a été trouvé, vérifiez sur le site ("+novel.getSiteTrad().getLien()+") si c'est normal, si ce n'est pas le cas et que le problème persiste, merci de l'indiquer sur le discord");
    }

    public void addChapitre(Chapitre chapitre) {
        adapterChapitreList.add(chapitre);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.boutonFavori){
            if(FavoriStockage.estFavori(novel.getLien())){
                FavoriStockage.supprimerFavori(novel);
                ((ImageButton)findViewById(R.id.boutonFavori)).setImageResource(R.mipmap.favori_no_icone);
            }
            else{
                FavoriStockage.ajouterFavori(novel);
                // met l'image du bouton en plein
                ((ImageButton)findViewById(R.id.boutonFavori)).setImageResource(R.mipmap.favori_icone);
            }
        }
        else if(view.getId() == R.id.synopsis || view.getId() == R.id.lireSuite){
            TextView synopsis = (TextView) findViewById(R.id.synopsis);
            if (synopsisAgrandi){
                synopsis.setMaxLines(8);
                ((TextView)findViewById(R.id.lireSuite)).setText("toucher pour agrandir\n");
                synopsisAgrandi = false;
            }else{
                synopsis.setMaxLines(Integer.MAX_VALUE);
                ((TextView)findViewById(R.id.lireSuite)).setText("toucher pour réduire\n");
                synopsisAgrandi = true;
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Chapitre chapitre = (Chapitre) adapterView.getItemAtPosition(position);
        ChapitreLuStockage.marquerLu(chapitre);
        Commun.setchapitreOuvert(chapitre);
        //Démarrer l'activity LectureActivity
        startActivity(new Intent(this, LectureActivity.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(contenuNovel != null){
            contenuNovel.stopConnexion();
        }
    }

    private void showPopupDownload() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.popup_download);

        AlertDialog dialog = builder.create();
        dialog.show();
        EditText champChapMin = ((AlertDialog) dialog).findViewById(R.id.chapMin);
        EditText champChapMax = ((AlertDialog) dialog).findViewById(R.id.chapMax);
        champChapMin.setText(PositionChapitreStockage.getLastChapitreLu(novel)+"");
        champChapMax.setText(novel.getListeChapitres().size()+"");

        dialog.findViewById(R.id.boutonValider).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code à exécuter lorsque l'utilisateur clique sur le bouton Valider
                try {
                    int chapMin = Integer.parseInt(champChapMin.getText().toString());
                    int chapMax = Integer.parseInt(champChapMax.getText().toString());
                    // Appel de la fonction souhaitée avec les entrées de l'utilisateur
                    if(chapMin>chapMax){
                        TextView messageErreur = ((AlertDialog) dialog).findViewById(R.id.MessageErreurDownload);
                        messageErreur.setVisibility(View.VISIBLE);
                        messageErreur.setText("Le chapitre minimum doit être inférieur au chapitre maximum");
                    }
                    else if(chapMin<1){
                        TextView messageErreur = ((AlertDialog) dialog).findViewById(R.id.MessageErreurDownload);
                        messageErreur.setVisibility(View.VISIBLE);
                        messageErreur.setText("Le chapitre minimum doit être supérieur à 0");
                    }
                    else if(chapMax>adapterChapitreList.getCount()){
                        TextView messageErreur = ((AlertDialog) dialog).findViewById(R.id.MessageErreurDownload);
                        messageErreur.setVisibility(View.VISIBLE);
                        messageErreur.setText("Le chapitre maximum doit être inférieur ou égal au nombre de chapitre");
                    }
                    else{
                        // recup tous les chapitres indiqués par l'utilisateur
                        Commun.setStopTelechargement(false);
                        ArrayList<Chapitre> chapitres = new ArrayList<>();
                        for(int i=chapMin-1; i<chapMax; i++){
                            chapitres.add(adapterChapitreList.getItem(i));
                        }
                        // lance le téléchargement
                        TelechargementStockage.addNovelHorsLigne(novel);
                        new DownloadChapitresHorsLigne(chapitres, NovelActivity.this);

                        dialog.dismiss();
                    }
                } catch (NumberFormatException e) {
                    TextView messageErreur = ((AlertDialog) dialog).findViewById(R.id.MessageErreurDownload);
                    messageErreur.setVisibility(View.VISIBLE);
                    messageErreur.setText("Veuillez entrer des nombres valides");
                }
            }
        });

        dialog.findViewById(R.id.boutonAnnuler).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code à exécuter lorsque l'utilisateur clique sur le bouton annuler
                dialog.dismiss();
            }
        });
    }

    public void positionnerAChapLu(){
        // positionne la liste sur le dernier chapitre lu
        int chapitreLu = PositionChapitreStockage.getLastChapitreLu(novel);
        for(int i=0; i<adapterChapitreList.getCount(); i++){
            if(adapterChapitreList.getItem(i).getNumero() == chapitreLu){
                ((ListView)findViewById(R.id.ListChapitre)).setSelection(i);
            }
        }
    }
}