import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
// Name: Yinsheng Dong
// Student Number: 11148648
// NSID: yid164
// Lecture Section: CMPT 280
public class RadixSortMSD {

	// *************************************************************
	// TODO Write YOUR RADIX SORT HERE
	// *************************************************************

	// radix of ASCII size
	private static final int R = 100;

	/**
	 * Sort the Array key by msd radix sort
	 * @param keys is the input array of string
	 */
	public static void MsdRadixSort(String[] keys)
	{
		// find the length of the keys
		int l = keys.length;
		// create the list that size is same as the keys
		String[] list = new String[l];
		// call the sortByDigits function
		sortByDigits(keys, 0, l-1, 0, list);
	}

	/**
	 * The helper function to find the position character of string
	 * @param s the string that have
	 * @param d the position
	 * @return the dth character of s
	 */
	private static int charAt(String s, int d)
	{
		assert  d >= 0 && d<= s.length();
		if(d==s.length())
		{
			return -1;
		}
		else
		{
			return s.charAt(d);
		}
	}

	/**
	 *
	 * @param keys keys to be sorted
	 * @param start the Initial digit of the key
	 * @param end the end digit of the key
	 * @param i digit on which to partition -- i = 0 is the left-most digit
	 * @param list the new list for each digit
	 */
	private static void sortByDigits(String[]keys, int start, int end, int i, String[] list)
	{
		// end digit can not same as start digit
		if(end<=start)
		{
			return;
		}

		// to compute frequency counts
		int count[] = new int[R+2];

		// for k from start to end, compute frequency counts
		for(int k = start; k<= end; k++)
		{
			int c = charAt(keys[k],i);
			count[c+2]++;
		}

		// go transform counts to indicaies
		for(int k = 0; k<R+1; k++)
		{
			count[k+1] += count[k];
		}

		// make the distribution if the i-th digit of the key has value k
		for(int k = start; k<=end; k++)
		{
			int c = charAt(keys[k],i);
			list[count[c+1]++] = keys[k];
		}

		// copy back the key to list
		for(int k = start; k<=end; k++)
		{
			keys[k] = list[k-start];
		}

		// if there is another digit to consider, recursively sort for each character
		for(int k = 0; k<R; k++)
		{
			sortByDigits(keys,start+count[k],start+count[k+1]-1, i+1, list);
		}
	}

	
	public static void main(String args[]) {
		
		// *************************************************************
		// Change the input file by changing the line below.
		// *************************************************************
		String inputFile = "RadixSortMSD-Template/words-basictest.txt";
		
		// Initialize a scanner to read the input file.
		Scanner S = null;
		try {
			S = new Scanner(new File(inputFile));  
		} catch (FileNotFoundException e) {
			System.out.println("Error: " + inputFile + "was not found.");
			return;
		}
		
		// Read the first line of the file and convert it to an integer to see how many
		// words are in the file.
		int numWords = Integer.valueOf(S.nextLine());
		
		// Initialize an array large enough to store numWords words.
		String items[] = new String[numWords];
		
		// Read each word from the input file and store it in the next free element of 
		// the items array.
		int j=0;
		while(S.hasNextLine()) {
			items[j++] = S.nextLine().toUpperCase();
		}
		S.close();
		System.out.println("Done reading " + numWords + " words.");
		

		// Test and time your radix sort.
		long startTime = System.nanoTime();
		
		// *************************************************************
		// TODO CALL YOUR RADIX SORT TO SORT THE 'items' ARRAY HERE
		// *************************************************************
		MsdRadixSort(items);

		long stopTime = System.nanoTime();
		for(String s : items)
		{
			System.out.println(s);
		}
		
		// Uncomment this section if you want the sorted list to be printed to the console.
		// (Good idea for testing with words-basictest.txt; leave it commented out though
		// for testing files with more than 50 words).
		/*
		for(int i=0; i < items.length; i++) {
			System.out.println(items[i]);
		}
		*/
		
		// Print out how long the sort took in milliseconds.
		System.out.println("Sorted " + items.length + " strings in " + (stopTime-startTime)/1000000.0 + "ms");
	
	}
	
}
