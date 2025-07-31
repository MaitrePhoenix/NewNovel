package fr.maitrephoenix.newnovel.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fr.maitrephoenix.newnovel.ListeSiteTrad;
import fr.maitrephoenix.newnovel.R;
import fr.maitrephoenix.newnovel.arrayAdapter.SiteTradOptionsAdapter;
import fr.maitrephoenix.newnovel.objet.SiteTrad;
import fr.maitrephoenix.newnovel.stokage.SettingsStockage;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private SiteTradOptionsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        recyclerView = findViewById(R.id.listeSiteTradOptions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<SiteTrad> data = ListeSiteTrad.getListeSiteTrad();

        adapter = new SiteTradOptionsAdapter(data, SettingsStockage.getSiteExclu());
        recyclerView.setAdapter(adapter);

        //set listeners
        findViewById(R.id.boutonFavoris).setOnClickListener(this);
        findViewById(R.id.boutonListeNovel).setOnClickListener(this);
        findViewById(R.id.boutonTelechargement).setOnClickListener(this);
        findViewById(R.id.boutonOptions).setOnClickListener(this);
        findViewById(R.id.boutonDiscord).setOnClickListener(this);
        findViewById(R.id.btnDark).setOnClickListener(this);
        findViewById(R.id.btnLight).setOnClickListener(this);
        findViewById(R.id.btnCharger).setOnClickListener(this);
        findViewById(R.id.btnRecuperer).setOnClickListener(this);
        findViewById(R.id.btnValiderCharger).setOnClickListener(this);
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
                finish();
                startActivity(new Intent(this, TelechargementsActivity.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.boutonOptions:
                break;

            case R.id.boutonDiscord:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://discord.gg/BjpmmFAhGB"));
                startActivity(browserIntent);
                break;

            case R.id.btnDark:
                SettingsStockage.saveTheme(AppCompatDelegate.MODE_NIGHT_YES);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case R.id.btnLight:
                SettingsStockage.saveTheme(AppCompatDelegate.MODE_NIGHT_NO);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;

            case R.id.btnRecuperer:
                findViewById(R.id.textSave).setVisibility(View.VISIBLE);
                Button boutonRecuperer = (Button) findViewById(R.id.btnRecuperer);
                SpannableString contentRecup = new SpannableString("Récupérer");
                contentRecup.setSpan(new UnderlineSpan(), 0, contentRecup.length(), 0);
                boutonRecuperer.setText(contentRecup);

                ((Button) findViewById(R.id.btnCharger)).setText("Charger");
                findViewById(R.id.btnValiderCharger).setVisibility(View.GONE);

                ((EditText) findViewById(R.id.textSave)).setText(SettingsStockage.getAllSave());
                break;

            case R.id.btnCharger:
                findViewById(R.id.textSave).setVisibility(View.VISIBLE);
                Button boutonCharger = (Button) findViewById(R.id.btnCharger);
                SpannableString contentCharger = new SpannableString("Charger");
                contentCharger.setSpan(new UnderlineSpan(), 0, contentCharger.length(), 0);
                boutonCharger.setText(contentCharger);

                ((Button) findViewById(R.id.btnRecuperer)).setText("Récupérer");

                ((EditText) findViewById(R.id.textSave)).setText("");
                findViewById(R.id.btnValiderCharger).setVisibility(View.VISIBLE);

                break;

            case R.id.btnValiderCharger:
                //envoie un message de confirmation
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(true);
                builder.setTitle("Importer une sauvegarde");
                builder.setMessage("Êtes-vous sûr de vouloir importer cette sauvegarde ? Toutes les données actuelles seront perdues.\nPour le moment aucune vérification n'est faite par l'application avant d'importer les données, du coup en cas de données invalide des problèmes risquent survenir. \n\nPar sécurité, RECUPEREZ ET FAITES UNE BACKUP DES DONNEES ACTUELLES AVANT !!");
                builder.setPositiveButton("Confirmer",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                findViewById(R.id.textSave).setVisibility(View.GONE);
                                ((Button) findViewById(R.id.btnCharger)).setText("Charger");
                                SettingsStockage.overwriteAllSave(((EditText) findViewById(R.id.textSave)).getText().toString());
                                ((EditText) findViewById(R.id.textSave)).setText("");
                            }
                        });
                builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                break;
        }
    }
}