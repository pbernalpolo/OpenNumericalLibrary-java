package numericalLibrary.optimization;


import numericalLibrary.types.Matrix;



/**
 * Represents a vector function whose norm squared can be minimized by an {@link IterativeOptimizationAlgorithm}.
 * <p>
 * {@link OptimizableFunction}s take an input of type <T> and produces an output that can be represented as a column vector.
 * They also provide their Jacobian matrix evaluated at the current input.
 * 
 * @param <T>   concrete type of inputs to this {@link OptimizableFunction}.
 * 
 * @see IterativeOptimizationAlgorithm
 */
public interface OptimizableFunction<T>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Sets the parameters of the {@link OptimizableFunction}.
     * 
     * @param theta     {@link Matrix} containing the parameters of the {@link OptimizableFunction}.
     */
    public void setParameters( Matrix theta );
    
    
    /**
     * Returns the current parameters of the {@link OptimizableFunction}.
     * <p>
     * Used to get the initial point in the solution space from which the {@link IterativeOptimizationAlgorithm} starts.
     * 
     * @return  current parameters of the {@link OptimizableFunction}.
     */
    public Matrix getParameters();
    
    
    /**
     * Sets the input to the {@link OptimizableFunction}.
     * 
     * @param x     input of the {@link OptimizableFunction}.
     */
    public void setInput( T x );
    
    
    /**
     * Returns the output of the {@link OptimizableFunction}.
     * <p>
     * The function is evaluated at the point defined by the parameters set by {@link #setParameters(Matrix)}, and the inputs set by {@link #setInput(Object)}.
     * 
     * @return  output of the {@link OptimizableFunction}.
     */
    public Matrix getOutput();
    
    
    /**
     * Returns the Jacobian of the {@link OptimizableFunction}.
     * <p>
     * The Jacobian is evaluated at the point defined by the parameters set by {@link #setParameters(Matrix)}, and the inputs set by {@link #setInput(Object)}.
     * 
     * @return  Jacobian of the {@link OptimizableFunction}.
     */
    public Matrix getJacobian();
    
}
