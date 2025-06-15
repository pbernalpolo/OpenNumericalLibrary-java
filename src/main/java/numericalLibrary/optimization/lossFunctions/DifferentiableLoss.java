package numericalLibrary.optimization.lossFunctions;



/**
 * Represents a {@link Loss} whose gradient can be computed.
 */
public interface DifferentiableLoss
    extends Loss
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
	/**
     * Returns the {@link DifferentiableLossResults} obtained from this {@link DifferentiableLoss}.
     * <p>
     * The loss function is evaluated at the point defined by the internally stored parameter vector.
     * 
     * @return  {@link DifferentiableLossResults} obtained from this {@link DifferentiableLoss}.
     */
    public abstract DifferentiableLossResults getDifferentiableLossResults();
    
}
