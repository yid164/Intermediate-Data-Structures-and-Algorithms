package lib280.tree;


// Name: Yinsheng Dong
// Student Number: 11148648
// NSID: yid164
// Lecture Section: CMPT 280
import lib280.base.CursorPosition280;
import lib280.base.Keyed280;
import lib280.base.Pair280;
import lib280.dictionary.KeyedDict280;
import lib280.exception.AfterTheEnd280Exception;
import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.DuplicateItems280Exception;
import lib280.exception.InvalidArgument280Exception;
import lib280.exception.InvalidState280Exception;
import lib280.exception.NoCurrentItem280Exception;

public class IterableTwoThreeTree280<K extends Comparable<? super K>, I extends Keyed280<K>> extends TwoThreeTree280<K, I> implements KeyedDict280<K,I> {

	// References to the leaf nodes with the smallest and largest keys.
	LinkedLeafTwoThreeNode280<K,I> smallest, largest;
	
	// These next two variables represent the cursor which
	// the methods inherited from KeyedLinearIterator280 will
	// manipulate.  The cursor may only be positioned at leaf
	// nodes, never at internal nodes.
	
	// Reference to the leaf node at which the cursor is positioned.
	LinkedLeafTwoThreeNode280<K,I> cursor;
	
	// Reference to the predecessor of the node referred to by 'cursor' 
	// (or null if no such node exists).
	LinkedLeafTwoThreeNode280<K,I> prev;
	
	
	protected LinkedLeafTwoThreeNode280<K,I> createNewLeafNode(I newItem) {
		return new LinkedLeafTwoThreeNode280<K,I>(newItem);
	}


	@Override
	public void insert(I newItem) {

		if( this.has(newItem.key()) ) 
			throw new DuplicateItems280Exception("Key already exists in the tree.");

		// If the tree is empty, just make a leaf node. 
		if( this.isEmpty() ) {
			this.rootNode = createNewLeafNode(newItem);
			// Set the smallest and largest nodes to be the one leaf node in the tree.
			this.smallest = (LinkedLeafTwoThreeNode280<K, I>) this.rootNode;
			this.largest = (LinkedLeafTwoThreeNode280<K, I>) this.rootNode;
		}
		// If the tree has one node, make an internal node, and make it the parent
		// of both the existing leaf node and the new leaf node.
		else if( !this.rootNode.isInternal() ) {
			LinkedLeafTwoThreeNode280<K,I> newLeaf = createNewLeafNode(newItem);
			LinkedLeafTwoThreeNode280<K,I> oldRoot = (LinkedLeafTwoThreeNode280<K,I>)rootNode;
			InternalTwoThreeNode280<K,I> newRoot;
			if( newItem.key().compareTo(oldRoot.getKey1()) < 0) {
				// New item's key is smaller than the existing item's key...
				newRoot = createNewInternalNode(newLeaf, oldRoot.getKey1(), oldRoot, null, null);	
				newLeaf.setNext(oldRoot);
				oldRoot.setPrev(newLeaf);
				
				// There was one leaf node, now there's two.  Update smallest and largest nodes.
				this.smallest = newLeaf;
				this.largest = oldRoot;
			}
			else {
				// New item's key is larger than the existing item's key. 
				newRoot = createNewInternalNode(oldRoot, newItem.key(), newLeaf, null, null);
				oldRoot.setNext(newLeaf);
				newLeaf.setPrev(oldRoot);
				
				// There was one leaf node, now there's two.  Update smallest and largest nodes.
				this.smallest = oldRoot;
				this.largest = newLeaf;
			}
			this.rootNode = newRoot;
		}
		else {
			Pair280<TwoThreeNode280<K,I>, K> extra = this.insert((InternalTwoThreeNode280<K,I>)this.rootNode, newItem);

			// If extra returns non-null, then the root was split and we need
			// to make a new root.
			if( extra != null ) {
				InternalTwoThreeNode280<K,I> oldRoot = (InternalTwoThreeNode280<K,I>)rootNode;

				// extra always contains larger keys than its sibling.
				this.rootNode = createNewInternalNode(oldRoot, extra.secondItem(), extra.firstItem(), null, null);				
			}
		}
	}


	/**
	 * Recursive helper for the public insert() method.
	 * @param root Root of the (sub)tree into which we are inserting.
	 * @param newItem The item to be inserted.
	 */
	protected Pair280<TwoThreeNode280<K,I>, K> insert( TwoThreeNode280<K,I> root, 
			I newItem) {

		if( !root.isInternal() ) {
			// If root is a leaf node, then it's time to create a new
			// leaf node for our new element and return it so it gets linked
			// into root's parent.
			Pair280<TwoThreeNode280<K,I>, K> extraNode;
			LinkedLeafTwoThreeNode280<K,I> oldLeaf = (LinkedLeafTwoThreeNode280<K, I>) root;

			// If the new element is smaller than root, copy root's element to
			// a new leaf node, put new element in existing leaf node, and
			// return new leaf node.
			if( newItem.key().compareTo(root.getKey1()) < 0) {
				extraNode = new Pair280<TwoThreeNode280<K,I>, K>(createNewLeafNode(root.getData()), root.getKey1());
				((LeafTwoThreeNode280<K,I>)root).setData(newItem);
			}
			else {
				// Otherwise, just put the new element in a new leaf node
				// and return it.
				extraNode = new Pair280<TwoThreeNode280<K,I>, K>(createNewLeafNode(newItem), newItem.key());
			}
			
			LinkedLeafTwoThreeNode280<K,I> newLeaf= (LinkedLeafTwoThreeNode280<K, I>) extraNode.firstItem();
		
			// No matter what happens above, the node 'newLeaf' is a new leaf node that is 
			// immediately to the right of the node 'oldLeaf'.
			
			// TODO Link newLeaf to its proper successor/predecessor nodes and
			// adjust links of successor/predecessor nodes accordingly.
			
			// Also adjust this.largest if necessary.
			
			// (this.smallest will never need adjustment because if a new
			//  smallest element is inserted, it gets put in the existing 
			//  leaf node, and the old smallest element is copied to a  
			//  new node -- this is "true" case for the previous if/else.)

			if(oldLeaf.next() == null)
			{
				this.largest = newLeaf;
				oldLeaf.setNext(newLeaf);
				newLeaf.setPrev(oldLeaf);
				newLeaf.setNext(null);
			}
			else
			{
				LinkedLeafTwoThreeNode280<K,I> temp = oldLeaf.next();
				oldLeaf.setNext(newLeaf);
				newLeaf.setPrev(oldLeaf);
				newLeaf.setNext(temp);
				temp.setPrev(newLeaf);
			}
			
		
			return extraNode;
		}
		else { // Otherwise, recurse! 
			Pair280<TwoThreeNode280<K,I>, K> extra;
			TwoThreeNode280<K,I> insertSubtree;

			if( newItem.key().compareTo(root.getKey1()) < 0 ) {
				// decide to recurse left
				insertSubtree = root.getLeftSubtree();
			}
			else if(!root.isRightChild() || newItem.key().compareTo(root.getKey2()) < 0 ) {
				// decide to recurse middle
				insertSubtree = root.getMiddleSubtree();
			}
			else {
				// decide to recurse right
				insertSubtree = root.getRightSubtree();
			}

			// Actually recurse where we decided to go.
			extra = insert(insertSubtree, newItem);

			// If recursion resulted in a new node needs to be linked in as a child
			// of root ...
			if( extra != null ) {
				// Otherwise, extra.firstItem() is an internal node... 
				if( !root.isRightChild() ) {
					// if root has only two children.  
					if( insertSubtree == root.getLeftSubtree() ) {
						// if we inserted in the left subtree...
						root.setRightSubtree(root.getMiddleSubtree());
						root.setMiddleSubtree(extra.firstItem());
						root.setKey2(root.getKey1());
						root.setKey1(extra.secondItem());
						return null;
					}
					else {
						// if we inserted in the right subtree...
						root.setRightSubtree(extra.firstItem());
						root.setKey2(extra.secondItem());
						return null;
					}
				}
				else {
					// otherwise root has three children
					TwoThreeNode280<K, I> extraNode;
					if( insertSubtree == root.getLeftSubtree()) {
						// if we inserted in the left subtree
						extraNode = createNewInternalNode(root.getMiddleSubtree(), root.getKey2(), root.getRightSubtree(), null, null);
						root.setMiddleSubtree(extra.firstItem());
						root.setRightSubtree(null);
						K k1 = root.getKey1();
						root.setKey1(extra.secondItem());
						return new Pair280<TwoThreeNode280<K,I>, K>(extraNode, k1);
					}
					else if( insertSubtree == root.getMiddleSubtree()) {
						// if we inserted in the middle subtree
						extraNode = createNewInternalNode(extra.firstItem(), root.getKey2(), root.getRightSubtree(), null, null);
						root.setKey2(null);
						root.setRightSubtree(null);
						return new Pair280<TwoThreeNode280<K,I>, K>(extraNode, extra.secondItem());
					}
					else {
						// we inserted in the right subtree
						extraNode = createNewInternalNode(root.getRightSubtree(), extra.secondItem(), extra.firstItem(), null, null);
						K k2 = root.getKey2();
						root.setKey2(null);
						root.setRightSubtree(null);
						return new Pair280<TwoThreeNode280<K,I>, K>(extraNode, k2);
					}
				}
			}
			// Otherwise no new node was returned, so there is nothing extra to link in.
			else return null;
		}		
	}


	@Override
	public void delete(K keyToDelete) {
		if( this.isEmpty() ) return;

		if( !this.rootNode.isInternal()) {
			if( this.rootNode.getKey1() == keyToDelete ) {
				this.rootNode = null;
				this.smallest = null;
				this.largest = null;
			}
		}
		else {
			delete(this.rootNode, keyToDelete);	
			// If the root only has one child, replace the root with its
			// child.
			if( this.rootNode.getMiddleSubtree() == null) {
				this.rootNode = this.rootNode.getLeftSubtree();
				if( !this.rootNode.isInternal() ) {
					this.smallest = (LinkedLeafTwoThreeNode280<K, I>) this.rootNode;
					this.largest = (LinkedLeafTwoThreeNode280<K, I>) this.rootNode;
				}
			}
		}
	}


	/**
	 * Given a key, delete the corresponding key-item pair from the tree.
	 * @param root root of the current tree
	 * @param keyToDelete The key to be deleted, if it exists.
	 */
	protected void delete(TwoThreeNode280<K, I> root, K keyToDelete ) {
		if( root.getLeftSubtree().isInternal() ) {
			// root is internal, so recurse.
			TwoThreeNode280<K,I> deletionSubtree;
			if( keyToDelete.compareTo(root.getKey1()) < 0){
				// recurse left
				deletionSubtree = root.getLeftSubtree();
			}
			else if( root.getRightSubtree() == null || keyToDelete.compareTo(root.getKey2()) < 0 ){
				// recurse middle
				deletionSubtree = root.getMiddleSubtree();
			}
			else {
				// recurse right
				deletionSubtree = root.getRightSubtree();
			}

			delete(deletionSubtree, keyToDelete);

			// Do the first possible of:
			// steal left, steal right, merge left, merge right
			if( deletionSubtree.getMiddleSubtree() == null)  
				if(!stealLeft(root, deletionSubtree))
					if(!stealRight(root, deletionSubtree))
						if(!giveLeft(root, deletionSubtree))
							if(!giveRight(root, deletionSubtree))
								throw new InvalidState280Exception("This should never happen!");

		}
		else {
			// children of root are leaf nodes
			if( root.getLeftSubtree().getKey1().compareTo(keyToDelete) == 0 ) {
				// leaf to delete is on left

				// TODO Unlink leaf from it's linear successor/predecessor
				// Hint: Be prepared to typecast where appropriate.
				if(((LinkedLeafTwoThreeNode280<K,I>) root.getLeftSubtree()).prev() == null)
				{
					LinkedLeafTwoThreeNode280<K,I> small = ((LinkedLeafTwoThreeNode280<K,I>) root.getLeftSubtree()).next();
					small.setPrev(null);
					this.smallest = small;
				}
				else
				{
					LinkedLeafTwoThreeNode280<K,I> small = ((LinkedLeafTwoThreeNode280<K,I>) root.getLeftSubtree()).prev();
					small.setNext(((LinkedLeafTwoThreeNode280<K, I>) root.getLeftSubtree()).next());
					((LinkedLeafTwoThreeNode280<K, I>) root.getLeftSubtree()).next().setPrev(small);
				}
				((LinkedLeafTwoThreeNode280<K, I>) root.getLeftSubtree()).setNext(null);
				((LinkedLeafTwoThreeNode280<K, I>) root.getLeftSubtree()).setPrev(null);

				
				
				
				// Proceed with deletion of leaf from the 2-3 tree.
				root.setLeftSubtree(root.getMiddleSubtree());
				root.setMiddleSubtree(root.getRightSubtree());
				if(root.getMiddleSubtree() == null)
					root.setKey1(null);
				else 
					root.setKey1(root.getKey2());
				if( root.getRightSubtree() != null) root.setKey2(null);
				root.setRightSubtree(null);					
			}
			else if( root.getMiddleSubtree().getKey1().compareTo(keyToDelete) == 0 ) {
				// leaf to delete is in middle

				// TODO Unlink leaf from it's linear successor/predecessor
				// Hint: Be prepared to typecast where appropriate.
				if(((LinkedLeafTwoThreeNode280<K,I>) root.getMiddleSubtree()).next()!=null)
				{
					LinkedLeafTwoThreeNode280<K,I> temp = ((LinkedLeafTwoThreeNode280<K, I>) root.getMiddleSubtree()).prev();
					temp.setNext(((LinkedLeafTwoThreeNode280<K, I>) root.getMiddleSubtree()).next());
					((LinkedLeafTwoThreeNode280<K, I>) root.getMiddleSubtree()).next().setPrev(temp);
				}
				else
				{
					((LinkedLeafTwoThreeNode280<K, I>) root.getMiddleSubtree()).prev().setNext(null);
					this.largest = ((LinkedLeafTwoThreeNode280<K, I>) root.getMiddleSubtree()).prev();
				}

				((LinkedLeafTwoThreeNode280<K, I>) root.getMiddleSubtree()).setNext(null);
				((LinkedLeafTwoThreeNode280<K, I>) root.getMiddleSubtree()).setPrev(null);
		
				
				
				
				// Proceed with deletion from the 2-3 tree.
				root.setMiddleSubtree(root.getRightSubtree());				
				if(root.getMiddleSubtree() == null)
					root.setKey1(null);
				else 
					root.setKey1(root.getKey2());

				if( root.getRightSubtree() != null) {
					root.setKey2(null);
					root.setRightSubtree(null);
				}
			}
			else if( root.getRightSubtree() != null && root.getRightSubtree().getKey1().compareTo(keyToDelete) == 0 ) {
				// leaf to delete is on the right

				// TODO Unlink leaf from it's linear successor/predecessor
				// Hint: Be prepared to typecast where appropriate.

				if(((LinkedLeafTwoThreeNode280<K,I>)root.getRightSubtree()).next()!=null)
				{
					LinkedLeafTwoThreeNode280<K,I> temp = ((LinkedLeafTwoThreeNode280<K, I>) root.getRightSubtree()).prev();
					temp.setNext(((LinkedLeafTwoThreeNode280<K, I>) root.getRightSubtree()).next());
					((LinkedLeafTwoThreeNode280<K, I>) root.getRightSubtree()).next().setPrev(temp);
				}
				else
				{
					((LinkedLeafTwoThreeNode280<K, I>) root.getRightSubtree()).next().setNext(null);
					this.largest = ((LinkedLeafTwoThreeNode280<K, I>) root.getRightSubtree()).prev();
				}
				((LinkedLeafTwoThreeNode280<K, I>) root.getRightSubtree()).setNext(null);
				((LinkedLeafTwoThreeNode280<K, I>) root.getRightSubtree()).setPrev(null);

				
				
				
				// Proceed with deletion of the node from the 2-3 tree.
				root.setKey2(null);
				root.setRightSubtree(null);
			}
			else {
				// key to delete does not exist in tree.
			}
		}		
	}	
	
	
	@Override
	public K itemKey() throws NoCurrentItem280Exception {
		// TODO Return the key of the item in the node
		// on which the cursor is positioned.
		
		// This is just a placeholder to avoid compile errors. Remove it when ready.
		if(itemExists())
		{
			return this.cursor.getKey1();
		}
		else
		{
			throw new NoCurrentItem280Exception("No current item in the current position");
		}
	}


	@Override
	public Pair280<K, I> keyItemPair() throws NoCurrentItem280Exception {
		// Return a pair consisting of the key of the item
		// at which the cursor is positioned, and the entire
		// item in the node at which the cursor is positioned.
		if( !itemExists() ) 
			throw new NoCurrentItem280Exception("There is no current item from which to obtain its key.");
		return new Pair280<K, I>(this.itemKey(), this.item());
	}


	@Override
	public I item() throws NoCurrentItem280Exception {
		// TODO Return the item in the node at which the cursor is
		// positioned.

		// This is just a placeholder to avoid compile errors. Remove it when ready.
		if(itemExists())
		{
			return this.cursor.getData();
		}
		else
		{
			throw new NoCurrentItem280Exception("No current item in the current position");
		}
	}


	@Override
	public boolean itemExists() {
		return this.cursor != null;
	}


	@Override
	public boolean before() {
		return this.cursor == null && this.prev == null;
	}


	@Override
	public boolean after() {
		return this.cursor == null && this.prev != null;
	}


	@Override
	public void goForth() throws AfterTheEnd280Exception {
		if( this.after() ) throw new AfterTheEnd280Exception("Cannot advance the cursor past the end.");
		if( this.before() ) this.goFirst();
		else {
			this.prev = this.cursor;
			this.cursor = this.cursor.next();
		}
	}


	@Override
	public void goFirst() throws ContainerEmpty280Exception {
		this.prev = null;
		this.cursor = this.smallest;
	}


	@Override
	public void goBefore() {
		this.prev = null;
		this.cursor = null;
	}


	@Override
	public void goAfter() {
		this.prev = this.largest;
		this.cursor = null;
	}


	@Override
	public CursorPosition280 currentPosition() {
		return new TwoThreeTreePosition280<K,I>(this.cursor, this.prev);
	}


	@SuppressWarnings("unchecked")
	@Override
	public void goPosition(CursorPosition280 c) {
		if(c instanceof TwoThreeTreePosition280 ) {
			this.cursor = ((TwoThreeTreePosition280<K,I>) c).cursor;
			this.prev = ((TwoThreeTreePosition280<K,I>) c).prev;		
		}
		else {
			throw new InvalidArgument280Exception("The provided position was not a TwoThreeTreePosition280 object.");
		}
	}


	public void search(K k) {
		// TODO Position the cursor at the item with key k
		// (if such an item exists).
		
		// Don't use the cursor for this so the search 
		// can be sub-linear time. Use the inherited protected
		// find() method to locate the node containing k if
		// it exists, then adjust the cursor variables to 
		// refer to it.  If no item with key k can be found
		// leave the cursor in the after position.

		if(find(rootNode,k)==null)
		{
			goAfter();
		}
		else
		{
			cursor = (LinkedLeafTwoThreeNode280<K,I>) find(rootNode,k);
			prev = cursor.prev();
		}
		

	}


	@Override
	public void searchCeilingOf(K k) {
		// Position the cursor at the smallest item that
		// has key at least as large as 'k', if such an
		// item exists.  If no such item exists, leave 
		// the cursor in the after position.
		
		// This one is easier to do with a linear search.
		// Could make it potentially faster but the solution is
		// not obvious -- just use linear search via the cursor.
		
		// If it's empty, do nothing; itemExists() will be false.
		if( this.isEmpty() ) 
			return;
		
		// Find first item item >= k.  If there is no such item,
		// cursor will end up in after position, and that's fine
		// since itemExists() will be false.
		this.goFirst();
		while(this.itemExists() && this.itemKey().compareTo(k) < 0) {
			this.goForth();
		}
		
	}

	@Override
	public void setItem(I x) throws NoCurrentItem280Exception,
			InvalidArgument280Exception {
		// TODO Store the item x in the node at which the cursor is
		// positioned.  If the item at which the cursor is 
		// positioned does not have the same key as x, throw
		// an appropriate exception.
		if(!itemExists())
		{
			throw new NoCurrentItem280Exception("There is not current item exists");
		}
		if(x.key().compareTo(this.cursor.getKey1())!=0)
		{
			throw new InvalidArgument280Exception("invalidArgument");
		}
		this.cursor.setData(x);

	}


	@Override
	public void deleteItem() throws NoCurrentItem280Exception {
		// TODO Remove the item at which the cursor is positioned from the tree.
		// Leave the cursor on the successor of the deleted item.
	
		// Hint: If this takes more than 5 or 6 lines, you're doing it wrong!

		if(!itemExists())
		{
			throw new NoCurrentItem280Exception("There is not current item to delete");
		}

		K deleteItem = this.cursor.getKey1();
		delete(deleteItem);
	}


	
	
	
    @Override
    public String toStringByLevel() {
        String s = super.toStringByLevel();
        
        s += "\nThe Linear Ordering is: ";
        CursorPosition280 savedPos = this.currentPosition();
        this.goFirst();
        while(this.itemExists()) {
            s += this.itemKey() + ", ";
            this.goForth();
        }
        this.goPosition(savedPos);
        
        if( smallest != null)
            s += "\nSmallest: " + this.smallest.getKey1();
        if( largest != null ) {
            s += "\nLargest: " + this.largest.getKey1();
        }
        return s;
    }

	public static void main(String args[]) {

		// A class for an item that is compatible with our
		// 2-3 Tree class.  It has to implement Keyed280
		// as required by the class header of the 2-3 tree.
		// Keyed280 just requires that the item have a method
		// called key() that returns its key.

		// You must test your tree using Loot objects.

		class Loot implements Keyed280<String> {
			protected int goldValue;
			protected String key;

			@Override
			public String key() {
				return key;
			}

			@SuppressWarnings("unused")
			public int itemValue() {
				return this.goldValue;
			}

			Loot(String key, int i) {
				this.goldValue = i;
				this.key = key;
			}

		}

		// Create a tree to test with. 
		IterableTwoThreeTree280<String, Loot> T =
				new IterableTwoThreeTree280<String, Loot>();

		// An example of instantiating an item. (you can remove this if you wish)
		Loot sampleItem = new Loot("Magic Armor", 1000);

		// Insert your first item! (you can remove this if you wish)
		T.insert(sampleItem);

		// TODO Write your regression test here

		// Testing itemExists() method
		T.goFirst();
		if (T.itemExists())
		{
			System.out.println("The itemExists() method passed");
		}
		else
		{
			System.out.println("The itemExists() method failed.");
		}

		// Testing item() method
		if (T.item().equals(sampleItem)) {
			System.out.println("The item() method passed.");
		}
		else
		{
			System.out.println("The item() method failed");
		}

		// Testing itemKey() method
		if(T.itemKey().equals("Magic Armor"))
		{
			System.out.println("The itemKey() method passed");
		}
		else
		{
			System.out.println("The itemKey() method failed");
		}

		// Testing keyItemPair()
		Pair280<String, Loot> keyPair = new Pair280<String, Loot>("Magic Armor", sampleItem);
		if(T.keyItemPair().toString().equals(keyPair.toString()))
		{
			System.out.println("The keyItemPair() method passed");
		}
		else
		{
			System.out.println("The keyItemPair() method failed");
		}

		// Testing search() method
		// 'G' before the 'M'
		Loot sampleItem1 = new Loot("GG", 5000);
		T.insert(sampleItem1);
		T.search("GG");
		if(T.item().equals(sampleItem1))
		{
			System.out.println("The search() method passed");
		}
		else
		{
			System.out.println("The search() method failed");
		}

		// Testing searchCeilingOf() method
		Loot sampleItem2 = new Loot("Dong", 8000);
		// because the sampleItem2 is not inserted in the T, so the cursor should be in the sampleItem1
		// 'D' is before the 'G'
		T.searchCeilingOf(sampleItem2.key());
		if(T.item().equals(sampleItem1))
		{
			System.out.println("The searchCeilingOf() method passed ");
		}
		else
		{
			System.out.println("The searchCeilingOf() method failed");
		}

		// Testing setItem() method
		Loot sampleItem3 = new Loot("GG",3000);
		T.search(sampleItem1.key());
		try {

			T.setItem(sampleItem2);
		}
		catch (InvalidArgument280Exception e)
		{
			T.search(sampleItem1.key());
			T.setItem(sampleItem3);
			if(T.item().equals(sampleItem3))
			{
				System.out.println("The setItem() method passed");
			}
			else
			{
				System.out.println("The setItem() method failed");
			}
		}

		// Testing deleteItem() method
		// Delete the root item, so the first item should be the sampleItem3
		T.search(sampleItem.key());
		T.deleteItem();
		T.goFirst();
		if(T.item().equals(sampleItem3))
		{
			System.out.println("The deleteItem() method passed");
		}
		else
		{
			System.out.println("The deleteItem() method failed");
		}

		// Testing the goAfter() method
		T.goAfter();
		try {
			T.item();
		}
		catch (NoCurrentItem280Exception e)
		{
			if(T.after())
			{
				System.out.println("The goAfter() method passed");
			}
			else
			{
				System.out.println("The goAfter() method failed");
			}
		}

		// Testing the goBefore() method
		T.goBefore();
		try {
			T.item();
		}
		catch (NoCurrentItem280Exception e)
		{
			if(T.before())
			{
				System.out.println("The goBefore() method passed");
			}
			else
			{
				System.out.println("The goBefore() method failed");
			}
		}

		// Testing the goForth() method
		T.insert(sampleItem);
		T.goFirst();
		T.goForth();
		if(T.item().equals(sampleItem))
		{
			System.out.println("The goForth() method passed");
		}
		else
		{
			System.out.println("The goForth() method failed");
		}

		// Testing currentPosition() method
		T.search(sampleItem.key());
		TwoThreeTreePosition280<String, Loot> position280 = (TwoThreeTreePosition280<String, Loot>) T.currentPosition();
		if(position280.cursor.getData().equals(sampleItem)&&position280.prev.getData().equals(sampleItem3))
		{
			System.out.println("The currentPosition() method passed");
		}
		else
		{
			System.out.println("The currentPosition() method failed");
		}

		// Testing goPosition() method
		LinkedLeafTwoThreeNode280<String, Loot> cp = new LinkedLeafTwoThreeNode280<String, Loot>(sampleItem);
		CursorPosition280 position2801 = new TwoThreeTreePosition280<>(cp, null);
		T.goPosition(position2801);
		if(T.item().equals(sampleItem))
		{
			System.out.println("The goPosition() method passed");
		}
		else
		{
			System.out.println("The goPosition() method failed");
		}

		System.out.println("The regression test passed");





	}


	
}
