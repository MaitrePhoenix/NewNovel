package fr.maitrephoenix.newnovel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import fr.maitrephoenix.newnovel.Commun;
import fr.maitrephoenix.newnovel.ListeNovel;
import fr.maitrephoenix.newnovel.R;
import fr.maitrephoenix.newnovel.activity.novel.NovelActivity;
import fr.maitrephoenix.newnovel.arrayAdapter.NovelArrayAdapter;
import fr.maitrephoenix.newnovel.objet.Novel;
import fr.maitrephoenix.newnovel.stokage.FavoriStockage;

public class FavoriActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favori);
        ArrayList<Novel> listeFavori = FavoriStockage.getFavoris();
        ((TextView) findViewById(R.id.nbrFavoris)).setText("Nombre de favoris : " + listeFavori.size());

        NovelArrayAdapter adapterFavoriList = new NovelArrayAdapter(this, listeFavori);
        ListView listeFavoriLayout = (ListView) findViewById(R.id.listeFavori);
        listeFavoriLayout.setAdapter(adapterFavoriList);

        listeFavoriLayout.setOnItemClickListener(this);

        for (Novel novel:listeFavori) {
            ListeNovel.addNovel(novel);
        }

        findViewById(R.id.boutonFavoris).setOnClickListener(this);
        findViewById(R.id.boutonListeNovel).setOnClickListener(this);
        findViewById(R.id.boutonTelechargement).setOnClickListener(this);
        findViewById(R.id.boutonOptions).setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Novel novel = (Novel) adapterView.getItemAtPosition(position);

        //Démarrer l'activity NovelActivity
        Commun.setChargementHorsLigne(false);
        Commun.setNovelOuvert(novel);
        startActivity(new Intent(this, NovelActivity.class));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.boutonFavoris:
                break;
            case R.id.boutonListeNovel:
                finish();
                overridePendingTransition(0, 0);
                break;
            case R.id.boutonTelechargement:
                finish();
                startActivity(new Intent(this, TelechargementsActivity.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.boutonOptions:
                finish();
                startActivity(new Intent(this, SettingsActivity.class));
                overridePendingTransition(0, 0);
                break;
        }
    }
}