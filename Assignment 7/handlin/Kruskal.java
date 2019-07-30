// Name: Yinsheng Dong
// Student Number: 11148648
// NSID: yid164
// Lecture Section: CMPT 280


import lib280.graph.*;

import java.util.Collections;
import java.util.LinkedList;

public class Kruskal {

	/**
	 *
	 * @param G A weighted, undirected graph
	 * @return minST
	 */
	public static WeightedGraphAdjListRep280<Vertex280> minSpanningTree(WeightedGraphAdjListRep280<Vertex280> G) {

		// TODO -- Complete this method.
		// create minST that is an undirected, weighted graph with the same node set as G, but no edges
		WeightedGraphAdjListRep280<Vertex280> minST = new WeightedGraphAdjListRep280<>(G.capacity(),false);
		// The LinkedList for sorting the edges
		LinkedList<WeightedEdge280> minSTList = new LinkedList<>();
		// a union-find data structure with the node set of G in which each node is initially in its own subset
		UnionFind280 UF = new UnionFind280(G.capacity());

		// add the same node as G to minST by using for-loops
		for (int i = 1; i <= G.capacity();i++)
		{
			minST.addVertex(i);
		}

		// adding the edges to the minSTList by using this loops
		for (int i = 1; i <= G.capacity();i++)
		{
			G.eGoFirst(G.vertex(i));
			while (G.eItemExists())
			{
				minSTList.add(G.eItem());
				G.eGoForth();
			}

		}
		// sorting the minSTList from smallest to largest weight
		Collections.sort(minSTList);

		// for each edge e=(a,b) in sorted order
		for(WeightedEdge280 e : minSTList)
		{
			// set av be the e's first item
			Vertex280 av = (Vertex280)e.firstItem();
			// set bv be the e's second item
			Vertex280 bv = (Vertex280)e.secondItem();

			// for each e=(a,b), a should be the index of av, b should be the index of bv
			int a = av.index();
			int b = bv.index();

			if(UF.find(a)!=UF.find(b))
			{
				minST.addEdge(a,b);
				minST.setEdgeWeight(a,b,G.getEdgeWeight(a,b));
				UF.union(a,b);
			}

		}

		return minST;
	}
	
	
	public static void main(String args[]) {
		WeightedGraphAdjListRep280<Vertex280> G = new WeightedGraphAdjListRep280<Vertex280>(1, false);
		// If you get a file not found error here and you're using eclipse just remove the 
		// 'Kruskal-template/' part from the path string.
		G.initGraphFromFile("Kruskal-template/mst.graph");
		System.out.println(G);

		WeightedGraphAdjListRep280<Vertex280> minST = minSpanningTree(G);

		System.out.println(minST);
	}
}


