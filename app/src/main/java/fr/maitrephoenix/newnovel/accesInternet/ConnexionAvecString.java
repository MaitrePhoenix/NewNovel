package fr.maitrephoenix.newnovel.accesInternet;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class ConnexionAvecString extends android.os.AsyncTask<String, Void, String> {
    private Listeners appelleur;
    private InputStream connexion;

    public interface Listeners {
        void onPostExecute(String resultat);
    }

    public ConnexionAvecString(Listeners appelleur){
        this.appelleur = appelleur;
    }

    @Override
    protected void onPostExecute(String success) {
        super.onPostExecute(success);
        appelleur.onPostExecute(success);
    }

    @Override
    protected String doInBackground(String... lien){
        //pour un site en particulier
        if(lien[0].split("https").length == 1){
            lien[0] = "https" + lien[0].split("http")[1];
        }
        String content = null;
        URLConnection connection = null;
        try {
            connection = new URL(lien[0]).openConnection();
            connexion = connection.getInputStream();
            Scanner scanner = new Scanner(connexion);
            scanner.useDelimiter("\\Z");
            content = scanner.next();
            scanner.close();

            return content;
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
