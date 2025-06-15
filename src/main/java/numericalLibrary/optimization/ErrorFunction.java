package numericalLibrary.optimization;


import numericalLibrary.optimization.lossFunctions.DifferentiableLoss;
import numericalLibrary.optimization.lossFunctions.LocallyQuadraticLoss;
import numericalLibrary.types.MatrixReal;



/**
 * Represents a vector function used to define a {@link LocallyQuadraticLoss}.
 * <p>
 * {@link ErrorFunction}s take:
 * <ul>
 * 	<li> an input of type <T>,
 *  <li> an internally stored parameter vector,
 * </ul>
 * and produces an output in the form of a column vector.
 * They also provide their Jacobian matrix evaluated at the current input, and the current internally stored parameter vector.
 * <p>
 * Note that a {@link ErrorFunction} is different from a {@link DifferentiableLoss} in that the output of a {@link DifferentiableLoss} is scalar,
 * while the output of an {@link ErrorFunction} can be multidimensional.
 * Also, a {@link DifferentiableLoss} gives a gradient, while an {@link ErrorFunction} gives a Jacobian.
 * 
 * @param <T>   type of inputs to this {@link ErrorFunction}.
 * 
 * @see LocallyQuadraticLoss
 */
public interface ErrorFunction<T>
	extends Shiftable
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Sets the input to the {@link ErrorFunction}.
     * 
     * @param x     input of the {@link ErrorFunction}.
     */
    public abstract void setInput( T x );
    
    
    /**
     * Returns the output of the {@link ErrorFunction} as a column {@link MatrixReal}.
     * <p>
     * The function is evaluated at the point defined by the internally stored parameter vector, and the inputs set by {@link #setInput(Object)}.
     * 
     * @return  output of the {@link ErrorFunction} as a column {@link MatrixReal}.
     */
    public abstract MatrixReal getError();
    
    
    /**
     * Returns the Jacobian of the {@link ErrorFunction}.
     * <p>
     * The Jacobian is evaluated at the point defined by the internally stored parameter vector, and the inputs set by {@link #setInput(Object)}.
     * 
     * @return  Jacobian of the {@link ErrorFunction}.
     */
    public abstract MatrixReal getJacobian();
    
}
