import java.io.*;
import java.util.LinkedList; 
import java.util.Queue;
import java.util.Collections; 
  
/** This class provides an implementation of the Fraction class as a ratio p/q 
  * of two integers p and q, q != 0). 
  * 
  * @author 
  * 
  * @version 1.0 ()                                                     */ 
  
public class PathFinding {    
     
    private static final long  serialVersionUID = 987654321L; 
     
    public int [ ] [ ] graph;                                                                     
    public int row_length, column_length, layer_size, source_index, destination_index;            
    public int [ ] [ ] directions; 
    public int [ ] predecessor, cost; 
    public boolean [ ] visited; 
     
    /** This constructor is a default constructor for the class                      */ 
     
    public PathFinding ( ) { 
         
    }; // constructor      
     
    /** This method converts a char array into a graph. There are only inbound edges for E and O.
      * If the puzzle below has a depth of 3: 
      * puzzle       index equivalent  layer_index
      * XE              01                 0
      * OO      ->      23                 1
      * SX              45                 2                                      */ 
     
    public void transcribe ( char [ ] [ ] puzzle, int n, int m, int depth ) {
      
      int index = 0;          // every position in the puzzle is assigned a number, this is the starting position: representing puzzle[0][0] 
      int layer_index;        // this represents the layer in which the index being checked is found (which depth it is on) 
      layer_size = n*m;       // this represents the number of elements found in one depth of the puzzle 
      
      row_length = n;           // representing the width of the puzzle
      column_length = m*depth;  // representing the length of the puzzle 
      
      graph = new int [layer_size*depth] [layer_size*depth];       // graph is created for all the vertices ( layer_size*depth is the number of vertices ) 
      
      for ( int i=0 ; i<puzzle.length ; i++ ) {      // iterate through the puzzle 
        for ( int j=0 ; j<puzzle[i].length ; j++ ) {
          layer_index = index / layer_size;          // formula from Prof. Earl Foxwell 
          
          if ( puzzle[i][j] == 'O' || puzzle[i][j] == 'E' ) {         // there are only inbound edges for E and O
            add_AdjacentInboundEdges(puzzle,i,j,index,layer_index);   // check for inbound edges in 4 directions of puzzle 
            
            if ( depth > 1 ) {                             // floors are checked only if puzzle has a depth of more than 1 
              check_FloorInboundEdges(puzzle,index,depth); // check for inbound edges on different floors 
            
            }
            
          }                                                           
          
          index++;       // increase in index until all vertices in puzzle have been checked 
        
        }; 
      
      }; 

    };  // transcribe     
     
    /** This method returns the denominator of the fraction. It is for 
      * interoperability and should only be used by implementation classes. 
      * 
      * @return int the numerator.                                               */ 
     
    public void add_AdjacentInboundEdges ( char [ ] [ ] puzzle, int i, int j, int k, int l ) {
      
      int n;  
      int position;
      int position_layer; 
      
      int [ ] [ ] directions = { {0,1}, {1,0}, {0,-1}, {-1,0} };  // represents the 4 directions to look at 
      
      // for puzzle[i][j], look in four directions to get any inbound edge 
      for ( int p=0 ; p<directions.length ; p++ ) { 
        int x = i + directions[p][0];  
        int y = j + directions[p][1]; 
        position = k + check1(p);                            // professor's solution
        position_layer = position/layer_size; 
        
        if ( (x >= 0  && x < column_length) && (y >= 0 && y < row_length) && (position_layer == l) ) {
          graph[position][k] = 1; 
        
        }
      
      };
         
    }; // add_AdjacentInboundEdges
    
    /** This method returns the denominator of the fraction. It is for 
      * interoperability and should only be used by implementation classes. 
      * 
      * @return int the numerator.                                               */ 
     
    public void check_FloorInboundEdges ( char [ ] [ ] puzzle, int k, int l ) {
      
      int new_index1 = k + layer_size; 
      int index1_layer = new_index1/layer_size; 
      int new_index2 = k - layer_size; 
      int index2_layer = new_index2/layer_size; 
      
      if ( index1_layer < l ) {
        graph[new_index1][k] = 1; 
        
      }
      
      if ( new_index2 >= 0 && index2_layer >= 0 ) {
        graph[new_index2][k] = 1; 
        
      }
         
    }; // check_FloorInboundEdges  
     
    /** This method gives the value required to be added to an index to get to another position in either 4 directions
      * puzzle       index equivalent 
      * XE              01                 
      * OO      ->      23                 
      * SX              45                                                      
      *
      * E.g: To get from index 3 to 5, two is added (+row_length = 2)                                    */ 
     
    public int check1 ( int h ) {
      
      // this represents the direction to the east
      if ( h == 0 ) {
        return 1; 
        
      }
      
      // this represents the direction to the south
      if ( h == 1 ) {
        return row_length;
        
      }
      
      // this represents the direction to the west
      if ( h == 2 ) {
        return -1;
        
      }
      
      // this represents the direction to the north
      if ( h == 3 ) {
        return -row_length; 
        
      }
      
      return 0;
         
    }; // check1    
     
   /** This method performs breadth first search algorithm on the graph       */    
    
    public int breadth_first_search ( int s, int d ) { 
      
      int dest_found = 0;        // integer used for iteration until destination has been found 
      int check;                 // index of vertex that will be added and removed from queue 
      int l = graph.length;      // this represents how many vertices there are in the graph
      int num;
      
      destination_index = d; 
      source_index = s;  
        
      Queue<Integer> queue = new LinkedList<Integer>(); // queue in which indexes of vertices along possible paths will be added 
      
      visited = new boolean [l];      // array to check for visited vertices 
      predecessor = new int [l];      // array to keep track of the predecessor of a vertex
      cost = new int [l];             // array to keep track of the cost needed to get to a vertex 
        
      queue.add(source_index); 
      visited[source_index] = true;   // source has been marked as visited, can no longer go through it 
        
      while ( queue.size() != 0 && dest_found == 0 ) {  // iteration occurs until there are no more possible paths or destination index has been found 
        
        check = queue.poll();        // get index at top of queue 
        
        for ( int k=0 ; k<graph.length ; k++ ) {    // go through graph and check for possible, unvisited paths from the check value (an index) 
          
          if ( graph[check][k] == 1 && visited[k] == false ) {    // k is an index that has a path from check index 
            
            predecessor[k] = check;       // predecessor of new index(k) path is now check
            cost[k] = cost[check] + 1;    // cost to get to k is adding 1 to cost of its predecessor 
            queue.add(k); 
            visited[k] = true;            // k has been added to queue, so it is now a visited index 
            
            if ( k == destination_index ) {               
              dest_found = 1;        // destination index has been found, so 'while' loop is exited
              num = getPath();       // directions and cost of path is obtained
              return num;
            
            } 
          
          }
        
        };                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      
      
      }
      
      // if destination index has not been found from the puzzle 
      if ( dest_found == 0 ) {
        return 0;
      
      }
      
      return -1;
         
    }; // breadth_first_search      
     
    // This method finds the path taken for the shortest length (directions) and its cost.
    
    private int getPath ( ) {
      
      int x = 0;  // integer used to iterate list of indexes 
      int i1;     // integer representing an index 
      int i2;     // integer representing an index 
      
      LinkedList<Integer> list = new LinkedList<Integer>();    // list of indexes travelled during optimal path
      
      LinkedList<String> list_path = new LinkedList<String>(); // list of directions taken during optimal path 
      
      list.add(destination_index);     // destination index is added to list (it is in reverse right now)   
       
      int point = destination_index; 
      
      // start by destination index in predecessor array, get its predecessor. Then go to that index and get its predecessor. 
      // This is repeated until the source index is reached 
      while ( point != source_index ) {
        point = predecessor[point]; 
        list.add(point);  
        
      }
      
      Collections.reverse(list); // The list obtained from predecessors is reversed to get the indexes of the optimal path from start to exit
      
      // Every two pairs of values are taken in the list and compared to get the direction taken
      while ( (x+1) < list.size() ) {
        i1 = list.get(x); 
        i2 = list.get(x+1); 
        
        // if path changes floor, the difference will be one layer-size (number of elements found in one depth of the puzzle) 
        if ( i2-i1 == layer_size ) { 
          list_path.add("D");  
          
        }
        
        // if path changes floor, the difference will be one -layer-size (number of elements found in one depth of the puzzle)
        else if ( i2-i1 == -layer_size ) {
          list_path.add("U"); 
          
        }
        
        // if path goes west, the index will decrease by 1
        else if ( i2-i1 == -1 ) {
          list_path.add("W"); 
          
        }
        
        // if path goes east, the index will increase by 1
        else if ( i2-i1 == 1 ) {
          list_path.add("E"); 
          
        }
        
        // if path goes north, the index will decrease by one row length (width of puzzle)
        else if ( i2-i1 == -row_length ) {
          list_path.add("N"); 
          
        }
        
        // if path goes south, the index will increase by one row length (width of puzzle)
        else if ( i2-i1 == row_length ) {
          list_path.add("S");  
          
        }
        
        x++;  // increase in x until every pair of values have been checked 
        
      }
      
      return cost[destination_index]; 
         
    }; // getPath       
     
} // PathFinding