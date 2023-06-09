package PriorityQueue;

import Dijkstra.DijkstraVEB.DoublyLinkedNode;
import PriorityQueue.FibHeap.FibNode;

import java.util.HashMap;
import java.util.NoSuchElementException;

public class FibHeap implements PQ<FibNode> {
    private FibNode minNode;
    private int numNodes, numRoots;
    private final HashMap<Integer, FibNode> degTable;
    public FibHeap() {
        this.minNode = null;
        this.numNodes = this.numRoots = 0;
        this.degTable = new HashMap<>();
    }
    public static class FibNode extends DoublyLinkedNode {
        private int degree;
        private boolean mark;
        private FibNode parent, child;

        public FibNode(int label) {
            super(label);
            this.degree = 0;
            this.parent = this.child = null;
            this.mark = false;
        }

        public void clear()    {
            super.clear();
            this.degree = 0;
            this.parent = this.child = null;
            this.mark = false;
        }
    }

    @Override
    public boolean isEmpty()   {
        return minNode == null;
    }

    private int getMin()    {
        if(isEmpty())  {
            throw new NoSuchElementException("Empty PQ");
        }
        return minNode.getDistance();
    }

    @Override
    public void insert(FibNode node)  {
        if(isEmpty())   {
            minNode = node;
            minNode.right = minNode.left = minNode;
        }
        else    {
            node.left = minNode;
            node.right = minNode.right;
            minNode.right = node;
            node.right.left = node;

            if(node.getDistance() < getMin())  {
                minNode = node;
            }
        }
        numNodes++;
        numRoots++;
    }

    @Override
    public FibNode extractMin()    {
        FibNode z = minNode;
        if (z != null)   {
            FibNode child = z.child;
            FibNode next;
            /* for each child of z */
            for (int i = z.degree; i > 0; i--)   {
                next = (FibNode) child.right;
                removeFromChildList(child);
                insertToRootList(child);
                child = next;
            }
            removeFromRootList(z);
            if (z == z.right)    {
                minNode = null;
            } else    {
                minNode = (FibNode) z.right;
                consolidate();
            }
            numNodes--;
        }
        return z;
    }

    private void consolidate() {
        degTable.clear();
        FibNode x = minNode;
        int tempRoots = numRoots;

        while (tempRoots > 0)   {
            int d = x.degree;
            FibNode next = (FibNode) x.right;
            while (degTable.get(d) != null)  {
                FibNode y = degTable.get(d);
                if (x.getDistance() > y.getDistance())   {
                    FibNode swap = x;
                    x = y;
                    y = swap;
                }
                link(y, x);
                degTable.remove(d);
                d++;
            }
            degTable.put(d, x);
            x = next;
            tempRoots--;
        }
        minNode = null;
        for (FibNode node : degTable.values())   {
            if (node != null)    {
                insertToRootList(node);
            }
        }
    }

    private void insertToRootList(FibNode node) {
        if (isEmpty()) {
            minNode = node;
            minNode.left = minNode.right = minNode;
            numRoots = 1;
        } else    {
            node.left = minNode;
            node.right = minNode.right;
            minNode.right.left = node;
            minNode.right = node;
            numRoots++;
            if (node.getDistance() < getMin())   {
                minNode = node;
            }
        }
    }

    private void addChildToParent(FibNode x, FibNode y) {
        if (x.child == null) {
            x.child = y;
            y.right = y.left = y;
        } else    {
            y.left = x.child;
            y.right = x.child.right;
            x.child.right.left = y;
            x.child.right = y;
        }
    }

    private void removeFromRootList(FibNode x) {
        x.right.left = x.left;
        x.left.right = x.right;
        numRoots--;
    }

    private void removeFromChildList(FibNode x) {
        x.left.right = x.right;
        x.right.left = x.left;
        x.parent = null;
    }

    private void link(FibNode y, FibNode x) {
        removeFromRootList(y);
        addChildToParent(x, y);
        x.degree++;
        y.parent = x;
        y.mark = false;
    }

    public void decreaseKey(FibNode x, int key)  {
        if (key > x.getDistance())   {
            throw new IllegalArgumentException("new key is greater than current key");
        }
        FibNode parent = x.parent;
        if (parent != null && x.getDistance() < parent.getDistance())    {
            /* restore heap property */
            cut(x, parent);
            cascadingCut(parent);
        }
        if (isEmpty() || x.getDistance() < getMin())  {
            minNode = x;
        }
    }


    private void cut(FibNode x, FibNode y)  {
        if (y.child == x)    {
            if (x.right != x)    {
                y.child = (FibNode) x.right;
            } else {
                y.child = null;
            }
        }
        removeFromChildList(x);
        y.degree--;
        insertToRootList(x);
        y.mark = false;
    }

    /* y has lost a second child node */
    private void cascadingCut(FibNode y)    {
        FibNode parent = y.parent;
        if (parent != null)   {
            if(!y.mark) {
                y.mark = true;
            } else    {
                cut(y, parent);
                cascadingCut(parent);
            }
        }
    }

    @Override
    public void delete(FibNode x)  {
        decreaseKey(x, Integer.MIN_VALUE);
        extractMin();
    }
}