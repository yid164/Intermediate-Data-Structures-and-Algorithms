// Name: Yinsheng Dong
// Student Number: 11148648
// NSID: yid164
// Lecture: CMPT 280

package lib280.list;


import lib280.base.BilinearIterator280;
import lib280.exception.BeforeTheStart280Exception;
import lib280.exception.Container280Exception;
import lib280.exception.ContainerEmpty280Exception;

/**	A LinkedIterator which has functions to move forward and back, 
	and to the first and last items of the list.  It keeps track of 
	the current item, and also has functions to determine if it is 
	before the start or after the end of the list. */
public class BilinkedIterator280<I> extends LinkedIterator280<I> implements BilinearIterator280<I>
{

	/**	Constructor creates a new iterator for list 'list'. <br>
		Analysis : Time = O(1) 
		@param list list to be iterated */
	public BilinkedIterator280(BilinkedList280<I> list)
	{
		super(list);
	}

	/**	Create a new iterator at a specific position in the newList. <br>
		Analysis : Time = O(1)
		@param newList list to be iterated
		@param initialPrev the previous node for the initial position
		@param initialCur the current node for the initial position */
	public BilinkedIterator280(BilinkedList280<I> newList, 
			LinkedNode280<I> initialPrev, LinkedNode280<I> initialCur)
	{
		super(newList, initialPrev, initialCur);
	}
    
	/**
	 * Move the cursor to the last element in the list.
	 * @precond The list is not empty.
	 */
	public void  goLast() throws ContainerEmpty280Exception
	{
        // If the list is empty, throw a new empty exception
		if(this.list.isEmpty()) throw new ContainerEmpty280Exception("Can not move cursor when list is empty");
        // make the current node become the last node
		this.cur=this.list.lastNode();
        // use a while loops to comfirm the pre node
		LinkedNode280<I> node = this.list.head;
		while (node.nextNode()!=this.list.tail)
		{
			node=node.nextNode();
			this.prev = node;
		}
		// TODO
	}

	/**
	 * Move the cursor one element closer to the beginning of the list
	 * @precond !before() - the cursor cannot already be before the first element.
	 */
	public void goBack() throws BeforeTheStart280Exception
	{
        // If the list is empty, throw a new empty exception
		if(this.list.isEmpty()) throw new ContainerEmpty280Exception("Can not go back to a empty list");
        // If the current node is in the before, throw a new exception
		if(this.before()) throw new Container280Exception("Can not go back in before position");
        // If the pre node is on the head, let the current node in the front
		if(this.prev==this.list.head) this.cur=this.prev; this.cur=null;
        // For normal situation, make the current node become the previous node
		this.cur = this.prev;
        // use a while loops to comfire the pre node
		LinkedNode280<I> node = this.list.head;
		while(node.nextNode()!=this.cur)
		{
			node=node.nextNode();
			this.prev=node;
		}




		// TODO
	}

	/**	A shallow clone of this object. <br> 
	Analysis: Time = O(1) */
	public BilinkedIterator280<I> clone()
	{
		return (BilinkedIterator280<I>) super.clone();
	}


} 
