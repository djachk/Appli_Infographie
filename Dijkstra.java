import java.util.ArrayList;
import java.util.HashSet;

public class Dijkstra {
    /* Le tableau qui contient la hauteur simple de chaques points à la position x y */
    private int[][] carte;
    /* Une ArrayList qui contient à la positition i, une ArrayList de l'étiquette de ses voisins dans des tableaux à
     * 2 cases de la forme: {sommets d'arrivée, poids}  ex: {{126, 25}, {346, 36}, ...}
     * sauf s'il y a des sommets à ignorer, dans ce cas il y aura à la position i, des ArrayList vide.
     * Précision: poids est calculé par la distance euclidienne, càd racineCarré((x - x')^2 + (y - y')^2 + (z - z')^2) */
    private ArrayList<ArrayList<int[]>> aretes;
    /* Une Arraylist qui contient la liste des étiquettes des sommets, dans l'ordre du point d'arrivé au point de départ */
    private ArrayList<Integer> plusCourtChemin;
    /* Une HashSet qui contient l'étiquettes des sommets à ignorer, par ex ceux des lacs, mais aussi ceux des sommets
     * trop haut qui serons ajouté au fur et à mesure de la fonction constructListArete(hauteurAIgnorer)
     * Important: Cette structure, ainsi que celles dans la fonction de recherche du plus court chemin, à été choisie car
     * ça complexité en terme d'appel à la fonction contains est de O(1). */
    private HashSet<Integer> sommetsAIgnorer;

    /**
     * Constructeur de la méthode Dijkstra qui fait appelle à la fonction ReadFile, créée pour les premiers fichiers
     * générés automatiquement lors de nos testes avec une carte carré. Une fois l'appel au ReadFile terminé, elle
     * récupère la carte générée et la met dans le tableau de la méthode, appelé "carte".
     *
     * @param filename La chaîne de caractère qui représente le chemin vers le fichier.txt qui contient la carte
     */
    /*public Dijkstra(String filename) {
        ReadFile rf = new ReadFile(filename);
        rf.constructMap();
        this.carte = rf.getCarte();
        Carte.altCarte = rf.getCarte();
        for (int i = 0; i < carte.length; i++) {
            for (int j = 0; j < carte[0].length; j++) {
                Carte.eauCarte[i][j] = false;
            }
        }
        sommetsAIgnorer = new HashSet<>();
    }*/

    /**
     * Conctructeur de la méthode Dijkstra qui récupère un tableau avec la hauteur pour chaques sommets à la
     * position i et j, et la met dans le tableau de la méthode, appelé "carte".
     *
     * @param carte Un tableau d'entier à 2 dimensions repsrésentant les données de la carte
     */
    public Dijkstra(int[][] carte) {
        this.carte = carte;
        sommetsAIgnorer = new HashSet<>();
    }

    /**
     * Constructeur de la méthode Dijkstra qui récupère un tableau depuis la méthode Carte.
     */
    public Dijkstra() {
        carte = Carte.altCarte;
        sommetsAIgnorer = new HashSet<>();
    }

    /**
     * Fonction qui va remplir l'ArrayList d'arêtes en ignorant les points à ignorer, déjà données par la carte,
     * tel que les lacs, forêts, et autre... Mais elle ne prend pas en compte une hauteur à ignorer. Fonction à appeler
     * après création de l'objet Dijkstra.
     */
    public void setAretes() {
        sommetsAIgnorer = new HashSet<>(); // Initialisation de la HashMap quand c'est la 1ère utilisation, où pour écraser la précédente initialisation
        aretes = new ArrayList<>();        // Initialisation pour les mêmes raison que sommetsAIgnorer
        int pos;                           // Valeur de l'étiquettes à la position i j (càd (largeur * i) + j )
        ArrayList<int[]> al;               // Liste des voisins de l'étiquette correspondant à la position i j

        for (int i = 0; i < getLongueur(); i++) {
            for (int j = 0; j < getLargeur(); j++) {
                al = new ArrayList<>();
                if (!Carte.eauCarte[i][j]) { // on y va ssi ce n'est pas un lac où autre (donc pas un sommet à ignorer)
                    // Nord
                    if (i != 0) {
                        if (!Carte.eauCarte[i - 1][j]) { // on ajoute que si ce sommets n'est pas à ignorer, pareil pour les autres en dessous
                            pos = getSummitNameFromCoordinates(i - 1, j);
                            al.add(new int[]{pos, (int) Math.sqrt(Math.pow((i - (i - 1)), 2)
                                    + Math.pow((carte[i][j] - carte[i - 1][j]), 2))});
                        }

                        // Nord-Est
                        if (j < (getLargeur() - 1)) {
                            if (!Carte.eauCarte[i - 1][j + 1]) {
                                pos = getSummitNameFromCoordinates(i - 1, j + 1);
                                al.add(new int[]{pos, (int) Math.sqrt(Math.pow((i - (i - 1)), 2)
                                        + Math.pow((j - (j + 1)), 2)
                                        + Math.pow((carte[i][j] - carte[i - 1][j + 1]), 2))});
                            }
                        }

                        // Nord-Ouest
                        if (j != 0) {
                            if (!Carte.eauCarte[i - 1][j - 1]) {
                                pos = getSummitNameFromCoordinates(i - 1, j - 1);
                                al.add(new int[]{pos, (int) Math.sqrt(Math.pow((i - (i - 1)), 2)
                                        + Math.pow((j - (j - 1)), 2)
                                        + Math.pow((carte[i][j] - carte[i - 1][j - 1]), 2))});
                            }
                        }
                    }
                    // Est
                    if (j < (getLargeur() - 1)) {
                        if (!Carte.eauCarte[i][j + 1]) {
                            pos = getSummitNameFromCoordinates(i, j + 1);
                            al.add(new int[]{pos, (int) Math.sqrt(Math.pow((j - (j + 1)), 2)
                                    + Math.pow((carte[i][j] - carte[i][j + 1]), 2))});
                        }
                    }
                    // Sud
                    if (i < (getLongueur() - 1)) {
                        if (!Carte.eauCarte[i + 1][j]) {
                            pos = getSummitNameFromCoordinates(i + 1, j);
                            al.add(new int[]{pos, (int) Math.sqrt(Math.pow((i - (i + 1)), 2)
                                    + Math.pow((carte[i][j] - carte[i + 1][j]), 2))});
                        }

                        // Sud-Est
                        if (j < (getLargeur() - 1)) {
                            if (!Carte.eauCarte[i + 1][j + 1]) {
                                pos = getSummitNameFromCoordinates(i + 1, j + 1);
                                al.add(new int[]{pos, (int) Math.sqrt(Math.pow((i - (i + 1)), 2)
                                        + Math.pow((j - (j + 1)), 2)
                                        + Math.pow((carte[i][j] - carte[i + 1][j + 1]), 2))});
                            }
                        }

                        // Sud-Ouest
                        if (j != 0) {
                            if (!Carte.eauCarte[i + 1][j - 1]) {
                                pos = getSummitNameFromCoordinates(i + 1, j - 1);
                                al.add(new int[]{pos, (int) Math.sqrt(Math.pow((i - (i + 1)), 2)
                                        + Math.pow((j - (j - 1)), 2)
                                        + Math.pow((carte[i][j] - carte[i + 1][j - 1]), 2))});
                            }
                        }
                    }
                    // Ouest
                    if (j != 0) {
                        if (!Carte.eauCarte[i][j - 1]) {
                            pos = getSummitNameFromCoordinates(i, j - 1);
                            al.add(new int[]{pos, (int) Math.sqrt(Math.pow((j - (j - 1)), 2)
                                    + Math.pow((carte[i][j] - carte[i][j - 1]), 2))});
                        }
                    }
                }

                aretes.add(al); // ajout de la liste des voisins à la liste des aretes
            }
        }
    }

    /**
     * Fonction qui va remplir l'ArrayList d'arêtes en ignorant les points à ignorer, déjà données par la carte,
     * tel que les lacs, forêts, et autre... Mais également les hauteurs à ignorer qui serons détectés et ajoutées au
     * fur et à mesure du parcours de cette même fonction. Fonction à appeler après création de l'objet Dijkstra.
     *
     * @param hauteurAIgnorer Un entier représentant la hauteur des sommets à ignorer
     */
    public void setAretesHauteur(int hauteurAIgnorer) {
        sommetsAIgnorer = new HashSet<>();  // Initialisation de la HashMap quand c'est la 1ère utilisation, où pour écraser la précédente initialisation
        aretes = new ArrayList<>();         // Initialisation pour les mêmes raison que sommetsAIgnorer
        int pos;                            // Valeur de l'étiquettes à la position i j (càd (largeur * i) + j )
        ArrayList<int[]> al;                // Liste des voisins de l'étiquette correspondant à la position i j

        for (int i = 0; i < getLongueur(); i++) {
            for (int j = 0; j < getLargeur(); j++) {
                al = new ArrayList<>();
                if (carte[i][j] < hauteurAIgnorer) {  // on y va ssi la hauteur du point est strictement inférieur à la hauteur max à ignorer
                    if (!Carte.eauCarte[i][j]) { // on y va ssi ce n'est pas un lac, une montagne trop haute, où autre
                        // Nord
                        if (i != 0) {
                            if (!Carte.eauCarte[i - 1][j]) { // on y va que si ce n'est pas déjà un sommets à ignorer
                                pos = getSummitNameFromCoordinates(i - 1, j);
                                if (carte[i - 1][j] < hauteurAIgnorer) // on y va si la hauteur est respecté
                                    al.add(new int[]{pos, (int) Math.sqrt(Math.pow((i - (i - 1)), 2)
                                            + Math.pow((carte[i][j] - carte[i - 1][j]), 2))});
                                else // sinon on l'ajoute au sommets à ignorer, et il s'agit de la même chose par la suite
                                    sommetsAIgnorer.add(pos);
                            }

                            // Nord-Est
                            if (j < (getLargeur() - 1)) {
                                if (!Carte.eauCarte[i - 1][j + 1]) {
                                    pos = getSummitNameFromCoordinates(i - 1, j + 1);
                                    if (carte[i - 1][j + 1] < hauteurAIgnorer)
                                        al.add(new int[]{pos, (int) Math.sqrt(Math.pow((i - (i - 1)), 2)
                                                + Math.pow((j - (j + 1)), 2)
                                                + Math.pow((carte[i][j] - carte[i - 1][j + 1]), 2))});
                                    else
                                        sommetsAIgnorer.add(pos);
                                }
                            }

                            // Nord-Ouest
                            if (j != 0) {
                                if (!Carte.eauCarte[i - 1][j - 1]) {
                                    pos = getSummitNameFromCoordinates(i - 1, j - 1);
                                    if (carte[i - 1][j - 1] < hauteurAIgnorer)
                                        al.add(new int[]{pos, (int) Math.sqrt(Math.pow((i - (i - 1)), 2)
                                                + Math.pow((j - (j - 1)), 2)
                                                + Math.pow((carte[i][j] - carte[i - 1][j - 1]), 2))});
                                    else
                                        sommetsAIgnorer.add(pos);
                                }
                            }
                        }
                        // Est
                        if (j < (getLargeur() - 1)) {
                            if (!Carte.eauCarte[i][j + 1]) {
                                pos = getSummitNameFromCoordinates(i, j + 1);
                                if (carte[i][j + 1] < hauteurAIgnorer)
                                    al.add(new int[]{pos, (int) Math.sqrt(Math.pow((j - (j + 1)), 2)
                                            + Math.pow((carte[i][j] - carte[i][j + 1]), 2))});
                                else
                                    sommetsAIgnorer.add(pos);
                            }
                        }
                        // Sud
                        if (i < (getLongueur() - 1)) {
                            if (!Carte.eauCarte[i + 1][j]) {
                                pos = getSummitNameFromCoordinates(i + 1, j);
                                if (carte[i + 1][j] < hauteurAIgnorer)
                                    al.add(new int[]{pos, (int) Math.sqrt(Math.pow((i - (i + 1)), 2)
                                            + Math.pow((carte[i][j] - carte[i + 1][j]), 2))});
                                else
                                    sommetsAIgnorer.add(pos);
                            }

                            // Sud-Est
                            if (j < (getLargeur() - 1)) {
                                if (!Carte.eauCarte[i + 1][j + 1]) {
                                    pos = getSummitNameFromCoordinates(i + 1, j + 1);
                                    if (carte[i + 1][j + 1] < hauteurAIgnorer)
                                        al.add(new int[]{pos, (int) Math.sqrt(Math.pow((i - (i + 1)), 2)
                                                + Math.pow((j - (j + 1)), 2)
                                                + Math.pow((carte[i][j] - carte[i + 1][j + 1]), 2))});
                                    else
                                        sommetsAIgnorer.add(pos);
                                }
                            }

                            // Sud-Ouest
                            if (j != 0) {
                                if (!Carte.eauCarte[i + 1][j - 1]) {
                                    pos = getSummitNameFromCoordinates(i + 1, j - 1);
                                    if (carte[i + 1][j - 1] < hauteurAIgnorer)
                                        al.add(new int[]{pos, (int) Math.sqrt(Math.pow((i - (i + 1)), 2)
                                                + Math.pow((j - (j - 1)), 2)
                                                + Math.pow((carte[i][j] - carte[i + 1][j - 1]), 2))});
                                    else
                                        sommetsAIgnorer.add(pos);
                                }
                            }
                        }
                        // Ouest
                        if (j != 0) {
                            if (!Carte.eauCarte[i][j - 1]) {
                                pos = getSummitNameFromCoordinates(i, j - 1);
                                if (carte[i][j - 1] < hauteurAIgnorer)
                                    al.add(new int[]{pos, (int) Math.sqrt(Math.pow((j - (j - 1)), 2)
                                            + Math.pow((carte[i][j] - carte[i][j - 1]), 2))});
                                else
                                    sommetsAIgnorer.add(pos);
                            }
                        }
                    }
                } else // si c'est une hauteur trop haute, on l'ajoute aux sommets à ignorer
                    sommetsAIgnorer.add(getSummitNameFromCoordinates(i, j));
                aretes.add(al); // ajout de la liste des voisins à la liste des aretes, même vide pour garder une ArrayList avec le bon nombre de sommets permettant de faire un get des éléments
            }
        }
    }

    public void setAretesPente(double penteAIgnorer) {
        aretes = new ArrayList<>();         // Initialisation pour les mêmes raison que sommetsAIgnorer
        int pos;                            // Valeur de l'étiquettes à la position i j (càd (largeur * i) + j )
        ArrayList<int[]> al;                // Liste des voisins de l'étiquette correspondant à la position i j
        int distance;

        for (int i = 0; i < getLongueur(); i++) {
            for (int j = 0; j < getLargeur(); j++) {
                al = new ArrayList<>();
                if (!Carte.eauCarte[i][j]) { // on y va ssi ce n'est pas un lac, une montagne trop haute, où autre
                    // Nord
                    if (i != 0) {
                        if (!Carte.eauCarte[i - 1][j]) { // on y va que si ce n'est pas déjà un sommets à ignorer
                            distance = (int) Math.sqrt(Math.pow((i - (i - 1)), 2)
                                    + Math.pow((carte[i][j] - carte[i - 1][j]), 2));
                            if (((carte[i - 1][j] - carte[i][j]) / distance) <= penteAIgnorer) { // on y va si la hauteur est respecté
                                pos = getSummitNameFromCoordinates(i - 1, j);
                                al.add(new int[]{pos, distance});
                            }
                        }

                        // Nord-Est
                        if (j < (getLargeur() - 1)) {
                            if (!Carte.eauCarte[i - 1][j + 1]) {
                                distance = (int) Math.sqrt(Math.pow((i - (i - 1)), 2)
                                        + Math.pow((j - (j + 1)), 2)
                                        + Math.pow((carte[i][j] - carte[i - 1][j + 1]), 2));
                                if (((carte[i - 1][j + 1] - carte[i][j]) / distance) <= penteAIgnorer) {
                                    pos = getSummitNameFromCoordinates(i - 1, j + 1);
                                    al.add(new int[]{pos, distance});
                                }
                            }
                        }

                        // Nord-Ouest
                        if (j != 0) {
                            if (!Carte.eauCarte[i - 1][j - 1]) {
                                distance = (int) Math.sqrt(Math.pow((i - (i - 1)), 2)
                                        + Math.pow((j - (j - 1)), 2)
                                        + Math.pow((carte[i][j] - carte[i - 1][j - 1]), 2));
                                if (((carte[i - 1][j - 1] - carte[i][j]) / distance) <= penteAIgnorer) {
                                    pos = getSummitNameFromCoordinates(i - 1, j - 1);
                                    al.add(new int[]{pos, distance});
                                }
                            }
                        }
                    }
                    // Est
                    if (j < (getLargeur() - 1)) {
                        if (!Carte.eauCarte[i][j + 1]) {
                            distance = (int) Math.sqrt(Math.pow((j - (j + 1)), 2)
                                    + Math.pow((carte[i][j] - carte[i][j + 1]), 2));
                            if (((carte[i][j + 1] - carte[i][j]) / distance) <= penteAIgnorer) {
                                pos = getSummitNameFromCoordinates(i, j + 1);
                                al.add(new int[]{pos, distance});
                            }
                        }
                    }
                    // Sud
                    if (i < (getLongueur() - 1)) {
                        if (!Carte.eauCarte[i + 1][j]) {
                            distance = (int) Math.sqrt(Math.pow((i - (i + 1)), 2)
                                    + Math.pow((carte[i][j] - carte[i + 1][j]), 2));
                            if (((carte[i + 1][j] - carte[i][j]) / distance) <= penteAIgnorer) {
                                pos = getSummitNameFromCoordinates(i + 1, j);
                                al.add(new int[]{pos, distance});
                            }
                        }

                        // Sud-Est
                        if (j < (getLargeur() - 1)) {
                            if (!Carte.eauCarte[i + 1][j + 1]) {
                                distance = (int) Math.sqrt(Math.pow((i - (i + 1)), 2)
                                        + Math.pow((j - (j + 1)), 2)
                                        + Math.pow((carte[i][j] - carte[i + 1][j + 1]), 2));
                                if (((carte[i + 1][j + 1] - carte[i][j]) / distance) <= penteAIgnorer) {
                                    pos = getSummitNameFromCoordinates(i + 1, j + 1);
                                    al.add(new int[]{pos, distance});
                                }
                            }
                        }

                        // Sud-Ouest
                        if (j != 0) {
                            if (!Carte.eauCarte[i + 1][j - 1]) {
                                distance = (int) Math.sqrt(Math.pow((i - (i + 1)), 2)
                                        + Math.pow((j - (j - 1)), 2)
                                        + Math.pow((carte[i][j] - carte[i + 1][j - 1]), 2));
                                if (((carte[i + 1][j - 1] - carte[i][j]) / distance) <= penteAIgnorer) {
                                    pos = getSummitNameFromCoordinates(i + 1, j - 1);
                                    al.add(new int[]{pos, distance});
                                }
                            }
                        }
                    }
                    // Ouest
                    if (j != 0) {
                        if (!Carte.eauCarte[i][j - 1]) {
                            distance = (int) Math.sqrt(Math.pow((j - (j - 1)), 2)
                                    + Math.pow((carte[i][j] - carte[i][j - 1]), 2));
                            if (((carte[i][j - 1] - carte[i][j]) / distance) <= penteAIgnorer) {
                                pos = getSummitNameFromCoordinates(i, j - 1);
                                al.add(new int[]{pos, distance});
                            }
                        }
                    }
                }
                aretes.add(al); // ajout de la liste des voisins à la liste des aretes, même vide pour garder une ArrayList avec le bon nombre de sommets permettant de faire un get des éléments
            }
        }
    }

    public void setAretes(int hauteurAIgnorer, double penteAIgnorer) {
        sommetsAIgnorer = new HashSet<>();  // Initialisation de la HashMap quand c'est la 1ère utilisation, où pour écraser la précédente initialisation
        aretes = new ArrayList<>();         // Initialisation pour les mêmes raison que sommetsAIgnorer
        int pos;                            // Valeur de l'étiquettes à la position i j (càd (largeur * i) + j )
        ArrayList<int[]> al;                // Liste des voisins de l'étiquette correspondant à la position i j
        int distance;

        for (int i = 0; i < getLongueur(); i++) {
            for (int j = 0; j < getLargeur(); j++) {
                al = new ArrayList<>();
                if (carte[i][j] < hauteurAIgnorer) {  // on y va ssi la hauteur du point est strictement inférieur à la hauteur max à ignorer
                    if (!Carte.eauCarte[i][j]) { // on y va ssi ce n'est pas un lac, une montagne trop haute, où autre
                        // Nord
                        if (i != 0) {
                            if (!Carte.eauCarte[i - 1][j]) { // on y va que si ce n'est pas déjà un sommets à ignorer
                                pos = getSummitNameFromCoordinates(i - 1, j);
                                if (carte[i - 1][j] < hauteurAIgnorer) { // on y va si la hauteur est respecté
                                   distance = (int) Math.sqrt(Math.pow((i - (i - 1)), 2)
                                            + Math.pow((carte[i][j] - carte[i - 1][j]), 2));
                                   if (((carte[i - 1][j] - carte[i][j]) ) < penteAIgnorer) //modif distance en dist!!
                                        al.add(new int[]{pos, distance});
                                } else // sinon on l'ajoute au sommets à ignorer, et il s'agit de la même chose par la suite
                                    sommetsAIgnorer.add(pos);
                            }

                            // Nord-Est
                            if (j < (getLargeur() - 1)) {
                                if (!Carte.eauCarte[i - 1][j + 1]) {
                                    pos = getSummitNameFromCoordinates(i - 1, j + 1);
                                    if (carte[i - 1][j + 1] < hauteurAIgnorer) {
                                        distance = (int) Math.sqrt(Math.pow((i - (i - 1)), 2)
                                                + Math.pow((j - (j + 1)), 2)
                                                + Math.pow((carte[i][j] - carte[i - 1][j + 1]), 2));
                                        //if (((carte[i - 1][j + 1] - carte[i][j]) / distance) < penteAIgnorer)
                                        if (((carte[i - 1][j + 1] - carte[i][j]) / 1.414) < penteAIgnorer)
                                        	al.add(new int[]{pos, distance});
                                    } else
                                        sommetsAIgnorer.add(pos);
                                }
                            }

                            // Nord-Ouest
                            if (j != 0) {
                                if (!Carte.eauCarte[i - 1][j - 1]) {
                                    pos = getSummitNameFromCoordinates(i - 1, j - 1);
                                    if (carte[i - 1][j - 1] < hauteurAIgnorer) {
                                        distance = (int) Math.sqrt(Math.pow((i - (i - 1)), 2)
                                                + Math.pow((j - (j - 1)), 2)
                                                + Math.pow((carte[i][j] - carte[i - 1][j - 1]), 2));
                                        if (((carte[i - 1][j - 1] - carte[i][j]) / 1.414) < penteAIgnorer)
                                            al.add(new int[]{pos, distance});
                                    } else
                                        sommetsAIgnorer.add(pos);
                                }
                            }
                        }
                        // Est
                        if (j < (getLargeur() - 1)) {
                            if (!Carte.eauCarte[i][j + 1]) {
                                pos = getSummitNameFromCoordinates(i, j + 1);
                                if (carte[i][j + 1] < hauteurAIgnorer) {
                                    distance = (int) Math.sqrt(Math.pow((j - (j + 1)), 2)
                                            + Math.pow((carte[i][j] - carte[i][j + 1]), 2));
                                    if (((carte[i][j + 1] - carte[i][j])) < penteAIgnorer)
                                        al.add(new int[]{pos, distance});
                                } else
                                    sommetsAIgnorer.add(pos);
                            }
                        }
                        // Sud
                        if (i < (getLongueur() - 1)) {
                            if (!Carte.eauCarte[i + 1][j]) {
                                pos = getSummitNameFromCoordinates(i + 1, j);
                                if (carte[i + 1][j] < hauteurAIgnorer) {
                                    distance = (int) Math.sqrt(Math.pow((i - (i + 1)), 2)
                                            + Math.pow((carte[i][j] - carte[i + 1][j]), 2));
                                    if (((carte[i + 1][j] - carte[i][j])) < penteAIgnorer)
                                        al.add(new int[]{pos, distance});
                                } else
                                    sommetsAIgnorer.add(pos);
                            }

                            // Sud-Est
                            if (j < (getLargeur() - 1)) {
                                if (!Carte.eauCarte[i + 1][j + 1]) {
                                    pos = getSummitNameFromCoordinates(i + 1, j + 1);
                                    if (carte[i + 1][j + 1] < hauteurAIgnorer) {
                                        distance = (int) Math.sqrt(Math.pow((i - (i + 1)), 2)
                                                + Math.pow((j - (j + 1)), 2)
                                                + Math.pow((carte[i][j] - carte[i + 1][j + 1]), 2));
                                        if (((carte[i + 1][j + 1] - carte[i][j]) /1.414) < penteAIgnorer)
                                            al.add(new int[]{pos, distance});
                                    } else
                                        sommetsAIgnorer.add(pos);
                                }
                            }

                            // Sud-Ouest
                            if (j != 0) {
                                if (!Carte.eauCarte[i + 1][j - 1]) {
                                    pos = getSummitNameFromCoordinates(i + 1, j - 1);
                                    if (carte[i + 1][j - 1] < hauteurAIgnorer) {
                                        distance = (int) Math.sqrt(Math.pow((i - (i + 1)), 2)
                                                + Math.pow((j - (j - 1)), 2)
                                                + Math.pow((carte[i][j] - carte[i + 1][j - 1]), 2));
                                        if (((carte[i + 1][j - 1] - carte[i][j]) / 1.414) < penteAIgnorer)
                                            al.add(new int[]{pos, distance});
                                    } else
                                        sommetsAIgnorer.add(pos);
                                }
                            }
                        }
                        // Ouest
                        if (j != 0) {
                            if (!Carte.eauCarte[i][j - 1]) {
                                pos = getSummitNameFromCoordinates(i, j - 1);
                                if (carte[i][j - 1] < hauteurAIgnorer) {
                                    distance = (int) Math.sqrt(Math.pow((j - (j - 1)), 2)
                                            + Math.pow((carte[i][j] - carte[i][j - 1]), 2));
                                    if (((carte[i][j - 1] - carte[i][j]) ) < penteAIgnorer)
                                        al.add(new int[]{pos, distance});
                                } else
                                    sommetsAIgnorer.add(pos);
                            }
                        }
                    }
                } else // si c'est une hauteur trop haute, on l'ajoute aux sommets à ignorer
                    sommetsAIgnorer.add(getSummitNameFromCoordinates(i, j));
                aretes.add(al); // ajout de la liste des voisins à la liste des aretes, même vide pour garder une ArrayList avec le bon nombre de sommets permettant de faire un get des éléments
            }
        }
    }

    /**
     * Fonction qui parcoure la liste des sommets déjà vus et qui renvoi l'étiquette, représantant le sommet, avec le
     * poid le plus petit, qu'elle retirera de la liste des sommet déjà vu. Elle va également remplir dans une ArrayList
     * tout les autres sommets qui ont le même poid que le sommet au poid le plus petit, sans oublier de les retirer de
     * la liste des sommets déjà vus.
     *
     * @param dist Un tableau d'entier contenant la distance de chaques sommets
     * @param q    Une ArrayList contenant les sommets déjà vu
     * @param q2   Une ArrayList qui va contenir les sommets au même poids que le sommet au poid le plus petit
     * @return Un entier représentant l'étiquettes, représantant ici le sommet, au poid le plus petit
     */
    public int findMin(int[] dist, ArrayList<Integer> q, ArrayList<Integer> q2) {
        int min = -1;
        int sommet = -1;
        int pos = -1;

        for (int i = 0; i < q.size(); i++) {
            if (min == -1) {
                if (dist[q.get(i)] != min) {
                    min = dist[q.get(i)];
                    sommet = q.get(i);
                    pos = i;
                }
            } else {
                if (dist[q.get(i)] != -1 && dist[q.get(i)] < min) {
                    min = dist[q.get(i)];
                    sommet = q.get(i);
                    pos = i;
                }
            }
        }
        q.remove(pos);
        for (int i = 0; i < q.size(); i++) {
            if (dist[q.get(i)] == min) {
                q2.add(q.get(i));
                q.remove(i);
            }
        }
        return sommet;
    }

    /**
     * Fonction qui fait le parcour du plus court chemin et le renvoi.
     *
     * @param depart Un entier qui représente l'étiquette (ici le sommet) de départ
     * @param arrive Un entier qui représente l'étiquette (ici le sommet) d'arrivé
     * @return Une ArrayList d'entier qui contient tout les points du plus court chemin en incluant le point de départ et d'arrivé, la liste est ordonné du point d'arrivé au point de départ
     */
    public ArrayList<Integer> shortestPathAlgorithm(int depart, int arrive) {
        boolean pointDArriveAtteint = false;                    // Condition d'arrêt de la boucle for principale
        int[] dist = new int[getLongueur() * getLargeur()];     // Tableau des distances des points
        int[] pred = new int[dist.length];                      // Tableau des predecesseurs des points
        ArrayList<Integer> q = new ArrayList<>();               // Liste des sommets rencontrés
        ArrayList<Integer> q2 = new ArrayList<>();              // Liste des sommets rencontrés avec le même poid
        HashSet<Integer> alreadySeen = new HashSet<>();         // Liste des sommets que l'on a vu et retiré de la liste des sommets rencontrés
        HashSet<Integer> alreadyAdded = new HashSet<>();        // Liste des sommets déjà retirés, où toujours présents dans la liste des sommets rencontrés
        int voisinPlusPetit;                                    // Etiquette du voisin au poid le plus petit choisi
        ArrayList<int[]> listVoisinsDuVoisinPlusPetit;          // Liste des voisins du voisin au poid le plus petit
        int poid;                                               // Variable pour calculer le nouveau poid avec les nouvelles arêtes en plus, que l'on choisira ou non

        // Initialisation des variables
        for (int i = 0; i < dist.length; i++) {
            dist[i] = -1;
            pred[i] = -1;
        }
        dist[depart] = 0;
        q.add(depart); // ajout du sommet de départ à la liste des sommets vus
        alreadyAdded.add(depart); // ajout à la liste des sommets toujours présents dans la liste des sommets vus

        while (!pointDArriveAtteint && !q.isEmpty()) { // Si on a pas encore trouvé et choisi l'arete vers le point d'arrivé = false, sinon true
            if (q2.isEmpty()) { // s'il n'y a pas de sommets au poid similaires
                voisinPlusPetit = findMin(dist, q, q2); // chercher le prochain poid le plus petit
            } else { // sinon
                voisinPlusPetit = q2.get(0); // récupérer le point au suivant au même poid
                q2.remove(0);
            }
            listVoisinsDuVoisinPlusPetit = aretes.get(voisinPlusPetit); // récupérer ses voisin
            alreadySeen.add(voisinPlusPetit); // ajouter à la liste des déjà-vu

            for (int[] pointSuivant : listVoisinsDuVoisinPlusPetit) { // parcourir tout les voisins du voisin au poid le plus petit
                if (!alreadySeen.contains(pointSuivant[0])) { // si on l'a pas déjà vu
                    poid = dist[voisinPlusPetit] + pointSuivant[1]; // tester son poid
                    if ((dist[pointSuivant[0]] > poid) || (dist[pointSuivant[0]] == -1)) { // test si on met à jours dist et pred
                        dist[pointSuivant[0]] = poid; // mise à jour des distance
                        pred[pointSuivant[0]] = voisinPlusPetit; // et mise à jour des predecesseurs
                        if (voisinPlusPetit == arrive) { // si on a trouvé le sommet le plus petit on change le booléen d'arrêt
                            pointDArriveAtteint = true;
                            break;
                        }
                        if (!alreadyAdded.contains(pointSuivant)) // si on l'a pas déjà ajouté à la liste des déjà-ajouté, on l'ajoute
                            q.add(pointSuivant[0]);
                    }
                }
            }
        }
        // Après avoir trouvé le point d'arrivé
        int sommet = arrive;
        ArrayList<Integer> chemin = new ArrayList<>(); // on initialise le chemin

        if (pred[sommet] == -1) { // si c'est égal à -1 c'est qu'il n'y a pas de predecesseur à l'arrivé, donc aucun chemin pour y accéder
            System.out.println("Aucun chemin n'a pû être trouvé !");
            chemin.add(arrive);
            chemin.add(depart);
            return chemin;
        }

        while (pred[sommet] != depart) { // on parcoure et ajoute les predecesseurs jusqu'à arriver au point de départ
            chemin.add(sommet);
            sommet = pred[sommet];
        }

        if (dist[sommet] == 0) // puis on ajoute le point de départ
            chemin.add(sommet);

        return chemin; // on revoi le plus court chemin
    }

    /**
     * Fonction principale de recherche du plus court chemin. Cette méthode fait les testes nécessaire sur les points
     * pour savoir si les points sont supposé être accessible ou non avant de faire appelle ou non à la fonction
     * de recherche du plus court chemin.
     *
     * @param x1 Un entier représentant la coordonnée x du point de départ
     * @param y1 Un entier représentant la coordonnée y du point de départ
     * @param x2 Un entier représentant la coordonnée x du point d'arrivé
     * @param y2 Un entier représentant la coordonnée y du point d'arrivé
     */
    public ArrayList<Integer> searchShortestPath(int x1, int y1, int x2, int y2) {
        if (!Carte.eauCarte[x1][y1]) {
            if (!sommetsAIgnorer.contains(getSummitNameFromCoordinates(x1, y1))) { // on y va que si le point de départ ne fait pas partie des points à ignorer
                if (!Carte.eauCarte[x2][y2]) {
                    if (!sommetsAIgnorer.contains(getSummitNameFromCoordinates(x2, y2))) { // on y va que si le point d'arrivé ne fait pas partie des points à ignorer
                        if (x1 == x2 && y1 == y2) { // si le 1er point est le même que le second
                            this.plusCourtChemin = new ArrayList<>();
                            this.plusCourtChemin.add(getSummitNameFromCoordinates(x1, y1)); // on renvoi la liste avec 1 seul point
                            System.out.println("Vous être déjà là ou vous voulez vous rendre !");
                        } else {
                            this.plusCourtChemin = shortestPathAlgorithm(getSummitNameFromCoordinates(x1, y1),
                                    getSummitNameFromCoordinates(x2, y2)); // sinon on fait la recherche
                        }
                    } else {
                        if (x1 == x2 && y1 == y2) { // si le 1er point est le même que le second
                            this.plusCourtChemin = new ArrayList<>();
                            this.plusCourtChemin.add(getSummitNameFromCoordinates(x1, y1));
                        } else { // sinon on met les 2 points
                            this.plusCourtChemin = new ArrayList<>();
                            this.plusCourtChemin.add(getSummitNameFromCoordinates(x1, y1));
                            this.plusCourtChemin.add(getSummitNameFromCoordinates(x2, y2));
                        }
                        System.out.println("La 2ème coordonnée est sur une zone dont la hauteur est à ignorer !");
                    }
                } else {
                    if (x1 == x2 && y1 == y2) { // si le 1er point est le même que le second
                        this.plusCourtChemin = new ArrayList<>();
                        this.plusCourtChemin.add(getSummitNameFromCoordinates(x1, y1));
                    } else { // sinon on met les 2 points
                        this.plusCourtChemin = new ArrayList<>();
                        this.plusCourtChemin.add(getSummitNameFromCoordinates(x1, y1));
                        this.plusCourtChemin.add(getSummitNameFromCoordinates(x2, y2));
                    }
                    System.out.println("La 2ème coordonnée est sur l'eau !");
                }
            } else {
                if (x1 == x2 && y1 == y2) { // si le 1er point est le même que le second
                    this.plusCourtChemin = new ArrayList<>();
                    this.plusCourtChemin.add(getSummitNameFromCoordinates(x1, y1));
                } else { // sinon on met les 2 points
                    this.plusCourtChemin = new ArrayList<>();
                    this.plusCourtChemin.add(getSummitNameFromCoordinates(x1, y1));
                    this.plusCourtChemin.add(getSummitNameFromCoordinates(x2, y2));
                }
                System.out.println("La 1ère coordonnée est sur une zone dont la hauteur est à ignorer !");
            }
        } else {
            if (x1 == x2 && y1 == y2) { // si le 1er point est le même que le second
                this.plusCourtChemin = new ArrayList<>();
                this.plusCourtChemin.add(getSummitNameFromCoordinates(x1, y1));
            } else { // sinon on met les 2 points
                this.plusCourtChemin = new ArrayList<>();
                this.plusCourtChemin.add(getSummitNameFromCoordinates(x1, y1));
                this.plusCourtChemin.add(getSummitNameFromCoordinates(x2, y2));
            }
            System.out.println("La 1ère coordonnée est sur l'eau !");
        }
        return getShortestPath();
    }

    /**
     * Méthode get qui prend en argument la position (x, y), et va retourner l'étiquette correspondant par un calcul
     * simple, qui est (largeur de la carte * x) + y. On remarquera donc que pour récupérer le (x, y), il suffira
     * de faire une division euclidienne de l'étiquète par la largeur de la carte, à ce moment là, x sera le quotient,
     * et y sera le reste.
     *
     * @param x L'entier représentant la position x de la carte
     * @param y L'entier représentant la position y de la carte
     * @return Un entier représentant l'étiquette du point (x, y) de la carte
     */
    public int getSummitNameFromCoordinates(int x, int y) {
        return ((getLargeur() * x) + y);
    }

    /**
     * Méthode get qui renvoi la coordonnée x de l'étiquette passé en argument
     *
     * @param NameSumit Un entier représentant l'étiquette (ici le sommet)
     * @return Un entier représantant la coordonnée x de l'étiquette
     */
    public int getX(int NameSumit) {
        return (NameSumit / getLargeur());
    }

    /**
     * Méthode get qui renvoi la coordonnée y de l'étiquette passé en argument
     *
     * @param NameSumit Un entier représentant l'étiquette (ici le sommet)
     * @return Un entier représantant la coordonnée y de l'étiquette
     */
    public int getY(int NameSumit) {
        return (NameSumit % getLargeur());
    }

    /**
     * Méthode get qui renvoi la liste du plus court chemin entre 2 points choisie de la carte
     *
     * @return Une ArrayList d'entier qui représente les points de passages du chemin le plus court
     */
    public ArrayList<Integer> getShortestPath() {
        return this.plusCourtChemin;
    }

    /**
     * Méthode get qui renvoi la hauteur/longueur de la carte
     *
     * @return Un entier représantant la hauteur/longueur de la carte
     */
    public int getLongueur() {
        return carte.length;
    }

    /**
     * Méthode get qui renvoi la largeur de la carte
     *
     * @return Un entier représantant la largeur de la carte
     */
    public int getLargeur() {
        return carte[0].length;
    }
}
