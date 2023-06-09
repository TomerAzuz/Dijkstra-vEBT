package GUI;

import Dijkstra.Graph;
import processing.core.PApplet;
import static processing.core.PApplet.*;

public class FructhermanReingold {
    private final PApplet p;
    private float k, temperature;
    private int numIterations;
    private final int width, height;

    public FructhermanReingold(PApplet p, int width, int height) {
        this.p = p;
        this.k = 0.1f;
        this.temperature = 100.0f;
        this.numIterations = 0;
        this.width = width;
        this.height = height;
    }

    void updateLayout(Graph g) {
        float minDistance = 5.0f;
        float minSeparation = 120f;

        float scalingFactor = min(width, height) / 1000f;
        minSeparation *= scalingFactor;

        if (numIterations < 100) {
            // Calculate repulsive forces between nodes
            for (Graph.Vertex v1 : g.getVertices()) {
                for (Graph.Vertex v2 : g.getVertices()) {
                    if (v1 != v2) {
                        float dx = v2.getX() - v1.getX();
                        float dy = v2.getY() - v1.getY();
                        float distanceSquared = dx * dx + dy * dy;
                        float distance = sqrt(distanceSquared);

                        if (distance > minDistance) {
                            float force = (k * k) / distance * 0.2f;
                            v1.setDx(v1.getDx() - (dx / distance) * force);
                            v1.setDy(v1.getDy() - (dy / distance) * force);
                        }
                    }
                }
            }

            // Calculate attractive forces between connected nodes
            for (Graph.Vertex v1 : g.getVertices()) {
                for (Graph.Vertex v2 : v1.getEdges().keySet()) {
                    float dx = v2.getX() - v1.getX();
                    float dy = v2.getY() - v1.getY();
                    float distanceSquared = dx * dx + dy * dy;
                    float distance = sqrt(distanceSquared);
                    float force = (distanceSquared / k) * 0.2f;

                    // Apply force to the nodes
                    v1.setDx(v1.getDx() + (dx / distance) * force);
                    v1.setDy(v1.getDy() + (dy / distance) * force);
                    v2.setDx(v2.getDx() - (dx / distance) * force);
                    v2.setDy(v2.getDy() - (dy / distance) * force);
                }
            }

            // Update node positions based on forces and handle collision
            for (Graph.Vertex v1 : g.getVertices()) {
                float speed = sqrt(v1.getDx() * v1.getDx() + v1.getDy() * v1.getDy());
                float temperatureFactor = Math.min(temperature, speed) / speed;
                v1.setX(v1.getX() + v1.getDx() * temperatureFactor);
                v1.setY(v1.getY() + v1.getDy() * temperatureFactor);
                v1.setDx(v1.getDx() * temperatureFactor);
                v1.setDy(v1.getDy() * temperatureFactor);

                // Handle node collision
                for (Graph.Vertex v2 : g.getVertices()) {
                    if (v1 != v2) {
                        float dx = v2.getX() - v1.getX();
                        float dy = v2.getY() - v1.getY();
                        float distanceSquared = dx * dx + dy * dy;
                        float distance = sqrt(distanceSquared);

                        if (distance < minSeparation) {
                            float separation = minSeparation - distance;
                            float separationX = (dx / distance) * separation * 0.1f;
                            float separationY = (dy / distance) * separation * 0.1f;
                            v1.setX(v1.getX() - separationX);
                            v1.setY(v1.getY() - separationY);
                            v2.setX(v2.getX() + separationX);
                            v2.setY(v2.getY() + separationY);
                        }
                    }
                }

                v1.setX(constrain(v1.getX(), 100, width - 100));
                v1.setY(constrain(v1.getY(), 200, height - 100));
            }

            // Determine the minimum and maximum X and Y coordinates of the vertices
            float minX = Float.POSITIVE_INFINITY;
            float minY = Float.POSITIVE_INFINITY;
            float maxX = Float.NEGATIVE_INFINITY;
            float maxY = Float.NEGATIVE_INFINITY;
            for (Graph.Vertex v : g.getVertices()) {
                minX = min(minX, v.getX());
                minY = min(minY, v.getY());
                maxX = max(maxX, v.getX());
                maxY = max(maxY, v.getY());
            }

            // Calculate the center coordinates of the graph
            float centerX = (maxX + minX) / 2.0f;
            float centerY = (maxY + minY) / 2.0f;

            // Calculate the translation values to move the graph to the center of the screen
            float translateX = width / 2.0f - centerX;
            float translateY = height / 2.0f - centerY;

            // Translate the coordinates of all vertices to move the graph to the center of the screen
            for (Graph.Vertex v : g.getVertices()) {
                v.setX(v.getX() + translateX);
                v.setY(v.getY() + translateY);
            }

            // Separate vertices that are drawn on top of each other
            for (int j = 0; j < g.getNumVertices(); j++) {
                for (int k = j + 1; k < g.getNumVertices(); k++) {
                    Graph.Vertex v1 = g.getVertices()[j];
                    Graph.Vertex v2 = g.getVertices()[k];

                    float dx = v2.getX() - v1.getX();
                    float dy = v2.getY() - v1.getY();
                    float distanceSquared = dx * dx + dy * dy;

                    if (distanceSquared < minSeparation * minSeparation) {
                        float separation = minSeparation - (float) Math.sqrt(distanceSquared);
                        float separationX = (dx / (float) Math.sqrt(distanceSquared)) * separation * 0.1f;
                        float separationY = (dy / (float) Math.sqrt(distanceSquared)) * separation * 0.1f;

                        v1.setX(v1.getX() - (int) separationX);
                        v1.setY(v1.getY() - (int) separationY);
                        v2.setX(v2.getX() + (int) separationX);
                        v2.setY(v2.getY() + (int) separationY);
                    }
                }
            }

            temperature *= 0.90f;
            numIterations++;
        }
    }
}
