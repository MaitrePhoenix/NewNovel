package fr.maitrephoenix.newnovel.activity;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import fr.maitrephoenix.newnovel.objet.ConnexionIndication;

public class ConnexionAvecIndication extends android.os.AsyncTask<ConnexionIndication, Void, ConnexionIndication> {
    private Listeners appelleur;
    private InputStream connexion;
    private boolean stop = false;

    public interface Listeners {
        void onPostExecute(ConnexionIndication resultat);
    }

    public ConnexionAvecIndication(Listeners appelleur) {
        this.appelleur = appelleur;
    }

    @Override
    protected void onPostExecute(ConnexionIndication success) {
        super.onPostExecute(success);
        appelleur.onPostExecute(success);
    }

    @Override
    protected ConnexionIndication doInBackground(ConnexionIndication... indication) {
        T t = new T(indication[0]);
        t.start();
        while (!t.isFini() && !stop) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(stop){
            t.interrupt();
            return null;
        }
        return t.getResultat();
    }

    public void stopConnexion() {
        stop = true;
    }

    private class T extends Thread {
        private ConnexionIndication connexionIndication;
        private boolean fini  = false;

        public T(ConnexionIndication indication) {
            this.connexionIndication = indication;
        }

        public ConnexionIndication getResultat() {
            return connexionIndication;
        }

        public boolean isFini() {
            return fini;
        }

        @Override
        public void run() {
            String content = null;
            URLConnection connection = null;
            try {
                connection = new URL(connexionIndication.getLien()).openConnection();
                connexion = connection.getInputStream();
                Scanner scanner = new Scanner(connexion);
                scanner.useDelimiter("\\Z");
                content = scanner.next();
                scanner.close();

                connexionIndication.setPage(content);
                fini = true;
            } catch (Exception e) {
                connexionIndication = null;
                fini = true;
            }
        }
    }
}