package numericalLibrary.optimization.lossFunctions;


import numericalLibrary.optimization.ModelFunction;
import numericalLibrary.types.Matrix;



/**
 * Represents a {@link DifferentiableLoss} that behaves quadratically around a specific point.
 * These type of losses usually have the form:
 * L(\theta) = \sum_i || f( x_i , \theta ) ||^2
 * where:
 * - f is a {@link ModelFunction},
 * - { x_i }_i is the set of input points to the {@link ModelFunction},
 * - \theta is the parameter vector, represented as a row {@link Matrix}.
 * In such case, the Hessian of L(\theta) can be approximated using the jacobian of f, J, as J^T * J.
 */
public interface LocallyQuadraticLoss
    extends DifferentiableLoss
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Returns the approximate Hessian of the {@link LocallyQuadraticLoss}.
     * <p>
     * The approximate Hessian matrix is evaluated at the point defined by the parameters set by {@link #setParameters(Matrix)}.
     * 
     * @return  approximate Hessian matrix of the {@link LocallyQuadraticLoss}.
     */
    public abstract Matrix getGaussNewtonMatrix();
    
}
