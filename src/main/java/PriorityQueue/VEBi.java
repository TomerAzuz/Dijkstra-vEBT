package PriorityQueue;

public interface VEBi<T> extends PQ<Integer> {
    Integer successor(Integer x);
    Integer getMin();
    void clear();
}

