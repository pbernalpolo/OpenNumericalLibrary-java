package numericalLibrary.optimization;


import numericalLibrary.optimization.lossFunctions.DifferentiableLoss;
import numericalLibrary.optimization.lossFunctions.LocallyQuadraticLoss;
import numericalLibrary.types.Matrix;



/**
 * Represents a vector function used to define a {@link LocallyQuadraticLoss}.
 * <p>
 * {@link ModelFunction}s take an input of type <T> and produces an output in the form of a column vector.
 * They also provide their Jacobian matrix evaluated at the current input.
 * <p>
 * Note that a {@link ModelFunction} is different from a {@link DifferentiableLoss} in that the output of a {@link DifferentiableLoss} is scalar,
 * while the output of a {@link ModelFunction} can be multidimensional.
 * 
 * @param <T>   type of inputs to this {@link ModelFunction}.
 * 
 * @see LocallyQuadraticLoss
 */
public interface ModelFunction<T>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Sets the parameters of the {@link ModelFunction}.
     * 
     * @param theta     row {@link Matrix} containing the parameters of the {@link ModelFunction}.
     */
    public abstract void setParameters( Matrix theta );
    
    
    /**
     * Returns the current parameters of the {@link ModelFunction} as a row {@link Matrix}.
     * 
     * @return  current parameters of the {@link ModelFunction} as a row {@link Matrix}.
     */
    public abstract Matrix getParameters();
    
    
    /**
     * Sets the input to the {@link ModelFunction}.
     * 
     * @param x     input of the {@link ModelFunction}.
     */
    public abstract void setInput( T x );
    
    
    /**
     * Returns the output of the {@link ModelFunction} as a column {@link Matrix}.
     * <p>
     * The function is evaluated at the point defined by the parameters set by {@link #setParameters(Matrix)}, and the inputs set by {@link #setInput(Object)}.
     * 
     * @return  output of the {@link ModelFunction} as a column {@link Matrix}.
     */
    public abstract Matrix getOutput();
    
    
    /**
     * Returns the Jacobian of the {@link ModelFunction}.
     * <p>
     * The Jacobian is evaluated at the point defined by the parameters set by {@link #setParameters(Matrix)}, and the inputs set by {@link #setInput(Object)}.
     * 
     * @return  Jacobian of the {@link ModelFunction}.
     */
    public abstract Matrix getJacobian();
    
}
