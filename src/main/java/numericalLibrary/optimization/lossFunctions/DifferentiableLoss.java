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
     * 
     * @return  gradient of the {@link DifferentiableLoss} as a column matrix.
     */
    public abstract Matrix getGradient();
    
}
