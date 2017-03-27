package fr.umlv.graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
/** SeamCarving permet d'écrire, de lire et de réalisation des opération avec des fichiers .pgm
 *
 */
public class SeamCarving {
  
	@SuppressWarnings("resource")
	/** Methode qui renvoie un int[][] qui représente une image à partir d'un fichier .pgm 
	 * @param path Le chemin d'acces du fichier.pgm 
	 * @return Un int[][] qui représente l'image du fichier .pgm
	 */
	public static int[][] readpgm(Path path) throws IOException {
    
		try(BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			reader.readLine();  // magic
      
			String line = reader.readLine();
			while (line.startsWith("#")) {
				line = reader.readLine();
			}
      
			Scanner scanner = new Scanner(line);
			int width = scanner.nextInt();
			int height = scanner.nextInt();
      
			line = reader.readLine();
			scanner = new Scanner(line);
			scanner.nextInt();  // maxVal
      
			int[][] im = new int[height][width];
			scanner = new Scanner(reader);
			int count = 0;
			while (count < height * width) {
				im[count / width][count % width] = scanner.nextInt();
				count++;
			}
			return im;
		}
	}
	/** Methode qui créer un fichier .pgm à partir d'un int[][] qui représente une image
	 * @param image L'image à écrire dans le nouveau fichier
	 * @param filename Le chemin d'acces de la nouvelle 'image au format .pgm 
	 */
	public static void writepgm(int[][] image, String filename){
		File f = new File(filename);
		try{
			FileWriter fw = new FileWriter (f);
			
			fw.write("P2\n");
			fw.write(String.valueOf(image[0].length)+" "+String.valueOf(image.length)+"\n");
			fw.write(String.valueOf(255)+"\n");
			
			for(int i[] : image){
				for(int j : i)
					fw.write(String.valueOf(j)+" ");
				fw.write("\n");
			}
			fw.close();
		}
		
		catch(IOException exception){
			System.out.println ("Erreur lors de la lecture : " + exception.getMessage());
		}
	}
	
	/** Methode qui renvoie un int[][] contenant le facteur d'interet de chaque pixel de l'image fournit en parametre
	 * @param image image à analyser
	 * @return  int[][] contenant les facteurs d'interets de chaque pixel
	 */
	public static int[][] interest (int [][] image){
		int inter[][] = new int [image.length][image[0].length];
		for(int i = 0; i<inter.length; i++){
			for(int j = 0; j<inter[i].length; j++){
				if(j == 0)
					inter[i][j] = Math.abs(image[i][j] - image[i][j+1]);
				else if(j+1 == inter[i].length)
					inter[i][j] = Math.abs(image[i][j] - image[i][j-1]);
				else
					inter[i][j] = Math.abs(image[i][j] - (image[i][j+1] + image[i][j-1]) / 2);
			}	
		}
		return inter;
	}
	
	/** Methode qui renvoie une nouvelle image selon les instructions de decoupage reçu en parametre. 
	 * @param imageOriginal L'image original qui va etre allégée d'un pixel par ligne
	 * @param indiceCoupe Tableau d'entier qui contient les indices de decoupe de chaque ligne.

	 * @return Une nouvelle image crée à partir de imageOriginal 
	 */
	public static int[][] cutImage(int[][] imageOriginal, int[] indiceCoupe){
		int[][] newImage = new int[imageOriginal.length][imageOriginal[0].length -1];
		
		for(int i = 0; i < imageOriginal.length; i++){
			/*if(i < imageOriginal.length-1 && Math.abs(indiceCoupe[i]-indiceCoupe[i+1]) > 1)
				System.out.println(indiceCoupe[i]+" "+ indiceCoupe[i+1]);*/
			for(int j = 0, j2 = 0; j < imageOriginal[0].length && j2 < newImage[0].length; j++){
				if (j != indiceCoupe[i]){
					newImage[i][j2] = imageOriginal[i][j];
					j2++;
				}
			}
		}
		return newImage;
	}
	
}
