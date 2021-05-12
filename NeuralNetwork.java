import BasicIO.*;                 // for IO classes 
import java.util.*;               // for Dates 
import static BasicIO.Formats.*;  // for field formats 
import static java.lang.Math.*;   // for math constants and functions
import java.util.Arrays; 
import java.text.DecimalFormat; 
import java.io.File; 
import java.io.IOException; 
 
 /** This class is a program to test a network implementation for a number of training examples
  * 
  * @author Annauth Muditaa
  * @version 1.0 (December 2020)                                                        */ 

public class NeuralNetwork {
  
  private LinkedList<double [ ]> list1; 
  private LinkedList<double [ ]> list2; 
  private LinkedList<double [ ]> list3; 
  private int count1;                      // to keep track of the length of an input pattern at every line (in file read)
  private int count2;                      // to keep track of the length of an output pattern at every line (in file read)
  private double [ ] arr1;                 // to store an input pattern at every line (in file read)
  private double [ ] arr2;                 // to store an output pattern at every line (in file read)
  private int input;                       // number of input nodes 
  private int hidden;                      // number of hidden nodes
  private int output;                      // number of ouput nodes
  private double [ ] summation_l1;        // summation array of layer 1
  private double [ ] activation_l1;       // activation array of layer 1
  private double [ ] summation_l2;        // summation array of layer 2
  private double [ ] activation_l2;       // activation array of layer 2
  private double [ ] l1_DO;               // DO function array of layer 1
  private double [ ] l1_DI;               // DI function array of layer 1
  private double [ ] l2_DO;               // DO function array of layer 2
  private double [ ] l2_DI;               // DI function array of layer 2
  private long s,e;
  private double total_sum;
  private double mean_squared_error;
  private double [ ] [ ] weights_IH;
  private double [ ] [ ] weights_HO;
  private Layer_IHO layer;
  private ArrayList<double [ ] [ ]> weights_layer1;
  private ArrayList<double [ ] [ ]> weights_layer2;
  private ArrayList<Long> list_time;
     
    /** This constructor reads the scores for each game and does the calculation for the added fields and
      * the summary details                                                             */ 
     
    public NeuralNetwork ( int a, String name ) {
      
      double rate;                    // learning rate
      String filename;                // name of file 
      int iterations;                 // to keep count of the number of iterations
      long duration; 
      
      total_sum=0; 
      mean_squared_error=0; 
      
      input = a; 
      output = 1; 
      filename = name; 
      
      Scanner scan = new Scanner(System.in); 
      list_time = new ArrayList<>(); 
      
      weights_layer1 = new ArrayList<double [ ] [ ]>();
      weights_layer2 = new ArrayList<double [ ] [ ]>();
      
      hidden = 16;  
      
      layer = new Layer_IHO(); 
      
      list1 = new LinkedList<>();          // list for 1d arrays of input patterns(one array for each training example) 
      list2 = new LinkedList<>();          // list for 1d arrays of actual output patterns(one array for each training example) 
      list3 = new LinkedList<>();          // list for 1d arrays of network output patterns(one array for each training example)
      
      int count1;          // to keep track of the length of an input pattern at every line (in file read)
      int count2;          // to keep track of the length of an output pattern at every line (in file read)
      double [ ] arr1;     // to store an input pattern at every line (in file read)
      double [ ] arr2;     // to store an output pattern at every line (in file read)
      
      rate = 0.01; 
      
      while ( rate <= 1.0 ) { 
        run(10000,name,rate,0); 
        rate = rate + 0.01; 
        duration = e-s; 
        list_time.add(duration); 
        total_sum=0; 
        mean_squared_error=0;  
        // System.out.println(rate); 
        
      } 
      
      double min = 100000.0; 
      int num = 0; 
      
      for ( int f=0 ; f<list_time.size() ; f++ ) { 
        
        if ( list_time.get(f) < min ) {
          min = list_time.get(f); 
          num = f+1; 
          
        }
        
      };
      
      rate = 0.01*num; 
      
      total_sum=0; 
      mean_squared_error=0; 
      
      list1 = new LinkedList<>();          // list for 1d arrays of input patterns(one array for each training example) 
      list2 = new LinkedList<>();          // list for 1d arrays of actual output patterns(one array for each training example) 
      list3 = new LinkedList<>();          // list for 1d arrays of network output patterns(one array for each training example)
      
      run(1,"data1.txt",rate,num-1); 
      
         
    };  // constructor
    
    private void run ( int iterations, String filename, double rate, int count ) {
      
      /** The file from which the data is being taken from is read. The data are stored as such: Each line contains 
        * one training example with one input pattern and its output. There are 16 lines corresponding to 16 training examples */
      
      try {
        Scanner read = new Scanner(new File(filename)); 
      
        while ( read.hasNextInt() == true ) {
          count1 = 0;                    // to keep track of the length of an input pattern at every line            
          count2 = 0;                    // to keep track of the length of an output pattern at every line
          arr1 = new double[input];      // to store an input pattern at every line
          arr2 = new double[output];     // to store an output pattern at every line
          
          /** reading the input pattern of one training example */
          while ( count1 < input && read.hasNextInt() == true ) { 
            arr1[count1] = (double)(read.nextInt());  
            count1++; 
            
          }
          
          // adding the 1d array to the list storing the input patterns
          list1.add(arr1);  
          
          /** reading the output pattern(s) of one training example */
          while ( count2 < output && read.hasNextInt() == true ) {
            arr2[count2] = (double)(read.nextInt()); 
            count2++; 
            
          }
          
          // adding the 1d array to the list storing the output patterns
          if ( iterations != 1 ) {
            list2.add(arr2);
            
          }
        
        } 
        
      } 
      
      // error in the event the file cannot be found
      catch ( IOException e ) {
        System.out.println("Error"); 
    
      } 
      
      if ( iterations != 1 ) {
        weights_IH = initializeWeights(input,hidden);     // weights between input and hidden nodes
        weights_HO = initializeWeights(hidden,output);    // weights between hidden and output nodes
      
      }
      
      if ( iterations == 1 ) {
        weights_IH = weights_layer1.get(count);  
        weights_HO = weights_layer2.get(count); 
        
      } 
      
      s = System.nanoTime(); 
      
      for ( int y=0 ; y<iterations ; y++ ) {                        // count of the number of iterations
        for ( int z=0 ; z<list1.size() ; z++ ) {                    // count of the training examples
          summation_l1 = layer.summation_function(weights_IH,list1.get(z),hidden);  // implements the summation function at layer 1(hidden layer)
          activation_l1 = layer.activation_function(summation_l1,hidden);           // implements the activation function at layer 1(hidden layer)
          
          summation_l2 = layer.summation_function(weights_HO,activation_l1,output);  // implements the summation function at layer 2(output layer)
          activation_l2 = layer.activation_function(summation_l2,output);            // implements the activation function at layer 2(output layer)
          
          if ( iterations != 1 ) {
            l2_DO = layer.error_derivativeOutput1(activation_l2,list2.get(z),output);  // implements the DO function at layer2(output layer)
            l2_DI = layer.error_derivativeInput(l2_DO,summation_l2,output);            // implements the DI function at layer 2(output layer)
          
            l1_DO = layer.error_derivativeOutput2(l2_DI,weights_HO,hidden);            // implements the DO function at layer 1(hidden layer)
            l1_DI = layer.error_derivativeInput(l1_DO,summation_l1,hidden);            // implements the DI function at layer 1(hidden layer)
          
            weights_HO = layer.weight_update(weights_HO,activation_l1,l2_DI,rate);     // updates the weights between hidden and output nodes
            weights_IH = layer.weight_update(weights_IH,list1.get(z),l1_DI,rate);     // updates the weights between input and hidden nodes
            
            total_sum = total_sum + error_sum(l2_DO);       // error_sum is ((T(i) - [A(i) of layer 2])^2)/2 from the MSE formula. The sum adds for each training example.            
          
          }
          
          // stores network output of each input pattern only at the last iteration (stored in activation array of layer 2) 
          if ( iterations == 1 ) { 
            list3.add(activation_l2); 
            
          } 
          
        };
        
        mean_squared_error = total_sum/((Math.pow(2,input))*output);          // MSE formula. (Math.pow(2,input) is the number of training examples
        total_sum = 0;                                                        // re-initilize total_sum for a new iteration
        
        if ( mean_squared_error < 0.0002 ) { 
          y = iterations; 
          
        }
        
      }; 
      
      weights_layer1.add(weights_IH); 
      weights_layer2.add(weights_HO); 
      
      e = System.nanoTime(); 
      
      if ( iterations == 1 ) { 
        System.out.println("From the neural network, the optimal path: ");
        double [ ] array = list3.get(0); 
        System.out.println(array[0]*9);
        System.out.println("");
        
      } 
      
    }; 
    
    
    /** This method initializes a 2d array for the weights found between the input and hidden nodes,
      * hidden and output nodes. The weights are stored as such: a row contains all the weights connected to either one input or output node */ 
    
    private double [ ] [ ] initializeWeights ( int x, int y ) {
      
      double [ ][ ] weights = new double [x][y]; 
      
      for ( int i=0 ; i<weights.length ; i++ ) {
        for ( int j=0 ; j<weights[i].length ; j++ ) {
          weights[i][j] = (Math.random()*1) - 0.5;    // calls for random value between 0.5 and -0.5 and adds to the array
          
        }; 
        
      }; 
      
      return weights; 
      
    }; // initializeWeights
     
    /** This method calculates the summation ((T(i) - [A(i) of layer 2])^2)/2 from the MSE formula. This is done for
      * each training example. array a represents ( T(i) - [A(i) of layer 2] ) of an input pattern  */
    
    private double error_sum ( double [ ] a ) {
      
      double sum = 0.0; 
      
      for ( int i= 0 ; i<a.length ; i++ ) {
        sum = sum + ((Math.pow(a[i],2))/2); 
        
      }; 
      
      return sum; 
        
    };  // error_sum
    
    /** This method prints the network output with a = list of 16 input patterns(one in each array), c = list of its 
      * corresponding output (actual output) and b = list of outputs of the neural network */
   
    private void print_network ( LinkedList<double [ ]> a, LinkedList<double [ ]> b, LinkedList<double [ ]> c ) {
      
      for ( int u=0 ; u<a.size() ; u++ ) {
        System.out.print(Arrays.toString(a.get(u)) + "---"); 
        System.out.print(Arrays.toString(b.get(u)) + "---"); 
        System.out.println(Arrays.toString(c.get(u)));  
        
      };        
        
    };  // print_network     
     
} // NeuralNetwork
