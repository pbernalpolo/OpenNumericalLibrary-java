package numericalLibrary.optimization.lossFunctions;


import numericalLibrary.optimization.ErrorFunction;
import numericalLibrary.types.MatrixReal;



/**
 * Represents a {@link DifferentiableLoss} that behaves quadratically around a specific point.
 * <p>
 * An example of this type of losses has the form:
 * <br>
 * L(\theta) = \sum_i || e( x_i , \theta ) ||^2
 * <br>
 * where:
 * <ul>
 *  <li> e is an {@link ErrorFunction} to be optimized,
 *  <li> x_i is the i-th input to the {@link ErrorFunction},
 *  <li> \theta is the parameter vector, represented as a column {@link MatrixReal}.
 * </ul>
 * In such case, the Hessian of L(\theta) can be approximated using the Jacobian of e, J, as J^T * J.
 * Such approximate Hessian is called Gauss-Newton matrix.
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
     * The Gauss-Newton matrix is evaluated at the point defined by the internally stored parameter vector.
     * 
     * @return  approximate Hessian matrix of the {@link LocallyQuadraticLoss}.
     */
    public abstract MatrixReal getGaussNewtonMatrix();
    
}
