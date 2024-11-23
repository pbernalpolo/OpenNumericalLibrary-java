package numericalLibrary.optimization.lossFunctions;


import numericalLibrary.types.Matrix;



/**
 * Represents a {@link Loss} whose Jacobian can be computed.
 */
public interface DifferentiableLoss
    extends Loss
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Returns the Jacobian of the {@link DifferentiableLoss}.
     * <p>
     * The Jacobian is evaluated at the point defined by the parameters set by {@link #setParameters(Matrix)}.
     * 
     * @return  Jacobian of the {@link DifferentiableLoss}.
     */
    public abstract Matrix getJacobian();
    
}
