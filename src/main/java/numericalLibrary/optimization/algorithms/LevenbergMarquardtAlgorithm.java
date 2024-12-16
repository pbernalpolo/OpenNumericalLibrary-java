package numericalLibrary.optimization.algorithms;


import numericalLibrary.optimization.lossFunctions.LocallyQuadraticLoss;
import numericalLibrary.types.Matrix;



/**
 * Implements the Levenberg-Marquardt algorithm.
 * <p>
 * The Levenberg-Marquardt algorithm aims to minimize the loss function:
 * L( \theta ) = F( \theta ) + \lambda || \delta ||^2
 * <br>
 * where:
 * <br>
 * <ul>
 *  <li> F is a {@link LocallyQuadraticLoss},
 *  <li> \theta is the parameter vector, represented as a column {@link Matrix},
 *  <li> \lambda is the damping factor,
 *  <li> \delta is the increment in the parameter space.
 * </ul>
 * <p>
 * The Levenberg-Marquardt parameter update step takes the form:
 * <br>
 * \theta_{k+1} = \theta_k - ( H^{-1} + \lambda I ) g
 * <br>
 * where:
 * <ul>
 *  <li> g is the gradient of F,
 *  <li> H is the Gauss-Newton matrix of F,
 *  <li> I is the identity matrix.
 * </ul>
 * If we think about a loss function defined as:
 * <br>
 * L( \theta ) = \sum_i || f( x_i , \theta ) - y_i ||^2
 * <br>
 * then the Levenberg-Marquardt parameter update step takes the more familiar form:
 * <br>
 * \theta_{k+1} = \theta_k - ( \sum_i  J_i^T  J_i  +  \lambda I )^{-1}  ( \sum_i  J_i^T  ( f( x_i , \theta ) - y_i ) )
 * 
 * @see <a href>https://en.wikipedia.org/wiki/Levenberg%E2%80%93Marquardt_algorithm</a>
 */
public class LevenbergMarquardtAlgorithm
    extends IterativeOptimizationAlgorithm<LocallyQuadraticLoss>
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * Identity matrix multiplied by the damping factor used to increase stability.
     * 
     * @see #setDampingFactor(double)
     */
    private Matrix lambdaIdentity;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link LevenbergMarquardtAlgorithm}.
     * 
     * @param lossFunction  {@link LocallyQuadraticLoss} to be minimized.
     */
    public LevenbergMarquardtAlgorithm( LocallyQuadraticLoss lossFunction )
    {
        super( lossFunction );
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Sets the damping factor.
     * <p>
     * The damping factor controls the transition from Gauss-Newton algorithm to the gradient descent algorithm:
     * <ul>
     *  <li> A damping factor of 0 makes the algorithm be purely Gauss-Newton.
     *  <li> The bigger the damping factor, the closer to a gradient descent algorithm.
     * </ul>
     * Introducing a small damping factor usually helps solving problems when matrices are ill-conditioned (producing failures in the Cholesky decomposition).
     * 
     * @param dampingFactor     damping factor to be set.
     */
    public void setDampingFactor( double dampingFactor )
    {
        Matrix theta = this.lossFunction.getParameters();
        this.lambdaIdentity = Matrix.one( theta.rows() ).scaleInplace( dampingFactor );
    }
    
    
    /**
     * {@inheritDoc}
     * 
     * @throws IllegalStateException if a non positive-definite {@link Matrix} is obtained. In such a case, try using the {@link LevenbergMarquardtAlgorithm} instead.
     */
    public Matrix getDeltaParameters()
    {
        Matrix gaussNewtonMatrix = this.lossFunction.getGaussNewtonMatrix();
        gaussNewtonMatrix.addInplace( this.lambdaIdentity );
        Matrix L = null;
        try {
            L = gaussNewtonMatrix.choleskyDecompositionInplace();
        } catch( IllegalArgumentException e ) {
            throw new IllegalStateException( "Cholesky decomposition applied to non positive definite matrix. Setting a larger damping factor with setDampingFactor method might help." );
        }
        Matrix gradient = this.lossFunction.getGradient();
        return gradient.inverseAdditive().divideLeftByPositiveDefiniteUsingItsCholeskyDecompositionInplace( L );
    }
    
}
