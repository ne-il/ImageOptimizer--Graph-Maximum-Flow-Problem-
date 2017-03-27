package fr.umlv.graph;

import java.io.IOException;
import java.nio.file.Paths;



public class Main {

	public static void main(String[] args) throws IOException {
		int image[][] = SeamCarving.readpgm(Paths.get("ex1.pgm"));
		int interest[][];
		int imageCoupe[][] ;
		int tabIndice[];
		Graph graph ;
		
		for(int i = 0; i < 50; i++){
//		  SeamCarving.interest lit l'image et calcul le facteur d'interet de chaque pixels. On met le resultat dans un tableau 2D interest. 

			interest = SeamCarving.interest(image);
			
//			Maintenant on va utiliser la methode Graph.toGraph(interest) Pour obtenir un Graph à partir des facteurs d'interets*/

			graph = Graph.toGraph(interest);
			
//			On initialise le graphe à une valeur de flot "raisonable" qu'on va ensuite chercher à augmenter avec Ford-Fulkerson

			graph.initGraph(interest);
			
/*Et hop! on augmente le flot avec Ford-Fu*/

			graph.calculFlotMax();
			tabIndice = graph.coupeMinimal(interest.length, interest[0].length);
			imageCoupe = SeamCarving.cutImage(image, tabIndice);
			image = imageCoupe;
			System.out.println("Colonne "+ i);
		}
		
		SeamCarving.writepgm(image, "result.pgm");
		System.out.println("Fin!");
	}

}
