package PriorityQueue;

import Dijkstra.Graph.Vertex;
import java.util.NoSuchElementException;

public class UnsortedArray implements PQ<Vertex>{
    private final Vertex[] items;
    private int numItems;

    public UnsortedArray(int capacity) {
        this.items = new Vertex[capacity];
        this.numItems = 0;
    }

    @Override
    public void insert(Vertex v) {
        items[v.getId()] = v;
        numItems++;
    }

    @Override
    public boolean isEmpty() {
        return numItems == 0;
    }

    @Override
    public void decreaseKey(Vertex v, int key) {
        if (key > v.getDistance())    {
            throw new IllegalArgumentException("new key is greater than current key");
        }
        items[v.getId()].setDistance(key);
    }

    @Override
    public Vertex extractMin() {
        if (isEmpty())   {
            throw new NoSuchElementException("Empty PQ");
        }
        int minDistance = Integer.MAX_VALUE;
        Vertex min = null;
        for (Vertex v : items)   {
            if(v != null && minDistance > v.getDistance())   {
                minDistance = v.getDistance();
                min = v;
            }
        }
        if (min != null) {
            items[min.getId()] = null;
        }
        numItems--;
        return min;
    }

    @Override
    public void delete(Vertex v) {
        items[v.getId()] = null;
    }
}
