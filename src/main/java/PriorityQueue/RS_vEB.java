package PriorityQueue;
import java.util.*;

public class RS_vEB implements VEBi<Integer> {
    private final int u;
    private Integer min, max;
    private RS_vEB summary;
    private HashMap<Integer, RS_vEB> cluster;

    public RS_vEB(int universe) {
        this.u = isValidSize(universe) ? universe : roundToPowerOf2(universe);
        this.min = this.max = null;
        this.summary = null;
        this.cluster = isLeaf() ? null : new HashMap<>();
    }

    public void clear() {
        min = max = null;
        summary = null;
        cluster = isLeaf() ? null : new HashMap<>();
    }

    public RS_vEB getCluster(int x)  {
        cluster.putIfAbsent(x, new RS_vEB(lowerSqrt()));
        return cluster.get(x);
    }

    public RS_vEB getSummary()   {
        if (summary == null)   {
            summary = new RS_vEB(upperSqrt());
        }
        return summary;
    }

    @Override
    public boolean isEmpty()    {
        return min == null;
    }

    private boolean isValidSize(int u) {
        return u > 1 && ((u & u - 1) == 0);
    }

    private int roundToPowerOf2(int u)    {
        if(u <= 0) return 2;
        return (int) Math.pow(2, Math.ceil(Math.log(u) / Math.log(2)));
    }

    private int upperSqrt() {
        return (int) Math.pow(2, Math.ceil((Math.log(u) / Math.log(2)) / 2));
    }

    private int lowerSqrt() {
        return (int) Math.pow(2, Math.floor((Math.log(u) / Math.log(2)) / 2));
    }

    public boolean isLeaf()    {
        return u == 2;
    }

    private int high(int x) {
        return (int) Math.floor(x * 1.0 / lowerSqrt());
    }

    private int low(int x)  {
        return x % lowerSqrt();
    }

    private int index(int x, int y) {
        return x * lowerSqrt() + y;
    }

    public Integer getMin() {
        return min;
    }

    public Integer getMax() {
        return max;
    }

    private boolean member(Integer x)    {
        if (isEmpty())    {
            return false;
        }
        if (x.equals(min) || x.equals(max)) {
            return true;
        }
        if (isLeaf()) {
            return false;
        }
        return getCluster(high(x)).member(low(x));
    }

    @Override
    public Integer successor(Integer x) {
        if (isLeaf()) {
            /* The successor is present in a leaf only if x == 0 and max == 1 */
            if (x == 0 && !isEmpty() && max == 1) {
                return 1;
            }
            return null;
        }
        /* If x is less than the min, the successor must be min */
        if (!isEmpty() && x < min)   {
            return min;
        }
            /* Max element in x's cluster */
        Integer maxLow = getCluster(high(x)).max;

        /* if there exist an element in x's cluster that is greater than x,
           look for the successor in that cluster */
        if (maxLow != null && low(x) < maxLow)   {
            /* x's successor is in x's cluster */
            Integer offset = getCluster(high(x)).successor(low(x));
            return index(high(x), offset);
        }
        /* x's successor is not in x's cluster.
           Find the successor cluster of x's cluster */
        Integer succCluster = getSummary().successor(high(x));
        if (succCluster == null) {
            return null;
        }
        /* min in succCluster must be the successor */
        Integer offset = getCluster(succCluster).min;
        return index(succCluster, offset);
    }

    private Integer predecessor(Integer x)   {
        if (this.isLeaf()) {
            if (x == 1 && !isEmpty() && min == 0) {
                return 0;
            }
            return null;
        }
        if (!isEmpty() && x > max)   {
            return max;
        }
        Integer minLow = getCluster(high(x)).min;
        if (minLow != null && low(x) > minLow)   {
            Integer offset = getCluster(high(x)).predecessor(low(x));
            return index(high(x), offset);
        }
        Integer predCluster = getSummary().predecessor(high(x));
        if (predCluster == null) {
            if (min != null && x > min)    {
                return min;
            }
            return null;
        }
        Integer offset = getCluster(predCluster).max;
        return index(predCluster, offset);
    }

    private void emptyInsert(int x) {
        min = max = x;
    }

    @Override
    public void insert(Integer x)   {
        if (isEmpty())    {
            emptyInsert(x);
            return;
        }
        if (x < min)   {
            int swap = x;
            x = min;
            min = swap;
        }
        if (!isLeaf())  {
            if (getCluster(high(x)).isEmpty())   {
                getSummary().insert(high(x));
                getCluster(high(x)).emptyInsert(low(x));
            } else    {
                getCluster(high(x)).insert(low(x));
            }
        }
        max = Math.max(x, max);
    }

    @Override
    public void delete(Integer x)  {
        /* vEBT contains one element */
        if (!isEmpty() && min.equals(max))    {
            min = max = null;
            /* Base case */
        } else if(isLeaf())    {
            min = x == 0 ? 1 : 0;
            max = min;
        }
        /* vEBT contains >= 2 elements and u >= 4
            If x == min, find the new min */
        else    {
            if (!isEmpty() && min.equals(x))  {
                Integer firstCluster = summary.min;
                x = index(firstCluster, getCluster(firstCluster).min);
                min = x;
            }
            /* delete x from its cluster */
            getCluster(high(x)).delete(low(x));

            /* If the cluster becomes empty after deletion, update summary */
            if (getCluster(high(x)).isEmpty())   {
                getSummary().delete(high(x));

                /* If x == max, find the index of the last nonempty cluster */
                if (!isEmpty() && max.equals(x))   {
                    if(getSummary().isEmpty())  {
                        /* All clusters are empty - The only element left is min; update max */
                        max = min;
                    } else    {
                        /* If not all clusters are empty, update max */
                        Integer summaryMax = getSummary().max;
                        max = index(summaryMax, getCluster(summaryMax).max);
                    }
                }
            }
            /* x's cluster is not empty after deletion. if x == max, update max */
            else if (!isEmpty() && x.equals(max))  {
                max = index(high(x), getCluster(high(x)).max);
            }
        }
    }

    @Override
    public void decreaseKey(Integer item, int key){
        delete(item);
        insert(key);
    }

    @Override
    public Integer extractMin() {
        if (isEmpty())   {
            throw new NoSuchElementException("Empty vEB");
        }
        Integer minElement = min;
        delete(min);
        return minElement;
    }
}
