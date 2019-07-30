package lib280.tree;


// Name: Yinsheng Dong
// Student Number: 11148648
// NSID: yid164
// Lecutre: CMPT 280

import lib280.base.Dispenser280;
import lib280.exception.ContainerFull280Exception;
import lib280.exception.DuplicateItems280Exception;
import lib280.exception.NoCurrentItem280Exception;

/**
 * Created by yid164 on 2017/1/29.
 */
public class ArrayedHeap280 < I extends Comparable<I>> extends ArrayedBinaryTree280<I> implements Dispenser280<I>{
    /**
     * Name: insert function
     * Pre: the tree is not full
     * insert the item x to the tree by its type
     * return nothing
     */
    @Override
    public void insert(I x) throws ContainerFull280Exception, DuplicateItems280Exception {
        // if the tree is full, throw a new full exception
        if(isFull())
        {
            throw new ContainerFull280Exception("Can not insert in a full heap");
        }
        // if the insertion is in normal condition
        else
        {
            count++;
            items[count] = x;
            currentNode=count;
        }
        // I variable is for swapping
        I temp;
        int a = count;
        
        // While intert item is larger than its parent and is not at the root, swap it with its parent
        while (this.items[a] != items[1] && items[a].compareTo(this.items[findParent(a)])>0)
        {
            temp = items[findParent(a)];
            items[findParent(a)] = items[a];
            items[a] = temp;
            a = findParent(a);
        }

    }
    /**
     * Name: deleteItem function
     * Pre: the item is in the cursor must exists
     * delete the item in the cursor and re-arrenge tree
     * return nothing
     */
    @Override
    public void deleteItem() throws NoCurrentItem280Exception {
        
        // If the item is in the cursor does not exist, throw new exception
        if(!itemExists() )
        {
            throw new NoCurrentItem280Exception();
        }
        // Let the cursor is in the first item (root)
        currentNode = 1;
        
        // If the cursor is in the tree has more than one item, do not delete the last one
        // copy the last item in the array to the current position.
        if( count > 1 && currentNode < count )
        {
            items[currentNode] = items[count];
        }

        count--;
        
        // when deleting the last item, redo the previous cursor (item)
        if( currentNode == count+1 )
        {
            currentNode--;
        }
        
        // the int variable is one, to make surethe left or right child is bigger than the new root
        int n = 1;
        
        // while the variable has a left child
        while( findLeftChild(n) <= count )
        {
            int child = findLeftChild(n);

            // if the right child exists and is larger than left, select right
            if( child + 1 <= count && items[child].compareTo(items[child+1]) < 0 ) {
                child++;
            }

            // if the parent node is smaller than the child, then swap them
            if( items[n].compareTo(items[child]) < 0 )
            {
                I temp = items[n];
                items[n] = items[child];
                items[child] = temp;
                n = child;
            }
            else return;

        }




    }

    
    /**
     * Constructor of the ArrayedHeap280
     */
    @SuppressWarnings("unchecked")
    public ArrayedHeap280(int cap)
    {
        super(cap);
        this.items = (I[]) new Comparable[cap+1];
    }

    /**
     * void main function for testing the heap
     */
    public static void main(String [] args)
    {
        // test the create a new heap, it is has 10 cap
        ArrayedHeap280<Integer> H = new ArrayedHeap280<Integer>(10);
        
        // test the insert function, from insert integer 1 to 10
        H.insert(1);
        H.insert(2);
        H.insert(3);
        System.out.println("The Heap list should be 3, 1, 2,");
        System.out.println(H);
        H.insert(4);
        System.out.println("The Heap list should be 4, 3, 2, 1");
        System.out.println(H);
        H.insert(5);
        System.out.println("The Heap list should be 5, 4, 2, 1, 3");
        System.out.println(H);
        H.insert(6);
        System.out.println("The Heap list should be 6, 4, 5, 1, 3, 2");
        System.out.println(H);
        H.insert(7);
        System.out.println("The Heap list should be 7, 4, 6, 1, 3, 2, 5");
        System.out.println(H);
        H.insert(8);
        System.out.println("The Heap list should be 8, 7, 6, 4, 3, 2, 5, 1");
        System.out.println(H);
        H.insert(9);
        System.out.println("The Heap list should be 9, 8, 6, 7, 3, 2, 5, 1, 4");
        System.out.println(H);
        H.insert(10);
        System.out.println("The Heap list should be 10, 9, 6, 7, 8, 2, 5, 1, 4, 3");
        
        System.out.println(H);
        
        // test the exception of insert while the heap is full
        Exception x = null;
        try
        {
            H.insert(5);
        }
        catch (ContainerFull280Exception e)
        {
            x=e;
        }
        finally {
            if(x==null)
            {
                System.out.println("Expected exception insert the item in a full heap.  Got none.");
            }
            else
            {
                System.out.println("Catch the container full exception, Okay");
            }
        }
        System.out.println("The cursor is now in the " + H.currentNode);
        
        // test for deleteItem function, delete 10 time, to see if it is correct
        H.deleteItem();
        System.out.println("The Heap list should be 9, 8, 6, 7, 3, 2, 5, 1, 4,");
        System.out.println(H);
        H.deleteItem();
        System.out.println("The Heap list should be 8, 7, 6, 4, 3, 2, 5, 1,");
        System.out.println(H);
        H.deleteItem();
        System.out.println("The Heap list should be 7, 4, 6, 1, 3, 2, 5,");
        System.out.println(H);
        H.deleteItem();
        System.out.println("The Heap list should be 6, 4, 5, 1, 3, 2,");
        System.out.println(H);
        H.deleteItem();
        System.out.println("The Heap list should be 5, 4, 2, 1, 3,");
        System.out.println(H);
        H.deleteItem();
        System.out.println("The Heap list should be 4, 3, 2, 1,");
        System.out.println(H);
        H.deleteItem();
        System.out.println("The Heap list should be 3, 1, 2,");
        System.out.println(H);
        H.deleteItem();
        System.out.println("The Heap list should be 2, 1,");
        System.out.println(H);
        H.deleteItem();
        System.out.println("The Heap list should be 1,");
        System.out.println(H);
        H.deleteItem();
        System.out.println(H);
        System.out.println("The Heap list should be empty,");
        
        // test the delete exception while deleting the empty heap 
        try
        {
            H.deleteItem();
        }
        catch (NoCurrentItem280Exception e)
        {
            x=e;
        }
        finally {
            if(x==null)
            {
                System.out.println("Expected exception delete the item in a empty heap.  Got none.");
            }
            else
            {
                System.out.println("Catch the No current item exception, Okay");
            }
        }


    }
}
