package lib280.tree;
// Name: Yinsheng Dong
// Student Number: 11148648
// NSID: yid164
// Lecture: CMPT 280
import lib280.base.Cursor280;
import lib280.base.CursorPosition280;
import lib280.dictionary.Dict280;
import lib280.exception.AfterTheEnd280Exception;
import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.ContainerFull280Exception;
import lib280.exception.DuplicateItems280Exception;
import lib280.exception.InvalidArgument280Exception;
import lib280.exception.InvalidState280Exception;
import lib280.exception.ItemNotFound280Exception;
import lib280.exception.NoCurrentItem280Exception;

public class ArrayedBinaryTreeWithCursors280<I> extends
		ArrayedBinaryTree280<I> implements Dict280<I>, Cursor280<I> {

	protected boolean searchesRestart;
	
	public ArrayedBinaryTreeWithCursors280(int cap) {
		super(cap);
		searchesRestart = true;
	}

	@Override
	public I obtain(I y) throws ItemNotFound280Exception {
		CursorPosition280 saved = this.currentPosition();
		this.goFirst();
		while(this.itemExists()) {
			if( membershipEquals(this.item(), y)) {
				I found = this.item();
				this.goPosition(saved);
				return found;
			}
			this.goForth();
		}
		this.goPosition(saved);
		throw new ItemNotFound280Exception("The given item could not be found.");
	}

	@Override
	// insert the element to the tree
	public void insert(I x) throws ContainerFull280Exception, DuplicateItems280Exception {
		// if the tree is already full
		if(isFull())
		{
			throw new ContainerFull280Exception("Can not insert elements in a full tree");
		}
		// if the element has already been inserted
		if(has(x))
		{
			throw new DuplicateItems280Exception("the item is inserted");
		}

		// count add 1, and insert it
		count++;
		items[count]= x;

	}

	@Override
	// delete the element by given
	public void delete(I x) throws ItemNotFound280Exception {
		if(!has(x))
		{
			throw new ItemNotFound280Exception("Did not find this item" + x);
		}
		else
		{
			items[count] = x;
			count--;
		}


	}

	@Override
	// delete the element by cursor
	public void deleteItem() throws NoCurrentItem280Exception {
		// if the element not exist, throw new exception
		if(!itemExists())
		{
			throw new NoCurrentItem280Exception("There are no current item exists");
		}
		// if the element is not in the last item and there are some element inside
		if (currentNode > 0 && currentNode<count)
		{
			items[currentNode] = items[count];
		}
		count = count-1;

		// if the element is in the last location
		if(currentNode == count+1)
		{
			currentNode--;
		}



	}


	@Override
	// the function that determine if the tree has the element or not
	public boolean has(I y) {
		// for-loops to check the element exsist
		for(int i=1; i<=count; i++)
		{
			if(items[i] == y)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		return false;

	}

	@Override
	public boolean membershipEquals(I x, I y) {
	    return x.equals(y);
	}

	@Override
	public void search(I x) {
		for(int i=1; i<=count; i++)
		{
			if(items[i] == x)
			{
				currentNode = i;
			}
			else
			{
				currentNode = count+1;
			}
		}

	}

	@Override
	public void restartSearches() {
		this.searchesRestart = true;
	}

	@Override
	public void resumeSearches() {
		this.searchesRestart = false;
	}

    @Override
	public CursorPosition280 currentPosition() {
		return new ArrayedBinaryTreePosition280(this.currentNode);
	}

	@Override
	public void goPosition(CursorPosition280 c) {
		if (!(c instanceof ArrayedBinaryTreePosition280))
			throw new InvalidArgument280Exception("The cursor position parameter"
					    + " must be a ArrayedBinaryTreePosition280<I>");

		this.currentNode = ((ArrayedBinaryTreePosition280)c).currentNode;
	}

	/**
	 * Move the cursor to the parent of the current node.
	 * @precond Current node is not the root.
	 * @throws InvalidState280Exception when the cursor is on the root already.
	 */
	public void parent() throws InvalidState280Exception {
		// if the current node is in the root or not exsist
		if(currentNode <= 1)
		{
			throw new InvalidState280Exception("Can not move the parent of the root");
		}
		// in the normal situation, move the current node to the parent
		else
		{
			currentNode = findParent(currentNode);
		}
        // TODO - Implement this method
	}

	/**
	 * Move the cursor to the left child of the current node.
	 * 
	 * @precond The tree must not be empty and the current node must have a left child.
	 * @throws ContainerEmpty280Exception if the tree is empty.
	 * @throws InvalidState280Exception if the current node has no left child.
	 */
	public void goLeftChild()  throws InvalidState280Exception, ContainerEmpty280Exception {
		// if the tree is an empty tree, exception
		if(isEmpty())
		{
			throw new ContainerEmpty280Exception("Can not move left in an empty tree");
		}
		// if the current node has no left child
		if(findLeftChild(currentNode) > count)
		{
			throw new InvalidState280Exception("There are no left child at this root");
		}
		// if the current node has left child
		else
		{
			currentNode = findLeftChild(currentNode);

		}
        // TODO - Implement this method
	}
	
	/**
	 * Move the cursor to the right child of the current node.
	 * 
	 * @precond The tree must not be empty and the current node must have a right child.
	 * @throws ContainerEmpty280Exception if the tree is empty.
	 * @throws InvalidState280Exception if the current item has no right child.
	 */
	public void goRightChild() throws InvalidState280Exception, ContainerEmpty280Exception {
		// if the tree is an empty tree, throw exception
		if(isEmpty())
		{
			throw new ContainerEmpty280Exception("Can not move right in an empty tree");
		}
		// if the current node has no right child
		if(findRightChild(currentNode) > count)
		{
			throw new InvalidState280Exception("There are no right child at this root");
		}

		// if the current node has right child, normal situation
		else
		{
			currentNode = findRightChild(currentNode);
		}
        // TODO - Implement this method
	}	
	
	/**
	 * Move the cursor to the sibling of the current node.
	 * 
	 * @precond The current node must have a sibling.  The tree must not be empty.
	 * @throws ContainerEmpty280Exception if the tree is empty.
	 * @throws InvalidState280Exception if the current item has no sibling.
	 */	
	public void goSibling() throws InvalidState280Exception, ContainerEmpty280Exception {
		// if the tree is an empty tree
		if(isEmpty())
		{
			throw new ContainerEmpty280Exception("Can not move the cursor to the sibling in a empty tree");
		}
		// if the current node is in the left child, then go to right
		if(currentNode % 2 == 0)
		{
			if(currentNode < count)
			{
				currentNode++;
			}
			else
			{
				throw new InvalidState280Exception("There are no right sibling in this left cursor");
			}
		}
		// if the current node is in the right child, then go left
		else
		{
			if(currentNode >1)
			{
				currentNode --;
			}
			else
			{
				throw new InvalidState280Exception("There are no left sibling in this right cursor");
			}
		}

		// TODO - Implement this method

	}
	
	/**
	 * Move the cursor to the root of the tree.
	 * 
	 * @precond The tree must not be empty.
	 * @throws ContainerEmpty280Exception if the tree is empty.
	 */
	public void root() throws ContainerEmpty280Exception {
		// if the tree is an empty tree
		if(isEmpty())
		{
			throw new ContainerEmpty280Exception("There are no roots in empty tree");
		}
		// go to the first current node, this is the root location
		else
		{
			currentNode = 1;
		}
        // TODO - Implement this method
	}	
	

	public static void main(String[] args) {
		ArrayedBinaryTreeWithCursors280<Integer> T = new ArrayedBinaryTreeWithCursors280<Integer>(10);
		
		// IsEmpty on empty tree.
		if(!T.isEmpty()) System.out.println("Test of isEmpty() on empty tree failed.");
		
		// Test root() on empty tree.
		Exception x = null;
		try {
			T.root();
		}
		catch(ContainerEmpty280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception moving to root of empty tree.  Got none.");
		}
		
		// test goFirst() on empty tree
		x = null;
		try {
			T.goFirst();
		}
		catch(ContainerEmpty280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception moving to first elelement of empty tree.  Got none.");
		}
	
		
		
		// Test goLeftChild() on empty tree.
		x = null;
		try {
			T.goLeftChild();
		}
		catch(ContainerEmpty280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception moving to left child in empty tree.  Got none.");
		}
		
		// Test goLeftChild() on empty tree.
		x = null;
		try {
			T.goRightChild();
		}
		catch(ContainerEmpty280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception moving to right child in empty tree.  Got none.");
		}
		
		
		// Check itemExists on empty tree
		if(T.itemExists() ) System.out.println("itemExists() returned true on an empty tree.");
		
		// Insert on empty tree.
		T.insert(1);
		
		// Check ItemExists on tree with one element.
		T.root();
		if(!T.itemExists() ) System.out.println("itemExists() returned false on a tree with one element with cursor at the root.");
		
		// isEmpty on tree with 1 element.
		if(T.isEmpty()) System.out.println("Test of isEmpty() on non-empty tree failed.");

		// Insert on tree with 1 element
		T.insert(2);
		
		// Insert some more elements
		for(int i=3; i <= 10; i++) T.insert(i);

		if(T.count() != 10  ) System.out.println("Expected tree count to be 10, got "+ T.count());

		
		// Test for isFull on a full tree.
		if(!T.isFull()) System.out.println("Test of isFull() on a full tree failed.");
		
		// Test insert on a full tree
		x = null;
		try {
			T.insert(11);
		}
		catch(ContainerFull280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception inserting into a full tree.  Got none.");
		}
		
		

		
		// Test positioning methods
		
		// Test root()
		T.root();
		if( T.item() != 1 ) System.out.println("Expected item at root to be 1, got " + T.item());
		
		T.goLeftChild();
		
		if( T.item() != 2 ) System.out.println("Expected current item to be 2, got " + T.item());
		
		T.goRightChild();
		if( T.item() != 5 ) System.out.println("Expected current item to be 5, got " + T.item());

		
		T.goLeftChild();
		if( T.item() != 10 ) System.out.println("Expected current item to be 10,  got " + T.item());
		
		// Current node now has no children.
		x = null;
		try {
			T.goLeftChild();
		}
		catch( InvalidState280Exception e ) {
			x = e;
		}
		finally {
			if( x == null) System.out.println("Expected exception moving to left child of a leaf.  Got none.");
		}
		
		x = null;
		try {
			T.goRightChild();
		}
		catch( InvalidState280Exception e ) {
			x = e;
		}
		finally {
			if( x == null) System.out.println("Expected exception moving to right child of a leaf.  Got none.");
		}
		
		// Remove the last item ( a leaf)
		T.deleteItem();
		if( T.item() != 9 ) System.out.println("Expected current item to be 9, got " + T.item());

		T.parent();
		
		
		
		// Remove a node with 2 children.  The right child 9 gets promoted.
		T.deleteItem();
		if( T.item() != 9 ) System.out.println("Expected current item to be 9, got " + T.item());
		
		
		// Remove a node with 1 child.  The left child 8 gets promoted.
		T.deleteItem();
		if( T.item() != 8 ) System.out.println("Expected current item to be 8, got " + T.item());
		
		// Remove the root successively.  There are 7 items left.
		T.root();
		T.deleteItem();
		if( T.item() != 7 ) System.out.println("Expected root to be 7, got " + T.item());

		T.deleteItem();
		if( T.item() != 6 ) System.out.println("Expected root to be 6, got " + T.item());

		T.deleteItem();
		if( T.item() != 5 ) System.out.println("Expected root to be 5, got " + T.item());
		
		T.deleteItem();
		if( T.item() != 8 ) System.out.println("Expected root to be 8, got " + T.item());

		T.deleteItem();
		if( T.item() != 3 ) System.out.println("Expected root to be 3, got " + T.item());

		T.deleteItem();
		if( T.item() != 2 ) System.out.println("Expected root to be 2, got " + T.item());

		// Tree has one item.  Try parent() on one item.
		x = null;
		try {
			T.parent();
		}
		catch( InvalidState280Exception e ) {
			x = e;
		}
		finally {
			if( x == null) System.out.println("Expected exception moving to parent of root.  Got none.");
		}
		
		
		// Try to go to the sibling
		x = null;
		try {
			T.goSibling();
		}
		catch(InvalidState280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception moving to sibling when at the root.  Got none.");
		}
		
		
		
		T.deleteItem(); 
		
		
		// Tree should now be empty
		if(!T.isEmpty()) System.out.println("Expected empty tree.  isEmpty() returned false.");

		if(T.capacity() != 10) System.out.println("Expected capacity to be 10, got "+ T.capacity());
		
		if(T.count() != 0  ) System.out.println("Expected tree count to be 0, got "+ T.count());
		
		// Remove from empty tree.
		x = null;
		try {
			T.deleteItem();
		}
		catch(NoCurrentItem280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception deleting from an empty tree.  Got none.");
		}
		
		
		
		// Try to go to the sibling
		x = null;
		try {
			T.goSibling();
		}
		catch(ContainerEmpty280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception moving to sibling in empty tree tree.  Got none.");
		}
		
		
		T.insert(1);
		T.root();
		
		// Try to go to the sibling when there is no child.
		x = null;
		try {
			T.goSibling();
		}
		catch(InvalidState280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception moving to sibling of node with no sibling.  Got none.");
		}
		
		T.goBefore();
		if(!T.before()) System.out.println("Error: Should be in 'before' position, but before() reports otherwise.");
		if(T.after()) System.out.println("Error: T.after() reports cursor in the after position when it should not be.");
		
		T.goForth();
		if(T.before()) System.out.println("Error: T.before() reports cursor in the before position when it should not be.");
		if(T.after()) System.out.println("Error: T.after() reports cursor in the after position when it should not be.");

		T.goForth();
		if(!T.after()) System.out.println("Error: Should be in 'after' position, but after() reports otherwise.");
		if(T.before()) System.out.println("Error: T.before() reports cursor in the before position when it should not be.");
		
		x=null;
		try {
			T.goForth();
		}
		catch(AfterTheEnd280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception advancing cursor when already after the end.  Got none.");
		}

		
		int y=-1;
		T.goBefore();
		try {
			y =  T.obtain(1); 
		}
		catch( ItemNotFound280Exception e ) {
			System.out.println("Error: Unexpected exception occured when attempting T.obtain(1).");
		}
		finally {
			if(y != 1 ) System.out.println("Obtained item should be 1 but it isn't.");
			if(!T.before()) System.out.println("Error: cursor should still be in the before() position after T.obtain(1), but it isn't.");
		}
		
		if(!T.has(1)) System.out.println("Error: Tree has element 1, but T.has(1) reports that it does not.");
		
		
		
		System.out.println("Regression test complete.");
	}

	@Override
	// return true if the current node is in the 0
	public boolean before() {
		if(currentNode==0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	// return true if the current node is after the count
	public boolean after() {
		if(currentNode>count)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	// move the cursor to the last location
	public void goForth() throws AfterTheEnd280Exception {
		// it is can not be in the after
		if(after())
		{
			throw new AfterTheEnd280Exception("Can not move forth when cursor is in after end");
		}
		// in other situation, move the current node to next
		else
		{
			currentNode = currentNode+1;
		}

	}

	@Override
	// move the cursor to the first element location
	public void goFirst() throws ContainerEmpty280Exception {
		// can move when the tree is empty
		if(isEmpty())
		{
			throw new ContainerEmpty280Exception("Can not go first when the tree is empty");
		}
		// current node should be in the 1st
		else
		{
			currentNode = 1;

		}

	}

	@Override
	// move the cursor to the before location
	public void goBefore() {
		// current node should be in the 0 location
		currentNode = 0;

	}

	@Override
	// move the cursor to the after location
	public void goAfter() {
		// current node should move to the after location
		currentNode = count+1;

	}
}
