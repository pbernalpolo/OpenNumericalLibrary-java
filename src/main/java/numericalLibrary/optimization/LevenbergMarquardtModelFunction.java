package numericalLibrary.optimization;


import numericalLibrary.types.Matrix;



/**
 * Represents a function to be used in the {@link LevenbergMarquardtAlgorithm}.
 * <p>
 * This function models the relation between {@link LevenbergMarquardtEmpiricalPair}s.
 */
public interface LevenbergMarquardtModelFunction<T>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Sets the parameters of the {@link LevenbergMarquardtModelFunction}.
     * 
     * @param theta     {@link Matrix} containing the parameters of the {@link LevenbergMarquardtModelFunction}.
     */
    public void setParameters( Matrix theta );
    
    
    /**
     * Returns the current parameters of the {@link LevenbergMarquardtModelFunction}.
     * <p>
     * Used to get the initial point in the solution space from which the iterative algorithm starts.
     * 
     * @return  current parameters of the {@link LevenbergMarquardtModelFunction}.
     */
    public Matrix getParameters();
    
    
    /**
     * Sets the input to the {@link LevenbergMarquardtModelFunction}.
     * 
     * @param x     input of the {@link LevenbergMarquardtModelFunction}.
     */
    public void setInput( T x );
    
    
    /**
     * Returns the output of the {@link LevenbergMarquardtModelFunction}.
     * <p>
     * The function is evaluated at the point defined by the parameters set by {@link #setParameters(Matrix)}, and the inputs set by {@link #setInput(Object)}.
     * 
     * @return  output of the {@link LevenbergMarquardtModelFunction}.
     */
    public double getOutput();
    
    
    /**
     * Returns the Jacobian of the {@link LevenbergMarquardtModelFunction}.
     * <p>
     * The Jacobian is evaluated at the point defined by the parameters set by {@link #setParameters(Matrix)}, and the inputs set by {@link #setInput(Object)}.
     * 
     * @return  Jacobian of the {@link LevenbergMarquardtModelFunction}.
     */
    public Matrix getJacobian();
    
}
