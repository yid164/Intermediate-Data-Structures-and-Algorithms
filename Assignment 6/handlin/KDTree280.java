package lib280.tree;


// Name: Yinsheng Dong
// Student Number: 11148648
// NSID; yid164
// Lecture Section: CMPT 280
import lib280.base.NDPoint280;
import lib280.list.LinkedList280;


/**
 * Created by yid164 on 2017/3/5.
 */
public class KDTree280{

    // the root node of the KD tree
    KDNode280 rootNode;
    // The dimension of the KD tree
    private int dim;

    /**
     * The constructor of KD Tree
     * @param dim
     */
    public KDTree280(int dim)
    {
        this.dim = dim;
        rootNode = null;
    }

    /**
     *
     * @param list array of comparable elements
     * @param left offset of start of subarray for which we want the median element
     * @param right offset of the end of subarray for which we want the median element
     * @param depth the height of the KD tree
     * @param j we wat to find the element that belongs at array index j
     * @PreCond: left <= j <= right
     * @PreCond: all element in the list are unique
     * @PostCond: the element x  that belongs at offset j
     * To find the median of the subarray between array indices 'left' and 'right', pass j = (right +left) /2
     */
    public void jSmallest(NDPoint280 [] list, int left, int right, int depth, int j)
    {
        // Partition the subarray suing last element, list[right] as a pivotIndex
        int pivotIndex;

        if(right > left)
        {
            // if the pivotIndex is equal to j, then we found the j-th smallest
            // element and it is the right place
            pivotIndex = partition(list,left,right,depth);

            // if the position j is smaller than the pivot index, we know that
            // the j-th smallest element must be between left, and pivotIndex-1
            // so do the recursion
            if(pivotIndex > j)
            {
                jSmallest(list, left, pivotIndex-1, depth,j);
            }

            // if j is larger than pivotIndex
            // so the j-th smallest element must be between pivotIndex+1 and right
            else if(pivotIndex < j)
            {
                jSmallest(list,pivotIndex+1, right, depth, j);
            }

            // Otherwise, the pivot ended up at list[j], and the pivot is the
            // j-th smallest element and done
        }
    }

    /**
     * Partition a subarray using its last element as a pivot
     * @param list array of comparable element
     * @param left lower limit on subarray to be partitioned
     * @param right upper limit on subarray to be partitioned
     * @param dim the dimension of the tree
     * @return the offset at which pivot element eded up
     *
     * @PreCond: all element in list are unique
     * @PostCOnd: all element smaller than the pivot appear in the leftmost part of the subarray
     *
     */
    public int partition (NDPoint280 list[], int left, int right, int dim)
    {
        // the index of pivot
        double pivot;
        // the temp NDPoint280 variable to do the exchange
        NDPoint280 temp;

        pivot = list[right].idx(dim);

        // the swapOffset must be the left side first
        int swapOffset = left;
        // the loop is doing for the element all element smaller than pivot appear in the
        // left most part of the subarray
        for(int i = left; i< right; i++)
        {
            if(list[i].idx(dim)<= pivot)
            {
                temp = list[swapOffset];
                list[swapOffset] = list[i];
                list[i] = temp;
                swapOffset++;
            }

        }
        temp = list[swapOffset];
        list[swapOffset] = list[right];
        list[right] = temp;
        return swapOffset;
    }


    /**
     * Building a k-d tree from a set of k-dimensional points is given
     * And it is a helper function to build a k-d tree
     * @param pointArray array of k-d point
     * @param left offset of start of subarray from which to build a kd-tree
     * @param right offset of end of subarray from which to build a kd-tree
     * @param depth the current depth in the partially built tree
     * @return
     */
    public KDNode280 kdtree(NDPoint280 [] pointArray, int left, int right, int depth)
    {
        // when the left is larger than the right, the tree is empty
        if(left>right)
        {
            return null;
        }

        // Select axis based on depth so that axis cycles through all
        int d = depth % dim;
        int median = (right-left)/2 + left;

        // put the median element in the correct position
        // Assume the dimension d parameter is added
        jSmallest(pointArray, left,right,d,median);
        // The dimension must equal to the tree dimension
        if(pointArray[median].dim() != this.dim)
        {
            throw new IllegalArgumentException("Wrong");
        }

        // Recreate node and construct subtree
        KDNode280 node = new KDNode280(pointArray[median], null, null);
        node.setLeftNode(kdtree(pointArray,left,median-1,depth+1));
        node.setRightNode(kdtree(pointArray,median+1,right,depth+1));
        return node;


    }

    /**
     * The actual built function for invoke the kdtree() method to create a new KD-tree
     * @param array the pointArray
     * setup the kd-tree to a new tree
     */
    public void createNewKDTree(NDPoint280[] array)
    {
        rootNode = kdtree(array,0,array.length-1,0);
    }


    /**
     * The range searching find on the lower and upper of the tree
     * invoke it in the main()
     * @param l the lowest numbers of the searching
     * @param u the highest numbers of the searching
     * @return the linkedList of the points in this range
     */
    public LinkedList280<NDPoint280> searchRange(NDPoint280 l, NDPoint280 u)
    {
        LinkedList280<NDPoint280> list = new LinkedList280<>();
        rangeSearch(rootNode, l, u, list, 0);
        return list;
    }


    /**
     * the method to do the range of the searching of the tree
     * @param node the current node of the tree
     * @param low the lowest value for searching
     * @param up the highest value for searching
     * @param list the list that store the founded items
     * @param depth the height of the tree
     */

    public void rangeSearch(KDNode280 node, NDPoint280 low, NDPoint280 up, LinkedList280<NDPoint280> list, int depth)
    {

        // the axis value of the tree
        int d = depth % dim;
        // when node is null, go return
        if(node == null)
        {
            return;
        }

        // The current node's item
        NDPoint280 currentItem = node.item;

        // if the current node's item is larger than lowest item, go continue
        // searching left tree
        if(currentItem.compareByDim(d,low)>=0)
        {
            rangeSearch(node.leftNode(),low, up, list, depth+1 );
        }

        // if the current node's item is smaller than the largest value, go continue
        // searching right tree
        if(currentItem.compareByDim(d,up)<=0)
        {
            rangeSearch(node.rightNode(), low, up, list, depth+1);
        }


        // setup a boolean variable to do the situation judgement
        boolean equal = true;
        for(int i = 0; i< dim && equal; i++)
        {
            // equal is false when the dimension of the point at the node
            if(currentItem.compareByDim(i,low)<0 || currentItem.compareByDim(i,up)>0)
            {
                // do not search outside of the range
                equal = false;
            }

        }
        // if the point is in the range, insert the item to the list
        if(equal==true)
        {
            list.insertFirst(currentItem);
        }



    }


    /**
     * toStringByLevel method, helper method to display the tree
     * @param result the root node
     * @param i the level of the node position
     * @return a string that display the tree
     * it is from other class in this package, and I just changed some variable
     */
    protected String toStringByLevel(KDNode280 result, int i)
    {
        StringBuffer blank = new StringBuffer((i-1) *5);
        for(int k = 0; k<i-1; k++)
        {
            blank.append("      ");
        }
        String rt = new String();
        if(result!=null && (this.rootNode.leftNode()!=null || result.rightNode()!=null))
        {
            rt += toStringByLevel(result.rightNode(),i+1);
        }
        rt += "\n" + blank + i +": ";

        if(result==null)
        {
            rt += "-";
        }
        else
        {
            rt += result.toString();
            if(this.rootNode.leftNode()!=null || result.rightNode()!=null)
            {
                rt += toStringByLevel(result.leftNode(), i+1);
            }
        }
        return rt;
    }

    /**
     *
     * @return to display the tree
     */
    public String toString()
    {
        return toStringByLevel(this.rootNode,1);
    }


    public static void main(String[] args)
    {

        // the 2-d tree testing
        // create a new NDPoint280 array
        NDPoint280[]array = new NDPoint280[7];
        // set variables in the array
        array[0] = new NDPoint280(new Double[]{5.0, 2.0});
        array[1] = new NDPoint280(new Double[]{9.0, 10.0});
        array[2] = new NDPoint280(new Double[]{11.0, 1.0});
        array[3] = new NDPoint280(new Double[]{4.0,3.0});
        array[4] = new NDPoint280(new Double[]{2.0,12.0});
        array[5] = new NDPoint280(new Double[]{3.0,7.0});
        array[6] = new NDPoint280(new Double[] {1.0,5.0});
        System.out.println("Input 2D points: ");
        for(int i=0; i< array.length; i++)
        {
            System.out.println(array[i]);
        }
        // using the array to create a 2-d tree, and print it out
        KDTree280 tree = new KDTree280(2);
        tree.createNewKDTree(array);
        System.out.println(tree+"\n");


        // the 3-d tree testing
        // create a new NDPoint280 array
        NDPoint280[] array1 = new NDPoint280[8];
        // set variables in the array
        array1[0] = new NDPoint280(new Double[] {1.0 , 12.0 , 1.0});
        array1[1] = new NDPoint280(new Double[]{18.0 , 1.0 , 2.0});
        array1[2] = new NDPoint280(new Double[]{2.0 , 12.0 , 16.0});
        array1[3] = new NDPoint280(new Double[]{7.0 , 3.0 , 3.0});
        array1[4] = new NDPoint280(new Double[]{3.0 , 7.0 , 5.0});
        array1[5] = new NDPoint280(new Double[]{16.0 , 4.0 , 4.0});
        array1[6] = new NDPoint280(new Double[]{4.0 , 6.0 , 1.0});
        array1[7] = new NDPoint280(new Double[]{5.0 , 5.0 , 17.0});
        System.out.println("Input 3D points: ");
        for(int i=0; i< array1.length; i++)
        {
            System.out.println(array1[i]);
        }
        // using this array to create a 3-d tree, and print it out
        KDTree280 tree1 = new KDTree280(3);
        tree1.createNewKDTree(array1);
        System.out.println(tree1);


        // rangeSearching() method test
        System.out.println("Looking for points between (0.0 , 1.0 , 0.0) and (4.0 , 6.0 , 3.0).");
        System.out.println("Found: ");
        double[] low = {0.0,1.0,0.0};
        double[] high ={4.0, 6.0, 3.0};
        NDPoint280 lower = new NDPoint280(low);
        NDPoint280 higher = new NDPoint280(high);

        LinkedList280<NDPoint280> find = tree1.searchRange(lower,higher);
        if(find.isEmpty())
        {
            System.out.println("There are no item between these range\n");

        }
        else
        {
            System.out.println(find.toString()+"\n");
        }


        System.out.println("Looking for points between (0.0 , 1.0 , 0.0) and (8.0 , 7.0 , 4.0).");
        System.out.println("Found");
        double [] low1 = {0.0 , 1.0 , 0.0};
        double [] high1 = {8.0 , 7.0 , 4.0};
        NDPoint280 lower1 = new NDPoint280(low1);
        NDPoint280 higher1 = new NDPoint280(high1);
        LinkedList280<NDPoint280> find1 = tree1.searchRange(lower1, higher1);
        if(find1.isEmpty())
        {
            System.out.println("There are no item between these range\n");

        }
        else
        {
            System.out.println(find1.toString()+"\n");
        }

        System.out.println("Looking for points between (0.0 , 1.0 , 0.0) and (17.0 , 9.0 , 10.0).");
        System.out.println("Found ");
        double [] low2 = {0.0 , 1.0 , 0.0};
        double [] high2 = {17.0 , 9.0 , 10.0};
        NDPoint280 lower2 = new NDPoint280(low2);
        NDPoint280 higher2 = new NDPoint280(high2);
        LinkedList280<NDPoint280> find2 = tree1.searchRange(lower2, higher2);
        if(find2.isEmpty())
        {
            System.out.println("There are no item between these range");

        }
        else
        {
            System.out.println(find2.toString()+"\n");
        }



    }

}
