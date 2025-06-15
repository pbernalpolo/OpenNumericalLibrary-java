package numericalLibrary.optimization;


import numericalLibrary.types.MatrixReal;



/**
 * Represents a mathematical object that lives in a manifold, and can be displaced using a vector of the dimension of the manifold.
 */
public interface Shiftable
{
	/**
     * Returns the degrees of freedom of the mathematical object.
     * <p>
     * As an example, if the mathematical object is a 3-dimensional vector, then this method will return 3.
     * On the other hand, if the mathematical object represents a direction using a unit 3-dimensional vector, then this method will return 2;
     * although a unit 3-dimensional vector has 3 components, it can be fully defined using 2 parameters, and we can express each component using those 2 parameters.
     * 
     * @return  degrees of freedom of the mathematical object as an int.
     */
    public abstract int degreesOfFreedom();
    
    
	/**
	 * Updates the parameters of {@code this} {@link Shiftable}.
     * <p>
     * This method defines how a vector changes the parameters of this mathematical object,
     * being the vector of the same dimension as the manifold in which the mathematical object lives:
     * <br>
     * \theta_{k+1}  =  \theta_k  (+)  \delta \theta
     * <br>
     * where
     * <ul>
     * 	<li> (+) represents the shift operation,
     * 	<li> \theta_k is the parameter vector before calling {@link #shift(MatrixReal)},
     * 	<li> \theta_{k+1} is the parameter vector after calling {@link #shift(MatrixReal)}, and
     * 	<li> \delta \theta represents the parameter vector correction.
     * </ul>
     * <p>
     * As an example, if the mathematical object is a 3-dimensional vector, then this operation can be defined as a plain addition.
     * On the other hand, if the mathematical object is a unit quaternion, then this operation could be defined mapping the vector to another unit quaternion that multiplies the first one.
     * The dimension of the input must match the degrees of freedom of the mathematical object (see {@link #degreesOfFreedomOfParameterVector()}).
     * <p>
     * This method can also be used to impose constraints on the resulting mathematical object after each shift operation.
     * For example, in the case of unit quaternions, instead of defining this operation using the quaternion product, one could define it using quaternion addition followed by a quaternion normalization.
     * 
	 * @param deltaParameters	parameter correction vector. It is a {@link MatrixReal} with a single column and same rows as degrees of freedom of the mathematical object.
	 * 
     * @see #degreesOfFreedomOfParameterVector()
	 */
	public abstract void shift( MatrixReal deltaParameters );
	
}
