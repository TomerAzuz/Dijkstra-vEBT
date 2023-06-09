package Dijkstra;

import Dijkstra.Graph.Vertex;
import GUI.GraphVisualizer;
import PriorityQueue.PQ;

public class DijkstraHeap implements DijkstraInterface {
    private final PQ<Vertex> pq;
    private final Vertex[] vertices;
    private final GraphVisualizer gv;

    public DijkstraHeap(Vertex[] vertices, PQ pq, GraphVisualizer gv) {
        this.vertices = vertices;
        this.pq = pq;
        this.gv = gv;
        insertNodes();
    }

    private void insertNodes()  {
        for(Vertex v : vertices)    {
            pq.insert(v);
        }
    }

    public void relax(Vertex u, Vertex v)    {
        if (v.getDistance() > u.getDistance() + u.getEdges().get(v)) {
            int key = u.getDistance() + u.getEdges().get(v);
            v.setDistance(key);
            v.setPredecessor(u);
            pq.decreaseKey(v, key);
        }
    }

    public void step(Vertex src)  {
        if (!pq.isEmpty())  {
            Vertex u = pq.extractMin();
            if (u.getDistance() == Integer.MAX_VALUE)   {
                gv.setCurrNode(null);
                return;
            }
            gv.setCurrNode(u);
        }
    }

    public void findShortestPath(Vertex src) {
        while (!pq.isEmpty()) {
            Vertex u = pq.extractMin();
            for (Vertex v : u.getEdges().keySet()) {
                relax(u, v);
            }
        }
    }
}
