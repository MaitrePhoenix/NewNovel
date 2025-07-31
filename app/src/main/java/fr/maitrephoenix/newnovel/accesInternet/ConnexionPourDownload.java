package fr.maitrephoenix.newnovel.accesInternet;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import fr.maitrephoenix.newnovel.objet.Chapitre;

public class ConnexionPourDownload extends android.os.AsyncTask<Chapitre, Void, Chapitre> {
    private Listeners appelleur;
    private InputStream connexion;

    public interface Listeners {
        void onPostExecute(Chapitre resultat);
    }

    public ConnexionPourDownload(Listeners appelleur){
        this.appelleur = appelleur;
    }

    @Override
    protected void onPostExecute(Chapitre success) {
        super.onPostExecute(success);
        appelleur.onPostExecute(success);
    }

    @Override
    protected Chapitre doInBackground(Chapitre... chapitre){
        String lien = chapitre[0].getLien();
        if(lien.split("https").length == 1){
            lien = "https" + lien.split("http")[1];
        }
        String content = null;
        URLConnection connection = null;
        try {
            connection = new URL(lien).openConnection();
            connexion = connection.getInputStream();
            Scanner scanner = new Scanner(connexion);
            scanner.useDelimiter("\\Z");
            content = scanner.next();
            scanner.close();

            chapitre[0].setPage(content);

            return chapitre[0];
        }
        catch (Exception e) {
            return null;
        }
    }

    public void stopConnexion() {
        if(connexion!=null){
            try {
                connexion.close();
            } catch (IOException e) {

            }
        }
    }
}
