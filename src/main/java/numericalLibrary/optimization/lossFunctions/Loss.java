package numericalLibrary.optimization.lossFunctions;


import numericalLibrary.optimization.Shiftable;



/**
 * Represents a loss function.
 * <p>
 * Each loss is responsible for storing and managing its parameter vector,
 * and it is {@link Shiftable} in the sense of its parameter vector.
 * <p>
 * It doesn't make sense to separate the parameter vector from the {@link Loss}
 * because each loss and its derivatives will depend on the particular parameterization
 * chosen for the parameter vector.
 */
public interface Loss
	extends Shiftable
{
    ////////////////////////////////////////////////////////////////
    /// PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
	/**
     * Returns the {@link LossResults} obtained from this {@link Loss}.
     * <p>
     * The loss function is evaluated at the point defined by the internally stored parameter vector.
     * 
     * @return  {@link LossResults} obtained from this {@link Loss}.
     */
    public abstract LossResults getLossResults();
    
}
