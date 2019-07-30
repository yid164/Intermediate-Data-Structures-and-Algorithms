// Name: Yinsheng Dong
// Student Number: 11148648
// NSID: yid164
// Lecture Section: CMPT 280

import lib280.graph.Edge280;
import lib280.graph.GraphAdjListRep280;
import lib280.graph.Vertex280;


public class UnionFind280 {
	GraphAdjListRep280<Vertex280, Edge280<Vertex280>> G;
	
	/**
	 * Create a new union-find structure.
	 * 
	 * @param numElements Number of elements (numbered 1 through numElements, inclusive) in the set.
	 * @postcond The structure is initialized such that each element is in its own subset.
	 */
	public UnionFind280(int numElements) {
		G = new GraphAdjListRep280<Vertex280, Edge280<Vertex280>>(numElements, true);
		G.ensureVertices(numElements);		
	}
	
	/**
	 * Return the representative element (equivalence class) of a given element.
	 * @param id The elements whose equivalence class we wish to find.
	 * @return The representative element (equivalence class) of the element 'id'.
	 */
	public int find(int id) {
		// TODO - Write this method
		// Follow the chain of directed edges starting from id
		Vertex280 result;
		G.goIndex(id);
		G.eGoFirst(G.item());
		while(G.eItemExists())
		{
			G.goIndex(G.eItem().secondItem().index());
			G.eGoFirst(G.item());
		}
		result = G.item();
		G.goFirst();
		G.eGoFirst(G.item());
		// Since at this point result has no outgoing edge, it must be the representaive element of the set ot which a belongs
		return result.index();

	}

	/**
	 * Merge the subsets containing two items, id1 and id2, making them, and all of the other elemnets in both sets, "equivalent".
	 * @param id1 First element.
	 * @param id2 Second element.
	 */
	public void union(int id1, int id2) {
		// TODO - Write this method.
		// if id1 and id2 are already in the same set, do nothing
		if(find(id1)==find(id2))
		{
			return;
		}
		// Otherwise, merge the sets
		else
		{
			G.addEdge(find(id1),find(id2));
		}

	}
	
	
	
}
