package GUI;

import Dijkstra.Graph;
import Dijkstra.Graph.Vertex;
import processing.core.PApplet;

import static GUI.Sketch.NODE_RADIUS;

public class GraphVisualizer {
    // Processing variables
    public final PApplet p;
    private final int width, height;

    // Graph variables
    private final Graph g;
    private Vertex src, dest, currNode;

    // Fruchterman-Reingold
    private final FructhermanReingold fr;

    public GraphVisualizer(PApplet p, Graph graph, int width, int height) {
        this.p = p;
        this.g = graph;
        this.src = null;
        this.dest = null;
        this.currNode = null;
        this.width = width;
        this.height = height;
        this.fr = new FructhermanReingold(p, width, height);
    }

    public void setSrc(Vertex src) {
        this.src = src;
        src.setDistance(0);
        this.currNode = src;
    }

    public void setDest(Vertex dest) {
        this.dest = dest;
    }

    public void setCurrNode(Vertex currNode) {
        this.currNode = currNode;
    }

    public Vertex getCurrNode() {
        return this.currNode;
    }

    void drawGraph() {
        drawEdges();
        drawVertices();
    }

    private void drawEdges()    {
        for (Vertex v1 : g.getVertices()) {
            for (Vertex v2 : v1.getEdges().keySet()) {
                if (v1.getId() < v2.getId())    {
                    p.stroke(0);
                    p.strokeWeight(2);
                    p.line(v1.getX(), v1.getY(), v2.getX(), v2.getY());
                    drawEdgeWeight(v1, v2);
                }
            }
        }
    }

    private void drawVertices() {
        for (Vertex v : g.getVertices()) {
            if (v == src) {
                p.fill(255, 0, 0);
                v.setDistance(0);
            } else if (v == dest) {
                p.fill(0, 255, 0);
            } else {
                p.fill(255);
            }
            p.stroke(0);
            p.ellipse(v.getX(), v.getY(), NODE_RADIUS, NODE_RADIUS);
            drawNodeID(v);
        }
    }

    private void drawEdgeWeight(Vertex v1, Vertex v2) {
        int weight = v1.getEdges().get(v2);
        float x1 = v1.getX();
        float y1 = v1.getY();
        float x2 = v2.getX();
        float y2 = v2.getY();
        float weightOffsetX = (x2 - x1) * 0.2f;
        float weightOffsetY = (y2 - y1) * 0.2f;
        float weightX = (x1 + x2) / 2 + weightOffsetX;
        float weightY = (y1 + y2) / 2 + weightOffsetY;

        p.fill(255, 0, 0);
        p.textSize(15);
        p.textAlign(PApplet.CENTER, PApplet.CENTER);
        p.text(weight, weightX, weightY);
    }

    public void drawNodeID(Vertex v) {
        p.fill(0);
        p.textSize(14);
        p.textAlign(PApplet.CENTER, PApplet.CENTER);
        p.text(v.getId(), v.getX(), v.getY());
    }

    void initLayout(Graph g) {
        for (Graph.Vertex v : g.getVertices()) {
            v.setX(p.random(200, width - 200));
            v.setY(p.random(200, height - 200));
        }
    }

    void setupLayout()  {
        this.fr.updateLayout(g);
    }
}
