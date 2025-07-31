package fr.maitrephoenix.newnovel.objet;

//contient les informations pour le bon fonctionnement de la connexion pour la liste des novels
public class ConnexionIndication {
    private static int connexionNonFinie = 0;

    private String lien;
    private String[] lienSplit;
    private SiteTrad siteTrad;
    private int numeroPage = 1;
    private String page;
    private boolean suite;

    public ConnexionIndication(String lien, String[] lienSplit, SiteTrad siteTrad) {
        this.lien = lien;
        this.lienSplit = lienSplit;
        this.siteTrad = siteTrad;
        this.suite = true;
        connexionNonFinie++;
    }

    public ConnexionIndication(String lien, SiteTrad siteTrad) {
        this.lien = lien;
        this.siteTrad = siteTrad;
        this.suite = false;
        connexionNonFinie++;
    }


    public static int getConnexionNonFinie() {
        return connexionNonFinie;
    }

    public static void connexionFinie() {
        connexionNonFinie--;
    }

    public static  void forcerAllConnexionFinie(){
        connexionNonFinie = 0;
    }


    public String getLien() {
        return lien;
    }

    public void setLien(String lien) {
        this.lien = lien;
    }

    public String[] getLienSplit() {
        return lienSplit;
    }

    public SiteTrad getSiteTrad() {
        return siteTrad;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public int getNumeroPage() {
        return numeroPage;
    }

    public boolean getSuite() {
        return suite;
    }

    public void incrementeNumeroPage() {
        this.numeroPage++;
    }
}
