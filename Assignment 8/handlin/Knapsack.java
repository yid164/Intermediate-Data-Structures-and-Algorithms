// Name: Yinsheng Dong
// Student Number: 11148648
// NISD: yid164
// Lecture Section: CMPT 280

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * A non-public class that stores an item's value and weight
 */
class Item implements Comparable<Item> {

	protected Double value;
	protected Double weight;

	Item(Double v, Double w) {
		value = v;
		weight = w;
	}

	@Override
	public int compareTo(Item o) {
		return this.value.compareTo(o.value);
	}

	/**
	 * @return the value
	 */
	public Double value() {
		return value;
	}

	/**
	 * @return the weight
	 */
	public Double weight() {
		return weight;
	}

}

/**
 * A non-public class that stores an instance of Knapsack.
 */
class KnapsackInstance {
	/**
	 * The number of items in the problem instance.
	 */
	protected Integer numItems;

	/**
	 * The items to be considered.
	 */
	Item items[];

	/**
	 * The capacity of the knapsack in the problem instance.
	 */
	protected Double W;

	/**
	 * Initialize a knapsack instance.
	 * @param numItems Number of items in the problem instance
	 * @param W Capacity of the backpack.
	 */
	KnapsackInstance(Integer numItems, Double W) {
		this.numItems = numItems;
		this.W = W;
		this.items = new Item[this.numItems];
	}

	/**
	 * @return The number of items in the problem instance.
	 */
	public Integer numItems() { return this.numItems; }

	/**
	 * Set the value and weight of the id-th item.
	 */
	public void setItem(Double value, Double weight, Integer id) {
		this.items[id] = new Item(value, weight);
	}

	/**
	 * Obtain an item's value
	 */
	public Double value(int i) { return this.items[i].value(); }

	/**
	 * Obtain and item's weight
	 */
	public Double weight(int i) { return this.items[i].weight(); }

	/**
	 * Obtain the knapsack's capacity
	 */
	public Double capacity() { return this.W; }

	/**
	 * Obtain the array of items.
	 */
	public Item[] items() { return this.items; }

	/**
	 * Printable representation of the problem instance.
	 */
	public String toString() {
		StringBuffer result = new StringBuffer();

		result.append("W = " + this.W + "\n");
		for(int i=0; i < this.numItems; i++) {
			result.append(this.items[i].value + ", " + this.items[i].weight + "\n");
		}
		return result.toString();
	}

}


public class Knapsack {


	public static KnapsackInstance readKnapsackInstance(String filename) {

		Scanner infile = null;
		try {
			infile = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			System.out.println("Error: " + filename + " could not be opened.");
		}

		// Try to read the knapsack capacity and the number of items.
		if (!infile.hasNextDouble())
			throw new RuntimeException("Error: expected knapsack weight as a real number");
		Double W = infile.nextDouble();

		if (!infile.hasNextInt())
			throw new RuntimeException("Error: expected integer number of items.");
		Integer numItems = infile.nextInt();

		// Create a knapsack instance for the given number of items.
		KnapsackInstance K = new KnapsackInstance(numItems, W);

		// Read each value-weight pair.
		for(int i=0; i < numItems; i++) {
			if(!infile.hasNextDouble())
				throw new RuntimeException("Error: expected a value while reading item " + i +".");
			Double v = infile.nextDouble();
			if(!infile.hasNextDouble())
				throw new RuntimeException("Error: expected a weight while reading item " + i +".");
			Double w = infile.nextDouble();

			// Store the value-weight pair in the problem instance.
			K.setItem(v,w,i);
		}

		infile.close();

		return K;
	}

	// TODO Write your backtracking and greedy solutions to Knapsack here.


	// ************************* Question 2 BackTracking Algorithm ************************************
	/**
	 * the best Value that final get
	 */
	public static double bestValue;


	/**
	 * the best wight that final get
	 */
	public static double bestWeight;


	/**
	 * the sequence array that use to store the items
	 */
	public static ArrayList<Item> sequence;


	/**
	 * the temp wight that will help to get the final best wight
	 */
	public static double weight;


	/**
	 * the temp value that will hep to get the final best value
	 */
	public static double value;


	/**
	 * to calculate the path
	 */
	public static int extensionsTried;

	/**
	 * to final the sub value after the i-th item in the K
	 * @param K the KnapsackInstance that will use to do the backtrack
	 * @param i the position of the K's items
	 * @return the sub value of the i-th item in the K
	 */
	public static double getSubValue(KnapsackInstance K,int i)
	{
		double sub = 0;
		for(int j = i; j<K.numItems(); j++)
		{
			sub += sequence.get(j).value();
		}
		return sub;
	}

	/**
	 * the helper method to do the backtrack algorithm
	 * @param K the KnapsackInstance that will use to do the backtrack
	 * @param i the start position of the K's items
	 */
	public static void backtrackingHelper(KnapsackInstance K,int i)
	{
		// if i has already out of the K, set up the bestValue and bestWeight, and terminate
		if(i>K.numItems()-1)
		{
			bestValue = value;
			bestWeight = weight;
			return;
		}

		// if the sequence of the i's weight plus the current wight is smaller than the capacity weight of the K
		// weight will be added before, value will be added by before
		// then do the recursion for the next position of i, and go back the weight and value's index
		if(sequence.get(i).weight()+weight<=K.capacity())
		{
			weight += sequence.get(i).weight();
			value += sequence.get(i).value();
			extensionsTried++;
			backtrackingHelper(K,i+1);
			weight -= sequence.get(i).weight();
			value -= sequence.get(i).value();
		}
		// if there then value plus the sub value larger than the best value, do the recursion
		if(value+getSubValue(K, i+1)>bestValue)
		{
			extensionsTried++;
			backtrackingHelper(K,i+1);
		}
	}


	/**
	 * The main method of the backtracking algorithm
	 * to do the initialization for the every static method
	 * @param K the KnapsackInstance to do the backtracking algorithm
	 */
	public static void backtracking(KnapsackInstance K)
	{


		bestValue = 0;
		bestWeight = 0;
		weight = 0;
		value = 0;
		sequence = new ArrayList<>();
		extensionsTried=0;
		// using the for-loops to add the items to the sequence array list
		for(int i = 0; i<K.numItems(); i++)
		{
			sequence.add(i,new Item(K.value(i),K.weight(i)));
		}
		// call the helper method
		backtrackingHelper(K,0);
	}


	// ************************* Question 3 Greedy Algorithm ************************************
	/**
	 * the total weight of the greedy algorithm
	 */
	public static double greedyWeight;

	/**
	 * the total value of the greedy algorithm
	 */
	public static double greedyValue;

	/**
	 * The arrayList for the all item sequence
	 */
	public static ArrayList<Item> greedySequence;


	/**
	 * the visited array to store all the visited items
	 */
	public static Item visited[];

	/**
	 * the count integer to make the loop completed
	 */
	public static int count;

	/**
	 * The method to make the greedy algorithm
	 * @param K the KnapsackInstance that we use to do the greedy algorithm
	 * @param n the start point that positioned of the K
	 */
	public static void greedy(KnapsackInstance K, int n)
	{
		// to do the initialization for all the static variables
		greedyWeight = 0;
		greedyValue = 0;
		count = 0;
		extensionsTried = 0;
		visited = new Item[K.numItems()];
		greedySequence = new ArrayList<>();

		// create a Item variable to do the changing and swapping
		Item items;

		// using the for-loops to store all the items in the K to greedySequence arrayList
		for(int i = 0; i< K.numItems(); i++)
		{
			greedySequence.add(new Item(K.value(i),K.weight(i)));
		}

		// this while loop greedy go start
		while(n<K.numItems())
		{
			// set up the Item variable
			items = new Item(0.0,0.0);

			// this for-loops going to get the largest value of the item
			for(int i = 0; i<greedySequence.size(); i++)
			{
				// if it is not the largest, save to the item variable
				if(items.value()<greedySequence.get(i).value())
				{
					items = new Item(greedySequence.get(i).value(), greedySequence.get(i).weight());
				}
			}

			// this for-loops going to find the item that has already tested
			for(int i = 0; i<greedySequence.size(); i++)
			{
				// if it does be tested, remove it
				if(items.value()==greedySequence.get(i).value()&& items.weight() == greedySequence.get(i).weight())
				{
					greedySequence.remove(i);
				}
			}

			// set up the greedyValue and greedyWight index
			greedyValue += items.value();
			greedyWeight += items.weight();

			// save the item to the visited array
			visited[count] = new Item(items.value(),items.weight());

			// if the weight is not exceed to the capacity, the count added
			if(greedyWeight <K.capacity())
			{
				count++;
			}
			// otherwise, delete the last item's weight and value
			else
			{
				greedyWeight -= items.weight();
				greedyValue -= items.value();
			}
			extensionsTried++;
			n++;

		}


	}



	public static void main(String args[]) {
		KnapsackInstance K = readKnapsackInstance("Knapsack-Template/knapsack-basictest.dat");

		// this line is mostly just to prevent a warning that K is unused.  You can
		// delete it when you're ready.  It has the added bonus of letting you see
		// the problem instance.
		System.out.println(K);
		// Test and time your radix sort.
		// TODO Call your algorithms to solve the knapsack instance K here.


		// Testing the backtracking algorithm
		System.out.println("************** Starting test Backtracking Algorithm **************");
		backtracking(K);
		System.out.println("(BACKTRACK TEST) The best value is: "+bestValue);
		System.out.println("(BACKTRACK TEST) The best weight is: "+bestWeight);
		// Testing the greedy algorithm
		System.out.println("************** Starting test Greedy Algorithm **************");
		greedy(K,0);
		System.out.println("(GREEDY TEST) The best value is: "+greedyValue);
		System.out.println("(GREEDY TEST) The best weight is: "+ greedyWeight);
		// Record the ratio V(greedy)/V(backtrack)
		System.out.println("************** Starting counting the ratio  **************");
		System.out.println("The ratio of V(greedy)/V(backtrack) is " + (greedyValue/bestValue)*100 +"%");


	}
}
