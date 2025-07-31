package fr.maitrephoenix.newnovel;

import java.util.ArrayList;

import fr.maitrephoenix.newnovel.objet.SiteTrad;
import fr.maitrephoenix.newnovel.objet.SplitIndication;

public class ListeSiteTrad {
    private static ArrayList<SiteTrad> listeSiteTrad = new ArrayList<>();

    public static void initialiserSitesTrad() {
        if(listeSiteTrad.isEmpty()) {
            //chireads
            SiteTrad chiread = new SiteTrad("Chireads", "https://chireads.com", "HTML", "Chargement");
            chiread.addLienListeNovel("https://chireads.com/category/translatedtales/page/[nombre]/");
            chiread.addLienListeNovel("https://chireads.com/category/original/page/[nombre]/");
            chiread.addSplitIndicationListeNovel(new SplitIndication("<div class=\"romans-list-news\">", 1, ""));
            chiread.addSplitIndicationListeNovel(new SplitIndication("<ul>", 1, ""));
            chiread.addSplitIndicationListeNovel(new SplitIndication("</ul>", 0, ""));
            SplitIndication splitIndicationListe = new SplitIndication("<li>", 0, "");
            splitIndicationListe.addSousSplit(new SplitIndication("<a href=\"", 1, ""));
            splitIndicationListe.addSousSplit(new SplitIndication("\" title=\"", 1, "lien"));
            splitIndicationListe.addSousSplit(new SplitIndication("\" alt=\"", 1, "titre"));
            splitIndicationListe.addSousSplit(new SplitIndication("\"><img src=\"", 1, ""));
            splitIndicationListe.addSousSplit(new SplitIndication("\" alt", 1, "image"));
            chiread.addSplitIndicationListeNovel(splitIndicationListe);


            ArrayList<String> listeIndicationMultiple = new ArrayList<>();
            listeIndicationMultiple.add("Auteur : ");
            listeIndicationMultiple.add("Babelcheck : ");
            listeIndicationMultiple.add("Fantrad : ");
            listeIndicationMultiple.add("Traducteur : ");
            splitIndicationListe = new SplitIndication(listeIndicationMultiple, -1, "");
            splitIndicationListe.addSousSplit(new SplitIndication("</div>", 0, "auteur/traducteur"));
            chiread.addSplitIndicationContenuNovel(splitIndicationListe);
            chiread.addSplitIndicationContenuNovel(new SplitIndication("Statut de Parution : ", 1, ""));
            chiread.addSplitIndicationContenuNovel(new SplitIndication("</div>", 1, "parution"));
            chiread.addSplitIndicationContenuNovel(new SplitIndication("<div class=\"inform-intr-txt\">", 1, ""));
            chiread.addSplitIndicationContenuNovel(new SplitIndication("<span>\n", 1, ""));
            chiread.addSplitIndicationContenuNovel(new SplitIndication("</span>\n", 1, "synopsis"));
            chiread.addSplitIndicationContenuNovel(new SplitIndication("<div class=\"inform-annexe-list\">", 2, ""));
            chiread.addSplitIndicationContenuNovel(new SplitIndication("<ul>", 1, ""));
            chiread.addSplitIndicationContenuNovel(new SplitIndication("id=\"disqus thread", 0, ""));
            splitIndicationListe = new SplitIndication("<li>", 0, "");
            splitIndicationListe.addSousSplit(new SplitIndication("<a href=\"", 1, ""));
            splitIndicationListe.addSousSplit(new SplitIndication("\" class=\"", 1, "lien"));
            splitIndicationListe.addSousSplit(new SplitIndication("title=\"", 1, ""));
            splitIndicationListe.addSousSplit(new SplitIndication("\">", 1, ""));
            splitIndicationListe.addSousSplit(new SplitIndication("</a>", 1, "titre"));
            chiread.addSplitIndicationContenuNovel(splitIndicationListe);

            chiread.addSplitIndicationLecture(new SplitIndication("<div id=\"content\" class=\"read-txt\">", 1, ""));
            chiread.addSplitIndicationLecture(new SplitIndication("\n</div>", 0, "texte"));

            listeSiteTrad.add(chiread);


            //novel de glace
            SiteTrad novelDeGlace = new SiteTrad("Novel de Glace", "https://noveldeglace.com/", "HTML", "Le chargement des novels venant de ce site est possiblement très longs (jusqu'à 40s)");
            novelDeGlace.addLienListeNovel("https://noveldeglace.com/roman/");
            splitIndicationListe = new SplitIndication("<div class=\"su-list\" style=\"margin-left:0px\"></div>", 0, "");
            splitIndicationListe.addSousSplit(new SplitIndication("<h2 class=\"entry-title\">\r\n                                <a href=\"", 1, ""));
            splitIndicationListe.addSousSplit(new SplitIndication("\" title=\"", 1, "lien"));
            splitIndicationListe.addSousSplit(new SplitIndication("\" rel=\"bookmark\">", 1, ""));
            splitIndicationListe.addSousSplit(new SplitIndication("</a>", 1, "titre"));
            splitIndicationListe.addSousSplit(new SplitIndication("<img src=\"", 1, ""));
            splitIndicationListe.addSousSplit(new SplitIndication("\" alt=\"", 1, "image"));
            novelDeGlace.addSplitIndicationListeNovel(splitIndicationListe);

            novelDeGlace.addSplitIndicationContenuNovel(new SplitIndication("<strong>Classification", 1, ""));
            novelDeGlace.addSplitIndicationContenuNovel(new SplitIndication("\">", 2, ""));
            novelDeGlace.addSplitIndicationContenuNovel(new SplitIndication("</span>", 1, "classification"));
            novelDeGlace.addSplitIndicationContenuNovel(new SplitIndication("<div class=\"line_roman \"><strong>Rythme de sortie : </strong>", 1, "", true));
            novelDeGlace.addSplitIndicationContenuNovel(new SplitIndication("</div>", 1, "parution"));
            novelDeGlace.addSplitIndicationContenuNovel(new SplitIndication("data-title=\"Synopsis\"><div class=\"content\"><p>", 1, ""));
            novelDeGlace.addSplitIndicationContenuNovel(new SplitIndication("</p></div></div>", 1, "synopsis"));
            splitIndicationListe = new SplitIndication("<li class=\"chpt\"><a href=\"", 0, "");
            splitIndicationListe.addSousSplit(new SplitIndication("\">", 1, "lien"));
            splitIndicationListe.addSousSplit(new SplitIndication("</a>", 1, "titre"));
            novelDeGlace.addSplitIndicationContenuNovel(splitIndicationListe);

            novelDeGlace.addSplitIndicationLecture(new SplitIndication("</h2>", 1, ""));
            novelDeGlace.addSplitIndicationLecture(new SplitIndication("<div class=\"mistape_caption\">", 1, "texte"));

            listeSiteTrad.add(novelDeGlace);


            //error404
            SiteTrad error404 = new SiteTrad("Error404", "https://error404.fr/", "HTML", "Chargement");
            error404.addLienListeNovel("https://error404.fr/traduction-de-lights-et-web-novels/");
            error404.addSplitIndicationListeNovel(new SplitIndication("Les Œuvres en cours :", 1, ""));
            error404.addSplitIndicationListeNovel(new SplitIndication("Les Oeuvres terminées :", 0, ""));
            splitIndicationListe = new SplitIndication("<a href=\"", 0, "");
            splitIndicationListe.addSousSplit(new SplitIndication("\">", 1, "lien"));
            splitIndicationListe.addSousSplit(new SplitIndication("src=\"", 1, ""));
            splitIndicationListe.addSousSplit(new SplitIndication("\" class=\"", 1, "image"));
            splitIndicationListe.addSousSplit(new SplitIndication("<span class=\"elementor-title\"", 1, ""));
            splitIndicationListe.addSousSplit(new SplitIndication(">\n\t\t\t\t", 1, ""));
            splitIndicationListe.addSousSplit(new SplitIndication("</span>", 1, "titre"));
            error404.addSplitIndicationListeNovel(splitIndicationListe);

            error404.addSplitIndicationContenuNovel(new SplitIndication("<strong>Synopsis", 1, ""));
            error404.addSplitIndicationContenuNovel(new SplitIndication(":", 1, ""));
            error404.addSplitIndicationContenuNovel(new SplitIndication("Contient", 1, "synopsis"));
            error404.addSplitIndicationContenuNovel(new SplitIndication("style= »fancy »]</p>", 1, ""));
            error404.addSplitIndicationContenuNovel(new SplitIndication("<p>[/su_spoiler]</p>", 0, ""));
            splitIndicationListe = new SplitIndication("<p style=\"text-align: center;\"><a href=\"", 0, "");
            splitIndicationListe.addSousSplit(new SplitIndication("\">", 1, "lien"));
            splitIndicationListe.addSousSplit(new SplitIndication("</a>", 1, "titre"));
            error404.addSplitIndicationContenuNovel(splitIndicationListe);
            /*listeIndicationMultiple = new ArrayList<>();
            listeIndicationMultiple.add("<strong>Synopsis : </strong>");
            listeIndicationMultiple.add("<strong>Synopsis</strong> : ");
            listeIndicationMultiple.add("<strong>Synopsis :</strong> ");
            listeIndicationMultiple.add("<strong>Synopsis</strong> : ");
            listeIndicationMultiple.add("<strong>Synopsis :</strong> ");
            listeIndicationMultiple.add("<strong>Synopsis : </strong>");
            error404.addSplitIndicationContenuNovel(new SplitIndication(listeIndicationMultiple, 1, ""));
            error404.addSplitIndicationContenuNovel(new SplitIndication("Contient", 1, "synopsis"));
            listeIndicationMultiple = new ArrayList<>();
            listeIndicationMultiple.add("<div class=\"su-spoiler-content su-u-clearfix su-u-trim\">");
            listeIndicationMultiple.add("<p>Traduction d&rsquo;une œuvre originale de Sudar Zeremdulam, alias Kakemo, que");
            listeIndicationMultiple.add("<p style=\"text-align: center;\"><strong>Prologue : Monter les échelons en tant");
            splitIndicationListe = new SplitIndication(listeIndicationMultiple, 0, "");
            listeIndicationMultiple = new ArrayList<>();
            listeIndicationMultiple.add("</div></div>");
            listeIndicationMultiple.add("<p style=\"text-align: center;\"><a href=\"https://error404.fr/traduction-de");
            listeIndicationMultiple.add("<p style=\"text-align: center;\">Version originale en pause pour une durée indéterminée ~ </p>");
            splitIndicationListe.addSousSplit(new SplitIndication(listeIndicationMultiple, 0, ""));
            listeIndicationMultiple = new ArrayList<>();
            listeIndicationMultiple.add("<p style=\"text-align: center;\"><a href=\"");
            listeIndicationMultiple.add("<p style=\"text-align: center;\"><strong><a href=\"");
            SplitIndication splitIndicationListe2 = new SplitIndication("<p style=\"text-align: center;\"><a href=\"", 0, "");
            splitIndicationListe2.addSousSplit(new SplitIndication("\">", 1, "lien"));
            splitIndicationListe2.addSousSplit(new SplitIndication("</a>", 1, "titre"));
            splitIndicationListe.addSousSplit(splitIndicationListe2);
            error404.addSplitIndicationContenuNovel(splitIndicationListe);*/

            error404.addSplitIndicationLecture(new SplitIndication("<p style=\"text-align: center;\"><strong>", 1, ""));
            error404.addSplitIndicationLecture(new SplitIndication("</strong></p>", 1, ""));
            error404.addSplitIndicationLecture(new SplitIndication("<p style", 1, "texte"));

            listeSiteTrad.add(error404);


            //nyan team
            /*SiteTrad nyanTeam = new SiteTrad("Nyan Team", "https://nyan-team.albirew.fr", "HTML", "Chargement");
            nyanTeam.addLienListeNovel("https://nyan-team.albirew.fr/projects/");
            nyanTeam.addSplitIndicationListeNovel(new SplitIndication("<h2>Light novels</h2>", 1, ""));
            nyanTeam.addSplitIndicationListeNovel(new SplitIndication("<h2>Webtoons</h2>", 0, ""));
            splitIndicationListe = new SplitIndication("<div class='item'><a href='", 0, "");
            splitIndicationListe.addSousSplit(new SplitIndication("'><img src='", 1, "lien"));
            splitIndicationListe.addSousSplit(new SplitIndication("' width='", 1, "image"));
            splitIndicationListe.addSousSplit(new SplitIndication("' title='", 1, ""));
            splitIndicationListe.addSousSplit(new SplitIndication("' alt='", 1, "titre"));
            nyanTeam.addSplitIndicationListeNovel(splitIndicationListe);

            nyanTeam.addSplitIndicationContenuNovel(new SplitIndication("<span class=\"header\">Description</span><br>", 1, ""));
            nyanTeam.addSplitIndicationContenuNovel(new SplitIndication("<span class=\"description\">", 1, ""));
            nyanTeam.addSplitIndicationContenuNovel(new SplitIndication("</span>", 1, "synopsis"));
            nyanTeam.addSplitIndicationContenuNovel(new SplitIndication("<div id=\"release-container\">", 1, ""));
            splitIndicationListe = new SplitIndication("<span class=\"title\"><a name='release", 1, "");
            splitIndicationListe.addSousSplit(new SplitIndication("</a>", 1, ""));
            splitIndicationListe.addSousSplit(new SplitIndication("</span>", 1, "titre"));
            splitIndicationListe.addSousSplit(new SplitIndication("<a href='", 1, ""));
            splitIndicationListe.addSousSplit(new SplitIndication("' target=", 1, "lien"));
            nyanTeam.addSplitIndicationContenuNovel(splitIndicationListe);

            nyanTeam.addSplitIndicationLecture(new SplitIndication("<div class=\"entry-content\">", 1, ""));
            listeIndicationMultiple = new ArrayList<>();
            listeIndicationMultiple.add("<center style=\"font-size: 400%;\">");
            listeIndicationMultiple.add("<footer class=\"entry-footer clear\">");
            nyanTeam.addSplitIndicationLecture(new SplitIndication(listeIndicationMultiple, 1, "texte"));

            listeSiteTrad.add(nyanTeam);*/


            //novHell
            SiteTrad novHell = new SiteTrad("NovHell", "https://novhell.org", "HTML", "Chargement", true);
            novHell.addLienListeNovel("https://novhell.org");

            novHell.addSplitIndicationListeNovel(new SplitIndication("<article", 1, ""));
            novHell.addSplitIndicationListeNovel(new SplitIndication("</article>", 0, ""));
            splitIndicationListe = new SplitIndication("figure class=\"", 1, "");
            splitIndicationListe.addSousSplit(new SplitIndication("<a href=\"", 1, ""));
            splitIndicationListe.addSousSplit(new SplitIndication("\">", 1, "lien"));
            splitIndicationListe.addSousSplit(new SplitIndication("src=\"", 1, ""));
            splitIndicationListe.addSousSplit(new SplitIndication("\" alt=\"\"", 1, "image"));
            splitIndicationListe.addSousSplit(new SplitIndication("<strong>", 1, ""));
            splitIndicationListe.addSousSplit(new SplitIndication("</a>", 1, "titre"));
            novHell.addSplitIndicationListeNovel(splitIndicationListe);

            novHell.addSplitIndicationContenuNovel(new SplitIndication("<article", 1, ""));
            novHell.addSplitIndicationContenuNovel(new SplitIndication("</article>", 0, ""));
            novHell.addSplitIndicationContenuNovel(new SplitIndication("Traduction française : ", 1, ""));
            novHell.addSplitIndicationContenuNovel(new SplitIndication("</p>", 1, "auteur/traducteur"));
            novHell.addSplitIndicationContenuNovel(new SplitIndication("Synopsis</strong>", 1, ""));
            novHell.addSplitIndicationContenuNovel(new SplitIndication("<p>", 1, ""));
            novHell.addSplitIndicationContenuNovel(new SplitIndication("</p>", 1, "synopsis"));
            listeIndicationMultiple = new ArrayList<>();
            listeIndicationMultiple.add("<strong><a href=\"");
            listeIndicationMultiple.add("<p><a href=\"");
            splitIndicationListe = new SplitIndication(listeIndicationMultiple, 1, "");
            splitIndicationListe.addSousSplit(new SplitIndication("\">", 1, "lien"));
            splitIndicationListe.addSousSplit(new SplitIndication("</a>", 1, "titre"));
            novHell.addSplitIndicationContenuNovel(splitIndicationListe);

            novHell.addSplitIndicationLecture(new SplitIndication("<style>", 5, ""));
            novHell.addSplitIndicationLecture(new SplitIndication("<p>", 1, ""));
            novHell.addSplitIndicationLecture(new SplitIndication("</div>", 1, "texte"));



            listeSiteTrad.add(novHell);

        }
    }

    public static ArrayList<SiteTrad> getListeSiteTrad() {
        return listeSiteTrad;
    }

    public static SiteTrad getUnSiteTrad(String nomSite) {
        for (SiteTrad siteTrad : listeSiteTrad) {
            if (siteTrad.getNom().equals(nomSite)) {
                return siteTrad;
            }
        }
        return null;
    }
}
