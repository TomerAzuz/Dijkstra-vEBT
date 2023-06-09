package Dijkstra;
import Dijkstra.Graph.Vertex;

public interface DijkstraInterface {
    void findShortestPath(Vertex src);
    void relax(Vertex u, Vertex v);
    void step(Vertex src);
}
