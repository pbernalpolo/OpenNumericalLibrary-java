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
	 * Returns the {@link LocallyQuadraticLossResults} obtained from this {@link LocallyQuadraticLoss}.
	 * <p>
     * The loss function is evaluated at the point defined by the internally stored parameter vector.
	 * 
	 * @return	{@link LocallyQuadraticLossResults} obtained from this {@link LocallyQuadraticLoss}.
	 */
    public abstract LocallyQuadraticLossResults getLocallyQuadraticLossResults();
    
}
