package fr.maitrephoenix.newnovel.activity.lecture;

import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import fr.maitrephoenix.newnovel.Commun;
import fr.maitrephoenix.newnovel.R;
import fr.maitrephoenix.newnovel.objet.Chapitre;
import fr.maitrephoenix.newnovel.objet.Novel;
import fr.maitrephoenix.newnovel.stokage.ChapitreLuStockage;
import fr.maitrephoenix.newnovel.stokage.PositionChapitreStockage;

public class LectureActivity extends AppCompatActivity implements View.OnClickListener {
    private Chapitre chapitre;
    private Novel novel;
    private RecupChapitre recupChapitre;

    private ScrollView scroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture);

        chapitre = Commun.getchapitreOuvert();
        novel = chapitre.getNovel();

        ((TextView)findViewById(R.id.TitreChapitre)).setText(chapitre.getTitre());


        Button bouttonPrecedent = findViewById(R.id.bouttonPrecedent);
        bouttonPrecedent.setOnClickListener(this);

        Button bouttonSuivant = findViewById(R.id.bouttonSuivant);
        bouttonSuivant.setOnClickListener(this);

        scroll = findViewById(R.id.scroll);
        //code appelé quand une fois qu'un element visuelle est chargé et à chaque fois que sa taille change
        scroll.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                scroll.setScrollY(PositionChapitreStockage.getPositionChapitre(chapitre));
            }
        });

        if(Commun.isChargementHorsLigne()){
            ((TextView)findViewById(R.id.texte)).setText(chapitre.getPage());
        }
        else{
            ((TextView)findViewById(R.id.texte)).setText(novel.getSiteTrad().getTexteChargement());
            recupChapitre = new RecupChapitre(chapitre, this);
        }
    }

    private void chargerChapitre(int numero){
        System.out.println(numero);
        chapitre = novel.getListeChapitres().get(numero);

        Commun.setchapitreOuvert(chapitre);
        ((TextView)findViewById(R.id.TitreChapitre)).setText(chapitre.getTitre());
        ((TextView)findViewById(R.id.texte)).setText(novel.getSiteTrad().getTexteChargement());
        findViewById(R.id.scroll).scrollTo(0,0);
        if(Commun.isChargementHorsLigne()){
            editTexte(chapitre.getPage());
        }
        else{
            new RecupChapitre(chapitre, this);
        }
    }

    public void erreurChargement() {
        ((TextView)findViewById(R.id.texte)).setText("Erreur lors du chargement, si le problème persiste, merci de signaler l'erreur sur le discord");
    }

    public void editTexte(String texte) {
        ((TextView)findViewById(R.id.texte)).setText("\n"+texte);
        scroll.setScrollY(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bouttonPrecedent:
                if(novel.getChapitrePosition(chapitre.getNumero())-1 >= 0){
                    if(Commun.isChargementHorsLigne()) {
                        //cela a pour objectif de prendre en compte l'écart si le premier chapitre téléchargé n'est pas le chapitre 0
                        ChapitreLuStockage.marquerLu(novel.getListeChapitres().get(novel.getChapitrePosition(chapitre.getNumero()) - 1));
                        chargerChapitre(novel.getChapitrePosition(chapitre.getNumero()) - 1);
                    }
                    else{
                        ChapitreLuStockage.marquerLu(novel.getListeChapitres().get(chapitre.getNumero()-2));
                        chargerChapitre(chapitre.getNumero()-2);
                    }
                }
                break;
            case R.id.bouttonSuivant:
                if(novel.getChapitrePosition(chapitre.getNumero())+1 < novel.getListeChapitres().size()){
                    if(Commun.isChargementHorsLigne()) {
                        //cela a pour objectif de prendre en compte l'écart si le premier chapitre téléchargé n'est pas le chapitre 0
                        ChapitreLuStockage.marquerLu(novel.getListeChapitres().get(novel.getChapitrePosition(chapitre.getNumero()) +1));
                        chargerChapitre(novel.getChapitrePosition(chapitre.getNumero()) + 1);
                    }
                    else{
                        ChapitreLuStockage.marquerLu(novel.getListeChapitres().get(chapitre.getNumero()));
                        chargerChapitre(chapitre.getNumero());
                    }
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!Commun.isChargementHorsLigne()){
            recupChapitre.stopConnexion();
        }
    }

    @Override
    protected void onPause() {
        PositionChapitreStockage.setPositionChapitre(chapitre, scroll.getScrollY());
        super.onPause();
    }
}