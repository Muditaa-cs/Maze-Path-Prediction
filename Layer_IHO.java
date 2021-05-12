import BasicIO.*;                // for IO classes
import static BasicIO.Formats.*; // for field formats
import static java.lang.Math.*;  // for math constants and functions
import java.util.Arrays; 

/** This class represents the functions to be carried out in a neural network.
  * 
  * @author Annauth Muditaa
  * 
  * @version 1.0 (December 2020)                                                     */

public class Layer_IHO {
  
  
    /** Default constructor to call on the functions defined in this class  */
  
    public Layer_IHO ( ) { 
        
    
    };  // constructor
        
    /** array1 represents either the input or output nodes. array2 represents the summation array created. a is the number of 
      * hidden nodes. The weights are stored as such: a row contains all the weights connected to either one input or output node */
    
    public double [ ] summation_function ( double [ ] [ ] weights, double [ ] array1, int a ) {
      
      double sum = 0.0;  
      double [ ] array2 = new double [a]; 
      
      for ( int i=0 ; i<weights[0].length ; i++ ) {
        for ( int j=0 ; j<weights.length ; j++ ) { 
          sum = sum + array1[j]*weights[j][i];          // elements of one column(for one hidden node) in the weights array are each multiplied to its corresponding node, array1[j]
          
        }; 
        
        array2[i] = sum; 
        sum = 0.0; 
        
      }; 
      
      return array2; 
        
    };  // summation_function
                                    
    
    /** This function returns the activation function array, arr1 is the summation function array  */
    
    public double [ ] activation_function ( double [ ] arr1, int a ) {
      
      double [ ] arr2 = new double [a];             
      
      for ( int i=0 ; i<arr1.length ; i++ ) {
        arr2[i] = sigmoidFormula(arr1[i]);  
        
      };
      
      return arr2; 
        
    };  // activation_function 
    
    
    /** This function returns the value of a sigmoid function with input a  */
    
    public double sigmoidFormula ( double a ) {
      
      return (1/(1+Math.exp(-a))); 
        
    };  // sigmoidFormula
    
    
    /** This function returns the value of a derivative sigmoid function with input a  */
    
    public double derivativeSigmoid ( double a ) {
      
      return sigmoidFormula(a)*(1-sigmoidFormula(a)); 
        
    };  // derivativeSigmoid
                                   
    
    /** a = DO array, b = sigmoid function array, c = DI function array. It is used at both the hidden and
      * output layers */ 
    
    public double [ ] error_derivativeInput ( double [ ] a, double [ ] b, int d ) {
      
      double [ ] c = new double [d]; 
      
      for ( int i=0 ; i<a.length ; i++ ) {
        c[i] = a[i]*derivativeSigmoid(b[i]); 
        
      }; 
      
      return c; 
        
    };  // error_derivativeInput
    
    
    /** a3 = DO function array for the output layer, b is for value of size output nodes, a1 is the sigmoid function array for layer 2
      * a2 is the actual output array */
    
    public double [ ] error_derivativeOutput1 ( double [ ] a1, double [ ] a2, int b ) {
      
      double [ ] a3 = new double[b]; 
      
      for ( int i=0 ; i<a1.length ; i++ ) {
        a3[i] = a1[i] - a2[i]; 
        
      }; 
      
      return a3; 
            
    };  // error_derivativeOutput1
      
    /** a1 = DI function array for the hidden layer, a2 = DO function array, The weights correspond to between hidden and output nodes */ 
     
    public double [ ] error_derivativeOutput2 ( double [ ] a1, double [ ] [ ] weights, int c ) {
      
      double sum = 0.0; 
      double [ ] a2 = new double [c]; 
      
      for ( int i=0 ; i<weights.length ; i++ ) {
        for ( int j=0 ; j<weights[i].length ; j++ ) {
          sum = sum + a1[j]*weights[i][j]; 
          
        };
        
        a2[i] = sum; 
        sum = 0.0; 
        
      };
      
      return a2; 
            
    };  // error_derivativeOutput2
      
    /** This function calculates the error of the weights and updates them either between the input and 
      * hidden nodes or the hidden and output nodes. c is the learning rate  */
    
   // For weights between hidden and output nodes, a = activation_l1, b = l2_DI 
   // For weights between input and hidden nodes, a = input pattern array, b = l1_DI 
    
    public double [ ] [ ] weight_update ( double [ ] [ ] weights, double [ ] a, double [ ] b, double c ) {
      
      double error; 
      
      for ( int i=0 ; i<weights.length ; i++ ) {
        for ( int j=0 ; j<weights[i].length ; j++ ) {
          error = a[i]*b[j]*c;                          // error calculation
          weights[i][j] = weights[i][j] - error;        // weight update
          
        }; 
        
      }; 
      
      return weights; 
            
    };  // weight_update    
    
}  // Layer_IHO