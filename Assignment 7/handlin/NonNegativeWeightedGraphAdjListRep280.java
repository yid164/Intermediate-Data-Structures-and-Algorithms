 package lib280.graph;

// Name: Yinsheng Dong
// Student Number: 11148648
// NSID: yid164
// Lecture Section: CMPT 280

//import java.io.File;
//import java.io.IOException;
//import java.util.Scanner;

 import lib280.base.Pair280;
 import lib280.exception.InvalidArgument280Exception;

 import java.util.InputMismatchException;
 import java.util.Scanner;


 public class NonNegativeWeightedGraphAdjListRep280<V extends Vertex280> extends
         WeightedGraphAdjListRep280<V> {

     public NonNegativeWeightedGraphAdjListRep280(int cap, boolean d,
             String vertexTypeName) {
         super(cap, d, vertexTypeName);
     }

     public NonNegativeWeightedGraphAdjListRep280(int cap, boolean d) {
         super(cap, d);
     }

     /**
      * Replaces the current graph with a graph read from a data file.
      *
      * File format is a sequence of integers. The first integer is the total
      * number of nodes which will be numbered between 1 and n.
      *
      * Remaining integers are treated as ordered pairs of (source, destination)
      * indicies defining graph edges.
      *
      * @param fileName
      *            Name of the file from which to read the graph.
      * @precond The weights on the edges in the data file fileName are non negative.
      * @throws RuntimeException
      *             if the file format is incorrect, or an edge appears more than
      *             once in the input.
      */


     @Override
     public void setEdgeWeight(V v1, V v2, double weight) {
         // Overriding this method to throw an exception if a weight is negative will cause
         // super.initGraphFromFile to throw an exception when it tries to set a weight to
         // something negative.

         // Verify that the weight is non-negative
         if(weight < 0) throw new InvalidArgument280Exception("Specified weight is negative.");

         // If it is, then just set the edge weight using the superclass method.
         super.setEdgeWeight(v1, v2, weight);
     }

     @Override
     public void setEdgeWeight(int srcIdx, int dstIdx, double weight) {
         // Get the vetex objects associated with each index and pass off to the
         // version of setEdgeWEight that accepts vertex objects.
         this.setEdgeWeight(this.vertex(srcIdx), this.vertex(dstIdx), weight);
     }


     /**
      * Implementation of Dijkstra's algorithm.
      * @param startVertex Start vertex for the single-source shortest paths.
      * @return An array of size G.numVertices()+1 in which offset k contains the shortest
      *         path from startVertex to k.  Offset 0 is unused since vertex indices start
      *         at 1.
      */
     public Pair280<double[], int[]> shortestPathDijkstra(int startVertex) {
         // TODO Implement this method


         // from the algorithm pseudocode, it is v.tentativeDistance
         double tentativeDistance[] = new double[this.numVertices+1];

         // from the algorithm pseudocode, it is v.visited
         boolean visited[] = new boolean[this.numVertices+1];

         // from the algorithm pseudocode, it is v.predecessorNode
         int predecessorNode[] = new int[this.numVertices+1];

         // the count number of node that have been processed by Dijkstra's algorithm
         // it will shows the shortest path length
         double processedTime = 0;

         // Initialize the shortest path by using this for-loops from the algorithm pseudocode:
         // v.tentativeDistance = infinity
         // v.visited = false
         // v.predecessorNode = null (because can not be null, so it should be -1)
         for(int i = 0; i<= this.numVertices; i++)
         {
             tentativeDistance[i] = Double.MAX_VALUE;
             visited[i] = false;
             predecessorNode[i] = -1;
         }

         //s.tentativeDistance = 0 from the algorithm, the start vertex distance should be 0
         tentativeDistance[startVertex] = 0;

         //while the processedTime is smaller or equal to the numberVertices
         while(processedTime <= this.numVertices)
         {
             // the unvisited vertex with the smallest tentative distance
             int i = 1;
             // find the fist unvisited vertex
             while(visited[i]&& i<this.numVertices)
             {
                 i++;
             }

             int cur = i;

             // find the next shortest distance vertex from last vertex
             while(i<=this.numVertices)
             {
                 if(!visited[i]&&tentativeDistance[i]<tentativeDistance[cur])
                 {
                     cur = i;
                 }
                 i++;
             }

             // this vertex is visited when everything done
             visited[cur]= true;
             processedTime++;

             // update tentative distances for adjacent vertices if needed
             // find all index these are shortest path
             for(this.eGoFirst(this.vertexArray[cur-1]);!this.eAfter();this.eGoForth())
             {
                 int a = this.eItem.secondItem().index();
                 if(!visited[a]&&tentativeDistance[a]>this.eItem.getWeight() + tentativeDistance[cur])
                 {
                     tentativeDistance[a] = this.eItem.getWeight() + tentativeDistance[cur];
                     predecessorNode[a] = cur;
                 }
             }
         }
         return new Pair280<>(tentativeDistance, predecessorNode);

     }

     // Given a predecessors array output from this.shortestPathDijkatra, return a string
     // that represents a path from the start node to the given destination vertex 'destVertex'.
     private static String extractPath(int[] predecessors, int destVertex) {
         // TODO Implement this method

         // if the predecessors of vertex is now vertex, not reachable
         if(predecessors[destVertex]==-1)
         {
             return("Not reachable");
         }
         String r = ""+destVertex;
         int i = destVertex;
         // find all the reachable vertex
         while (predecessors[i]>0)
         {
             r = predecessors[i]+", "+r;
             i = predecessors[i];
         }
         r = "The path to "+destVertex+" is " + r;
         return r;
     }

     // Regression Test
     public static void main(String args[]) {
         NonNegativeWeightedGraphAdjListRep280<Vertex280> G = new NonNegativeWeightedGraphAdjListRep280<Vertex280>(1, false);

         if( args.length == 0)
             G.initGraphFromFile("lib280-asn7/src/lib280/graph/weightedtestgraph.gra");
         	 // If you're using Eclipse and you get an error opening the file, comment
         	 // the line above, and uncomment the line below:
         	 // G.initGraphFromFile("src/lib280/graph/weightedtestgraph.gra");
 
         else
             G.initGraphFromFile(args[0]);

         System.out.println("Enter the number of the start vertex: ");
         Scanner in = new Scanner(System.in);
         int startVertex;
         try {
             startVertex = in.nextInt();
         }
         catch(InputMismatchException e) {
             in.close();
             System.out.println("That's not an integer!");
             return;
         }

         if( startVertex < 1 || startVertex > G.numVertices() ) {
             in.close();
             System.out.println("That's not a valid vertex number for this graph.");
             return;
         }
         in.close();


         Pair280<double[], int[]> dijkstraResult = G.shortestPathDijkstra(startVertex);
         double[] finalDistances = dijkstraResult.firstItem();
         //double correctDistances[] = {-1, 0.0, 1.0, 3.0, 23.0, 7.0, 16.0, 42.0, 31.0, 36.0};
         int[] predecessors = dijkstraResult.secondItem();

         for(int i=1; i < G.numVertices() +1; i++) {
             System.out.println("The length of the shortest path from vertex " + startVertex + " to vertex " + i + " is: " + finalDistances[i]);
 //			if( correctDistances[i] != finalDistances[i] )
 //				System.out.println("Length of path from to vertex " + i + " is incorrect; should be " + correctDistances[i] + ".");
 //			else {
                 System.out.println(extractPath(predecessors, i));
 //			}
         }
     }

 }
