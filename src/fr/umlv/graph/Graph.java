package fr.umlv.graph;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
/**
 * Un graph est représenté par une liste de liste de Edges qui s'organise de la sorte.
 * 
 * 	A l'indice i de adjacenyList, il y a une liste contenant tout les edges comprenant le sommet i en source ou en destination
 * 
 * @author ne-il
 *
 */
public class Graph {
	
  private final ArrayList<ArrayList<Edge>> adjacenyList;
  
  /** Constructeur de Graph 
	 * @param vertexCount Le nombre de sommet du graph
	 */
  public Graph(int vertexCount) {
    adjacenyList = new ArrayList<>(vertexCount);
    for (int v = 0; v < vertexCount; v++) {
      adjacenyList.add(new ArrayList<>());
    }
  }
  /** Methode qui renvoie le nombre de sommet du graph
	 * @return nombre de sommet du graph
	 */
  public int vertices() {
    return adjacenyList.size();
  }
  
/*Ajoute dans l'adjacenyList un edge à l'indice d'origine et de destination*/
  public void addEdge(Edge edge) {
    adjacenyList.get(edge.from).add(edge);
    adjacenyList.get(edge.to).add(edge);
  }
  
/** Methode qui renvoie la liste des edges dont le sommet vertex est l'origine ou la destination
	 * @param vertex sommet étudié
	 * @return La liste des edges dont le sommet vertex est l'origine ou la destination
*/
  public Iterable<Edge> adjacent(int vertex) {
    return adjacenyList.get(vertex);
  }
  
  
  /** Methode qui renvoie la liste sans doublons de tout les edges du graph 
	 * @return La liste sans doublons de tout les edges du graph 
*/
  public Iterable<Edge> edges() {
    ArrayList<Edge> list = new ArrayList<>();
    for (int vertex = 0; vertex < vertices(); vertex++) {
      for (Edge edge : adjacent(vertex)) {
        if (edge.to != vertex) {
          list.add(edge);
        }
      }
    }
    return list;
  }
  
  
/*initialise le tableau d'intérêt*/
  
  /** Methode qui crée un graph à partir d'un tableau de facteur d'interet
   * @param itr Tableau de facteur d'interet
	 * @return Le graph crée à partir des facteurs d'interet
*/
  public static Graph toGraph(int itr[][]){
	  Graph graph = new Graph((itr.length * itr[0].length) + 2);
	  for(int i = 0; i < itr.length; i++){
		  graph.addEdge(new Edge(itr.length * itr[0].length, itr[0].length*i, 1000, 0));
		  for(int j = 0; j < itr[0].length; j++){
			  if(j+1 < itr[0].length)
				  graph.addEdge(new Edge(itr[0].length*i + j, itr[0].length*i + j + 1, itr[i][j], 0));
			  if(j == itr[0].length - 1)
				  graph.addEdge(new Edge(itr[0].length*i + j, itr[0].length*itr.length + 1, itr[i][j], 0));
			  if(j-1 >= 0)
				  graph.addEdge(new Edge(itr[0].length*i + j, itr[0].length*i + j - 1, 1000, 0));
			  if(j-1 >= 0 && i-1 >= 0)
				  graph.addEdge(new Edge(itr[0].length*i + j, itr[0].length*(i-1) + j - 1, 1000, 0));
			  if(j-1 >= 0 && i+1 < itr.length)
				  graph.addEdge(new Edge(itr[0].length*i + j, itr[0].length*(i+1) + j - 1, 1000, 0));
		  }
	  }
	  return graph;
  }
  
  /** Methode qui met un flot raisonable, on parcourt chaque ligne et on l'augmente du minimum de chaque ligne
   * @param itr Tableau de facteur d'interet
*/
  public void initGraph(int itr[][]){
	  Iterable<Edge> iterable = edges();
	  int min[] = new int [itr.length];
	  for(int i = 0; i< itr.length; i++){
		  min[i] = itr[i][0];
		  for(int j = 1; j<itr[0].length && min[i]!=0; j++){
			  if(min[i] > itr[i][j])
				  min[i] = itr[i][j];
		  }
	  }
	  for(Edge e : iterable)
		  if(e.capacity < 1000)
			  e.setUsed(min[e.from/itr[0].length]);
  }
  
  /** Methode qui renvoie l'amelioration residuel d'un edge
   * @param edges La liste de tout les Edge du graph.
   * @param from La source du Edge 
   * @param to La destination du Edge
   * @return l'amelioration residuel d'un edge
*/
  public int ameliorationResiduelle(ArrayList <Edge> edges, int from, int to){
	  for(Edge e : edges){
		  if(e.from == from && e.to == to)
			  return e.capacity - e.used;
	  }
	  return 0;
  }
  
  
  /** Methode qui renvoie l'augmentation maximum de flot d'un chemin
   * @param pred Le chemin à augmenter, c'et un tableau de predecesseur .
   * @return L'augmentation maximum de flot du chemin parcouru à l'aide de pred[]
*/
  public int augmentationMax(int[] pred ){
	  int i = adjacenyList.size() - 1;
	  int tmp;
	  int min = ameliorationResiduelle(adjacenyList.get(pred[i]), pred[i], i);
	  while(i != adjacenyList.size() - 2){
		  if((tmp = ameliorationResiduelle(adjacenyList.get(pred[i]), pred[i], i)) < min)
			  min = tmp;
		i = pred[i];
	  }
	  return min;
  }
  
  /* augmente un edge de la valeur augmentation */
  
  /** Methode qui augment le flot d'un edge de la valeut augmenntation
   * @param to Destination du edge.
   * @param from Source du edge.
   * @param augmentation La valeur dont on veut augment le flot du edge.
*/
  public void upgradeEdge(int to, int from, int augmentation){
	  for(Edge e : adjacenyList.get(from)){
		  if(e.from == from && e.to == to)
			  e.used += augmentation;
	  }
  }
  
/** Methode qui augmente  les flots de tout les edges du chemin pred. 
   * @param augmentation Valeur dont on augmente les edges.
   * @param pred[] Le chemin à augmenter.
*/
  public void upgradeGraph(int augmentation, int pred[]){
	  int i = adjacenyList.size() - 1;
	  while( i != adjacenyList.size() - 2){
		  upgradeEdge(i, pred[i], augmentation);
		  i = pred[i];
	  }
  }
  
  
  /** Methode qui permet d'atteindre le flot maximum d'un graph
   * @return 0 si pas de probleme
*/
  public int calculFlotMax(){
	  int pred[];
	  pred = verifChemin(); 
	  while(pred[adjacenyList.size() - 1] != -1){
		  upgradeGraph(augmentationMax(pred), pred);
		  pred = verifChemin();
	  }
	  return 0;
  }
  
  
/** Vérifie si il existe des chemins existants et renvoie le tableau des predecesseurs
   * @return Le chemin sous forme d'un tableau de prédecesseurs 
*/
  public int[] verifChemin(){
	  int head;
	  
	  int pred[] = new int[adjacenyList.size()] ;
	  
	  for(int i = 0; i < adjacenyList.size(); i++)
		  pred[i] = -1;

	  ArrayList<Integer> file = new ArrayList<>();
		  
	  file.add( adjacenyList.size() - 2 );
	  while(!file.isEmpty()){
		  
		  head = file.get(0);

		  for( Edge e : adjacent( head )){
			  if(e.from == head && (pred[e.to] == -1) && e.capacity>e.used){
				  file.add(e.to);
				  pred[e.to] = head;
				  if(e.to == adjacenyList.size() - 1)
					  return pred;
			  }
		  }
		  file.remove(0); /*On coupe la tete*/
	  }
	  return pred;
  }
  
  /** Renvoie true si elle trouve un pont plein ayant comme origine le sommet indice
   * @param indice Indice du sommet étudié.
   * @return true si le pont est plein , false sinon
*/
  public boolean condition(int indice){
	  if(indice < 0)
		  return false;
	  if(indice >= adjacenyList.size())
		  return false;
	  for(Edge e: adjacenyList.get(indice)){
		  if(e.from == indice && e.capacity < 1000 && e.capacity == e.used)
			 return true; 
	  }
	  return false;
  }
 
  
  
/** Methode qui établit la coupe minimal du graphe.
   * @param ligne Nombre de ligne du graphe
   * @param colonne Nombre de colonne du graphe
   * @return Un tableau qui contient les indices de coupe de chaque ligne du graphe
*/
  public int[] coupeMinimal(int ligne, int colonne){
	  boolean marqueur[] = new boolean[ligne*colonne];
	  int indiceCoupe[] = new int[ligne];
	  int decalage[] = new int [ligne];
	  int inc = 0;
	  for(int i = 0; i< colonne && inc < ligne; i++){
		  for(Edge e : adjacenyList.get(i))
			  if(e.from == i && e.capacity == e.used){
				  indiceCoupe[0] = i;
				  inc = 1;
				  decalage[inc] = 0;
				  while(inc > 0 && inc < ligne){
					  if(condition(i+inc*colonne + decalage[inc]) && !marqueur[i+inc*colonne + decalage[inc]]){
						  marqueur[i+inc*colonne + decalage[inc]] = true;
						  indiceCoupe[inc] = i + decalage[inc];
						  inc++;
						  if(inc<ligne)
							  decalage[inc] = decalage[inc-1];
					  }
					  else if(condition(i+inc*colonne + decalage[inc] + 1) && !marqueur[i+inc*colonne + decalage[inc] + 1]){
						  marqueur[i+inc*colonne + decalage[inc] + 1] = true;
						  indiceCoupe[inc] = i + decalage[inc] + 1;
						  inc++;
						  if(inc<ligne)
							  decalage[inc] = decalage[inc-1] + 1;
					  }
					  else if(condition(i+inc*colonne + decalage[inc] - 1) && !marqueur[i+inc*colonne + decalage[inc] - 1]){
						  marqueur[i+inc*colonne + decalage[inc] - 1] = true;
						  indiceCoupe[inc] = i + decalage[inc] - 1;
						  inc++;
						  if(inc<ligne)
							  decalage[inc] = decalage[inc-1] - 1;
					  }
					  else 
						  inc--;
				  }
			  }
	  }
	  return indiceCoupe;
  }
  
  public void writeFile(Path path) throws IOException {
    try(BufferedWriter writer = Files.newBufferedWriter(path);
        PrintWriter printer = new PrintWriter(writer)) {
      
      printer.println("digraph G{");
      for (Edge e : edges()) {
        printer.println(e.from + "->" + e.to + "[label=\"" + e.used + "/" + e.capacity + "\"];");
      }
      printer.println("}");
    }
  }
}
