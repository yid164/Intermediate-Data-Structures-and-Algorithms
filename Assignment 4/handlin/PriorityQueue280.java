package lib280.dispenser;

// Name: Yinsheng Dong
// Student Number: 11148648
// NSID: yid164
// Lecture Section: CMPT 280
import lib280.exception.ContainerFull280Exception;
import lib280.tree.ArrayedBinaryTreeIterator280;
import lib280.tree.IterableArrayedHeap280;

public class PriorityQueue280<I extends Comparable<? super I>> {

	// This is the heap that we are restricting.
	// Items in the priority queue get stored in the heap.
	protected IterableArrayedHeap280<I> items;


	/**
	 * Create a new priorty queue with a given capacity.
	 * @param cap The maximum number of items that can be in the queue.
	 */
	public PriorityQueue280(int cap) {
		items = new IterableArrayedHeap280<I>(cap);
	}

	public String toString() {
		return items.toString();
	}

	// TODO
	// Add Priority Queue ADT methods (from the specification) here.

	/**
	 * the insertion of the priority queue
	 * @param a
	 */
	public void insert(I a)
	{
		this.items.insert(a);
	}

	/**
	 * Determine the queue is empty or not
	 * @return true if the queue is empty, false otherwise
	 */
	public boolean isEmpty()
	{
		if(this.items.isEmpty())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Determine the queue is full or not
	 * @return true if the queue is full, false otherwise
	 */
	public boolean isFull()
	{
		if(this.items.isFull())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * to get the max item in the queue
	 * @return the max item
	 * the max item should be in the root, so iterator should go first
	 */
	public I maxItem()
	{
		ArrayedBinaryTreeIterator280<I> itr = this.items.iterator();
		itr.goFirst();
		return itr.item();
	}

	/**
	 * to get the min item in the queue
	 * @return the min item
	 */
	public I minItem()
	{
		// create a new iterator for this
		ArrayedBinaryTreeIterator280<I> itr = this.items.iterator();
		// make a half of items count
		int half = (1/2) * this.items.count();
		// let itr go before position, and crate a min variable to read it, get the min variable
		itr.goBefore();
		for (int i = 0; i <= half; i++)
		{
			itr.goForth();
		}
		I min = itr.item();
		int otherHalf = 0;
		// use a for-loops to find out other half of tree to get the min variable
		for (int i = 1; i < this.items.count() - half; i++) {
			itr.goForth();
			if (itr.item().compareTo(min) < 0){
				otherHalf = i;
				min = itr.item();
			}
		}

		// then get the total of the tree
		int total = half + otherHalf + 1;
		itr.goFirst();
		// to read the min variable
		for (int i = 1; i < total; i++) {
			if (itr.item().compareTo(min) == 0){
				min = itr.item();
				break;
			}
			itr.goForth();
		}
		return min;


	}

	/**
	 *
	 * @return the how many variable that this have
	 */
	public int count()
	{
		return this.items.count();
	}

	/**
	 * delete the min variable
	 */
	public void deleteMin()
	{
		// create a new iterator for this, and call the minItem() to get the min variable
		ArrayedBinaryTreeIterator280<I> itr = this.items.iterator();
		I min = minItem();
		// move the cursor to the min, and delete it
		itr.item().equals(min);
		items.deleteAtPosition(itr);
	}

	/**
	 * delete the max item in the queue, the max item is in the first node
	 */
	public void deleteMax()
	{
		this.items.deleteItem();
	}

	/**
	 * delete all max item in the queue
	 */
	public void deleteAllMax()
	{
		// use the I max to find all max item in the queue
		I max = this.maxItem();
		while (!this.items.isEmpty() && this.maxItem().compareTo(max) == 0) {
			// delete them
			this.deleteMax();
		}
	}

	/* UNCOMMENT THE REGRESSION TEST WHEN YOU ARE READY
/*/
	public static void main(String args[]) {
		class PriorityItem<I> implements Comparable<PriorityItem<I>> {
			I item;
			Double priority;

			public PriorityItem(I item, Double priority) {
				super();
				this.item = item;
				this.priority = priority;
			}

			public int compareTo(PriorityItem<I> o) {
				return this.priority.compareTo(o.priority);
			}

			public String toString() {
				return this.item + ":" + this.priority;
			}
		}

		PriorityQueue280<PriorityItem<String>> Q = new PriorityQueue280<PriorityItem<String>>(5);

		// Test isEmpty()
		if( !Q.isEmpty())
			System.out.println("Error: Queue is empty, but isEmpty() says it isn't.");

		// Test insert() and maxItem()
		Q.insert(new PriorityItem<String>("Sing", 5.0));
		if( Q.maxItem().item.compareTo("Sing") != 0) {
			System.out.println("??Error: Front of queue should be 'Sing' but it's not. It is: " + Q.maxItem().item);
		}

		// Test isEmpty() when queue not empty
		if( Q.isEmpty())
			System.out.println("Error: Queue is not empty, but isEmpty() says it is.");

		// test count()
		if( Q.count() != 1 ) {
			System.out.println("Error: Count should be 1 but it's not.");
		}

		// test minItem() with one element
		if( Q.minItem().item.compareTo("Sing")!=0) {
			System.out.println("Error: min priority item should be 'Sing' but it's not.");
		}

		// insert more items
		Q.insert(new PriorityItem<String>("Fly", 5.0));
		if( Q.maxItem().item.compareTo("Sing")!=0) System.out.println("Front of queue should be 'Sing' but it's not.");
		Q.insert(new PriorityItem<String>("Dance", 3.0));
		if( Q.maxItem().item.compareTo("Sing")!=0) System.out.println("Front of queue should be 'Sing' but it's not.");
		Q.insert(new PriorityItem<String>("Jump", 7.0));
		if( Q.maxItem().item.compareTo("Jump")!=0) System.out.println("Front of queue should be 'Jump' but it's not.");

		if(Q.minItem().item.compareTo("Dance") != 0) System.out.println("minItem() should be 'Dance' but it's not.");

		if( Q.count() != 4 ) {
			System.out.println("Error: Count should be 4 but it's not.");
		}

		// Test isFull() when not full
		if( Q.isFull())
			System.out.println("Error: Queue is not full, but isFull() says it is.");

		Q.insert(new PriorityItem<String>("Eat", 10.0));
		if( Q.maxItem().item.compareTo("Eat")!=0) System.out.println("Front of queue should be 'Eat' but it's not.");

		if( !Q.isFull())
			System.out.println("Error: Queue is full, but isFull() says it isn't.");

		// Test insertion on full queue
		try {
			Q.insert(new PriorityItem<String>("Sleep", 15.0));
			System.out.println("Expected ContainerFull280Exception inserting to full queue but got none.");
		}
		catch(ContainerFull280Exception e) {
			// Expected exception
		}
		catch(Exception e) {
			System.out.println("Expected ContainerFull280Exception inserting to full queue but got a different exception.");
			e.printStackTrace();
		}

		// test deleteMin
		Q.deleteMin();
		if(Q.minItem().item.compareTo("Sing") != 0) System.out.println("Min item should be 'Sing', but it isn't.");

		Q.insert(new PriorityItem<String>("Dig", 1.0));
		if(Q.minItem().item.compareTo("Dig") != 0) System.out.println("minItem() should be 'Dig' but it's not.");

		// Test deleteMax
		Q.deleteMax();
		if( Q.maxItem().item.compareTo("Jump")!=0) System.out.println("Front of queue should be 'Jump' but it's not.");

		Q.deleteMax();
		if( Q.maxItem().item.compareTo("Fly")!=0) System.out.println("Front of queue should be 'Fly' but it's not.");

		if(Q.minItem().item.compareTo("Dig") != 0) System.out.println("minItem() should be 'Dig' but it's not.");

		Q.deleteMin();
		if( Q.maxItem().item.compareTo("Fly")!=0) System.out.println("Front of queue should be 'Fly' but it's not.");

		Q.insert(new PriorityItem<String>("Scream", 2.0));
		Q.insert(new PriorityItem<String>("Run", 2.0));

		if( Q.maxItem().item.compareTo("Fly")!=0) System.out.println("Front of queue should be 'Fly' but it's not.");

		// test deleteAllMax()
		Q.deleteAllMax();
		if( Q.maxItem().item.compareTo("Scream")!=0) System.out.println("Front of queue should be 'Scream' but it's not.");
		if( Q.minItem().item.compareTo("Scream") != 0) System.out.println("minItem() should be 'Scream' but it's not.");
		Q.deleteAllMax();

		// Queue should now be empty again.
		if( !Q.isEmpty())
			System.out.println("Error: Queue is empty, but isEmpty() says it isn't.");

		System.out.println("Regression test complete.");
	}
}
