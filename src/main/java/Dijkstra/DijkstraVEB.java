package Dijkstra;

import Dijkstra.Graph.*;
import GUI.GraphVisualizer;
import PriorityQueue.*;

import java.util.HashMap;

public class DijkstraVEB implements DijkstraInterface  {
    private final HashMap<Integer, DoublyLinkedNode> buckets;
    private VEBi<Integer> lowVEB;
    private VEBi<Integer> highVEB;
    private final int c;
    private int offset;
    private final GraphVisualizer gv;
    private Integer minDist;

    public DijkstraVEB(Graph g, VEBi<Integer> vEBT, GraphVisualizer gv) {
        this.c = g.getMaxWeight();
        this.lowVEB = vEBT;
        this.highVEB = new RS_vEB(c+1);
        this.buckets = new HashMap<>();
        this.offset = 0;
        this.gv = gv;
    }

    private void insertSrc(Vertex src)  {
        buckets.put(0, (DoublyLinkedNode) src);
        this.minDist = 0;
    }

    @Override
    public void findShortestPath(Vertex src) {
        buckets.put(0, (DoublyLinkedNode) src);
        Integer minDist = 0;
        while (minDist != null)  {
            DoublyLinkedNode u = popHead(minDist + offset);
            while (u != null) {
                for (Vertex v : u.getEdges().keySet())   {
                    relax(u,v);
                }
                u = popHead(minDist + offset);
            }
            minDist = lowVEB.successor(minDist % (c+1));
            if (minDist == null && !highVEB.isEmpty()) {
                offset += c + 1;
                VEBi<Integer> swap = lowVEB;
                lowVEB = highVEB;
                highVEB = swap;
                highVEB.clear();
                minDist = lowVEB.getMin();
            }
        }
    }

    @Override
    public void relax(Vertex u, Vertex v) {
        int vDist = v.getDistance();
        int w = u.getDistance() + u.getEdges().get(v);
        if (vDist > w)   {
            if (vDist != Integer.MAX_VALUE)  {
                removeFromBucket((DoublyLinkedNode) v);
                if (!buckets.containsKey(vDist))  {
                    if (vDist <= c + offset)   {
                        lowVEB.delete(vDist % (c+1));
                    } else {
                        highVEB.delete(vDist % (c+1));
                    }
                }
            }
            v.setDistance(w);
            v.setPredecessor(u);
            vDist = v.getDistance();
            /* duplicates are not inserted into the vEB */
            if (!buckets.containsKey(vDist)) {
                if (vDist <= c + offset)  {
                    lowVEB.insert(vDist % (c+1));
                } else    {
                    highVEB.insert(vDist % (c+1));
                }
            }
            addToBucket((DoublyLinkedNode) v);
        }
    }

    private void addToBucket(DoublyLinkedNode v)  {
        int vDist = v.getDistance();
        if (!buckets.containsKey(vDist)) {
            buckets.put(vDist, v);
        } else {
            DoublyLinkedNode head = buckets.get(vDist);
            v.right = head.right;
            v.left = head;
            head.right.left = v;
            head.right = v;
        }
    }

    private void removeFromBucket(DoublyLinkedNode v)   {
        int vDist = v.getDistance();
        if (v == v.right)    {
            buckets.remove(vDist);
        } else {
            if (isHead(v))   {
                buckets.put(vDist, v.right);
            }
            v.left.right = v.right;
            v.right.left = v.left;
            v.right = v.left = v;
        }
    }

    private boolean isHead(Vertex v)    {
        return v.getId() == buckets.get(v.getDistance()).getId();
    }

    private DoublyLinkedNode popHead(int bucketIndex) {
        if (buckets.get(bucketIndex) != null)    {
            DoublyLinkedNode head = buckets.get(bucketIndex);
            removeFromBucket(head);
            return head;
        }
        return null;
    }

    public void step(Vertex src) {
        if (minDist != null)    {
            DoublyLinkedNode u = popHead(minDist + offset);
            if (u != null) {
                this.gv.setCurrNode(u);
            } else  {
                popHead(minDist + offset);
                minDist = lowVEB.successor(minDist % (c + 1));
                if (minDist == null && !highVEB.isEmpty()) {
                    offset += c + 1;
                    VEBi<Integer> swap = lowVEB;
                    lowVEB = highVEB;
                    highVEB = swap;
                    highVEB.clear();
                    minDist = lowVEB.getMin();
                }
            }
        }
    }

    public static class DoublyLinkedNode extends Vertex {
        public DoublyLinkedNode left, right;
        public DoublyLinkedNode(int id) {
            super(id);
            this.left = this.right = this;
        }

        public void clear() {
            super.clear();
            this.left = this.right = this;
        }
    }
}
