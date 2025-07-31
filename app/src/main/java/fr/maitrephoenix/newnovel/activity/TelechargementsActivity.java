package fr.maitrephoenix.newnovel.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import fr.maitrephoenix.newnovel.Commun;
import fr.maitrephoenix.newnovel.R;
import fr.maitrephoenix.newnovel.activity.novel.DownloadChapitresHorsLigne;
import fr.maitrephoenix.newnovel.activity.novel.NovelActivity;
import fr.maitrephoenix.newnovel.arrayAdapter.NovelArrayAdapter;
import fr.maitrephoenix.newnovel.objet.Novel;
import fr.maitrephoenix.newnovel.stokage.TelechargementStockage;

public class TelechargementsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {
    NovelArrayAdapter adapterDownloadList;
    boolean longClickActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telechargement);
        ArrayList<Novel> listeNovel = TelechargementStockage.getAllNovelHorsLigne();
        adapterDownloadList = new NovelArrayAdapter(this, listeNovel);
        ListView listView = findViewById(R.id.listNovelTelecharge);
        listView.setAdapter(adapterDownloadList);

        ((TextView)findViewById(R.id.nbrDownload)).setText("nombre de  novels téléchargés : "+listeNovel.size()+"\nMaintenez enfoncé pour supprimer un novel");

        findViewById(R.id.boutonFavoris).setOnClickListener(this);
        findViewById(R.id.boutonListeNovel).setOnClickListener(this);
        findViewById(R.id.boutonTelechargement).setOnClickListener(this);
        findViewById(R.id.boutonOptions).setOnClickListener(this);

        listView.setOnItemLongClickListener(this);
        listView.setOnItemClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.boutonFavoris:
                finish();
                startActivity(new Intent(this, FavoriActivity.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.boutonListeNovel:
                finish();
                overridePendingTransition(0, 0);
                break;
            case R.id.boutonTelechargement:
                break;
            case R.id.boutonOptions:
                finish();
                startActivity(new Intent(this, SettingsActivity.class));
                overridePendingTransition(0, 0);
                break;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        longClickActive = true;
        Novel novel = (Novel) adapterView.getItemAtPosition(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Êtes vous sur de vouloir supprimer \""+ novel.getNom() +"\" de vos téléchargements?")
                .setTitle("Supprimer")
                .setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        TelechargementStockage.delNovelTelecharge(novel);
                        adapterDownloadList.remove(novel);
                        Commun.setStopTelechargement(true);
                        DownloadChapitresHorsLigne.stopConnexion();
                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        longClickActive = false;
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if(longClickActive){
            return;
        }
        Novel novel = (Novel) adapterView.getItemAtPosition(position);

        //Démarrer l'activity NovelActivity
        Commun.setChargementHorsLigne(true);
        Commun.setNovelOuvert(novel);
        startActivity(new Intent(this, NovelActivity.class));
    }
}