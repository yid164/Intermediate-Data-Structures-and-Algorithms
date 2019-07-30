package lib280.tree;


// Name: Yinsheng Dong
// Student Number: 11148648
// NSID: yid164
// Lecture Section: CMPT 280
import lib280.base.Searchable280;
import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.NoCurrentItem280Exception;

/**
 * Created by yid164 on 2017/2/19.
 */
public class AVLTree280 <I extends Comparable<? super I>> extends LinkedSimpleTree280<I> implements Searchable280<I>{

    /**
     * inside node class constructor of the AVLTreeNode
     * @param <I>
     */
    protected static class AVLTreeNode280 < I extends Comparable<? super I>> extends BinaryNode280<I>
    {
        protected int lHeight;
        protected int rHeight;

        public int getLHeight()
        {
            return lHeight;
        }

        public int getRHeight()
        {
            return rHeight;
        }
        public void setLHeight(int l)
        {
            lHeight = l;
        }

        public void setRHeight(int r)
        {
            rHeight = r;
        }

        @Override
        public AVLTreeNode280<I> leftNode()
        {
            return (AVLTreeNode280<I>) super.leftNode();
        }
        @Override
        public AVLTreeNode280<I> rightNode()
        {
            return (AVLTreeNode280<I>) super.rightNode();
        }
        public AVLTreeNode280(I x) {
            super(x);
            lHeight = 0;
            rHeight = 0;
        }
    }



    /**	The current node of the AVL Tree
     * */
    protected BinaryNode280<I> currentNode;

    /**
     *The parent node of the AVL Tree
     * */
    protected BinaryNode280<I> parent;

    /**	If the searches are continued or not */
    protected boolean searchesContinue = false;

    /** get from OrderedSimpleTree*/
    protected boolean objectReferenceComparison = false;

    /**
     *
     * @return the left subtree
     * @throws ContainerEmpty280Exception
     */
    @Override
    public AVLTree280<I> rootLeftSubtree() throws ContainerEmpty280Exception
    {
        return (AVLTree280<I>) super.rootLeftSubtree();
    }

    /**
     *
     * @return the right subtree
     * @throws ContainerEmpty280Exception
     */
    @Override
    public AVLTree280<I> rootRightSubtree() throws ContainerEmpty280Exception
    {
        return (AVLTree280<I>) super.rootRightSubtree();
    }

    /**
     * Constructor of the AVL tree
     */
    public AVLTree280()
    {
        this.setRootNode(null);
    }

    @Override
    protected AVLTreeNode280<I> createNewNode(I item) {
        return new AVLTreeNode280<I>(item);
    }


    /**
     *
     * @return true if the root is null, false otherwise
     */
    @Override
    public boolean isEmpty()
    {
        if(this.rootNode == null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     *
     * @return false in all conditions because the AVL tree won't be full
     */
    @Override
    public boolean isFull()
    {
        return false;
    }

    /**
     *
     * @return the current item
     * @throws NoCurrentItem280Exception while the item does not exists
     */
    @Override
    public I item() throws NoCurrentItem280Exception
    {
        if(!itemExists())
        {
            throw new NoCurrentItem280Exception("There is no item in current position");
        }
        else
        {
            return currentNode.item();
        }
    }

    /**
     *
     * @return true if the current item is not null, false otherwise
     */
    @Override
    public boolean itemExists() {
        if(currentNode == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * to determine the current node is in below bottom of the tree or not
     * @return true if it is in the bottom, false otherwise
     * the big - O should be O(1)
     */
    public boolean below()
    {
        if(currentNode == null && parent!=null && isEmpty())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * to determine the current node in above of the tree root or not
     * @return true if it is in the before the root, false otherwise
     * the big - O should be O(1)
     */
    public boolean above()
    {
        if(parent==null && currentNode == null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * to determine if the tree contains the item y or not
     * @param y item whose presence is to be determined
     * @return true if y is contained in the tree, false otherwise
     * the big - O should be O(n) of the worst case which n is the height of the tree
     */
    @Override
    public boolean has(I y) {

        BinaryNode280<I> sParent = parent;
        BinaryNode280<I> sCur = currentNode;
        search(y);

        boolean fi;
        fi = itemExists();
        parent = sParent;
        currentNode = sCur;
        return fi;

    }

    /**
     * Override the method of membershipEquals in the OrderedSimpleTree280.java
     * @param x item to be compared to y
     * @param y item to be compared to x
     * @return true if the x y are equal whatever they are String, int, double... false otherwise
     */
    @Override
    public boolean membershipEquals(I x, I y) {
        if(objectReferenceComparison)
        {
            // if the x y are the int, double, float.. primitive variable
            if(x == y)
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        // if the x y are the instance of the comparable variable
        else if((x instanceof Comparable) && (y instanceof Comparable))
        {
            if(x.compareTo(y) == 0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        // if the x y are the string variable
        else if (x.equals(y))
        {
            return true;
        }

        else
        {
            return false;
        }
    }

    /**
     * Go to the position of item x in the AVL tree
     * @param x item being sought
     * @return nothing
     */
    @Override
    public void search(I x) {
        boolean found = false;
        // if the searchesContinue is false, and the current node is int above position, move the current node to the root
        // and the tree is null
        if(!searchesContinue || above())
        {
            parent = null;
            currentNode = rootNode;
        }
        // if the current node is not in the after last line of the tree, let parent node be the current node, and turn right
        else if(!below())
        {
            parent = currentNode;
            currentNode = currentNode.rightNode;
        }

        // if the item does not be found, and the item exists, then
        while(!found && itemExists())
        {
            // if the item is smaller than the current parent node, then turn left
            if(x.compareTo(item()) < 0)
            {
                parent = currentNode;
                currentNode = currentNode.leftNode;
            }
            // if the item is equal to the current node, the found item
            else if(x.compareTo(item())==0)
            {
                found = true;
            }

            // if the item is larger than the current parent node, then turn left
            else
            {
                parent = currentNode;
                currentNode = parent.rightNode;
            }
        }


    }

    /**
     * to restart the searches each time search is called
     * the Big - O is O(1)
     */
    @Override
    public void restartSearches() {

        searchesContinue = false;

    }


    /**
     * to resume the searches
     * the big - O is O(1)
     */
    @Override
    public void resumeSearches() {
        searchesContinue = true;

    }


    /**
     * To set the variable rootNode to the local variable
     * @return the BinaryTree node and set the AVLTreeNode
     */
    @Override
    public AVLTreeNode280<I> rootNode()
    {
        return (AVLTreeNode280<I>) super.rootNode();
    }


    /**
     * do the insertion by the given item
     * @param item that people want to insert
     */
    public void insert(I item)
    {
        this.insert(item,null);
    }


    /**
     * The insertion of the AVL tree
     * @param item the item that will be inserted
     * @param parent the root for the tree
     */
    protected void insert (I item, AVLTreeNode280<I> parent)
    {
        // if the tree is empty, put the item on the root
        if(this.isEmpty())
        {
            this.setRootNode(this.createNewNode(item));
            return;
        }

        // if the tree has already have root, take the root to the variable
        AVLTreeNode280<I> root = this.rootNode();
        // if the item smaller than the root, go left
        if(item.compareTo(root.item())<0)
        {
            // if the left node is not null, be recursive and inserted
            if(root.leftNode()!=null)
            {
                this.rootLeftSubtree().insert(item,this.rootNode());
            }
            // if the left node is null, put the item on the left node
            else
            {
                root.setLeftNode(new AVLTreeNode280<I>(item));
            }

            // the maximum left height should be left + right + 1
            int leftHeight = Math.max(root.leftNode().getLHeight(),root.leftNode().getRHeight()) + 1;
            root.setLHeight(leftHeight);
        }

        // if the item is bigger than root, go right
        else
        {
            // if the right node is not null, be recursive and inserted
            if(root.rightNode()!=null)
            {
                this.rootRightSubtree().insert(item,this.rootNode());
            }

            // if the right node is null, put the item on the left node
            else
            {
                root.setRightNode(new AVLTreeNode280<I>(item));
            }

            // the maximum right height should be right + left + 1
            int rightHeight = Math.max(root.rightNode().getLHeight(),root.rightNode().getRHeight()) + 1;
            root.setRHeight(rightHeight);
        }
        this.restore(parent);
    }

    /**
     * calling the delete(), and do the deletion for the current node
     * @throws NoCurrentItem280Exception
     */
    public void deleteForCursor() throws NoCurrentItem280Exception
    {
        // if the item is not existed, throw an exception
        if(!itemExists())
        {
            throw new NoCurrentItem280Exception("There is no this item for deleting");
        }

        // make the item be the current node item
        I item = this.item();

        // set a node named parent, it is now the current node
        AVLTreeNode280<I> parent = (AVLTreeNode280<I>)this.currentNode;
        // set a other node named rightOfParent, it is now the current node's right node
        AVLTreeNode280<I> rightOfParent = (AVLTreeNode280<I>) this.currentNode.rightNode();
        // while the current and current's right node's left node are not null, let parent be the right node, and the right of parent's node
        // be the left node, this step doing the in-order successor
        while(parent!=null && rightOfParent.leftNode()!=null)
        {
            parent = rightOfParent;
            rightOfParent = rightOfParent.leftNode();
        }

        // then delete the item that now has, and take the in-order successor node go up
        this.delete(item,parent,this);
        this.currentNode = rightOfParent;
        this.parent = parent;
    }

    /**
     * The real deletion of delete the item
     * @param item the people want to delete
     * @param parent the parent node of the tree
     * @param tree0 the tree of the parent node
     * @throws ContainerEmpty280Exception
     */
    public void delete(I item, AVLTreeNode280<I> parent, AVLTree280<I> tree0) throws ContainerEmpty280Exception
    {
        // when the tree is empty, throw the exception
        if(this.isEmpty())
        {
            throw new ContainerEmpty280Exception("Can not delete item in an empty tree");
        }
        // let root be the tree root node
        AVLTreeNode280<I> root = this.rootNode();
        // if the root's item is the item that going be deleted
        if(item.compareTo(root.item())==0)
        {
            // create a swapNode that doing the swap
            AVLTreeNode280<I> swapNode = null;
            boolean swap = false;
            // if the root has no right node, go left, swap the node to the swapNode
            if(root.rightNode() == null)
            {
                swapNode = root.leftNode();
                swap = true;
            }
            // if the root has no left node, go right, swap the left node to the swapNode
            if(root.leftNode() == null)
            {
                swapNode = root.rightNode();
                swap = true;
            }
            // if the swap is true, then set up the parent's left and right node to the swapNode
            if(swap==true)
            {
                if(parent.leftNode() == root)
                {
                    parent.setLeftNode(swapNode);
                }
                else if(parent.rightNode() == root)
                {
                    parent.setRightNode(swapNode);
                }
                else if(parent==null)
                {
                    this.rootNode = swapNode;
                }
            }
            // if the swap is not true, then set up a new AVL node be the right node, and create a copied node and delete it
            else
            {
                AVLTreeNode280<I> newNode= (AVLTreeNode280<I>)this.rootNode().rightNode();
                while(newNode.leftNode()!=null)
                {
                    newNode = newNode.leftNode();
                }

                AVLTreeNode280<I> copiedNode = this.rootNode();
                I copiedItem = newNode.item();
                this.delete(copiedItem, null, tree0);
                copiedNode.setItem(copiedItem);
            }
        }

        // if the item is smaller than root item, then go left, do the recursion
        else if(item.compareTo(root.item())<0)
        {
            this.rootLeftSubtree().delete(item, root, tree0);

            // set the height of the left node, so can call the restore function to determine if should do the rotation
            if(root.leftNode()==null)
            {
                root.setLHeight(0);
            }
            else
            {
                int leftHeight = root.leftNode().getLHeight();
                int rightHeight = root.leftNode().getRHeight();
                root.setLHeight(Math.max(leftHeight,rightHeight)+1);
            }
            restore(parent);
        }

        // if the item is larger than root item, then go right, do the recursion
        else
        {
            this.rootRightSubtree().delete(item, root, tree0);

            // set the height of the right node.. call the restore function to determine the rotation's working
            if(root.rightNode()==null)
            {
                root.setRHeight(0);
            }
            else
            {
                int leftHeight = root.rightNode().getLHeight();
                int rightHeight = root.rightNode().getRHeight();
                root.setRHeight(Math.max(leftHeight,rightHeight)+1);
            }
            restore(parent);
        }
    }


    /**
     * the left rotation of the AVL tree
     * @param parent the root of the AVL tree
     */
    public void leftRotation(AVLTreeNode280<I> parent)
    {
        // root is the critical node
        AVLTree280<I> root = this;
        // right is the critical node's right node
        AVLTree280<I> right = this.rootRightSubtree();
        // leftForRight is the critical node's right node's left node
        AVLTree280<I> leftForRight = right.rootLeftSubtree();


        // if the parent node is not null, change to the right of critical node
        if(parent != null)
        {
            if(parent.leftNode() != this.rootNode())
            {
                parent.setRightNode(right.rootNode());
            }
            else
            {
                parent.setLeftNode(right.rootNode());
            }
        }

        // if the leftForRight node is empty, so the right height of the root should be 0
        if(leftForRight.isEmpty())
        {
            root.rootNode().setRHeight(0);
        }
        // otherwise, the right height of the root should be maximum of the right
        else
        {
            int rightHeight = Math.max(leftForRight.rootNode().getLHeight(),leftForRight.rootNode().getRHeight()+1);
            root.rootNode().setRHeight(rightHeight);
        }


        // if the critical node is empty, now the left height should be 0
        if(root.isEmpty())
        {
            right.rootNode().setLHeight(0);
        }
        // otherwise, the left height should be maximum of the left
        else
        {
            int rightHeight = Math.max(root.rootNode().getLHeight(), root.rootNode().getRHeight()+1);
            right.rootNode().setLHeight(rightHeight);
        }

        // change the root to the right of the root
        root.rootNode().setRightNode(leftForRight.rootNode());
        // change the right node to the root node, and put the original root to the sub node of the right node
        right.rootNode().setLeftNode(root.rootNode());
        this.setRootNode(right.rootNode());
    }


    /**
     * The right rotation of the AVL tree
     * @param parent the root node of the tree
     */
    public void rightRotation(AVLTreeNode280<I> parent)
    {
        // root is the critical node
        AVLTree280<I> root = this;
        // left is the critical node's left node
        AVLTree280<I> left = this.rootLeftSubtree();
        // rightForLeft is the critical node's left node's right node
        AVLTree280<I> rightForLeft = left.rootRightSubtree();


        // if the parent is not null, move the cursor to the critical node
        if(parent != null)
        {
            if(parent.leftNode() == this.rootNode())
            {
                parent.setLeftNode(left.rootNode());
            }
            else
            {
                parent.setRightNode(left.rootNode());
            }
        }

        // if the critical node is not empty, the right tree height should be the maximum of the left and right +1
        if(!root.isEmpty())
        {
            int rightHeight = Math.max(root.rootNode().getLHeight(), root.rootNode().getRHeight()+1);
            left.rootNode().setRHeight(rightHeight);
        }
        // otherwise the left root node's height should be 0
        else
        {
            left.rootNode().setRHeight(0);
        }

        if(!rightForLeft.isEmpty())
        {
            int leftHeight = Math.max(rightForLeft.rootNode().getLHeight(), rightForLeft.rootNode().getRHeight()+1);
            root.rootNode().setLHeight(leftHeight);
        }
        else
        {
            root.rootNode().setLHeight(0);
        }

        // move the rightForLeft tree to be the sub tree of the original critical node
        root.rootNode().setLeftNode(rightForLeft.rootNode());
        // move the original critical node to be the sub tree of the original left node
        left.rootNode().setRightNode(root.rootNode());
        // replace the original left node to the critical node
        this.setRootNode(left.rootNode());
    }

    /**
     * the double left rotation of the AVL tree
     * @param parent the root of the AVL tree
     */
    public void doubleLeftRotation(AVLTreeNode280<I> parent)
    {
        // do the right rotation first, then do left rotation
        this.rootRightSubtree().rightRotation(this.rootNode());
        this.leftRotation(parent);
    }

    /**
     * the double right rotation of the AVL tree
     * @param parent the root of the AVL tree
     */
    public void doubleRightRotation(AVLTreeNode280<I> parent)
    {
        // do the left rotation first, then do right rotation
        this.rootLeftSubtree().leftRotation(this.rootNode());
        this.rightRotation(parent);
    }


    /**
     * to the imbalance method to calculate the left height and right height's deduction
     * @param parent the root node of the tree
     * @return the integer of there's deduction
     */
    protected int imbalance(AVLTreeNode280<I> parent) {
        int a = parent.getLHeight()-parent.getRHeight();
        return a;
    }


    /**
     * restore to do judgment of the different situation for the AVL tree
     * @param parent the root node of the AVL tree
     */
    public void restore(AVLTreeNode280<I> parent)
    {
        int imbalanceR = this.imbalance(this.rootNode());

        // If the imbalance is <= 1, then N is not a critical node and no rotations are necessary.
        if( Math.abs(imbalanceR) <= 1) return;

        if( imbalanceR == 2 )  { // If root is left heavy...
            if( this.imbalance(this.rootNode().leftNode()) >= 0 ) {  // If root.leftNode is left-heavy...
                // Then we need a right rotation.
                this.rightRotation(parent);
            }
            else {
                // We need a double-right rotation.
                this.doubleRightRotation(parent);
            }
        }
        else {  // Root is right heavy
            if( this.imbalance(this.rootNode().rightNode()) <= 0 ) {  // If root.rightNode is right-heavy
                // We need a left rotation.
                this.leftRotation(parent);
            }
            else {
                // We need a double left rotation.
                this.doubleLeftRotation(parent);
            }
        }


    }

    /**
     * method from LinkedSimpleTree280.java
     * to show up the level of tree
     * @param i the level for the root of this (sub)lib280.tree
     * @return the string
     * the Big-O should be O(n)
     */
    @Override
    public String toStringByLevel(int i)
    {
        StringBuffer blanks = new StringBuffer((i - 1) * 5);
        for (int j = 0; j < i - 1; j++)
            blanks.append("     ");

        String result = new String();
        if (!isEmpty() && (!rootLeftSubtree().isEmpty() || !rootRightSubtree().isEmpty()))
            result += rootRightSubtree().toStringByLevel(i+1);

        result += "\n" + blanks + i + ": " ;
        if (isEmpty())
            result += " - ";
        else
        {
            result += rootItem();
            if (!rootLeftSubtree().isEmpty() || !rootRightSubtree().isEmpty())
                result += rootLeftSubtree().toStringByLevel(i+1);
        }
        return result;
    }

    @Override
    public String toString() {
        return this.toStringByLevel(1);
    }


    /**
     * the main method for testing
     * @param args
     */
    public static void main(String[] args) {


        // test the right rotation and insert method
        System.out.println("*******************************************************");
        System.out.println("*******************************************************");
        System.out.println("Now, I am going to test the right rotation and insert method");
        System.out.println("My input order is 10, 9, 8, 7, 6, 5, 4, 3, 2, 1");
        AVLTree280<Integer> tree = new AVLTree280<>();
        tree.insert(10);
        tree.insert(9);
        tree.insert(8);
        tree.insert(7);
        tree.insert(6);
        tree.insert(5);
        tree.insert(4);
        tree.insert(3);
        tree.insert(2);
        tree.insert(1);
        System.out.println(tree);

        // test the left rotation and insert method
        System.out.println("*******************************************************");
        System.out.println("*******************************************************");
        System.out.println("Now, I am going to test the left rotation and insert method");
        System.out.println("My input order is 11, 12, 13, 14, 15, 16, 17, 18, 19, 20");
        tree = new AVLTree280<>();
        tree.insert(11);
        tree.insert(12);
        tree.insert(13);
        tree.insert(14);
        tree.insert(15);
        tree.insert(16);
        tree.insert(17);
        tree.insert(18);
        tree.insert(19);
        tree.insert(20);
        System.out.println(tree);
        System.out.println("*******************************************************");
        System.out.println("*******************************************************");

        // test the has(), search(), deletion method

        System.out.println("Now, I am going to test the has() method of the tree");
        if(tree.has(11))
        {
            System.out.println("Yes, the tree has the integer 11");
        }
        else
        {
            System.out.println("Noo, the tree does not have the integer 11, the application error!!!");
        }
        if(tree.has(12))
        {
            System.out.println("Yes, the tree has the integer 12");
        }
        else
        {
            System.out.println("Noo, the tree does not have the integer 12, the application error!!!");
        }
        if(tree.has(13))
        {
            System.out.println("Yes, the tree has the integer 13");
        }
        else
        {
            System.out.println("Noo, the tree does not have the integer 13, the application error!!!");
        }
        if(tree.has(14))
        {
            System.out.println("Yes, the tree has the integer 14");
        }
        else
        {
            System.out.println("Noo, the tree does not have the integer 14, the application error!!!");
        }
        if(tree.has(15))
        {
            System.out.println("Yes, the tree has the integer 15");
        }
        else
        {
            System.out.println("Noo, the tree does not have the integer 15, the application error!!!");
        }
        System.out.println("*******************************************************");
        System.out.println("*******************************************************");

        System.out.println("Now, I am going to test the search() and deleteForCursor() methods");
        tree.search(16);
        if(tree.currentNode.item() == 16)
        {
            System.out.println("Yes, I used the search() method to get 16, and the current node is now on 16");
            System.out.println("Now, I am going to delete Integer 16 which I just used the search() function founded");
            tree.deleteForCursor();
            System.out.println(tree);
        }
        else
        {
            System.out.println("Noo, something wrong, I didn't use search() method to get 16");
        }
        System.out.println("*******************************************************");
        System.out.println("*******************************************************");
        tree.search(14);
        if(tree.currentNode.item() == 14)
        {
            System.out.println("Yes, I used the search() method to get 14, and the current node is now on 14");
            System.out.println("Now, I am going to delete Integer 14 which I just used the search() function founded");
            tree.deleteForCursor();
            System.out.println(tree);
        }
        else
        {
            System.out.println("Noo, something wrong, I didn't use search() method to get 17");
        }
        System.out.println("*******************************************************");
        System.out.println("*******************************************************");
        tree.search(15);
        if(tree.currentNode.item() == 15)
        {
            System.out.println("Yes, I used the search() method to get 15, and the current node is now on 15");
            System.out.println("Now, I am going to delete Integer 15 which I just used the search() function founded");
            tree.deleteForCursor();
            System.out.println(tree);
        }
        else
        {
            System.out.println("Noo, something wrong, I didn't use search() method to get 15");
        }
        System.out.println("*******************************************************");
        System.out.println("*******************************************************");
        System.out.println("*******************************************************");
        System.out.println("The test succeed");


    }
}
