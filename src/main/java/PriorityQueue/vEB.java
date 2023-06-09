package PriorityQueue;

import java.util.Vector;

public class vEB {
    private final long u;
    private Integer min, max;
    private final vEB summary;
    private final Vector<vEB> clusters;

    public vEB(long u) {
        this.u = u;
        this.min = this.max = null;

        if (!isLeaf())  {
            int numClusters = this.upperSqrt();
            this.summary = new vEB(numClusters);
            this.clusters = new Vector<>(numClusters);

            for (int i = 0; i < numClusters; i++)   {
                this.clusters.add(new vEB(lowerSqrt()));
            }
        } else    {
            this.summary = null;
            this.clusters = null;
        }
    }

    private int upperSqrt()    {
        return (int) Math.pow(2, Math.ceil((Math.log(u) / Math.log(2)) / 2));
    }

    private int lowerSqrt()    {
        return (int) (Math.pow(2, Math.floor((Math.log(u) / Math.log(2)) / 2)));
    }

    private int high(int x) {
        return (int) (Math.floor(1.0 * x / lowerSqrt()));
    }

    private int low(int x)  {
        return x % lowerSqrt();
    }

    private int index(int x, int y) {
        return x * lowerSqrt() + y;
    }

    private boolean isEmpty()   {
        return min == null;
    }

    private boolean isLeaf()    {
        return u == 2;
    }

    private boolean member(int x)   {
        if (isEmpty())  {
            return false;
        }
        if (min == x || max == x)   {
            return true;
        }
        if (isLeaf())   {
            return false;
        }
        return this.clusters.get(high(x)).member(low(x));
    }


    private Integer successor(int x)    {
        if (isLeaf())   {
            if (x == 0 && max != null && max == 1) {
                return 1;
            }
            else return null;
        }
        if (!isEmpty() && x < min)   {
            return min;
        }
        Integer maxLow = clusters.get(high(x)).max;
        if (maxLow != null && low(x) < maxLow)   {
            Integer offset = clusters.get(high(x)).successor(low(x));
            return index(high(x), offset);
        } else    {
            Integer succCluster = summary.successor(high(x));
            if(succCluster == null) {
                return null;
            } else    {
                Integer offset = clusters.get(succCluster).min;
                return index(succCluster, offset);
            }
        }
    }


    private Integer predecessor(int x)  {
        if(isLeaf())   {
            if(x == 1 && !isEmpty() && min == 0) {
                return 0;
            }
            return null;
        } else if(!isEmpty() && x > max)   {
            return max;
        }

        Integer minLow = clusters.get(high(x)).min;
        if (minLow != null && low(x) > minLow)   {
            Integer offset = clusters.get(high(x)).predecessor(low(x));
            return index(high(x), offset);
        } else    {
            Integer prevCluster = summary.predecessor(high(x));
            if(prevCluster == null) {
                if(min != null && x > min)    {
                    return min;
                }
                return null;
            } else    {
                Integer offset = clusters.get(prevCluster).max;
                return index(prevCluster, offset);
            }
        }
    }

    private void delete(int x)   {
        if(!isEmpty() && min.equals(max))    {
            min = max = null;
        } else if(isLeaf())  {
            min = x == 0 ? 1 : 0;
            max = min;
        } else    {
            if (x == min)  {
                Integer firstCluster = summary.min;
                x = index(firstCluster, clusters.get(firstCluster).min);
                min = x;
            }
            clusters.get(high(x)).delete(low(x));
            if (clusters.get(high(x)).isEmpty())    {
                summary.delete(high(x));
                if (x == max)   {
                    Integer summaryMax = summary.max;
                    if (summaryMax == null)  {
                        max = min;
                    } else    {
                        max = index(summaryMax, clusters.get(summaryMax).max);
                    }
                }
            } else if (!isEmpty() && max == x) {
                max = index(high(x), clusters.get(high(x)).max);
            }
        }
    }

    private void emptyInsert(int x) {
        min = max = x;
    }

    private void insert(int x)  {
        if (isEmpty())  {
            emptyInsert(x);
            return;
        }
        if (x < min)   {
            int swap = min;
            min = x;
            x = swap;
        }
        if (!isLeaf())  {
            if(clusters.get(high(x)).isEmpty())    {
                summary.insert(high(x));
                clusters.get(high(x)).emptyInsert(low(x));
            } else    {
                clusters.get(high(x)).insert(low(x));
            }
        }
        if (x > max)    {
            max = x;
        }
    }
}
