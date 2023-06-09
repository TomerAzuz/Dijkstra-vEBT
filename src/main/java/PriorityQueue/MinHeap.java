package PriorityQueue;

import java.util.HashMap;
import java.util.NoSuchElementException;

import Dijkstra.Graph.Vertex;

public class MinHeap implements PQ<Vertex>    {
    private final Vertex[] items;
    private int numItems;
    private final HashMap<Integer, Integer> idToIndex;  /* maps vertex id to index in heap */

    public MinHeap(int capacity) {
        this.numItems = 0;
        this.items = new Vertex[capacity + 1];
        this.idToIndex = new HashMap<>();
    }

    @Override
    public boolean isEmpty()   {
        return numItems == 0;
    }

    private int parent(int i)   {
        return i >> 1;
    }

    private int left(int i) {
        return i << 1;
    }

    private int right(int i)    {
        return (i << 1) + 1;
    }

    private void swap(int i, int j) {
        idToIndex.replace(items[i].getId(), i, j);
        idToIndex.replace(items[j].getId(), j, i);
        Vertex swap = items[i];
        items[i] = items[j];
        items[j] = swap;
    }

    @Override
    public Vertex extractMin()   {
        if (numItems < 1)    {
            throw new NoSuchElementException("Empty PQ");
        }
        Vertex min = items[1];
        idToIndex.remove(min.getId());
        items[1] = items[numItems--];
        idToIndex.replace(items[1].getId(), 1);
        minHeapify(1);
        return min;
    }

    @Override
    public void decreaseKey(Vertex v, int key)    {
        int index = idToIndex.get(v.getId());
        if (key > items[index].getDistance())   {
            throw new IllegalArgumentException("new key is greater than current key");
        }
        while (index > 1 && items[parent(index)].getDistance() > items[index].getDistance()) {
            swap(index, parent(index));
            index = parent(index);
        }
    }

    @Override
    public void insert(Vertex v)   {
        items[++numItems] = v;
        idToIndex.put(v.getId(), numItems);
        decreaseKey(v, v.getDistance());
    }

    @Override
    public void delete(Vertex v)    {
        decreaseKey(v, Integer.MIN_VALUE);
        idToIndex.remove(v.getId());
        items[1] = items[numItems--];
        minHeapify(1);
    }

    private void minHeapify(int i)  {
        int l = left(i);
        int r = right(i);
        int smallest;
        if (l <= numItems && items[l].getDistance() < items[i].getDistance())   {
            smallest = l;
        } else    {
            smallest = i;
        }
        if (r <= numItems && items[r].getDistance() < items[smallest].getDistance()) {
            smallest = r;
        }
        if (smallest != i)   {
            swap(i, smallest);
            minHeapify(smallest);
        }
    }
}
