package Dijkstra;

import Dijkstra.Graph.Vertex;
import PriorityQueue.*;
import PriorityQueue.FibHeap.FibNode;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;

public class Dijkstra {
    private final Graph g;
    private Vertex dest;

    public Dijkstra(Graph g, Vertex src, Vertex dest, int PQType) {
        this.g = g;
        setSrc(src);
        setDest(dest);
        DijkstraInterface dijkstraImp = initInterface(PQType);
        long startTime = System.currentTimeMillis();
        dijkstraImp.findShortestPath(src);
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("Execution Time: " + executionTime + " milliseconds");
    }

    private DijkstraInterface initInterface(int pqType) {
        switch (pqType) {
            case 0: {
                System.out.println("Unsorted Array");
                int capacity = g.getNumVertices();
                PQ<Vertex> pq = new UnsortedArray(capacity);
                return new DijkstraHeap(g.getVertices(), pq, null);
            }
            case 1: {
                System.out.println("Min Heap");
                int capacity = g.getNumVertices();
                PQ<Vertex> pq = new MinHeap(capacity);
                return new DijkstraHeap(g.getVertices(), pq, null);
            }
            case 2: {
                System.out.println("Fibonacci Heap");
                PQ<FibNode> pq = new FibHeap();
                return new DijkstraHeap(g.getVertices(), pq, null);
            }
            case 3: {
                System.out.println("vEBT");
                int u = g.getMaxWeight() + 1;
                VEBi<Integer> pq = new RS_vEB(u);
                return new DijkstraVEB(g, pq, null);
            }
        }
        throw new IllegalArgumentException("Invalid PQ type");
    }

    private boolean nodeExists(Vertex node)    {
        return g.getVertices()[node.getId()] != null;
    }

    private void setSrc(Vertex src)  {
        if(!nodeExists(src))  {
            throw new IllegalArgumentException("Invalid source");
        }
        src.setDistance(0);
    }

    private void setDest(Vertex dest) {
        if(!nodeExists(dest))  {
            throw new IllegalArgumentException("Invalid destination");
        }
        this.dest = dest;
    }

    private void printPath(Vertex destination, int count)    {
        if(destination.getDistance() == Integer.MAX_VALUE)  {
            System.out.println("Destination is unreachable from source");
            return;
        }
        if(destination.getPredecessor() == null)   {
            System.out.println("Total nodes in path: " + (count + 1));
            System.out.print(destination.getId() + " --> ");
            return;
        }
        printPath(destination.getPredecessor(), count + 1);
        if(destination == dest)    {
            System.out.print(destination.getId());
        }
        else    {
            System.out.print(destination.getId() + " --> ");
        }
        if(count % 10 == 0) {
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Graph g = new Graph( 1000, 0.02f);
        Vertex src = g.getVertices()[0];
        Vertex dest = g.getVertices()[999];
        Dijkstra d = new Dijkstra(g, src, dest, 3);
        d.printPath(dest, 0);
    }
}