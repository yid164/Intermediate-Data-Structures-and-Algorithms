package lib280.tree;


// Name: Yinsheng Dong
// Student Number: 11148648
// NSID: yid164
// Lecture Section: CMPT 280
import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.NoCurrentItem280Exception;

public class IterableArrayedHeap280<I extends Comparable<? super I>> extends ArrayedHeap280<I>  {

	/**
	 * Create an iterable heap with a given capacity.
	 * @param cap The maximum number of elements that can be in the heap.
	 */
	public IterableArrayedHeap280(int cap) {
		super(cap);
	}

	// TODO
	// Add iterator() and deleteAtPosition() methods here.

	/**
	 * create a new iterator
	 * @return the new iterator of heap
	 */
	public ArrayedBinaryTreeIterator280<I> iterator()
	{
		return new ArrayedBinaryTreeIterator280<I>(this);
	}


	/**
	 * delete the current position's item
	 * @param iterator
	 * @throws NoCurrentItem280Exception
	 * @throws ContainerEmpty280Exception
	 */
	public void deleteAtPosition(ArrayedBinaryTreeIterator280<I> iterator) throws NoCurrentItem280Exception, ContainerEmpty280Exception
	{
		// if the current node is greater than count, the position should be in the after position
		if(iterator.currentNode > this.count)
		{
			throw new NoCurrentItem280Exception("Can not delete the item that does not exists");
		}
		// if the current count is 0, the container should be empty
		if(this.count==0)
		{
			throw new ContainerEmpty280Exception("Can not delete the item that in a empty container");
		}
		// so make the current item be the last position, and delete it
		this.items[currentNode] = this.items[count];
		this.count--;

		// if delete the last item, and the container becomes empty
		if (this.count == 0) {
			this.currentNode = 0;
			return;
		}
		// while the tree has a left node
		while(findLeftChild(currentNode)<=this.count)
		{
			// move the cursor to the left
			int child = findLeftChild(currentNode);

			// if the right node is greater than left, move the cursor to the right
			if(child+1 <= count && items[child].compareTo(items[child+1]) <0)
			{
				child = child + 1;
			}

			// if the parent node is smaller than the child node, swap them
			if(items[currentNode].compareTo(items[child])<0)
			{
				I temp = items[currentNode];
				items[currentNode] = items[child];
				items[child] = temp;
				currentNode = child;
			}
			else
			{
				return;
			}
		}
	}
	
}
