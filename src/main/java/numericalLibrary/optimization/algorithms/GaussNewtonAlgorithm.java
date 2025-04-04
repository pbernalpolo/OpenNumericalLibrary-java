package numericalLibrary.optimization.algorithms;


import numericalLibrary.optimization.lossFunctions.LocallyQuadraticLoss;
import numericalLibrary.types.MatrixReal;



/**
 * Implements the Gauss-Newton algorithm.
 * <p>
 * The Gauss-Newton algorithm aims to minimize the loss function:
 * <br>
 * L( \theta ) = F( \theta )
 * <br>
 * where:
 * <ul>
 *  <li> F is a {@link LocallyQuadraticLoss},
 *  <li> \theta is the parameter vector, represented as a column {@link MatrixReal}.
 * </ul>
 * <p>
 * The Gauss-Newton parameter update step takes the form:
 * <br>
 * \theta_{k+1} = \theta_k - H^{-1} g
 * <br>
 * where:
 * <ul>
 *  <li> g is the gradient of F,
 *  <li> H is the Gauss-Newton matrix of F.
 * </ul>
 * If we think about a loss function defined as:
 * <br>
 * L( \theta ) = \sum_i || f( x_i , \theta ) - y_i ||^2
 * <br>
 * then the Gauss-Newton parameter update step takes the more familiar form:
 * <br>
 * \theta_{k+1} = \theta_k - ( \sum_i  J_i^T  J_i )^{-1}  ( \sum_i  J_i^T  ( f( x_i , \theta ) - y_i ) )
 * 
 * @see <a href>https://en.wikipedia.org/wiki/Gauss%E2%80%93Newton_algorithm</a>
 */
public class GaussNewtonAlgorithm
    implements IterativeOptimizationAlgorithm<LocallyQuadraticLoss>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     * 
     * @throws IllegalStateException if a non positive-definite {@link MatrixReal} is obtained. In such a case, try using the {@link LevenbergMarquardtAlgorithm} instead.
     */
    public MatrixReal getDeltaParameters( LocallyQuadraticLoss lossFunction )
    {
        MatrixReal gaussNewtonMatrix = lossFunction.getGaussNewtonMatrix();
        MatrixReal L = null;
        try {
            L = gaussNewtonMatrix.choleskyDecompositionInplace();
        } catch( IllegalArgumentException e ) {
            throw new IllegalStateException( "Cholesky decomposition applied to non positive definite matrix. Using the Levenberg-Marquardt algorithm with a small damping factor can help." );
        }
        MatrixReal gradient = lossFunction.getGradient();
        return gradient.inverseAdditiveInplace().divideLeftByPositiveDefiniteUsingItsCholeskyDecompositionInplace( L );
    }
    
}
