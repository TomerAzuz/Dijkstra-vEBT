package PriorityQueue;

public interface PQ<T> {
    void insert(T item);
    boolean isEmpty();
    void decreaseKey(T item, int key);
    T extractMin();
    void delete(T item);
}

