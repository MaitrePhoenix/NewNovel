package fr.maitrephoenix.newnovel.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.ArrayList;
import java.util.Comparator;

import fr.maitrephoenix.newnovel.Commun;
import fr.maitrephoenix.newnovel.ListeNovel;
import fr.maitrephoenix.newnovel.ListeSiteTrad;
import fr.maitrephoenix.newnovel.R;
import fr.maitrephoenix.newnovel.activity.FavoriActivity;
import fr.maitrephoenix.newnovel.activity.SettingsActivity;
import fr.maitrephoenix.newnovel.activity.TelechargementsActivity;
import fr.maitrephoenix.newnovel.activity.novel.NovelActivity;
import fr.maitrephoenix.newnovel.arrayAdapter.NovelArrayAdapter;
import fr.maitrephoenix.newnovel.objet.Novel;
import fr.maitrephoenix.newnovel.stokage.BasicStockage;
import fr.maitrephoenix.newnovel.stokage.SettingsStockage;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener, SearchView.OnQueryTextListener {
    private ListView listeNovelLayout;
    private RecupNovel recupNovel;
    private NovelArrayAdapter adapterNovelList;

    ArrayList<Novel> listeNovel = new ArrayList<>();

    private boolean trieAlphabetique = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BasicStockage.initialiserStockage(this);

        AppCompatDelegate.setDefaultNightMode(SettingsStockage.getTheme());

        ListeSiteTrad.initialiserSitesTrad();

        adapterNovelList = new NovelArrayAdapter(this, new ArrayList());

        listeNovelLayout = (ListView) findViewById(R.id.listNovel);
        listeNovelLayout.setAdapter(adapterNovelList);

        if(Commun.getchargementNovelFini()){
            ((TextView) findViewById(R.id.nbrNovel)).setText("Nombre de novel : " + ListeNovel.getNovels().size());
            adapterNovelList.addAll(ListeNovel.getNovels());
            adapterNovelList.notifyDataSetChanged();
        }
        else{
            recupNovel = new RecupNovel(this);
        }

        listeNovelLayout.setOnItemClickListener(this);

        findViewById(R.id.boutonFavoris).setOnClickListener(this);
        findViewById(R.id.boutonListeNovel).setOnClickListener(this);
        findViewById(R.id.boutonTelechargement).setOnClickListener(this);
        findViewById(R.id.boutonOptions).setOnClickListener(this);
        ((SearchView) findViewById(R.id.recherche)).setOnQueryTextListener(this);
        findViewById(R.id.trier).setOnClickListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(!Commun.getchargementNovelFini()){
            ListeNovel.clear();
        }
        recupNovel.arreterChargement();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(recupNovel == null || recupNovel.getArretForce()){
            findViewById(R.id.chargementtxt).setVisibility(View.VISIBLE);
            findViewById(R.id.layoutRecherche).setVisibility(View.GONE);
            recupNovel = new RecupNovel(this);
            adapterNovelList = new NovelArrayAdapter(this, new ArrayList());
            ((TextView)findViewById(R.id.nbrNovel)).setText("Nombre de novels : 0");
            listeNovelLayout.setAdapter(adapterNovelList);
        }
    }

    public void addNovel(Novel novel) {
        adapterNovelList.add(novel);
        listeNovel.add(novel);
        ((TextView)findViewById(R.id.nbrNovel)).setText("Nombre de novels : " + adapterNovelList.getCount());
    }

    public void finChargement() {
        Commun.chargementNovelFini();
        findViewById(R.id.chargementtxt).setVisibility(View.GONE);
        findViewById(R.id.layoutRecherche).setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Novel novel = (Novel) adapterView.getItemAtPosition(position);

        if(novel != null){
            //Démarrer l'activity NovelActivity
            Commun.setChargementHorsLigne(false);
            Commun.setNovelOuvert(novel);
            startActivity(new Intent(this, NovelActivity.class));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.boutonFavoris:
                startActivity(new Intent(this, FavoriActivity.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.boutonListeNovel:
                break;
            case R.id.boutonOptions:
                startActivity(new Intent(this, SettingsActivity.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.boutonTelechargement:
                startActivity(new Intent(this, TelechargementsActivity.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.trier:
                if(trieAlphabetique){
                    ((Button)findViewById(R.id.trier)).setText("Trier (A-Z)");
                    adapterNovelList.clear();
                    listeNovel.sort(Comparator.comparing(n -> n.getSiteTrad().getNom()));
                    adapterNovelList.addAll(listeNovel);
                    trieAlphabetique = false;
                }
                else{
                    ((Button)findViewById(R.id.trier)).setText("Trier (Site)");
                    adapterNovelList.sort(Comparator.naturalOrder());
                    trieAlphabetique = true;
                }
                break;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    //quand on recherche un novel
    @Override
    public boolean onQueryTextChange(String s) {
        ArrayList<Novel> liste = ListeNovel.getListeNovel();
        ArrayList<Novel> listeFiltre = (ArrayList<Novel>) liste.clone();
        String[] sSplits = s.split(" ");
        for (Novel novel : liste) {
            for (String sSplit : sSplits) {
                if (!novel.getNom().toLowerCase().contains(sSplit.toLowerCase()) && !novel.getNomSiteTrad().toLowerCase().contains(sSplit.toLowerCase())) {
                    listeFiltre.remove(novel);
                }
            }
        }
        adapterNovelList.clear();
        adapterNovelList.addAll(listeFiltre);
        adapterNovelList.sort(Comparator.naturalOrder());
        ((TextView)findViewById(R.id.nbrNovel)).setText("Nombre de novels : " + adapterNovelList.getCount());
        return false;
    }
}