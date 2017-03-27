package fr.umlv.graph;
/** Classe qui repr�sente un edge (pont en fran�ais)
 * 	Un pont est d�fini par: un sommet source
 * 										un sommet destination
 * 										une capacit�
 * 										un flot qu'on appele used
 */
public class Edge {
  final int from;
  final int to;
  final int capacity;
  int used;

  public Edge(int from, int to, int capacity, int used) {
    this.from = from;
    this.to = to;
    this.capacity = capacity;
    this.used = used;
  }

  public int getFrom() {
    return from;
  }
  public int getTo() {
    return to;
  }
  public int getCapacity() {
    return capacity;
  }
  public int getUsed() {
    return used;
  }
  public void setUsed(int used) {
    this.used = used;
  }
/*Si l'origine est celle demand�e, on renvoie la destination
 * Sinon on renvoie l'origine			*/
  public int other(int v) {
    return (from == v)? to: from;
  }
}
