package numericalLibrary.optimization.lossFunctions;


import numericalLibrary.types.Matrix;



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
     * Returns the gradient of the {@link DifferentiableLoss} as a column matrix.
     * <p>
     * The gradient is evaluated at the point defined by the parameters set by {@link #setParameters(Matrix)}.
     * The value of the gradient must be updated before calling this method.
     * 
     * @return  gradient of the {@link DifferentiableLoss} as a column matrix.
     * 
     * @see #updateCostAndGradient()
     */
    public abstract Matrix getGradient();
    
    
    /**
     * Allocates the column {@link Matrix} that holds the gradient.
     * 
     * @see #getGradient()
     */
    public abstract void allocateGradient();
    
    
    /**
     * Updates the values returned by {@link #getCost()} and {@link #getGradient()}.
     */
    public abstract void updateCostAndGradient();
    
}
