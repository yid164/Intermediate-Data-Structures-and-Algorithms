package lib280.tree;

// Name: Yinsheng Dong
// Student Number: 11148648
// NSID: yid164
// Lecture Section: CMPT 280
import lib280.base.NDPoint280;

/**
 * Created by yid164 on 2017/3/5.
 */
public class KDNode280 extends BinaryNode280<NDPoint280> {


    /**
     * Construct a new node with item x.
     *
     * @param x the item placed in the new node
     * @timing Time = O(1)
     */
    public KDNode280(NDPoint280 x) {
        super(x);
    }

    @Override
    public KDNode280 leftNode()
    {
        return (KDNode280) this.leftNode;
    }

    @Override
    public KDNode280 rightNode()
    {
        return (KDNode280) this.rightNode;
    }

    /**
     * the real constructor of the KD-Node
     * @param x the NDPoint
     * @param leftNode left node of the KD-Tree
     * @param rightNode right node of the KD-Tree
     */
    public KDNode280(NDPoint280 x, KDNode280 leftNode, KDNode280 rightNode)
    {
        super(x);
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }
}
