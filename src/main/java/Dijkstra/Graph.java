package Dijkstra;

import java.util.*;

import PriorityQueue.FibHeap.FibNode;

public class Graph {
    private final Vertex[] vertices;
    private final int numVertices;
    private int maxEdgeWeight;
    private long numEdges;
    private float density;

    public Graph(int numNodes, float density) {
        this.numVertices = numNodes;
        this.vertices = new Vertex[numNodes];
        if (density > 0)    {
            int temp = (int) (density * numVertices);
            numEdges = ((long) temp * (numVertices - 1));
            this.density = density;
            generateRandom();
        }
    }

    private void generateRandom() {
        createVertices();
        int c = 100;
        for (int i = 0; i < numVertices; i++) {
            Vertex src = vertices[i];
            for (int j = i + 1; j < numVertices; j++) {
                Vertex dest = vertices[j];
                if (Math.random() < density) {
                    int randWeight = (int) (Math.random() * c) + 1;
                    maxEdgeWeight = Math.max(maxEdgeWeight, randWeight);
                    if (!src.getEdges().containsKey(dest) && !dest.getEdges().containsKey(src)) {
                        src.getEdges().put(dest, randWeight);
                        dest.getEdges().put(src, randWeight);
                    }
                }
            }
        }
    }

    private void createVertices()   {
        for (int i = 0; i < numVertices; i++)   {
            Vertex v = new FibNode(i);
            vertices[i] = v;
        }
    }

    public int getNumVertices() {
        return numVertices;
    }

    public Vertex[] getVertices() {
        return vertices;
    }

    public int getMaxWeight()   {
        return maxEdgeWeight;
    }

    public long getNumEdges()    {
        return numEdges;
    }

    public void printLog()  {
        long temp = ((long) numVertices * (numVertices - 1));
        double density = (1.0 * numEdges) / temp;  // For directed graphs
        density = (long) (density * 1e5) / 1e5;
        System.out.println("|V|: " + numVertices);
        System.out.println("|E|: " + numEdges);
        System.out.println("|c|: " + maxEdgeWeight);
        System.out.println("Density: " + density);
    }

    public static class Vertex  {
        private final int id;
        private int distance;
        private final HashMap<Vertex, Integer> edges;
        private Vertex predecessor;
        private float x, y, dx, dy;

        public Vertex(int id) {
            this.id = id;
            this.distance = Integer.MAX_VALUE;
            this.predecessor = null;
            this.edges = new HashMap<>();
            this.dx = 0;
            this.dy = 0;
        }

        public float getX() {
            return this.x;
        }

        public float getY() {
            return this.y;
        }

        public void setX(float x)   {
            this.x = x;
        }

        public void setY(float y)   {
            this.y = y;
        }

        public float getDx()    {
            return this.dx;
        }

        public void setDx(float dx) {
            this.dx = dx;
        }

        public float getDy()    {
            return this.dy;
        }

        public void setDy(float dy)    {
            this.dy = dy;
        }

        public void clear() {
            this.distance = Integer.MAX_VALUE;
            this.predecessor = null;
        }

        public int getId() {
            return id;
        }

        public int getDistance() {
            return distance;
        }

        public HashMap<Vertex, Integer> getEdges() {
            return edges;
        }

        public Vertex getPredecessor() {
            return predecessor;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public void setPredecessor(Vertex predecessor) {
            this.predecessor = predecessor;
        }
    }
}
