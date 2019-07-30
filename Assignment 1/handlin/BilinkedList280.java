// Name: Yinsheng Dong
// Student Number: 11148648
// NSID: yid164
// Lecture: CMPT 280

package lib280.list;


import lib280.base.BilinearIterator280;
import lib280.base.CursorPosition280;
import lib280.base.Pair280;
import lib280.exception.*;

/**	This list class incorporates the functions of an iterated 
	dictionary such as has, obtain, search, goFirst, goForth, 
	deleteItem, etc.  It also has the capabilities to iterate backwards 
	in the list, goLast and goBack. */
public class BilinkedList280<I> extends LinkedList280<I> implements BilinearIterator280<I>
{
	/* 	Note that because firstRemainder() and remainder() should not cut links of the original list,
		the previous node reference of firstNode is not always correct.
		Also, the instance variable prev is generally kept up to date, but may not always be correct.  
		Use previousNode() instead! */

	/**	Construct an empty list.
		Analysis: Time = O(1) */


	public BilinkedList280()
	{
		super();
	}

	/**
	 * Create a BilinkedNode280 this Bilinked list.  This routine should be
	 * overridden for classes that extend this class that need a specialized node.
	 * @param item - element to store in the new node
	 * @return a new node containing item
	 */

	protected BilinkedNode280<I> createNewNode(I item)
	{
        // Create a new BilinkedNode
	    BilinkedNode280<I> node = new BilinkedNode280<I>(item);
		return node;  // This line is present only to prevent a compile error.  You should remove it before
		              // completing this method.
		//TODO
	}


	/**
	 * Insert element at the beginning of the list
	 * @param x item to be inserted at the beginning of the list 
	 */
	public void insertFirst(I x) 
	{
        // create a node to store the item I(x)
        BilinkedNode280<I> newItem = createNewNode(x);
        // insert I(x) in front of the list
        newItem.setNextNode(this.head);
        // If the cursor is at the first node, cursor becomes the new node.
        if( this.position == this.head ) this.prevPosition = newItem;

        // If the list is empty, the new item becomes tail.
        if( this.isEmpty() ) this.tail = newItem;
        this.head = newItem;
		// TODO
	}

	/**
	 * Insert element at the beginning of the list
	 * @param x item to be inserted at the beginning of the list 
	 */
	public void insert(I x) 
	{
		this.insertFirst(x);
	}

	/**
	 * Insert an item before the current position.
	 * @param x - The item to be inserted.
	 */
	public void insertBefore(I x) throws InvalidState280Exception {
		if( this.before() ) throw new InvalidState280Exception("Cannot insertBefore() when the cursor is already before the first element.");
		
		// If the item goes at the beginning or the end, handle those special cases.
		if( this.head == position ) {
			insertFirst(x);  // special case - inserting before first element
		}
		else if( this.after() ) {
			insertLast(x);   // special case - inserting at the end
		}
		else {
			// Otherwise, insert the node between the current position and the previous position.
			BilinkedNode280<I> newNode = createNewNode(x);
			newNode.setNextNode(position);
			newNode.setPreviousNode((BilinkedNode280<I>)this.prevPosition);
			prevPosition.setNextNode(newNode);
			((BilinkedNode280<I>)this.position).setPreviousNode(newNode);
			
			// since position didn't change, but we changed it's predecessor, prevPosition needs to be updated to be the new previous node.
			prevPosition = newNode;			
		}
	}
	
	
	/**	Insert x before the current position and make it current item. <br>
		Analysis: Time = O(1)
		@param x item to be inserted before the current position */
	public void insertPriorGo(I x) 
	{
		this.insertBefore(x);
		this.goBack();
	}

	/**	Insert x after the current item. <br>
		Analysis: Time = O(1) 
		@param x item to be inserted after the current position */
	public void insertNext(I x) 
	{
		if (isEmpty() || before())
			insertFirst(x); 
		else if (this.position==lastNode())
			insertLast(x); 
		else if (after()) // if after then have to deal with previous node  
		{
			insertLast(x); 
			this.position = this.prevPosition.nextNode();
		}
		else // in the list, so create a node and set the pointers to the new node 
		{
			BilinkedNode280<I> temp = createNewNode(x);
			temp.setNextNode(this.position.nextNode());
			temp.setPreviousNode((BilinkedNode280<I>)this.position);
			((BilinkedNode280<I>) this.position.nextNode()).setPreviousNode(temp);
			this.position.setNextNode(temp);
		}
	}

	/**
	 * Insert a new element at the end of the list
	 * @param x item to be inserted at the end of the list 
	 */
	public void insertLast(I x) 
	{
        // create a new node store the I(x)
	    BilinkedNode280<I> newItem = createNewNode(x);
        // if the cursor is in the after, make the previou position store the new item
        if( this.after() ) this.prevPosition = newItem;

        // If list is empty, the both of head and tail are the new item
        if( this.isEmpty() ) {
            this.head = newItem;
            this.tail = newItem;
        }
        else {
            this.tail.setNextNode(newItem);
            this.tail = newItem;
        }
		// TODO
	}

	/**
	 * Delete the item at which the cursor is positioned
	 * @precond itemExists() must be true (the cursor must be positioned at some element)
	 */
	public void deleteItem() throws NoCurrentItem280Exception
	{
		if(!itemExists()) throw new NoCurrentItem280Exception("Cannot delete the non-exists item.");
		delete(this.currentPosition().item());
		// TODO

	}

	
	@Override
	public void delete(I x) throws ItemNotFound280Exception {
		if( this.isEmpty() ) throw new ContainerEmpty280Exception("Cannot delete from an empty list.");

		// Save cursor position
		LinkedIterator280<I> savePos = this.currentPosition();
		
		// Find the item to be deleted.
		search(x);
		if( !this.itemExists() ) throw new ItemNotFound280Exception("Item to be deleted wasn't in the list.");

		// If we are about to delete the item that the cursor was pointing at,
		// advance the cursor in the saved position, but leave the predecessor where
		// it is because it will remain the predecessor.
		if( this.position == savePos.cur ) savePos.cur = savePos.cur.nextNode();
		
		// If we are about to delete the predecessor to the cursor, the predecessor 
		// must be moved back one item.
		if( this.position == savePos.prev ) {
			
			// If savePos.prev is the first node, then the first node is being deleted
			// and savePos.prev has to be null.
			if( savePos.prev == this.head ) savePos.prev = null;
			else {
				// Otherwise, Find the node preceding savePos.prev
				LinkedNode280<I> tmp = this.head;
				while(tmp.nextNode() != savePos.prev) tmp = tmp.nextNode();
				
				// Update the cursor position to be restored.
				savePos.prev = tmp;
			}
		}
				
		// Unlink the node to be deleted.
		if( this.prevPosition != null)
			// Set previous node to point to next node.
			// Only do this if the node we are deleting is not the first one.
			this.prevPosition.setNextNode(this.position.nextNode());
		
		if( this.position.nextNode() != null )
			// Set next node to point to previous node 
			// But only do this if we are not deleting the last node.
			((BilinkedNode280<I>)this.position.nextNode()).setPreviousNode(((BilinkedNode280<I>)this.position).previousNode());
		
		// If we deleted the first or last node (or both, in the case
		// that the list only contained one element), update head/tail.
		if( this.position == this.head ) this.head = this.head.nextNode();
		if( this.position == this.tail ) this.tail = this.prevPosition;
		
		// Clean up references in the node being deleted.
		this.position.setNextNode(null);
		((BilinkedNode280<I>)this.position).setPreviousNode(null);
		
		// Restore the old, possibly modified cursor.
		this.goPosition(savePos);
		
	}
	/**
	 * Remove the first item from the list.
	 * @precond !isEmpty() - the list cannot be empty
	 */
	public void deleteFirst() throws ContainerEmpty280Exception
	{
        
        // if the list is empty, throw a new empty exception
		if( this.isEmpty() ) throw new ContainerEmpty280Exception("Cannot delete an item from an empty list.");

		// If the cursor is on the second node, set the prev pointer to null.
		if( this.prevPosition == this.head ) this.prevPosition = null;
        // else, if the cursor is on the first node, set the cursor to the next node.
		else if (this.position == this.head )  this.position = this.position.nextNode();

		// If deleting the last item, set the tail is null
		// Setting the head to null gets handled automatically in the following
		// unlinking.
		if( this.head == this.tail ) this.tail = null;

		// Unlink the first node.
		LinkedNode280<I> oldhead = this.head;
		this.head = this.head.nextNode();
		oldhead.setNextNode(null);
		// TODO
	}

	/**
	 * Remove the last item from the list.
	 * @precond !isEmpty() - the list cannot be empty
	 */
	public void deleteLast() throws ContainerEmpty280Exception
	{
        // if the list is empty, throw a new empty exception
		if( this.isEmpty() ) throw new ContainerEmpty280Exception("Cannot delete an item from an empty list.");
		else if( this.head != null && this.head == this.tail ) this.deleteFirst();
		else {

			// If the cursor is on the last node, we need to update the cursor.
			if( this.position == this.tail ) {
				// Find the node prior to this.position
				LinkedNode280<I> newPrev = this.head;
				while( newPrev.nextNode() != this.prevPosition) newPrev = newPrev.nextNode();
				this.position = this.prevPosition;
				this.prevPosition = newPrev;
			}

			// 	Find the second-last node -- note this makes the deleteLast() algorithm O(n)
			LinkedNode280<I> penultimate = this.head;
			while(penultimate.nextNode() != this.tail) penultimate = penultimate.nextNode();

			// If the cursor is in the after() position, then prevPosition
			// has to become the second last node.
			if( this.after() ) {
				this.prevPosition = penultimate;
			}

			// Unlink the last node.
			penultimate.setNextNode(null);
			this.tail = penultimate;
		}
	}

	
	/**
	 * Move the cursor to the last item in the list.
	 * @precond The list is not empty.
	 */
	public void goLast() throws ContainerEmpty280Exception
	{
        // if the list is empty, throw a new empty exception
		if(this.isEmpty()) throw new ContainerEmpty280Exception("Can not move cursor when list is empty");
        // the position is in the last node
		this.position=this.lastNode();
        // create a new node be the head, and use a while loop to comfirm the prePosition.
		LinkedNode280<I> node = this.head;
		while (node.nextNode()!=this.position)
		{
			node=node.nextNode();
			this.prevPosition = node;
		}

		// TODO
	}
  
	/**	Move back one item in the list. 
		Analysis: Time = O(1)
		@precond !before() 
	 */
	public void goBack() throws BeforeTheStart280Exception
	{
        // if the list is empty, throw a new empty exception
		if(this.isEmpty()) throw new ContainerEmpty280Exception("Can not go back to a empty list");
        // if the cursor is in the before, throw new exception
		if(this.before()) throw new Container280Exception("Can not go back in before position");
        // if the pre-position is in the head, the current position should be to the preposition, and pre is to null;
		if(this.prevPosition==this.head) this.position=this.prevPosition; this.position=null;
        // in other situation
		this.position = this.prevPosition;
		LinkedNode280<I> node = this.head;
		while(node.nextNode()!=this.position)
		{
			node=node.nextNode();
			this.prevPosition=node;
		}

		// TODO
	}

	/**	Iterator for list initialized to first item. 
		Analysis: Time = O(1) 
	*/
	public BilinkedIterator280<I> iterator()
	{
		return new BilinkedIterator280<I>(this);
	}

	/**	Go to the position in the list specified by c. <br>
		Analysis: Time = O(1) 
		@param c position to which to go */
	@SuppressWarnings("unchecked")
	public void goPosition(CursorPosition280 c)
	{
		if (!(c instanceof BilinkedIterator280))
			throw new InvalidArgument280Exception("The cursor position parameter" 
					    + " must be a BilinkedIterator280<I>");
		BilinkedIterator280<I> lc = (BilinkedIterator280<I>) c;
		this.position = lc.cur;
		this.prevPosition = lc.prev;
	}

	/**	The current position in this list. 
		Analysis: Time = O(1) */
	public BilinkedIterator280<I> currentPosition()
	{
		return  new BilinkedIterator280<I>(this, this.prevPosition, this.position);
	}

	
  
	/**	A shallow clone of this object. 
		Analysis: Time = O(1) */
	public BilinkedList280<I> clone() throws CloneNotSupportedException
	{
		return (BilinkedList280<I>) super.clone();
	}


	/* Regression test. */
	public static void main(String[] args) {

		// For List creation and insertFirst(), insertLast()
		BilinkedList280<Integer> list = new BilinkedList280<Integer>();
		System.out.println("The Bilinkedlist is : "+list);
		System.out.println("The Bilinkedlist should be empty ");
        // empty list checking
		if(list.isEmpty())
		{
			System.out.println("The empty check is correct");
		}
		else
		{
			System.out.println("The empty check is not correct");
		}
        // insert the number to the list: 2, 3, 4, 5
		list.insert(5);
		list.insert(4);
		list.insert(3);
		list.insert(2);
		System.out.println("after insertions, the list now is: "+ list);
		System.out.println("The list should be 2, 3, 4, 5");

        // test insert first function add 1
		list.insertFirst(1);
        // test the insert last function add 6
		list.insertLast(6);
        // now the list should be 1, 2, 3, 4, 5, 6
		System.out.println("After insertFirst and insertLast, the list now is "+list);
		System.out.println("The list should be 1, 2, 3, 4, 5, 6");
        // test the first item and last item are 1 and 6
		if(list.firstItem()==1&&list.lastItem()==6)
		{
			System.out.println("The insertFirst and insertLast functions are successfully run. ");
		}
		else System.out.println("The insertFirst and insertLast function are fail. ");

		// testing for createNewNode()
		BilinkedNode280<Integer>node = list.createNewNode(0);
		if(node.item().equals(0))
		{
			System.out.println("The createNewNode() function is success");
		}
		else System.out.println("The node is fail to creation"+node);


		// test for goLast() and goBack()
        // move the cursor to the last one 6, test the goLast
		list.goLast();
		System.out.println("The cursor should be in 6");
		if(list.item()==6)
		{
			System.out.println("... and it is okay ");
		}
		else System.out.println("but it is not in the 6... fail");

        // move the cursor to the second last one 5, test the goBack
		list.goBack();
		System.out.println("The cursor should now be in 5");
		if(list.item()==5)
		{
			System.out.println("... and it is okay ");
		}
		else System.out.println("but it is not in the 5... fail");

        // move the cursor to the thrid last one 4, test the goBack
		list.goBack();
		System.out.println("The cursor should now be in 4");
		if(list.item()==4)
		{
			System.out.println(".. and it is okay ");
		}
		else  System.out.println("BUt it is not in the 4... fail");

        // move the cursor to the last 6 agiain, test the goLast
		list.goLast();
		System.out.println("Now, back to last again, the cursor should now in the 6");
		if(list.item()==6)
		{
			System.out.println("... and it is okay");
		}
		else System.out.println("but in is not in the 6, fail");

		// test for deleteItem(), deleteFirst(), deleteLast()
        // Now the cursror is in the last, so when call the deleteItem(), should delete the last item 6
		list.deleteItem();
		System.out.println("Now delete the item 6, now print out the list should be 1, 2, 3, 4, 5, ");
		System.out.println(list);
        // use the insertFirst() to add the 0 to the beginning
		list.insertFirst(0);
		System.out.println("insert 0 to the first, so now print out the list should be 0, 1, 2, 3, 4, 5,");
		System.out.println(list);
        // use the insertLast() to add the 6 to the endding of the list
		list.insertLast(6);
		System.out.println("Now insert 6 to the last, so now print out the list should be 0, 1, 2, 3, 4, 5, 6,");
		System.out.println(list);
        // use the deleteFirst() to delete the beginning item 0
		list.deleteFirst();
		System.out.println("Now, delete the first item, the list should be 1, 2, 3, 4, 5, 6, ");
		System.out.println(list);
        // use the deleteLast() to delete the endding item 6
		list.deleteLast();
		System.out.println("Now, delete the last item, the list now should be 1, 2, 3, 4, 5,");
		System.out.println(list);

		// test for special case, insertFirst and insertLast in the empty list
        // make an empty list to test the insertFirst and insertLast
		list = new BilinkedList280<Integer>();
		System.out.println("Now the list will come back empty");
		System.out.println(list);
		list.insertFirst(0);
		System.out.println("insert first 0 to the empty list, now the list should be 0,");
		System.out.println(list);
		list = new BilinkedList280<Integer>();
		System.out.println("Now the list will come back empty again");
		System.out.println(list);
		list.insertLast(4);
		System.out.println("insert last 4 to the empty list, now the list should be 4,");
		System.out.println(list);

		// test for special case, deleteFirst(), delete(), and deleteLast() in the empty list
		list = new BilinkedList280<Integer>();
        // test the empty list delete first
		System.out.println("Deleting first item from empty list.");
		try {
			list.deleteFirst();
			System.out.println("Error: exception should have been thrown, but wasn't.");
		}
		catch( ContainerEmpty280Exception e) {
			System.out.println("Caught exception.  OK!");
		}
		list = new BilinkedList280<Integer>();
        // test the empty list delete last
		System.out.println("Deleting last item form empty list. ");
		try{
			list.deleteLast();
			System.out.println("Error: exception should have been thrown, but wasn't");
		}
		catch (ContainerEmpty280Exception e)
		{
			System.out.println("Caught exception.  Okay!");
		}
		list = new BilinkedList280<Integer>();
        // test the empty list delete any.
		System.out.println("Deleting a item from empty list: 5");
		try{
			list.delete(5);
			System.out.println("Error: exception should have been thrown, but wasn't");
		}
		catch (ContainerEmpty280Exception e)
		{
			System.out.println("Caught exception. okay");
		}
        // test the empty list delete the cursor item
		System.out.println("Deleting item from empty list");
		try{
			list.deleteItem();
			System.out.println("Error: exception should have been thrown, but wasn't");
		}
		catch (NoCurrentItem280Exception e)
		{
			System.out.println("Caught exception. Okay");
		}

		// test special case for goLast and goBack
		list = new BilinkedList280<Integer>();
        // test the empty list for move cursor to the last
		System.out.println("Go last in an empty list");
		try{
			list.goBack();
			System.out.println("Error: exception should have been thrown, but wasn't ");
		}
		catch (ContainerEmpty280Exception e)
		{
			System.out.println("Caught exception, Okay!");
		}
        
        // test the empty list for moving cursor to the back
		System.out.println("Go last in an empty list");
		list = new BilinkedList280<Integer>();
		try{
			list.goLast();
			System.out.println("Error: exception should have been thrown, but wasn't");
		}
		catch(ContainerEmpty280Exception e)
		{
			System.out.println("Caught exception, Okay!");
		}

		// test iterator function goLast() and goBack()
		list = new BilinkedList280<Integer>();
		list.insert(3);
		list.insert(2);
		list.insert(1);
		list.insert(0);
		System.out.println("Created a new list should be : 0, 1, 2, 3,");
		System.out.println(list);
		// crate a new iterator
		BilinkedIterator280<Integer> itr = new BilinkedIterator280<Integer>(list);
        // itr go to the last, so the last item should be 3
		itr.goLast();
		if(itr.item() == 3) System.out.println("The goLast() is working");
		else System.out.println("The goLast() is wrong");
		itr.goBack();
        // itr go to the back, so the item should be 2
		if(itr.item() == 2) System.out.println("The goBack() is working");
		else System.out.println("The goBack() function is wrong");

		// case for use iterator in the empty list
		list = new BilinkedList280<Integer>();
		BilinkedIterator280<Integer> itr1 = new BilinkedIterator280<Integer>(list);
        // move the cursor to the last when the list is empty
		System.out.println("Use the iterator go last in the empty list");
		try{
			itr1.goLast();
			System.out.println("Error: exception should have been thrown, but wasn't");
		}
		catch (ContainerEmpty280Exception e)
		{
			System.out.println("Caught exception, Okay!");
		}
        // move the cursor to the back when the list is emtpy
		System.out.println("Use the iterator go back in the empty list");
		try{
			itr1.goBack();
			System.out.println("Error: exception should have been thrown, but wasn't");
		}
		catch (ContainerEmpty280Exception e)
		{
			System.out.println("Caught exception, Okay!");
		}

		// TODO
	}
} 
