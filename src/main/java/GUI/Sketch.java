package GUI;

import Dijkstra.*;
import Dijkstra.Graph.Vertex;
import PriorityQueue.*;
import controlP5.*;
import processing.core.*;

import java.util.*;

public class Sketch extends PApplet {
    static final int NODE_RADIUS = 40;

    private ControlP5 cp5;
    private ControlFont cf1;

    private Graph g;
    private DijkstraInterface d;

    private Vertex src, dest;
    private ArrayList<Vertex> currNeighbors;
    private boolean terminated;

    private GraphVisualizer gv;

    private int nodeCount;
    private float density;
    private int animationRate;
    private int nodesVisited;

    public void settings() {
        fullScreen();
    }

    public void setup() {
        cp5 = new ControlP5(this);
        cf1 = new ControlFont(createFont("Arial", 18));
        noStroke();
        createButtons();
        createSliders();
        src = null;
        dest = null;
        currNeighbors = new ArrayList<>();
        terminated = false;
        nodesVisited = 0;
    }

    public void draw() {
        background(255);
        drawText();
        if (g != null) {
            gv.drawGraph();
            gv.setupLayout();
            if (terminated) {
                terminate();
            } else if (d != null) {
                dijkstra();
                delay(1000 / animationRate); // Adjust the delay based on animationRate
            }
        }
    }

    public void mousePressed() {
        if (g != null) {
            if (mouseButton == LEFT) {
                for (Vertex v : g.getVertices()) {
                    if (dist(mouseX, mouseY, v.getX(), v.getY()) < NODE_RADIUS / 2f) {
                        handleSourceNodeSelection(v);
                        handleDestinationNodeSelection(v);
                        break;
                    }
                }
            }
        }
    }

    private void handleSourceNodeSelection(Vertex v) {
        if (src == null) {
            src = v;
            src.setDistance(0);
            gv.setSrc(src);
        }
    }

    private void handleDestinationNodeSelection(Vertex v) {
        if (dest == null && v != src) {
            dest = v;
            gv.setDest(dest);
        }
    }

    public void createButtons() {
        background(255);
        createGenerateGraphButton();
        createShortestPathButton();
    }

    private void createGenerateGraphButton() {
        Button genGraph = cp5.addButton("Generate Graph")
                .setValue(128)
                .setPosition(50, height - 130)
                .setSize(250, 30)
                .setLabel("Generate Graph");
        genGraph.getCaptionLabel().setFont(cf1);
        genGraph.addCallback(event -> {
            if (event.getAction() == ControlP5.ACTION_CLICK) {
                reset();
                g = new Graph( nodeCount, density);
                gv = new GraphVisualizer(Sketch.this, g, width, height);
                gv.initLayout(g);
            }
        });
    }

    private void createShortestPathButton() {
        Button shortestPath = cp5.addButton("path")
                .setValue(128)
                .setPosition(50, height - 90)
                .setSize(250, 30)
                .setLabel("FIND SHORTEST PATH");
        shortestPath.getCaptionLabel().setFont(cf1);
        shortestPath.addCallback(event -> {
            if (event.getAction() == ControlP5.ACTION_CLICK) {
                if (g != null) {
                    /* initialize with min heap */
                    int capacity = g.getNumVertices();
                    PQ<Vertex> pq = new MinHeap(capacity);
                    d = new DijkstraHeap(g.getVertices(), pq, gv);
                }
            }
        });
    }

    private void createSliders() {
        /* animation rate slider */
        animationRate = 60;
        cp5.addSlider("animationRate")
                .setPosition(90, height - 300)
                .setSize(150, 18)
                .setRange(1, 60)
                .setValue(animationRate)
                .getValueLabel().setFont(cf1);
        cp5.getController("animationRate").plugTo(this, "animationRateEvent");

        /* num nodes slider */
        nodeCount = 10;
        cp5.addSlider("nodeCount")
                .setPosition(90, height - 240)  // Adjust the Y coordinate to position it at the bottom
                .setSize(150, 18)
                .setRange(2, 100)
                .setNumberOfTickMarks(1000)
                .setValue(nodeCount)
                .getValueLabel().setFont(cf1);
        cp5.getController("nodeCount").plugTo(this, "nodeCountEvent");

        /* density slider */
        density = 0.5f;
        cp5.addSlider("density")
                .setPosition(90, height - 180)  // Adjust the Y coordinate to position it at the bottom
                .setSize(150, 18)
                .setRange(0, 1)
                .setDecimalPrecision(3)
                .setNumberOfTickMarks(1001)
                .setValue(density)
                .getValueLabel().setFont(cf1);
        cp5.getController("density").plugTo(this, "densityEvent");
    }

    private void drawText() {
        fill(0);
        textSize(22);
        textAlign(CENTER, CENTER);
        text("Animation Speed", 160, height - 320);
        text("Nodes", 160, height - 260);
        text("Density", 160, height - 200);
    }

    public void nodeCountEvent(int value) {
        nodeCount = value;
    }

    public void densityEvent(float value) {
        density = value;
    }

    public void animationRateEvent(int value) {
        animationRate = value;
        frameRate(animationRate);
    }

    private void reset()   {
        src = null;
        dest = null;
        g = null;
        d = null;
        currNeighbors = new ArrayList<>();
        terminated = false;
        nodesVisited = 0;
    }

    private boolean pathFound() {
        return dest.getDistance() != Integer.MAX_VALUE;
    }

    private void terminate() {
        fill(0);
        textSize(25);
        textAlign(CENTER, CENTER);
        if (pathFound()) {
            text("Path Found", width - 200, height - 300);
            text("Total Distance: " + dest.getDistance(), width - 200, height - 230);
            text("Nodes in path: " + nodesVisited, width - 200, height - 180);
            printPath(dest);
        } else {
            text("No Path", width - 200, height - 300);
        }
    }

    private void dijkstra() {
        Vertex currNode = gv.getCurrNode();

        if (currNode == null || terminated) {
            return;
        }

        if (currNode == dest) {
            gv.setCurrNode(null);
            countNodesInPath(dest);
            terminated = true;
            return;
        }

        if (currNeighbors.isEmpty()) {
            d.step(src);
            currNode = gv.getCurrNode();
            if (currNode == null) {
                terminated = true;
                return;
            }
            currNeighbors = new ArrayList<>(currNode.getEdges().keySet());
        }

        if (currNeighbors.isEmpty()) {
            terminated = true;
            return;
        }

        markNode(currNode, false);
        Vertex neighbor = currNeighbors.remove(0);
        d.relax(currNode, neighbor);

        markNode(neighbor, true);
        stroke(255, 165, 0);
        strokeWeight(3);
        line(currNode.getX(), currNode.getY(), neighbor.getX(), neighbor.getY());
    }

    private void markNode(Vertex node, boolean isNeighbor) {
        if (isNeighbor) {
            fill(255, 165, 0, 150);
            stroke(255, 165, 0, 150);
        } else {
            fill(0, 0, 255, 150);
            stroke(0, 0, 255, 150);
        }
        ellipse(node.getX(), node.getY(), NODE_RADIUS, NODE_RADIUS);
    }

    private void printPath(Vertex destination) {
        if (destination.getDistance() == Integer.MAX_VALUE) {
            return;
        }
        Vertex pred = destination.getPredecessor();
        if (pred != null) {
            stroke(0, 0, 255);
            strokeWeight(3);
            line(destination.getX(), destination.getY(), pred.getX(), pred.getY());
            markNode(destination, false);
            printPath(destination.getPredecessor());
            gv.drawNodeID(destination);
        }
    }

    private void countNodesInPath(Vertex destination) {
        if (destination.getDistance() == Integer.MAX_VALUE) {
            return;
        }
        nodesVisited++;
        Vertex pred = destination.getPredecessor();
        if (pred != null) {
            countNodesInPath(destination.getPredecessor());
        }
    }

    public static void main(String[] args) {
        PApplet.main(Sketch.class);
    }
}
