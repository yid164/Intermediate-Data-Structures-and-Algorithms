package QuestPrerequisites;

// Name: Yinsheng Dong
// Student Number: 11148648
// NSID: yid164
// Lecture Section: CMPT 280

import java.io.*;
import java.util.Scanner;

import lib280.base.CursorPosition280;
import lib280.dispenser.Queue280;
import lib280.exception.Exception280;
import lib280.graph.Edge280;
import lib280.graph.GraphMatrixRep280;
import lib280.list.ArrayedList280;
import lib280.list.LinkedList280;
import lib280.tree.ArrayedHeap280;

public class QuestProgression {
	
	// File format for quest data:
	// First line: Number of quests N
	// Next N lines consist of the following items, separated by commas:
	//     quest ID, quest name, quest area, quest XP
	//     (Quest ID's must be between 1 and N, but the line for each quest IDs may appear in any order).
	// Remaining lines consist of a comma separated pair of id's i and j where i and j are quest IDs indicating
	// that quest i must be done before quest j (i.e. that (i,j) is an edge in the quest graph).
	
	/**
	 * Read the quest data from a text file and build a graph of quest prerequisites.
	 * @param filename Filename from which to read quest data.
	 * @return A graph representing quest prerequisites.  If quest with id i must be done before a quest with id j, then there is an edge in the graph from vertex i to vertex j.
	 */
	public static GraphMatrixRep280<QuestVertex, Edge280<QuestVertex>> readQuestFile(String filename) {
		Scanner infile;
		
		// Attempt to open the input filename.
		try {
			infile = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			System.out.println("Error: Unable to open" + filename);
			e.printStackTrace();
			return null;
		}
		
		// Set the delimiters for parsing to commas, and vertical whitespace.
		infile.useDelimiter("[,\\v]");

		// Read the number of quests for which there is data.
		int numQuests = infile.nextInt();
		
		// read the quest data for each quest.
		LinkedList280<Quest> questList = new LinkedList280<Quest>();
		for(int i=0; i < numQuests; i++) {
			int qId = infile.nextInt();
			String qName = infile.next();
			String qArea = infile.next();
			int qXp = infile.nextInt();		
			questList.insertLast(new Quest(qId, qName, qArea, qXp));
		}
	
		// Make a graph with the vertices we created from the quest data.
		GraphMatrixRep280<QuestVertex, Edge280<QuestVertex>> questGraph = 
				new GraphMatrixRep280<QuestVertex, Edge280<QuestVertex>> (numQuests, true, "QuestPrerequisites.QuestVertex", "lib280.graph.Edge280");
		
		// Add enough vertices for all of our quests.
		questGraph.ensureVertices(numQuests);
		
		// Store each quest in a different vertex.  The quest with id i gets stored vertex i.
		questList.goFirst();
		while(questList.itemExists()) {
			questGraph.vertex(questList.item().id()).setQuest(questList.item());
			questList.goForth();
		}
		
		// Continue reading the input file for the quest prerequisite informaion and add an edge to the graph
		// for each prerequisite.
		while(infile.hasNext()) {
			questGraph.addEdge(infile.nextInt(), infile.nextInt());
		}
				
		infile.close();
		
		return questGraph;
	}
	

	/**
	 * Test whether vertex v has incoming edges or not
	 * @param G A graph.
	 * @param v The integer identifier of a node in G (corresponds to quest ID)
	 * @return Returns true if v has no incoming edges.  False otherwise.
	 */
	public static boolean hasNoIncomingEdges(GraphMatrixRep280<QuestVertex,Edge280<QuestVertex>> G, int v) {

		// TODO Write this method
		// create a boolean is always true
		boolean hasNoIncomingEdges = true;
		// using the for-loops to check if the item in G is adjacent with identifier node v
		for(int i = 1; i<= G.numVertices(); i++)
		{
			if(G.isAdjacent(i,v))
			{
				hasNoIncomingEdges = false;
			}
		}

		return hasNoIncomingEdges;
	}
	
	
	/**
	 * Perform a topological sort of the quests in the quest prerequisite graph G, with priority given
	 * to the highest experience value among the available quests.
	 * @param G The graph on which to perform a topological sort.
	 * @return A list of quests that is the result of the topological sort, that is, the order in which the quests should be done if we always pick the available quest with the largest XP reward first.
	 */
	public static LinkedList280<Quest> questProgression(GraphMatrixRep280<QuestVertex,Edge280<QuestVertex>> G) {

		// TODO Write this method
		// Empty list that will contain the result of the topological sort
		LinkedList280<Quest> L = new LinkedList280<>();
		// heap of quest whose corresponding nodes in G have no incoming edges
		ArrayedHeap280<Quest> H = new ArrayedHeap280<Quest>(G.numVertices());
		// Go to the first item in graph G
		G.goFirst();
		// store the quest to the heap by using the while loops
		while(!G.after())
		{
			if(hasNoIncomingEdges(G,G.item().index()))
			{
				H.insert(G.item().quest());
			}
			G.goForth();
		}
		// while H is non-empty do
		while(!H.isEmpty())
		{
			// the n is the H's item
			Quest n = H.item();
			// remove the quest n from H
			H.deleteItem();
			// add quest n to the end of the list L
			L.insertLast(n);
			// Let the cursor go to the first item of G
			G.goFirst();
			// using the while loops to judge there is an edge  e from n's graph node
			while(!G.after())
			{
				//if n and G.item is adjacent remove the edge
				if(G.isAdjacent(n.id(),G.item().index()))
				{
					QuestVertex qv = new QuestVertex(n.id());
					qv.setQuest(n);
					G.eSearch(qv,G.item());
					G.deleteEItem();
					// if there are no incoming edges, insert the quest to the heap
					if(hasNoIncomingEdges(G,G.item().index()))
					{
						H.insert(G.item().quest());
					}
				}
				G.goForth();
			}
		}

		// if the graph has any edges in it them, throw the exception
		if(!hasNoIncomingEdges(G,G.capacity()))
		{
			throw new Exception280("The graph had at least one cycle!!!");
		}
		// otherwise return L (a topologically sorted order)
		else
		{
			return L;
		}

	}
	
	public static void main(String args[]) {
		// Read the quest data and construct the graph.
		
		// If you get an error reading the file here and you're using Eclipse, 
		// remove the 'QuestPrerequisites-Template/' portion of the filename.
		GraphMatrixRep280<QuestVertex,Edge280<QuestVertex>> questGraph = readQuestFile("QuestPrerequisites-Template/quests16.txt");
		
		// Perform a topological sort on the graph.
		LinkedList280<Quest> questListForMaxXp = questProgression(questGraph);
		
		// Display the quests to be completed in the order determined by the topologial sort.
		questListForMaxXp.goFirst();
		while(questListForMaxXp.itemExists()) {
			System.out.println(questListForMaxXp.item());
			questListForMaxXp.goForth();
		}
				
	}
}
