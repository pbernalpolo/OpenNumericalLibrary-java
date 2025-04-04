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
 * 
 * @param <T>   type of inputs to this {@link ErrorFunction}.
 * 
 * @see LocallyQuadraticLoss
 */
public interface ErrorFunction<T>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
	/**
     * Returns the degrees of freedom of the parameter vector.
     * <p>
     * As an example, if the parameter vector represents a 3-dimensional vector, then this method will return 3.
     * On the other hand, if the parameter vector represents a direction using a unit 3-dimensional vector, then this method will return 2;
     * although a unit 3-dimensional vector has 3 components, it can be fully defined using 2 parameters, and we can express each component using those 2 parameters.
     * 
     * @return  degrees of freedom of the parameter vector as an int.
     */
    public int degreesOfFreedomOfParameterVector();
    
    
	/**
	 * Updates the parameters of {@code this} {@link ErrorFunction}.
     * <p>
     * This method defines how parameters are updated in each iteration step of an optimization algorithm:
     * \theta_{k+1}  =  \theta_k  (+)  \delta \theta
     * where (+) represents the update operation,
     * \theta_k is the parameter vector before calling {@link #shift(MatrixReal)},
     * \theta_{k+1} is the parameter vector after calling {@link #shift(MatrixReal)}, and
     * \delta \theta represents the parameter vector correction.
     * <p>
     * As an example, if the parameter vector contains the components of a 3-dimensional vector, then this operation can be defined as a plain addition.
     * On the other hand, if the parameter vector contains the components of a unit quaternion, then this operation could be defined using the quaternion product.
     * The dimension of the input must match the degrees of freedom of the parameter vector (see {@link #degreesOfFreedomOfParameterVector()}).
     * <p>
     * This method can also be used to impose constraints on the parameter vector on each update.
     * For example, in the case of unit quaternions, instead of defining this operation using the quaternion product, one could define it using quaternion addition followed by a quaternion normalization.
     * 
	 * @param deltaParameters	parameter correction vector. It is a {@link MatrixReal} with a single column and same rows as degrees of freedom of the parameter vector.
	 * 
     * @see #degreesOfFreedomOfParameterVector()
	 */
	public abstract void shift( MatrixReal deltaParameters );
	
    
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
    public abstract MatrixReal getOutput();
    
    
    /**
     * Returns the Jacobian of the {@link ErrorFunction}.
     * <p>
     * The Jacobian is evaluated at the point defined by the internally stored parameter vector, and the inputs set by {@link #setInput(Object)}.
     * 
     * @return  Jacobian of the {@link ErrorFunction}.
     */
    public abstract MatrixReal getJacobian();
    
}
