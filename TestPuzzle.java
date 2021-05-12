import BasicIO.*;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
  
/** This class is a program to serve as a testbed for the PathFinding class. 
  * 
  * @PathFinding 
  * 
  * @author
  * 
  * @version 1.0 ()                                                     */ 
  
public class TestPuzzle {     
     
    private Scanner input; 
    private char [ ] [ ] puzzle; 
    private char [ ][ ] maze; 
    private int width, height, depth, source_index, destination_index; 
    private ArrayList<Character> list1; 
    private ArrayList<Double> list2;
    private ArrayList<ArrayList<Character>> list3;
          
    /** The constructor obtains values from user input and performs the tests.              */ 
     
    public TestPuzzle ( ) {
       
      int choice = -1; 
      depth = 1;
      int count;
      String name;
      String name1;
      input = new Scanner (System.in);
      
      PathFinding path = new PathFinding();       // instance of PathFinding class is called to solve the puzzle
      list2 = new ArrayList<Double>();
      list3 = new ArrayList<ArrayList<Character>>();
  
      do {
        try {
          choice=menu();
          
          switch (choice) {
            
            case 1:
              
              System.out.println("What is the width of the maze?"); 
              width = input.nextInt();                                  // width from user input
              
              System.out.println("What is the height of the maze?"); 
              height = input.nextInt();                                 // height from user input
              
              input.nextLine();
              
              System.out.println("Name of file to be created that will contain the training examples (with extension: .txt)"); 
              name = input.nextLine(); 
              
              // width = 4;              
              // height = 4;            
              
              puzzle = new char[height*depth][width];   // char puzzle is created from width and (depth,height: length column of puzzle)  
              
              System.out.println(""); 
              
              for ( int g=0 ; g<400 ; g++ ) {
                list1 = new ArrayList<Character>(); 
              
              createMaze(height,width);
              
              // input will be line by line of the puzzle (iteration done for as many lines as there are: height*depth) 
              for ( int t=0 ; t<(height*depth) ; t++ ) {
                count = 0;                                // count to get characters found on every line    
                String line = retrieveLine(t);            // line is retrieved from user input
                
                while ( count < width ) {                 // count to get charAt(0) to charAt(width-1) for one line 
                  
                  puzzle[t][count] = line.charAt(count);   // add character to puzzle ( t = line index )
                  list1.add(line.charAt(count));
                  
                  if ( line.charAt(count) == 'S' ) {
                    source_index = t*width + count;   // source index is found. 't*width+count' calculates the index of S
                  
                  }
                  
                  if ( line.charAt(count) == 'E' ) {
                    destination_index = t*width + count;  // destination index is found. 't*width+count' calculates the index of E 
                  
                  }
                  
                  count++;    // increase in count until all characters of line are obtained 
                  
                } 
                
              };
              
              path.transcribe(puzzle,width,height,depth);                             // puzzle received from user input is transcribed into adjacency matrix 
              int output = path.breadth_first_search(source_index,destination_index); // matrix undergoes breadth first search to get shortest path, if present
              list2.add((double)output/(height*width));
              list3.add(list1);
              
              };
              
              try {
                File file = new File (name); 
                
                if (file.createNewFile()) {
                
                }
                
                FileWriter writer = new FileWriter (name);
                
                for ( int d=0 ; d<list3.size() ; d++ ) {
                  String q = "";
                  list1 = list3.get(d);
          
                  for ( int c=0 ; c<list1.size() ; c++ ) {
                    char v = list1.get(c);
                    
                    if ( v == 'S' ) {
                      q = q + "1";
                      q = q + " ";
                      
                    }
                    
                    else if ( v == 'O' ) {
                      q = q + "2";
                      q = q + " ";
                      
                    }
                    
                    else if ( v == 'X' ) {
                      q = q + "3";
                      q = q + " ";
                      
                    }
                    
                    else if ( v == 'E' ) {
                      q = q + "4";
                      q = q + " ";
                      
                    }
                  
                  };
                  
                  q = q + String.valueOf(list2.get(d));
                  writer.write(q);
                  writer.write(System.lineSeparator());
                
                };
                
                writer.close();
              
              }
              
              catch ( IOException e ) {
                System.out.println("Error!");
              
              }
              
              NeuralNetwork network = new NeuralNetwork(height*width,name); 
              
              break; 
    
            case 0:
              System.out.println("Goodbye!"); 
              break;
    
            default:
              System.out.println("Unrecognized Option");
    
          }
   
        }
   
        catch ( InputMismatchException i ) {
          input.next(); 
          System.out.println("Please try correct input.\n");  
        
        }
  
      } while (choice != 0);
         
    }; // constructor
    
    /** This method gives the options for which step is to be carried out next                                        */
    
    private int menu() { 
      
      System.out.println("1. Enter new puzzle "); 
      System.out.println("0. Quit"); 
      System.out.println(""); 
      return input.nextInt(); 
    
    }; // menu 
    
    /** This method keeps taking blank input until an actual String is given ( from Prof. Earl Foxwell )              */
    
    private String retrieveLine ( int c ) {
      
      String line2 = "";
      char [ ] line1 = new char [width];
      
      for ( int n=0 ; n<maze[c].length ; n++ ) {
        line1[n] = maze[c][n];
        
      };
      
      for ( int m=0 ; m<line1.length ; m++ ) {
        line2 = line2 + String.valueOf(line1[m]);
        
      };
      
      return line2; 
    
    }; // retrieveLine
    
    /** This method keeps taking blank input until an actual String is given ( from Prof. Earl Foxwell )              */
    
    private void createMaze ( int a, int b ) {
      
      int ran1;
      int ran2;
      int ran3;
      int ran4;
      
      maze = new char[a][b];
      
      Random random = new Random();
      
      ran1 = random.nextInt(a);
      ran2 = random.nextInt(b);
      ran3 = ran1;
      ran4 = ran2;
      
      maze[ran1][ran2] = 'S';
      
      while ( (ran3 == ran1) || (ran4 == ran2) ) {
        ran3 = random.nextInt(a);
        ran4 = random.nextInt(b);
        
      }
      
      maze[ran3][ran4] = 'E';
      
      for ( int t=0 ; t<maze.length ; t++ ) {
        for ( int s=0 ; s<maze[t].length ; s++ ) {
          if ( maze[t][s] != 'S' && maze[t][s] != 'E' ) {
            double z = random.nextDouble();
              
            
            if ( z < 0.6 ) {
              maze[t][s] = 'O';
              
            }
            
            else if ( z >= 0.6 ) {
              maze[t][s] = 'X';
            }
            
          }
          
        };
        
      };
    
    }; // retrieveLine
     
    public static void main ( String[] args ) { TestPuzzle t = new TestPuzzle(); }; 
     
     
} // TestPuzzle 