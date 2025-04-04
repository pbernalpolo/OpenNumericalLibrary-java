package numericalLibrary.optimization.lossFunctions;


import numericalLibrary.types.MatrixReal;



/**
 * Represents a loss function.
 * <p>
 * Each loss is responsible for storing and managing its parameter vector.
 */
public interface Loss
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
	/**
     * Returns the degrees of freedom of the parameter vector of {@code this} {@link Loss}.
     * <p>
     * As an example, if the parameter vector represents a 3-dimensional vector, then this method will return 3.
     * On the other hand, if the parameter vector represents a direction using a unit 3-dimensional vector, then this method will return 2;
     * although a unit 3-dimensional vector has 3 components, it can be fully defined using 2 parameters, and we can express each component using those 2 parameters.
     * 
     * @return  degrees of freedom of the parameter vector as an int.
     */
    public int degreesOfFreedomOfParameterVector();
    
    
	/**
	 * Updates the parameter vector of {@code this} {@link Loss}.
     * <p>
     * This method defines how parameters are updated in each iteration step of an optimization algorithm:
     * <br>
     * \theta_{k+1}  =  \theta_k  (+)  \delta \theta
     * <br>
     * where
     * <ul>
     * 	<li> (+) represents the update operation,
     * 	<li> \theta_k is the parameter vector before calling {@link #shift(MatrixReal)},
     * 	<li> \theta_{k+1} is the parameter vector after calling {@link #shift(MatrixReal)}, and
     * 	<li> \delta \theta represents the parameter vector correction.
     * </ul>
     * <p>
     * As an example, if the parameter vector contains the components of a 3-dimensional vector, then this operation can be defined as a plain addition.
     * On the other hand, if the parameter vector contains the components of a unit quaternion, then this operation could be defined using the quaternion product.
     * The dimension of the input must match the degrees of freedom of the parameter vector (see {@link #degreesOfFreedomOfParameterVector()}).
     * <p>
     * This method can also be used to impose constraints on the parameter vector on each update.
     * 
	 * @param deltaParameters	parameter correction vector. It is a {@link MatrixReal} with a single column and same rows as degrees of freedom of the parameter vector.
     * @see #degreesOfFreedomOfParameterVector()
	 */
	public abstract void shift( MatrixReal deltaParameters );
	
	
    /**
     * Returns the output of the {@link Loss}.
     * <p>
     * The loss function is evaluated at the point defined by the internally stored parameter vector.
     * 
     * @return  output of the {@link Loss}.
     */
    public abstract double getCost();
    
}
